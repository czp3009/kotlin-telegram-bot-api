package com.hiczp.telegram.bot.application.updatesource

import com.hiczp.telegram.bot.application.exception.TelegramBotShuttingDownException
import com.hiczp.telegram.bot.protocol.model.Update
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

private val logger = KotlinLogging.logger {}

/**
 * Mock TelegramUpdateSource for testing purposes.
 *
 * Forwards updates from an external [Channel], making it useful for testing
 * bot logic without requiring a real Telegram connection.
 *
 * Unlike typical network-based sources, this mock source supports **infinite start/stop cycles**.
 * Calling [stop] will cleanly suspend consumption without closing the underlying [source] channel.
 * Subsequent calls to [start] will resume consuming from where it left off.
 * The consumer loop will also naturally complete if the [source] channel is closed externally.
 *
 * Example usage:
 * ```kotlin
 * val channel = Channel<Update>(Channel.UNLIMITED)
 * val mockSource = MockTelegramUpdateSource(channel)
 *
 * // Cycle 1: Start and process
 * app.start()
 * channel.send(testUpdate1)
 * // Stop the application (mockSource suspends consumption, channel remains open)
 * app.stop()
 * * // Cycle 2: Restart and process
 * app.start()
 * channel.send(testUpdate2)
 * ```
 *
 * @param source The channel to receive updates from.
 */
open class MockTelegramUpdateSource(
    private val source: Channel<Update>,
) : TelegramUpdateSource {
    private val isRunning = atomic(false)
    private val currentRunJob = atomic<Job?>(null)
    private val completionSignal = atomic<CompletableDeferred<Unit>?>(null)

    /**
     * Start consuming updates from the source channel.
     *
     * Iterates over the channel and passes each update to the [consume] callback.
     * Suspends gracefully if [stop] is called, or returns naturally if [source] is closed.
     *
     * @param consume Suspended function to process each update.
     * @throws IllegalStateException if the source is already running.
     */
    override suspend fun start(consume: suspend (Update) -> Unit) {
        check(isRunning.compareAndSet(expect = false, update = true)) {
            "${this::class.simpleName} can only be started once per cycle. Stop it before starting again"
        }

        logger.debug { "${this::class.simpleName} started" }
        val signal = CompletableDeferred<Unit>()
        completionSignal.value = signal

        try {
            coroutineScope {
                val job = launch(start = CoroutineStart.LAZY) {
                    for (update in source) {
                        try {
                            consume(update)
                        } catch (_: TelegramBotShuttingDownException) {
                            break
                        } catch (e: CancellationException) {
                            if (!isActive) {
                                throw e
                            }
                            logger.warn(e) { "Update processing cancelled: $update" }
                        } catch (e: Exception) {
                            logger.error(e) { "Update process error: $update" }
                        }
                    }
                }
                currentRunJob.value = job
                job.start()
            }
        } finally {
            currentRunJob.value = null
            isRunning.value = false
            logger.debug { "${this::class.simpleName} stopped" }
            signal.complete(Unit)
        }
    }

    /**
     * Gracefully pause consumption.
     *
     * Cancels the current consumption job, breaking the loop instantly, 
     * but **does not** close the underlying [source] channel.
     */
    override suspend fun stop() {
        if (isRunning.value) {
            logger.debug { "Received stop command, cancelling current run job..." }
            currentRunJob.value?.cancel()
            completionSignal.value?.await()
        }
    }

    override suspend fun onFinalize() {
        // No final ACK needed for mock
    }
}
