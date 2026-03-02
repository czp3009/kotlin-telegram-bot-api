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

// --- onMessageEventText (stackable) ---

@JvmName("onMessageEventTextMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventText(
    exact: String,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({ if (it.event.message.text == exact) it else null }, build)

@JvmName("onMessageEventTextTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventText(
    exact: String,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onMessageEventText(exact, build) }

// --- whenMessageEventText (terminal) ---

@JvmName("whenMessageEventTextMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventText(
    exact: String,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({ if (it.event.message.text == exact) it else null }) { handle(handler) }

@JvmName("whenMessageEventTextTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventText(
    exact: String,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenMessageEventText(exact, handler) }

// --- onMessageEventTextRegex (stackable) ---

@JvmName("onMessageEventTextRegexMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventTextRegex(
    pattern: Regex,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({ if (it.event.message.text?.matches(pattern) == true) it else null }, build)

@JvmName("onMessageEventTextRegexTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventTextRegex(
    pattern: Regex,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onMessageEventTextRegex(pattern, build) }

// --- whenMessageEventTextRegex (terminal) ---

@JvmName("whenMessageEventTextRegexMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventTextRegex(
    pattern: Regex,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({ if (it.event.message.text?.matches(pattern) == true) it else null }) { handle(handler) }

@JvmName("whenMessageEventTextRegexTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventTextRegex(
    pattern: Regex,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenMessageEventTextRegex(pattern, handler) }

// --- onMessageEventTextContains (stackable) ---

@JvmName("onMessageEventTextContainsMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventTextContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({ if (it.event.message.text?.contains(substring, ignoreCase) == true) it else null }, build)

@JvmName("onMessageEventTextContainsTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventTextContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onMessageEventTextContains(substring, ignoreCase, build) }

// --- whenMessageEventTextContains (terminal) ---

@JvmName("whenMessageEventTextContainsMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventTextContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({ if (it.event.message.text?.contains(substring, ignoreCase) == true) it else null }) { handle(handler) }

@JvmName("whenMessageEventTextContainsTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventTextContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenMessageEventTextContains(substring, ignoreCase, handler) }

// --- onMessageEventTextStartsWith (stackable) ---

@JvmName("onMessageEventTextStartsWithMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventTextStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({ if (it.event.message.text?.startsWith(prefix, ignoreCase) == true) it else null }, build)

@JvmName("onMessageEventTextStartsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventTextStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onMessageEventTextStartsWith(prefix, ignoreCase, build) }

// --- whenMessageEventTextStartsWith (terminal) ---

@JvmName("whenMessageEventTextStartsWithMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventTextStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({ if (it.event.message.text?.startsWith(prefix, ignoreCase) == true) it else null }) { handle(handler) }

@JvmName("whenMessageEventTextStartsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventTextStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenMessageEventTextStartsWith(prefix, ignoreCase, handler) }

// --- onMessageEventTextEndsWith (stackable) ---

@JvmName("onMessageEventTextEndsWithMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventTextEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({ if (it.event.message.text?.endsWith(suffix, ignoreCase) == true) it else null }, build)

@JvmName("onMessageEventTextEndsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventTextEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onMessageEventTextEndsWith(suffix, ignoreCase, build) }

// --- whenMessageEventTextEndsWith (terminal) ---

@JvmName("whenMessageEventTextEndsWithMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventTextEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({ if (it.event.message.text?.endsWith(suffix, ignoreCase) == true) it else null }) { handle(handler) }

@JvmName("whenMessageEventTextEndsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventTextEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenMessageEventTextEndsWith(suffix, ignoreCase, handler) }

// ==================== Media Matchers ====================

// --- onMessageEventPhoto (stackable) ---

@JvmName("onMessageEventPhotoMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventPhoto(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.photo != null) it else null }, build)

@JvmName("onMessageEventPhotoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventPhoto(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventPhoto(build) }

// --- whenMessageEventPhoto (terminal) ---

@JvmName("whenMessageEventPhotoMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventPhoto(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.photo != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventPhotoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventPhoto(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventPhoto(handler) }

// --- onMessageEventVideo (stackable) ---

@JvmName("onMessageEventVideoMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventVideo(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.video != null) it else null }, build)

@JvmName("onMessageEventVideoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventVideo(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventVideo(build) }

// --- whenMessageEventVideo (terminal) ---

@JvmName("whenMessageEventVideoMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventVideo(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.video != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventVideoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventVideo(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventVideo(handler) }

// --- onMessageEventAudio (stackable) ---

@JvmName("onMessageEventAudioMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventAudio(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.audio != null) it else null }, build)

@JvmName("onMessageEventAudioTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventAudio(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventAudio(build) }

// --- whenMessageEventAudio (terminal) ---

@JvmName("whenMessageEventAudioMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventAudio(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.audio != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventAudioTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventAudio(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventAudio(handler) }

// --- onMessageEventDocument (stackable) ---

@JvmName("onMessageEventDocumentMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventDocument(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.document != null) it else null }, build)

@JvmName("onMessageEventDocumentTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventDocument(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventDocument(build) }

// --- whenMessageEventDocument (terminal) ---

@JvmName("whenMessageEventDocumentMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventDocument(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.document != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventDocumentTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventDocument(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventDocument(handler) }

// --- onMessageEventSticker (stackable) ---

@JvmName("onMessageEventStickerMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventSticker(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.sticker != null) it else null }, build)

@JvmName("onMessageEventStickerTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventSticker(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventSticker(build) }

// --- whenMessageEventSticker (terminal) ---

@JvmName("whenMessageEventStickerMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventSticker(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.sticker != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventStickerTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventSticker(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventSticker(handler) }

// --- onMessageEventVoice (stackable) ---

@JvmName("onMessageEventVoiceMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventVoice(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.voice != null) it else null }, build)

@JvmName("onMessageEventVoiceTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventVoice(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventVoice(build) }

// --- whenMessageEventVoice (terminal) ---

@JvmName("whenMessageEventVoiceMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventVoice(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.voice != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventVoiceTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventVoice(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventVoice(handler) }

// --- onMessageEventVideoNote (stackable) ---

@JvmName("onMessageEventVideoNoteMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventVideoNote(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.videoNote != null) it else null }, build)

@JvmName("onMessageEventVideoNoteTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventVideoNote(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventVideoNote(build) }

// --- whenMessageEventVideoNote (terminal) ---

@JvmName("whenMessageEventVideoNoteMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventVideoNote(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.videoNote != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventVideoNoteTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventVideoNote(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventVideoNote(handler) }

// --- onMessageEventAnimation (stackable) ---

@JvmName("onMessageEventAnimationMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventAnimation(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.animation != null) it else null }, build)

@JvmName("onMessageEventAnimationTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventAnimation(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventAnimation(build) }

// --- whenMessageEventAnimation (terminal) ---

@JvmName("whenMessageEventAnimationMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventAnimation(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.animation != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventAnimationTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventAnimation(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventAnimation(handler) }

// --- onMessageEventContact (stackable) ---

@JvmName("onMessageEventContactMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventContact(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.contact != null) it else null }, build)

@JvmName("onMessageEventContactTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventContact(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventContact(build) }

// --- whenMessageEventContact (terminal) ---

@JvmName("whenMessageEventContactMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventContact(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.contact != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventContactTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventContact(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventContact(handler) }

// --- onMessageEventLocation (stackable) ---

@JvmName("onMessageEventLocationMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventLocation(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.location != null) it else null }, build)

@JvmName("onMessageEventLocationTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventLocation(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventLocation(build) }

// --- whenMessageEventLocation (terminal) ---

@JvmName("whenMessageEventLocationMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventLocation(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.location != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventLocationTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventLocation(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventLocation(handler) }

// --- onMessageEventVenue (stackable) ---

@JvmName("onMessageEventVenueMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventVenue(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.venue != null) it else null }, build)

@JvmName("onMessageEventVenueTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventVenue(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventVenue(build) }

// --- whenMessageEventVenue (terminal) ---

@JvmName("whenMessageEventVenueMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventVenue(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.venue != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventVenueTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventVenue(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    onMessageEvent { whenMessageEventVenue(handler) }

// --- onMessageEventPoll (stackable) ---

@JvmName("onMessageEventPollMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventPoll(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.poll != null) it else null }, build)

@JvmName("onMessageEventPollTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventPoll(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventPoll(build) }

// --- whenMessageEventPoll (terminal) ---

@JvmName("whenMessageEventPollMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventPoll(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.poll != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventPollTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventPoll(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventPoll(handler) }

// --- onMessageEventDice (stackable) ---

@JvmName("onMessageEventDiceMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventDice(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.dice != null) it else null }, build)

@JvmName("onMessageEventDiceTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventDice(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventDice(build) }

// --- whenMessageEventDice (terminal) ---

@JvmName("whenMessageEventDiceMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventDice(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.dice != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventDiceTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventDice(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventDice(handler) }

// --- onMessageEventDiceWithEmoji (stackable) ---

@JvmName("onMessageEventDiceWithEmojiMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventDiceWithEmoji(
    emoji: String,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({ if (it.event.message.dice?.emoji == emoji) it else null }, build)

@JvmName("onMessageEventDiceWithEmojiTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventDiceWithEmoji(
    emoji: String,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onMessageEventDiceWithEmoji(emoji, build) }

// --- whenMessageEventDiceWithEmoji (terminal) ---

@JvmName("whenMessageEventDiceWithEmojiMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventDiceWithEmoji(
    emoji: String,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({ if (it.event.message.dice?.emoji == emoji) it else null }) { handle(handler) }

@JvmName("whenMessageEventDiceWithEmojiTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventDiceWithEmoji(
    emoji: String,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenMessageEventDiceWithEmoji(emoji, handler) }

// ==================== Reply Matchers ====================

// --- onMessageEventReply (stackable) ---

@JvmName("onMessageEventReplyMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventReply(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.replyToMessage != null) it else null }, build)

@JvmName("onMessageEventReplyTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventReply(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventReply(build) }

// --- whenMessageEventReply (terminal) ---

@JvmName("whenMessageEventReplyMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventReply(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.replyToMessage != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventReplyTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventReply(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventReply(handler) }

// --- onMessageEventReplyTo (stackable) ---

@JvmName("onMessageEventReplyToMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventReplyTo(
    messageId: Long,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({ if (it.event.message.replyToMessage?.messageId == messageId) it else null }, build)

@JvmName("onMessageEventReplyToTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventReplyTo(
    messageId: Long,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onMessageEventReplyTo(messageId, build) }

// --- whenMessageEventReplyTo (terminal) ---

@JvmName("whenMessageEventReplyToMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventReplyTo(
    messageId: Long,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({ if (it.event.message.replyToMessage?.messageId == messageId) it else null }) { handle(handler) }

@JvmName("whenMessageEventReplyToTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventReplyTo(
    messageId: Long,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenMessageEventReplyTo(messageId, handler) }

// ==================== User/Chat Matchers ====================

// --- onMessageEventFromUser (stackable) ---

@JvmName("onMessageEventFromUserMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventFromUser(
    userId: Long,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({ if (it.event.message.from?.id == userId) it else null }, build)

@JvmName("onMessageEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventFromUser(
    userId: Long,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onMessageEventFromUser(userId, build) }

// --- whenMessageEventFromUser (terminal) ---

@JvmName("whenMessageEventFromUserMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({ if (it.event.message.from?.id == userId) it else null }) { handle(handler) }

@JvmName("whenMessageEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenMessageEventFromUser(userId, handler) }

// --- onMessageEventFromUsers (stackable) ---

@JvmName("onMessageEventFromUsersMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventFromUsers(
    userIds: Set<Long>,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({ if (it.event.message.from?.id in userIds) it else null }, build)

@JvmName("onMessageEventFromUsersTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventFromUsers(
    userIds: Set<Long>,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onMessageEventFromUsers(userIds, build) }

// --- whenMessageEventFromUsers (terminal) ---

@JvmName("whenMessageEventFromUsersMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventFromUsers(
    userIds: Set<Long>,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({ if (it.event.message.from?.id in userIds) it else null }) { handle(handler) }

@JvmName("whenMessageEventFromUsersTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventFromUsers(
    userIds: Set<Long>,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenMessageEventFromUsers(userIds, handler) }

// --- onMessageEventInChat (stackable) ---

@JvmName("onMessageEventInChatMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventInChat(
    chatId: Long,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({ if (it.event.message.chat.id == chatId) it else null }, build)

@JvmName("onMessageEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventInChat(
    chatId: Long,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onMessageEventInChat(chatId, build) }

// --- whenMessageEventInChat (terminal) ---

@JvmName("whenMessageEventInChatMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({ if (it.event.message.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenMessageEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenMessageEventInChat(chatId, handler) }

// --- onMessageEventInChats (stackable) ---

@JvmName("onMessageEventInChatsMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventInChats(
    chatIds: Set<Long>,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({ if (it.event.message.chat.id in chatIds) it else null }, build)

@JvmName("onMessageEventInChatsTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventInChats(
    chatIds: Set<Long>,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onMessageEventInChats(chatIds, build) }

// --- whenMessageEventInChats (terminal) ---

@JvmName("whenMessageEventInChatsMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventInChats(
    chatIds: Set<Long>,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({ if (it.event.message.chat.id in chatIds) it else null }) { handle(handler) }

@JvmName("whenMessageEventInChatsTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventInChats(
    chatIds: Set<Long>,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenMessageEventInChats(chatIds, handler) }

// ==================== Forwarded Matchers ====================

// --- onMessageEventForwarded (stackable) ---

@JvmName("onMessageEventForwardedMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventForwarded(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forwardOrigin != null) it else null }, build)

@JvmName("onMessageEventForwardedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventForwarded(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventForwarded(build) }

// --- whenMessageEventForwarded (terminal) ---

@JvmName("whenMessageEventForwardedMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventForwarded(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forwardOrigin != null) it else null }) { handle(handler) }

@JvmName("whenMessageEventForwardedTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventForwarded(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventForwarded(handler) }

// --- onMessageEventForwardedFromUser (stackable) ---

@JvmName("onMessageEventForwardedFromUserMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventForwardedFromUser(
    userId: Long,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = select({
    val origin = it.event.message.forwardOrigin
    if (origin is MessageOriginUser && origin.senderUser.id == userId) it else null
}, build)

@JvmName("onMessageEventForwardedFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventForwardedFromUser(
    userId: Long,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onMessageEventForwardedFromUser(userId, build) }

// --- whenMessageEventForwardedFromUser (terminal) ---

@JvmName("whenMessageEventForwardedFromUserMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventForwardedFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = select({
    val origin = it.event.message.forwardOrigin
    if (origin is MessageOriginUser && origin.senderUser.id == userId) it else null
}) { handle(handler) }

@JvmName("whenMessageEventForwardedFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventForwardedFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenMessageEventForwardedFromUser(userId, handler) }

// --- onMessageEventForwardedFromChat (stackable) ---

@JvmName("onMessageEventForwardedFromChatMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventForwardedFromChat(
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

@JvmName("onMessageEventForwardedFromChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventForwardedFromChat(
    chatId: Long,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = onMessageEvent { onMessageEventForwardedFromChat(chatId, build) }

// --- whenMessageEventForwardedFromChat (terminal) ---

@JvmName("whenMessageEventForwardedFromChatMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventForwardedFromChat(
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

@JvmName("whenMessageEventForwardedFromChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventForwardedFromChat(
    chatId: Long,
    handler: suspend HandlerBotCall<MessageEvent>.() -> Unit
) = onMessageEvent { whenMessageEventForwardedFromChat(chatId, handler) }

// --- onMessageEventForwardedHiddenUser (stackable) ---

@JvmName("onMessageEventForwardedHiddenUserMessageEvent")
fun HandlerRoute<MessageEvent>.onMessageEventForwardedHiddenUser(build: HandlerRoute<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forwardOrigin is MessageOriginHiddenUser) it else null }, build)

@JvmName("onMessageEventForwardedHiddenUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageEventForwardedHiddenUser(build: HandlerRoute<MessageEvent>.() -> Unit) =
    on<MessageEvent> { onMessageEventForwardedHiddenUser(build) }

// --- whenMessageEventForwardedHiddenUser (terminal) ---

@JvmName("whenMessageEventForwardedHiddenUserMessageEvent")
fun HandlerRoute<MessageEvent>.whenMessageEventForwardedHiddenUser(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    select({ if (it.event.message.forwardOrigin is MessageOriginHiddenUser) it else null }) { handle(handler) }

@JvmName("whenMessageEventForwardedHiddenUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageEventForwardedHiddenUser(handler: suspend HandlerBotCall<MessageEvent>.() -> Unit) =
    on<MessageEvent> { whenMessageEventForwardedHiddenUser(handler) }
