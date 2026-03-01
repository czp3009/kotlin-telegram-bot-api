package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.EventRoute
import com.hiczp.telegram.bot.protocol.event.*
import kotlinx.coroutines.CoroutineScope
import kotlin.jvm.JvmName

@JvmName("pollUpdateTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.pollUpdate(
    build: EventRoute<PollEvent>.() -> Unit
): EventRoute<PollEvent> = on<PollEvent> { build() }

@JvmName("whenPollUpdateTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenPollUpdate(
    handler: suspend CoroutineScope.(TelegramBotEventContext<PollEvent>) -> Unit
) = pollUpdate { handle(handler) }

@JvmName("pollUpdateWithPollIdTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.pollUpdate(
    pollId: String,
    build: EventRoute<PollEvent>.() -> Unit
): EventRoute<PollEvent> = on<PollEvent> {
    select({ if (it.event.poll.id == pollId) it else null }, build)
}

@JvmName("whenPollUpdateWithPollIdTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenPollUpdate(
    pollId: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<PollEvent>) -> Unit
) = pollUpdate(pollId) { handle(handler) }

@JvmName("pollAnswerTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.pollAnswer(
    build: EventRoute<PollAnswerEvent>.() -> Unit
): EventRoute<PollAnswerEvent> = on<PollAnswerEvent> { build() }

@JvmName("whenPollAnswerTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenPollAnswer(
    handler: suspend CoroutineScope.(TelegramBotEventContext<PollAnswerEvent>) -> Unit
) = pollAnswer { handle(handler) }

@JvmName("pollAnswerWithPollIdTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.pollAnswer(
    pollId: String,
    build: EventRoute<PollAnswerEvent>.() -> Unit
): EventRoute<PollAnswerEvent> = on<PollAnswerEvent> {
    select({ if (it.event.pollAnswer.pollId == pollId) it else null }, build)
}

@JvmName("whenPollAnswerWithPollIdTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenPollAnswer(
    pollId: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<PollAnswerEvent>) -> Unit
) = pollAnswer(pollId) { handle(handler) }

@JvmName("pollAnswerFromUserTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.pollAnswerFromUser(
    userId: Long,
    build: EventRoute<PollAnswerEvent>.() -> Unit
): EventRoute<PollAnswerEvent> = on<PollAnswerEvent> {
    select({ if (it.event.pollAnswer.user?.id == userId) it else null }, build)
}

@JvmName("whenPollAnswerFromUserTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenPollAnswerFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<PollAnswerEvent>) -> Unit
) = pollAnswerFromUser(userId) { handle(handler) }

@JvmName("messageReactionTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.messageReaction(
    build: EventRoute<MessageReactionEvent>.() -> Unit
): EventRoute<MessageReactionEvent> = on<MessageReactionEvent> { build() }

@JvmName("whenMessageReactionTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenMessageReaction(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageReactionEvent>) -> Unit
) = messageReaction { handle(handler) }

@JvmName("messageReactionInChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.messageReactionInChat(
    chatId: Long,
    build: EventRoute<MessageReactionEvent>.() -> Unit
): EventRoute<MessageReactionEvent> = on<MessageReactionEvent> {
    select({ if (it.event.messageReaction.chat.id == chatId) it else null }, build)
}

@JvmName("whenMessageReactionInChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenMessageReactionInChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageReactionEvent>) -> Unit
) = messageReactionInChat(chatId) { handle(handler) }

@JvmName("messageReactionToMessageTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.messageReactionToMessage(
    messageId: Long,
    build: EventRoute<MessageReactionEvent>.() -> Unit
): EventRoute<MessageReactionEvent> = on<MessageReactionEvent> {
    select({ if (it.event.messageReaction.messageId == messageId) it else null }, build)
}

@JvmName("whenMessageReactionToMessageTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenMessageReactionToMessage(
    messageId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageReactionEvent>) -> Unit
) = messageReactionToMessage(messageId) { handle(handler) }

@JvmName("messageReactionCountTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.messageReactionCount(
    build: EventRoute<MessageReactionCountEvent>.() -> Unit
): EventRoute<MessageReactionCountEvent> = on<MessageReactionCountEvent> { build() }

@JvmName("whenMessageReactionCountTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenMessageReactionCount(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageReactionCountEvent>) -> Unit
) = messageReactionCount { handle(handler) }

@JvmName("messageReactionCountInChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.messageReactionCountInChat(
    chatId: Long,
    build: EventRoute<MessageReactionCountEvent>.() -> Unit
): EventRoute<MessageReactionCountEvent> = on<MessageReactionCountEvent> {
    select({ if (it.event.messageReactionCount.chat.id == chatId) it else null }, build)
}

@JvmName("whenMessageReactionCountInChatTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenMessageReactionCountInChat(
    chatId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageReactionCountEvent>) -> Unit
) = messageReactionCountInChat(chatId) { handle(handler) }
