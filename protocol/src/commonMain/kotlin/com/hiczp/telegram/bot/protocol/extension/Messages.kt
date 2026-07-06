package com.hiczp.telegram.bot.protocol.extension

import com.hiczp.telegram.bot.protocol.TelegramBotApi
import com.hiczp.telegram.bot.protocol.model.MessageEntity
import com.hiczp.telegram.bot.protocol.model.MaybeInaccessibleMessage
import com.hiczp.telegram.bot.protocol.model.Message
import com.hiczp.telegram.bot.protocol.model.PhotoSize
import com.hiczp.telegram.bot.protocol.model.ReplyParameters
import com.hiczp.telegram.bot.protocol.model.deleteMessage
import com.hiczp.telegram.bot.protocol.model.deleteMessages
import com.hiczp.telegram.bot.protocol.type.TelegramResponse

/**
 * Returns `true` if this message-like object is inaccessible.
 *
 * An inaccessible message has a `date` of 0, which indicates the message
 * was deleted or is otherwise unavailable.
 */
val MaybeInaccessibleMessage.isInaccessible get() = date == 0L

/**
 * Returns `true` if this message is a bot command.
 *
 * A message is considered a command if its text content starts with "/".
 * This is a simple check and does not validate the command format or
 * check for bot username suffixes (e.g., "/command@bot_username").
 */
val Message.isCommand get() = text?.startsWith("/") == true

/**
 * The largest available photo size in this collection, preferring higher pixel area
 * and using file size as a tie-breaker when it is available.
 */
val Iterable<PhotoSize>.largestPhotoSize: PhotoSize?
    get() = maxWithOrNull(compareBy<PhotoSize> { it.width * it.height }.thenBy { it.fileSize ?: 0L })

/**
 * The largest available photo size attached to this message.
 */
val Message.largestPhoto get() = photo?.largestPhotoSize

/**
 * Builds reply parameters that target this message.
 *
 * [includeChatId] is useful when replying across chats. Leave it disabled for
 * ordinary same-chat replies, especially for business account messages where
 * Telegram does not support cross-chat reply parameters.
 */
fun Message.toReplyParameters(
    includeChatId: Boolean = false,
    allowSendingWithoutReply: Boolean? = null,
    quote: String? = null,
    quoteParseMode: String? = null,
    quoteEntities: List<MessageEntity>? = null,
    quotePosition: Long? = null,
    checklistTaskId: Long? = null,
    pollOptionId: String? = null,
) = ReplyParameters(
    messageId = messageId,
    chatId = if (includeChatId) chat.id.toString() else null,
    allowSendingWithoutReply = allowSendingWithoutReply,
    quote = quote,
    quoteParseMode = quoteParseMode,
    quoteEntities = quoteEntities,
    quotePosition = quotePosition,
    checklistTaskId = checklistTaskId,
    pollOptionId = pollOptionId,
)

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
