package com.hiczp.telegram.bot.application.context.action

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.protocol.event.CallbackQueryEvent
import com.hiczp.telegram.bot.protocol.form.editMessageMedia
import com.hiczp.telegram.bot.protocol.model.*
import com.hiczp.telegram.bot.protocol.type.TelegramResponse
import io.ktor.client.request.forms.*

suspend fun TelegramBotEventContext<CallbackQueryEvent>.answerCallbackQuery(
    text: String? = null,
) = client.answerCallbackQuery(
    callbackQueryId = event.callbackQuery.id,
    text = text,
)

suspend fun TelegramBotEventContext<CallbackQueryEvent>.editMessageText(
    text: String,
    inlineKeyboardMarkup: InlineKeyboardMarkup? = null,
) = client.editMessageText(
    businessConnectionId = event.callbackQuery.message?.businessConnectionId,
    chatId = event.callbackQuery.message?.chat?.id?.toString(),
    messageId = event.callbackQuery.message?.messageId,
    inlineMessageId = event.callbackQuery.inlineMessageId,
    text = text,
    replyMarkup = inlineKeyboardMarkup,
)

suspend fun TelegramBotEventContext<CallbackQueryEvent>.editMessageCaption(
    caption: String,
    inlineKeyboardMarkup: InlineKeyboardMarkup? = null,
) = client.editMessageCaption(
    businessConnectionId = event.callbackQuery.message?.businessConnectionId,
    chatId = event.callbackQuery.message?.chat?.id?.toString(),
    messageId = event.callbackQuery.message?.messageId,
    inlineMessageId = event.callbackQuery.inlineMessageId,
    caption = caption,
    replyMarkup = inlineKeyboardMarkup,
)

suspend fun TelegramBotEventContext<CallbackQueryEvent>.editMessageMedia(
    media: InputMedia,
    attachments: List<FormPart<ChannelProvider>>? = null,
    inlineKeyboardMarkup: InlineKeyboardMarkup? = null,
) = client.editMessageMedia(
    businessConnectionId = event.callbackQuery.message?.businessConnectionId,
    chatId = event.callbackQuery.message?.chat?.id?.toString(),
    messageId = event.callbackQuery.message?.messageId,
    inlineMessageId = event.callbackQuery.inlineMessageId,
    media = media,
    replyMarkup = inlineKeyboardMarkup,
    attachments = attachments,
)

suspend fun TelegramBotEventContext<CallbackQueryEvent>.editMessageLiveLocation(
    latitude: Double,
    longitude: Double,
    inlineKeyboardMarkup: InlineKeyboardMarkup? = null,
) = client.editMessageLiveLocation(
    businessConnectionId = event.callbackQuery.message?.businessConnectionId,
    chatId = event.callbackQuery.message?.chat?.id?.toString(),
    messageId = event.callbackQuery.message?.messageId,
    inlineMessageId = event.callbackQuery.inlineMessageId,
    latitude = latitude,
    longitude = longitude,
    replyMarkup = inlineKeyboardMarkup,
)

suspend fun TelegramBotEventContext<CallbackQueryEvent>.stopMessageLiveLocation(
    inlineKeyboardMarkup: InlineKeyboardMarkup? = null,
) = client.stopMessageLiveLocation(
    businessConnectionId = event.callbackQuery.message?.businessConnectionId,
    chatId = event.callbackQuery.message?.chat?.id?.toString(),
    messageId = event.callbackQuery.message?.messageId,
    inlineMessageId = event.callbackQuery.inlineMessageId,
    replyMarkup = inlineKeyboardMarkup,
)

suspend fun TelegramBotEventContext<CallbackQueryEvent>.editMessageChecklist(
    checklist: InputChecklist,
    inlineKeyboardMarkup: InlineKeyboardMarkup? = null,
): TelegramResponse<Message> {
    val errorMessage = { "Message must be sent by business connection" }
    return client.editMessageChecklist(
        businessConnectionId = checkNotNull(event.callbackQuery.message?.businessConnectionId, errorMessage),
        chatId = checkNotNull(event.callbackQuery.message?.chat?.id, errorMessage),
        messageId = checkNotNull(event.callbackQuery.message?.messageId, errorMessage),
        checklist = checklist,
        replyMarkup = inlineKeyboardMarkup,
    )
}

suspend fun TelegramBotEventContext<CallbackQueryEvent>.editMessageReplyMarkup(
    inlineKeyboardMarkup: InlineKeyboardMarkup? = null,
) = client.editMessageReplyMarkup(
    businessConnectionId = event.callbackQuery.message?.businessConnectionId,
    chatId = event.callbackQuery.message?.chat?.id?.toString(),
    messageId = event.callbackQuery.message?.messageId,
    inlineMessageId = event.callbackQuery.inlineMessageId,
    replyMarkup = inlineKeyboardMarkup,
)

suspend fun TelegramBotEventContext<CallbackQueryEvent>.stopPoll(
    inlineKeyboardMarkup: InlineKeyboardMarkup? = null,
): TelegramResponse<Poll> {
    val errorMessage = { "Can't access original message" }
    return client.stopPoll(
        businessConnectionId = event.callbackQuery.message?.businessConnectionId,
        chatId = checkNotNull(event.callbackQuery.message?.chat?.id, errorMessage).toString(),
        messageId = checkNotNull(event.callbackQuery.message?.messageId, errorMessage),
        replyMarkup = inlineKeyboardMarkup,
    )
}

suspend fun TelegramBotEventContext<CallbackQueryEvent>.deleteMessage(): TelegramResponse<Boolean> {
    val errorMessage = { "Can't access original message" }
    return client.deleteMessage(
        chatId = checkNotNull(event.callbackQuery.message?.chat?.id, errorMessage).toString(),
        messageId = checkNotNull(event.callbackQuery.message?.messageId, errorMessage),
    )
}
