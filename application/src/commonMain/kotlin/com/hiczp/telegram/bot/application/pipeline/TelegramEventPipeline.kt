package com.hiczp.telegram.bot.application.pipeline

import com.hiczp.telegram.bot.application.context.DefaultTelegramBotEventContext
import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.TelegramEventDispatcher
import com.hiczp.telegram.bot.application.interceptor.TelegramEventInterceptor
import com.hiczp.telegram.bot.application.interceptor.TelegramEventProcessor
import com.hiczp.telegram.bot.client.TelegramBotClient
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import io.ktor.util.*
import kotlinx.coroutines.CoroutineScope

private class DispatcherTelegramEventProcessor(
    private val dispatcher: TelegramEventDispatcher
) : TelegramEventProcessor {
    override suspend fun process(context: TelegramBotEventContext) {
        dispatcher.dispatch(context)
    }
}

private class InterceptedTelegramEventProcessor(
    private val interceptor: TelegramEventInterceptor,
    private val nextProcessor: TelegramEventProcessor,
) : TelegramEventProcessor {
    override suspend fun process(context: TelegramBotEventContext) {
        interceptor(nextProcessor, context)
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
 * val interceptors = listOf<TelegramEventInterceptor> { context ->
 *     println("Before processing: ${context.event.updateId}")
 *     this.process(context)
 *     println("After processing: ${context.event.updateId}")
 * }
 *
 * val pipeline = TelegramEventPipeline(client, interceptors, eventDispatcher)
 * pipeline(event)
 * ```
 *
 * @param client The Telegram bot client.
 * @param interceptors List of interceptors to apply, in order from outermost to innermost.
 * @param dispatcher The final dispatcher that handles event routing to business logic.
 */
internal class TelegramEventPipeline(
    private val client: TelegramBotClient,
    private val applicationScope: CoroutineScope,
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
     *
     * A new [TelegramBotEventContext] is created for each event execution.
     *
     * @param event The event to process.
     */
    suspend fun execute(event: TelegramBotEvent) {
        val context = DefaultTelegramBotEventContext(
            client = client,
            event = event,
            applicationScope = applicationScope,
            attributes = Attributes(concurrent = true),
        )
        pipeline.process(context)
    }
}
