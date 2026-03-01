@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.*
import kotlin.jvm.JvmName

// ==================== BusinessConnectionEvent Filtered Matchers ====================

// --- onBusinessConnectionFromUser (stackable) ---

@JvmName("onBusinessConnectionFromUserBusinessConnectionEvent")
fun HandlerRoute<BusinessConnectionEvent>.onBusinessConnectionFromUser(
    userId: Long,
    build: HandlerRoute<BusinessConnectionEvent>.() -> Unit
) = select({ if (it.event.businessConnection.user.id == userId) it else null }, build)

@JvmName("onBusinessConnectionFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onBusinessConnectionFromUser(
    userId: Long,
    build: HandlerRoute<BusinessConnectionEvent>.() -> Unit
) = onBusinessConnectionEvent { onBusinessConnectionFromUser(userId, build) }

// --- whenBusinessConnectionFromUser (terminal) ---

@JvmName("whenBusinessConnectionFromUserBusinessConnectionEvent")
fun HandlerRoute<BusinessConnectionEvent>.whenBusinessConnectionFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<BusinessConnectionEvent>.() -> Unit
) = select({ if (it.event.businessConnection.user.id == userId) it else null }) { handle(handler) }

@JvmName("whenBusinessConnectionFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenBusinessConnectionFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<BusinessConnectionEvent>.() -> Unit
) = onBusinessConnectionEvent { whenBusinessConnectionFromUser(userId, handler) }

// ==================== BusinessMessageEvent Filtered Matchers ====================

// --- onBusinessMessageFromUser (stackable) ---

@JvmName("onBusinessMessageFromUserBusinessMessageEvent")
fun HandlerRoute<BusinessMessageEvent>.onBusinessMessageFromUser(
    userId: Long,
    build: HandlerRoute<BusinessMessageEvent>.() -> Unit
) = select({ if (it.event.businessMessage.from?.id == userId) it else null }, build)

@JvmName("onBusinessMessageFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onBusinessMessageFromUser(
    userId: Long,
    build: HandlerRoute<BusinessMessageEvent>.() -> Unit
) = onBusinessMessageEvent { onBusinessMessageFromUser(userId, build) }

// --- whenBusinessMessageFromUser (terminal) ---

@JvmName("whenBusinessMessageFromUserBusinessMessageEvent")
fun HandlerRoute<BusinessMessageEvent>.whenBusinessMessageFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<BusinessMessageEvent>.() -> Unit
) = select({ if (it.event.businessMessage.from?.id == userId) it else null }) { handle(handler) }

@JvmName("whenBusinessMessageFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenBusinessMessageFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<BusinessMessageEvent>.() -> Unit
) = onBusinessMessageEvent { whenBusinessMessageFromUser(userId, handler) }

// --- onBusinessMessageInChat (stackable) ---

@JvmName("onBusinessMessageInChatBusinessMessageEvent")
fun HandlerRoute<BusinessMessageEvent>.onBusinessMessageInChat(
    chatId: Long,
    build: HandlerRoute<BusinessMessageEvent>.() -> Unit
) = select({ if (it.event.businessMessage.chat.id == chatId) it else null }, build)

@JvmName("onBusinessMessageInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onBusinessMessageInChat(
    chatId: Long,
    build: HandlerRoute<BusinessMessageEvent>.() -> Unit
) = onBusinessMessageEvent { onBusinessMessageInChat(chatId, build) }

// --- whenBusinessMessageInChat (terminal) ---

@JvmName("whenBusinessMessageInChatBusinessMessageEvent")
fun HandlerRoute<BusinessMessageEvent>.whenBusinessMessageInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<BusinessMessageEvent>.() -> Unit
) = select({ if (it.event.businessMessage.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenBusinessMessageInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenBusinessMessageInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<BusinessMessageEvent>.() -> Unit
) = onBusinessMessageEvent { whenBusinessMessageInChat(chatId, handler) }

// ==================== EditedBusinessMessageEvent Filtered Matchers ====================

// --- onEditedBusinessMessageFromUser (stackable) ---

@JvmName("onEditedBusinessMessageFromUserEditedBusinessMessageEvent")
fun HandlerRoute<EditedBusinessMessageEvent>.onEditedBusinessMessageFromUser(
    userId: Long,
    build: HandlerRoute<EditedBusinessMessageEvent>.() -> Unit
) = select({ if (it.event.editedBusinessMessage.from?.id == userId) it else null }, build)

@JvmName("onEditedBusinessMessageFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedBusinessMessageFromUser(
    userId: Long,
    build: HandlerRoute<EditedBusinessMessageEvent>.() -> Unit
) = onEditedBusinessMessageEvent { onEditedBusinessMessageFromUser(userId, build) }

// --- whenEditedBusinessMessageFromUser (terminal) ---

@JvmName("whenEditedBusinessMessageFromUserEditedBusinessMessageEvent")
fun HandlerRoute<EditedBusinessMessageEvent>.whenEditedBusinessMessageFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<EditedBusinessMessageEvent>.() -> Unit
) = select({ if (it.event.editedBusinessMessage.from?.id == userId) it else null }) { handle(handler) }

@JvmName("whenEditedBusinessMessageFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedBusinessMessageFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<EditedBusinessMessageEvent>.() -> Unit
) = onEditedBusinessMessageEvent { whenEditedBusinessMessageFromUser(userId, handler) }

// --- onEditedBusinessMessageInChat (stackable) ---

@JvmName("onEditedBusinessMessageInChatEditedBusinessMessageEvent")
fun HandlerRoute<EditedBusinessMessageEvent>.onEditedBusinessMessageInChat(
    chatId: Long,
    build: HandlerRoute<EditedBusinessMessageEvent>.() -> Unit
) = select({ if (it.event.editedBusinessMessage.chat.id == chatId) it else null }, build)

@JvmName("onEditedBusinessMessageInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedBusinessMessageInChat(
    chatId: Long,
    build: HandlerRoute<EditedBusinessMessageEvent>.() -> Unit
) = onEditedBusinessMessageEvent { onEditedBusinessMessageInChat(chatId, build) }

// --- whenEditedBusinessMessageInChat (terminal) ---

@JvmName("whenEditedBusinessMessageInChatEditedBusinessMessageEvent")
fun HandlerRoute<EditedBusinessMessageEvent>.whenEditedBusinessMessageInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<EditedBusinessMessageEvent>.() -> Unit
) = select({ if (it.event.editedBusinessMessage.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenEditedBusinessMessageInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedBusinessMessageInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<EditedBusinessMessageEvent>.() -> Unit
) = onEditedBusinessMessageEvent { whenEditedBusinessMessageInChat(chatId, handler) }

// ==================== DeletedBusinessMessagesEvent Filtered Matchers ====================

// --- onDeletedBusinessMessagesInChat (stackable) ---

@JvmName("onDeletedBusinessMessagesInChatDeletedBusinessMessagesEvent")
fun HandlerRoute<DeletedBusinessMessagesEvent>.onDeletedBusinessMessagesInChat(
    chatId: Long,
    build: HandlerRoute<DeletedBusinessMessagesEvent>.() -> Unit
) = select({ if (it.event.deletedBusinessMessages.chat.id == chatId) it else null }, build)

@JvmName("onDeletedBusinessMessagesInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onDeletedBusinessMessagesInChat(
    chatId: Long,
    build: HandlerRoute<DeletedBusinessMessagesEvent>.() -> Unit
) = onDeletedBusinessMessagesEvent { onDeletedBusinessMessagesInChat(chatId, build) }

// --- whenDeletedBusinessMessagesInChat (terminal) ---

@JvmName("whenDeletedBusinessMessagesInChatDeletedBusinessMessagesEvent")
fun HandlerRoute<DeletedBusinessMessagesEvent>.whenDeletedBusinessMessagesInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<DeletedBusinessMessagesEvent>.() -> Unit
) = select({ if (it.event.deletedBusinessMessages.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenDeletedBusinessMessagesInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenDeletedBusinessMessagesInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<DeletedBusinessMessagesEvent>.() -> Unit
) = onDeletedBusinessMessagesEvent { whenDeletedBusinessMessagesInChat(chatId, handler) }
