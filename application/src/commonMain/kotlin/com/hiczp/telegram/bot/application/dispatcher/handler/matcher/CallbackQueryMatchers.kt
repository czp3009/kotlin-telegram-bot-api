package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.EventRoute
import com.hiczp.telegram.bot.protocol.event.CallbackQueryEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlinx.coroutines.CoroutineScope

/**
 * Registers a handler for callback queries with specific data.
 *
 * @param data The callback data string to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching callback.
 */
fun EventRoute<CallbackQueryEvent>.callbackData(
    data: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<CallbackQueryEvent>) -> Unit
) {
    select({ if (it.event.callbackQuery.data == data) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a callback data handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.callbackData(
    data: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<CallbackQueryEvent>) -> Unit
) = on<CallbackQueryEvent> { callbackData(data, handler) }

/**
 * Registers a handler for callback queries whose data matches a regular expression.
 *
 * @param pattern The regular expression pattern to match against callback data.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching callback.
 */
fun EventRoute<CallbackQueryEvent>.callbackDataRegex(
    pattern: Regex,
    handler: suspend CoroutineScope.(TelegramBotEventContext<CallbackQueryEvent>) -> Unit
) {
    select({ if (it.event.callbackQuery.data?.matches(pattern) == true) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a callback data regex handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.callbackDataRegex(
    pattern: Regex,
    handler: suspend CoroutineScope.(TelegramBotEventContext<CallbackQueryEvent>) -> Unit
) = on<CallbackQueryEvent> { callbackDataRegex(pattern, handler) }
