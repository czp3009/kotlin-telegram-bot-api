package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.EventRoute
import com.hiczp.telegram.bot.protocol.event.*
import kotlinx.coroutines.CoroutineScope

/**
 * Registers a handler for poll state updates.
 *
 * Bots receive only updates about manually stopped polls and polls
 * which were sent by the bot.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenPoll(
    handler: suspend CoroutineScope.(TelegramBotEventContext<PollEvent>) -> Unit
) = on<PollEvent> { handle(handler) }

/**
 * Registers a handler for poll state updates with a specific poll ID.
 *
 * @param pollId The poll ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenPoll(
    pollId: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<PollEvent>) -> Unit
) = on<PollEvent> {
    select({ if (it.event.poll.id == pollId) it else null }) {
        handle(handler)
    }
}


/**
 * Registers a handler for poll answer events.
 *
 * Triggered when a user changes their answer in a non-anonymous poll.
 * Bots receive new votes only in polls that were sent by the bot itself.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenPollAnswer(
    handler: suspend CoroutineScope.(TelegramBotEventContext<PollAnswerEvent>) -> Unit
) = on<PollAnswerEvent> { handle(handler) }

/**
 * Registers a handler for poll answer events with a specific poll ID.
 *
 * @param pollId The poll ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenPollAnswer(
    pollId: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<PollAnswerEvent>) -> Unit
) = on<PollAnswerEvent> {
    select({ if (it.event.pollAnswer.pollId == pollId) it else null }) {
        handle(handler)
    }
}

/**
 * Registers a handler for poll answer events from a specific user.
 *
 * @param userId The Telegram user ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenPollAnswerFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<PollAnswerEvent>) -> Unit
) = on<PollAnswerEvent> {
    select({ if (it.event.pollAnswer.user?.id == userId) it else null }) {
        handle(handler)
    }
}


/**
 * Registers a handler for message reaction events.
 *
 * Triggered when a reaction to a message was changed by a user.
 * The bot must be an administrator in the chat and must explicitly specify
 * "message_reaction" in the list of allowed_updates to receive these updates.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenMessageReaction(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageReactionEvent>) -> Unit
) = on<MessageReactionEvent> { handle(handler) }

/**
 * Registers a handler for message reaction events in a specific chat.
 *
 * @param chatId The Telegram chat ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenMessageReactionInChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageReactionEvent>) -> Unit
) = on<MessageReactionEvent> {
    select({ if (it.event.messageReaction.chat.id == chatId) it else null }) {
        handle(handler)
    }
}

/**
 * Registers a handler for message reaction events on a specific message.
 *
 * @param messageId The message ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenMessageReactionToMessage(
    messageId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageReactionEvent>) -> Unit
) = on<MessageReactionEvent> {
    select({ if (it.event.messageReaction.messageId == messageId) it else null }) {
        handle(handler)
    }
}


/**
 * Registers a handler for message reaction count events.
 *
 * Triggered when reactions to a message with anonymous reactions were changed.
 * The bot must be an administrator in the chat and must explicitly specify
 * "message_reaction_count" in the list of allowed_updates to receive these updates.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenMessageReactionCount(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageReactionCountEvent>) -> Unit
) = on<MessageReactionCountEvent> { handle(handler) }

/**
 * Registers a handler for message reaction count events in a specific chat.
 *
 * @param chatId The Telegram chat ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.whenMessageReactionCountInChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageReactionCountEvent>) -> Unit
) = on<MessageReactionCountEvent> {
    select({ if (it.event.messageReactionCount.chat.id == chatId) it else null }) {
        handle(handler)
    }
}
