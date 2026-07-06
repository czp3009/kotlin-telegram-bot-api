@file:Suppress("unused")

package com.hiczp.telegram.bot.application.interceptor.builtin.conversation

import com.hiczp.telegram.bot.application.command.matchesCommand
import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.context.extractChatId
import com.hiczp.telegram.bot.application.context.extractThreadId
import com.hiczp.telegram.bot.application.context.extractUserId
import com.hiczp.telegram.bot.application.dispatcher.handler.DefaultHandlerFilterCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerFilterCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.application.dispatcher.handler.RouteNode
import com.hiczp.telegram.bot.protocol.event.BusinessMessageEvent
import com.hiczp.telegram.bot.protocol.event.CallbackQueryEvent
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlinx.coroutines.channels.Channel
import kotlin.jvm.JvmName
import kotlin.time.Duration

private class ConversationStartContext<T : TelegramBotEvent>(
    private val delegate: TelegramBotEventContext<T>,
    val conversationId: ConversationId,
) : TelegramBotEventContext<T> by delegate

private fun defaultConversationId(event: TelegramBotEvent): ConversationId? =
    event.extractChatId()?.let { chatId ->
        ConversationId(
            chatId = chatId,
            threadId = event.extractThreadId(),
            userId = event.extractUserId()
        )
    }

private suspend fun defaultConversationReceive(context: TelegramBotEventContext<TelegramBotEvent>): Boolean {
    val event = context.event
    return event is MessageEvent || event is BusinessMessageEvent || event is CallbackQueryEvent
}

private suspend fun defaultConversationCancel(context: TelegramBotEventContext<TelegramBotEvent>): Boolean {
    val event = context.event
    return event is MessageEvent && event.matchesCommand("cancel", context.me().username)
}

/**
 * Declares a conversation starter at the current handler route.
 *
 * This function only controls how a conversation starts. Once a conversation is active,
 * [conversationInterceptor] restores it before normal handler routing and routes matching
 * events to the conversation state machine.
 *
 * The starting event still follows normal handler DSL order. Parent filters only decide
 * whether this event can start the conversation; they do not constrain later conversation
 * events after the conversation has started.
 */
fun <T : TelegramBotEvent> HandlerRoute<T>.conversation(
    start: suspend HandlerFilterCall<T>.() -> Boolean = { true },
    id: suspend HandlerFilterCall<T>.() -> ConversationId? = { defaultConversationId(event) },
    timeout: Duration? = null,
    capacity: Int = Channel.UNLIMITED,
    receive: suspend (TelegramBotEventContext<TelegramBotEvent>) -> Boolean = ::defaultConversationReceive,
    cancel: suspend (TelegramBotEventContext<TelegramBotEvent>) -> Boolean = ::defaultConversationCancel,
    onTimeout: suspend TelegramBotEventContext<T>.() -> Unit = {},
    onCancel: suspend TelegramBotEventContext<TelegramBotEvent>.() -> Unit = {},
    block: suspend ConversationScope.() -> Unit,
) {
    val childNode = RouteNode { context ->
        @Suppress("UNCHECKED_CAST")
        val typedContext = context as TelegramBotEventContext<T>
        val filterCall = DefaultHandlerFilterCall(typedContext)
        if (!filterCall.start()) {
            null
        } else {
            filterCall.id()?.let { ConversationStartContext(typedContext, it) }
        }
    }
    childNode.handler = { context ->
        @Suppress("UNCHECKED_CAST")
        val startContext = context as ConversationStartContext<T>
        startContext.startConversation(
            id = startContext.conversationId,
            timeout = timeout,
            capacity = capacity,
            interceptPredicate = receive,
            cancelPredicate = cancel,
            onTimeout = onTimeout,
            onCancel = onCancel,
            block = block,
        )
    }
    node.children.add(childNode)
}

/**
 * Declares a conversation started by a Telegram bot command.
 *
 * The command is only the start condition. After the conversation starts, later
 * matching events are restored by [conversationInterceptor] before normal handler routing.
 * The starting command itself still follows normal handler DSL order.
 */
fun HandlerRoute<MessageEvent>.conversationCommand(
    name: String,
    id: suspend HandlerFilterCall<MessageEvent>.() -> ConversationId? = { defaultConversationId(event) },
    timeout: Duration? = null,
    capacity: Int = Channel.UNLIMITED,
    receive: suspend (TelegramBotEventContext<TelegramBotEvent>) -> Boolean = ::defaultConversationReceive,
    cancel: suspend (TelegramBotEventContext<TelegramBotEvent>) -> Boolean = ::defaultConversationCancel,
    onTimeout: suspend TelegramBotEventContext<MessageEvent>.() -> Unit = {},
    onCancel: suspend TelegramBotEventContext<TelegramBotEvent>.() -> Unit = {},
    block: suspend ConversationScope.() -> Unit,
) {
    conversation(
        start = { event.matchesCommand(name, me().username) },
        id = id,
        timeout = timeout,
        capacity = capacity,
        receive = receive,
        cancel = cancel,
        onTimeout = onTimeout,
        onCancel = onCancel,
        block = block,
    )
}

/**
 * Root-level shorthand for [conversationCommand]. Equivalent to:
 *
 * ```kotlin
 * message {
 *     conversationCommand(name) { ... }
 * }
 * ```
 */
@JvmName("conversationCommandOnTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.conversationCommand(
    name: String,
    id: suspend HandlerFilterCall<MessageEvent>.() -> ConversationId? = { defaultConversationId(event) },
    timeout: Duration? = null,
    capacity: Int = Channel.UNLIMITED,
    receive: suspend (TelegramBotEventContext<TelegramBotEvent>) -> Boolean = ::defaultConversationReceive,
    cancel: suspend (TelegramBotEventContext<TelegramBotEvent>) -> Boolean = ::defaultConversationCancel,
    onTimeout: suspend TelegramBotEventContext<MessageEvent>.() -> Unit = {},
    onCancel: suspend TelegramBotEventContext<TelegramBotEvent>.() -> Unit = {},
    block: suspend ConversationScope.() -> Unit,
) = filter<MessageEvent> {
    conversationCommand(
        name = name,
        id = id,
        timeout = timeout,
        capacity = capacity,
        receive = receive,
        cancel = cancel,
        onTimeout = onTimeout,
        onCancel = onCancel,
        block = block,
    )
}
