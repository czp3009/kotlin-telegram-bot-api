package com.hiczp.telegram.bot.application.interceptor.builtin.conversation

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.context.extractChatId
import com.hiczp.telegram.bot.application.context.extractThreadId
import com.hiczp.telegram.bot.application.context.extractUserId
import com.hiczp.telegram.bot.application.interceptor.TelegramEventInterceptor
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.util.*
import io.ktor.util.collections.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel

private val logger = KotlinLogging.logger("ConversationInterceptor")

/**
 * Represents a unique identifier for a conversation session.
 *
 * Used to route events to the correct active conversation. The ID is composed of:
 * - [chatId]: The chat where the conversation takes place (required)
 * - [userId]: The user participating in the conversation (optional, for per-user conversations in group chats)
 * - [threadId]: The thread/topic within a chat (optional, for forum topics or message threads)
 *
 * Common patterns:
 * - Whole chat: `ConversationId(chatId = 123)` - all users share one conversation
 * - User in chat: `ConversationId(chatId = 123, userId = 456)` - per-user conversation in group
 * - Thread in chat: `ConversationId(chatId = 123, threadId = 789)` - whole thread shares conversation
 * - User in thread: `ConversationId(chatId = 123, threadId = 789, userId = 456)` - per-user in thread
 *
 * @property chatId The chat ID where the conversation takes place.
 * @property threadId The thread/topic ID within the chat, or null for main chat conversations.
 * @property userId The user ID participating in the conversation, or null for chat-wide conversations.
 */
data class ConversationId(
    val chatId: Long,
    val threadId: Long? = null,
    val userId: Long? = null,
)

/**
 * Holds the state for an active conversation.
 *
 * @property channel The channel that receives event contexts for this conversation.
 * @property interceptPredicate A suspending function that determines which event contexts should be routed to this conversation.
 *         Receives the full [TelegramBotEventContext], allowing access to client, attributes, and other context data.
 */
data class ConversationRecord(
    val channel: Channel<TelegramBotEventContext<TelegramBotEvent>>,
    val interceptPredicate: suspend (TelegramBotEventContext<TelegramBotEvent>) -> Boolean,
) : AutoCloseable {
    override fun close() {
        channel.cancel()
    }
}

/**
 * Manages active conversations across the bot application.
 *
 * This class holds a map of active conversations keyed by [ConversationId].
 * It is stored in the [TelegramBotEventContext.attributes] and shared across all event processing.
 *
 * @param idExtractors A list of functions that extract [ConversationId] from events.
 *        The first non-null result is used. Defaults to trying in order:
 *        1. chatId + threadId + userId (most specific: user in a thread)
 *        2. chatId + userId (user in chat, no thread)
 *        3. chatId + threadId (whole thread, no specific user)
 *        4. chatId (whole chat)
 */
open class ConversationManager(
    val idExtractors: List<(TelegramBotEvent) -> ConversationId?> = listOf(
        // chatId + threadId + userId (most specific)
        { event ->
            val chatId = event.extractChatId()
            val threadId = event.extractThreadId()
            val userId = event.extractUserId()
            if (chatId != null && threadId != null && userId != null) {
                ConversationId(chatId, threadId, userId)
            } else null
        },
        // chatId + userId (user in chat, no thread)
        { event ->
            val chatId = event.extractChatId()
            val userId = event.extractUserId()
            if (chatId != null && userId != null) {
                ConversationId(chatId, userId = userId)
            } else null
        },
        // chatId + threadId (whole thread)
        { event ->
            val chatId = event.extractChatId()
            val threadId = event.extractThreadId()
            if (chatId != null && threadId != null) {
                ConversationId(chatId, threadId)
            } else null
        },
        // chatId (whole chat)
        { event ->
            event.extractChatId()?.let { ConversationId(it) }
        }
    )
) {
    /**
     * Map of active conversations, keyed by [ConversationId].
     * Each entry contains a [ConversationRecord] with the event channel and intercept predicate.
     */
    val activeConversations = ConcurrentMap<ConversationId, ConversationRecord>()
}

/**
 * Attribute key for accessing the [ConversationManager] from a [TelegramBotEventContext].
 */
val ConversationManagerKey = AttributeKey<ConversationManager>(ConversationManager::class.simpleName!!)

/**
 * Exception thrown when a conversation is cancelled by the user.
 *
 * This is a subclass of [CancellationException] to integrate cleanly with Kotlin coroutine cancellation.
 *
 * @property context The event context that triggered the cancellation.
 */
class ConversationCancelledException(
    val context: TelegramBotEventContext<TelegramBotEvent>
) : CancellationException("Conversation cancelled")

/**
 * Creates an interceptor that enables conversation support for the bot application.
 *
 * This interceptor must be installed in the pipeline for [startConversation] to work.
 * It checks incoming events for active conversations and routes them to the appropriate
 * conversation channel instead of the normal processing pipeline.
 *
 * **Note**: This interceptor must not be installed more than once in the pipeline.
 * Multiple installations will cause conversations to malfunction as each instance
 * uses its own [ConversationManager] and overwrites the attribute key.
 *
 * Example usage:
 * ```kotlin
 * val app = TelegramBotApplication.longPolling(
 *     botToken = "YOUR_TOKEN",
 *     interceptors = listOf(conversationInterceptor()),
 *     eventDispatcher = eventDispatcher
 * )
 * ```
 *
 * @param manager The [ConversationManager] to use for tracking active conversations.
 * @return A [TelegramEventInterceptor] that handles conversation routing.
 */
fun conversationInterceptor(
    manager: ConversationManager = ConversationManager()
): TelegramEventInterceptor = Interceptor@{ context ->
    context.attributes.put(ConversationManagerKey, manager)

    for (idExtractor in manager.idExtractors) {
        val conversationId = idExtractor(context.event) ?: continue
        val record = manager.activeConversations[conversationId]
        if (record != null && record.interceptPredicate(context)) {
            val result = record.channel.trySend(context)
            when {
                result.isSuccess -> return@Interceptor
                result.isClosed -> {
                    logger.debug { "Channel closed just before send. Routing normally" }
                    break
                }
                else -> {
                    logger.warn { "Conversation buffer full for session $conversationId. Dropping event: ${context.event}" }
                    return@Interceptor
                }
            }
        }
    }
    process(context)
}
