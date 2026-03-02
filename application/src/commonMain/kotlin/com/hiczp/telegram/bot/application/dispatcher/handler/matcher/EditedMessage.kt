@file:Suppress("unused", "DuplicatedCode")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.EditedChannelPostEvent
import com.hiczp.telegram.bot.protocol.event.EditedMessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlin.jvm.JvmName

// ==================== EditedMessageEvent Inner Scope Text Matchers ====================

// --- onEditedMessageEventText (stackable) ---

@JvmName("onEditedMessageEventTextEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.onEditedMessageEventText(
    exact: String,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.text == exact) it else null }, build)

@JvmName("onEditedMessageEventTextTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedMessageEventText(
    exact: String,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { onEditedMessageEventText(exact, build) }

// --- whenEditedMessageEventText (terminal) ---

@JvmName("whenEditedMessageEventTextEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.whenEditedMessageEventText(
    exact: String,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.text == exact) it else null }) { handle(handler) }

@JvmName("whenEditedMessageEventTextTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedMessageEventText(
    exact: String,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { whenEditedMessageEventText(exact, handler) }

// --- onEditedMessageEventTextRegex (stackable) ---

@JvmName("onEditedMessageEventTextRegexEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.onEditedMessageEventTextRegex(
    pattern: Regex,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.text?.matches(pattern) == true) it else null }, build)

@JvmName("onEditedMessageEventTextRegexTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedMessageEventTextRegex(
    pattern: Regex,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { onEditedMessageEventTextRegex(pattern, build) }

// --- whenEditedMessageEventTextRegex (terminal) ---

@JvmName("whenEditedMessageEventTextRegexEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.whenEditedMessageEventTextRegex(
    pattern: Regex,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.text?.matches(pattern) == true) it else null }) { handle(handler) }

@JvmName("whenEditedMessageEventTextRegexTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedMessageEventTextRegex(
    pattern: Regex,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { whenEditedMessageEventTextRegex(pattern, handler) }

// --- onEditedMessageEventTextContains (stackable) ---

@JvmName("onEditedMessageEventTextContainsEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.onEditedMessageEventTextContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.text?.contains(substring, ignoreCase) == true) it else null }, build)

@JvmName("onEditedMessageEventTextContainsTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedMessageEventTextContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { onEditedMessageEventTextContains(substring, ignoreCase, build) }

// --- whenEditedMessageEventTextContains (terminal) ---

@JvmName("whenEditedMessageEventTextContainsEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.whenEditedMessageEventTextContains(
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

@JvmName("whenEditedMessageEventTextContainsTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedMessageEventTextContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { whenEditedMessageEventTextContains(substring, ignoreCase, handler) }

// --- onEditedMessageEventTextStartsWith (stackable) ---

@JvmName("onEditedMessageEventTextStartsWithEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.onEditedMessageEventTextStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.text?.startsWith(prefix, ignoreCase) == true) it else null }, build)

@JvmName("onEditedMessageEventTextStartsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedMessageEventTextStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { onEditedMessageEventTextStartsWith(prefix, ignoreCase, build) }

// --- whenEditedMessageEventTextStartsWith (terminal) ---

@JvmName("whenEditedMessageEventTextStartsWithEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.whenEditedMessageEventTextStartsWith(
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

@JvmName("whenEditedMessageEventTextStartsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedMessageEventTextStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { whenEditedMessageEventTextStartsWith(prefix, ignoreCase, handler) }

// --- onEditedMessageEventTextEndsWith (stackable) ---

@JvmName("onEditedMessageEventTextEndsWithEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.onEditedMessageEventTextEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.text?.endsWith(suffix, ignoreCase) == true) it else null }, build)

@JvmName("onEditedMessageEventTextEndsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedMessageEventTextEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { onEditedMessageEventTextEndsWith(suffix, ignoreCase, build) }

// --- whenEditedMessageEventTextEndsWith (terminal) ---

@JvmName("whenEditedMessageEventTextEndsWithEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.whenEditedMessageEventTextEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.text?.endsWith(suffix, ignoreCase) == true) it else null }) { handle(handler) }

@JvmName("whenEditedMessageEventTextEndsWithTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedMessageEventTextEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { whenEditedMessageEventTextEndsWith(suffix, ignoreCase, handler) }

// ==================== EditedMessageEvent Inner Scope Media Matchers ====================

// --- onEditedMessageEventPhoto (stackable) ---

@JvmName("onEditedMessageEventPhotoEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.onEditedMessageEventPhoto(build: HandlerRoute<EditedMessageEvent>.() -> Unit) =
    select({ if (it.event.editedMessage.photo != null) it else null }, build)

@JvmName("onEditedMessageEventPhotoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedMessageEventPhoto(build: HandlerRoute<EditedMessageEvent>.() -> Unit) =
    on<EditedMessageEvent> { onEditedMessageEventPhoto(build) }

// --- whenEditedMessageEventPhoto (terminal) ---

@JvmName("whenEditedMessageEventPhotoEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.whenEditedMessageEventPhoto(handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit) =
    select({ if (it.event.editedMessage.photo != null) it else null }) { handle(handler) }

@JvmName("whenEditedMessageEventPhotoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedMessageEventPhoto(handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit) =
    on<EditedMessageEvent> { whenEditedMessageEventPhoto(handler) }

// --- onEditedMessageEventVideo (stackable) ---

@JvmName("onEditedMessageEventVideoEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.onEditedMessageEventVideo(build: HandlerRoute<EditedMessageEvent>.() -> Unit) =
    select({ if (it.event.editedMessage.video != null) it else null }, build)

@JvmName("onEditedMessageEventVideoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedMessageEventVideo(build: HandlerRoute<EditedMessageEvent>.() -> Unit) =
    on<EditedMessageEvent> { onEditedMessageEventVideo(build) }

// --- whenEditedMessageEventVideo (terminal) ---

@JvmName("whenEditedMessageEventVideoEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.whenEditedMessageEventVideo(handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit) =
    select({ if (it.event.editedMessage.video != null) it else null }) { handle(handler) }

@JvmName("whenEditedMessageEventVideoTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedMessageEventVideo(handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit) =
    on<EditedMessageEvent> { whenEditedMessageEventVideo(handler) }

// --- onEditedMessageEventDocument (stackable) ---

@JvmName("onEditedMessageEventDocumentEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.onEditedMessageEventDocument(build: HandlerRoute<EditedMessageEvent>.() -> Unit) =
    select({ if (it.event.editedMessage.document != null) it else null }, build)

@JvmName("onEditedMessageEventDocumentTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedMessageEventDocument(build: HandlerRoute<EditedMessageEvent>.() -> Unit) =
    on<EditedMessageEvent> { onEditedMessageEventDocument(build) }

// --- whenEditedMessageEventDocument (terminal) ---

@JvmName("whenEditedMessageEventDocumentEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.whenEditedMessageEventDocument(handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit) =
    select({ if (it.event.editedMessage.document != null) it else null }) { handle(handler) }

@JvmName("whenEditedMessageEventDocumentTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedMessageEventDocument(handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit) =
    on<EditedMessageEvent> { whenEditedMessageEventDocument(handler) }

// --- onEditedMessageEventAudio (stackable) ---

@JvmName("onEditedMessageEventAudioEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.onEditedMessageEventAudio(build: HandlerRoute<EditedMessageEvent>.() -> Unit) =
    select({ if (it.event.editedMessage.audio != null) it else null }, build)

@JvmName("onEditedMessageEventAudioTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedMessageEventAudio(build: HandlerRoute<EditedMessageEvent>.() -> Unit) =
    on<EditedMessageEvent> { onEditedMessageEventAudio(build) }

// --- whenEditedMessageEventAudio (terminal) ---

@JvmName("whenEditedMessageEventAudioEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.whenEditedMessageEventAudio(handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit) =
    select({ if (it.event.editedMessage.audio != null) it else null }) { handle(handler) }

@JvmName("whenEditedMessageEventAudioTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedMessageEventAudio(handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit) =
    on<EditedMessageEvent> { whenEditedMessageEventAudio(handler) }

// ==================== EditedMessageEvent Inner Scope User/Chat Matchers ====================

// --- onEditedMessageEventFromUser (stackable) ---

@JvmName("onEditedMessageEventFromUserEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.onEditedMessageEventFromUser(
    userId: Long,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.from?.id == userId) it else null }, build)

@JvmName("onEditedMessageEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedMessageEventFromUser(
    userId: Long,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { onEditedMessageEventFromUser(userId, build) }

// --- whenEditedMessageEventFromUser (terminal) ---

@JvmName("whenEditedMessageEventFromUserEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.whenEditedMessageEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.from?.id == userId) it else null }) { handle(handler) }

@JvmName("whenEditedMessageEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedMessageEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { whenEditedMessageEventFromUser(userId, handler) }

// --- onEditedMessageEventInChat (stackable) ---

@JvmName("onEditedMessageEventInChatEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.onEditedMessageEventInChat(
    chatId: Long,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.chat.id == chatId) it else null }, build)

@JvmName("onEditedMessageEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedMessageEventInChat(
    chatId: Long,
    build: HandlerRoute<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { onEditedMessageEventInChat(chatId, build) }

// --- whenEditedMessageEventInChat (terminal) ---

@JvmName("whenEditedMessageEventInChatEditedMessageEvent")
fun HandlerRoute<EditedMessageEvent>.whenEditedMessageEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = select({ if (it.event.editedMessage.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenEditedMessageEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedMessageEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<EditedMessageEvent>.() -> Unit
) = on<EditedMessageEvent> { whenEditedMessageEventInChat(chatId, handler) }

// ==================== EditedChannelPostEvent Inner Scope Matchers ====================

// --- onEditedChannelPostEventText (stackable) ---

@JvmName("onEditedChannelPostEventTextEditedChannelPostEvent")
fun HandlerRoute<EditedChannelPostEvent>.onEditedChannelPostEventText(
    exact: String,
    build: HandlerRoute<EditedChannelPostEvent>.() -> Unit
) = select({ if (it.event.editedChannelPost.text == exact) it else null }, build)

@JvmName("onEditedChannelPostEventTextTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedChannelPostEventText(
    exact: String,
    build: HandlerRoute<EditedChannelPostEvent>.() -> Unit
) = on<EditedChannelPostEvent> { onEditedChannelPostEventText(exact, build) }

// --- whenEditedChannelPostEventText (terminal) ---

@JvmName("whenEditedChannelPostEventTextEditedChannelPostEvent")
fun HandlerRoute<EditedChannelPostEvent>.whenEditedChannelPostEventText(
    exact: String,
    handler: suspend HandlerBotCall<EditedChannelPostEvent>.() -> Unit
) = select({ if (it.event.editedChannelPost.text == exact) it else null }) { handle(handler) }

@JvmName("whenEditedChannelPostEventTextTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedChannelPostEventText(
    exact: String,
    handler: suspend HandlerBotCall<EditedChannelPostEvent>.() -> Unit
) = on<EditedChannelPostEvent> { whenEditedChannelPostEventText(exact, handler) }

// --- onEditedChannelPostEventTextRegex (stackable) ---

@JvmName("onEditedChannelPostEventTextRegexEditedChannelPostEvent")
fun HandlerRoute<EditedChannelPostEvent>.onEditedChannelPostEventTextRegex(
    pattern: Regex,
    build: HandlerRoute<EditedChannelPostEvent>.() -> Unit
) = select({ if (it.event.editedChannelPost.text?.matches(pattern) == true) it else null }, build)

@JvmName("onEditedChannelPostEventTextRegexTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedChannelPostEventTextRegex(
    pattern: Regex,
    build: HandlerRoute<EditedChannelPostEvent>.() -> Unit
) = on<EditedChannelPostEvent> { onEditedChannelPostEventTextRegex(pattern, build) }

// --- whenEditedChannelPostEventTextRegex (terminal) ---

@JvmName("whenEditedChannelPostEventTextRegexEditedChannelPostEvent")
fun HandlerRoute<EditedChannelPostEvent>.whenEditedChannelPostEventTextRegex(
    pattern: Regex,
    handler: suspend HandlerBotCall<EditedChannelPostEvent>.() -> Unit
) = select({ if (it.event.editedChannelPost.text?.matches(pattern) == true) it else null }) { handle(handler) }

@JvmName("whenEditedChannelPostEventTextRegexTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedChannelPostEventTextRegex(
    pattern: Regex,
    handler: suspend HandlerBotCall<EditedChannelPostEvent>.() -> Unit
) = on<EditedChannelPostEvent> { whenEditedChannelPostEventTextRegex(pattern, handler) }

// --- onEditedChannelPostEventFromChat (stackable) ---

@JvmName("onEditedChannelPostEventFromChatEditedChannelPostEvent")
fun HandlerRoute<EditedChannelPostEvent>.onEditedChannelPostEventFromChat(
    chatId: Long,
    build: HandlerRoute<EditedChannelPostEvent>.() -> Unit
) = select({ if (it.event.editedChannelPost.chat.id == chatId) it else null }, build)

@JvmName("onEditedChannelPostEventFromChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onEditedChannelPostEventFromChat(
    chatId: Long,
    build: HandlerRoute<EditedChannelPostEvent>.() -> Unit
) = on<EditedChannelPostEvent> { onEditedChannelPostEventFromChat(chatId, build) }

// --- whenEditedChannelPostEventFromChat (terminal) ---

@JvmName("whenEditedChannelPostEventFromChatEditedChannelPostEvent")
fun HandlerRoute<EditedChannelPostEvent>.whenEditedChannelPostEventFromChat(
    chatId: Long,
    handler: suspend HandlerBotCall<EditedChannelPostEvent>.() -> Unit
) = select({ if (it.event.editedChannelPost.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenEditedChannelPostEventFromChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenEditedChannelPostEventFromChat(
    chatId: Long,
    handler: suspend HandlerBotCall<EditedChannelPostEvent>.() -> Unit
) = on<EditedChannelPostEvent> { whenEditedChannelPostEventFromChat(chatId, handler) }
