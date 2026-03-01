package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.EventRoute
import com.hiczp.telegram.bot.protocol.constant.ChatType
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlinx.coroutines.CoroutineScope

/**
 * Registers a handler for messages from private chats only.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the private chat message.
 */
fun EventRoute<MessageEvent>.whenPrivateChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.chat.type == ChatType.PRIVATE) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a private chat handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenPrivateChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenPrivateChat(handler) }

/**
 * Registers a handler for messages from group chats only.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the group chat message.
 */
fun EventRoute<MessageEvent>.whenGroupChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.chat.type == ChatType.GROUP) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a group chat handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenGroupChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenGroupChat(handler) }

/**
 * Registers a handler for messages from supergroup chats only.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the supergroup chat message.
 */
fun EventRoute<MessageEvent>.whenSupergroupChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.chat.type == ChatType.SUPERGROUP) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a supergroup chat handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenSupergroupChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenSupergroupChat(handler) }

/**
 * Registers a handler for messages from channels only.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the channel message.
 */
fun EventRoute<MessageEvent>.whenChannel(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.chat.type == ChatType.CHANNEL) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a channel handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.whenChannel(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenChannel(handler) }
