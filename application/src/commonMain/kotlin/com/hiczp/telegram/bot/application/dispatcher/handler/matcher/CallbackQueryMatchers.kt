package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.EventRoute
import com.hiczp.telegram.bot.protocol.event.CallbackQueryEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlinx.coroutines.CoroutineScope
import kotlin.jvm.JvmName

@JvmName("callbackDataCallbackQueryEvent")
fun EventRoute<CallbackQueryEvent>.callbackData(
    data: String,
    build: EventRoute<CallbackQueryEvent>.() -> Unit
): EventRoute<CallbackQueryEvent> = select({ if (it.event.callbackQuery.data == data) it else null }, build)

@JvmName("callbackDataTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.callbackData(
    data: String,
    build: EventRoute<CallbackQueryEvent>.() -> Unit
): EventRoute<CallbackQueryEvent> = on<CallbackQueryEvent> { callbackData(data, build) }

@JvmName("whenCallbackDataCallbackQueryEvent")
fun EventRoute<CallbackQueryEvent>.whenCallbackData(
    data: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<CallbackQueryEvent>) -> Unit
) = callbackData(data) { handle(handler) }

@JvmName("whenCallbackDataTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenCallbackData(
    data: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<CallbackQueryEvent>) -> Unit
) = on<CallbackQueryEvent> { whenCallbackData(data, handler) }

@JvmName("callbackDataRegexCallbackQueryEvent")
fun EventRoute<CallbackQueryEvent>.callbackDataRegex(
    pattern: Regex,
    build: EventRoute<CallbackQueryEvent>.() -> Unit
): EventRoute<CallbackQueryEvent> =
    select({ if (it.event.callbackQuery.data?.matches(pattern) == true) it else null }, build)

@JvmName("callbackDataRegexTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.callbackDataRegex(
    pattern: Regex,
    build: EventRoute<CallbackQueryEvent>.() -> Unit
): EventRoute<CallbackQueryEvent> = on<CallbackQueryEvent> { callbackDataRegex(pattern, build) }

@JvmName("whenCallbackDataRegexCallbackQueryEvent")
fun EventRoute<CallbackQueryEvent>.whenCallbackDataRegex(
    pattern: Regex,
    handler: suspend CoroutineScope.(TelegramBotEventContext<CallbackQueryEvent>) -> Unit
) = callbackDataRegex(pattern) { handle(handler) }

@JvmName("whenCallbackDataRegexTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenCallbackDataRegex(
    pattern: Regex,
    handler: suspend CoroutineScope.(TelegramBotEventContext<CallbackQueryEvent>) -> Unit
) = on<CallbackQueryEvent> { whenCallbackDataRegex(pattern, handler) }
