package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.EventRoute
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import com.hiczp.telegram.bot.protocol.model.MessageOriginChannel
import com.hiczp.telegram.bot.protocol.model.MessageOriginChat
import com.hiczp.telegram.bot.protocol.model.MessageOriginHiddenUser
import com.hiczp.telegram.bot.protocol.model.MessageOriginUser
import kotlinx.coroutines.CoroutineScope
import kotlin.jvm.JvmName

// ==================== Text Matchers ====================

@JvmName("textMessageEvent")
fun EventRoute<MessageEvent>.text(
    exact: String,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.text == exact) it else null }, build)

@JvmName("textTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.text(
    exact: String,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { text(exact, build) }

@JvmName("whenTextMessageEvent")
fun EventRoute<MessageEvent>.whenText(
    exact: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = text(exact) { handle(handler) }

@JvmName("whenTextTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenText(
    exact: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenText(exact, handler) }

@JvmName("textRegexMessageEvent")
fun EventRoute<MessageEvent>.textRegex(
    pattern: Regex,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.text?.matches(pattern) == true) it else null }, build)

@JvmName("textRegexTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.textRegex(
    pattern: Regex,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { textRegex(pattern, build) }

@JvmName("whenTextRegexMessageEvent")
fun EventRoute<MessageEvent>.whenTextRegex(
    pattern: Regex,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = textRegex(pattern) { handle(handler) }

@JvmName("whenTextRegexTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenTextRegex(
    pattern: Regex,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenTextRegex(pattern, handler) }

@JvmName("textContainsMessageEvent")
fun EventRoute<MessageEvent>.textContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> =
    select({ if (it.event.message.text?.contains(substring, ignoreCase) == true) it else null }, build)

@JvmName("textContainsTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.textContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { textContains(substring, ignoreCase, build) }

@JvmName("whenTextContainsMessageEvent")
fun EventRoute<MessageEvent>.whenTextContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = textContains(substring, ignoreCase) { handle(handler) }

@JvmName("whenTextContainsTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenTextContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenTextContains(substring, ignoreCase, handler) }

@JvmName("textStartsWithMessageEvent")
fun EventRoute<MessageEvent>.textStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> =
    select({ if (it.event.message.text?.startsWith(prefix, ignoreCase) == true) it else null }, build)

@JvmName("textStartsWithTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.textStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { textStartsWith(prefix, ignoreCase, build) }

@JvmName("whenTextStartsWithMessageEvent")
fun EventRoute<MessageEvent>.whenTextStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = textStartsWith(prefix, ignoreCase) { handle(handler) }

@JvmName("whenTextStartsWithTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenTextStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenTextStartsWith(prefix, ignoreCase, handler) }

@JvmName("textEndsWithMessageEvent")
fun EventRoute<MessageEvent>.textEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> =
    select({ if (it.event.message.text?.endsWith(suffix, ignoreCase) == true) it else null }, build)

@JvmName("textEndsWithTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.textEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { textEndsWith(suffix, ignoreCase, build) }

@JvmName("whenTextEndsWithMessageEvent")
fun EventRoute<MessageEvent>.whenTextEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = textEndsWith(suffix, ignoreCase) { handle(handler) }

@JvmName("whenTextEndsWithTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenTextEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenTextEndsWith(suffix, ignoreCase, handler) }

// ==================== Media Matchers ====================

@JvmName("photoMessageEvent")
fun EventRoute<MessageEvent>.photo(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.photo != null) it else null }, build)

@JvmName("photoTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.photo(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { photo(build) }

@JvmName("whenPhotoMessageEvent")
fun EventRoute<MessageEvent>.whenPhoto(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = photo { handle(handler) }

@JvmName("whenPhotoTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenPhoto(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenPhoto(handler) }

@JvmName("videoMessageEvent")
fun EventRoute<MessageEvent>.video(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.video != null) it else null }, build)

@JvmName("videoTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.video(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { video(build) }

@JvmName("whenVideoMessageEvent")
fun EventRoute<MessageEvent>.whenVideo(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = video { handle(handler) }

@JvmName("whenVideoTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenVideo(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenVideo(handler) }

@JvmName("audioMessageEvent")
fun EventRoute<MessageEvent>.audio(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.audio != null) it else null }, build)

@JvmName("audioTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.audio(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { audio(build) }

@JvmName("whenAudioMessageEvent")
fun EventRoute<MessageEvent>.whenAudio(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = audio { handle(handler) }

@JvmName("whenAudioTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenAudio(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenAudio(handler) }

@JvmName("documentMessageEvent")
fun EventRoute<MessageEvent>.document(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.document != null) it else null }, build)

@JvmName("documentTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.document(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { document(build) }

@JvmName("whenDocumentMessageEvent")
fun EventRoute<MessageEvent>.whenDocument(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = document { handle(handler) }

@JvmName("whenDocumentTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenDocument(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenDocument(handler) }

@JvmName("stickerMessageEvent")
fun EventRoute<MessageEvent>.sticker(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.sticker != null) it else null }, build)

@JvmName("stickerTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.sticker(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { sticker(build) }

@JvmName("whenStickerMessageEvent")
fun EventRoute<MessageEvent>.whenSticker(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = sticker { handle(handler) }

@JvmName("whenStickerTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenSticker(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenSticker(handler) }

@JvmName("voiceMessageEvent")
fun EventRoute<MessageEvent>.voice(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.voice != null) it else null }, build)

@JvmName("voiceTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.voice(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { voice(build) }

@JvmName("whenVoiceMessageEvent")
fun EventRoute<MessageEvent>.whenVoice(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = voice { handle(handler) }

@JvmName("whenVoiceTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenVoice(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenVoice(handler) }

@JvmName("videoNoteMessageEvent")
fun EventRoute<MessageEvent>.videoNote(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.videoNote != null) it else null }, build)

@JvmName("videoNoteTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.videoNote(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { videoNote(build) }

@JvmName("whenVideoNoteMessageEvent")
fun EventRoute<MessageEvent>.whenVideoNote(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = videoNote { handle(handler) }

@JvmName("whenVideoNoteTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenVideoNote(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenVideoNote(handler) }

@JvmName("animationMessageEvent")
fun EventRoute<MessageEvent>.animation(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.animation != null) it else null }, build)

@JvmName("animationTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.animation(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { animation(build) }

@JvmName("whenAnimationMessageEvent")
fun EventRoute<MessageEvent>.whenAnimation(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = animation { handle(handler) }

@JvmName("whenAnimationTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenAnimation(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenAnimation(handler) }

@JvmName("contactMessageEvent")
fun EventRoute<MessageEvent>.contact(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.contact != null) it else null }, build)

@JvmName("contactTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.contact(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { contact(build) }

@JvmName("whenContactMessageEvent")
fun EventRoute<MessageEvent>.whenContact(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = contact { handle(handler) }

@JvmName("whenContactTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenContact(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenContact(handler) }

@JvmName("locationMessageEvent")
fun EventRoute<MessageEvent>.location(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.location != null) it else null }, build)

@JvmName("locationTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.location(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { location(build) }

@JvmName("whenLocationMessageEvent")
fun EventRoute<MessageEvent>.whenLocation(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = location { handle(handler) }

@JvmName("whenLocationTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenLocation(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenLocation(handler) }

@JvmName("venueMessageEvent")
fun EventRoute<MessageEvent>.venue(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.venue != null) it else null }, build)

@JvmName("venueTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.venue(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { venue(build) }

@JvmName("whenVenueMessageEvent")
fun EventRoute<MessageEvent>.whenVenue(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = venue { handle(handler) }

@JvmName("whenVenueTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenVenue(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenVenue(handler) }

@JvmName("pollMessageEvent")
fun EventRoute<MessageEvent>.poll(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.poll != null) it else null }, build)

@JvmName("pollTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.poll(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { poll(build) }

@JvmName("whenPollMessageEvent")
fun EventRoute<MessageEvent>.whenPoll(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = poll { handle(handler) }

@JvmName("whenPollTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenPoll(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenPoll(handler) }

@JvmName("diceMessageEvent")
fun EventRoute<MessageEvent>.dice(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.dice != null) it else null }, build)

@JvmName("diceTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.dice(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { dice(build) }

@JvmName("whenDiceMessageEvent")
fun EventRoute<MessageEvent>.whenDice(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = dice { handle(handler) }

@JvmName("whenDiceTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenDice(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenDice(handler) }

@JvmName("diceWithEmojiMessageEvent")
fun EventRoute<MessageEvent>.dice(
    emoji: String,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.dice?.emoji == emoji) it else null }, build)

@JvmName("diceWithEmojiTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.dice(
    emoji: String,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { dice(emoji, build) }

@JvmName("whenDiceWithEmojiMessageEvent")
fun EventRoute<MessageEvent>.whenDice(
    emoji: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = dice(emoji) { handle(handler) }

@JvmName("whenDiceWithEmojiTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenDice(
    emoji: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenDice(emoji, handler) }

// ==================== Reply Matchers ====================

@JvmName("replyMessageEvent")
fun EventRoute<MessageEvent>.reply(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.replyToMessage != null) it else null }, build)

@JvmName("replyTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.reply(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { reply(build) }

@JvmName("whenReplyMessageEvent")
fun EventRoute<MessageEvent>.whenReply(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = reply { handle(handler) }

@JvmName("whenReplyTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenReply(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenReply(handler) }

@JvmName("replyToMessageEvent")
fun EventRoute<MessageEvent>.replyTo(
    messageId: Long,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> =
    select({ if (it.event.message.replyToMessage?.messageId == messageId) it else null }, build)

@JvmName("replyToTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.replyTo(
    messageId: Long,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { replyTo(messageId, build) }

@JvmName("whenReplyToMessageEvent")
fun EventRoute<MessageEvent>.whenReplyTo(
    messageId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = replyTo(messageId) { handle(handler) }

@JvmName("whenReplyToTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenReplyTo(
    messageId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenReplyTo(messageId, handler) }

// ==================== User/Chat Matchers ====================

@JvmName("fromUserMessageEvent")
fun EventRoute<MessageEvent>.fromUser(
    userId: Long,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.from?.id == userId) it else null }, build)

@JvmName("fromUserTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.fromUser(
    userId: Long,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { fromUser(userId, build) }

@JvmName("whenFromUserMessageEvent")
fun EventRoute<MessageEvent>.whenFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = fromUser(userId) { handle(handler) }

@JvmName("whenFromUserTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenFromUser(userId, handler) }

@JvmName("fromUsersMessageEvent")
fun EventRoute<MessageEvent>.fromUsers(
    userIds: Set<Long>,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.from?.id in userIds) it else null }, build)

@JvmName("fromUsersTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.fromUsers(
    userIds: Set<Long>,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { fromUsers(userIds, build) }

@JvmName("whenFromUsersMessageEvent")
fun EventRoute<MessageEvent>.whenFromUsers(
    userIds: Set<Long>,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = fromUsers(userIds) { handle(handler) }

@JvmName("whenFromUsersTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenFromUsers(
    userIds: Set<Long>,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenFromUsers(userIds, handler) }

@JvmName("inChatMessageEvent")
fun EventRoute<MessageEvent>.inChat(
    chatId: Long,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.chat.id == chatId) it else null }, build)

@JvmName("inChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.inChat(
    chatId: Long,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { inChat(chatId, build) }

@JvmName("whenInChatMessageEvent")
fun EventRoute<MessageEvent>.whenInChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = inChat(chatId) { handle(handler) }

@JvmName("whenInChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenInChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenInChat(chatId, handler) }

@JvmName("inChatsMessageEvent")
fun EventRoute<MessageEvent>.inChats(
    chatIds: Set<Long>,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.chat.id in chatIds) it else null }, build)

@JvmName("inChatsTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.inChats(
    chatIds: Set<Long>,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { inChats(chatIds, build) }

@JvmName("whenInChatsMessageEvent")
fun EventRoute<MessageEvent>.whenInChats(
    chatIds: Set<Long>,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = inChats(chatIds) { handle(handler) }

@JvmName("whenInChatsTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenInChats(
    chatIds: Set<Long>,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenInChats(chatIds, handler) }

// ==================== Forwarded Matchers ====================

@JvmName("forwardedMessageEvent")
fun EventRoute<MessageEvent>.forwarded(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ if (it.event.message.forwardOrigin != null) it else null }, build)

@JvmName("forwardedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.forwarded(
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { forwarded(build) }

@JvmName("whenForwardedMessageEvent")
fun EventRoute<MessageEvent>.whenForwarded(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = forwarded { handle(handler) }

@JvmName("whenForwardedTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenForwarded(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenForwarded(handler) }

@JvmName("forwardedFromChatMessageEvent")
fun EventRoute<MessageEvent>.forwardedFromChat(
    chatId: Long,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ ctx ->
    when (val origin = ctx.event.message.forwardOrigin) {
        is MessageOriginChannel -> origin.chat.id
        is MessageOriginChat -> origin.senderChat.id
        is MessageOriginHiddenUser -> null
        is MessageOriginUser -> null
        null -> null
    }?.let { if (it == chatId) ctx else null }
}, build)

@JvmName("forwardedFromChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.forwardedFromChat(
    chatId: Long,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { forwardedFromChat(chatId, build) }

@JvmName("whenForwardedFromChatMessageEvent")
fun EventRoute<MessageEvent>.whenForwardedFromChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = forwardedFromChat(chatId) { handle(handler) }

@JvmName("whenForwardedFromChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenForwardedFromChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenForwardedFromChat(chatId, handler) }

@JvmName("forwardedFromUserMessageEvent")
fun EventRoute<MessageEvent>.forwardedFromUser(
    userId: Long,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = select({ ctx ->
    val origin = ctx.event.message.forwardOrigin
    if (origin is MessageOriginUser && origin.senderUser.id == userId) {
        ctx
    } else {
        null
    }
}, build)

@JvmName("forwardedFromUserTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.forwardedFromUser(
    userId: Long,
    build: EventRoute<MessageEvent>.() -> Unit
): EventRoute<MessageEvent> = on<MessageEvent> { forwardedFromUser(userId, build) }

@JvmName("whenForwardedFromUserMessageEvent")
fun EventRoute<MessageEvent>.whenForwardedFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = forwardedFromUser(userId) { handle(handler) }

@JvmName("whenForwardedFromUserTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenForwardedFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { whenForwardedFromUser(userId, handler) }
