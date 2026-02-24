package com.hiczp.telegram.bot.application.dispatcher

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent

/**
 * A simple dispatcher implementation that delegates event handling to a lambda function.
 *
 * This is the most straightforward implementation of [TelegramEventDispatcher],
 * useful for simple use cases or testing where all events are handled uniformly.
 *
 * Example usage:
 * ```kotlin
 * val dispatcher = SimpleTelegramEventDispatcher { context ->
 *     println("Received event: ${context.event.updateId}")
 *     // Handle the event...
 * }
 * ```
 *
 * @param action The suspend function to invoke for each event.
 */
open class SimpleTelegramEventDispatcher(
    private val action: suspend (TelegramBotEventContext<TelegramBotEvent>) -> Unit
) : TelegramEventDispatcher {
    /**
     * Dispatch an event by invoking the configured [action] function.
     *
     * @param context The bot context containing client, event, and attributes.
     */
    override suspend fun dispatch(context: TelegramBotEventContext<TelegramBotEvent>) {
        action(context)
    }
}
