package com.hiczp.telegram.bot.api

import de.jensklingenberg.ktorfit.Ktorfit
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

private val logger = KotlinLogging.logger {}

private val telegramBotApi by lazy {
    val botToken = getBotToken()
    checkNotNull(botToken) { "BOT_TOKEN environment variable is not set" }
    val httpClient = HttpClient(ktorEngine()) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json()
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
