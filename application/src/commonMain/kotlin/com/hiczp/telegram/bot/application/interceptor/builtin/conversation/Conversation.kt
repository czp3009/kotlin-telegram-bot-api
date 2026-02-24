package com.hiczp.telegram.bot.application.interceptor.builtin.conversation

import com.hiczp.telegram.bot.application.context.SessionKey
import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.context.sessionKey
import com.hiczp.telegram.bot.application.interceptor.TelegramEventInterceptor
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.util.*
import io.ktor.util.collections.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel

private val logger = KotlinLogging.logger("ConversationInterceptor")

/**
 * Holds the state for an active conversation.
 *
 * @property channel The channel that receives event contexts for this conversation.
 * @property interceptPredicate A function that determines which events should be routed to this conversation.
 */
data class ConversationRecord(
    val channel: Channel<TelegramBotEventContext<TelegramBotEvent>>,
    val interceptPredicate: (TelegramBotEvent) -> Boolean,
)

/**
 * Manages active conversations across the bot application.
 *
 * This class holds a map of active conversations keyed by session identifier (chatId, userId).
 * It is stored in the [TelegramBotEventContext.attributes] and shared across all event processing.
 */
open class ConversationManager {
    /**
     * Map of active conversations, keyed by (chatId, userId) pairs.
     * Each entry contains a [ConversationRecord] with the event channel and intercept predicate.
     * The userId may be null for channel posts.
     */
    val activeConversations = ConcurrentMap<SessionKey, ConversationRecord>()
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

    val sessionKey = context.event.sessionKey
    if (sessionKey != null) {
        val record = manager.activeConversations[sessionKey]
        if (record != null && record.interceptPredicate(context.event)) {
            val result = record.channel.trySend(context)
            when {
                result.isSuccess -> return@Interceptor
                result.isClosed -> logger.debug { "Channel closed just before send. Routing normally" }
                else -> {
                    logger.warn { "Conversation buffer full for session $sessionKey. Dropping event: ${context.event}" }
                    return@Interceptor
                }
            }
        }
    }
    process(context)
}
