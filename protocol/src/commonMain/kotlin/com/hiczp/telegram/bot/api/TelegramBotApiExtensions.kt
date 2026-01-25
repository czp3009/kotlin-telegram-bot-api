// Auto-generated from Swagger specificationDo not modify this file manuallyExtension functions for multipart operations
package com.hiczp.telegram.bot.api

import com.hiczp.telegram.bot.api.model.EditMessageMediaRequest
import com.hiczp.telegram.bot.api.model.File
import com.hiczp.telegram.bot.api.model.Message
import com.hiczp.telegram.bot.api.model.SendAnimationRequest
import com.hiczp.telegram.bot.api.model.SendAudioRequest
import com.hiczp.telegram.bot.api.model.SendDocumentRequest
import com.hiczp.telegram.bot.api.model.SendMediaGroupRequest
import com.hiczp.telegram.bot.api.model.SendPhotoRequest
import com.hiczp.telegram.bot.api.model.SendStickerRequest
import com.hiczp.telegram.bot.api.model.SendVideoNoteRequest
import com.hiczp.telegram.bot.api.model.SendVideoRequest
import com.hiczp.telegram.bot.api.model.SendVoiceRequest
import com.hiczp.telegram.bot.api.model.SetChatPhotoRequest
import com.hiczp.telegram.bot.api.model.SetStickerSetThumbnailRequest
import com.hiczp.telegram.bot.api.model.SetWebhookRequest
import com.hiczp.telegram.bot.api.model.UploadStickerFileRequest
import com.hiczp.telegram.bot.api.type.TelegramResponse
import kotlin.Boolean
import kotlin.collections.List

public suspend fun TelegramBotApi.setWebhook(request: SetWebhookRequest): TelegramResponse<Boolean> = setWebhook(
    url = request.url,
    certificate = request.certificate,
    ipAddress = request.ipAddress,
    maxConnections = request.maxConnections,
    allowedUpdates = request.allowedUpdates,
    dropPendingUpdates = request.dropPendingUpdates,
    secretToken = request.secretToken
)

public suspend fun TelegramBotApi.sendPhoto(request: SendPhotoRequest): TelegramResponse<Message> = sendPhoto(
    businessConnectionId = request.businessConnectionId,
    chatId = request.chatId,
    messageThreadId = request.messageThreadId,
    directMessagesTopicId = request.directMessagesTopicId,
    photo = request.photo,
    caption = request.caption,
    parseMode = request.parseMode,
    captionEntities = request.captionEntities,
    showCaptionAboveMedia = request.showCaptionAboveMedia,
    hasSpoiler = request.hasSpoiler,
    disableNotification = request.disableNotification,
    protectContent = request.protectContent,
    allowPaidBroadcast = request.allowPaidBroadcast,
    messageEffectId = request.messageEffectId,
    suggestedPostParameters = request.suggestedPostParameters,
    replyParameters = request.replyParameters,
    replyMarkup = request.replyMarkup
)

public suspend fun TelegramBotApi.sendAudio(request: SendAudioRequest): TelegramResponse<Message> = sendAudio(
    businessConnectionId = request.businessConnectionId,
    chatId = request.chatId,
    messageThreadId = request.messageThreadId,
    directMessagesTopicId = request.directMessagesTopicId,
    audio = request.audio,
    caption = request.caption,
    parseMode = request.parseMode,
    captionEntities = request.captionEntities,
    duration = request.duration,
    performer = request.performer,
    title = request.title,
    thumbnail = request.thumbnail,
    disableNotification = request.disableNotification,
    protectContent = request.protectContent,
    allowPaidBroadcast = request.allowPaidBroadcast,
    messageEffectId = request.messageEffectId,
    suggestedPostParameters = request.suggestedPostParameters,
    replyParameters = request.replyParameters,
    replyMarkup = request.replyMarkup
)

public suspend fun TelegramBotApi.sendDocument(request: SendDocumentRequest): TelegramResponse<Message> = sendDocument(
    businessConnectionId = request.businessConnectionId,
    chatId = request.chatId,
    messageThreadId = request.messageThreadId,
    directMessagesTopicId = request.directMessagesTopicId,
    document = request.document,
    thumbnail = request.thumbnail,
    caption = request.caption,
    parseMode = request.parseMode,
    captionEntities = request.captionEntities,
    disableContentTypeDetection = request.disableContentTypeDetection,
    disableNotification = request.disableNotification,
    protectContent = request.protectContent,
    allowPaidBroadcast = request.allowPaidBroadcast,
    messageEffectId = request.messageEffectId,
    suggestedPostParameters = request.suggestedPostParameters,
    replyParameters = request.replyParameters,
    replyMarkup = request.replyMarkup
)

public suspend fun TelegramBotApi.sendVideo(request: SendVideoRequest): TelegramResponse<Message> = sendVideo(
    businessConnectionId = request.businessConnectionId,
    chatId = request.chatId,
    messageThreadId = request.messageThreadId,
    directMessagesTopicId = request.directMessagesTopicId,
    video = request.video,
    duration = request.duration,
    width = request.width,
    height = request.height,
    thumbnail = request.thumbnail,
    cover = request.cover,
    startTimestamp = request.startTimestamp,
    caption = request.caption,
    parseMode = request.parseMode,
    captionEntities = request.captionEntities,
    showCaptionAboveMedia = request.showCaptionAboveMedia,
    hasSpoiler = request.hasSpoiler,
    supportsStreaming = request.supportsStreaming,
    disableNotification = request.disableNotification,
    protectContent = request.protectContent,
    allowPaidBroadcast = request.allowPaidBroadcast,
    messageEffectId = request.messageEffectId,
    suggestedPostParameters = request.suggestedPostParameters,
    replyParameters = request.replyParameters,
    replyMarkup = request.replyMarkup
)

public suspend fun TelegramBotApi.sendAnimation(request: SendAnimationRequest): TelegramResponse<Message> = sendAnimation(
    businessConnectionId = request.businessConnectionId,
    chatId = request.chatId,
    messageThreadId = request.messageThreadId,
    directMessagesTopicId = request.directMessagesTopicId,
    animation = request.animation,
    duration = request.duration,
    width = request.width,
    height = request.height,
    thumbnail = request.thumbnail,
    caption = request.caption,
    parseMode = request.parseMode,
    captionEntities = request.captionEntities,
    showCaptionAboveMedia = request.showCaptionAboveMedia,
    hasSpoiler = request.hasSpoiler,
    disableNotification = request.disableNotification,
    protectContent = request.protectContent,
    allowPaidBroadcast = request.allowPaidBroadcast,
    messageEffectId = request.messageEffectId,
    suggestedPostParameters = request.suggestedPostParameters,
    replyParameters = request.replyParameters,
    replyMarkup = request.replyMarkup
)

public suspend fun TelegramBotApi.sendVoice(request: SendVoiceRequest): TelegramResponse<Message> = sendVoice(
    businessConnectionId = request.businessConnectionId,
    chatId = request.chatId,
    messageThreadId = request.messageThreadId,
    directMessagesTopicId = request.directMessagesTopicId,
    voice = request.voice,
    caption = request.caption,
    parseMode = request.parseMode,
    captionEntities = request.captionEntities,
    duration = request.duration,
    disableNotification = request.disableNotification,
    protectContent = request.protectContent,
    allowPaidBroadcast = request.allowPaidBroadcast,
    messageEffectId = request.messageEffectId,
    suggestedPostParameters = request.suggestedPostParameters,
    replyParameters = request.replyParameters,
    replyMarkup = request.replyMarkup
)

public suspend fun TelegramBotApi.sendVideoNote(request: SendVideoNoteRequest): TelegramResponse<Message> = sendVideoNote(
    businessConnectionId = request.businessConnectionId,
    chatId = request.chatId,
    messageThreadId = request.messageThreadId,
    directMessagesTopicId = request.directMessagesTopicId,
    videoNote = request.videoNote,
    duration = request.duration,
    length = request.length,
    thumbnail = request.thumbnail,
    disableNotification = request.disableNotification,
    protectContent = request.protectContent,
    allowPaidBroadcast = request.allowPaidBroadcast,
    messageEffectId = request.messageEffectId,
    suggestedPostParameters = request.suggestedPostParameters,
    replyParameters = request.replyParameters,
    replyMarkup = request.replyMarkup
)

public suspend fun TelegramBotApi.sendMediaGroup(request: SendMediaGroupRequest): TelegramResponse<List<Message>> = sendMediaGroup(
    businessConnectionId = request.businessConnectionId,
    chatId = request.chatId,
    messageThreadId = request.messageThreadId,
    directMessagesTopicId = request.directMessagesTopicId,
    media = request.media,
    disableNotification = request.disableNotification,
    protectContent = request.protectContent,
    allowPaidBroadcast = request.allowPaidBroadcast,
    messageEffectId = request.messageEffectId,
    replyParameters = request.replyParameters
)

public suspend fun TelegramBotApi.setChatPhoto(request: SetChatPhotoRequest): TelegramResponse<Boolean> = setChatPhoto(
    chatId = request.chatId,
    photo = request.photo
)

public suspend fun TelegramBotApi.editMessageMedia(request: EditMessageMediaRequest): TelegramResponse<Boolean> = editMessageMedia(
    businessConnectionId = request.businessConnectionId,
    chatId = request.chatId,
    messageId = request.messageId,
    inlineMessageId = request.inlineMessageId,
    media = request.media,
    replyMarkup = request.replyMarkup
)

public suspend fun TelegramBotApi.sendSticker(request: SendStickerRequest): TelegramResponse<Message> = sendSticker(
    businessConnectionId = request.businessConnectionId,
    chatId = request.chatId,
    messageThreadId = request.messageThreadId,
    directMessagesTopicId = request.directMessagesTopicId,
    sticker = request.sticker,
    emoji = request.emoji,
    disableNotification = request.disableNotification,
    protectContent = request.protectContent,
    allowPaidBroadcast = request.allowPaidBroadcast,
    messageEffectId = request.messageEffectId,
    suggestedPostParameters = request.suggestedPostParameters,
    replyParameters = request.replyParameters,
    replyMarkup = request.replyMarkup
)

public suspend fun TelegramBotApi.uploadStickerFile(request: UploadStickerFileRequest): TelegramResponse<File> = uploadStickerFile(
    userId = request.userId,
    sticker = request.sticker,
    stickerFormat = request.stickerFormat
)

public suspend fun TelegramBotApi.setStickerSetThumbnail(request: SetStickerSetThumbnailRequest): TelegramResponse<Boolean> = setStickerSetThumbnail(
    name = request.name,
    userId = request.userId,
    thumbnail = request.thumbnail,
    format = request.format
)
