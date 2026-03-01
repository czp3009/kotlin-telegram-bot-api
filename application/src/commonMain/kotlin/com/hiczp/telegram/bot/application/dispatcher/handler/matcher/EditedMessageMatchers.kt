package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.EventRoute
import com.hiczp.telegram.bot.protocol.event.EditedChannelPostEvent
import com.hiczp.telegram.bot.protocol.event.EditedMessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlinx.coroutines.CoroutineScope
import kotlin.jvm.JvmName

@JvmName("editedMessageTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.editedMessage(
    build: EventRoute<EditedMessageEvent>.() -> Unit
): EventRoute<EditedMessageEvent> = on<EditedMessageEvent> { build() }

@JvmName("whenEditedMessageTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenEditedMessage(
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = editedMessage { handle(handler) }

@JvmName("editedTextTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.editedText(
    exact: String,
    build: EventRoute<EditedMessageEvent>.() -> Unit
): EventRoute<EditedMessageEvent> = on<EditedMessageEvent> {
    select({ if (it.event.editedMessage.text == exact) it else null }, build)
}

@JvmName("whenEditedTextTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenEditedText(
    exact: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = editedText(exact) { handle(handler) }

@JvmName("editedTextRegexTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.editedTextRegex(
    pattern: Regex,
    build: EventRoute<EditedMessageEvent>.() -> Unit
): EventRoute<EditedMessageEvent> = on<EditedMessageEvent> {
    select({ if (it.event.editedMessage.text?.matches(pattern) == true) it else null }, build)
}

@JvmName("whenEditedTextRegexTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenEditedTextRegex(
    pattern: Regex,
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = editedTextRegex(pattern) { handle(handler) }

@JvmName("editedTextContainsTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.editedTextContains(
    substring: String,
    ignoreCase: Boolean = false,
    build: EventRoute<EditedMessageEvent>.() -> Unit
): EventRoute<EditedMessageEvent> = on<EditedMessageEvent> {
    select({ if (it.event.editedMessage.text?.contains(substring, ignoreCase) == true) it else null }, build)
}

@JvmName("whenEditedTextContainsTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenEditedTextContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = editedTextContains(substring, ignoreCase) { handle(handler) }

@JvmName("editedTextStartsWithTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.editedTextStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    build: EventRoute<EditedMessageEvent>.() -> Unit
): EventRoute<EditedMessageEvent> = on<EditedMessageEvent> {
    select({ if (it.event.editedMessage.text?.startsWith(prefix, ignoreCase) == true) it else null }, build)
}

@JvmName("whenEditedTextStartsWithTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenEditedTextStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = editedTextStartsWith(prefix, ignoreCase) { handle(handler) }

@JvmName("editedTextEndsWithTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.editedTextEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    build: EventRoute<EditedMessageEvent>.() -> Unit
): EventRoute<EditedMessageEvent> = on<EditedMessageEvent> {
    select({ if (it.event.editedMessage.text?.endsWith(suffix, ignoreCase) == true) it else null }, build)
}

@JvmName("whenEditedTextEndsWithTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenEditedTextEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = editedTextEndsWith(suffix, ignoreCase) { handle(handler) }

@JvmName("editedPhotoTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.editedPhoto(
    build: EventRoute<EditedMessageEvent>.() -> Unit
): EventRoute<EditedMessageEvent> = on<EditedMessageEvent> {
    select({ if (it.event.editedMessage.photo != null) it else null }, build)
}

@JvmName("whenEditedPhotoTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenEditedPhoto(
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = editedPhoto { handle(handler) }

@JvmName("editedVideoTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.editedVideo(
    build: EventRoute<EditedMessageEvent>.() -> Unit
): EventRoute<EditedMessageEvent> = on<EditedMessageEvent> {
    select({ if (it.event.editedMessage.video != null) it else null }, build)
}

@JvmName("whenEditedVideoTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenEditedVideo(
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = editedVideo { handle(handler) }

@JvmName("editedDocumentTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.editedDocument(
    build: EventRoute<EditedMessageEvent>.() -> Unit
): EventRoute<EditedMessageEvent> = on<EditedMessageEvent> {
    select({ if (it.event.editedMessage.document != null) it else null }, build)
}

@JvmName("whenEditedDocumentTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenEditedDocument(
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = editedDocument { handle(handler) }

@JvmName("editedAudioTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.editedAudio(
    build: EventRoute<EditedMessageEvent>.() -> Unit
): EventRoute<EditedMessageEvent> = on<EditedMessageEvent> {
    select({ if (it.event.editedMessage.audio != null) it else null }, build)
}

@JvmName("whenEditedAudioTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenEditedAudio(
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = editedAudio { handle(handler) }

@JvmName("editedFromUserTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.editedFromUser(
    userId: Long,
    build: EventRoute<EditedMessageEvent>.() -> Unit
): EventRoute<EditedMessageEvent> = on<EditedMessageEvent> {
    select({ if (it.event.editedMessage.from?.id == userId) it else null }, build)
}

@JvmName("whenEditedFromUserTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenEditedFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = editedFromUser(userId) { handle(handler) }

@JvmName("editedInChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.editedInChat(
    chatId: Long,
    build: EventRoute<EditedMessageEvent>.() -> Unit
): EventRoute<EditedMessageEvent> = on<EditedMessageEvent> {
    select({ if (it.event.editedMessage.chat.id == chatId) it else null }, build)
}

@JvmName("whenEditedInChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenEditedInChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = editedInChat(chatId) { handle(handler) }

@JvmName("editedChannelPostTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.editedChannelPost(
    build: EventRoute<EditedChannelPostEvent>.() -> Unit
): EventRoute<EditedChannelPostEvent> = on<EditedChannelPostEvent> { build() }

@JvmName("whenEditedChannelPostTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenEditedChannelPost(
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedChannelPostEvent>) -> Unit
) = editedChannelPost { handle(handler) }

@JvmName("editedChannelPostTextTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.editedChannelPostText(
    exact: String,
    build: EventRoute<EditedChannelPostEvent>.() -> Unit
): EventRoute<EditedChannelPostEvent> = on<EditedChannelPostEvent> {
    select({ if (it.event.editedChannelPost.text == exact) it else null }, build)
}

@JvmName("whenEditedChannelPostTextTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenEditedChannelPostText(
    exact: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedChannelPostEvent>) -> Unit
) = editedChannelPostText(exact) { handle(handler) }

@JvmName("editedChannelPostTextRegexTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.editedChannelPostTextRegex(
    pattern: Regex,
    build: EventRoute<EditedChannelPostEvent>.() -> Unit
): EventRoute<EditedChannelPostEvent> = on<EditedChannelPostEvent> {
    select({ if (it.event.editedChannelPost.text?.matches(pattern) == true) it else null }, build)
}

@JvmName("whenEditedChannelPostTextRegexTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenEditedChannelPostTextRegex(
    pattern: Regex,
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedChannelPostEvent>) -> Unit
) = editedChannelPostTextRegex(pattern) { handle(handler) }

@JvmName("editedChannelPostFromChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.editedChannelPostFromChat(
    chatId: Long,
    build: EventRoute<EditedChannelPostEvent>.() -> Unit
): EventRoute<EditedChannelPostEvent> = on<EditedChannelPostEvent> {
    select({ if (it.event.editedChannelPost.chat.id == chatId) it else null }, build)
}

@JvmName("whenEditedChannelPostFromChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenEditedChannelPostFromChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedChannelPostEvent>) -> Unit
) = editedChannelPostFromChat(chatId) { handle(handler) }
