package com.hiczp.telegram.bot.application.updatesource


import com.hiczp.telegram.bot.application.exception.TelegramBotShuttingDownException
import com.hiczp.telegram.bot.protocol.TelegramBotApi
import com.hiczp.telegram.bot.protocol.exception.TelegramErrorResponseException
import com.hiczp.telegram.bot.protocol.model.Update
import com.hiczp.telegram.bot.protocol.query.getUpdates
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

private val logger = KotlinLogging.logger {}

/**
 * Long polling update source with configurable processing modes.
 *
 * Processing modes control how updates are processed:
 * - [ProcessingMode.SEQUENTIAL]: Updates processed one at a time. CancellationException instantly breaks the loop.
 * - [ProcessingMode.CONCURRENT_BATCH]: Each batch is processed concurrently, but producer waits for batch completion.
 * - [ProcessingMode.CONCURRENT]: Updates launched individually for concurrent processing without waiting.
 *
 * Graceful shutdown and Exception semantics:
 * - [TelegramBotShuttingDownException]: Signals framework shutdown. The batch is aborted and the offset is NOT advanced (Zero Data Loss).
 * - [CancellationException] / [Exception]: Signals business logic failure (e.g., timeout). The update is skipped and the offset is advanced to prevent infinite loops.
 *
 * This leverages Kotlin's native coroutine cancellation semantics for clean shutdown.
 *
 * @param client The Telegram bot client used to fetch updates.
 * @param allowedUpdates List of update types the bot is interested in. Defaults to all types.
 * @param processingMode The processing mode for handling updates. Defaults to [ProcessingMode.CONCURRENT].
 * @param maxPendingUpdates Maximum number of concurrent updates being processed. Only applies to concurrent modes.
 *                          Must be positive if specified. Defaults(null) to no limit.
 * @param fastFail If `true`, exceptions during polling will be propagated. If `false` (default),
 *                 errors are logged and polling continues with retry.
 * @throws IllegalArgumentException if [maxPendingUpdates] is specified and not positive.
 */
open class LongPollingTelegramUpdateSource(
    private val client: TelegramBotApi,
    private val allowedUpdates: List<String>? = null,
    private val processingMode: ProcessingMode = ProcessingMode.CONCURRENT,
    maxPendingUpdates: Int? = null,
    private val fastFail: Boolean = false,
) : TelegramUpdateSource {
    init {
        require(maxPendingUpdates == null || maxPendingUpdates > 0) { "maxPendingUpdates must be positive" }
    }

    // Independent fetch scope for cutting off network IO at any time
    private val fetchScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    // Semaphore for limiting concurrent update processing
    private val semaphore = maxPendingUpdates?.let { Semaphore(it) }

    private val isRunning = atomic(false)
    private var nextOffset = 0L
    private var latestTimeReceivedUpdate = Instant.DISTANT_PAST

    /**
     * Start fetching updates via long polling.
     *
     * The [consume] callback processes each update. If the application is shutting down,
     * [consume] should throw [TelegramBotShuttingDownException], which will instantly terminate
     * the processing flow in sequential mode, or silently cancel individual tasks in concurrent modes.
     *
     * This method can only be called once per instance.
     *
     * @param consume Suspended function to process each update.
     * @throws IllegalStateException if this method has already been called.
     */
    override suspend fun start(consume: suspend (Update) -> Unit) {
        check(isRunning.compareAndSet(expect = false, update = true)) {
            "${this::class.simpleName} can only be started once"
        }
        nextOffset = 0L

        logger.debug { "${this::class.simpleName} started (mode: $processingMode)" }
        val updatesProcessor: suspend CoroutineScope.(List<Update>) -> Unit = when (processingMode) {
            ProcessingMode.SEQUENTIAL -> { updates ->
                for (update in updates) {
                    try {
                        consume(update)
                        nextOffset = update.updateId + 1
                    } catch (_: TelegramBotShuttingDownException) {
                        // Application is shutting down. Break the loop instantly and preserve the offset
                        break
                    } catch (e: CancellationException) {
                        if (!isActive) {
                            throw e
                        }
                        // Business timeout/cancellation. Advance offset to prevent stuck bot
                        logger.warn(e) { "Update processing cancelled: $update" }
                        nextOffset = update.updateId + 1
                    } catch (e: Exception) {
                        // Normal business exception. Advance offset to skip poisonous update
                        logger.error(e) { "Update process error: $update" }
                        nextOffset = update.updateId + 1
                    }
                }
            }

            ProcessingMode.CONCURRENT_BATCH -> { updates ->
                val abortedByShutdown = atomic(false)
                supervisorScope {
                    updates.forEach { update ->
                        launch {
                            try {
                                consume(update)
                            } catch (e: TelegramBotShuttingDownException) {
                                abortedByShutdown.value = true
                                throw e
                            } catch (e: CancellationException) {
                                if (isActive) {
                                    logger.warn(e) { "Update processing cancelled: $update" }
                                }
                                // If consume throws CancellationException, launch silently cancels this child task
                                throw e
                            } catch (e: Exception) {
                                logger.error(e) { "Update process error: $update" }
                            }
                        }
                    }
                }
                if (!abortedByShutdown.value) {
                    nextOffset = updates.last().updateId + 1
                }
            }

            ProcessingMode.CONCURRENT -> { updates ->
                updates.forEach { update ->
                    semaphore?.acquire()
                    launch {
                        try {
                            consume(update)
                        } catch (e: TelegramBotShuttingDownException) {
                            throw e
                        } catch (e: CancellationException) {
                            if (isActive) {
                                logger.warn(e) { "Update processing cancelled: $update" }
                            }
                            throw e
                        } catch (e: Exception) {
                            logger.error(e) { "Update process error: $update" }
                        }
                    }.invokeOnCompletion {
                        semaphore?.release()
                    }
                }
                nextOffset = updates.last().updateId + 1
            }
        }

        supervisorScope {
            while (isRunning.value && isActive) {
                // If there are no new updates for at least a week, then the identifier of the next update will be chosen randomly instead of sequentially.
                if (Clock.System.now() - latestTimeReceivedUpdate > IDLE_RESET) {
                    nextOffset = 0L
                }

                val updates = try {
                    fetchScope.async { fetchFromNetwork() }.await()
                } catch (_: CancellationException) {
                    emptyList() // fetchScope cancelled by stop(), return empty to proceed to the loop end
                }

                if (updates.isEmpty()) continue
                latestTimeReceivedUpdate = Clock.System.now()

                // Rely on native coroutine features for extremely concise dispatch logic
                updatesProcessor(updates)
            }
        }
    }

    private suspend fun fetchFromNetwork(): List<Update> {
        return try {
            client.getUpdates(
                offset = nextOffset,
                timeout = TIMEOUT,
                allowedUpdates = allowedUpdates,
            ).getOrThrow()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            if (fastFail) throw e
            logger.warn(e) { "getUpdates failed, retrying..." }
            (e as? TelegramErrorResponseException)?.parameters?.retryAfter?.let {
                delay(it.seconds)
            }
            emptyList()
        }
    }

    /**
     * Trigger source shutdown.
     *
     * Immediately cancels the fetch scope, cutting off any pending long polling requests.
     * Called by [com.hiczp.telegram.bot.application.TelegramBotApplication.stop] before waiting for handlers.
     */
    override suspend fun stop() {
        logger.debug { "Received stop signal, cutting off fetch..." }
        isRunning.value = false
        fetchScope.cancel()
    }

    /**
     * Perform final cleanup by sending a Final ACK.
     *
     * Sends a final `getUpdates` call with the last processed offset to confirm
     * that all updates up to that point have been processed. This prevents
     * reprocessing of updates if the bot restarts.
     *
     * This method is called after all handlers have completed and runs in a
     * non-cancellable context to ensure the ACK is sent even during shutdown.
     */
    override suspend fun onFinalize() {
        if (nextOffset > 0) {
            logger.debug { "Sending Final ACK, final Offset: $nextOffset" }
            withContext(NonCancellable) {
                runCatching {
                    client.getUpdates(
                        offset = nextOffset,
                        limit = 1,
                        timeout = 0,
                        allowedUpdates = allowedUpdates,
                    )
                }
            }
        }
    }

    /**
     * Processing modes for controlling update processing strategy and delivery semantics.
     *
     * @property SEQUENTIAL Updates are processed strictly one at a time. Provides natural flow control
     * and strict ordering. Guarantees **At-Least-Once** delivery.
     * @property CONCURRENT_BATCH Updates in a batch are processed concurrently, but the fetcher waits
     * for the entire batch to complete before fetching the next one.
     * Provides batch-level backpressure and guarantees **At-Least-Once** delivery.
     * (Recommended for most use cases requiring high throughput and data safety).
     * @property CONCURRENT Updates are launched individually for fully detached concurrent processing.
     * Provides maximum throughput with no backpressure.
     * Provides **At-Most-Once** delivery (In-flight updates may be lost during abrupt shutdown).
     */
    enum class ProcessingMode {
        SEQUENTIAL,
        CONCURRENT_BATCH,
        CONCURRENT,
    }

    companion object {
        private const val TIMEOUT = 50L
        private val IDLE_RESET = 6.days
    }
}
