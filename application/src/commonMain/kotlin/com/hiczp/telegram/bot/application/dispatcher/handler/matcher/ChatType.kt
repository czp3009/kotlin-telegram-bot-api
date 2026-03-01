@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.constant.ChatType
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlin.jvm.JvmName

// --- onPrivateChat (stackable) ---

@JvmName("onPrivateChatMessageEvent")
fun HandlerRoute<MessageEvent>.onPrivateChat(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.chat.type == ChatType.PRIVATE) it else null }, build)

@JvmName("onPrivateChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onPrivateChat(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onPrivateChat(build) }

// --- whenPrivateChat (terminal) ---

@JvmName("whenPrivateChatMessageEvent")
fun HandlerRoute<MessageEvent>.whenPrivateChat(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.chat.type == ChatType.PRIVATE) it else null }) { handle(handler) }

@JvmName("whenPrivateChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenPrivateChat(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenPrivateChat(handler) }

// --- onGroupChat (stackable) ---

@JvmName("onGroupChatMessageEvent")
fun HandlerRoute<MessageEvent>.onGroupChat(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.chat.type == ChatType.GROUP) it else null }, build)

@JvmName("onGroupChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onGroupChat(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onGroupChat(build) }

// --- whenGroupChat (terminal) ---

@JvmName("whenGroupChatMessageEvent")
fun HandlerRoute<MessageEvent>.whenGroupChat(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.chat.type == ChatType.GROUP) it else null }) { handle(handler) }

@JvmName("whenGroupChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenGroupChat(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenGroupChat(handler) }

// --- onSupergroupChat (stackable) ---

@JvmName("onSupergroupChatMessageEvent")
fun HandlerRoute<MessageEvent>.onSupergroupChat(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.chat.type == ChatType.SUPERGROUP) it else null }, build)

@JvmName("onSupergroupChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onSupergroupChat(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onSupergroupChat(build) }

// --- whenSupergroupChat (terminal) ---

@JvmName("whenSupergroupChatMessageEvent")
fun HandlerRoute<MessageEvent>.whenSupergroupChat(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.chat.type == ChatType.SUPERGROUP) it else null }) { handle(handler) }

@JvmName("whenSupergroupChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenSupergroupChat(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenSupergroupChat(handler) }

// --- onChannel (stackable) ---

@JvmName("onChannelMessageEvent")
fun HandlerRoute<MessageEvent>.onChannel(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.chat.type == ChatType.CHANNEL) it else null }, build)

@JvmName("onChannelTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onChannel(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onChannel(build) }

// --- whenChannel (terminal) ---

@JvmName("whenChannelMessageEvent")
fun HandlerRoute<MessageEvent>.whenChannel(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.chat.type == ChatType.CHANNEL) it else null }) { handle(handler) }

@JvmName("whenChannelTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenChannel(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenChannel(handler) }
