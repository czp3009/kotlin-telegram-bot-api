package com.hiczp.telegram.bot.application.pipeline

import com.hiczp.telegram.bot.application.dispatcher.TelegramEventDispatcher
import com.hiczp.telegram.bot.application.interceptor.TelegramEventInterceptor
import com.hiczp.telegram.bot.application.interceptor.TelegramEventProcessor
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent

private class DispatcherTelegramEventProcessor(
    private val dispatcher: TelegramEventDispatcher
) : TelegramEventProcessor {
    override suspend fun process(event: TelegramBotEvent) {
        dispatcher.dispatch(event)
    }
}

private class InterceptedTelegramEventProcessor(
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
 * pipeline(event)
 * ```
 *
 * @param interceptors List of interceptors to apply, in order from outermost to innermost.
 * @param dispatcher The final dispatcher that handles event routing to business logic.
 */
internal class TelegramEventPipeline(
    interceptors: List<TelegramEventInterceptor>,
    dispatcher: TelegramEventDispatcher,
) {
    private val pipeline = interceptors.foldRight<TelegramEventInterceptor, TelegramEventProcessor>(
        DispatcherTelegramEventProcessor(dispatcher)
    ) { interceptor, nextProcessor ->
        InterceptedTelegramEventProcessor(interceptor, nextProcessor)
    }

    /**
     * Execute the pipeline for an event.
     *
     * This starts execution from the outermost interceptor and proceeds
     * inward through each layer until reaching the dispatcher.
     */
    suspend fun execute(event: TelegramBotEvent) {
        pipeline.process(event)
    }
}
