/**
 * Interceptor Bot Sample
 *
 * A Telegram bot demonstrating custom interceptor implementations for cross-cutting concerns
 * including localization, timeout handling, exception catching, and blacklist filtering.
 *
 * ## Usage
 *
 * Run with bot token as the command line argument:
 * ```
 * ./gradlew :sample:jvmRun --args="YOUR_BOT_TOKEN" -DmainClass="com.hiczp.telegram.bot.sample.basic.interceptor.InterceptorBotKt" --quiet
 * ```
 *
 * ## Features Demonstrated
 *
 * - [localizationInterceptor] - Extracts user's language code and stores it in context attributes
 * - [timeoutInterceptor] - Cancels operations that exceed a time limit
 * - [exceptionCatchingInterceptor] - Catches exceptions globally and replies error message to user
 * - [createBlacklistInterceptor] - Blocks users in blacklist and sends error message
 * - Using [banUser] to dynamically add users to blacklist
 * - Accessing interceptor data via [languageCode] extension property
 *
 * ## Available Commands
 *
 * - `/start` - Show help message
 * - `/lang` - Show detected language code
 * - `/slow` - Simulate a slow operation (triggers timeout)
 * - `/crash` - Simulate an exception (triggers exception catcher)
 * - `/ping` - Health check
 * - `/ban` - Ban yourself using the `banUser` extension function
 *
 * @see localizationInterceptor
 * @see timeoutInterceptor
 * @see exceptionCatchingInterceptor
 * @see createBlacklistInterceptor
 * @see banUser
 */
package com.hiczp.telegram.bot.sample.basic.interceptor

import com.hiczp.telegram.bot.application.TelegramBotApplication
import com.hiczp.telegram.bot.application.context.action.replyMessage
import com.hiczp.telegram.bot.application.context.castOrNull
import com.hiczp.telegram.bot.application.context.extractChatId
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerTelegramEventDispatcher
import com.hiczp.telegram.bot.application.dispatcher.handler.command.commandEndpoint
import com.hiczp.telegram.bot.application.dispatcher.handler.handling
import com.hiczp.telegram.bot.application.dispatcher.handler.matcher.onMessageEventPrivateChat
import com.hiczp.telegram.bot.application.interceptor.builtin.logging.loggingInterceptor
import com.hiczp.telegram.bot.protocol.constant.ChatType
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import com.hiczp.telegram.bot.protocol.extension.isCommand
import com.hiczp.telegram.bot.protocol.model.sendMessage
import kotlin.time.Duration.Companion.seconds

private val TelegramBotEvent.isCommandInPrivateChat get() = this is MessageEvent && message.chat.type == ChatType.PRIVATE && message.isCommand

/**
 * InterceptorBot demonstrates custom interceptor implementations:
 * - Localization: Extract user language code for i18n support
 * - Timeout: Prevent long-running operations from blocking the bot
 * - Exception catching: Global error handling with user notification
 * - Blacklist: Block abusive users from using the bot (with dynamic banning via banUser)
 */
private suspend fun runInterceptorBot(botToken: String) {
    // Create the blacklist interceptor
    val blacklistInterceptor = createBlacklistInterceptor(
        blacklist = mutableSetOf(),
        messageFilter = { event -> event.isCommandInPrivateChat },
        onBlocked = { context, _ ->
            // Only handle MessageEvent types
            val context = context.castOrNull<MessageEvent>() ?: return@createBlacklistInterceptor

            // Ignore exception
            runCatching {
                context.replyMessage("🚫 You are blocked from using this bot")
            }
        }
    )

    val eventDispatcher = HandlerTelegramEventDispatcher(handling {
        onMessageEventPrivateChat {
            // Help command
            commandEndpoint("start") {
                replyMessage(
                    """
                    Welcome to InterceptorBot!

                    This bot demonstrates custom interceptor implementations.

                    **Commands:**
                    /start - Show this help
                    /lang - Show your detected language code
                    /slow - Simulate a slow operation (will timeout)
                    /crash - Simulate an exception (will be caught)
                    /ping - Health check
                    /ban - Ban yourself using banUser() (for testing)
                    """.trimIndent()
                )
            }

            // Health check with language info
            commandEndpoint("ping") {
                replyMessage("Pong!")
            }

            // Show detected language code
            commandEndpoint("lang") {
                val lang = languageCode
                if (lang != null) {
                    replyMessage("Your detected language code: `$lang`")
                } else {
                    replyMessage("Could not detect your language code.")
                }
            }

            // Simulate a slow operation - will be cancelled by timeoutInterceptor
            commandEndpoint("slow") {
                replyMessage("Starting slow operation (will timeout in 5 seconds)...")
                kotlinx.coroutines.delay(60.seconds) // This will be cancelled
                replyMessage("Operation completed!") // This won't be reached
            }

            // Simulate an exception - will be caught by exceptionCatchingInterceptor
            commandEndpoint("crash") {
                throw RuntimeException("Simulated crash for testing exceptionCatchingInterceptor")
            }

            // Ban current user using banUser() extension function
            commandEndpoint("ban") {
                val userId = event.message.from?.id
                if (userId != null) {
                    banUser(userId)
                    replyMessage("You have been marked for banning. You will be blocked after this message.")
                } else {
                    replyMessage("Could not determine your user ID.")
                }
            }
        }
    })

    // Configure interceptors in order (outermost to innermost):
    // 1. Logging - logs all events
    // 2. Blacklist - blocks blacklisted users (should be early to prevent any processing)
    // 3. Localization - extracts language code for handlers to use
    // 4. Timeout - cancels slow operations
    // 5. Exception catching - catches all exceptions (should be innermost to catch all errors)
    val interceptors = listOf(
        loggingInterceptor(),
        blacklistInterceptor,  // Use the class instance directly
        localizationInterceptor(),
        timeoutInterceptor(
            timeout = 5.seconds,
            messageFilter = { event -> event.isCommandInPrivateChat }
        ) { context, _ ->
            val chatId = context.event.extractChatId()
            if (chatId != null) {
                runCatching {
                    context.client.sendMessage(
                        chatId = chatId.toString(),
                        text = "⏱️ Operation timed out. Please try again."
                    )
                }
            }
        },
        exceptionCatchingInterceptor(
            messageFilter = { event -> event.isCommandInPrivateChat },
        ) { context, error ->
            val chatId = context.event.extractChatId()
            if (chatId != null) {
                runCatching {
                    context.client.sendMessage(
                        chatId = chatId.toString(),
                        text = "❌ An error occurred: ${error.message}"
                    )
                }
            }
        }
    )

    val app = TelegramBotApplication.longPolling(
        botToken = botToken,
        eventDispatcher = eventDispatcher,
        interceptors = interceptors,
    )

    println("Starting interceptor bot...")
    println("Send /start to see available commands.")
    app.start()
    app.join()
}

suspend fun main(args: Array<String>) {
    val botToken = args.firstOrNull() ?: error("Bot token is required")
    runInterceptorBot(botToken)
}
