@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.*
import kotlin.jvm.JvmName

// --- onPollUpdate with pollId (stackable) ---

@JvmName("onPollUpdateWithPollIdPollEvent")
fun HandlerRoute<PollEvent>.onPollUpdate(
    pollId: String,
    build: HandlerRoute<PollEvent>.() -> Unit
) = select({ if (it.event.poll.id == pollId) it else null }, build)

@JvmName("onPollUpdateWithPollIdTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onPollUpdate(
    pollId: String,
    build: HandlerRoute<PollEvent>.() -> Unit
) = onPollEvent { onPollUpdate(pollId, build) }

// --- whenPollUpdate with pollId (terminal) ---

@JvmName("whenPollUpdateWithPollIdPollEvent")
fun HandlerRoute<PollEvent>.whenPollUpdate(
    pollId: String,
    handler: suspend HandlerBotCall<PollEvent>.() -> Unit
) = select({ if (it.event.poll.id == pollId) it else null }) { handle(handler) }

@JvmName("whenPollUpdateWithPollIdTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenPollUpdate(
    pollId: String,
    handler: suspend HandlerBotCall<PollEvent>.() -> Unit
) = onPollEvent { whenPollUpdate(pollId, handler) }

// --- onPollAnswer with pollId (stackable) ---

@JvmName("onPollAnswerWithPollIdPollAnswerEvent")
fun HandlerRoute<PollAnswerEvent>.onPollAnswer(
    pollId: String,
    build: HandlerRoute<PollAnswerEvent>.() -> Unit
) = select({ if (it.event.pollAnswer.pollId == pollId) it else null }, build)

@JvmName("onPollAnswerWithPollIdTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onPollAnswer(
    pollId: String,
    build: HandlerRoute<PollAnswerEvent>.() -> Unit
) = onPollAnswerEvent { onPollAnswer(pollId, build) }

// --- whenPollAnswer with pollId (terminal) ---

@JvmName("whenPollAnswerWithPollIdPollAnswerEvent")
fun HandlerRoute<PollAnswerEvent>.whenPollAnswer(
    pollId: String,
    handler: suspend HandlerBotCall<PollAnswerEvent>.() -> Unit
) = select({ if (it.event.pollAnswer.pollId == pollId) it else null }) { handle(handler) }

@JvmName("whenPollAnswerWithPollIdTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenPollAnswer(
    pollId: String,
    handler: suspend HandlerBotCall<PollAnswerEvent>.() -> Unit
) = onPollAnswerEvent { whenPollAnswer(pollId, handler) }

// --- onPollAnswerFromUser (stackable) ---

@JvmName("onPollAnswerFromUserPollAnswerEvent")
fun HandlerRoute<PollAnswerEvent>.onPollAnswerFromUser(
    userId: Long,
    build: HandlerRoute<PollAnswerEvent>.() -> Unit
) = select({ if (it.event.pollAnswer.user?.id == userId) it else null }, build)

@JvmName("onPollAnswerFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onPollAnswerFromUser(
    userId: Long,
    build: HandlerRoute<PollAnswerEvent>.() -> Unit
) = onPollAnswerEvent { onPollAnswerFromUser(userId, build) }

// --- whenPollAnswerFromUser (terminal) ---

@JvmName("whenPollAnswerFromUserPollAnswerEvent")
fun HandlerRoute<PollAnswerEvent>.whenPollAnswerFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<PollAnswerEvent>.() -> Unit
) = select({ if (it.event.pollAnswer.user?.id == userId) it else null }) { handle(handler) }

@JvmName("whenPollAnswerFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenPollAnswerFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<PollAnswerEvent>.() -> Unit
) = onPollAnswerEvent { whenPollAnswerFromUser(userId, handler) }

// --- onMessageReactionInChat (stackable) ---

@JvmName("onMessageReactionInChatMessageReactionEvent")
fun HandlerRoute<MessageReactionEvent>.onMessageReactionInChat(
    chatId: Long,
    build: HandlerRoute<MessageReactionEvent>.() -> Unit
) = select({ if (it.event.messageReaction.chat.id == chatId) it else null }, build)

@JvmName("onMessageReactionInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageReactionInChat(
    chatId: Long,
    build: HandlerRoute<MessageReactionEvent>.() -> Unit
) = onMessageReactionEvent { onMessageReactionInChat(chatId, build) }

// --- whenMessageReactionInChat (terminal) ---

@JvmName("whenMessageReactionInChatMessageReactionEvent")
fun HandlerRoute<MessageReactionEvent>.whenMessageReactionInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<MessageReactionEvent>.() -> Unit
) = select({ if (it.event.messageReaction.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenMessageReactionInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageReactionInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<MessageReactionEvent>.() -> Unit
) = onMessageReactionEvent { whenMessageReactionInChat(chatId, handler) }

// --- onMessageReactionToMessage (stackable) ---

@JvmName("onMessageReactionToMessageMessageReactionEvent")
fun HandlerRoute<MessageReactionEvent>.onMessageReactionToMessage(
    messageId: Long,
    build: HandlerRoute<MessageReactionEvent>.() -> Unit
) = select({ if (it.event.messageReaction.messageId == messageId) it else null }, build)

@JvmName("onMessageReactionToMessageTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageReactionToMessage(
    messageId: Long,
    build: HandlerRoute<MessageReactionEvent>.() -> Unit
) = onMessageReactionEvent { onMessageReactionToMessage(messageId, build) }

// --- whenMessageReactionToMessage (terminal) ---

@JvmName("whenMessageReactionToMessageMessageReactionEvent")
fun HandlerRoute<MessageReactionEvent>.whenMessageReactionToMessage(
    messageId: Long,
    handler: suspend HandlerBotCall<MessageReactionEvent>.() -> Unit
) = select({ if (it.event.messageReaction.messageId == messageId) it else null }) { handle(handler) }

@JvmName("whenMessageReactionToMessageTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageReactionToMessage(
    messageId: Long,
    handler: suspend HandlerBotCall<MessageReactionEvent>.() -> Unit
) = onMessageReactionEvent { whenMessageReactionToMessage(messageId, handler) }

// --- onMessageReactionCountInChat (stackable) ---

@JvmName("onMessageReactionCountInChatMessageReactionCountEvent")
fun HandlerRoute<MessageReactionCountEvent>.onMessageReactionCountInChat(
    chatId: Long,
    build: HandlerRoute<MessageReactionCountEvent>.() -> Unit
) = select({ if (it.event.messageReactionCount.chat.id == chatId) it else null }, build)

@JvmName("onMessageReactionCountInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onMessageReactionCountInChat(
    chatId: Long,
    build: HandlerRoute<MessageReactionCountEvent>.() -> Unit
) = onMessageReactionCountEvent { onMessageReactionCountInChat(chatId, build) }

// --- whenMessageReactionCountInChat (terminal) ---

@JvmName("whenMessageReactionCountInChatMessageReactionCountEvent")
fun HandlerRoute<MessageReactionCountEvent>.whenMessageReactionCountInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<MessageReactionCountEvent>.() -> Unit
) = select({ if (it.event.messageReactionCount.chat.id == chatId) it else null }) { handle(handler) }

@JvmName("whenMessageReactionCountInChatTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenMessageReactionCountInChat(
    chatId: Long,
    handler: suspend HandlerBotCall<MessageReactionCountEvent>.() -> Unit
) = onMessageReactionCountEvent { whenMessageReactionCountInChat(chatId, handler) }
