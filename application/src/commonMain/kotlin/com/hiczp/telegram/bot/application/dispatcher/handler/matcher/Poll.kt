@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.*
import kotlin.jvm.JvmName

// --- onPollEventPollId (stackable) ---

@JvmName("onPollEventPollIdPollEvent")
fun HandlerRoute<PollEvent>.onPollEventPollId(
    pollId: String,
    build: HandlerRoute<PollEvent>.() -> Unit
) = select({ if (it.event.poll.id == pollId) it else null }, build)

@JvmName("onPollEventPollIdTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onPollEventPollId(
    pollId: String,
    build: HandlerRoute<PollEvent>.() -> Unit
) = onPollEvent { onPollEventPollId(pollId, build) }

// --- whenPollEventPollId (terminal) ---

@JvmName("whenPollEventPollIdPollEvent")
fun HandlerRoute<PollEvent>.whenPollEventPollId(
    pollId: String,
    handler: suspend HandlerBotCall<PollEvent>.() -> Unit
) = select({ if (it.event.poll.id == pollId) it else null }) { handle(handler) }

@JvmName("whenPollEventPollIdTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenPollEventPollId(
    pollId: String,
    handler: suspend HandlerBotCall<PollEvent>.() -> Unit
) = onPollEvent { whenPollEventPollId(pollId, handler) }

// --- onPollAnswerEventPollId (stackable) ---

@JvmName("onPollAnswerEventPollIdPollAnswerEvent")
fun HandlerRoute<PollAnswerEvent>.onPollAnswerEventPollId(
    pollId: String,
    build: HandlerRoute<PollAnswerEvent>.() -> Unit
) = select({ if (it.event.pollAnswer.pollId == pollId) it else null }, build)

@JvmName("onPollAnswerEventPollIdTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onPollAnswerEventPollId(
    pollId: String,
    build: HandlerRoute<PollAnswerEvent>.() -> Unit
) = onPollAnswerEvent { onPollAnswerEventPollId(pollId, build) }

// --- whenPollAnswerEventPollId (terminal) ---

@JvmName("whenPollAnswerEventPollIdPollAnswerEvent")
fun HandlerRoute<PollAnswerEvent>.whenPollAnswerEventPollId(
    pollId: String,
    handler: suspend HandlerBotCall<PollAnswerEvent>.() -> Unit
) = select({ if (it.event.pollAnswer.pollId == pollId) it else null }) { handle(handler) }

@JvmName("whenPollAnswerEventPollIdTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenPollAnswerEventPollId(
    pollId: String,
    handler: suspend HandlerBotCall<PollAnswerEvent>.() -> Unit
) = onPollAnswerEvent { whenPollAnswerEventPollId(pollId, handler) }

// --- onPollAnswerEventFromUser (stackable) ---

@JvmName("onPollAnswerEventFromUserPollAnswerEvent")
fun HandlerRoute<PollAnswerEvent>.onPollAnswerEventFromUser(
    userId: Long,
    build: HandlerRoute<PollAnswerEvent>.() -> Unit
) = select({ if (it.event.pollAnswer.user?.id == userId) it else null }, build)

@JvmName("onPollAnswerEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onPollAnswerEventFromUser(
    userId: Long,
    build: HandlerRoute<PollAnswerEvent>.() -> Unit
) = onPollAnswerEvent { onPollAnswerEventFromUser(userId, build) }

// --- whenPollAnswerEventFromUser (terminal) ---

@JvmName("whenPollAnswerEventFromUserPollAnswerEvent")
fun HandlerRoute<PollAnswerEvent>.whenPollAnswerEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<PollAnswerEvent>.() -> Unit
) = select({ if (it.event.pollAnswer.user?.id == userId) it else null }) { handle(handler) }

@JvmName("whenPollAnswerEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenPollAnswerEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<PollAnswerEvent>.() -> Unit
) = onPollAnswerEvent { whenPollAnswerEventFromUser(userId, handler) }

// --- onMessageReactionEventInChat (stackable) ---

@JvmName("onMessageReactionEventInChatMessageReactionEvent")
fun HandlerRoute<MessageReactionEvent>.onMessageReactionEventInChat(
    chatId: Long,
    build: HandlerRoute<MessageReactionEvent>.() -> Unit
) = select({ if (it.event.messageReaction.chat.id == chatId) it else null }, build)

@JvmName("onMessageReactionEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageReactionEventInChat(
    chatId: Long,
    build: HandlerRoute<MessageReactionEvent>.() -> Unit
) = onMessageReactionEvent { onMessageReactionEventInChat(chatId, build) }

// --- whenMessageReactionEventInChat (terminal) ---

@JvmName("whenMessageReactionEventInChatMessageReactionEvent")
fun HandlerRoute<MessageReactionEvent>.whenMessageReactionEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<MessageReactionEvent>.() -> Unit
) = select({ if (it.event.messageReaction.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenMessageReactionEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageReactionEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<MessageReactionEvent>.() -> Unit
) = onMessageReactionEvent { whenMessageReactionEventInChat(chatId, handler) }

// --- onMessageReactionEventToMessage (stackable) ---

@JvmName("onMessageReactionEventToMessageMessageReactionEvent")
fun HandlerRoute<MessageReactionEvent>.onMessageReactionEventToMessage(
    messageId: Long,
    build: HandlerRoute<MessageReactionEvent>.() -> Unit
) = select({ if (it.event.messageReaction.messageId == messageId) it else null }, build)

@JvmName("onMessageReactionEventToMessageTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageReactionEventToMessage(
    messageId: Long,
    build: HandlerRoute<MessageReactionEvent>.() -> Unit
) = onMessageReactionEvent { onMessageReactionEventToMessage(messageId, build) }

// --- whenMessageReactionEventToMessage (terminal) ---

@JvmName("whenMessageReactionEventToMessageMessageReactionEvent")
fun HandlerRoute<MessageReactionEvent>.whenMessageReactionEventToMessage(
    messageId: Long,
    handler: suspend HandlerBotCall<MessageReactionEvent>.() -> Unit
) = select({ if (it.event.messageReaction.messageId == messageId) it else null }) { handle(handler) }

@JvmName("whenMessageReactionEventToMessageTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageReactionEventToMessage(
    messageId: Long,
    handler: suspend HandlerBotCall<MessageReactionEvent>.() -> Unit
) = onMessageReactionEvent { whenMessageReactionEventToMessage(messageId, handler) }

// --- onMessageReactionCountEventInChat (stackable) ---

@JvmName("onMessageReactionCountEventInChatMessageReactionCountEvent")
fun HandlerRoute<MessageReactionCountEvent>.onMessageReactionCountEventInChat(
    chatId: Long,
    build: HandlerRoute<MessageReactionCountEvent>.() -> Unit
) = select({ if (it.event.messageReactionCount.chat.id == chatId) it else null }, build)

@JvmName("onMessageReactionCountEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageReactionCountEventInChat(
    chatId: Long,
    build: HandlerRoute<MessageReactionCountEvent>.() -> Unit
) = onMessageReactionCountEvent { onMessageReactionCountEventInChat(chatId, build) }

// --- whenMessageReactionCountEventInChat (terminal) ---

@JvmName("whenMessageReactionCountEventInChatMessageReactionCountEvent")
fun HandlerRoute<MessageReactionCountEvent>.whenMessageReactionCountEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<MessageReactionCountEvent>.() -> Unit
) = select({ if (it.event.messageReactionCount.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenMessageReactionCountEventInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageReactionCountEventInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<MessageReactionCountEvent>.() -> Unit
) = onMessageReactionCountEvent { whenMessageReactionCountEventInChat(chatId, handler) }
