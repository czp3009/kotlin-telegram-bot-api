@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.constant.ChatType
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlin.jvm.JvmName

// --- onMessageEventPrivateChat (stackable) ---

@JvmName("onMessageEventPrivateChatMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventPrivateChat(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.chat.type == ChatType.PRIVATE) it else null }, build)

@JvmName("onMessageEventPrivateChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventPrivateChat(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventPrivateChat(build) }

// --- whenMessageEventPrivateChat (terminal) ---

@JvmName("whenMessageEventPrivateChatMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventPrivateChat(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.chat.type == ChatType.PRIVATE) it else null }) { handle(handler) }

@JvmName("whenMessageEventPrivateChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventPrivateChat(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventPrivateChat(handler) }

// --- onMessageEventGroupChat (stackable) ---

@JvmName("onMessageEventGroupChatMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventGroupChat(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.chat.type == ChatType.GROUP) it else null }, build)

@JvmName("onMessageEventGroupChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventGroupChat(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventGroupChat(build) }

// --- whenMessageEventGroupChat (terminal) ---

@JvmName("whenMessageEventGroupChatMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventGroupChat(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.chat.type == ChatType.GROUP) it else null }) { handle(handler) }

@JvmName("whenMessageEventGroupChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventGroupChat(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventGroupChat(handler) }

// --- onMessageEventSupergroupChat (stackable) ---

@JvmName("onMessageEventSupergroupChatMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventSupergroupChat(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.chat.type == ChatType.SUPERGROUP) it else null }, build)

@JvmName("onMessageEventSupergroupChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventSupergroupChat(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventSupergroupChat(build) }

// --- whenMessageEventSupergroupChat (terminal) ---

@JvmName("whenMessageEventSupergroupChatMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventSupergroupChat(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.chat.type == ChatType.SUPERGROUP) it else null }) { handle(handler) }

@JvmName("whenMessageEventSupergroupChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventSupergroupChat(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventSupergroupChat(handler) }

// --- onMessageEventChannel (stackable) ---

@JvmName("onMessageEventChannelMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventChannel(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.chat.type == ChatType.CHANNEL) it else null }, build)

@JvmName("onMessageEventChannelTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventChannel(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventChannel(build) }

// --- whenMessageEventChannel (terminal) ---

@JvmName("whenMessageEventChannelMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventChannel(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.chat.type == ChatType.CHANNEL) it else null }) { handle(handler) }

@JvmName("whenMessageEventChannelTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventChannel(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventChannel(handler) }
