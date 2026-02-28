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

/**
 * Registers a handler for messages with the exact text match.
 *
 * @param exact The exact text string to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching message.
 */
fun EventRoute<MessageEvent>.text(
    exact: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.text == exact) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a text handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.text(
    exact: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { text(exact, handler) }

/**
 * Registers a handler for messages matching a regular expression.
 *
 * @param pattern The regular expression pattern to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching message.
 */
fun EventRoute<MessageEvent>.textRegex(
    pattern: Regex,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.text?.matches(pattern) == true) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a text regex handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.textRegex(
    pattern: Regex,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { textRegex(pattern, handler) }

/**
 * Registers a handler for messages that contain a specific substring.
 *
 * @param substring The substring to search for in the message text.
 * @param ignoreCase Whether the search should be case-insensitive. Default is false.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching message.
 */
fun EventRoute<MessageEvent>.textContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.text?.contains(substring, ignoreCase) == true) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a text contains handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.textContains(
    substring: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { textContains(substring, ignoreCase, handler) }

/**
 * Registers a handler for messages whose text starts with a specific prefix.
 *
 * @param prefix The prefix to match at the start of the message text.
 * @param ignoreCase Whether the comparison should be case-insensitive. Default is false.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching message.
 */
fun EventRoute<MessageEvent>.textStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.text?.startsWith(prefix, ignoreCase) == true) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a text starts with handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.textStartsWith(
    prefix: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { textStartsWith(prefix, ignoreCase, handler) }

/**
 * Registers a handler for messages whose text ends with a specific suffix.
 *
 * @param suffix The suffix to match at the end of the message text.
 * @param ignoreCase Whether the comparison should be case-insensitive. Default is false.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching message.
 */
fun EventRoute<MessageEvent>.textEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.text?.endsWith(suffix, ignoreCase) == true) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a text ends with handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.textEndsWith(
    suffix: String,
    ignoreCase: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { textEndsWith(suffix, ignoreCase, handler) }

/**
 * Registers a handler for messages that contain a photo.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the photo message.
 */
fun EventRoute<MessageEvent>.photo(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.photo != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a photo handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.photo(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { photo(handler) }

/**
 * Registers a handler for messages that contain a video.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the video message.
 */
fun EventRoute<MessageEvent>.video(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.video != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a video handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.video(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { video(handler) }

/**
 * Registers a handler for messages that contain an audio file.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the audio message.
 */
fun EventRoute<MessageEvent>.audio(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.audio != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers an audio handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.audio(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { audio(handler) }

/**
 * Registers a handler for messages that contain a document.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the document message.
 */
fun EventRoute<MessageEvent>.document(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.document != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a document handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.document(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { document(handler) }

/**
 * Registers a handler for messages that contain a sticker.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the sticker message.
 */
fun EventRoute<MessageEvent>.sticker(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.sticker != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a sticker handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.sticker(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { sticker(handler) }

/**
 * Registers a handler for messages that contain a voice note.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the voice message.
 */
fun EventRoute<MessageEvent>.voice(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.voice != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a voice handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.voice(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { voice(handler) }

/**
 * Registers a handler for messages that contain a video note (rounded video).
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the video note message.
 */
fun EventRoute<MessageEvent>.videoNote(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.videoNote != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a video note handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.videoNote(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { videoNote(handler) }

/**
 * Registers a handler for messages that contain an animation (GIF).
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the animation message.
 */
fun EventRoute<MessageEvent>.animation(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.animation != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers an animation handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.animation(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { animation(handler) }

/**
 * Registers a handler for messages that contain a contact.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the contact message.
 */
fun EventRoute<MessageEvent>.contact(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.contact != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a contact handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.contact(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { contact(handler) }

/**
 * Registers a handler for messages that contain a location.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the location message.
 */
fun EventRoute<MessageEvent>.location(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.location != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a location handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.location(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { location(handler) }

/**
 * Registers a handler for messages that contain a venue.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the venue message.
 */
fun EventRoute<MessageEvent>.venue(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.venue != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a venue handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.venue(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { venue(handler) }

/**
 * Registers a handler for messages that contain a poll.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the poll message.
 */
fun EventRoute<MessageEvent>.poll(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.poll != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a poll handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.poll(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { poll(handler) }

/**
 * Registers a handler for messages that contain a dice.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the dice message.
 */
fun EventRoute<MessageEvent>.dice(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.dice != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a dice handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.dice(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { dice(handler) }

/**
 * Registers a handler for messages that are replies to another message.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the reply message.
 */
fun EventRoute<MessageEvent>.reply(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.replyToMessage != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a reply handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.reply(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { reply(handler) }

/**
 * Registers a handler for messages that are replies to a specific message.
 *
 * @param messageId The ID of the message that this message is replying to.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the reply message.
 */
fun EventRoute<MessageEvent>.replyTo(
    messageId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.replyToMessage?.messageId == messageId) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a replyTo handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.replyTo(
    messageId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { replyTo(messageId, handler) }

/**
 * Registers a handler for messages from a specific user.
 *
 * @param userId The Telegram user ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the message.
 */
fun EventRoute<MessageEvent>.fromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.from?.id == userId) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a from user handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.fromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { fromUser(userId, handler) }

/**
 * Registers a handler for messages from any of the specified users.
 *
 * @param userIds The set of Telegram user IDs to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the message.
 */
fun EventRoute<MessageEvent>.fromUsers(
    userIds: Set<Long>,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.from?.id in userIds) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a from users handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.fromUsers(
    userIds: Set<Long>,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { fromUsers(userIds, handler) }

/**
 * Registers a handler for messages in a specific chat.
 *
 * @param chatId The Telegram chat ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the message.
 */
fun EventRoute<MessageEvent>.inChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.chat.id == chatId) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers an in chat handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.inChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { inChat(chatId, handler) }

/**
 * Registers a handler for messages in any of the specified chats.
 *
 * @param chatIds The set of Telegram chat IDs to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the message.
 */
fun EventRoute<MessageEvent>.inChats(
    chatIds: Set<Long>,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.chat.id in chatIds) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers an in chats handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.inChats(
    chatIds: Set<Long>,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { inChats(chatIds, handler) }

/**
 * Registers a handler for messages that are forwarded.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the forwarded message.
 */
fun EventRoute<MessageEvent>.forwarded(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.forwardOrigin != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a forwarded handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.forwarded(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { forwarded(handler) }

/**
 * Registers a handler for messages forwarded from a specific chat.
 *
 * @param chatId The Telegram chat ID from which the message was forwarded.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the forwarded message.
 */
fun EventRoute<MessageEvent>.forwardedFromChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ ctx ->
        when (val origin = ctx.event.message.forwardOrigin) {
            is MessageOriginChannel -> origin.chat.id
            is MessageOriginChat -> origin.senderChat.id
            is MessageOriginHiddenUser -> null
            is MessageOriginUser -> null
            null -> null
        }?.let { if (it == chatId) ctx else null }
    }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a forwarded from chat handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.forwardedFromChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { forwardedFromChat(chatId, handler) }

/**
 * Registers a handler for messages forwarded from a specific user.
 *
 * @param userId The Telegram user ID from which the message was forwarded.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the forwarded message.
 */
fun EventRoute<MessageEvent>.forwardedFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ ctx ->
        val origin = ctx.event.message.forwardOrigin
        if (origin is MessageOriginUser && origin.senderUser.id == userId) {
            ctx
        } else {
            null
        }
    }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a forwarded from user handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.forwardedFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { forwardedFromUser(userId, handler) }

/**
 * Registers a handler for messages that contain a dice with a specific emoji.
 *
 * Dice emojis include: "🎲", "🎯", "🏀", "⚽", "🎳", "🎰"
 *
 * @param emoji The dice emoji to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the dice message.
 */
fun EventRoute<MessageEvent>.dice(
    emoji: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.dice?.emoji == emoji) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a dice with emoji handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.dice(
    emoji: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { dice(emoji, handler) }
