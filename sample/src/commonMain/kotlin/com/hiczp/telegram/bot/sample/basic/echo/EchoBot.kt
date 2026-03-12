/**
 * Echo Bot Sample
 *
 * A simple Telegram bot that echoes back any text message sent to it in private chats.
 * This sample demonstrates the basic usage of the Telegram Bot Application framework
 * with [SimpleTelegramEventDispatcher] for straightforward event handling.
 *
 * ## Usage
 *
 * Run with bot token as the command line argument:
 * ```
 * ./gradlew :sample:jvmRun --args="YOUR_BOT_TOKEN" -DmainClass="com.hiczp.telegram.bot.sample.basic.echo.EchoBotKt" --quiet
 * ```
 *
 * ## Features Demonstrated
 *
 * - Long polling for updates via [TelegramBotApplication.longPolling]
 * - Simple event dispatching with [SimpleTelegramEventDispatcher]
 * - Filtering by chat type (private chats only)
 * - Sending messages using the [sendMessage] extension function
 *
 * @see TelegramBotApplication
 * @see SimpleTelegramEventDispatcher
 */
package com.hiczp.telegram.bot.sample.basic.echo

import com.hiczp.telegram.bot.application.TelegramBotApplication
import com.hiczp.telegram.bot.application.dispatcher.SimpleTelegramEventDispatcher
import com.hiczp.telegram.bot.protocol.constant.ChatType
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.model.sendMessage
import kotlin.time.Instant

/**
 * Runs an echo bot that replies to private chat messages with the same text.
 *
 * This function creates and starts a long-polling Telegram bot application.
 * The bot will:
 * 1. Listen for incoming messages via long polling
 * 2. Filter for messages from private chats only
 * 3. Echo back any non-null text messages to the sender
 *
 * The bot runs indefinitely until stopped (e.g., via Ctrl+C or calling [TelegramBotApplication.stop]).
 *
 * @param botToken The Telegram bot token obtained from [@BotFather](https://t.me/botfather)
 * @throws IllegalStateException if the bot fails to start
 *
 * @see TelegramBotApplication.longPolling
 * @see SimpleTelegramEventDispatcher
 */
private suspend fun runEchoBot(botToken: String) {
    // Create a simple event dispatcher that handles incoming updates
    val eventDispatcher = SimpleTelegramEventDispatcher { context ->
        val event = context.event

        // Only process private chat messages
        if (event is MessageEvent && event.message.chat.type == ChatType.PRIVATE) {
            val message = event.message
            val text = message.text

            // Echo back non-null text messages
            if (text != null) {
                println("Received message[${Instant.fromEpochSeconds(message.date)}]: $text from user ${message.chat.username}[${message.chat.id}]")

                // Send the same text back to the user
                context.client.sendMessage(
                    chatId = message.chat.id.toString(),
                    text = text
                )
            }
        }
    }

    // Create a long-polling bot application
    val app = TelegramBotApplication.longPolling(
        botToken = botToken,
        eventDispatcher = eventDispatcher
    )
    // Or create a webhook-based bot application (requires application-updatesource-webhook dependency)
    // val app = TelegramBotApplication.webhook(
    //     botToken = botToken,
    //     eventDispatcher = eventDispatcher,
    //     path = "/webhook",
    //     configureEngine = {
    //         port = 8443
    //     }
    // )

    println("Starting echo bot...")
    app.start()  // Start the bot
    app.join()   // Suspend until the bot is stopped
}

suspend fun main(args: Array<String>) {
    val botToken = args.firstOrNull() ?: error("Bot token is required")
    runEchoBot(botToken)
}
