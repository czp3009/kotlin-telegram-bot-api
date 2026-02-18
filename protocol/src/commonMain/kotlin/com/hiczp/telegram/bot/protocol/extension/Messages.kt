package com.hiczp.telegram.bot.protocol.extension

import com.hiczp.telegram.bot.protocol.TelegramBotApi
import com.hiczp.telegram.bot.protocol.model.DeleteMessageRequest
import com.hiczp.telegram.bot.protocol.model.DeleteMessagesRequest
import com.hiczp.telegram.bot.protocol.model.Message
import com.hiczp.telegram.bot.protocol.type.TelegramResponse

val Message.isInaccessible get() = date == 0L

suspend fun TelegramBotApi.deleteMessage(message: Message) =
    deleteMessage(
        DeleteMessageRequest(
            chatId = message.chat.id.toString(),
            messageId = message.messageId,
        )
    )

suspend fun TelegramBotApi.deleteMessages(messages: Collection<Message>): TelegramResponse<Boolean> {
    require(messages.isNotEmpty()) { "Messages must not be empty" }
    require(messages.map { it.chat.id }.distinct().size == 1) { "All messages must be in the same chat" }
    return deleteMessages(
        DeleteMessagesRequest(
            chatId = messages.first().chat.id.toString(),
            messageIds = messages.map { it.messageId },
        )
    )
}

suspend fun TelegramBotApi.deleteMessages(vararg messages: Message) = deleteMessages(messages.toList())
