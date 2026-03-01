@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.*
import kotlin.jvm.JvmName

// ==================== ChatJoinRequestEvent Filtered Matchers ====================

// --- onChatJoinRequestFromChat (stackable) ---

@JvmName("onChatJoinRequestFromChatChatJoinRequestEvent")
fun HandlerRoute<ChatJoinRequestEvent>.onChatJoinRequestFromChat(
    chatId: Long,
    build: HandlerRoute<ChatJoinRequestEvent>.() -> Unit
) = select({ if (it.event.chatJoinRequest.chat.id == chatId) it else null }, build)

@JvmName("onChatJoinRequestFromChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onChatJoinRequestFromChat(
    chatId: Long,
    build: HandlerRoute<ChatJoinRequestEvent>.() -> Unit
) = onChatJoinRequestEvent { onChatJoinRequestFromChat(chatId, build) }

// --- whenChatJoinRequestFromChat (terminal) ---

@JvmName("whenChatJoinRequestFromChatChatJoinRequestEvent")
fun HandlerRoute<ChatJoinRequestEvent>.whenChatJoinRequestFromChat(
    chatId: Long,
    handler: suspend HandlerBotCall<ChatJoinRequestEvent>.() -> Unit
) = select({ if (it.event.chatJoinRequest.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenChatJoinRequestFromChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenChatJoinRequestFromChat(
    chatId: Long,
    handler: suspend HandlerBotCall<ChatJoinRequestEvent>.() -> Unit
) = onChatJoinRequestEvent { whenChatJoinRequestFromChat(chatId, handler) }

// --- onChatJoinRequestFromUser (stackable) ---

@JvmName("onChatJoinRequestFromUserChatJoinRequestEvent")
fun HandlerRoute<ChatJoinRequestEvent>.onChatJoinRequestFromUser(
    userId: Long,
    build: HandlerRoute<ChatJoinRequestEvent>.() -> Unit
) = select({ if (it.event.chatJoinRequest.from.id == userId) it else null }, build)

@JvmName("onChatJoinRequestFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onChatJoinRequestFromUser(
    userId: Long,
    build: HandlerRoute<ChatJoinRequestEvent>.() -> Unit
) = onChatJoinRequestEvent { onChatJoinRequestFromUser(userId, build) }

// --- whenChatJoinRequestFromUser (terminal) ---

@JvmName("whenChatJoinRequestFromUserChatJoinRequestEvent")
fun HandlerRoute<ChatJoinRequestEvent>.whenChatJoinRequestFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<ChatJoinRequestEvent>.() -> Unit
) = select({ if (it.event.chatJoinRequest.from.id == userId) it else null }) { handle(handler) }

@JvmName("whenChatJoinRequestFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenChatJoinRequestFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<ChatJoinRequestEvent>.() -> Unit
) = onChatJoinRequestEvent { whenChatJoinRequestFromUser(userId, handler) }

// ==================== ChatMemberEvent Filtered Matchers ====================

// --- onChatMemberUpdatedInChat (stackable) ---

@JvmName("onChatMemberUpdatedInChatChatMemberEvent")
fun HandlerRoute<ChatMemberEvent>.onChatMemberUpdatedInChat(
    chatId: Long,
    build: HandlerRoute<ChatMemberEvent>.() -> Unit
) = select({ if (it.event.chatMember.chat.id == chatId) it else null }, build)

@JvmName("onChatMemberUpdatedInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onChatMemberUpdatedInChat(
    chatId: Long,
    build: HandlerRoute<ChatMemberEvent>.() -> Unit
) = onChatMemberEvent { onChatMemberUpdatedInChat(chatId, build) }

// --- whenChatMemberUpdatedInChat (terminal) ---

@JvmName("whenChatMemberUpdatedInChatChatMemberEvent")
fun HandlerRoute<ChatMemberEvent>.whenChatMemberUpdatedInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<ChatMemberEvent>.() -> Unit
) = select({ if (it.event.chatMember.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenChatMemberUpdatedInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenChatMemberUpdatedInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<ChatMemberEvent>.() -> Unit
) = onChatMemberEvent { whenChatMemberUpdatedInChat(chatId, handler) }

// ==================== MyChatMemberEvent Filtered Matchers ====================

// --- onMyChatMemberUpdatedInChat (stackable) ---

@JvmName("onMyChatMemberUpdatedInChatMyChatMemberEvent")
fun HandlerRoute<MyChatMemberEvent>.onMyChatMemberUpdatedInChat(
    chatId: Long,
    build: HandlerRoute<MyChatMemberEvent>.() -> Unit
) = select({ if (it.event.myChatMember.chat.id == chatId) it else null }, build)

@JvmName("onMyChatMemberUpdatedInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMyChatMemberUpdatedInChat(
    chatId: Long,
    build: HandlerRoute<MyChatMemberEvent>.() -> Unit
) = onMyChatMemberEvent { onMyChatMemberUpdatedInChat(chatId, build) }

// --- whenMyChatMemberUpdatedInChat (terminal) ---

@JvmName("whenMyChatMemberUpdatedInChatMyChatMemberEvent")
fun HandlerRoute<MyChatMemberEvent>.whenMyChatMemberUpdatedInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<MyChatMemberEvent>.() -> Unit
) = select({ if (it.event.myChatMember.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenMyChatMemberUpdatedInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMyChatMemberUpdatedInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<MyChatMemberEvent>.() -> Unit
) = onMyChatMemberEvent { whenMyChatMemberUpdatedInChat(chatId, handler) }

// ==================== ChatBoostEvent Filtered Matchers ====================

// --- onChatBoostInChat (stackable) ---

@JvmName("onChatBoostInChatChatBoostEvent")
fun HandlerRoute<ChatBoostEvent>.onChatBoostInChat(
    chatId: Long,
    build: HandlerRoute<ChatBoostEvent>.() -> Unit
) = select({ if (it.event.chatBoost.chat.id == chatId) it else null }, build)

@JvmName("onChatBoostInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onChatBoostInChat(
    chatId: Long,
    build: HandlerRoute<ChatBoostEvent>.() -> Unit
) = onChatBoostEvent { onChatBoostInChat(chatId, build) }

// --- whenChatBoostInChat (terminal) ---

@JvmName("whenChatBoostInChatChatBoostEvent")
fun HandlerRoute<ChatBoostEvent>.whenChatBoostInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<ChatBoostEvent>.() -> Unit
) = select({ if (it.event.chatBoost.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenChatBoostInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenChatBoostInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<ChatBoostEvent>.() -> Unit
) = onChatBoostEvent { whenChatBoostInChat(chatId, handler) }

// ==================== RemovedChatBoostEvent Filtered Matchers ====================

// --- onRemovedChatBoostInChat (stackable) ---

@JvmName("onRemovedChatBoostInChatRemovedChatBoostEvent")
fun HandlerRoute<RemovedChatBoostEvent>.onRemovedChatBoostInChat(
    chatId: Long,
    build: HandlerRoute<RemovedChatBoostEvent>.() -> Unit
) = select({ if (it.event.removedChatBoost.chat.id == chatId) it else null }, build)

@JvmName("onRemovedChatBoostInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onRemovedChatBoostInChat(
    chatId: Long,
    build: HandlerRoute<RemovedChatBoostEvent>.() -> Unit
) = onRemovedChatBoostEvent { onRemovedChatBoostInChat(chatId, build) }

// --- whenRemovedChatBoostInChat (terminal) ---

@JvmName("whenRemovedChatBoostInChatRemovedChatBoostEvent")
fun HandlerRoute<RemovedChatBoostEvent>.whenRemovedChatBoostInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<RemovedChatBoostEvent>.() -> Unit
) = select({ if (it.event.removedChatBoost.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenRemovedChatBoostInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenRemovedChatBoostInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<RemovedChatBoostEvent>.() -> Unit
) = onRemovedChatBoostEvent { whenRemovedChatBoostInChat(chatId, handler) }
