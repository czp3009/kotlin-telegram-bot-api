@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.ChosenInlineResultEvent
import com.hiczp.telegram.bot.protocol.event.InlineQueryEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlin.jvm.JvmName

// --- onInlineQueryEventQuery (stackable) ---

@JvmName("onInlineQueryEventQueryInlineQueryEvent")
fun HandlerRoute<InlineQueryEvent>.onInlineQueryEventQuery(
    query: String,
    build: HandlerRoute<InlineQueryEvent>.() -> Unit
) = select({ if (it.event.inlineQuery.query == query) it else null }, build)

@JvmName("onInlineQueryEventQueryTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onInlineQueryEventQuery(
    query: String,
    build: HandlerRoute<InlineQueryEvent>.() -> Unit
) = onInlineQueryEvent { onInlineQueryEventQuery(query, build) }

// --- whenInlineQueryEventQuery (terminal) ---

@JvmName("whenInlineQueryEventQueryInlineQueryEvent")
fun HandlerRoute<InlineQueryEvent>.whenInlineQueryEventQuery(
    query: String,
    handler: suspend HandlerBotCall<InlineQueryEvent>.() -> Unit
) = select({ if (it.event.inlineQuery.query == query) it else null }) { handle(handler) }

@JvmName("whenInlineQueryEventQueryTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenInlineQueryEventQuery(
    query: String,
    handler: suspend HandlerBotCall<InlineQueryEvent>.() -> Unit
) = onInlineQueryEvent { whenInlineQueryEventQuery(query, handler) }

// --- onInlineQueryEventQueryRegex (stackable) ---

@JvmName("onInlineQueryEventQueryRegexInlineQueryEvent")
fun HandlerRoute<InlineQueryEvent>.onInlineQueryEventQueryRegex(
    pattern: Regex,
    build: HandlerRoute<InlineQueryEvent>.() -> Unit
) = select({ if (it.event.inlineQuery.query.matches(pattern)) it else null }, build)

@JvmName("onInlineQueryEventQueryRegexTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onInlineQueryEventQueryRegex(
    pattern: Regex,
    build: HandlerRoute<InlineQueryEvent>.() -> Unit
) = onInlineQueryEvent { onInlineQueryEventQueryRegex(pattern, build) }

// --- whenInlineQueryEventQueryRegex (terminal) ---

@JvmName("whenInlineQueryEventQueryRegexInlineQueryEvent")
fun HandlerRoute<InlineQueryEvent>.whenInlineQueryEventQueryRegex(
    pattern: Regex,
    handler: suspend HandlerBotCall<InlineQueryEvent>.() -> Unit
) = select({ if (it.event.inlineQuery.query.matches(pattern)) it else null }) { handle(handler) }

@JvmName("whenInlineQueryEventQueryRegexTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenInlineQueryEventQueryRegex(
    pattern: Regex,
    handler: suspend HandlerBotCall<InlineQueryEvent>.() -> Unit
) = onInlineQueryEvent { whenInlineQueryEventQueryRegex(pattern, handler) }

// --- onInlineQueryEventQueryContains (stackable) ---

@JvmName("onInlineQueryEventQueryContainsInlineQueryEvent")
fun HandlerRoute<InlineQueryEvent>.onInlineQueryEventQueryContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<InlineQueryEvent>.() -> Unit
) = select({ if (it.event.inlineQuery.query.contains(substring, ignoreCase)) it else null }, build)

@JvmName("onInlineQueryEventQueryContainsTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onInlineQueryEventQueryContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<InlineQueryEvent>.() -> Unit
) = onInlineQueryEvent { onInlineQueryEventQueryContains(substring, ignoreCase, build) }

// --- whenInlineQueryEventQueryContains (terminal) ---

@JvmName("whenInlineQueryEventQueryContainsInlineQueryEvent")
fun HandlerRoute<InlineQueryEvent>.whenInlineQueryEventQueryContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<InlineQueryEvent>.() -> Unit
) = select({ if (it.event.inlineQuery.query.contains(substring, ignoreCase)) it else null }) { handle(handler) }

@JvmName("whenInlineQueryEventQueryContainsTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenInlineQueryEventQueryContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<InlineQueryEvent>.() -> Unit
) = onInlineQueryEvent { whenInlineQueryEventQueryContains(substring, ignoreCase, handler) }

// --- onInlineQueryEventQueryStartsWith (stackable) ---

@JvmName("onInlineQueryEventQueryStartsWithInlineQueryEvent")
fun HandlerRoute<InlineQueryEvent>.onInlineQueryEventQueryStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<InlineQueryEvent>.() -> Unit
) = select({ if (it.event.inlineQuery.query.startsWith(prefix, ignoreCase)) it else null }, build)

@JvmName("onInlineQueryEventQueryStartsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onInlineQueryEventQueryStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<InlineQueryEvent>.() -> Unit
) = onInlineQueryEvent { onInlineQueryEventQueryStartsWith(prefix, ignoreCase, build) }

// --- whenInlineQueryEventQueryStartsWith (terminal) ---

@JvmName("whenInlineQueryEventQueryStartsWithInlineQueryEvent")
fun HandlerRoute<InlineQueryEvent>.whenInlineQueryEventQueryStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<InlineQueryEvent>.() -> Unit
) = select({ if (it.event.inlineQuery.query.startsWith(prefix, ignoreCase)) it else null }) { handle(handler) }

@JvmName("whenInlineQueryEventQueryStartsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenInlineQueryEventQueryStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<InlineQueryEvent>.() -> Unit
) = onInlineQueryEvent { whenInlineQueryEventQueryStartsWith(prefix, ignoreCase, handler) }

// --- onInlineQueryEventFromUser (stackable) ---

@JvmName("onInlineQueryEventFromUserInlineQueryEvent")
fun HandlerRoute<InlineQueryEvent>.onInlineQueryEventFromUser(
    userId: Long,
    build: HandlerRoute<InlineQueryEvent>.() -> Unit
) = select({ if (it.event.inlineQuery.from.id == userId) it else null }, build)

@JvmName("onInlineQueryEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onInlineQueryEventFromUser(
    userId: Long,
    build: HandlerRoute<InlineQueryEvent>.() -> Unit
) = onInlineQueryEvent { onInlineQueryEventFromUser(userId, build) }

// --- whenInlineQueryEventFromUser (terminal) ---

@JvmName("whenInlineQueryEventFromUserInlineQueryEvent")
fun HandlerRoute<InlineQueryEvent>.whenInlineQueryEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<InlineQueryEvent>.() -> Unit
) = select({ if (it.event.inlineQuery.from.id == userId) it else null }) { handle(handler) }

@JvmName("whenInlineQueryEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenInlineQueryEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<InlineQueryEvent>.() -> Unit
) = onInlineQueryEvent { whenInlineQueryEventFromUser(userId, handler) }

// --- onChosenInlineResultEventMatchAny (stackable) ---

@JvmName("onChosenInlineResultEventMatchAnyChosenInlineResultEvent")
fun HandlerRoute<ChosenInlineResultEvent>.onChosenInlineResultEventMatchAny(build: HandlerRoute<ChosenInlineResultEvent>.() -> Unit) =
    select({ it }, build)

@JvmName("onChosenInlineResultEventMatchAnyTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onChosenInlineResultEventMatchAny(build: HandlerRoute<ChosenInlineResultEvent>.() -> Unit) =
    on<ChosenInlineResultEvent> { onChosenInlineResultEventMatchAny(build) }

// --- whenChosenInlineResultEventMatchAny (terminal) ---

@JvmName("whenChosenInlineResultEventMatchAnyChosenInlineResultEvent")
fun HandlerRoute<ChosenInlineResultEvent>.whenChosenInlineResultEventMatchAny(handler: suspend HandlerBotCall<ChosenInlineResultEvent>.() -> Unit) =
    select({ it }) { handle(handler) }

@JvmName("whenChosenInlineResultEventMatchAnyTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenChosenInlineResultEventMatchAny(handler: suspend HandlerBotCall<ChosenInlineResultEvent>.() -> Unit) =
    on<ChosenInlineResultEvent> { whenChosenInlineResultEventMatchAny(handler) }

// --- onChosenInlineResultEventFromUser (stackable) ---

@JvmName("onChosenInlineResultEventFromUserChosenInlineResultEvent")
fun HandlerRoute<ChosenInlineResultEvent>.onChosenInlineResultEventFromUser(
    userId: Long,
    build: HandlerRoute<ChosenInlineResultEvent>.() -> Unit
) = select({ if (it.event.chosenInlineResult.from.id == userId) it else null }, build)

@JvmName("onChosenInlineResultEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onChosenInlineResultEventFromUser(
    userId: Long,
    build: HandlerRoute<ChosenInlineResultEvent>.() -> Unit
) = on<ChosenInlineResultEvent> { onChosenInlineResultEventFromUser(userId, build) }

// --- whenChosenInlineResultEventFromUser (terminal) ---

@JvmName("whenChosenInlineResultEventFromUserChosenInlineResultEvent")
fun HandlerRoute<ChosenInlineResultEvent>.whenChosenInlineResultEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<ChosenInlineResultEvent>.() -> Unit
) = select({ if (it.event.chosenInlineResult.from.id == userId) it else null }) { handle(handler) }

@JvmName("whenChosenInlineResultEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenChosenInlineResultEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<ChosenInlineResultEvent>.() -> Unit
) = on<ChosenInlineResultEvent> { whenChosenInlineResultEventFromUser(userId, handler) }
