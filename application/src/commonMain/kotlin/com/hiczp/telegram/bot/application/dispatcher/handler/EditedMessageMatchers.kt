package com.hiczp.telegram.bot.application.dispatcher.handler

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.protocol.event.EditedChannelPostEvent
import com.hiczp.telegram.bot.protocol.event.EditedMessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlinx.coroutines.CoroutineScope

/**
 * Registers a handler for edited messages.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the edited message.
 */
fun EventRoute<TelegramBotEvent>.editedMessage(
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = on<EditedMessageEvent> { handle(handler) }

/**
 * Registers a handler for edited messages with exact text match.
 *
 * @param exact The exact text string to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching edited message.
 */
fun EventRoute<TelegramBotEvent>.editedText(
    exact: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = on<EditedMessageEvent> {
    select({ if (it.event.editedMessage.text == exact) it else null }) {
        handle(handler)
    }
}

/**
 * Registers a handler for edited messages matching a regular expression.
 *
 * @param pattern The regular expression pattern to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching edited message.
 */
fun EventRoute<TelegramBotEvent>.editedTextRegex(
    pattern: Regex,
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = on<EditedMessageEvent> {
    select({ if (it.event.editedMessage.text?.matches(pattern) == true) it else null }) {
        handle(handler)
    }
}

/**
 * Registers a handler for edited messages that contain a specific substring.
 *
 * @param substring The substring to search for in the message text.
 * @param ignoreCase Whether the search should be case-insensitive. Default is false.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching edited message.
 */
fun EventRoute<TelegramBotEvent>.editedTextContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = on<EditedMessageEvent> {
    select({ if (it.event.editedMessage.text?.contains(substring, ignoreCase) == true) it else null }) {
        handle(handler)
    }
}

/**
 * Registers a handler for edited messages whose text starts with a specific prefix.
 *
 * @param prefix The prefix to match at the start of the message text.
 * @param ignoreCase Whether the comparison should be case-insensitive. Default is false.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching edited message.
 */
fun EventRoute<TelegramBotEvent>.editedTextStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = on<EditedMessageEvent> {
    select({ if (it.event.editedMessage.text?.startsWith(prefix, ignoreCase) == true) it else null }) {
        handle(handler)
    }
}

/**
 * Registers a handler for edited messages whose text ends with a specific suffix.
 *
 * @param suffix The suffix to match at the end of the message text.
 * @param ignoreCase Whether the comparison should be case-insensitive. Default is false.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching edited message.
 */
fun EventRoute<TelegramBotEvent>.editedTextEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = on<EditedMessageEvent> {
    select({ if (it.event.editedMessage.text?.endsWith(suffix, ignoreCase) == true) it else null }) {
        handle(handler)
    }
}

/**
 * Registers a handler for edited messages that contain a photo.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the edited photo message.
 */
fun EventRoute<TelegramBotEvent>.editedPhoto(
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = on<EditedMessageEvent> {
    select({ if (it.event.editedMessage.photo != null) it else null }) {
        handle(handler)
    }
}

/**
 * Registers a handler for edited messages that contain a video.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the edited video message.
 */
fun EventRoute<TelegramBotEvent>.editedVideo(
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = on<EditedMessageEvent> {
    select({ if (it.event.editedMessage.video != null) it else null }) {
        handle(handler)
    }
}

/**
 * Registers a handler for edited messages that contain a document.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the edited document message.
 */
fun EventRoute<TelegramBotEvent>.editedDocument(
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = on<EditedMessageEvent> {
    select({ if (it.event.editedMessage.document != null) it else null }) {
        handle(handler)
    }
}

/**
 * Registers a handler for edited messages that contain an audio file.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the edited audio message.
 */
fun EventRoute<TelegramBotEvent>.editedAudio(
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = on<EditedMessageEvent> {
    select({ if (it.event.editedMessage.audio != null) it else null }) {
        handle(handler)
    }
}

/**
 * Registers a handler for edited messages from a specific user.
 *
 * @param userId The Telegram user ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the edited message.
 */
fun EventRoute<TelegramBotEvent>.editedFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = on<EditedMessageEvent> {
    select({ if (it.event.editedMessage.from?.id == userId) it else null }) {
        handle(handler)
    }
}

/**
 * Registers a handler for edited messages in a specific chat.
 *
 * @param chatId The Telegram chat ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the edited message.
 */
fun EventRoute<TelegramBotEvent>.editedInChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedMessageEvent>) -> Unit
) = on<EditedMessageEvent> {
    select({ if (it.event.editedMessage.chat.id == chatId) it else null }) {
        handle(handler)
    }
}

/**
 * Registers a handler for edited channel posts.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the edited channel post.
 */
fun EventRoute<TelegramBotEvent>.editedChannelPost(
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedChannelPostEvent>) -> Unit
) = on<EditedChannelPostEvent> { handle(handler) }

/**
 * Registers a handler for edited channel posts with exact text match.
 *
 * @param exact The exact text string to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching edited channel post.
 */
fun EventRoute<TelegramBotEvent>.editedChannelPostText(
    exact: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedChannelPostEvent>) -> Unit
) = on<EditedChannelPostEvent> {
    select({ if (it.event.editedChannelPost.text == exact) it else null }) {
        handle(handler)
    }
}

/**
 * Registers a handler for edited channel posts matching a regular expression.
 *
 * @param pattern The regular expression pattern to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching edited channel post.
 */
fun EventRoute<TelegramBotEvent>.editedChannelPostTextRegex(
    pattern: Regex,
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedChannelPostEvent>) -> Unit
) = on<EditedChannelPostEvent> {
    select({ if (it.event.editedChannelPost.text?.matches(pattern) == true) it else null }) {
        handle(handler)
    }
}

/**
 * Registers a handler for edited channel posts from a specific channel.
 *
 * @param chatId The Telegram channel ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the edited channel post.
 */
fun EventRoute<TelegramBotEvent>.editedChannelPostFromChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<EditedChannelPostEvent>) -> Unit
) = on<EditedChannelPostEvent> {
    select({ if (it.event.editedChannelPost.chat.id == chatId) it else null }) {
        handle(handler)
    }
}
