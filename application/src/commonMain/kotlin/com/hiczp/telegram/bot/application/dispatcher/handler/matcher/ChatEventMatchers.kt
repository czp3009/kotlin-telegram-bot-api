package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.EventRoute
import com.hiczp.telegram.bot.protocol.event.*
import kotlinx.coroutines.CoroutineScope
import kotlin.jvm.JvmName

@JvmName("chatJoinRequestTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.chatJoinRequest(
    build: EventRoute<ChatJoinRequestEvent>.() -> Unit
): EventRoute<ChatJoinRequestEvent> = on<ChatJoinRequestEvent> { build() }

@JvmName("whenChatJoinRequestTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenChatJoinRequest(
    handler: suspend CoroutineScope.(TelegramBotEventContext<ChatJoinRequestEvent>) -> Unit
) = chatJoinRequest { handle(handler) }

@JvmName("chatJoinRequestFromChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.chatJoinRequestFromChat(
    chatId: Long,
    build: EventRoute<ChatJoinRequestEvent>.() -> Unit
): EventRoute<ChatJoinRequestEvent> = on<ChatJoinRequestEvent> {
    select({ if (it.event.chatJoinRequest.chat.id == chatId) it else null }, build)
}

@JvmName("whenChatJoinRequestFromChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenChatJoinRequestFromChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<ChatJoinRequestEvent>) -> Unit
) = chatJoinRequestFromChat(chatId) { handle(handler) }

@JvmName("chatJoinRequestFromUserTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.chatJoinRequestFromUser(
    userId: Long,
    build: EventRoute<ChatJoinRequestEvent>.() -> Unit
): EventRoute<ChatJoinRequestEvent> = on<ChatJoinRequestEvent> {
    select({ if (it.event.chatJoinRequest.from.id == userId) it else null }, build)
}

@JvmName("whenChatJoinRequestFromUserTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenChatJoinRequestFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<ChatJoinRequestEvent>) -> Unit
) = chatJoinRequestFromUser(userId) { handle(handler) }

@JvmName("chatMemberUpdatedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.chatMemberUpdated(
    build: EventRoute<ChatMemberEvent>.() -> Unit
): EventRoute<ChatMemberEvent> = on<ChatMemberEvent> { build() }

@JvmName("whenChatMemberUpdatedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenChatMemberUpdated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<ChatMemberEvent>) -> Unit
) = chatMemberUpdated { handle(handler) }

@JvmName("chatMemberUpdatedInChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.chatMemberUpdatedInChat(
    chatId: Long,
    build: EventRoute<ChatMemberEvent>.() -> Unit
): EventRoute<ChatMemberEvent> = on<ChatMemberEvent> {
    select({ if (it.event.chatMember.chat.id == chatId) it else null }, build)
}

@JvmName("whenChatMemberUpdatedInChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenChatMemberUpdatedInChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<ChatMemberEvent>) -> Unit
) = chatMemberUpdatedInChat(chatId) { handle(handler) }

@JvmName("myChatMemberUpdatedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.myChatMemberUpdated(
    build: EventRoute<MyChatMemberEvent>.() -> Unit
): EventRoute<MyChatMemberEvent> = on<MyChatMemberEvent> { build() }

@JvmName("whenMyChatMemberUpdatedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenMyChatMemberUpdated(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MyChatMemberEvent>) -> Unit
) = myChatMemberUpdated { handle(handler) }

@JvmName("myChatMemberUpdatedInChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.myChatMemberUpdatedInChat(
    chatId: Long,
    build: EventRoute<MyChatMemberEvent>.() -> Unit
): EventRoute<MyChatMemberEvent> = on<MyChatMemberEvent> {
    select({ if (it.event.myChatMember.chat.id == chatId) it else null }, build)
}

@JvmName("whenMyChatMemberUpdatedInChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenMyChatMemberUpdatedInChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MyChatMemberEvent>) -> Unit
) = myChatMemberUpdatedInChat(chatId) { handle(handler) }

@JvmName("chatBoostTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.chatBoost(
    build: EventRoute<ChatBoostEvent>.() -> Unit
): EventRoute<ChatBoostEvent> = on<ChatBoostEvent> { build() }

@JvmName("whenChatBoostTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenChatBoost(
    handler: suspend CoroutineScope.(TelegramBotEventContext<ChatBoostEvent>) -> Unit
) = chatBoost { handle(handler) }

@JvmName("chatBoostInChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.chatBoostInChat(
    chatId: Long,
    build: EventRoute<ChatBoostEvent>.() -> Unit
): EventRoute<ChatBoostEvent> = on<ChatBoostEvent> {
    select({ if (it.event.chatBoost.chat.id == chatId) it else null }, build)
}

@JvmName("whenChatBoostInChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenChatBoostInChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<ChatBoostEvent>) -> Unit
) = chatBoostInChat(chatId) { handle(handler) }

@JvmName("removedChatBoostTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.removedChatBoost(
    build: EventRoute<RemovedChatBoostEvent>.() -> Unit
): EventRoute<RemovedChatBoostEvent> = on<RemovedChatBoostEvent> { build() }

@JvmName("whenRemovedChatBoostTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenRemovedChatBoost(
    handler: suspend CoroutineScope.(TelegramBotEventContext<RemovedChatBoostEvent>) -> Unit
) = removedChatBoost { handle(handler) }

@JvmName("removedChatBoostInChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.removedChatBoostInChat(
    chatId: Long,
    build: EventRoute<RemovedChatBoostEvent>.() -> Unit
): EventRoute<RemovedChatBoostEvent> = on<RemovedChatBoostEvent> {
    select({ if (it.event.removedChatBoost.chat.id == chatId) it else null }, build)
}

@JvmName("whenRemovedChatBoostInChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenRemovedChatBoostInChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<RemovedChatBoostEvent>) -> Unit
) = removedChatBoostInChat(chatId) { handle(handler) }
