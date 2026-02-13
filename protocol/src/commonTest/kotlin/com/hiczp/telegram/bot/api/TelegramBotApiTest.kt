package com.hiczp.telegram.bot.api

import com.hiczp.telegram.bot.api.extension.deleteMessage
import com.hiczp.telegram.bot.api.extension.toFormPart
import com.hiczp.telegram.bot.api.form.sendPhoto
import de.jensklingenberg.ktorfit.Ktorfit
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.coroutines.test.runTest
import kotlinx.io.files.Path
import kotlinx.serialization.json.Json
import kotlin.test.Test

private val logger = KotlinLogging.logger {}
private val webpFilePath = Path("../resources/telegram.webp")

/**
 * Integration tests for Telegram Bot API.
 *
 * These tests require the following environment variables to be set:
 * - [EnvVars.BOT_TOKEN]: Telegram bot token
 * - [EnvVars.TEST_CHAT_ID]: Chat ID to use for sending test messages
 *
 * Tests will fail with an exception if these environment variables are not set.
 */
class TelegramBotApiTest {
    private val telegramBotApi by lazy {
        val botToken = getBotToken()
        checkNotNull(botToken) { "Failed to get ${EnvVars.BOT_TOKEN}" }
        val httpClient = HttpClient(createKtorEngine()) {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(Json(DefaultJson) {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                })
            }
            defaultRequest {
                headers.appendIfNameAbsent(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            }
        }
        Ktorfit.Builder().httpClient(httpClient)
            .baseUrl("https://api.telegram.org/bot${botToken}/")
            .build().createTelegramBotApi()
    }

    private val testChatId by lazy {
        checkNotNull(getTestChatId()) { "Failed to get ${EnvVars.TEST_CHAT_ID}" }
    }

    @Test
    fun getUpdates() = runTest {
        telegramBotApi.getUpdates().getOrThrow().let {
            logger.info { it }
        }
    }

    @Test
    fun sendPhoto() = runTest {
        //upload a new photo
        val message = telegramBotApi.sendPhoto(
            chatId = testChatId,
            photo = webpFilePath.toFormPart("photo", ContentType.Image.WEBP),
        ).getOrThrow()
        telegramBotApi.deleteMessage(message)
        //send photo by fileId
        val fileId = message.photo?.minByOrNull { it.height * it.width }?.fileId
        checkNotNull(fileId) { "No fileId returned from telegram server" }
        val message2 = telegramBotApi.sendPhoto(
            chatId = message.chat.id.toString(),
            photo = fileId
        ).getOrThrow()
        telegramBotApi.deleteMessage(message2)
    }
}
