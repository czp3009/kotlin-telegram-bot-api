@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.CallbackQueryEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlin.jvm.JvmName

// --- onCallbackData (stackable) ---

@JvmName("onCallbackDataCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.onCallbackData(
    data: String,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = select({ if (it.event.callbackQuery.data == data) it else null }, build)

@JvmName("onCallbackDataTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onCallbackData(
    data: String,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { onCallbackData(data, build) }

// --- whenCallbackData (terminal) ---

@JvmName("whenCallbackDataCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.whenCallbackData(
    data: String,
    handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit
) = select({ if (it.event.callbackQuery.data == data) it else null }) { handle(handler) }

@JvmName("whenCallbackDataTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenCallbackData(
    data: String,
    handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { whenCallbackData(data, handler) }

// --- onCallbackDataRegex (stackable) ---

@JvmName("onCallbackDataRegexCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.onCallbackDataRegex(
    pattern: Regex,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = select({ if (it.event.callbackQuery.data?.matches(pattern) == true) it else null }, build)

@JvmName("onCallbackDataRegexTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onCallbackDataRegex(
    pattern: Regex,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { onCallbackDataRegex(pattern, build) }

// --- whenCallbackDataRegex (terminal) ---

@JvmName("whenCallbackDataRegexCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.whenCallbackDataRegex(
    pattern: Regex,
    handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit
) = select({ if (it.event.callbackQuery.data?.matches(pattern) == true) it else null }) { handle(handler) }

@JvmName("whenCallbackDataRegexTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenCallbackDataRegex(
    pattern: Regex,
    handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { whenCallbackDataRegex(pattern, handler) }

// --- onCallbackDataContains (stackable) ---

@JvmName("onCallbackDataContainsCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.onCallbackDataContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = select({ if (it.event.callbackQuery.data?.contains(substring, ignoreCase) == true) it else null }, build)

@JvmName("onCallbackDataContainsTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onCallbackDataContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { onCallbackDataContains(substring, ignoreCase, build) }

// --- whenCallbackDataContains (terminal) ---

@JvmName("whenCallbackDataContainsCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.whenCallbackDataContains(
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

@JvmName("whenCallbackDataContainsTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenCallbackDataContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { whenCallbackDataContains(substring, ignoreCase, handler) }

// --- onCallbackDataStartsWith (stackable) ---

@JvmName("onCallbackDataStartsWithCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.onCallbackDataStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = select({ if (it.event.callbackQuery.data?.startsWith(prefix, ignoreCase) == true) it else null }, build)

@JvmName("onCallbackDataStartsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onCallbackDataStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { onCallbackDataStartsWith(prefix, ignoreCase, build) }

// --- whenCallbackDataStartsWith (terminal) ---

@JvmName("whenCallbackDataStartsWithCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.whenCallbackDataStartsWith(
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

@JvmName("whenCallbackDataStartsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenCallbackDataStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { whenCallbackDataStartsWith(prefix, ignoreCase, handler) }

// --- onCallbackQueryFromUser (stackable) ---

@JvmName("onCallbackQueryFromUserCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.onCallbackQueryFromUser(
    userId: Long,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = select({ if (it.event.callbackQuery.from.id == userId) it else null }, build)

@JvmName("onCallbackQueryFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onCallbackQueryFromUser(
    userId: Long,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { onCallbackQueryFromUser(userId, build) }

// --- whenCallbackQueryFromUser (terminal) ---

@JvmName("whenCallbackQueryFromUserCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.whenCallbackQueryFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit
) = select({ if (it.event.callbackQuery.from.id == userId) it else null }) { handle(handler) }

@JvmName("whenCallbackQueryFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenCallbackQueryFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { whenCallbackQueryFromUser(userId, handler) }

// --- onCallbackQueryInChat (stackable) ---

@JvmName("onCallbackQueryInChatCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.onCallbackQueryInChat(
    chatId: Long,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = select({ if (it.event.callbackQuery.message?.chat?.id == chatId) it else null }, build)

@JvmName("onCallbackQueryInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onCallbackQueryInChat(
    chatId: Long,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { onCallbackQueryInChat(chatId, build) }

// --- whenCallbackQueryInChat (terminal) ---

@JvmName("whenCallbackQueryInChatCallbackQueryEvent")
fun HandlerRoute<CallbackQueryEvent>.whenCallbackQueryInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit
) = select({ if (it.event.callbackQuery.message?.chat?.id == chatId) it else null }) { handle(handler) }

@JvmName("whenCallbackQueryInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenCallbackQueryInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<CallbackQueryEvent>.() -> Unit
) = on<CallbackQueryEvent> { whenCallbackQueryInChat(chatId, handler) }
