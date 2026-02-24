package com.hiczp.telegram.bot.application.interceptor

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent

/**
 * Base processor interface for handling Telegram events.
 *
 * Both interceptor wrappers and the final dispatcher implement this interface,
 * allowing them to be chained together in a pipeline. This enables the "onion model"
 * where each layer can process the event before and after the inner layers.
 */
interface TelegramEventProcessor {
    /**
     * Process a Telegram event.
     *
     * Implementations should handle the event or delegate to the next processor
     * in the chain by calling `this.process(context)` to pass control to the next layer.
     *
     * @param context The bot context containing client, event, and attributes.
     */
    suspend fun process(context: TelegramBotEventContext<TelegramBotEvent>)
}

/**
 * Interceptor function type for Telegram event processing.
 *
 * Inspired by Ktor's interceptor pattern, this is a suspend function with a receiver
 * that allows intercepting event processing. Inside the interceptor body, call
 * `this.process(context)` to pass control to the next layer in the pipeline.
 *
 * Example usage:
 * ```kotlin
 * val loggingInterceptor: TelegramEventInterceptor = { context ->
 *     println("Received event: ${context.event.updateId}")
 *     this.process(context) // Pass to next layer
 *     println("Finished processing event: ${context.event.updateId}")
 * }
 * ```
 */
typealias TelegramEventInterceptor = suspend TelegramEventProcessor.(TelegramBotEventContext<TelegramBotEvent>) -> Unit
