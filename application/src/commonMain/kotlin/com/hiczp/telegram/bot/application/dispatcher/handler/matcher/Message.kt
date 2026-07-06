@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.constant.ChatType
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import kotlin.jvm.JvmName

fun HandlerRoute<MessageEvent>.text(value: String, build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.text == value }, build)

fun HandlerRoute<MessageEvent>.text(pattern: Regex, build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.text?.matches(pattern) == true }, build)

fun HandlerRoute<MessageEvent>.textContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = filter({ event.message.text?.contains(substring, ignoreCase) == true }, build)

fun HandlerRoute<MessageEvent>.textStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = filter({ event.message.text?.startsWith(prefix, ignoreCase) == true }, build)

fun HandlerRoute<MessageEvent>.textEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = filter({ event.message.text?.endsWith(suffix, ignoreCase) == true }, build)

fun HandlerRoute<MessageEvent>.photo(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.photo != null }, build)

fun HandlerRoute<MessageEvent>.video(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.video != null }, build)

fun HandlerRoute<MessageEvent>.document(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.document != null }, build)

fun HandlerRoute<MessageEvent>.audio(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.audio != null }, build)

fun HandlerRoute<MessageEvent>.sticker(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.sticker != null }, build)

fun HandlerRoute<MessageEvent>.voice(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.voice != null }, build)

fun HandlerRoute<MessageEvent>.videoNote(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.videoNote != null }, build)

fun HandlerRoute<MessageEvent>.animation(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.animation != null }, build)

fun HandlerRoute<MessageEvent>.contact(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.contact != null }, build)

fun HandlerRoute<MessageEvent>.location(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.location != null }, build)

fun HandlerRoute<MessageEvent>.venue(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.venue != null }, build)

fun HandlerRoute<MessageEvent>.poll(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.poll != null }, build)

fun HandlerRoute<MessageEvent>.dice(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.dice != null }, build)

fun HandlerRoute<MessageEvent>.dice(emoji: String, build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.dice?.emoji == emoji }, build)

fun HandlerRoute<MessageEvent>.reply(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.replyToMessage != null }, build)

fun HandlerRoute<MessageEvent>.replyTo(messageId: Long, build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.replyToMessage?.messageId == messageId }, build)

@JvmName("messageFromUser")
fun HandlerRoute<MessageEvent>.fromUser(userId: Long, build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.from?.id == userId }, build)

@JvmName("messageFromUsers")
fun HandlerRoute<MessageEvent>.fromUsers(userIds: Set<Long>, build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.from?.id in userIds }, build)

@JvmName("messageInChat")
fun HandlerRoute<MessageEvent>.inChat(chatId: Long, build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.chat.id == chatId }, build)

@JvmName("messageInChats")
fun HandlerRoute<MessageEvent>.inChats(chatIds: Set<Long>, build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.chat.id in chatIds }, build)

fun HandlerRoute<MessageEvent>.privateChat(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.chat.type == ChatType.PRIVATE }, build)

fun HandlerRoute<MessageEvent>.groupChat(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.chat.type == ChatType.GROUP }, build)

fun HandlerRoute<MessageEvent>.supergroupChat(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.chat.type == ChatType.SUPERGROUP }, build)

fun HandlerRoute<MessageEvent>.channelChat(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.chat.type == ChatType.CHANNEL }, build)

fun HandlerRoute<MessageEvent>.forwarded(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.forwardOrigin != null }, build)

fun HandlerRoute<MessageEvent>.newChatMembers(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.newChatMembers != null }, build)

fun HandlerRoute<MessageEvent>.leftChatMember(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.leftChatMember != null }, build)

fun HandlerRoute<MessageEvent>.pinnedMessage(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter({ event.message.pinnedMessage != null }, build)
