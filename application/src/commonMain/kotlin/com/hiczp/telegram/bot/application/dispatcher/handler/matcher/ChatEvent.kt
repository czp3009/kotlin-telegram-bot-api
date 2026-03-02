@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.*
import kotlin.jvm.JvmName

// ==================== ChatJoinRequestEvent Filtered Matchers ====================

// --- onChatJoinRequestEventFromChat (stackable) ---

@JvmName("onChatJoinRequestEventFromChatChatJoinRequestEvent")
fun HandlerRoute<ChatJoinRequestEvent>.onChatJoinRequestEventFromChat(
    chatId: Long,
    build: HandlerRoute<ChatJoinRequestEvent>.() -> Unit
) = select({ if (it.event.chatJoinRequest.chat.id == chatId) it else null }, build)

@JvmName("onChatJoinRequestEventFromChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onChatJoinRequestEventFromChat(
    chatId: Long,
    build: HandlerRoute<ChatJoinRequestEvent>.() -> Unit
) = onChatJoinRequestEvent { onChatJoinRequestEventFromChat(chatId, build) }

// --- whenChatJoinRequestEventFromChat (terminal) ---

@JvmName("whenChatJoinRequestEventFromChatChatJoinRequestEvent")
fun HandlerRoute<ChatJoinRequestEvent>.whenChatJoinRequestEventFromChat(
    chatId: Long,
    handler: suspend HandlerBotCall<ChatJoinRequestEvent>.() -> Unit
) = select({ if (it.event.chatJoinRequest.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenChatJoinRequestEventFromChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenChatJoinRequestEventFromChat(
    chatId: Long,
    handler: suspend HandlerBotCall<ChatJoinRequestEvent>.() -> Unit
) = onChatJoinRequestEvent { whenChatJoinRequestEventFromChat(chatId, handler) }

// --- onChatJoinRequestEventFromUser (stackable) ---

@JvmName("onChatJoinRequestEventFromUserChatJoinRequestEvent")
fun HandlerRoute<ChatJoinRequestEvent>.onChatJoinRequestEventFromUser(
    userId: Long,
    build: HandlerRoute<ChatJoinRequestEvent>.() -> Unit
) = select({ if (it.event.chatJoinRequest.from.id == userId) it else null }, build)

@JvmName("onChatJoinRequestEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onChatJoinRequestEventFromUser(
    userId: Long,
    build: HandlerRoute<ChatJoinRequestEvent>.() -> Unit
) = onChatJoinRequestEvent { onChatJoinRequestEventFromUser(userId, build) }

// --- whenChatJoinRequestEventFromUser (terminal) ---

@JvmName("whenChatJoinRequestEventFromUserChatJoinRequestEvent")
fun HandlerRoute<ChatJoinRequestEvent>.whenChatJoinRequestEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<ChatJoinRequestEvent>.() -> Unit
) = select({ if (it.event.chatJoinRequest.from.id == userId) it else null }) { handle(handler) }

@JvmName("whenChatJoinRequestEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenChatJoinRequestEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<ChatJoinRequestEvent>.() -> Unit
) = onChatJoinRequestEvent { whenChatJoinRequestEventFromUser(userId, handler) }

// ==================== ChatMemberEvent Filtered Matchers ====================

// --- onChatMemberEventInChat (stackable) ---

@JvmName("onChatMemberEventInChatChatMemberEvent")
fun HandlerRoute<ChatMemberEvent>.onChatMemberEventInChat(
    chatId: Long,
    build: HandlerRoute<ChatMemberEvent>.() -> Unit
) = select({ if (it.event.chatMember.chat.id == chatId) it else null }, build)

@JvmName("onChatMemberEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onChatMemberEventInChat(
    chatId: Long,
    build: HandlerRoute<ChatMemberEvent>.() -> Unit
) = onChatMemberEvent { onChatMemberEventInChat(chatId, build) }

// --- whenChatMemberEventInChat (terminal) ---

@JvmName("whenChatMemberEventInChatChatMemberEvent")
fun HandlerRoute<ChatMemberEvent>.whenChatMemberEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<ChatMemberEvent>.() -> Unit
) = select({ if (it.event.chatMember.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenChatMemberEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenChatMemberEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<ChatMemberEvent>.() -> Unit
) = onChatMemberEvent { whenChatMemberEventInChat(chatId, handler) }

// ==================== MyChatMemberEvent Filtered Matchers ====================

// --- onMyChatMemberEventInChat (stackable) ---

@JvmName("onMyChatMemberEventInChatMyChatMemberEvent")
fun HandlerRoute<MyChatMemberEvent>.onMyChatMemberEventInChat(
    chatId: Long,
    build: HandlerRoute<MyChatMemberEvent>.() -> Unit
) = select({ if (it.event.myChatMember.chat.id == chatId) it else null }, build)

@JvmName("onMyChatMemberEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMyChatMemberEventInChat(
    chatId: Long,
    build: HandlerRoute<MyChatMemberEvent>.() -> Unit
) = onMyChatMemberEvent { onMyChatMemberEventInChat(chatId, build) }

// --- whenMyChatMemberEventInChat (terminal) ---

@JvmName("whenMyChatMemberEventInChatMyChatMemberEvent")
fun HandlerRoute<MyChatMemberEvent>.whenMyChatMemberEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<MyChatMemberEvent>.() -> Unit
) = select({ if (it.event.myChatMember.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenMyChatMemberEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMyChatMemberEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<MyChatMemberEvent>.() -> Unit
) = onMyChatMemberEvent { whenMyChatMemberEventInChat(chatId, handler) }

// ==================== ChatBoostEvent Filtered Matchers ====================

// --- onChatBoostEventInChat (stackable) ---

@JvmName("onChatBoostEventInChatChatBoostEvent")
fun HandlerRoute<ChatBoostEvent>.onChatBoostEventInChat(
    chatId: Long,
    build: HandlerRoute<ChatBoostEvent>.() -> Unit
) = select({ if (it.event.chatBoost.chat.id == chatId) it else null }, build)

@JvmName("onChatBoostEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onChatBoostEventInChat(
    chatId: Long,
    build: HandlerRoute<ChatBoostEvent>.() -> Unit
) = onChatBoostEvent { onChatBoostEventInChat(chatId, build) }

// --- whenChatBoostEventInChat (terminal) ---

@JvmName("whenChatBoostEventInChatChatBoostEvent")
fun HandlerRoute<ChatBoostEvent>.whenChatBoostEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<ChatBoostEvent>.() -> Unit
) = select({ if (it.event.chatBoost.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenChatBoostEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenChatBoostEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<ChatBoostEvent>.() -> Unit
) = onChatBoostEvent { whenChatBoostEventInChat(chatId, handler) }

// ==================== RemovedChatBoostEvent Filtered Matchers ====================

// --- onRemovedChatBoostEventInChat (stackable) ---

@JvmName("onRemovedChatBoostEventInChatRemovedChatBoostEvent")
fun HandlerRoute<RemovedChatBoostEvent>.onRemovedChatBoostEventInChat(
    chatId: Long,
    build: HandlerRoute<RemovedChatBoostEvent>.() -> Unit
) = select({ if (it.event.removedChatBoost.chat.id == chatId) it else null }, build)

@JvmName("onRemovedChatBoostEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onRemovedChatBoostEventInChat(
    chatId: Long,
    build: HandlerRoute<RemovedChatBoostEvent>.() -> Unit
) = onRemovedChatBoostEvent { onRemovedChatBoostEventInChat(chatId, build) }

// --- whenRemovedChatBoostEventInChat (terminal) ---

@JvmName("whenRemovedChatBoostEventInChatRemovedChatBoostEvent")
fun HandlerRoute<RemovedChatBoostEvent>.whenRemovedChatBoostEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<RemovedChatBoostEvent>.() -> Unit
) = select({ if (it.event.removedChatBoost.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenRemovedChatBoostEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenRemovedChatBoostEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<RemovedChatBoostEvent>.() -> Unit
) = onRemovedChatBoostEvent { whenRemovedChatBoostEventInChat(chatId, handler) }
