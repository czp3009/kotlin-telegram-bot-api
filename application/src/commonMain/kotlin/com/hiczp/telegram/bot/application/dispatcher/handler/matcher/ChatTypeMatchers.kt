package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.EventRoute
import com.hiczp.telegram.bot.protocol.constant.ChatType
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlinx.coroutines.CoroutineScope
import kotlin.jvm.JvmName

@JvmName("privateChatMessageEvent")
fun EventRoute<MessageEvent>.privateChat(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.chat.type == ChatType.PRIVATE) it else null }, build)

@JvmName("privateChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.privateChat(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { privateChat(build) }

@JvmName("whenPrivateChatMessageEvent")
fun EventRoute<MessageEvent>.whenPrivateChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = privateChat { handle(handler) }

@JvmName("whenPrivateChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenPrivateChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenPrivateChat(handler) }

@JvmName("groupChatMessageEvent")
fun EventRoute<MessageEvent>.groupChat(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.chat.type == ChatType.GROUP) it else null }, build)

@JvmName("groupChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.groupChat(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { groupChat(build) }

@JvmName("whenGroupChatMessageEvent")
fun EventRoute<MessageEvent>.whenGroupChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = groupChat { handle(handler) }

@JvmName("whenGroupChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenGroupChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenGroupChat(handler) }

@JvmName("supergroupChatMessageEvent")
fun EventRoute<MessageEvent>.supergroupChat(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.chat.type == ChatType.SUPERGROUP) it else null }, build)

@JvmName("supergroupChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.supergroupChat(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { supergroupChat(build) }

@JvmName("whenSupergroupChatMessageEvent")
fun EventRoute<MessageEvent>.whenSupergroupChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = supergroupChat { handle(handler) }

@JvmName("whenSupergroupChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenSupergroupChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenSupergroupChat(handler) }

@JvmName("channelMessageEvent")
fun EventRoute<MessageEvent>.channel(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.chat.type == ChatType.CHANNEL) it else null }, build)

@JvmName("channelTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.channel(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { channel(build) }

@JvmName("whenChannelMessageEvent")
fun EventRoute<MessageEvent>.whenChannel(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = channel { handle(handler) }

@JvmName("whenChannelTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenChannel(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenChannel(handler) }
