package com.hiczp.telegram.bot.application.interceptor.builtin.conversation

import com.hiczp.telegram.bot.application.command.matchesCommand
import com.hiczp.telegram.bot.application.context.*
import com.hiczp.telegram.bot.protocol.event.BusinessMessageEvent
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import com.hiczp.telegram.bot.protocol.model.ReplyParameters
import com.hiczp.telegram.bot.protocol.model.sendMessage
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.time.Duration

private val logger = KotlinLogging.logger("ConversationFSM")

/**
 * Scope for handling a multi-turn conversation with a user.
 *
 * Provides methods to await specific types of events from the user during a conversation.
 * Each await method suspends until the user sends an appropriate event.
 *
 * @param id The [ConversationId] identifying this conversation session.
 * @param channel The channel to receive event contexts from.
 * @param cancelPredicate A function that determines if an event should cancel the conversation.
 */
class ConversationScope(
    val id: ConversationId,
    private val channel: ReceiveChannel<TelegramBotEventContext<TelegramBotEvent>>,
    private val cancelPredicate: suspend (TelegramBotEventContext<TelegramBotEvent>) -> Boolean,
) {
    /**
     * Awaits the next event from the user.
     *
     * @return The event context containing the user's response.
     * @throws ConversationCancelledException if the received event matches the cancel predicate.
     */
    suspend fun awaitEvent(): TelegramBotEventContext<TelegramBotEvent> {
        val context = channel.receive()
        if (cancelPredicate(context)) {
            throw ConversationCancelledException(context)
        }
        return context
    }

    /**
     * Awaits the next event of the specified type from the user.
     *
     * This method repeatedly receives events until one matches the specified type [T].
     * Events of other types are silently ignored.
     *
     * Example usage:
     * ```kotlin
     * val messageContext = await<MessageEvent>()
     * val chatId = messageContext.event.message.chat.id
     * ```
     *
     * @param T The specific event type to wait for.
     * @return The event context containing the matching event.
     * @throws ConversationCancelledException if a received event matches the cancel predicate.
     */
    suspend inline fun <reified T : TelegramBotEvent> await(): TelegramBotEventContext<T> {
        while (true) {
            val context = awaitEvent()
            context.castOrNull<T>()?.let { return it }
        }
    }

    /**
     * Awaits the next message event from the user.
     *
     * Non-message events are ignored.
     *
     * @return The event context containing the user's message.
     * @throws ConversationCancelledException if the received event matches the cancel predicate.
     */
    suspend fun awaitMessage(): TelegramBotEventContext<MessageEvent> = await()

    /**
     * Awaits the next text message event from the user.
     *
     * Messages without text content (e.g., photos, stickers) are ignored.
     *
     * @return The event context containing the user's text message.
     * @throws ConversationCancelledException if the received event matches the cancel predicate.
     */
    suspend fun awaitTextMessage(): TelegramBotEventContext<MessageEvent> {
        while (true) {
            val context = awaitMessage()
            if (context.event.message.text != null) return context
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
    suspend fun awaitCommand(): TelegramBotEventContext<MessageEvent> {
        while (true) {
            val context = awaitTextMessage()
            if (context.event.message.text!!.startsWith("/")) return context
        }
    }
}

/**
 * Sends a text message to the conversation's chat within a conversation context.
 *
 * This is a convenience function that can be used inside a [ConversationScope] to send messages
 * without manually specifying the chat ID and thread ID. It automatically uses the conversation's
 * [ConversationId.chatId] and [ConversationId.threadId].
 *
 * This function requires two context receivers:
 * - [TelegramBotEventContext]: Provides access to the Telegram client for sending messages.
 * - [ConversationScope]: Provides the conversation's ID (chatId, threadId) for addressing.
 *
 * Example usage inside a conversation:
 * ```kotlin
 * startConversation {
 *     // Simple reply to the conversation's chat
 *     reply("What is your name?")
 *
 *     val name = awaitTextMessage().event.message.text
 *
 *     // Reply to a specific message
 *     reply("Hello, $name!", replyToMessageId = message.messageId)
 * }
 * ```
 *
 * @param text The text of the message to send.
 * @param replyToMessageId Optional message ID to reply to. If provided, the message will be sent
 *        as a reply to the specified message.
 * @see ConversationScope
 * @see startConversation
 */
context(telegramBotEventContext: TelegramBotEventContext<*>, conversationScope: ConversationScope)
suspend fun reply(text: String, replyToMessageId: Long? = null) =
    telegramBotEventContext.client.sendMessage(
        chatId = conversationScope.id.chatId.toString(),
        messageThreadId = conversationScope.id.threadId,
        text = text,
        replyParameters = if (replyToMessageId != null) {
            ReplyParameters(messageId = replyToMessageId, chatId = conversationScope.id.chatId.toString())
        } else {
            null
        }
    )

/**
 * Starts a multi-turn conversation with the user.
 *
 * This function launches a new coroutine that handles the conversation flow.
 * Subsequent events from the same user will be routed to the conversation's [ConversationScope]
 * instead of the normal event processing pipeline.
 *
 * Inside the conversation block, you can use the [reply] function to conveniently send messages
 * to the conversation's chat without manually specifying chat ID and thread ID.
 *
 * Example usage:
 * ```kotlin
 * if (message.text == "/survey") {
 *     startConversation(
 *         timeout = 5.minutes,
 *         onTimeout = {
 *             val chatId = event.extractChatId()
 *             if (chatId != null) {
 *                 client.sendMessage(chatId.toString(), "Survey timed out.")
 *             }
 *         },
 *         onCancel = {
 *             val chatId = event.extractChatId()
 *             if (chatId != null) {
 *                 client.sendMessage(chatId.toString(), "Survey cancelled.")
 *             }
 *         }
 *     ) {
 *         // Use reply() for convenience
 *         reply("What is your name?")
 *         val name = awaitTextMessage().event.message.text
 *         reply("Hello, $name! How old are you?")
 *         val age = awaitTextMessage().event.message.text
 *         reply("Thanks! You are $age years old.")
 *     }
 * }
 * ```
 *
 * Note: [conversationInterceptor] must be installed in the application pipeline for this to work.
 *
 * @param id The [ConversationId] for this conversation. Defaults to a user-in-thread-chat conversation.
 * @param timeout Optional duration after which the conversation times out. Defaults to no timeout.
 * @param capacity The capacity of the channel used for the conversation. Defaults to [Channel.UNLIMITED].
 *        - **[Channel.UNLIMITED]** (default): All matching events are buffered. Use with caution in high-traffic
 *          scenarios, as unconsumed events can accumulate and potentially cause OOM.
 *        - **Bounded (e.g., `Channel.BUFFERED` or a specific number)**: When the buffer is full, new events are
 *          dropped and a warning is logged. This protects against memory issues but may lose events.
 *        - **[Channel.RENDEZVOUS]** (capacity = 0): Events are only received when the conversation is actively
 *          awaiting via [ConversationScope.awaitEvent] or similar methods. Any event sent while the conversation
 *          is not waiting (e.g., during processing) is dropped. Use this when you only care about events
 *          that arrive while actively listening.
 * @param interceptPredicate A function that determines which events should be intercepted for the conversation.
 *        Defaults to intercepting [MessageEvent].
 * @param cancelPredicate A function that determines if an event should cancel the conversation.
 *        Defaults to checking if the event is a message matching the "/cancel" command.
 *        The default uses [matchesCommand], which recognizes both `/cancel` and `/cancel@bot_username` formats.
 * @param onTimeout Callback invoked when the conversation times out. Called with the original context.
 * @param onCancel Callback invoked when the conversation is canceled. Called with the context that triggered cancellation.
 * @param block The conversation logic to execute within a [ConversationScope].
 * @return The [Job] representing the conversation coroutine. Can be used to cancel the conversation externally.
 * @throws IllegalStateException if [conversationInterceptor] is not installed.
 * @throws IllegalStateException if the current event cannot provide chatId/userId for the default ConversationId.
 * @see reply
 * @see ConversationScope
 */
fun <T : TelegramBotEvent> TelegramBotEventContext<T>.startConversation(
    id: ConversationId = ConversationId(
        chatId = this.event.extractChatId() ?: error("No chatId"),
        threadId = this.event.extractThreadId(),
        userId = this.event.extractUserId(),
    ),
    timeout: Duration? = null,
    capacity: Int = Channel.UNLIMITED,
    interceptPredicate: suspend (TelegramBotEventContext<TelegramBotEvent>) -> Boolean = { context ->
        val event = context.event
        event is MessageEvent || event is BusinessMessageEvent
    },
    cancelPredicate: suspend (TelegramBotEventContext<TelegramBotEvent>) -> Boolean = { context ->
        val event = context.event
        event is MessageEvent && event.matchesCommand("cancel", context.me().username)
    },
    onTimeout: suspend TelegramBotEventContext<T>.() -> Unit = {},
    onCancel: suspend TelegramBotEventContext<TelegramBotEvent>.() -> Unit = {},
    block: suspend ConversationScope.() -> Unit,
): Job {
    val manager = attributes.getOrNull(ConversationManagerKey)
        ?: error("ConversationInterceptor not installed")

    // Use computeIfAbsent to simulate putIfAbsent behavior:
    // - If the key doesn't exist, insert the new record and return it
    // - If the key already exists, return the existing record
    // - Check reference equality to determine if we inserted or found an existing entry
    val newRecord = ConversationRecord(
        channel = Channel(
            capacity = capacity,
            onUndeliveredElement = { context ->
                logger.warn { "Conversation $id ended but event was left unconsumed in the buffer. Dropped event: ${context.event}" }
            }
        ),
        interceptPredicate = interceptPredicate,
    )
    val record = manager.activeConversations.computeIfAbsent(id) {
        newRecord
    }
    if (record !== newRecord) {
        newRecord.close()
        error("A conversation is already active for Conversation: $id. You must finish or cancel the existing conversation before starting a new one.")
    }

    return applicationScope.launch {
        try {
            if (timeout != null) {
                withTimeout(timeout) {
                    ConversationScope(id, record.channel, cancelPredicate).block()
                }
            } else {
                ConversationScope(id, record.channel, cancelPredicate).block()
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
            manager.activeConversations.remove(id)?.close()
        }
    }
}
