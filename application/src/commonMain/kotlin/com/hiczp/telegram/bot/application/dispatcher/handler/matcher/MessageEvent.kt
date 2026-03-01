@file:Suppress("unused", "DuplicatedCode")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import com.hiczp.telegram.bot.protocol.model.MessageOriginChannel
import com.hiczp.telegram.bot.protocol.model.MessageOriginChat
import com.hiczp.telegram.bot.protocol.model.MessageOriginHiddenUser
import com.hiczp.telegram.bot.protocol.model.MessageOriginUser
import kotlin.jvm.JvmName

// ==================== Text Matchers ====================

// --- onText (stackable) ---

@JvmName("onTextMessageEvent")
fun HandlerRoute<MessageEvent>.onText(
    exact: String,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({ if (it.event.message.text == exact) it else null }, build)

@JvmName("onTextTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onText(
    exact: String,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onText(exact, build) }

// --- whenText (terminal) ---

@JvmName("whenTextMessageEvent")
fun HandlerRoute<MessageEvent>.whenText(
    exact: String,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({ if (it.event.message.text == exact) it else null }) { handle(handler) }

@JvmName("whenTextTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenText(
    exact: String,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenText(exact, handler) }

// --- onTextRegex (stackable) ---

@JvmName("onTextRegexMessageEvent")
fun HandlerRoute<MessageEvent>.onTextRegex(
    pattern: Regex,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({ if (it.event.message.text?.matches(pattern) == true) it else null }, build)

@JvmName("onTextRegexTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onTextRegex(
    pattern: Regex,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onTextRegex(pattern, build) }

// --- whenTextRegex (terminal) ---

@JvmName("whenTextRegexMessageEvent")
fun HandlerRoute<MessageEvent>.whenTextRegex(
    pattern: Regex,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({ if (it.event.message.text?.matches(pattern) == true) it else null }) { handle(handler) }

@JvmName("whenTextRegexTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenTextRegex(
    pattern: Regex,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenTextRegex(pattern, handler) }

// --- onTextContains (stackable) ---

@JvmName("onTextContainsMessageEvent")
fun HandlerRoute<MessageEvent>.onTextContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({ if (it.event.message.text?.contains(substring, ignoreCase) == true) it else null }, build)

@JvmName("onTextContainsTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onTextContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onTextContains(substring, ignoreCase, build) }

// --- whenTextContains (terminal) ---

@JvmName("whenTextContainsMessageEvent")
fun HandlerRoute<MessageEvent>.whenTextContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({ if (it.event.message.text?.contains(substring, ignoreCase) == true) it else null }) { handle(handler) }

@JvmName("whenTextContainsTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenTextContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenTextContains(substring, ignoreCase, handler) }

// --- onTextStartsWith (stackable) ---

@JvmName("onTextStartsWithMessageEvent")
fun HandlerRoute<MessageEvent>.onTextStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({ if (it.event.message.text?.startsWith(prefix, ignoreCase) == true) it else null }, build)

@JvmName("onTextStartsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onTextStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onTextStartsWith(prefix, ignoreCase, build) }

// --- whenTextStartsWith (terminal) ---

@JvmName("whenTextStartsWithMessageEvent")
fun HandlerRoute<MessageEvent>.whenTextStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({ if (it.event.message.text?.startsWith(prefix, ignoreCase) == true) it else null }) { handle(handler) }

@JvmName("whenTextStartsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenTextStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenTextStartsWith(prefix, ignoreCase, handler) }

// --- onTextEndsWith (stackable) ---

@JvmName("onTextEndsWithMessageEvent")
fun HandlerRoute<MessageEvent>.onTextEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({ if (it.event.message.text?.endsWith(suffix, ignoreCase) == true) it else null }, build)

@JvmName("onTextEndsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onTextEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onTextEndsWith(suffix, ignoreCase, build) }

// --- whenTextEndsWith (terminal) ---

@JvmName("whenTextEndsWithMessageEvent")
fun HandlerRoute<MessageEvent>.whenTextEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({ if (it.event.message.text?.endsWith(suffix, ignoreCase) == true) it else null }) { handle(handler) }

@JvmName("whenTextEndsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenTextEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenTextEndsWith(suffix, ignoreCase, handler) }

// ==================== Media Matchers ====================

// --- onPhoto (stackable) ---

@JvmName("onPhotoMessageEvent")
fun HandlerRoute<MessageEvent>.onPhoto(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.photo != null) it else null }, build)

@JvmName("onPhotoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onPhoto(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onPhoto(build) }

// --- whenPhoto (terminal) ---

@JvmName("whenPhotoMessageEvent")
fun HandlerRoute<MessageEvent>.whenPhoto(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.photo != null) it else null }) { handle(handler) }

@JvmName("whenPhotoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenPhoto(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenPhoto(handler) }

// --- onVideo (stackable) ---

@JvmName("onVideoMessageEvent")
fun HandlerRoute<MessageEvent>.onVideo(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.video != null) it else null }, build)

@JvmName("onVideoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onVideo(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onVideo(build) }

// --- whenVideo (terminal) ---

@JvmName("whenVideoMessageEvent")
fun HandlerRoute<MessageEvent>.whenVideo(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.video != null) it else null }) { handle(handler) }

@JvmName("whenVideoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenVideo(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenVideo(handler) }

// --- onAudio (stackable) ---

@JvmName("onAudioMessageEvent")
fun HandlerRoute<MessageEvent>.onAudio(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.audio != null) it else null }, build)

@JvmName("onAudioTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onAudio(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onAudio(build) }

// --- whenAudio (terminal) ---

@JvmName("whenAudioMessageEvent")
fun HandlerRoute<MessageEvent>.whenAudio(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.audio != null) it else null }) { handle(handler) }

@JvmName("whenAudioTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenAudio(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenAudio(handler) }

// --- onDocument (stackable) ---

@JvmName("onDocumentMessageEvent")
fun HandlerRoute<MessageEvent>.onDocument(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.document != null) it else null }, build)

@JvmName("onDocumentTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onDocument(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onDocument(build) }

// --- whenDocument (terminal) ---

@JvmName("whenDocumentMessageEvent")
fun HandlerRoute<MessageEvent>.whenDocument(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.document != null) it else null }) { handle(handler) }

@JvmName("whenDocumentTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenDocument(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenDocument(handler) }

// --- onSticker (stackable) ---

@JvmName("onStickerMessageEvent")
fun HandlerRoute<MessageEvent>.onSticker(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.sticker != null) it else null }, build)

@JvmName("onStickerTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onSticker(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onSticker(build) }

// --- whenSticker (terminal) ---

@JvmName("whenStickerMessageEvent")
fun HandlerRoute<MessageEvent>.whenSticker(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.sticker != null) it else null }) { handle(handler) }

@JvmName("whenStickerTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenSticker(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenSticker(handler) }

// --- onVoice (stackable) ---

@JvmName("onVoiceMessageEvent")
fun HandlerRoute<MessageEvent>.onVoice(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.voice != null) it else null }, build)

@JvmName("onVoiceTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onVoice(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onVoice(build) }

// --- whenVoice (terminal) ---

@JvmName("whenVoiceMessageEvent")
fun HandlerRoute<MessageEvent>.whenVoice(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.voice != null) it else null }) { handle(handler) }

@JvmName("whenVoiceTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenVoice(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenVoice(handler) }

// --- onVideoNote (stackable) ---

@JvmName("onVideoNoteMessageEvent")
fun HandlerRoute<MessageEvent>.onVideoNote(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.videoNote != null) it else null }, build)

@JvmName("onVideoNoteTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onVideoNote(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onVideoNote(build) }

// --- whenVideoNote (terminal) ---

@JvmName("whenVideoNoteMessageEvent")
fun HandlerRoute<MessageEvent>.whenVideoNote(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.videoNote != null) it else null }) { handle(handler) }

@JvmName("whenVideoNoteTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenVideoNote(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenVideoNote(handler) }

// --- onAnimation (stackable) ---

@JvmName("onAnimationMessageEvent")
fun HandlerRoute<MessageEvent>.onAnimation(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.animation != null) it else null }, build)

@JvmName("onAnimationTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onAnimation(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onAnimation(build) }

// --- whenAnimation (terminal) ---

@JvmName("whenAnimationMessageEvent")
fun HandlerRoute<MessageEvent>.whenAnimation(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.animation != null) it else null }) { handle(handler) }

@JvmName("whenAnimationTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenAnimation(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenAnimation(handler) }

// --- onContact (stackable) ---

@JvmName("onContactMessageEvent")
fun HandlerRoute<MessageEvent>.onContact(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.contact != null) it else null }, build)

@JvmName("onContactTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onContact(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onContact(build) }

// --- whenContact (terminal) ---

@JvmName("whenContactMessageEvent")
fun HandlerRoute<MessageEvent>.whenContact(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.contact != null) it else null }) { handle(handler) }

@JvmName("whenContactTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenContact(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenContact(handler) }

// --- onLocation (stackable) ---

@JvmName("onLocationMessageEvent")
fun HandlerRoute<MessageEvent>.onLocation(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.location != null) it else null }, build)

@JvmName("onLocationTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onLocation(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onLocation(build) }

// --- whenLocation (terminal) ---

@JvmName("whenLocationMessageEvent")
fun HandlerRoute<MessageEvent>.whenLocation(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.location != null) it else null }) { handle(handler) }

@JvmName("whenLocationTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenLocation(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenLocation(handler) }

// --- onVenue (stackable) ---

@JvmName("onVenueMessageEvent")
fun HandlerRoute<MessageEvent>.onVenue(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.venue != null) it else null }, build)

@JvmName("onVenueTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onVenue(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onVenue(build) }

// --- whenVenue (terminal) ---

@JvmName("whenVenueMessageEvent")
fun HandlerRoute<MessageEvent>.whenVenue(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.venue != null) it else null }) { handle(handler) }

@JvmName("whenVenueTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenVenue(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenVenue(handler) }

// --- onPoll (stackable) ---

@JvmName("onPollMessageEvent")
fun HandlerRoute<MessageEvent>.onPoll(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.poll != null) it else null }, build)

@JvmName("onPollTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onPoll(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onPoll(build) }

// --- whenPoll (terminal) ---

@JvmName("whenPollMessageEvent")
fun HandlerRoute<MessageEvent>.whenPoll(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.poll != null) it else null }) { handle(handler) }

@JvmName("whenPollTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenPoll(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenPoll(handler) }

// --- onDice (stackable) ---

@JvmName("onDiceMessageEvent")
fun HandlerRoute<MessageEvent>.onDice(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.dice != null) it else null }, build)

@JvmName("onDiceTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onDice(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onDice(build) }

// --- whenDice (terminal) ---

@JvmName("whenDiceMessageEvent")
fun HandlerRoute<MessageEvent>.whenDice(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.dice != null) it else null }) { handle(handler) }

@JvmName("whenDiceTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenDice(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenDice(handler) }

// --- onDice with emoji (stackable) ---

@JvmName("onDiceWithEmojiMessageEvent")
fun HandlerRoute<MessageEvent>.onDice(
    emoji: String,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({ if (it.event.message.dice?.emoji == emoji) it else null }, build)

@JvmName("onDiceWithEmojiTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onDice(
    emoji: String,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onDice(emoji, build) }

// --- whenDice with emoji (terminal) ---

@JvmName("whenDiceWithEmojiMessageEvent")
fun HandlerRoute<MessageEvent>.whenDice(
    emoji: String,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({ if (it.event.message.dice?.emoji == emoji) it else null }) { handle(handler) }

@JvmName("whenDiceWithEmojiTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenDice(
    emoji: String,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenDice(emoji, handler) }

// ==================== Reply Matchers ====================

// --- onReply (stackable) ---

@JvmName("onReplyMessageEvent")
fun HandlerRoute<MessageEvent>.onReply(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.replyToMessage != null) it else null }, build)

@JvmName("onReplyTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onReply(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onReply(build) }

// --- whenReply (terminal) ---

@JvmName("whenReplyMessageEvent")
fun HandlerRoute<MessageEvent>.whenReply(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.replyToMessage != null) it else null }) { handle(handler) }

@JvmName("whenReplyTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenReply(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenReply(handler) }

// --- onReplyTo (stackable) ---

@JvmName("onReplyToMessageEvent")
fun HandlerRoute<MessageEvent>.onReplyTo(
    messageId: Long,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({ if (it.event.message.replyToMessage?.messageId == messageId) it else null }, build)

@JvmName("onReplyToTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onReplyTo(
    messageId: Long,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onReplyTo(messageId, build) }

// --- whenReplyTo (terminal) ---

@JvmName("whenReplyToMessageEvent")
fun HandlerRoute<MessageEvent>.whenReplyTo(
    messageId: Long,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({ if (it.event.message.replyToMessage?.messageId == messageId) it else null }) { handle(handler) }

@JvmName("whenReplyToTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenReplyTo(
    messageId: Long,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenReplyTo(messageId, handler) }

// ==================== User/Chat Matchers ====================

// --- onFromUser (stackable) ---

@JvmName("onFromUserMessageEvent")
fun HandlerRoute<MessageEvent>.onFromUser(
    userId: Long,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({ if (it.event.message.from?.id == userId) it else null }, build)

@JvmName("onFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onFromUser(
    userId: Long,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onFromUser(userId, build) }

// --- whenFromUser (terminal) ---

@JvmName("whenFromUserMessageEvent")
fun HandlerRoute<MessageEvent>.whenFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({ if (it.event.message.from?.id == userId) it else null }) { handle(handler) }

@JvmName("whenFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenFromUser(userId, handler) }

// --- onFromUsers (stackable) ---

@JvmName("onFromUsersMessageEvent")
fun HandlerRoute<MessageEvent>.onFromUsers(
    userIds: Set<Long>,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({ if (it.event.message.from?.id in userIds) it else null }, build)

@JvmName("onFromUsersTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onFromUsers(
    userIds: Set<Long>,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onFromUsers(userIds, build) }

// --- whenFromUsers (terminal) ---

@JvmName("whenFromUsersMessageEvent")
fun HandlerRoute<MessageEvent>.whenFromUsers(
    userIds: Set<Long>,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({ if (it.event.message.from?.id in userIds) it else null }) { handle(handler) }

@JvmName("whenFromUsersTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenFromUsers(
    userIds: Set<Long>,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenFromUsers(userIds, handler) }

// --- onInChat (stackable) ---

@JvmName("onInChatMessageEvent")
fun HandlerRoute<MessageEvent>.onInChat(
    chatId: Long,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({ if (it.event.message.chat.id == chatId) it else null }, build)

@JvmName("onInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onInChat(
    chatId: Long,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onInChat(chatId, build) }

// --- whenInChat (terminal) ---

@JvmName("whenInChatMessageEvent")
fun HandlerRoute<MessageEvent>.whenInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({ if (it.event.message.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenInChat(chatId, handler) }

// --- onInChats (stackable) ---

@JvmName("onInChatsMessageEvent")
fun HandlerRoute<MessageEvent>.onInChats(
    chatIds: Set<Long>,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({ if (it.event.message.chat.id in chatIds) it else null }, build)

@JvmName("onInChatsTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onInChats(
    chatIds: Set<Long>,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onInChats(chatIds, build) }

// --- whenInChats (terminal) ---

@JvmName("whenInChatsMessageEvent")
fun HandlerRoute<MessageEvent>.whenInChats(
    chatIds: Set<Long>,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({ if (it.event.message.chat.id in chatIds) it else null }) { handle(handler) }

@JvmName("whenInChatsTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenInChats(
    chatIds: Set<Long>,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenInChats(chatIds, handler) }

// ==================== Forwarded Matchers ====================

// --- onForwarded (stackable) ---

@JvmName("onForwardedMessageEvent")
fun HandlerRoute<MessageEvent>.onForwarded(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forwardOrigin != null) it else null }, build)

@JvmName("onForwardedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onForwarded(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onForwarded(build) }

// --- whenForwarded (terminal) ---

@JvmName("whenForwardedMessageEvent")
fun HandlerRoute<MessageEvent>.whenForwarded(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forwardOrigin != null) it else null }) { handle(handler) }

@JvmName("whenForwardedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenForwarded(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenForwarded(handler) }

// --- onForwardedFromUser (stackable) ---

@JvmName("onForwardedFromUserMessageEvent")
fun HandlerRoute<MessageEvent>.onForwardedFromUser(
    userId: Long,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({
    val origin = it.event.message.forwardOrigin
    if (origin is MessageOriginUser && origin.senderUser.id == userId) it else null
}, build)

@JvmName("onForwardedFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onForwardedFromUser(
    userId: Long,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onForwardedFromUser(userId, build) }

// --- whenForwardedFromUser (terminal) ---

@JvmName("whenForwardedFromUserMessageEvent")
fun HandlerRoute<MessageEvent>.whenForwardedFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({
    val origin = it.event.message.forwardOrigin
    if (origin is MessageOriginUser && origin.senderUser.id == userId) it else null
}) { handle(handler) }

@JvmName("whenForwardedFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenForwardedFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenForwardedFromUser(userId, handler) }

// --- onForwardedFromChat (stackable) ---

@JvmName("onForwardedFromChatMessageEvent")
fun HandlerRoute<MessageEvent>.onForwardedFromChat(
    chatId: Long,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({
    val origin = it.event.message.forwardOrigin
    when (origin) {
        is MessageOriginChat -> if (origin.senderChat.id == chatId) it else null
        is MessageOriginChannel -> if (origin.chat.id == chatId) it else null
        else -> null
    }
}, build)

@JvmName("onForwardedFromChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onForwardedFromChat(
    chatId: Long,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onForwardedFromChat(chatId, build) }

// --- whenForwardedFromChat (terminal) ---

@JvmName("whenForwardedFromChatMessageEvent")
fun HandlerRoute<MessageEvent>.whenForwardedFromChat(
    chatId: Long,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({
    val origin = it.event.message.forwardOrigin
    when (origin) {
        is MessageOriginChat -> if (origin.senderChat.id == chatId) it else null
        is MessageOriginChannel -> if (origin.chat.id == chatId) it else null
        else -> null
    }
}) { handle(handler) }

@JvmName("whenForwardedFromChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenForwardedFromChat(
    chatId: Long,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenForwardedFromChat(chatId, handler) }

// --- onForwardedHiddenUser (stackable) ---

@JvmName("onForwardedHiddenUserMessageEvent")
fun HandlerRoute<MessageEvent>.onForwardedHiddenUser(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forwardOrigin is MessageOriginHiddenUser) it else null }, build)

@JvmName("onForwardedHiddenUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onForwardedHiddenUser(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onForwardedHiddenUser(build) }

// --- whenForwardedHiddenUser (terminal) ---

@JvmName("whenForwardedHiddenUserMessageEvent")
fun HandlerRoute<MessageEvent>.whenForwardedHiddenUser(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forwardOrigin is MessageOriginHiddenUser) it else null }) { handle(handler) }

@JvmName("whenForwardedHiddenUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenForwardedHiddenUser(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenForwardedHiddenUser(handler) }
