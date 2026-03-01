package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.EventRoute
import com.hiczp.telegram.bot.protocol.event.*
import kotlinx.coroutines.CoroutineScope

/**
 * Registers a handler for chat join requests.
 *
 * Triggered when a user requests to join a chat.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenChatJoinRequest(
    handler: suspend CoroutineScope.(TelegramBotEventContext<ChatJoinRequestEvent>) -> Unit
) = on<ChatJoinRequestEvent> { handle(handler) }

/**
 * Registers a handler for chat join requests from a specific chat.
 *
 * @param chatId The Telegram chat ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenChatJoinRequestFromChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<ChatJoinRequestEvent>) -> Unit
) = on<ChatJoinRequestEvent> {
    select({ if (it.event.chatJoinRequest.chat.id == chatId) it else null }) {
        handle(handler)
    }
}

/**
 * Registers a handler for chat join requests from a specific user.
 *
 * @param userId The Telegram user ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenChatJoinRequestFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<ChatJoinRequestEvent>) -> Unit
) = on<ChatJoinRequestEvent> {
    select({ if (it.event.chatJoinRequest.from.id == userId) it else null }) {
        handle(handler)
    }
}


/**
 * Registers a handler for chat member status updates.
 *
 * The bot must be an administrator in the chat and must explicitly specify
 * "chat_member" in the list of allowed_updates to receive these updates.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenChatMemberUpdated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<ChatMemberEvent>) -> Unit
) = on<ChatMemberEvent> { handle(handler) }

/**
 * Registers a handler for chat member status updates in a specific chat.
 *
 * @param chatId The Telegram chat ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenChatMemberUpdatedInChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<ChatMemberEvent>) -> Unit
) = on<ChatMemberEvent> {
    select({ if (it.event.chatMember.chat.id == chatId) it else null }) {
        handle(handler)
    }
}


/**
 * Registers a handler for the bot's own chat member status updates.
 *
 * For private chats, this update is received when the bot is blocked or unblocked by the user.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenMyChatMemberUpdated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MyChatMemberEvent>) -> Unit
) = on<MyChatMemberEvent> { handle(handler) }

/**
 * Registers a handler for the bot's own chat member status updates in a specific chat.
 *
 * @param chatId The Telegram chat ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenMyChatMemberUpdatedInChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MyChatMemberEvent>) -> Unit
) = on<MyChatMemberEvent> {
    select({ if (it.event.myChatMember.chat.id == chatId) it else null }) {
        handle(handler)
    }
}


/**
 * Registers a handler for chat boost added or changed events.
 *
 * The bot must be an administrator in the chat to receive these updates.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenChatBoost(
    handler: suspend CoroutineScope.(TelegramBotEventContext<ChatBoostEvent>) -> Unit
) = on<ChatBoostEvent> { handle(handler) }

/**
 * Registers a handler for chat boost events in a specific chat.
 *
 * @param chatId The Telegram chat ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenChatBoostInChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<ChatBoostEvent>) -> Unit
) = on<ChatBoostEvent> {
    select({ if (it.event.chatBoost.chat.id == chatId) it else null }) {
        handle(handler)
    }
}


/**
 * Registers a handler for chat boost removed events.
 *
 * The bot must be an administrator in the chat to receive these updates.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenRemovedChatBoost(
    handler: suspend CoroutineScope.(TelegramBotEventContext<RemovedChatBoostEvent>) -> Unit
) = on<RemovedChatBoostEvent> { handle(handler) }

/**
 * Registers a handler for removed chat boost events in a specific chat.
 *
 * @param chatId The Telegram chat ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenRemovedChatBoostInChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<RemovedChatBoostEvent>) -> Unit
) = on<RemovedChatBoostEvent> {
    select({ if (it.event.removedChatBoost.chat.id == chatId) it else null }) {
        handle(handler)
    }
}
