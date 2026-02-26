package com.hiczp.telegram.bot.application.context

import com.hiczp.telegram.bot.protocol.event.InlineQueryEvent
import com.hiczp.telegram.bot.protocol.model.InlineQueryResult
import com.hiczp.telegram.bot.protocol.model.InlineQueryResultsButton
import com.hiczp.telegram.bot.protocol.model.answerInlineQuery

suspend fun TelegramBotEventContext<InlineQueryEvent>.answerInlineQuery(
    results: List<InlineQueryResult>,
    cacheTime: Long? = null,
    isPersonal: Boolean? = null,
    nextOffset: String? = null,
    button: InlineQueryResultsButton? = null,
) = client.answerInlineQuery(
    inlineQueryId = event.inlineQuery.id,
    results = results,
    cacheTime = cacheTime,
    isPersonal = isPersonal,
    nextOffset = nextOffset,
    button = button,
)
