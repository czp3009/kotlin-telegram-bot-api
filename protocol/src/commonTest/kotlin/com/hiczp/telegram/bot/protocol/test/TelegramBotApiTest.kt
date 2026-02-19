package com.hiczp.telegram.bot.protocol.test

import com.hiczp.telegram.bot.protocol.createTelegramBotApi
import com.hiczp.telegram.bot.protocol.exception.TelegramErrorResponseException
import com.hiczp.telegram.bot.protocol.exception.isRetryable
import com.hiczp.telegram.bot.protocol.extension.deleteMessage
import com.hiczp.telegram.bot.protocol.extension.deleteMessages
import com.hiczp.telegram.bot.protocol.form.*
import com.hiczp.telegram.bot.protocol.model.*
import com.hiczp.telegram.bot.protocol.plugin.TelegramFileDownloadPlugin
import com.hiczp.telegram.bot.protocol.plugin.TelegramLongPollingPlugin
import com.hiczp.telegram.bot.protocol.plugin.TelegramServerErrorPlugin
import com.hiczp.telegram.bot.protocol.test.extension.toFormPart
import com.hiczp.telegram.bot.protocol.test.extension.toInputFile
import com.hiczp.telegram.bot.protocol.type.InputFile
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
import kotlin.test.*

private val logger = KotlinLogging.logger {}
private val webpFilePath = Path("../resources/telegram.webp")
private val jpgFilePath = Path("../resources/telegram.jpg")
private val gifFilePath = Path("../resources/telegram.gif")
private val mp4FilePath = Path("../resources/telegram.mp4")
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
                    explicitNulls = false
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

    @AfterTest
    fun slowDown() {
        sleepMillis(1_000)
    }
    
    @Test
    fun getMe() = runTest {
        val me = telegramBotApi.getMe().getOrThrow()
        assertTrue(me.isBot)
    }

    @Ignore
    @Test
    fun close() = runTest {
        val isClosed = telegramBotApi.close().getOrThrow()
        assertTrue(isClosed)
    }

    @Test
    fun getUpdates() = runTest {
        val updates = telegramBotApi.getUpdates().result
        assertNotNull(updates)
        logger.info { updates }
    }

    @Test
    fun sendMessage() = runTest {
        val message = telegramBotApi.sendMessage(
            SendMessageRequest(
                chatId = testChatId,
                text = "Test message",
            )
        ).getOrThrow()
        assertTrue(telegramBotApi.deleteMessage(message).getOrThrow())
    }

    @Test
    fun sendChatAction() = runTest {
        val sent = telegramBotApi.sendChatAction(
            SendChatActionRequest(
                chatId = testChatId,
                action = "upload_photo",
            )
        ).getOrThrow()
        assertTrue(sent)
    }

    @Test
    fun getChat() = runTest {
        val chat = telegramBotApi.getChat(testChatId).getOrThrow()
        assertEquals(testChatId, chat.id.toString(), "Unexpected chat id")
    }

    @Test
    fun setMyCommands() = runTest {
        val command = BotCommand(command = "test", description = "test command")
        val setResult = telegramBotApi.setMyCommands(
            SetMyCommandsRequest(commands = listOf(command)),
        ).getOrThrow()
        assertTrue(setResult)
        val commands = telegramBotApi.getMyCommands().getOrThrow()
        assertTrue(commands.any { it.command == "test" }, "Expected /test in bot commands, actual: $commands")
        val deleteResult = telegramBotApi.deleteMyCommands(
            DeleteMyCommandsRequest(),
        ).getOrThrow()
        assertTrue(deleteResult)
    }

    //ignore due to can't roll back bot profile photo
    @Ignore
    @Test
    fun setMyProfilePhoto() = runTest {
        val result = telegramBotApi.setMyProfilePhoto(
            photo = InputProfilePhotoStatic(photo = "attach://photo1"),
            attachments = listOf(jpgFilePath.toFormPart("photo1")),
        ).getOrThrow()
        assertTrue(result)
        //may cause BOT_FALLBACK_UNSUPPORTED
        //telegramBotApi.removeMyProfilePhoto()
    }

    @Test
    fun sendPhoto() = runTest {
        //upload a new photo
        val message = telegramBotApi.sendPhoto(
            chatId = testChatId,
            photo = webpFilePath.toInputFile(),
        ).getOrThrow()
        assertTrue(telegramBotApi.deleteMessage(message).getOrThrow())
        sleepMillis(1_000)
        //send photo by fileId
        val fileId = message.photo?.minByOrNull { it.height * it.width }?.fileId
        assertNotNull(fileId, "No fileId returned from telegram server")
        val message2 = telegramBotApi.sendPhoto(
            chatId = message.chat.id.toString(),
            photo = InputFile.reference(fileId),
            caption = "test",
        ).getOrThrow()
        assertTrue(telegramBotApi.deleteMessage(message2).getOrThrow())
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
        assertTrue(telegramBotApi.deleteMessages(messages).getOrThrow())
    }

    @Test
    fun sendSticker() = runTest {
        val message = telegramBotApi.sendSticker(
            chatId = testChatId,
            sticker = InputFile.reference(getTestSticker().fileId),
        ).getOrThrow()
        assertTrue(telegramBotApi.deleteMessage(message).getOrThrow())
    }

    @Test
    fun sendWebpDocument() = runTest {
        val message = telegramBotApi.sendDocument(
            chatId = testChatId,
            document = webpFilePath.toInputFile(),
        ).getOrThrow()
        assertTrue(telegramBotApi.deleteMessage(message).getOrThrow())
    }

    @Test
    fun sendTxtDocument() = runTest {
        val message = telegramBotApi.sendDocument(
            chatId = testChatId,
            document = txtFilePath.toInputFile(),
        ).getOrThrow()
        assertTrue(telegramBotApi.deleteMessage(message).getOrThrow())
    }

    @Test
    fun sendTxtDocumentWithoutFileName() = runTest {
        val message = telegramBotApi.sendDocument(
            chatId = testChatId,
            document = InputFile.binary { ByteReadChannel(SystemFileSystem.source(txtFilePath).buffered()) },
        ).getOrThrow()
        assertTrue(telegramBotApi.deleteMessage(message).getOrThrow())
    }

    @Test
    fun sendAnimation() = runTest {
        val message = telegramBotApi.sendAnimation(
            chatId = testChatId,
            animation = gifFilePath.toInputFile(),
        ).getOrThrow()
        assertTrue(telegramBotApi.deleteMessage(message).getOrThrow())
    }

    @Test
    fun sendVideo() = runTest {
        val message = telegramBotApi.sendVideo(
            chatId = testChatId,
            video = mp4FilePath.toInputFile(),
        ).getOrThrow()
        assertTrue(telegramBotApi.deleteMessage(message).getOrThrow())
    }

    @Test
    fun downloadFile() = runTest {
        val file = telegramBotApi.getFile(getTestSticker().fileId).getOrThrow()
        logger.info { "Sticker file: $file" }
        val filePath = assertNotNull(file.filePath, "No filePath returned from telegram server")
        telegramBotApi.downloadFile(filePath).execute {
            val byteReadChannel = it.bodyAsChannel()
            var totalBytes = 0L
            val buffer = ByteArray(1024) // Define a buffer size for efficient reading
            while (!byteReadChannel.exhausted()) {
                val bytesRead = byteReadChannel.readAvailable(buffer)
                totalBytes += bytesRead
            }
            logger.info { "Downloaded size: $totalBytes" }
            assertTrue(totalBytes > 0, "Downloaded file should not be empty")
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
