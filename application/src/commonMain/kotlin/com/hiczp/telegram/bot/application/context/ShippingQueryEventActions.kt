package com.hiczp.telegram.bot.application.context

import com.hiczp.telegram.bot.protocol.event.ShippingQueryEvent
import com.hiczp.telegram.bot.protocol.model.ShippingOption
import com.hiczp.telegram.bot.protocol.model.answerShippingQuery

suspend fun TelegramBotEventContext<ShippingQueryEvent>.answerShippingQuery(
    ok: Boolean,
    shippingOptions: List<ShippingOption>? = null,
    errorMessage: String? = null,
) = client.answerShippingQuery(
    shippingQueryId = event.shippingQuery.id,
    ok = ok,
    shippingOptions = shippingOptions,
    errorMessage = errorMessage,
)

suspend fun TelegramBotEventContext<ShippingQueryEvent>.answerShippingQueryOk(
    shippingOptions: List<ShippingOption>,
) = answerShippingQuery(
    ok = true,
    shippingOptions = shippingOptions,
)

suspend fun TelegramBotEventContext<ShippingQueryEvent>.answerShippingQueryError(
    errorMessage: String,
) = answerShippingQuery(
    ok = false,
    errorMessage = errorMessage,
)
