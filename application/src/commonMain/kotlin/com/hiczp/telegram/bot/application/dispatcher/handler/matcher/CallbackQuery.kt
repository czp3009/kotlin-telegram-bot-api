@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.CallbackQueryEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlin.jvm.JvmName

// --- onCallbackQueryEventData (stackable) ---

@JvmName("onCallbackQueryEventDataCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.onCallbackQueryEventData(
    data: String,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = select({ if (it.event.callbackQuery.data == data) it else null }, build)

@JvmName("onCallbackQueryEventDataTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onCallbackQueryEventData(
    data: String,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { onCallbackQueryEventData(data, build) }

// --- whenCallbackQueryEventData (terminal) ---

@JvmName("whenCallbackQueryEventDataCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.whenCallbackQueryEventData(
    data: String,
    handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit
) = select({ if (it.event.callbackQuery.data == data) it else null }) { handle(handler) }

@JvmName("whenCallbackQueryEventDataTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenCallbackQueryEventData(
    data: String,
    handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { whenCallbackQueryEventData(data, handler) }

// --- onCallbackQueryEventDataRegex (stackable) ---

@JvmName("onCallbackQueryEventDataRegexCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.onCallbackQueryEventDataRegex(
    pattern: Regex,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = select({ if (it.event.callbackQuery.data?.matches(pattern) == true) it else null }, build)

@JvmName("onCallbackQueryEventDataRegexTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onCallbackQueryEventDataRegex(
    pattern: Regex,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { onCallbackQueryEventDataRegex(pattern, build) }

// --- whenCallbackQueryEventDataRegex (terminal) ---

@JvmName("whenCallbackQueryEventDataRegexCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.whenCallbackQueryEventDataRegex(
    pattern: Regex,
    handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit
) = select({ if (it.event.callbackQuery.data?.matches(pattern) == true) it else null }) { handle(handler) }

@JvmName("whenCallbackQueryEventDataRegexTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenCallbackQueryEventDataRegex(
    pattern: Regex,
    handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { whenCallbackQueryEventDataRegex(pattern, handler) }

// --- onCallbackQueryEventDataContains (stackable) ---

@JvmName("onCallbackQueryEventDataContainsCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.onCallbackQueryEventDataContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = select({ if (it.event.callbackQuery.data?.contains(substring, ignoreCase) == true) it else null }, build)

@JvmName("onCallbackQueryEventDataContainsTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onCallbackQueryEventDataContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { onCallbackQueryEventDataContains(substring, ignoreCase, build) }

// --- whenCallbackQueryEventDataContains (terminal) ---

@JvmName("whenCallbackQueryEventDataContainsCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.whenCallbackQueryEventDataContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit
) = select({
    if (it.event.callbackQuery.data?.contains(
            substring,
            ignoreCase
        ) == true
    ) it else null
}) { handle(handler) }

@JvmName("whenCallbackQueryEventDataContainsTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenCallbackQueryEventDataContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { whenCallbackQueryEventDataContains(substring, ignoreCase, handler) }

// --- onCallbackQueryEventDataStartsWith (stackable) ---

@JvmName("onCallbackQueryEventDataStartsWithCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.onCallbackQueryEventDataStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = select({ if (it.event.callbackQuery.data?.startsWith(prefix, ignoreCase) == true) it else null }, build)

@JvmName("onCallbackQueryEventDataStartsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onCallbackQueryEventDataStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { onCallbackQueryEventDataStartsWith(prefix, ignoreCase, build) }

// --- whenCallbackQueryEventDataStartsWith (terminal) ---

@JvmName("whenCallbackQueryEventDataStartsWithCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.whenCallbackQueryEventDataStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit
) = select({
    if (it.event.callbackQuery.data?.startsWith(
            prefix,
            ignoreCase
        ) == true
    ) it else null
}) { handle(handler) }

@JvmName("whenCallbackQueryEventDataStartsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenCallbackQueryEventDataStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { whenCallbackQueryEventDataStartsWith(prefix, ignoreCase, handler) }

// --- onCallbackQueryEventFromUser (stackable) ---

@JvmName("onCallbackQueryEventFromUserCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.onCallbackQueryEventFromUser(
    userId: Long,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = select({ if (it.event.callbackQuery.from.id == userId) it else null }, build)

@JvmName("onCallbackQueryEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onCallbackQueryEventFromUser(
    userId: Long,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { onCallbackQueryEventFromUser(userId, build) }

// --- whenCallbackQueryEventFromUser (terminal) ---

@JvmName("whenCallbackQueryEventFromUserCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.whenCallbackQueryEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit
) = select({ if (it.event.callbackQuery.from.id == userId) it else null }) { handle(handler) }

@JvmName("whenCallbackQueryEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenCallbackQueryEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { whenCallbackQueryEventFromUser(userId, handler) }

// --- onCallbackQueryEventInChat (stackable) ---

@JvmName("onCallbackQueryEventInChatCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.onCallbackQueryEventInChat(
    chatId: Long,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = select({ if (it.event.callbackQuery.message?.chat?.id == chatId) it else null }, build)

@JvmName("onCallbackQueryEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onCallbackQueryEventInChat(
    chatId: Long,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { onCallbackQueryEventInChat(chatId, build) }

// --- whenCallbackQueryEventInChat (terminal) ---

@JvmName("whenCallbackQueryEventInChatCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.whenCallbackQueryEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit
) = select({ if (it.event.callbackQuery.message?.chat?.id == chatId) it else null }) { handle(handler) }

@JvmName("whenCallbackQueryEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenCallbackQueryEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { whenCallbackQueryEventInChat(chatId, handler) }
