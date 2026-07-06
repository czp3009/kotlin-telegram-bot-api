@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.ChosenInlineResultEvent
import com.hiczp.telegram.bot.protocol.event.InlineQueryEvent
import kotlin.jvm.JvmName

fun HandlerRoute<InlineQueryEvent>.query(value: String, build: HandlerRoute<InlineQueryEvent>.() -> Unit) =
    filter({ event.inlineQuery.query == value }, build)

fun HandlerRoute<InlineQueryEvent>.query(pattern: Regex, build: HandlerRoute<InlineQueryEvent>.() -> Unit) =
    filter({ event.inlineQuery.query.matches(pattern) }, build)

fun HandlerRoute<InlineQueryEvent>.queryContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<InlineQueryEvent>.() -> Unit
) = filter({ event.inlineQuery.query.contains(substring, ignoreCase) }, build)

fun HandlerRoute<InlineQueryEvent>.queryStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<InlineQueryEvent>.() -> Unit
) = filter({ event.inlineQuery.query.startsWith(prefix, ignoreCase) }, build)

@JvmName("inlineFromUser")
fun HandlerRoute<InlineQueryEvent>.fromUser(userId: Long, build: HandlerRoute<InlineQueryEvent>.() -> Unit) =
    filter({ event.inlineQuery.from.id == userId }, build)

@JvmName("chosenInlineFromUser")
fun HandlerRoute<ChosenInlineResultEvent>.fromUser(
    userId: Long,
    build: HandlerRoute<ChosenInlineResultEvent>.() -> Unit
) = filter({ event.chosenInlineResult.from.id == userId }, build)
