package com.hiczp.telegram.bot.application.dispatcher.handler

import com.hiczp.telegram.bot.application.dispatcher.TelegramEventDispatcher
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * A dispatcher implementation that routes events to registered handler functions.
 *
 * This dispatcher receives a [Sequence] of handler functions and iterates through them
 * until one consumes the event. If no handler consumes the event, it is logged
 * as an unhandled event (dead letter).
 *
 * @param handlers A [Sequence] of handler functions. Each handler receives a [TelegramBotEvent]
 *                 and returns `true` if the event was consumed, `false` otherwise.
 *
 * Example usage:
 * ```kotlin
 * val handlers = sequence {
 *     yield { event ->
 *         if (event is MessageEvent && event.message.text == "/start") {
 *             // Handle /start command
 *             true // Consumed
 *         } else {
 *             false // Not consumed, try next handler
 *         }
 *     }
 * }
 * val dispatcher = TelegramEventHandlerDispatcher(handlers)
 * ```
 */
open class TelegramEventHandlerDispatcher(
    private val handlers: Sequence<(suspend (TelegramBotEvent) -> Boolean)>
) : TelegramEventDispatcher {
    /**
     * Dispatch an event by attempting to handle it with each registered handler.
     *
     * Handlers are iterated lazily. If any handler returns `true`, the event
     * is considered consumed and no further handlers are invoked.
     * If no handler consumes the event, it is passed to [deadLetter].
     *
     * @param event The event to dispatch.
     */
    override suspend fun dispatch(event: TelegramBotEvent) {
        for (handler in handlers) {
            if (handler(event)) {
                return
            }
        }
        deadLetter(event)
    }

    /**
     * Handle the event that was not consumed by any handler.
     *
     * @param event The unhandled event.
     */
    suspend fun deadLetter(event: TelegramBotEvent) {
        logger.debug { "Unhandled event: $event" }
    }
}
