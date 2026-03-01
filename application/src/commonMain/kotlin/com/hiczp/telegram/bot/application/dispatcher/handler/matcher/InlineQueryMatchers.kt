package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.EventRoute
import com.hiczp.telegram.bot.protocol.event.ChosenInlineResultEvent
import com.hiczp.telegram.bot.protocol.event.InlineQueryEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlinx.coroutines.CoroutineScope

/**
 * Registers a handler for inline queries with specific query text.
 *
 * @param query The query text to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching inline query.
 */
fun EventRoute<InlineQueryEvent>.whenInlineQuery(
    query: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) {
    select({ if (it.event.inlineQuery.query == query) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers an inline query handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenInlineQuery(
    query: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) = on<InlineQueryEvent> { whenInlineQuery(query, handler) }

/**
 * Registers a handler for inline queries whose text matches a regular expression.
 *
 * @param pattern The regular expression pattern to match against query text.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching inline query.
 */
fun EventRoute<InlineQueryEvent>.whenInlineQueryRegex(
    pattern: Regex,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) {
    select({ if (it.event.inlineQuery.query.matches(pattern)) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers an inline query regex handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenInlineQueryRegex(
    pattern: Regex,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) = on<InlineQueryEvent> { whenInlineQueryRegex(pattern, handler) }

/**
 * Registers a handler for inline queries whose text contains a specific substring.
 *
 * @param substring The substring to search for in the query text.
 * @param ignoreCase Whether the search should be case-insensitive. Default is false.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching inline query.
 */
fun EventRoute<InlineQueryEvent>.whenInlineQueryContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) {
    select({ if (it.event.inlineQuery.query.contains(substring, ignoreCase)) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers an inline query contains handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenInlineQueryContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) = on<InlineQueryEvent> { whenInlineQueryContains(substring, ignoreCase, handler) }

/**
 * Registers a handler for inline queries whose text starts with a specific prefix.
 *
 * @param prefix The prefix to match at the start of the query text.
 * @param ignoreCase Whether the comparison should be case-insensitive. Default is false.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching inline query.
 */
fun EventRoute<InlineQueryEvent>.whenInlineQueryStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) {
    select({ if (it.event.inlineQuery.query.startsWith(prefix, ignoreCase)) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers an inline query starts with handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenInlineQueryStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) = on<InlineQueryEvent> { whenInlineQueryStartsWith(prefix, ignoreCase, handler) }

/**
 * Registers a handler for inline queries from a specific user.
 *
 * @param userId The Telegram user ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the inline query.
 */
fun EventRoute<InlineQueryEvent>.whenInlineQueryFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) {
    select({ if (it.event.inlineQuery.from.id == userId) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers an inline query from user handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenInlineQueryFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) = on<InlineQueryEvent> { whenInlineQueryFromUser(userId, handler) }


/**
 * Registers a handler for chosen inline result events.
 *
 * Triggered when a user selects a result from an inline query.
 * Useful for collecting feedback on inline mode usage.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenChosenInlineResult(
    handler: suspend CoroutineScope.(TelegramBotEventContext<ChosenInlineResultEvent>) -> Unit
) = on<ChosenInlineResultEvent> { handle(handler) }

/**
 * Registers a handler for chosen inline results with a specific result ID.
 *
 * @param resultId The result ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenChosenInlineResult(
    resultId: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<ChosenInlineResultEvent>) -> Unit
) = on<ChosenInlineResultEvent> {
    select({ if (it.event.chosenInlineResult.resultId == resultId) it else null }) {
        handle(handler)
    }
}

/**
 * Registers a handler for chosen inline results from a specific user.
 *
 * @param userId The Telegram user ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenChosenInlineResultFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<ChosenInlineResultEvent>) -> Unit
) = on<ChosenInlineResultEvent> {
    select({ if (it.event.chosenInlineResult.from.id == userId) it else null }) {
        handle(handler)
    }
}
