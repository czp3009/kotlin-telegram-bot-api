package com.hiczp.telegram.bot.api

import com.hiczp.telegram.bot.api.exception.TelegramErrorResponseException
import com.hiczp.telegram.bot.api.exception.isRetryable
import com.hiczp.telegram.bot.api.extension.deleteMessage
import com.hiczp.telegram.bot.api.extension.deleteMessages
import com.hiczp.telegram.bot.api.extension.toFormPart
import com.hiczp.telegram.bot.api.extension.toInputFile
import com.hiczp.telegram.bot.api.form.sendDocument
import com.hiczp.telegram.bot.api.form.sendMediaGroup
import com.hiczp.telegram.bot.api.form.sendPhoto
import com.hiczp.telegram.bot.api.form.sendSticker
import com.hiczp.telegram.bot.api.model.InputMediaPhoto
import com.hiczp.telegram.bot.api.model.Sticker
import com.hiczp.telegram.bot.api.plugin.TelegramFileDownloadPlugin
import com.hiczp.telegram.bot.api.plugin.TelegramLongPollingPlugin
import com.hiczp.telegram.bot.api.plugin.TelegramServerErrorPlugin
import com.hiczp.telegram.bot.api.type.InputFile
import de.jensklingenberg.ktorfit.Ktorfit
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.statement.*
import io.ktor.client.utils.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import io.ktor.utils.io.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.serialization.json.Json
import kotlin.test.Test

private val logger = KotlinLogging.logger {}
private val webpFilePath = Path("../resources/telegram.webp")
private val txtFilePath = Path("../resources/telegram.txt")

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
            install(DefaultRequest) {
                headers.appendIfNameAbsent(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            }
            install(HttpRequestRetry) {
                retryOnExceptionIf(maxRetries = 3) { _, throwable ->
                    val cause = throwable.unwrapCancellationException()
                    if (cause is HttpRequestTimeoutException || cause is ConnectTimeoutException || cause is SocketTimeoutException) return@retryOnExceptionIf true
                    if (cause is TelegramErrorResponseException) return@retryOnExceptionIf cause.isRetryable
                    if (throwable is CancellationException) return@retryOnExceptionIf false
                    true
                }
                delayMillis {
                    (cause as? TelegramErrorResponseException)?.parameters?.retryAfter?.times(1000) ?: 1_000
                }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
                connectTimeoutMillis = 10_000
                socketTimeoutMillis = 10_000
            }
            install(TelegramLongPollingPlugin)
            install(TelegramFileDownloadPlugin)
            install(TelegramServerErrorPlugin)
        }
        Ktorfit.Builder().httpClient(httpClient)
            .baseUrl("https://api.telegram.org/bot${TestEnv.botToken}/")
            .build().createTelegramBotApi()
    }

    private val testChatId by lazy { TestEnv.testChatId }

    @Test
    fun getMe() = runTest {
        telegramBotApi.getMe()
    }

//    @Ignore
//    @Test
//    fun close() = runTest {
//        telegramBotApi.close()
//    }

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
            photo = webpFilePath.toInputFile(),
        ).getOrThrow()
        telegramBotApi.deleteMessage(message)
        //send photo by fileId
        val fileId = message.photo?.minByOrNull { it.height * it.width }?.fileId
        checkNotNull(fileId) { "No fileId returned from telegram server" }
        val message2 = telegramBotApi.sendPhoto(
            chatId = message.chat.id.toString(),
            photo = InputFile.reference(fileId),
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
                webpFilePath.toFormPart("photo1"),
                webpFilePath.toFormPart("photo2"),
            ),
        ).getOrThrow()
        telegramBotApi.deleteMessages(messages)
    }

    @Test
    fun sendSticker() = runTest {
        telegramBotApi.sendSticker(
            chatId = testChatId,
            sticker = InputFile.reference(getTestSticker().fileId),
        ).getOrThrow().let {
            telegramBotApi.deleteMessage(it)
        }
    }

    @Test
    fun sendWebpDocument() = runTest {
        telegramBotApi.sendDocument(
            chatId = testChatId,
            document = webpFilePath.toInputFile(),
        ).getOrThrow().let {
            telegramBotApi.deleteMessage(it)
        }
    }

    @Test
    fun sendTxtDocument() = runTest {
        telegramBotApi.sendDocument(
            chatId = testChatId,
            document = txtFilePath.toInputFile(),
        ).getOrThrow().let {
            telegramBotApi.deleteMessage(it)
        }
    }

    @Test
    fun sendTxtDocumentWithoutFileName() = runTest {
        telegramBotApi.sendDocument(
            chatId = testChatId,
            document = InputFile.binary { ByteReadChannel(SystemFileSystem.source(txtFilePath).buffered()) },
        ).getOrThrow().let {
            telegramBotApi.deleteMessage(it)
        }
    }

    @Test
    fun downloadFile() = runTest {
        val file = telegramBotApi.getFile(getTestSticker().fileId).getOrThrow()
        logger.info { "Sticker file: $file" }
        checkNotNull(file.filePath) { "No filePath returned from telegram server" }
        telegramBotApi.downloadFile(file.filePath).execute {
            val byteReadChannel = it.bodyAsChannel()
            var totalBytes = 0L
            val buffer = ByteArray(1024) // Define a buffer size for efficient reading
            while (!byteReadChannel.exhausted()) {
                val bytesRead = byteReadChannel.readAvailable(buffer)
                totalBytes += bytesRead
            }
            logger.info { "Downloaded size: $totalBytes" }
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
