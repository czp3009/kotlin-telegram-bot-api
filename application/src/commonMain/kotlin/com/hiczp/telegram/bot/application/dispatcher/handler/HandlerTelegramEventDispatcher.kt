package com.hiczp.telegram.bot.application.dispatcher.handler

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
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
 * This implements the Chain of Responsibility pattern, where each handler can decide
 * whether to handle an event or pass it to the next handler in the chain.
 *
 * Example usage:
 * ```kotlin
 * val handlers = sequence {
 *     yield { context ->
 *         if (context.event is MessageEvent && context.event.message.text == "/start") {
 *             // Handle /start command
 *             true // Consumed
 *         } else {
 *             false // Not consumed, try next handler
 *         }
 *     }
 * }
 * val dispatcher = HandlerTelegramEventDispatcher(handlers)
 * ```
 *
 * @param handlers A [Sequence] of handler functions. Each handler receives a [TelegramBotEventContext]
 *                 and returns `true` if the event was consumed, `false` otherwise.
 */
open class HandlerTelegramEventDispatcher(
    private val handlers: Sequence<suspend (TelegramBotEventContext<TelegramBotEvent>) -> Boolean>
) : TelegramEventDispatcher {
    /**
     * Dispatch an event by attempting to handle it with each registered handler.
     *
     * Handlers are iterated lazily. If any handler returns `true`, the event
     * is considered consumed and no further handlers are invoked.
     * If no handler consumes the event, it is passed to [deadLetter].
     *
     * @param context The bot context containing client, event, and attributes.
     */
    override suspend fun dispatch(context: TelegramBotEventContext<TelegramBotEvent>) {
        for (handler in handlers) {
            if (handler(context)) {
                return
            }
        }
        deadLetter(context)
    }

    /**
     * Handle the event that was not consumed by any handler.
     *
     * @param context The bot context containing the unhandled event.
     */
    open suspend fun deadLetter(context: TelegramBotEventContext<TelegramBotEvent>) {
        logger.warn { "Unhandled event: ${context.event}" }
    }
}
