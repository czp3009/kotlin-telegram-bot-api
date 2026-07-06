@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.CallbackQueryEvent
import kotlin.jvm.JvmName

fun HandlerRoute<CallbackQueryEvent>.data(value: String, build: HandlerRoute<CallbackQueryEvent>.() -> Unit) =
    filter({ event.callbackQuery.data == value }, build)

fun HandlerRoute<CallbackQueryEvent>.data(pattern: Regex, build: HandlerRoute<CallbackQueryEvent>.() -> Unit) =
    filter({ event.callbackQuery.data?.matches(pattern) == true }, build)

fun HandlerRoute<CallbackQueryEvent>.dataContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = filter({ event.callbackQuery.data?.contains(substring, ignoreCase) == true }, build)

fun HandlerRoute<CallbackQueryEvent>.dataStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<CallbackQueryEvent>.() -> Unit
) = filter({ event.callbackQuery.data?.startsWith(prefix, ignoreCase) == true }, build)

@JvmName("callbackFromUser")
fun HandlerRoute<CallbackQueryEvent>.fromUser(userId: Long, build: HandlerRoute<CallbackQueryEvent>.() -> Unit) =
    filter({ event.callbackQuery.from.id == userId }, build)

@JvmName("callbackInChat")
fun HandlerRoute<CallbackQueryEvent>.inChat(chatId: Long, build: HandlerRoute<CallbackQueryEvent>.() -> Unit) =
    filter({ event.callbackQuery.message?.chat?.id == chatId }, build)
