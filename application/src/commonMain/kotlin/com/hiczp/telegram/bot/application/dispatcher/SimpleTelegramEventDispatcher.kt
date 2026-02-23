package com.hiczp.telegram.bot.application.dispatcher

import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * A simple dispatcher implementation that delegates event handling to a lambda function.
 *
 * This is the most straightforward implementation of [TelegramEventDispatcher],
 * useful for simple use cases or testing where all events are handled uniformly.
 *
 * Example usage:
 * ```kotlin
 * val dispatcher = SimpleTelegramEventDispatcher { event ->
 *     println("Received event: ${event.updateId}")
 *     // Handle the event...
 * }
 * ```
 *
 * @param action The suspend function to invoke for each event.
 */
open class SimpleTelegramEventDispatcher(
    private val action: suspend (TelegramBotEvent) -> Unit
) : TelegramEventDispatcher {
    /**
     * Dispatch an event by invoking the configured [action] function.
     *
     * @param event The event to dispatch.
     */
    override suspend fun dispatch(event: TelegramBotEvent) {
        action(event)
    }
}
