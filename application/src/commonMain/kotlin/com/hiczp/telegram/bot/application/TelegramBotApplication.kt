package com.hiczp.telegram.bot.application

import com.hiczp.telegram.bot.application.dispatcher.TelegramEventDispatcher
import com.hiczp.telegram.bot.application.exception.TelegramBotShuttingDownException
import com.hiczp.telegram.bot.application.interceptor.TelegramEventInterceptor
import com.hiczp.telegram.bot.application.pipeline.TelegramEventPipeline
import com.hiczp.telegram.bot.application.updatesource.TelegramUpdateSource
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
 * val app = TelegramBotApplication(updateSource, interceptors, dispatcher)
 * app.start()
 * // ... later ...
 * app.stop(5.seconds)
 * ```
 *
 * @param updateSource The source of Telegram updates.
 * @param interceptors List of interceptors to apply to each event, in order from outermost to innermost.
 * @param eventDispatcher The dispatcher that handles the final event routing to business logic.
 */
class TelegramBotApplication(
    private val updateSource: TelegramUpdateSource,
    interceptors: List<TelegramEventInterceptor> = emptyList(),
    eventDispatcher: TelegramEventDispatcher,
) {
    private val pipeline = TelegramEventPipeline(interceptors, eventDispatcher)
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val mainJobDeferred = CompletableDeferred<Job>()

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

        val mainJob = appScope.launch {
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
     * This method is idempotent - calling it multiple times has no additional effect.
     *
     * Shutdown process:
     * 1. Transition state to STOPPING
     * 2. Cut off the update source immediately
     * 3. Wait within [gracePeriod] for existing handlers to complete
     * 4. Force cancel if timeout
     * 5. Call [TelegramUpdateSource.onFinalize] for final cleanup
     *
     * @param gracePeriod Maximum time to wait for graceful shutdown. Defaults to 10 seconds.
     */
    suspend fun stop(gracePeriod: Duration = 10.seconds) {
        if (!state.compareAndSet(State.RUNNING, State.STOPPING)) return

        logger.debug { "Received shutdown signal, starting graceful shutdown..." }

        // Instantly cut off the source
        updateSource.stop()

        // Wait safely in case start() is still initializing
        val mainJob = mainJobDeferred.await()
        logger.debug { "Waiting for existing tasks to complete (grace period: $gracePeriod)..." }

        // Implicit structured waiting
        val finishedCleanly = withTimeoutOrNull(gracePeriod) {
            mainJob.join()
            true
        }

        if (finishedCleanly == null) {
            logger.warn { "Grace period timeout! Force terminating remaining coroutines..." }
            mainJob.cancelAndJoin()
        } else {
            logger.debug { "All in-progress tasks completed successfully" }
        }

        // Final cleanup and ACK
        withContext(NonCancellable) {
            updateSource.onFinalize()
        }

        state.value = State.STOPPED
        appScope.cancel()
        logger.debug { "Bot exited" }
    }

    private enum class State { NEW, RUNNING, STOPPING, STOPPED }
}
