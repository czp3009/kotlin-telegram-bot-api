package com.hiczp.telegram.bot.protocol.extension

import com.hiczp.telegram.bot.protocol.TelegramBotApi
import com.hiczp.telegram.bot.protocol.model.Message
import com.hiczp.telegram.bot.protocol.model.deleteMessage
import com.hiczp.telegram.bot.protocol.model.deleteMessages
import com.hiczp.telegram.bot.protocol.type.TelegramResponse

/**
 * Returns `true` if this message is inaccessible.
 *
 * An inaccessible message has a `date` of 0, which indicates the message
 * was deleted or is otherwise unavailable.
 */
val Message.isInaccessible get() = date == 0L

/**
 * Returns `true` if this message is a bot command.
 *
 * A message is considered a command if its text content starts with "/".
 * This is a simple check and does not validate the command format or
 * check for bot username suffixes (e.g., "/command@bot_username").
 */
val Message.isCommand get() = text?.startsWith("/") == true

suspend fun TelegramBotApi.deleteMessage(message: Message) =
    deleteMessage(
        chatId = message.chat.id.toString(),
        messageId = message.messageId,
    )

suspend fun TelegramBotApi.deleteMessages(messages: Collection<Message>): TelegramResponse<Boolean> {
    require(messages.isNotEmpty()) { "Messages must not be empty" }
    require(messages.map { it.chat.id }.distinct().size == 1) { "All messages must be in the same chat" }
    return deleteMessages(
        chatId = messages.first().chat.id.toString(),
        messageIds = messages.map { it.messageId },
    )
}

suspend fun TelegramBotApi.deleteMessages(vararg messages: Message) = deleteMessages(messages.toList())
