package com.hiczp.telegram.bot.application.context

import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.model.*

suspend fun TelegramBotEventContext<MessageEvent>.reply(
    text: String,
    parseMode: String? = null,
    entities: List<MessageEntity>? = null,
    linkPreviewOptions: LinkPreviewOptions? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
) = client.sendMessage(
    businessConnectionId = event.message.businessConnectionId,
    chatId = event.message.chat.id.toString(),
    messageThreadId = event.message.messageThreadId,
    directMessagesTopicId = event.message.directMessagesTopic?.topicId,
    text = text,
    parseMode = parseMode,
    entities = entities,
    linkPreviewOptions = linkPreviewOptions,
    disableNotification = disableNotification,
    protectContent = protectContent,
    allowPaidBroadcast = allowPaidBroadcast,
    messageEffectId = messageEffectId,
    suggestedPostParameters = suggestedPostParameters,
    replyParameters = replyParameters,
    replyMarkup = replyMarkup,
)
