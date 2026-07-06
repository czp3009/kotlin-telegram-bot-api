package com.hiczp.telegram.bot.application.interceptor.builtin.conversation

import com.hiczp.telegram.bot.application.command.CommandParser
import com.hiczp.telegram.bot.application.command.ParsedCommand
import com.hiczp.telegram.bot.application.command.matchesCommand
import com.hiczp.telegram.bot.application.command.usernameMatched
import com.hiczp.telegram.bot.application.context.*
import com.hiczp.telegram.bot.protocol.TelegramBotApi
import com.hiczp.telegram.bot.protocol.event.BusinessMessageEvent
import com.hiczp.telegram.bot.protocol.event.CallbackQueryEvent
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import com.hiczp.telegram.bot.protocol.model.Message
import com.hiczp.telegram.bot.protocol.model.ReplyMarkup
import com.hiczp.telegram.bot.protocol.model.ReplyParameters
import com.hiczp.telegram.bot.protocol.model.User
import com.hiczp.telegram.bot.protocol.model.sendMessage
import com.hiczp.telegram.bot.protocol.type.TelegramResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.jvm.JvmName
import kotlin.time.Duration

private val logger = KotlinLogging.logger("ConversationFSM")

/**
 * Scope for handling a multi-turn conversation with a user.
 *
 * Provides methods to await specific types of events from the user during a conversation.
 * Each await method suspends until the user sends an appropriate event.
 *
 * The [channel] can be used directly for advanced use cases, such as implementing
 * custom await logic or integrating with other coroutine-based APIs.
 *
 * The [cancelPredicate] can be used to implement custom cancellation logic by
 * checking it against incoming events.
 *
 * This scope implements [CoroutineScope], allowing you to launch child coroutines
 * that are tied to the conversation's lifecycle. When the conversation ends
 * (completes, times out, or is cancelled), all child coroutines are automatically cancelled.
 *
 * @param id The [ConversationId] identifying this conversation session.
 * @param initialLastAwaitedMessageId The initial message ID that [reply] will respond to by default.
 *        Typically set to the message ID of the event that triggered the conversation.
 * @param channel The channel that receives event contexts for this conversation.
 *        Events are intercepted by the conversation interceptor and routed here.
 * @param cancelPredicate A function that determines if an event should cancel the conversation.
 *        Used internally by [awaitEvent] to check for cancellation.
 * @param coroutineScope The [CoroutineScope] that this conversation scope delegates to.
 *        Child coroutines launched within this scope will be cancelled when the conversation ends.
 * @param telegramBotEventContext The context that started this conversation.
 *        Used by [send], [reply], and [awaitCommand].
 */
class ConversationScope(
    val id: ConversationId,
    initialLastAwaitedMessageId: Long?,
    val channel: ReceiveChannel<TelegramBotEventContext<TelegramBotEvent>>,
    val cancelPredicate: suspend (TelegramBotEventContext<TelegramBotEvent>) -> Boolean,
    private val coroutineScope: CoroutineScope,
    private val telegramBotEventContext: TelegramBotEventContext<*>,
) : CoroutineScope by coroutineScope {
    /**
     * The Telegram API client from the context that started this conversation.
     */
    val client: TelegramBotApi
        get() = telegramBotEventContext.client

    /**
     * The application coroutine scope from the context that started this conversation.
     */
    val applicationScope: CoroutineScope
        get() = telegramBotEventContext.applicationScope

    /**
     * Shared attributes from the context that started this conversation.
     */
    val attributes: Attributes
        get() = telegramBotEventContext.attributes

    /**
     * The event that started this conversation.
     */
    val startEvent: TelegramBotEvent
        get() = telegramBotEventContext.event

    /**
     * Retrieves the bot's own user information.
     */
    suspend fun me(): User = telegramBotEventContext.me()

    /**
     * The message ID that [reply] will respond to by default.
     *
     * This value is initialized to the message ID of the event that triggered the conversation
     * and is updated each time [awaitMessage] or [awaitText] successfully returns.
     *
     * This allows [reply] to automatically respond to the most recent user message in the conversation.
     *
     * **Warning:** Modifying this property directly is generally not recommended. It is automatically
     * managed by the conversation framework. Only modify it if you have a specific use case that
     * requires overriding the default tracking behavior (e.g., implementing custom reply chains).
     */
    var lastAwaitedMessageId: Long? = initialLastAwaitedMessageId

    /**
     * The message ID of the last message sent by [send] or [reply].
     *
     * This value is updated each time [send] or [reply] successfully sends a message.
     *
     * This allows [awaitReply] to wait for replies to the most recent bot message.
     *
     * **Warning:** Modifying this property directly is generally not recommended. It is automatically
     * managed by the conversation framework. Only modify it if you have a specific use case that
     * requires overriding the default tracking behavior (e.g., when sending messages outside of
     * [send] or [reply] methods but still wanting to use [awaitReply]).
     */
    var lastSentMessageId: Long? = null

    /**
     * Awaits the next event from the user.
     *
     * @return The event from the user.
     * @throws ConversationCancelledException if the received event matches the cancel predicate.
     */
    suspend fun awaitEvent(): TelegramBotEvent {
        val context = channel.receive()
        if (cancelPredicate(context)) {
            throw ConversationCancelledException(context)
        }
        return context.event
    }

    /**
     * Awaits the next event of the specified type from the user.
     *
     * This method repeatedly receives events until one matches the specified type [T].
     * Events of other types are silently ignored.
     *
     * Example usage:
     * ```kotlin
     * val message = await<MessageEvent>()
     * val chatId = message.chat.id
     * ```
     *
     * @param T The specific event type to wait for.
     * @return The matching event.
     * @throws ConversationCancelledException if a received event matches the cancel predicate.
     */
    @JvmName("awaitEventReified")
    suspend inline fun <reified T : TelegramBotEvent> awaitEvent(): T {
        while (true) {
            val event = awaitEvent()
            if (event is T) return event
        }
    }

    /**
     * Awaits the next message event from the user.
     *
     * Non-message events are ignored.
     *
     * After this method returns, [lastAwaitedMessageId] is updated to the received message's ID.
     *
     * @return The message event from the user.
     * @throws ConversationCancelledException if the received event matches the cancel predicate.
     */
    suspend fun awaitMessage(): Message = awaitEvent<MessageEvent>().message.also {
        this.lastAwaitedMessageId = it.messageId
    }

    /**
     * Awaits the next text message event from the user and returns the text content.
     *
     * Messages without text content (e.g., photos, stickers) are ignored.
     *
     * After this method returns, [lastAwaitedMessageId] is updated to the received message.
     *
     * @return The text content of the user's message.
     * @throws ConversationCancelledException if the received event matches the cancel predicate.
     */
    suspend fun awaitText(): String {
        while (true) {
            awaitMessage().text?.let { return it }
        }
    }

    /**
     * Awaits a reply to a specific message.
     *
     * This method waits for a message that is a reply to either:
     * 1. The specified [messageId] parameter, if provided.
     * 2. The last message sent by [send] or [reply] (via [lastSentMessageId]), if [messageId] is null.
     *
     * If neither [messageId] nor [lastSentMessageId] is available, this method throws an [IllegalStateException].
     *
     * Messages that are not replies, or replies to other messages, are ignored.
     *
     * After this method returns, [lastAwaitedMessageId] is updated to the received message.
     *
     * Example usage:
     * ```kotlin
     * conversationCommand("reply") {
     *     send("Please reply to this message with your answer")
     *     // Wait for a reply to the message we just sent
     *     val replyMessage = awaitReply()
     *     val answer = replyMessage.text ?: "No text"
     *     reply("You answered: $answer")
     *
     *     // Or specify a specific message ID to wait for a reply to
     *     val replyToSpecific = awaitReply(messageId = 12345)
     * }
     * ```
     *
     * @param messageId The message ID to wait for a reply to. If null, uses [lastSentMessageId].
     * @return The [Message] that is a reply to the target message.
     * @throws IllegalStateException if neither [messageId] nor [lastSentMessageId] is available.
     * @throws ConversationCancelledException if the received event matches the cancel predicate.
     * @see lastSentMessageId
     * @see send
     * @see reply
     */
    suspend fun awaitReply(messageId: Long? = null): Message {
        val targetMessageId = messageId ?: lastSentMessageId
        ?: error("No message ID specified and lastSentMessageId is null. Call send() or reply() first, or provide a messageId parameter.")

        while (true) {
            val message = awaitMessage()
            if (message.replyToMessage?.messageId == targetMessageId) {
                return message
            }
        }
    }

    /**
     * Awaits the next command message from the user and returns the parsed command.
     *
     * A command is a text message starting with "/".
     *
     * This function validates that the command is intended for this bot by checking
     * the username suffix (e.g., `/command@bot_username`). Commands targeted at
     * other bots are ignored.
     *
     * After this method returns, [lastAwaitedMessageId] is updated to the received message.
     *
     * @return The [ParsedCommand] containing the parsed command name, arguments, and metadata.
     * @throws ConversationCancelledException if the received event matches the cancel predicate.
     */
    suspend fun awaitCommand(): ParsedCommand {
        val username = telegramBotEventContext.me().username
        while (true) {
            CommandParser.parse(awaitText())?.takeIf {
                if (username == null) true else it.usernameMatched(username)
            }?.let { return it }
        }
    }

    /**
     * Awaits the next callback query event from the user.
     *
     * Non-callback-query events are ignored.
     *
     * After this method returns, [lastAwaitedMessageId] is updated to the callback query's message ID (if present).
     *
     * @return The callback query event from the user.
     * @throws ConversationCancelledException if the received event matches the cancel predicate.
     */
    suspend fun awaitCallbackQuery(): CallbackQueryEvent = awaitEvent<CallbackQueryEvent>().also {
        this.lastAwaitedMessageId = it.callbackQuery.message?.messageId
    }

    /**
     * Sends a text message to the conversation's chat within a conversation context.
     *
     * This is a convenience method that can be used inside a [ConversationScope] to send messages
     * without manually specifying the chat ID and thread ID. It automatically uses the conversation's
     * [ConversationId.chatId] and [ConversationId.threadId].
     *
     * Example usage inside a conversation:
     * ```kotlin
     * conversationCommand("survey") {
     *     // Send a message to the conversation's chat
     *     send("What is your name?")
     *
     *     val name = awaitText()
     *     send("Hello, $name! How old are you?")
     *
     *     val age = awaitText()
     *     send("Thanks! You are $age years old.")
     *
     *     // Send with ForceReply to prompt user to reply
     *     send("Please reply to this message:", replyMarkup = ForceReply())
     * }
     * ```
     *
     * @param text The text of the message to send.
     * @param replyMarkup Additional interface options (e.g., [ForceReply], inline keyboard).
     * @see reply
     * @see ConversationScope
     */
    suspend fun send(text: String, replyMarkup: ReplyMarkup? = null): TelegramResponse<Message> {
        val response = telegramBotEventContext.client.sendMessage(
            chatId = id.chatId.toString(),
            messageThreadId = id.threadId,
            text = text,
            replyMarkup = replyMarkup,
        )
        lastSentMessageId = response.result?.messageId
        return response
    }

    /**
     * Sends a text message as a reply within a conversation context.
     *
     * This is a convenience method that can be used inside a [ConversationScope] to send reply messages.
     * It automatically uses the conversation's [ConversationId.chatId] and [ConversationId.threadId].
     *
     * The reply behavior is determined by the `replyToMessageId` parameter and follows this priority:
     * 1. If `replyToMessageId` is provided (non-null), replies to that specific message.
     * 2. If [lastAwaitedMessageId] is available, replies to that message.
     *    This allows `reply()` to automatically respond to the most recent user input.
     * 3. If no message ID can be determined, sends a regular message (same as [send]).
     *
     * Example usage inside a conversation:
     * ```kotlin
     * conversationCommand("survey") {
     *     // Reply to the message that triggered this conversation
     *     reply("What is your name?")
     *
     *     val name = awaitText()
     *     // reply() will automatically reply to the message containing the name
     *     reply("Hello, $name! How old are you?")
     *
     *     val ageString = awaitText()
     *     val age = ageString.toIntOrNull()
     *     if (age == null) {
     *         // reply() will automatically reply to the invalid age message
     *         reply("That doesn't seem like a valid age. Please try again.")
     *     }
     *
     *     // Reply to a specific message by ID
     *     reply("This is a reply to message 123", replyToMessageId = 123)
     * }
     * ```
     *
     * @param text The text of the message to send.
     * @param replyToMessageId Optional message ID to reply to. If null, attempts to use
     *        [lastAwaitedMessageId]. If that is also null, sends a regular message.
     * @see lastAwaitedMessageId
     * @see ConversationScope
     * @see send
     */
    suspend fun reply(text: String, replyToMessageId: Long? = null): TelegramResponse<Message> {
        val effectiveReplyToMessageId = replyToMessageId
            ?: lastAwaitedMessageId

        if (effectiveReplyToMessageId == null) {
            // Fallback to send behavior when no message ID is available
            return send(text)
        }

        val response = telegramBotEventContext.client.sendMessage(
            chatId = id.chatId.toString(),
            messageThreadId = id.threadId,
            text = text,
            replyParameters = ReplyParameters(
                messageId = effectiveReplyToMessageId,
                chatId = id.chatId.toString()
            )
        )
        lastSentMessageId = response.result?.messageId
        return response
    }
}

/**
 * Internal primitive that starts a multi-turn conversation with the user.
 *
 * Public handler DSL starters such as [conversation] and [conversationCommand] delegate here after their route filters
 * match the starting event. User-facing code should normally use those starters instead of calling this primitive.
 *
 * This function launches a new coroutine that handles the conversation flow. Subsequent matching events are routed to
 * the conversation's [ConversationScope] instead of the normal event processing pipeline.
 *
 * Inside the conversation block, you can use [ConversationScope.send] and [ConversationScope.reply]
 * methods to conveniently send messages to the conversation's chat without manually specifying
 * chat ID and thread ID.
 *
 * Note: [conversationInterceptor] must be installed in the application pipeline for this to work.
 *
 * The conversation body runs in a [supervisorScope]. Child coroutines launched from the conversation are cancelled
 * when the conversation ends, but a failed child does not automatically fail the main conversation flow. Await or join
 * child work explicitly if its failure should stop the conversation.
 *
 * Any uncaught [TimeoutCancellationException] from inside [block] is treated as a conversation timeout and invokes
 * [onTimeout]. Catch local step timeouts inside [block] if the conversation should continue.
 *
 * Conversation progress is held in coroutine execution state and channel buffers. It cannot be serialized,
 * snapshotted, restored after a crash, or shared across distributed bot instances.
 *
 * @param id The [ConversationId] for this conversation. Defaults to a user-in-thread-chat conversation.
 * @param timeout Optional duration after which the conversation times out. Defaults to no timeout.
 * @param capacity The capacity of the channel used for the conversation. Defaults to [Channel.UNLIMITED].
 *        - **[Channel.UNLIMITED]** (default): All matching events are buffered. This default is chosen for low-traffic
 *          bots, where preserving user input is usually more useful than applying back pressure. Use with caution in
 *          high-traffic scenarios, as unconsumed events can accumulate and potentially cause OOM.
 *        - **Bounded (e.g., `Channel.BUFFERED` or a specific number)**: When the buffer is full, new events are
 *          dropped and a warning is logged. This protects against memory issues but may lose events.
 *        - **[Channel.RENDEZVOUS]** (capacity = 0): Events are only received when the conversation is actively
 *          awaiting via [ConversationScope.awaitEvent] or similar methods. Any event sent while the conversation
 *          is not waiting (e.g., during processing) is dropped. Use this when you only care about events
 *          that arrive while actively listening.
 * @param interceptPredicate A function that determines which events should be intercepted for the conversation.
 *        Defaults to intercepting [MessageEvent], [BusinessMessageEvent], and [CallbackQueryEvent].
 * @param cancelPredicate A function that determines if an event should cancel the conversation.
 *        Defaults to checking if the event is a message matching the "/cancel" command.
 *        The default uses [matchesCommand], which recognizes both `/cancel` and `/cancel@bot_username` formats.
 * @param onTimeout Callback invoked when the conversation times out. Called with the original context.
 * @param onCancel Callback invoked when the conversation is canceled. Called with the context that triggered cancellation.
 * @param block The conversation logic to execute within a [ConversationScope].
 * @return The [Job] representing the conversation coroutine. Can be used to cancel the conversation externally.
 * @throws IllegalStateException if [conversationInterceptor] is not installed.
 * @throws IllegalStateException if the current event cannot provide chatId/userId for the default ConversationId.
 * @see ConversationScope.send
 * @see ConversationScope.reply
 * @see ConversationScope
 */
internal fun <T : TelegramBotEvent> TelegramBotEventContext<T>.startConversation(
    id: ConversationId = ConversationId(
        chatId = this.event.extractChatId() ?: error("No chatId"),
        threadId = this.event.extractThreadId(),
        userId = this.event.extractUserId(),
    ),
    timeout: Duration? = null,
    capacity: Int = Channel.UNLIMITED,
    interceptPredicate: suspend (TelegramBotEventContext<TelegramBotEvent>) -> Boolean = { context ->
        val event = context.event
        event is MessageEvent || event is BusinessMessageEvent || event is CallbackQueryEvent
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
            val action = suspend {
                supervisorScope {
                    ConversationScope(
                        id = id,
                        initialLastAwaitedMessageId = this@startConversation.event.extractMessageId(),
                        channel = record.channel,
                        cancelPredicate = cancelPredicate,
                        telegramBotEventContext = this@startConversation,
                        coroutineScope = this,
                    ).block()
                }
            }
            if (timeout != null) {
                withTimeout(timeout) { action() }
            } else {
                action()
            }
        } catch (_: TimeoutCancellationException) {
            this@startConversation.onTimeout()
        } catch (e: ConversationCancelledException) {
            e.context.onCancel()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            logger.error(e) { "Unhandled exception in Conversation FSM: ${e.message}" }
        } finally {
            manager.activeConversations.remove(id)?.close()
        }
    }
}
