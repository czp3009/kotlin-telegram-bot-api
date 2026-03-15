package com.hiczp.telegram.bot.application.compose.message

import androidx.compose.runtime.Composable
import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.context.extractChatId
import com.hiczp.telegram.bot.application.context.extractThreadId
import com.hiczp.telegram.bot.protocol.TelegramBotApi
import kotlinx.coroutines.CoroutineScope

/**
 * Composes a Telegram message with text and optional keyboard markup.
 *
 * This is the main entry point for using the Compose message DSL.
 * It creates a composition that tracks the message state and automatically
 * updates the Telegram message when the composition changes.
 *
 * Example:
 * ```kotlin
 * val session = composeMessage(
 *     chatId = 123456,
 *     client = telegramBotApi,
 *     scope = coroutineScope
 * ) {
 *     var count by remember { mutableStateOf(0) }
 *
 *     Text("Count: $count")
 *     InlineKeyboard {
 *         Row {
 *             Button("-") { count-- }
 *             Button("+") { count++ }
 *         }
 *     }
 * }
 *
 * // Later, to clean up:
 * session.dispose()
 * ```
 *
 * @param chatId The chat ID to send the message to.
 * @param threadId The thread/topic ID within the chat, or null for main chat.
 * @param client The Telegram Bot API client.
 * @param scope The coroutine scope for async operations.
 * @param content The composable content defining the message.
 * @return A [ComposeMessageSession] that manages the message lifecycle.
 */
fun composeMessage(
    chatId: Long,
    threadId: Long? = null,
    client: TelegramBotApi,
    scope: CoroutineScope,
    content: @Composable ComposeMessageScope.() -> Unit,
): ComposeMessageSession {
    val id = ComposeMessageId(chatId, threadId)
    val session = ComposeMessageSession(id, client, scope)
    session.start(content)
    return session
}

/**
 * Extension function to start a compose message session from a [TelegramBotEventContext].
 *
 * This extracts the chatId and threadId from the current event context automatically.
 *
 * Example:
 * ```kotlin
 * commandEndpoint("counter") {
 *     composeMessage {
 *         var count by remember { mutableStateOf(0) }
 *
 *         Text("Count: $count")
 *         InlineKeyboard {
 *             Row {
 *                 Button("-") { count-- }
 *                 Button("+") { count++ }
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * Note: [composeMessageInterceptor] must be installed for button callbacks to work.
 *
 * @param content The composable content defining the message.
 * @return A [ComposeMessageSession] that manages the message lifecycle.
 */
fun TelegramBotEventContext<*>.composeMessage(
    content: @Composable ComposeMessageScope.() -> Unit,
): ComposeMessageSession {
    val chatId = event.extractChatId() ?: error("Cannot extract chatId from event")
    val threadId = event.extractThreadId()

    return composeMessage(
        chatId = chatId,
        threadId = threadId,
        client = client,
        scope = applicationScope,
        content = content
    )
}
