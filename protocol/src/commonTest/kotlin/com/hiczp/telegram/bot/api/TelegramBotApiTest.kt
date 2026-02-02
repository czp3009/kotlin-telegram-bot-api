package com.hiczp.telegram.bot.api

import de.jensklingenberg.ktorfit.Ktorfit
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test

private val logger = KotlinLogging.logger {}

private val telegramBotApi by lazy {
    val botToken = getBotToken()
    checkNotNull(botToken) { "Failed to get BOT_TOKEN" }
    val httpClient = HttpClient(createKtorEngine()) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json(DefaultJson) {
                ignoreUnknownKeys = true
            })
        }
    }
    Ktorfit.Builder().httpClient(httpClient)
        .baseUrl("https://api.telegram.org/bot${botToken}/")
        .build().createTelegramBotApi()
}

class TelegramBotApiTest {
    @Test
    fun getUpdates() = runTest {
        val response = telegramBotApi.getUpdates()
        logger.info { response.result }
    }
}
