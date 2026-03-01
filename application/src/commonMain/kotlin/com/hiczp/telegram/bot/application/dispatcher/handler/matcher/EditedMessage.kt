@file:Suppress("unused", "DuplicatedCode")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.EditedChannelPostEvent
import com.hiczp.telegram.bot.protocol.event.EditedMessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlin.jvm.JvmName

// ==================== EditedMessageEvent Inner Scope Text Matchers ====================

// --- onEditedText (stackable) ---

@JvmName("onEditedTextEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.onEditedText(
    exact: String,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.text == exact) it else null }, build)

@JvmName("onEditedTextTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedText(
    exact: String,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { onEditedText(exact, build) }

// --- whenEditedText (terminal) ---

@JvmName("whenEditedTextEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.whenEditedText(
    exact: String,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.text == exact) it else null }) { handle(handler) }

@JvmName("whenEditedTextTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedText(
    exact: String,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { whenEditedText(exact, handler) }

// --- onEditedTextRegex (stackable) ---

@JvmName("onEditedTextRegexEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.onEditedTextRegex(
    pattern: Regex,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.text?.matches(pattern) == true) it else null }, build)

@JvmName("onEditedTextRegexTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedTextRegex(
    pattern: Regex,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { onEditedTextRegex(pattern, build) }

// --- whenEditedTextRegex (terminal) ---

@JvmName("whenEditedTextRegexEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.whenEditedTextRegex(
    pattern: Regex,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.text?.matches(pattern) == true) it else null }) { handle(handler) }

@JvmName("whenEditedTextRegexTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedTextRegex(
    pattern: Regex,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { whenEditedTextRegex(pattern, handler) }

// --- onEditedTextContains (stackable) ---

@JvmName("onEditedTextContainsEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.onEditedTextContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.text?.contains(substring, ignoreCase) == true) it else null }, build)

@JvmName("onEditedTextContainsTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedTextContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { onEditedTextContains(substring, ignoreCase, build) }

// --- whenEditedTextContains (terminal) ---

@JvmName("whenEditedTextContainsEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.whenEditedTextContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = select({
    if (it.event.editedMessage.text?.contains(
            substring,
            ignoreCase
        ) == true
    ) it else null
}) { handle(handler) }

@JvmName("whenEditedTextContainsTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedTextContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { whenEditedTextContains(substring, ignoreCase, handler) }

// --- onEditedTextStartsWith (stackable) ---

@JvmName("onEditedTextStartsWithEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.onEditedTextStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.text?.startsWith(prefix, ignoreCase) == true) it else null }, build)

@JvmName("onEditedTextStartsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedTextStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { onEditedTextStartsWith(prefix, ignoreCase, build) }

// --- whenEditedTextStartsWith (terminal) ---

@JvmName("whenEditedTextStartsWithEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.whenEditedTextStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = select({
    if (it.event.editedMessage.text?.startsWith(
            prefix,
            ignoreCase
        ) == true
    ) it else null
}) { handle(handler) }

@JvmName("whenEditedTextStartsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedTextStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { whenEditedTextStartsWith(prefix, ignoreCase, handler) }

// --- onEditedTextEndsWith (stackable) ---

@JvmName("onEditedTextEndsWithEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.onEditedTextEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.text?.endsWith(suffix, ignoreCase) == true) it else null }, build)

@JvmName("onEditedTextEndsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedTextEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { onEditedTextEndsWith(suffix, ignoreCase, build) }

// --- whenEditedTextEndsWith (terminal) ---

@JvmName("whenEditedTextEndsWithEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.whenEditedTextEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.text?.endsWith(suffix, ignoreCase) == true) it else null }) { handle(handler) }

@JvmName("whenEditedTextEndsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedTextEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { whenEditedTextEndsWith(suffix, ignoreCase, handler) }

// ==================== EditedMessageEvent Inner Scope Media Matchers ====================

// --- onEditedPhoto (stackable) ---

@JvmName("onEditedPhotoEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.onEditedPhoto(build: HandlerRoute<EditedMessageEvent>.() -> Unit) =
    select({ if (it.event.editedMessage.photo != null) it else null }, build)

@JvmName("onEditedPhotoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedPhoto(build: HandlerRoute<EditedMessageEvent>.() -> Unit) =
    on<EditedMessageEvent> { onEditedPhoto(build) }

// --- whenEditedPhoto (terminal) ---

@JvmName("whenEditedPhotoEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.whenEditedPhoto(handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit) =
    select({ if (it.event.editedMessage.photo != null) it else null }) { handle(handler) }

@JvmName("whenEditedPhotoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedPhoto(handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit) =
    on<EditedMessageEvent> { whenEditedPhoto(handler) }

// --- onEditedVideo (stackable) ---

@JvmName("onEditedVideoEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.onEditedVideo(build: HandlerRoute<EditedMessageEvent>.() -> Unit) =
    select({ if (it.event.editedMessage.video != null) it else null }, build)

@JvmName("onEditedVideoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedVideo(build: HandlerRoute<EditedMessageEvent>.() -> Unit) =
    on<EditedMessageEvent> { onEditedVideo(build) }

// --- whenEditedVideo (terminal) ---

@JvmName("whenEditedVideoEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.whenEditedVideo(handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit) =
    select({ if (it.event.editedMessage.video != null) it else null }) { handle(handler) }

@JvmName("whenEditedVideoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedVideo(handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit) =
    on<EditedMessageEvent> { whenEditedVideo(handler) }

// --- onEditedDocument (stackable) ---

@JvmName("onEditedDocumentEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.onEditedDocument(build: HandlerRoute<EditedMessageEvent>.() -> Unit) =
    select({ if (it.event.editedMessage.document != null) it else null }, build)

@JvmName("onEditedDocumentTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedDocument(build: HandlerRoute<EditedMessageEvent>.() -> Unit) =
    on<EditedMessageEvent> { onEditedDocument(build) }

// --- whenEditedDocument (terminal) ---

@JvmName("whenEditedDocumentEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.whenEditedDocument(handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit) =
    select({ if (it.event.editedMessage.document != null) it else null }) { handle(handler) }

@JvmName("whenEditedDocumentTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedDocument(handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit) =
    on<EditedMessageEvent> { whenEditedDocument(handler) }

// --- onEditedAudio (stackable) ---

@JvmName("onEditedAudioEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.onEditedAudio(build: HandlerRoute<EditedMessageEvent>.() -> Unit) =
    select({ if (it.event.editedMessage.audio != null) it else null }, build)

@JvmName("onEditedAudioTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedAudio(build: HandlerRoute<EditedMessageEvent>.() -> Unit) =
    on<EditedMessageEvent> { onEditedAudio(build) }

// --- whenEditedAudio (terminal) ---

@JvmName("whenEditedAudioEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.whenEditedAudio(handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit) =
    select({ if (it.event.editedMessage.audio != null) it else null }) { handle(handler) }

@JvmName("whenEditedAudioTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedAudio(handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit) =
    on<EditedMessageEvent> { whenEditedAudio(handler) }

// ==================== EditedMessageEvent Inner Scope User/Chat Matchers ====================

// --- onEditedFromUser (stackable) ---

@JvmName("onEditedFromUserEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.onEditedFromUser(
    userId: Long,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.from?.id == userId) it else null }, build)

@JvmName("onEditedFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedFromUser(
    userId: Long,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { onEditedFromUser(userId, build) }

// --- whenEditedFromUser (terminal) ---

@JvmName("whenEditedFromUserEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.whenEditedFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.from?.id == userId) it else null }) { handle(handler) }

@JvmName("whenEditedFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { whenEditedFromUser(userId, handler) }

// --- onEditedInChat (stackable) ---

@JvmName("onEditedInChatEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.onEditedInChat(
    chatId: Long,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.chat.id == chatId) it else null }, build)

@JvmName("onEditedInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedInChat(
    chatId: Long,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { onEditedInChat(chatId, build) }

// --- whenEditedInChat (terminal) ---

@JvmName("whenEditedInChatEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.whenEditedInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenEditedInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { whenEditedInChat(chatId, handler) }

// ==================== EditedChannelPostEvent Inner Scope Matchers ====================

// --- onEditedChannelPostText (stackable) ---

@JvmName("onEditedChannelPostTextEditedChannelPostEvent")
fun HandlerRoute<EditedChannelPostEvent>.onEditedChannelPostText(
    exact: String,
    build: HandlerRoute<EditedChannelPostEvent>.() -> Unit
) = select({ if (it.event.editedChannelPost.text == exact) it else null }, build)

@JvmName("onEditedChannelPostTextTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedChannelPostText(
    exact: String,
    build: HandlerRoute<EditedChannelPostEvent>.() -> Unit
) = on<EditedChannelPostEvent> { onEditedChannelPostText(exact, build) }

// --- whenEditedChannelPostText (terminal) ---

@JvmName("whenEditedChannelPostTextEditedChannelPostEvent")
fun HandlerRoute<EditedChannelPostEvent>.whenEditedChannelPostText(
    exact: String,
    handler: suspend HandlerBotCall<EditedChannelPostEvent>.() -> Unit
) = select({ if (it.event.editedChannelPost.text == exact) it else null }) { handle(handler) }

@JvmName("whenEditedChannelPostTextTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedChannelPostText(
    exact: String,
    handler: suspend HandlerBotCall<EditedChannelPostEvent>.() -> Unit
) = on<EditedChannelPostEvent> { whenEditedChannelPostText(exact, handler) }

// --- onEditedChannelPostTextRegex (stackable) ---

@JvmName("onEditedChannelPostTextRegexEditedChannelPostEvent")
fun HandlerRoute<EditedChannelPostEvent>.onEditedChannelPostTextRegex(
    pattern: Regex,
    build: HandlerRoute<EditedChannelPostEvent>.() -> Unit
) = select({ if (it.event.editedChannelPost.text?.matches(pattern) == true) it else null }, build)

@JvmName("onEditedChannelPostTextRegexTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedChannelPostTextRegex(
    pattern: Regex,
    build: HandlerRoute<EditedChannelPostEvent>.() -> Unit
) = on<EditedChannelPostEvent> { onEditedChannelPostTextRegex(pattern, build) }

// --- whenEditedChannelPostTextRegex (terminal) ---

@JvmName("whenEditedChannelPostTextRegexEditedChannelPostEvent")
fun HandlerRoute<EditedChannelPostEvent>.whenEditedChannelPostTextRegex(
    pattern: Regex,
    handler: suspend HandlerBotCall<EditedChannelPostEvent>.() -> Unit
) = select({ if (it.event.editedChannelPost.text?.matches(pattern) == true) it else null }) { handle(handler) }

@JvmName("whenEditedChannelPostTextRegexTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedChannelPostTextRegex(
    pattern: Regex,
    handler: suspend HandlerBotCall<EditedChannelPostEvent>.() -> Unit
) = on<EditedChannelPostEvent> { whenEditedChannelPostTextRegex(pattern, handler) }

// --- onEditedChannelPostFromChat (stackable) ---

@JvmName("onEditedChannelPostFromChatEditedChannelPostEvent")
fun HandlerRoute<EditedChannelPostEvent>.onEditedChannelPostFromChat(
    chatId: Long,
    build: HandlerRoute<EditedChannelPostEvent>.() -> Unit
) = select({ if (it.event.editedChannelPost.chat.id == chatId) it else null }, build)

@JvmName("onEditedChannelPostFromChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedChannelPostFromChat(
    chatId: Long,
    build: HandlerRoute<EditedChannelPostEvent>.() -> Unit
) = on<EditedChannelPostEvent> { onEditedChannelPostFromChat(chatId, build) }

// --- whenEditedChannelPostFromChat (terminal) ---

@JvmName("whenEditedChannelPostFromChatEditedChannelPostEvent")
fun HandlerRoute<EditedChannelPostEvent>.whenEditedChannelPostFromChat(
    chatId: Long,
    handler: suspend HandlerBotCall<EditedChannelPostEvent>.() -> Unit
) = select({ if (it.event.editedChannelPost.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenEditedChannelPostFromChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedChannelPostFromChat(
    chatId: Long,
    handler: suspend HandlerBotCall<EditedChannelPostEvent>.() -> Unit
) = on<EditedChannelPostEvent> { whenEditedChannelPostFromChat(chatId, handler) }
