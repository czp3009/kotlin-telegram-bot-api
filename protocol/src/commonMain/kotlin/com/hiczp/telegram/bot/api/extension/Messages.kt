package com.hiczp.telegram.bot.api.extension

import com.hiczp.telegram.bot.api.TelegramBotApi
import com.hiczp.telegram.bot.api.model.DeleteMessageRequest
import com.hiczp.telegram.bot.api.model.Message

val Message.isInaccessible get() = date == 0L

suspend fun TelegramBotApi.deleteMessage(message: Message) =
    deleteMessage(
        DeleteMessageRequest(
            chatId = message.chat.id.toString(),
            messageId = message.messageId,
        )
    )
