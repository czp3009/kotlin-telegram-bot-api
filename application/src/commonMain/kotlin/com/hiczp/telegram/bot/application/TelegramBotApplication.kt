package com.hiczp.telegram.bot.application

import com.hiczp.telegram.bot.application.dispatcher.TelegramEventDispatcher
import com.hiczp.telegram.bot.application.exception.TelegramBotShuttingDownException
import com.hiczp.telegram.bot.application.interceptor.TelegramEventInterceptor
import com.hiczp.telegram.bot.application.pipeline.TelegramEventPipeline
import com.hiczp.telegram.bot.application.updatesource.LongPollingTelegramUpdateSource
import com.hiczp.telegram.bot.application.updatesource.TelegramUpdateSource
import com.hiczp.telegram.bot.client.TelegramBotClient
import com.hiczp.telegram.bot.protocol.event.toTelegramBotEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private val logger = KotlinLogging.logger {}

/**
 * Telegram bot application orchestrator.
 *
 * Responsibilities:
 * - Lifecycle management (start/stop)
 * - Update consumption and processing
 * - Exception handling (log and continue)
 * - Graceful shutdown with CancellationException
 *
 * Graceful shutdown flow:
 * 1. Transition state to STOPPING → new updates rejected with [TelegramBotShuttingDownException]
 * 2. Call [TelegramUpdateSource.stop] → cuts off update source immediately
 * 3. Wait within grace period for existing handlers to complete
 * 4. Force cancel if timeout
 * 5. Call [TelegramUpdateSource.onFinalize] for final cleanup (e.g., Final ACK)
 *
 * Exception Handling & Offset Advancement Strategy:
 * - [TelegramBotShuttingDownException]: Framework shutdown. Instantly aborts processing WITHOUT advancing the offset to prevent data loss.
 * - [CancellationException]: Local business cancellation (e.g., `withTimeout`). Warns and advances the offset to prevent infinite retry loops (Stuck Bot).
 * - [Exception]: Unhandled business exception. Logs error and advances the offset to skip the poisonous update.
 *
 * The CancellationException mechanism:
 * - In sequential mode: instantly breaks the processing loop
 * - In concurrent mode (launch): silently cancels individual child tasks
 * - This leverages Kotlin's native coroutine cancellation semantics
 *
 * Example:
 * ```kotlin
 * val client = TelegramBotClient("YOUR_TOKEN")
 * val app = TelegramBotApplication(
 *     client = client,
 *     updateSource = LongPollingTelegramUpdateSource(client),
 *     interceptors = listOf(loggingInterceptor),
 *     eventDispatcher = eventDispatcher
 * )
 * app.start()
 * // ... later ...
 * app.stop(5.seconds)
 * ```
 *
 * Resource Management:
 * - You can create multiple [TelegramBotApplication] instances by reusing the same [TelegramBotClient].
 * - Because of this, [TelegramBotApplication] does NOT close the [TelegramBotClient.httpClient] when stopped.
 * - For some [io.ktor.client.engine.HttpClientEngine] implementations, you must explicitly close
 *   [TelegramBotClient.httpClient] to release resources (e.g., threads, connections).
 *
 * @param client The Telegram bot client for API calls.
 * @param updateSource The source of Telegram updates.
 * @param interceptors List of interceptors to apply to each event, in order from outermost to innermost.
 * @param eventDispatcher The dispatcher that handles the final event routing to business logic.
 * @param coroutineDispatcher The [CoroutineDispatcher] for the application scope.
 *   Defaults to `null`, which uses [Dispatchers.Default].
 */
class TelegramBotApplication(
    val client: TelegramBotClient,
    private val updateSource: TelegramUpdateSource,
    interceptors: List<TelegramEventInterceptor>,
    eventDispatcher: TelegramEventDispatcher,
    coroutineDispatcher: CoroutineDispatcher? = null,
) {
    private val applicationScope = CoroutineScope(SupervisorJob() + (coroutineDispatcher ?: Dispatchers.Default))
    private val mainJobDeferred = CompletableDeferred<Job>()
    private val shutdownJobDeferred = CompletableDeferred<Job>()
    private val pipeline = TelegramEventPipeline(
        client = client,
        applicationScope = applicationScope,
        interceptors = interceptors,
        dispatcher = eventDispatcher,
    )

    private val state = atomic(State.NEW)

    /**
     * Start the bot.
     *
     * The bot can only be started once. Subsequent calls will throw an exception.
     *
     * @throws IllegalStateException if the bot has already been started.
     */
    fun start() {
        check(state.compareAndSet(State.NEW, State.RUNNING)) {
            "Bot can only be started once"
        }

        logger.debug { "Starting Telegram Bot Application..." }

        val mainJob = applicationScope.launch {
            updateSource.start { update ->
                // Shutdown interceptor: Reject new updates when the bot is stopping.
                // Throwing a CancellationException subclass ensures it silently cancels 
                // the current processing task without causing the parent scope to crash.
                if (state.value != State.RUNNING) {
                    throw TelegramBotShuttingDownException()
                }

                // Normal dispatch
                val event = update.toTelegramBotEvent()
                pipeline.execute(event)
            }
        }
        // Publish the main job. This synchronizes state and prevents 
        // race conditions between start(), join(), and stop() methods.
        mainJobDeferred.complete(mainJob)
    }

    /**
     * Suspends until the bot's main job completes.
     *
     * This method can be used to keep the application alive until the bot stops.
     */
    suspend fun join() {
        mainJobDeferred.await().join()
    }

    /**
     * Stop the bot gracefully.
     *
     * This method is idempotent:
     * - If state is RUNNING, it starts the shutdown procedure and returns that shutdown [Job].
     * - If state is STOPPING or STOPPED, it returns a [Job] that waits for the ongoing shutdown to finish.
     * - If state is NEW, it is a no-op and returns an already completed [Job].
     *
     * Shutdown process (RUNNING only):
     * 1. Transition state to STOPPING
     * 2. Cut off the update source immediately
     * 3. Wait within [gracePeriod] for existing handlers to complete
     * 4. Force cancel if timeout
     * 5. Call [TelegramUpdateSource.onFinalize] for final cleanup
     *
     * The shutdown process is **irreversible** - once started, it will complete even if the returned
     * [Job] is cancelled. Cancelling the returned [Job] only stops waiting for the shutdown to complete,
     * not the shutdown itself.
     *
     * @param gracePeriod Maximum time to wait for the graceful shutdown. Defaults to 10 seconds.
     */
    fun stop(gracePeriod: Duration = 10.seconds): Job {
        if (state.compareAndSet(State.RUNNING, State.STOPPING)) {
            logger.debug { "Received shutdown signal, starting graceful shutdown..." }

            val shutdownJob = applicationScope.launch {
                withContext(NonCancellable) {
                    // Instantly cut off the source
                    runCatching { updateSource.stop() }.onFailure {
                        logger.error(it) { "Failed to cut off update source" }
                    }

                    // Wait safely in case start() is still initializing
                    val mainJob = mainJobDeferred.await()
                    logger.debug { "Waiting for existing tasks to complete (grace period: $gracePeriod)..." }

                    try {
                        withTimeout(gracePeriod) {
                            mainJob.join()
                        }
                        logger.debug { "All in-progress tasks completed successfully" }
                    } catch (_: TimeoutCancellationException) {
                        logger.warn { "Grace period timeout! Forcing termination..." }
                    }

                    mainJob.cancel()
                    runCatching { updateSource.onFinalize() }.onFailure {
                        logger.error(it) { "Failed to finalize update source" }
                    }
                    state.value = State.STOPPED
                    applicationScope.cancel()
                    logger.debug { "Bot exited" }
                }
            }
            shutdownJobDeferred.complete(shutdownJob)
            return shutdownJob
        }

        if (state.value == State.NEW) {
            return Job().apply { complete() }
        }
        return applicationScope.launch {
            shutdownJobDeferred.await().join()
        }
    }

    /**
     * Suspends until the bot and all its child coroutines have completely stopped.
     *
     * This method combines [stop] with an additional wait on the [applicationScope]'s job,
     * ensuring that not only the main update processing loop has terminated, but also
     * all coroutines launched within the application scope have completed.
     *
     * **Warning**: This method may never return if user code has launched coroutines
     * that do not properly respond to structured concurrency cancellation. Specifically:
     *
     * - Coroutines that catch [CancellationException] and continue running
     * - Coroutines using `withContext(NonCancellable)` for indefinite operations
     * - Coroutines blocked on non-interruptible I/O operations
     * - Infinite loops that don't check `isActive` or call `ensureActive()`
     *
     * Example of problematic code in a handler:
     * ```kotlin
     * command("bad") { ctx, _ ->
     *     launch {
     *         while (true) {
     *             // Missing isActive check - will never respond to cancellation
     *             delay(1000)
     *             println("Still running")
     *         }
     *     }
     * }
     * ```
     *
     * To ensure proper shutdown, user code should:
     * - Avoid catching [CancellationException] unless re-throwing it
     * - Use cancellable suspending functions (e.g., [delay], [withContext])
     * - Check [isActive][CoroutineScope.isActive] in loops or use [ensureActive]
     * - Only use `withContext(NonCancellable)` for short cleanup operations
     *
     * @param gracePeriod Maximum time to wait for the graceful shutdown. Defaults to 10 seconds.
     * @see stop For a non-suspending version that returns a [Job].
     */
    suspend fun stopSuspend(gracePeriod: Duration = 10.seconds) {
        stop(gracePeriod).join()
        applicationScope.coroutineContext[Job]?.join()
    }

    private enum class State { NEW, RUNNING, STOPPING, STOPPED }

    companion object {
        fun longPolling(
            botToken: String,
            eventDispatcher: TelegramEventDispatcher,
            interceptors: List<TelegramEventInterceptor> = emptyList(),
            coroutineDispatcher: CoroutineDispatcher? = null,
        ): TelegramBotApplication {
            val client = TelegramBotClient(botToken, coroutineDispatcher = coroutineDispatcher)
            return TelegramBotApplication(
                client = client,
                updateSource = LongPollingTelegramUpdateSource(client, coroutineDispatcher = coroutineDispatcher),
                interceptors = interceptors,
                eventDispatcher = eventDispatcher,
                coroutineDispatcher = coroutineDispatcher,
            )
        }
    }
}
