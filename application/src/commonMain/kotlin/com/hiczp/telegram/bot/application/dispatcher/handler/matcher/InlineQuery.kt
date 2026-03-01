@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.ChosenInlineResultEvent
import com.hiczp.telegram.bot.protocol.event.InlineQueryEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlin.jvm.JvmName

// --- onInlineQuery (stackable) ---

@JvmName("onInlineQueryInlineQueryEvent")
fun HandlerRoute<InlineQueryEvent>.onInlineQuery(
    query: String,
    build: HandlerRoute<InlineQueryEvent>.() -> Unit
) = select({ if (it.event.inlineQuery.query == query) it else null }, build)

@JvmName("onInlineQueryTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onInlineQuery(
    query: String,
    build: HandlerRoute<InlineQueryEvent>.() -> Unit
) = onInlineQueryEvent { onInlineQuery(query, build) }

// --- whenInlineQuery (terminal) ---

@JvmName("whenInlineQueryInlineQueryEvent")
fun HandlerRoute<InlineQueryEvent>.whenInlineQuery(
    query: String,
    handler: suspend HandlerBotCall<InlineQueryEvent>.() -> Unit
) = select({ if (it.event.inlineQuery.query == query) it else null }) { handle(handler) }

@JvmName("whenInlineQueryTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenInlineQuery(
    query: String,
    handler: suspend HandlerBotCall<InlineQueryEvent>.() -> Unit
) = onInlineQueryEvent { whenInlineQuery(query, handler) }

// --- onInlineQueryRegex (stackable) ---

@JvmName("onInlineQueryRegexInlineQueryEvent")
fun HandlerRoute<InlineQueryEvent>.onInlineQueryRegex(
    pattern: Regex,
    build: HandlerRoute<InlineQueryEvent>.() -> Unit
) = select({ if (it.event.inlineQuery.query.matches(pattern)) it else null }, build)

@JvmName("onInlineQueryRegexTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onInlineQueryRegex(
    pattern: Regex,
    build: HandlerRoute<InlineQueryEvent>.() -> Unit
) = onInlineQueryEvent { onInlineQueryRegex(pattern, build) }

// --- whenInlineQueryRegex (terminal) ---

@JvmName("whenInlineQueryRegexInlineQueryEvent")
fun HandlerRoute<InlineQueryEvent>.whenInlineQueryRegex(
    pattern: Regex,
    handler: suspend HandlerBotCall<InlineQueryEvent>.() -> Unit
) = select({ if (it.event.inlineQuery.query.matches(pattern)) it else null }) { handle(handler) }

@JvmName("whenInlineQueryRegexTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenInlineQueryRegex(
    pattern: Regex,
    handler: suspend HandlerBotCall<InlineQueryEvent>.() -> Unit
) = onInlineQueryEvent { whenInlineQueryRegex(pattern, handler) }

// --- onInlineQueryContains (stackable) ---

@JvmName("onInlineQueryContainsInlineQueryEvent")
fun HandlerRoute<InlineQueryEvent>.onInlineQueryContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<InlineQueryEvent>.() -> Unit
) = select({ if (it.event.inlineQuery.query.contains(substring, ignoreCase)) it else null }, build)

@JvmName("onInlineQueryContainsTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onInlineQueryContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<InlineQueryEvent>.() -> Unit
) = onInlineQueryEvent { onInlineQueryContains(substring, ignoreCase, build) }

// --- whenInlineQueryContains (terminal) ---

@JvmName("whenInlineQueryContainsInlineQueryEvent")
fun HandlerRoute<InlineQueryEvent>.whenInlineQueryContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<InlineQueryEvent>.() -> Unit
) = select({ if (it.event.inlineQuery.query.contains(substring, ignoreCase)) it else null }) { handle(handler) }

@JvmName("whenInlineQueryContainsTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenInlineQueryContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<InlineQueryEvent>.() -> Unit
) = onInlineQueryEvent { whenInlineQueryContains(substring, ignoreCase, handler) }

// --- onInlineQueryStartsWith (stackable) ---

@JvmName("onInlineQueryStartsWithInlineQueryEvent")
fun HandlerRoute<InlineQueryEvent>.onInlineQueryStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<InlineQueryEvent>.() -> Unit
) = select({ if (it.event.inlineQuery.query.startsWith(prefix, ignoreCase)) it else null }, build)

@JvmName("onInlineQueryStartsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onInlineQueryStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<InlineQueryEvent>.() -> Unit
) = onInlineQueryEvent { onInlineQueryStartsWith(prefix, ignoreCase, build) }

// --- whenInlineQueryStartsWith (terminal) ---

@JvmName("whenInlineQueryStartsWithInlineQueryEvent")
fun HandlerRoute<InlineQueryEvent>.whenInlineQueryStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<InlineQueryEvent>.() -> Unit
) = select({ if (it.event.inlineQuery.query.startsWith(prefix, ignoreCase)) it else null }) { handle(handler) }

@JvmName("whenInlineQueryStartsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenInlineQueryStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<InlineQueryEvent>.() -> Unit
) = onInlineQueryEvent { whenInlineQueryStartsWith(prefix, ignoreCase, handler) }

// --- onInlineQueryFromUser (stackable) ---

@JvmName("onInlineQueryFromUserInlineQueryEvent")
fun HandlerRoute<InlineQueryEvent>.onInlineQueryFromUser(
    userId: Long,
    build: HandlerRoute<InlineQueryEvent>.() -> Unit
) = select({ if (it.event.inlineQuery.from.id == userId) it else null }, build)

@JvmName("onInlineQueryFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onInlineQueryFromUser(
    userId: Long,
    build: HandlerRoute<InlineQueryEvent>.() -> Unit
) = onInlineQueryEvent { onInlineQueryFromUser(userId, build) }

// --- whenInlineQueryFromUser (terminal) ---

@JvmName("whenInlineQueryFromUserInlineQueryEvent")
fun HandlerRoute<InlineQueryEvent>.whenInlineQueryFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<InlineQueryEvent>.() -> Unit
) = select({ if (it.event.inlineQuery.from.id == userId) it else null }) { handle(handler) }

@JvmName("whenInlineQueryFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenInlineQueryFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<InlineQueryEvent>.() -> Unit
) = onInlineQueryEvent { whenInlineQueryFromUser(userId, handler) }

// --- onChosenInlineResult (stackable) ---

@JvmName("onChosenInlineResultChosenInlineResultEvent")
fun HandlerRoute<ChosenInlineResultEvent>.onChosenInlineResult(build: HandlerRoute<ChosenInlineResultEvent>.() -> Unit) =
    select({ it }, build)

@JvmName("onChosenInlineResultTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onChosenInlineResult(build: HandlerRoute<ChosenInlineResultEvent>.() -> Unit) =
    onChosenInlineResultEvent { onChosenInlineResult(build) }

// --- whenChosenInlineResult (terminal) ---

@JvmName("whenChosenInlineResultChosenInlineResultEvent")
fun HandlerRoute<ChosenInlineResultEvent>.whenChosenInlineResult(handler: suspend HandlerBotCall<ChosenInlineResultEvent>.() -> Unit) =
    select({ it }) { handle(handler) }

@JvmName("whenChosenInlineResultTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenChosenInlineResult(handler: suspend HandlerBotCall<ChosenInlineResultEvent>.() -> Unit) =
    onChosenInlineResultEvent { whenChosenInlineResult(handler) }

// --- onChosenInlineResultFromUser (stackable) ---

@JvmName("onChosenInlineResultFromUserChosenInlineResultEvent")
fun HandlerRoute<ChosenInlineResultEvent>.onChosenInlineResultFromUser(
    userId: Long,
    build: HandlerRoute<ChosenInlineResultEvent>.() -> Unit
) = select({ if (it.event.chosenInlineResult.from.id == userId) it else null }, build)

@JvmName("onChosenInlineResultFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onChosenInlineResultFromUser(
    userId: Long,
    build: HandlerRoute<ChosenInlineResultEvent>.() -> Unit
) = onChosenInlineResultEvent { onChosenInlineResultFromUser(userId, build) }

// --- whenChosenInlineResultFromUser (terminal) ---

@JvmName("whenChosenInlineResultFromUserChosenInlineResultEvent")
fun HandlerRoute<ChosenInlineResultEvent>.whenChosenInlineResultFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<ChosenInlineResultEvent>.() -> Unit
) = select({ if (it.event.chosenInlineResult.from.id == userId) it else null }) { handle(handler) }

@JvmName("whenChosenInlineResultFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenChosenInlineResultFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<ChosenInlineResultEvent>.() -> Unit
) = onChosenInlineResultEvent { whenChosenInlineResultFromUser(userId, handler) }
