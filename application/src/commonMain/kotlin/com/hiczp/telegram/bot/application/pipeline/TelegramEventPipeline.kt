package com.hiczp.telegram.bot.application.pipeline

import com.hiczp.telegram.bot.application.dispatcher.TelegramEventDispatcher
import com.hiczp.telegram.bot.application.interceptor.TelegramEventInterceptor
import com.hiczp.telegram.bot.application.interceptor.TelegramEventProcessor
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent

/**
 * Wrapper that binds the dispatcher to the processor interface.
 *
 * This class adapts a [TelegramEventDispatcher] to the [TelegramEventProcessor]
 * interface, allowing it to serve as the final processor in the pipeline chain.
 *
 * @param dispatcher The dispatcher to wrap.
 */
class DispatcherTelegramEventProcessor(private val dispatcher: TelegramEventDispatcher) : TelegramEventProcessor {
    override suspend fun process(event: TelegramBotEvent) {
        dispatcher.dispatch(event)
    }
}

/**
 * Wrapper that binds an interceptor to the next processor in the chain.
 *
 * This class is used during pipeline construction to create a linked chain
 * of interceptors, where each interceptor can decide whether to pass control
 * to the next processor.
 *
 * @param interceptor The interceptor function to execute.
 * @param nextProcessor The next processor to call when the interceptor invokes `process`.
 */
class InterceptedTelegramEventProcessor(
    private val interceptor: TelegramEventInterceptor,
    private val nextProcessor: TelegramEventProcessor,
) : TelegramEventProcessor {
    override suspend fun process(event: TelegramBotEvent) {
        interceptor.invoke(nextProcessor, event)
    }
}

/**
 * Middleware controller that manages the "onion model" for event processing.
 *
 * The pipeline assembles interceptors into a chain where each interceptor can:
 * - Process the event before passing it to the next layer
 * - Skip calling the next layer (short-circuit)
 * - Process the event after the inner layers have completed
 *
 * Key features:
 * - Static assembly: Uses a simple for-loop to build the chain during initialization
 * - Clean stacktraces: No recursion or runtime closure wrapping
 *
 * Example usage:
 * ```kotlin
 * val interceptors = listOf<TelegramEventInterceptor> { event ->
 *     println("Before processing: ${event.updateId}")
 *     this.process(event)
 *     println("After processing: ${event.updateId}")
 * }
 *
 * val pipeline = TelegramEventPipeline(interceptors, eventDispatcher)
 * pipeline.process(event)
 * ```
 *
 * @param interceptors List of interceptors to apply, in order from outermost to innermost.
 * @param dispatcher The final dispatcher that handles event routing to business logic.
 */
class TelegramEventPipeline(
    interceptors: List<TelegramEventInterceptor>,
    private val dispatcher: TelegramEventDispatcher,
) : TelegramEventProcessor {
    private val pipeline: TelegramEventProcessor = buildPipeline(interceptors)

    /**
     * Builds the interceptor chain during initialization.
     *
     * Iterates from back to front, wrapping each interceptor around the
     * previously built processor. The final result is a single processor
     * that executes all interceptors in order.
     */
    private fun buildPipeline(interceptors: List<TelegramEventInterceptor>): TelegramEventProcessor {
        var intercepted: TelegramEventProcessor = DispatcherTelegramEventProcessor(dispatcher)
        for (interceptor in interceptors.reversed()) {
            intercepted = InterceptedTelegramEventProcessor(interceptor, intercepted)
        }
        return intercepted
    }

    /**
     * Execute the pipeline for an event.
     *
     * This starts execution from the outermost interceptor and proceeds
     * inward through each layer until reaching the dispatcher.
     */
    override suspend fun process(event: TelegramBotEvent) {
        pipeline.process(event)
    }
}
