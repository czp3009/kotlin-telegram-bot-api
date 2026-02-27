package com.hiczp.telegram.bot.application.context.action

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.protocol.event.PreCheckoutQueryEvent
import com.hiczp.telegram.bot.protocol.model.answerPreCheckoutQuery

suspend fun TelegramBotEventContext<PreCheckoutQueryEvent>.answerPreCheckoutQuery(
    ok: Boolean,
    errorMessage: String? = null,
) = client.answerPreCheckoutQuery(
    preCheckoutQueryId = event.preCheckoutQuery.id,
    ok = ok,
    errorMessage = errorMessage,
)

suspend fun TelegramBotEventContext<PreCheckoutQueryEvent>.answerPreCheckoutQueryOk() =
    answerPreCheckoutQuery(ok = true)

suspend fun TelegramBotEventContext<PreCheckoutQueryEvent>.answerPreCheckoutQueryError(
    errorMessage: String,
) = answerPreCheckoutQuery(ok = false, errorMessage = errorMessage)
