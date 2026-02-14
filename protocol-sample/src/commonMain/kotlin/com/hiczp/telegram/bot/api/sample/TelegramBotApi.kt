package com.hiczp.telegram.bot.api.sample

import com.hiczp.telegram.bot.api.createTelegramBotApi
import com.hiczp.telegram.bot.api.plugin.TelegramFileDownloadPlugin
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.serialization.json.Json

private val telegramBotApi by lazy {
    val yourBotToken = "123456"
    val httpClient = HttpClient(CIO) {
        install(TelegramFileDownloadPlugin)
        install(ContentNegotiation) {
            json(Json(DefaultJson) {
                ignoreUnknownKeys = true
                encodeDefaults = true
            })
        }
        defaultRequest {
            headers.appendIfNameAbsent(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        }
        expectSuccess = true
    }
    Ktorfit.Builder().httpClient(httpClient)
        .baseUrl("https://api.telegram.org/bot${yourBotToken}/")
        .build().createTelegramBotApi()
}
