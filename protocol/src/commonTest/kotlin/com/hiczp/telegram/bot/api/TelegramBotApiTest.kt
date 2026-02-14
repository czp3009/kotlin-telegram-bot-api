package com.hiczp.telegram.bot.api

import com.hiczp.telegram.bot.api.extension.deleteMessage
import com.hiczp.telegram.bot.api.extension.deleteMessages
import com.hiczp.telegram.bot.api.extension.toFormPart
import com.hiczp.telegram.bot.api.form.sendMediaGroup
import com.hiczp.telegram.bot.api.form.sendPhoto
import com.hiczp.telegram.bot.api.form.sendSticker
import com.hiczp.telegram.bot.api.model.InputMediaPhoto
import com.hiczp.telegram.bot.api.model.Sticker
import com.hiczp.telegram.bot.api.plugin.TelegramFileDownloadPlugin
import de.jensklingenberg.ktorfit.Ktorfit
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.statement.*
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
        val httpClient = HttpClient(createKtorEngine()) {
            install(TelegramFileDownloadPlugin)
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
            expectSuccess = true
        }
        Ktorfit.Builder().httpClient(httpClient)
            .baseUrl("https://api.telegram.org/bot${TestEnv.botToken}/")
            .build().createTelegramBotApi()
    }

    private val testChatId by lazy { TestEnv.testChatId }

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
            photo = fileId,
            caption = "test",
        ).getOrThrow()
        telegramBotApi.deleteMessage(message2)
    }

    @Test
    fun sendPhotos() = runTest {
        val messages = telegramBotApi.sendMediaGroup(
            chatId = testChatId,
            media = listOf(
                InputMediaPhoto(media = "attach://photo1"),
                InputMediaPhoto(media = "attach://photo2"),
            ),
            attachments = listOf(
                webpFilePath.toFormPart("photo1", ContentType.Image.WEBP),
                webpFilePath.toFormPart("photo2", ContentType.Image.WEBP),
            ),
        ).getOrThrow()
        telegramBotApi.deleteMessages(messages)
    }

    @Test
    fun sendSticker() = runTest {
        val message = telegramBotApi.sendSticker(
            chatId = testChatId,
            sticker = getTestSticker().fileId,
        ).getOrThrow()
        telegramBotApi.deleteMessage(message)
    }

    @Test
    fun downloadFile() = runTest {
        val file = telegramBotApi.getFile(getTestSticker().fileId).getOrThrow()
        logger.info { "Sticker file: $file" }
        checkNotNull(file.filePath) { "No filePath returned from telegram server" }
        telegramBotApi.downloadFile(file.filePath).execute {
            val body = it.bodyAsBytes()
            logger.info { "Downloaded size: ${body.size}" }
        }
    }

    private lateinit var testSticker: Sticker
    private suspend fun getTestSticker(): Sticker {
        if (!::testSticker.isInitialized) {
            @Suppress("SpellCheckingInspection")
            testSticker = telegramBotApi.getStickerSet("pipagck").getOrThrow().stickers.first()
        }
        return testSticker
    }
}
