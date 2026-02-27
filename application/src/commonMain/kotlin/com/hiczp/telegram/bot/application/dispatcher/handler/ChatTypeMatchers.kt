package com.hiczp.telegram.bot.application.dispatcher.handler

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.protocol.constant.ChatType
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlinx.coroutines.CoroutineScope

/**
 * Registers a handler for messages from private chats only.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the private chat message.
 */
fun EventRoute<MessageEvent>.privateChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.chat.type == ChatType.PRIVATE) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a private chat handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.privateChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { privateChat(handler) }

/**
 * Registers a handler for messages from group chats only.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the group chat message.
 */
fun EventRoute<MessageEvent>.groupChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.chat.type == ChatType.GROUP) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a group chat handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.groupChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { groupChat(handler) }

/**
 * Registers a handler for messages from supergroup chats only.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the supergroup chat message.
 */
fun EventRoute<MessageEvent>.supergroupChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.chat.type == ChatType.SUPERGROUP) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a supergroup chat handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.supergroupChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { supergroupChat(handler) }

/**
 * Registers a handler for messages from channels only.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the channel message.
 */
fun EventRoute<MessageEvent>.channel(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.chat.type == ChatType.CHANNEL) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a channel handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.channel(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { channel(handler) }
