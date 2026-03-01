package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.EventRoute
import com.hiczp.telegram.bot.protocol.event.ChosenInlineResultEvent
import com.hiczp.telegram.bot.protocol.event.InlineQueryEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlinx.coroutines.CoroutineScope
import kotlin.jvm.JvmName

@JvmName("inlineQueryInlineQueryEvent")
fun EventRoute<InlineQueryEvent>.inlineQuery(
    query: String,
    build: EventRoute<InlineQueryEvent>.() -> Unit
): EventRoute<InlineQueryEvent> = select({ if (it.event.inlineQuery.query == query) it else null }, build)

@JvmName("inlineQueryTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.inlineQuery(
    query: String,
    build: EventRoute<InlineQueryEvent>.() -> Unit
): EventRoute<InlineQueryEvent> = on<InlineQueryEvent> { inlineQuery(query, build) }

@JvmName("whenInlineQueryInlineQueryEvent")
fun EventRoute<InlineQueryEvent>.whenInlineQuery(
    query: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) = inlineQuery(query) { handle(handler) }

@JvmName("whenInlineQueryTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenInlineQuery(
    query: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) = on<InlineQueryEvent> { whenInlineQuery(query, handler) }

@JvmName("inlineQueryRegexInlineQueryEvent")
fun EventRoute<InlineQueryEvent>.inlineQueryRegex(
    pattern: Regex,
    build: EventRoute<InlineQueryEvent>.() -> Unit
): EventRoute<InlineQueryEvent> = select({ if (it.event.inlineQuery.query.matches(pattern)) it else null }, build)

@JvmName("inlineQueryRegexTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.inlineQueryRegex(
    pattern: Regex,
    build: EventRoute<InlineQueryEvent>.() -> Unit
): EventRoute<InlineQueryEvent> = on<InlineQueryEvent> { inlineQueryRegex(pattern, build) }

@JvmName("whenInlineQueryRegexInlineQueryEvent")
fun EventRoute<InlineQueryEvent>.whenInlineQueryRegex(
    pattern: Regex,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) = inlineQueryRegex(pattern) { handle(handler) }

@JvmName("whenInlineQueryRegexTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenInlineQueryRegex(
    pattern: Regex,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) = on<InlineQueryEvent> { whenInlineQueryRegex(pattern, handler) }

@JvmName("inlineQueryContainsInlineQueryEvent")
fun EventRoute<InlineQueryEvent>.inlineQueryContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: EventRoute<InlineQueryEvent>.() -> Unit
): EventRoute<InlineQueryEvent> =
    select({ if (it.event.inlineQuery.query.contains(substring, ignoreCase)) it else null }, build)

@JvmName("inlineQueryContainsTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.inlineQueryContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: EventRoute<InlineQueryEvent>.() -> Unit
): EventRoute<InlineQueryEvent> = on<InlineQueryEvent> { inlineQueryContains(substring, ignoreCase, build) }

@JvmName("whenInlineQueryContainsInlineQueryEvent")
fun EventRoute<InlineQueryEvent>.whenInlineQueryContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) = inlineQueryContains(substring, ignoreCase) { handle(handler) }

@JvmName("whenInlineQueryContainsTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenInlineQueryContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) = on<InlineQueryEvent> { whenInlineQueryContains(substring, ignoreCase, handler) }

@JvmName("inlineQueryStartsWithInlineQueryEvent")
fun EventRoute<InlineQueryEvent>.inlineQueryStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: EventRoute<InlineQueryEvent>.() -> Unit
): EventRoute<InlineQueryEvent> =
    select({ if (it.event.inlineQuery.query.startsWith(prefix, ignoreCase)) it else null }, build)

@JvmName("inlineQueryStartsWithTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.inlineQueryStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: EventRoute<InlineQueryEvent>.() -> Unit
): EventRoute<InlineQueryEvent> = on<InlineQueryEvent> { inlineQueryStartsWith(prefix, ignoreCase, build) }

@JvmName("whenInlineQueryStartsWithInlineQueryEvent")
fun EventRoute<InlineQueryEvent>.whenInlineQueryStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) = inlineQueryStartsWith(prefix, ignoreCase) { handle(handler) }

@JvmName("whenInlineQueryStartsWithTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenInlineQueryStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) = on<InlineQueryEvent> { whenInlineQueryStartsWith(prefix, ignoreCase, handler) }

@JvmName("inlineQueryFromUserInlineQueryEvent")
fun EventRoute<InlineQueryEvent>.inlineQueryFromUser(
    userId: Long,
    build: EventRoute<InlineQueryEvent>.() -> Unit
): EventRoute<InlineQueryEvent> = select({ if (it.event.inlineQuery.from.id == userId) it else null }, build)

@JvmName("inlineQueryFromUserTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.inlineQueryFromUser(
    userId: Long,
    build: EventRoute<InlineQueryEvent>.() -> Unit
): EventRoute<InlineQueryEvent> = on<InlineQueryEvent> { inlineQueryFromUser(userId, build) }

@JvmName("whenInlineQueryFromUserInlineQueryEvent")
fun EventRoute<InlineQueryEvent>.whenInlineQueryFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) = inlineQueryFromUser(userId) { handle(handler) }

@JvmName("whenInlineQueryFromUserTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenInlineQueryFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) = on<InlineQueryEvent> { whenInlineQueryFromUser(userId, handler) }

@JvmName("chosenInlineResultTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.chosenInlineResult(
    build: EventRoute<ChosenInlineResultEvent>.() -> Unit
): EventRoute<ChosenInlineResultEvent> = on<ChosenInlineResultEvent> { build() }

@JvmName("whenChosenInlineResultTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenChosenInlineResult(
    handler: suspend CoroutineScope.(TelegramBotEventContext<ChosenInlineResultEvent>) -> Unit
) = chosenInlineResult { handle(handler) }

@JvmName("chosenInlineResultWithResultIdTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.chosenInlineResult(
    resultId: String,
    build: EventRoute<ChosenInlineResultEvent>.() -> Unit
): EventRoute<ChosenInlineResultEvent> = on<ChosenInlineResultEvent> {
    select({ if (it.event.chosenInlineResult.resultId == resultId) it else null }, build)
}

@JvmName("whenChosenInlineResultWithResultIdTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenChosenInlineResult(
    resultId: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<ChosenInlineResultEvent>) -> Unit
) = chosenInlineResult(resultId) { handle(handler) }

@JvmName("chosenInlineResultFromUserTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.chosenInlineResultFromUser(
    userId: Long,
    build: EventRoute<ChosenInlineResultEvent>.() -> Unit
): EventRoute<ChosenInlineResultEvent> = on<ChosenInlineResultEvent> {
    select({ if (it.event.chosenInlineResult.from.id == userId) it else null }, build)
}

@JvmName("whenChosenInlineResultFromUserTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenChosenInlineResultFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<ChosenInlineResultEvent>) -> Unit
) = chosenInlineResultFromUser(userId) { handle(handler) }
