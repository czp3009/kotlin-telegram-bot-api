package com.hiczp.telegram.bot.application.updatesource

import com.hiczp.telegram.bot.application.exception.TelegramBotShuttingDownException
import com.hiczp.telegram.bot.protocol.model.Update
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlin.jvm.JvmInline
import kotlin.time.Duration

private val logger = KotlinLogging.logger {}

/**
 * A stateless [TelegramUpdateSource] that allows external injection of updates.
 *
 * This source is designed for distributed systems where updates are received by one component
 * (e.g., a webhook receiver) and processed by another (e.g., a worker node consuming from a message queue).
 * Unlike [LongPollingTelegramUpdateSource] which actively fetches updates from Telegram servers,
 * this source passively receives updates pushed via [push].
 *
 * ## Architecture Pattern
 *
 * ```
 * ┌─────────────────┐     ┌─────────────────┐     ┌──────────────────────────────┐
 * │  Webhook Server │────▶│  Message Queue  │────▶│  Worker Node                 │
 * │  (Bot Instance) │     │  (Kafka/Rabbit) │     │  SimpleTelegramUpdateSource  │
 * └─────────────────┘     └─────────────────┘     └──────────────────────────────┘
 * ```
 *
 * ## Lifecycle and State Management
 *
 * This source is stateless and supports multiple start/stop cycles:
 * - Before [start] is called, [push] will throw [IllegalStateException].
 * - After [start], updates are processed by the provided consumer callback.
 * - After [stop], the source can be restarted by calling [start] again.
 *
 * ## Exception Semantics
 *
 * - [TelegramBotShuttingDownException]: Propagates to caller, signals framework shutdown.
 * - [CancellationException]: Propagates to caller if coroutine is being cancelled; otherwise logged and re-thrown.
 * - Other [Throwable]: Business exceptions are logged and re-thrown to the caller.
 *
 * ## Example Usage
 *
 * ```kotlin
 * // Create the source
 * val source = SimpleTelegramUpdateSource()
 *
 * // Use with TelegramBotApplication
 * val app = TelegramBotApplication(
 *     client = client,
 *     updateSource = source,
 *     eventDispatcher = dispatcher
 * )
 * app.start()
 *
 * // Push updates from external source (e.g., message queue consumer)
 * messageQueue.consume { update ->
 *     // This processes the update synchronously
 *     source.push(update)
 * }
 *
 * // Graceful shutdown
 * app.stop()
 * ```
 *
 * Thread Safety: [push] can be called from any thread and is safe for concurrent use.
 *
 * @see TelegramUpdateSource
 * @see LongPollingTelegramUpdateSource
 */
class SimpleTelegramUpdateSource : TelegramUpdateSource {
    // Atomic reference to the consume function wrapper. null means not started or stopped.
    private val consumeWrapperRef = atomic<ConsumeWrapper?>(null)

    /**
     * Start consuming updates and pass them to the consumer callback.
     *
     * This method does not suspend - it simply stores the [consume] callback for later use by [push].
     * This source supports multiple start/stop cycles. After [stop] is called, this method
     * can be called again to restart consumption.
     *
     * @param consume Suspended function to process each update.
     * @throws IllegalStateException if called while already running.
     */
    override suspend fun start(consume: suspend (Update) -> Unit) {
        val wrapper = ConsumeWrapper(consume)
        check(consumeWrapperRef.compareAndSet(expect = null, update = wrapper)) {
            "${this::class.simpleName} is already running. Call stop() before starting again."
        }
        logger.debug { "${this::class.simpleName} started" }
    }

    /**
     * Push an update to be processed by the bot framework.
     *
     * This method processes the update synchronously by invoking the consumer callback set by [start].
     * Exceptions from the consumer are propagated to the caller.
     *
     * @param update The Telegram update to process.
     * @throws IllegalStateException if [start] has not been called or [stop] has been called.
     * @throws TelegramBotShuttingDownException if the bot is shutting down.
     * @throws CancellationException if the coroutine is being cancelled.
     * @throws Throwable if the consumer throws any other exception.
     */
    suspend fun push(update: Update) {
        val wrapper = checkNotNull(consumeWrapperRef.value) {
            "${this::class.simpleName} is not running. Call start() first."
        }
        try {
            wrapper.consume(update)
        } catch (e: TelegramBotShuttingDownException) {
            throw e
        } catch (e: CancellationException) {
            if (currentCoroutineContext().isActive) {
                logger.warn(e) { "Update processing cancelled: $update" }
            }
            throw e
        } catch (e: Throwable) {
            logger.error(e) { "Update process error: $update" }
            throw e
        }
    }

    /**
     * Trigger source shutdown.
     *
     * Clears the consume callback, allowing the source to be restarted.
     * After this method completes, the source can be restarted by calling [start] again.
     *
     * Called by [com.hiczp.telegram.bot.application.TelegramBotApplication.stop] before waiting for handlers.
     *
     * @param gracePeriod Ignored for this implementation. Shutdown is immediate.
     */
    override suspend fun stop(gracePeriod: Duration) {
        logger.debug { "Received stop signal, clearing consumer..." }
        consumeWrapperRef.value = null
        logger.debug { "${this::class.simpleName} stopped" }
    }

    /**
     * Perform final cleanup.
     *
     * This is a no-op for this source since all state is managed in memory.
     * This method is called after all handlers have completed.
     */
    override suspend fun onFinalize() {
        logger.debug { "onFinalize invoked" }
    }

    // Wrapper class to hold the consume function (required because atomicfu doesn't support nullable suspend function types)
    @JvmInline
    private value class ConsumeWrapper(val consume: suspend (Update) -> Unit)
}
