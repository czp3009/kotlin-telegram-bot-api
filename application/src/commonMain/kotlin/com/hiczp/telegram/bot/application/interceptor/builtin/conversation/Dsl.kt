package com.hiczp.telegram.bot.application.interceptor.builtin.conversation

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.context.castOrNull
import com.hiczp.telegram.bot.application.context.sessionKey
import com.hiczp.telegram.bot.application.convention.Command
import com.hiczp.telegram.bot.protocol.event.BusinessMessageEvent
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration

private val logger = KotlinLogging.logger("ConversationFSM")

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
    private val channel: ReceiveChannel<TelegramBotEventContext<TelegramBotEvent>>,
    private val cancelPredicate: (TelegramBotEvent) -> Boolean,
) {
    /**
     * Awaits the next event from the user.
     *
     * @return The event context containing the user's response.
     * @throws ConversationCancelledException if the received event matches the cancel predicate.
     */
    suspend fun awaitEvent(): TelegramBotEventContext<TelegramBotEvent> {
        val eventContext = channel.receive()
        if (cancelPredicate(eventContext.event)) {
            throw ConversationCancelledException(eventContext)
        }
        return eventContext
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
            val eventContext = awaitEvent()
            eventContext.castOrNull<T>()?.let { return it }
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
            val eventContext = awaitMessage()
            if (eventContext.event.message.text != null) return eventContext
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
            val eventContext = awaitTextMessage()
            if (eventContext.event.message.text!!.startsWith("/")) return eventContext
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
 * @param capacity The capacity of the channel used for the conversation. Defaults to [Channel.BUFFERED].
 * @param interceptPredicate A function that determines which events should be intercepted for the conversation.
 *        Defaults to intercepting [MessageEvent] and [BusinessMessageEvent].
 * @param cancelPredicate A function that determines if an event should cancel the conversation.
 *        Defaults to checking if the event is a message with text "/cancel".
 * @param onTimeout Callback invoked when the conversation times out. Called with the original context.
 * @param onCancel Callback invoked when the conversation is cancelled. Called with the context that triggered cancellation.
 * @param block The conversation logic to execute within a [ConversationScope].
 * @throws IllegalStateException if [conversationInterceptor] is not installed.
 * @throws IllegalStateException if the current event cannot provide a conversation session key.
 */
fun <T : TelegramBotEvent> TelegramBotEventContext<T>.startConversation(
    timeout: Duration? = null,
    capacity: Int = Channel.BUFFERED,
    interceptPredicate: (TelegramBotEvent) -> Boolean = { event ->
        event is MessageEvent || event is BusinessMessageEvent
    },
    cancelPredicate: (TelegramBotEvent) -> Boolean = { event ->
        event is MessageEvent && event.message.text == Command("cancel")
    },
    onTimeout: suspend TelegramBotEventContext<T>.() -> Unit = {},
    onCancel: suspend TelegramBotEventContext<TelegramBotEvent>.() -> Unit = {},
    block: suspend ConversationScope.() -> Unit,
) {
    val manager = attributes.getOrNull(ConversationManagerKey)
        ?: error("ConversationInterceptor not installed")
    val sessionKey = this.event.sessionKey
        ?: error("Can't start conversation due to unsupported event type: ${this.event::class.simpleName}")

    val record = manager.activeConversations.computeIfAbsent(sessionKey) {
        ConversationRecord(
            channel = Channel(capacity),
            interceptPredicate = interceptPredicate,
        )
    }

    applicationScope.launch {
        try {
            if (timeout != null) {
                withTimeout(timeout) {
                    ConversationScope(record.channel, cancelPredicate).block()
                }
            } else {
                ConversationScope(record.channel, cancelPredicate).block()
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
            manager.activeConversations.remove(sessionKey)?.channel?.cancel()
        }
    }
}
