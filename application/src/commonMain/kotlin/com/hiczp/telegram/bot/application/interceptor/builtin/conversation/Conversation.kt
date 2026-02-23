package com.hiczp.telegram.bot.application.interceptor.builtin.conversation

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.convention.Command
import com.hiczp.telegram.bot.application.interceptor.TelegramEventInterceptor
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.util.*
import io.ktor.util.collections.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration

private val logger = KotlinLogging.logger {}

/**
 * Manages active conversations across the bot application.
 *
 * This class holds a map of active conversations keyed by session identifier (chatId, userId).
 * It is stored in the [TelegramBotEventContext.attributes] and shared across all event processing.
 */
class ConversationManager {
    /**
     * Map of active conversations, keyed by (chatId, userId) pairs.
     * The userId may be null for channel posts.
     */
    val activeConversations = ConcurrentMap<Pair<Long, Long?>, Channel<TelegramBotEventContext>>()
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
    val context: TelegramBotEventContext
) : CancellationException("Conversation cancelled")

private val defaultConversationManager by lazy { ConversationManager() }

/**
 * Interceptor that enables conversation support for the bot application.
 *
 * This interceptor must be installed in the pipeline for [startConversation] to work.
 * It checks incoming events for active conversations and routes them to the appropriate
 * conversation channel instead of the normal processing pipeline.
 *
 * Example usage:
 * ```kotlin
 * val app = TelegramBotApplication.longPolling(
 *     botToken = "YOUR_TOKEN",
 *     interceptors = listOf(conversationInterceptor),
 *     eventDispatcher = eventDispatcher
 * )
 * ```
 */
val conversationInterceptor: TelegramEventInterceptor = Interceptor@{ context ->
    val manager = defaultConversationManager
    context.attributes.put(ConversationManagerKey, manager)

    val sessionKey = context.event.sessionKey
    if (sessionKey != null) {
        val activeChannel = manager.activeConversations[sessionKey]
        if (activeChannel != null) {
            activeChannel.send(context)
            return@Interceptor
        }
    }
    process(context)
}

/**
 * Scope for handling a multi-turn conversation with a user.
 *
 * Provides methods to await specific types of events from the user during a conversation.
 * Each await method suspends until the user sends an appropriate event.
 *
 * @param channel The channel to receive event contexts from.
 * @param cancelPredicate A function that determines if an event should cancel the conversation.
 */
class ConversationScope(
    private val channel: ReceiveChannel<TelegramBotEventContext>,
    private val cancelPredicate: (TelegramBotEvent) -> Boolean
) {
    /**
     * Awaits the next event from the user.
     *
     * @return The event context containing the user's response.
     * @throws ConversationCancelledException if the received event matches the cancel predicate.
     */
    suspend fun awaitEvent(): TelegramBotEventContext {
        val eventContext = channel.receive()
        if (cancelPredicate(eventContext.event)) {
            throw ConversationCancelledException(eventContext)
        }
        return eventContext
    }

    /**
     * Awaits the next message event from the user.
     *
     * Non-message events are ignored.
     *
     * @return The event context containing the user's message.
     * @throws ConversationCancelledException if the received event matches the cancel predicate.
     */
    suspend fun awaitMessage(): TelegramBotEventContext {
        while (true) {
            val eventContext = awaitEvent()
            if (eventContext.event is MessageEvent) {
                return eventContext
            }
        }
    }

    /**
     * Awaits the next text message event from the user.
     *
     * Messages without text content (e.g., photos, stickers) are ignored.
     *
     * @return The event context containing the user's text message.
     * @throws ConversationCancelledException if the received event matches the cancel predicate.
     */
    suspend fun awaitTextMessage(): TelegramBotEventContext {
        while (true) {
            val eventContext = awaitMessage()
            if ((eventContext.event as MessageEvent).message.text != null) {
                return eventContext
            }
        }
    }

    /**
     * Awaits the next command message from the user.
     *
     * A command is a text message starting with "/".
     *
     * @return The event context containing the user's command message.
     * @throws ConversationCancelledException if the received event matches the cancel predicate.
     */
    suspend fun awaitCommand(): TelegramBotEventContext {
        while (true) {
            val eventContext = awaitTextMessage()
            if ((eventContext.event as MessageEvent).message.text!!.startsWith("/")) {
                return eventContext
            }
        }
    }
}

/**
 * Starts a multi-turn conversation with the user.
 *
 * This function launches a new coroutine that handles the conversation flow.
 * Subsequent events from the same user will be routed to the conversation's [ConversationScope]
 * instead of the normal event processing pipeline.
 *
 * Example usage:
 * ```kotlin
 * if (message.text == "/survey") {
 *     startConversation(
 *         timeout = 5.minutes,
 *         onTimeout = { reply("Survey timed out.") },
 *         onCancel = { reply("Survey cancelled.") }
 *     ) {
 *         val name = awaitTextMessage().event.message.text
 *         reply("Hello, $name!")
 *         val age = awaitTextMessage().event.message.text
 *         reply("Thanks! You are $age years old.")
 *     }
 * }
 * ```
 *
 * Note: [conversationInterceptor] must be installed in the application pipeline for this to work.
 *
 * @param timeout Optional duration after which the conversation times out. Defaults to no timeout.
 * @param cancelCommand The command that cancels the conversation. Defaults to "/cancel". Pass null to disable.
 * @param cancelPredicate A custom function to determine if an event should cancel the conversation.
 *        Defaults to checking if the event is a message matching [cancelCommand].
 * @param onTimeout Callback invoked when the conversation times out. Called with the original context.
 * @param onCancel Callback invoked when the conversation is cancelled. Called with the context that triggered cancellation.
 * @param block The conversation logic to execute within a [ConversationScope].
 * @throws IllegalStateException if [conversationInterceptor] is not installed.
 * @throws IllegalStateException if the event type doesn't support conversations (only [MessageEvent] is currently supported).
 */
fun TelegramBotEventContext.startConversation(
    timeout: Duration? = null,
    cancelCommand: String? = Command("cancel"),
    cancelPredicate: (TelegramBotEvent) -> Boolean = { event ->
        cancelCommand != null && event is MessageEvent && event.message.text == cancelCommand
    },
    onTimeout: suspend TelegramBotEventContext.() -> Unit = {},
    onCancel: suspend TelegramBotEventContext.() -> Unit = {},
    block: suspend ConversationScope.() -> Unit
) {
    val manager = attributes.getOrNull(ConversationManagerKey)
        ?: error("ConversationInterceptor not installed")
    val sessionKey = this.event.sessionKey
        ?: error("Can't start conversation due to unsupported event type: ${this.event::class.simpleName}")

    val channel = manager.activeConversations.computeIfAbsent(sessionKey) {
        Channel()
    }

    applicationScope.launch {
        try {
            if (timeout != null) {
                withTimeout(timeout) {
                    ConversationScope(channel, cancelPredicate).block()
                }
            } else {
                ConversationScope(channel, cancelPredicate).block()
            }
        } catch (_: TimeoutCancellationException) {
            this@startConversation.onTimeout()
        } catch (e: ConversationCancelledException) {
            e.context.onCancel()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception in Conversation FSM: ${e.message}" }
        } finally {
            manager.activeConversations.remove(sessionKey)
        }
    }
}

private val TelegramBotEvent.sessionKey
    get() = (this as? MessageEvent)?.let {
        it.message.chat.id to it.message.from?.id
    }
