package com.hiczp.telegram.bot.application.interceptor

import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent

/**
 * Base processor interface for handling Telegram events.
 *
 * Both interceptor wrappers and the final dispatcher implement this interface,
 * allowing them to be chained together in a pipeline.
 */
interface TelegramEventProcessor {
    /**
     * Process a Telegram event.
     *
     * @param event The event to process.
     */
    suspend fun process(event: TelegramBotEvent)
}

/**
 * Interceptor function type for Telegram event processing.
 *
 * Inspired by Ktor's interceptor pattern, this is a suspend function with a receiver
 * that allows intercepting event processing. Inside the interceptor body, call
 * `this.process(event)` to pass control to the next layer in the pipeline.
 *
 * Example usage:
 * ```kotlin
 * val loggingInterceptor: TelegramEventInterceptor = { event ->
 *     println("Received event: ${event.updateId}")
 *     this.process(event) // Pass to next layer
 *     println("Finished processing event: ${event.updateId}")
 * }
 * ```
 */
typealias TelegramEventInterceptor = suspend TelegramEventProcessor.(TelegramBotEvent) -> Unit
