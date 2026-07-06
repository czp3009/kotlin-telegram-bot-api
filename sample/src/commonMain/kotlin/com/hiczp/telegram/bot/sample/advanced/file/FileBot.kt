/**
 * File Bot Sample
 *
 * A Telegram bot demonstrating file upload and download capabilities.
 *
 * ## Usage
 *
 * Run with bot token as the command line argument:
 * ```
 * ./gradlew :sample:jvmRun --args="YOUR_BOT_TOKEN" -DmainClass="com.hiczp.telegram.bot.sample.advanced.file.FileBotKt" --quiet
 * ```
 */
package com.hiczp.telegram.bot.sample.advanced.file

import com.hiczp.telegram.bot.application.TelegramBotApplication
import com.hiczp.telegram.bot.application.context.action.replyMessage
import com.hiczp.telegram.bot.application.context.action.replyPhoto
import com.hiczp.telegram.bot.application.context.action.sendChatAction
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerTelegramEventDispatcher
import com.hiczp.telegram.bot.application.dispatcher.handler.command.command
import com.hiczp.telegram.bot.application.dispatcher.handler.handling
import com.hiczp.telegram.bot.application.dispatcher.handler.matcher.message
import com.hiczp.telegram.bot.application.dispatcher.handler.matcher.photo
import com.hiczp.telegram.bot.application.dispatcher.handler.matcher.privateChat
import com.hiczp.telegram.bot.application.dispatcher.handler.matcher.sticker
import com.hiczp.telegram.bot.protocol.constant.ChatAction
import com.hiczp.telegram.bot.protocol.extension.asInputFile
import com.hiczp.telegram.bot.protocol.extension.largestPhoto
import com.hiczp.telegram.bot.protocol.type.InputFile
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.launch
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

/**
 * FileBot demonstrates file operations with Telegram.
 */
private suspend fun runFileBot(botToken: String) {
    val eventDispatcher = HandlerTelegramEventDispatcher(handling {
        message {
            privateChat {
            // Help command
                command("start") {
                    handle {
                        replyMessage(
                            """
                    Welcome to FileBot!
    
                    **Commands:**
                    /start - Show this help
                    /photo - Send a sample photo (telegram.jpg)
                    /sticker - When you send a sticker, I'll echo back its image
    
                    **Auto-echo:**
                    - Photo -> echo photo
                    - Document -> echo document
                    - Video -> echo video
                    - Audio -> echo audio
                    - Animation -> echo animation
                    """.trimIndent()
                        )
                    }
                }

            // Send a local file as photo
                command("photo") {
                    handle {
                        val imagePath = Path("../resources/telegram.jpg")
                        try {
                            val inputFile = InputFile.binary(
                                content = ChannelProvider {
                                    ByteReadChannel(SystemFileSystem.source(imagePath).buffered())
                                },
                                fileName = imagePath.name,
                                contentType = ContentType.Image.JPEG,
                            )
                            replyPhoto(
                                photo = inputFile,
                                caption = "Here's telegram.jpg from local resources!"
                            )
                        } catch (e: Throwable) {
                            replyMessage("Error loading file: ${e.message}")
                        }
                    }
                }

            // Echo sticker - send back the sticker photo
                sticker {
                    handle {
                        val sticker = event.message.sticker!!
                        // Send chat action to provide user feedback during processing
                        launch { sendChatAction(ChatAction.TYPING) }
                        val filePath = client.getFile(sticker.fileId).getOrThrow().filePath
                        checkNotNull(filePath) { "Can't resolve filePath for sticker: ${sticker.fileId}" }
                        client.downloadFile(filePath).execute { response ->
                            val byteReadChannel = response.bodyAsChannel()
                            replyPhoto(
                                photo = InputFile.binary { byteReadChannel },
                                caption = "Sticker echoed!\nFile ID: ${sticker.fileId}",
                            )
                        }
                    }
                }

            // Echo photo
                photo {
                    handle {
                        val photo = event.message.largestPhoto
                        if (photo != null) {
                            replyPhoto(
                                photo = photo.asInputFile(),
                                caption = "Echoed photo!\nFile ID: ${photo.fileId}"
                            )
                        } else {
                            replyMessage("No available photo resolution in this message")
                        }
                    }
                }
            }
        }

        // Dead letter handler
        handle {
            println("Unhandled event: $event")
        }
    })

    val app = TelegramBotApplication.longPolling(
        botToken = botToken,
        eventDispatcher = eventDispatcher,
    )

    println("Starting file bot...")
    println("Send /start to see available commands.")
    app.start()
    app.join()
}

suspend fun main(args: Array<String>) {
    val botToken = args.firstOrNull() ?: error("Bot token is required")
    runFileBot(botToken)
}
