package com.hiczp.telegram.bot.api.plugin

import com.hiczp.telegram.bot.api.type.TelegramErrorResponse
import io.ktor.client.call.*
import io.ktor.client.plugins.api.*
import io.ktor.http.*

val TelegramServerErrorPlugin = createClientPlugin("TelegramServerErrorPlugin") {
    onResponse { response ->
        if (response.contentType()?.match(ContentType.Application.Json) != true) return@onResponse
        val telegramErrorResponse = response.body<TelegramErrorResponse>()
        val exception = telegramErrorResponse.toTelegramResponse().exceptionOrNull()
        if (exception != null) throw exception
    }
}
