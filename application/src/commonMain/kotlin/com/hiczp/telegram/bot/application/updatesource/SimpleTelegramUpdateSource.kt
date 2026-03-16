package com.hiczp.telegram.bot.application.updatesource

import com.hiczp.telegram.bot.application.exception.TelegramBotShuttingDownException
import com.hiczp.telegram.bot.protocol.model.Update
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
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
 * ## Graceful Shutdown and External Coroutines
 *
 * **Important:** Calling [stop] does NOT cancel coroutines that are currently executing [push].
 * If external code calls [push] from its own coroutines (not managed by [TelegramBotApplication]),
 * those coroutines will continue running until they complete naturally.
 * Callers are responsible for managing their own coroutine lifecycle and cancellation.
 *
 * See the "Concurrent Push with Backpressure" example below for a pattern that supports graceful shutdown.
 *
 * ## Example: Basic Usage
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
 * // Push updates from external source (e.g., pubsub subscriber)
 * pubSubSubscriber.subscribe { message ->
 *     val update = parseUpdate(message)
 *     source.push(update)
 * }
 *
 * // Graceful shutdown
 * app.stop()
 * ```
 *
 * ## Example: Concurrent Push with Backpressure
 *
 * When receiving updates from pubsub concurrently, use a [Semaphore] to limit concurrency.
 * The external scope should be canceled independently to allow graceful shutdown.
 *
 * ```kotlin
 * val source = SimpleTelegramUpdateSource()
 * val app = TelegramBotApplication(client, source, dispatcher)
 * app.start()
 *
 * // External scope for consuming from pubsub
 * val consumerScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
 * val semaphore = Semaphore(permits = 10)  // Max 10 concurrent updates
 *
 * val consumerJob = consumerScope.launch {
 *     pubSubSubscriber.subscribe { message ->
 *         semaphore.withPermit {
 *             val update = parseUpdate(message)
 *             source.push(update)
 *         }
 *     }
 * }
 *
 * // Graceful shutdown: stop subscriber first, then cancel scope, then stop app
 * pubSubSubscriber.stop()  // Stop receiving new messages
 * consumerJob.join()       // Wait for in-flight messages to complete processing
 * app.stop()               // Then stop the application
 * ```
 *
 * Thread Safety: [push] can be called from any thread and is safe for concurrent use.
 * For exception semantics and message acknowledgment strategies, see [push].
 *
 * @see TelegramUpdateSource
 * @see LongPollingTelegramUpdateSource
 */
class SimpleTelegramUpdateSource : TelegramUpdateSource {
    // Atomic reference to the consume function wrapper. null means not started or stopped.
    private val consumeWrapperRef = atomic<ConsumeWrapper?>(null)

    // Atomic reference to the stop signal. start() waits on this, stop() completes it.
    private val stopSignalRef = atomic<CompletableDeferred<Unit>>(CompletableDeferred())

    /**
     * Start consuming updates and pass them to the consumer callback.
     *
     * This method suspends until [stop] is called. This source supports multiple start/stop cycles.
     * After [stop] is called, this method can be called again to restart consumption.
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

        stopSignalRef.value.await()
    }

    /**
     * Push an update to be processed by the bot framework.
     *
     * This method processes the update synchronously by invoking the consumer callback set by [start].
     * Exceptions from the consumer are propagated to the caller.
     *
     * ## Pub/Sub Acknowledgment Strategy
     *
     * Callers consuming from a message queue (e.g., Kafka, RabbitMQ, Google PubSub) must decide
     * whether to acknowledge (ack) or negatively acknowledge (nack) messages based on the exception
     * thrown by this method:
     *
     * - **Success (no exception)**: Ack. Message processed successfully.
     * - [TelegramBotShuttingDownException]: Nack/Retry. Bot is shutting down; message should be
     *   redelivered to another worker.
     * - [CancellationException]: Nack/Retry. Coroutine was cancelled; the message may have been partially
     *   processed. You must ensure idempotency/atomicity of your handlers and decide whether the message
     *   should be redelivered to another worker.
     * - [IllegalStateException]: Nack/Retry. Source not started; retry after source is ready.
     * - Other [Throwable]: Business error. You should decide whether to ack based on your actual
     *   business requirements (e.g., ack to avoid infinite retry loop for permanent errors, or nack
     *   for transient errors that may succeed on retry).
     *
     * **Example with Google PubSub:**
     *
     * ```kotlin
     * val subscriber = pubSubSubscriber.subscribe { message ->
     *     val update = parseUpdate(message)
     *     try {
     *         source.push(update)
     *         message.ack()  // Success
     *     } catch (e: TelegramBotShuttingDownException) {
     *         message.nack()  // Redeliver to another worker
     *     } catch (e: CancellationException) {
     *         message.nack()  // Redeliver later
     *     } catch (e: IllegalStateException) {
     *         message.nack()  // Source not ready, retry
     *     } catch (e: Throwable) {
     *         logger.error(e) { "Business error processing update" }
     *         message.ack()  // Ack to avoid retry loop for permanent errors
     *     }
     * }
     * ```
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
     * Clears the consume callback and signals [start] to return, allowing the source to be restarted.
     * After this method completes, the source can be restarted by calling [start] again.
     *
     * Called by [com.hiczp.telegram.bot.application.TelegramBotApplication.stop] before waiting for handlers.
     *
     * @param gracePeriod Ignored for this implementation. Shutdown is immediate.
     */
    override suspend fun stop(gracePeriod: Duration) {
        logger.debug { "Received stop signal, clearing consumer..." }
        // Capture the current signal before clearing the consumer to ensure atomicity with concurrent start()
        val oldSignal = stopSignalRef.value
        consumeWrapperRef.value = null
        oldSignal.complete(Unit)
        stopSignalRef.compareAndSet(oldSignal, CompletableDeferred())
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
