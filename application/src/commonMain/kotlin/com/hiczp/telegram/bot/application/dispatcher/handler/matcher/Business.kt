@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.*
import kotlin.jvm.JvmName

// ==================== BusinessConnectionEvent Filtered Matchers ====================

// --- onBusinessConnectionEventFromUser (stackable) ---

@JvmName("onBusinessConnectionEventFromUserBusinessConnectionEvent")
fun HandlerRoute<BusinessConnectionEvent>.onBusinessConnectionEventFromUser(
    userId: Long,
    build: HandlerRoute<BusinessConnectionEvent>.() -> Unit
) = select({ if (it.event.businessConnection.user.id == userId) it else null }, build)

@JvmName("onBusinessConnectionEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onBusinessConnectionEventFromUser(
    userId: Long,
    build: HandlerRoute<BusinessConnectionEvent>.() -> Unit
) = onBusinessConnectionEvent { onBusinessConnectionEventFromUser(userId, build) }

// --- whenBusinessConnectionEventFromUser (terminal) ---

@JvmName("whenBusinessConnectionEventFromUserBusinessConnectionEvent")
fun HandlerRoute<BusinessConnectionEvent>.whenBusinessConnectionEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<BusinessConnectionEvent>.() -> Unit
) = select({ if (it.event.businessConnection.user.id == userId) it else null }) { handle(handler) }

@JvmName("whenBusinessConnectionEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenBusinessConnectionEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<BusinessConnectionEvent>.() -> Unit
) = onBusinessConnectionEvent { whenBusinessConnectionEventFromUser(userId, handler) }

// ==================== BusinessMessageEvent Filtered Matchers ====================

// --- onBusinessMessageEventFromUser (stackable) ---

@JvmName("onBusinessMessageEventFromUserBusinessMessageEvent")
fun HandlerRoute<BusinessMessageEvent>.onBusinessMessageEventFromUser(
    userId: Long,
    build: HandlerRoute<BusinessMessageEvent>.() -> Unit
) = select({ if (it.event.businessMessage.from?.id == userId) it else null }, build)

@JvmName("onBusinessMessageEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onBusinessMessageEventFromUser(
    userId: Long,
    build: HandlerRoute<BusinessMessageEvent>.() -> Unit
) = onBusinessMessageEvent { onBusinessMessageEventFromUser(userId, build) }

// --- whenBusinessMessageEventFromUser (terminal) ---

@JvmName("whenBusinessMessageEventFromUserBusinessMessageEvent")
fun HandlerRoute<BusinessMessageEvent>.whenBusinessMessageEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<BusinessMessageEvent>.() -> Unit
) = select({ if (it.event.businessMessage.from?.id == userId) it else null }) { handle(handler) }

@JvmName("whenBusinessMessageEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenBusinessMessageEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<BusinessMessageEvent>.() -> Unit
) = onBusinessMessageEvent { whenBusinessMessageEventFromUser(userId, handler) }

// --- onBusinessMessageEventInChat (stackable) ---

@JvmName("onBusinessMessageEventInChatBusinessMessageEvent")
fun HandlerRoute<BusinessMessageEvent>.onBusinessMessageEventInChat(
    chatId: Long,
    build: HandlerRoute<BusinessMessageEvent>.() -> Unit
) = select({ if (it.event.businessMessage.chat.id == chatId) it else null }, build)

@JvmName("onBusinessMessageEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onBusinessMessageEventInChat(
    chatId: Long,
    build: HandlerRoute<BusinessMessageEvent>.() -> Unit
) = onBusinessMessageEvent { onBusinessMessageEventInChat(chatId, build) }

// --- whenBusinessMessageEventInChat (terminal) ---

@JvmName("whenBusinessMessageEventInChatBusinessMessageEvent")
fun HandlerRoute<BusinessMessageEvent>.whenBusinessMessageEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<BusinessMessageEvent>.() -> Unit
) = select({ if (it.event.businessMessage.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenBusinessMessageEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenBusinessMessageEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<BusinessMessageEvent>.() -> Unit
) = onBusinessMessageEvent { whenBusinessMessageEventInChat(chatId, handler) }

// ==================== EditedBusinessMessageEvent Filtered Matchers ====================

// --- onEditedBusinessMessageEventFromUser (stackable) ---

@JvmName("onEditedBusinessMessageEventFromUserEditedBusinessMessageEvent")
fun HandlerRoute<EditedBusinessMessageEvent>.onEditedBusinessMessageEventFromUser(
    userId: Long,
    build: HandlerRoute<EditedBusinessMessageEvent>.() -> Unit
) = select({ if (it.event.editedBusinessMessage.from?.id == userId) it else null }, build)

@JvmName("onEditedBusinessMessageEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedBusinessMessageEventFromUser(
    userId: Long,
    build: HandlerRoute<EditedBusinessMessageEvent>.() -> Unit
) = onEditedBusinessMessageEvent { onEditedBusinessMessageEventFromUser(userId, build) }

// --- whenEditedBusinessMessageEventFromUser (terminal) ---

@JvmName("whenEditedBusinessMessageEventFromUserEditedBusinessMessageEvent")
fun HandlerRoute<EditedBusinessMessageEvent>.whenEditedBusinessMessageEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<EditedBusinessMessageEvent>.() -> Unit
) = select({ if (it.event.editedBusinessMessage.from?.id == userId) it else null }) { handle(handler) }

@JvmName("whenEditedBusinessMessageEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedBusinessMessageEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<EditedBusinessMessageEvent>.() -> Unit
) = onEditedBusinessMessageEvent { whenEditedBusinessMessageEventFromUser(userId, handler) }

// --- onEditedBusinessMessageEventInChat (stackable) ---

@JvmName("onEditedBusinessMessageEventInChatEditedBusinessMessageEvent")
fun HandlerRoute<EditedBusinessMessageEvent>.onEditedBusinessMessageEventInChat(
    chatId: Long,
    build: HandlerRoute<EditedBusinessMessageEvent>.() -> Unit
) = select({ if (it.event.editedBusinessMessage.chat.id == chatId) it else null }, build)

@JvmName("onEditedBusinessMessageEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedBusinessMessageEventInChat(
    chatId: Long,
    build: HandlerRoute<EditedBusinessMessageEvent>.() -> Unit
) = onEditedBusinessMessageEvent { onEditedBusinessMessageEventInChat(chatId, build) }

// --- whenEditedBusinessMessageEventInChat (terminal) ---

@JvmName("whenEditedBusinessMessageEventInChatEditedBusinessMessageEvent")
fun HandlerRoute<EditedBusinessMessageEvent>.whenEditedBusinessMessageEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<EditedBusinessMessageEvent>.() -> Unit
) = select({ if (it.event.editedBusinessMessage.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenEditedBusinessMessageEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedBusinessMessageEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<EditedBusinessMessageEvent>.() -> Unit
) = onEditedBusinessMessageEvent { whenEditedBusinessMessageEventInChat(chatId, handler) }

// ==================== DeletedBusinessMessagesEvent Filtered Matchers ====================

// --- onDeletedBusinessMessagesEventInChat (stackable) ---

@JvmName("onDeletedBusinessMessagesEventInChatDeletedBusinessMessagesEvent")
fun HandlerRoute<DeletedBusinessMessagesEvent>.onDeletedBusinessMessagesEventInChat(
    chatId: Long,
    build: HandlerRoute<DeletedBusinessMessagesEvent>.() -> Unit
) = select({ if (it.event.deletedBusinessMessages.chat.id == chatId) it else null }, build)

@JvmName("onDeletedBusinessMessagesEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onDeletedBusinessMessagesEventInChat(
    chatId: Long,
    build: HandlerRoute<DeletedBusinessMessagesEvent>.() -> Unit
) = onDeletedBusinessMessagesEvent { onDeletedBusinessMessagesEventInChat(chatId, build) }

// --- whenDeletedBusinessMessagesEventInChat (terminal) ---

@JvmName("whenDeletedBusinessMessagesEventInChatDeletedBusinessMessagesEvent")
fun HandlerRoute<DeletedBusinessMessagesEvent>.whenDeletedBusinessMessagesEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<DeletedBusinessMessagesEvent>.() -> Unit
) = select({ if (it.event.deletedBusinessMessages.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenDeletedBusinessMessagesEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenDeletedBusinessMessagesEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<DeletedBusinessMessagesEvent>.() -> Unit
) = onDeletedBusinessMessagesEvent { whenDeletedBusinessMessagesEventInChat(chatId, handler) }
