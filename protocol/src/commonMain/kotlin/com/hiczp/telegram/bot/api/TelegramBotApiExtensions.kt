// Auto-generated from Swagger specificationDo not modify this file manuallyExtension functions for multipart operations
package com.hiczp.telegram.bot.api

import com.hiczp.telegram.bot.api.form.EditMessageMediaForm
import com.hiczp.telegram.bot.api.form.SendAnimationForm
import com.hiczp.telegram.bot.api.form.SendAudioForm
import com.hiczp.telegram.bot.api.form.SendDocumentForm
import com.hiczp.telegram.bot.api.form.SendMediaGroupForm
import com.hiczp.telegram.bot.api.form.SendPhotoForm
import com.hiczp.telegram.bot.api.form.SendStickerForm
import com.hiczp.telegram.bot.api.form.SendVideoForm
import com.hiczp.telegram.bot.api.form.SendVideoNoteForm
import com.hiczp.telegram.bot.api.form.SendVoiceForm
import com.hiczp.telegram.bot.api.form.SetChatPhotoForm
import com.hiczp.telegram.bot.api.form.SetStickerSetThumbnailForm
import com.hiczp.telegram.bot.api.form.SetWebhookForm
import com.hiczp.telegram.bot.api.form.UploadStickerFileForm
import com.hiczp.telegram.bot.api.model.File
import com.hiczp.telegram.bot.api.model.Message
import com.hiczp.telegram.bot.api.type.TelegramResponse
import kotlin.Boolean
import kotlin.collections.List

public suspend fun TelegramBotApi.setWebhook(form: SetWebhookForm): TelegramResponse<Boolean> = setWebhook(
    url = form.url,
    certificate = form.certificate,
    ipAddress = form.ipAddress,
    maxConnections = form.maxConnections,
    allowedUpdates = form.allowedUpdates,
    dropPendingUpdates = form.dropPendingUpdates,
    secretToken = form.secretToken
)

public suspend fun TelegramBotApi.sendPhoto(form: SendPhotoForm): TelegramResponse<Message> = sendPhoto(
    businessConnectionId = form.businessConnectionId,
    chatId = form.chatId,
    messageThreadId = form.messageThreadId,
    directMessagesTopicId = form.directMessagesTopicId,
    photo = form.photo,
    caption = form.caption,
    parseMode = form.parseMode,
    captionEntities = form.captionEntities,
    showCaptionAboveMedia = form.showCaptionAboveMedia,
    hasSpoiler = form.hasSpoiler,
    disableNotification = form.disableNotification,
    protectContent = form.protectContent,
    allowPaidBroadcast = form.allowPaidBroadcast,
    messageEffectId = form.messageEffectId,
    suggestedPostParameters = form.suggestedPostParameters,
    replyParameters = form.replyParameters,
    replyMarkup = form.replyMarkup
)

public suspend fun TelegramBotApi.sendAudio(form: SendAudioForm): TelegramResponse<Message> = sendAudio(
    businessConnectionId = form.businessConnectionId,
    chatId = form.chatId,
    messageThreadId = form.messageThreadId,
    directMessagesTopicId = form.directMessagesTopicId,
    audio = form.audio,
    caption = form.caption,
    parseMode = form.parseMode,
    captionEntities = form.captionEntities,
    duration = form.duration,
    performer = form.performer,
    title = form.title,
    thumbnail = form.thumbnail,
    disableNotification = form.disableNotification,
    protectContent = form.protectContent,
    allowPaidBroadcast = form.allowPaidBroadcast,
    messageEffectId = form.messageEffectId,
    suggestedPostParameters = form.suggestedPostParameters,
    replyParameters = form.replyParameters,
    replyMarkup = form.replyMarkup
)

public suspend fun TelegramBotApi.sendDocument(form: SendDocumentForm): TelegramResponse<Message> = sendDocument(
    businessConnectionId = form.businessConnectionId,
    chatId = form.chatId,
    messageThreadId = form.messageThreadId,
    directMessagesTopicId = form.directMessagesTopicId,
    document = form.document,
    thumbnail = form.thumbnail,
    caption = form.caption,
    parseMode = form.parseMode,
    captionEntities = form.captionEntities,
    disableContentTypeDetection = form.disableContentTypeDetection,
    disableNotification = form.disableNotification,
    protectContent = form.protectContent,
    allowPaidBroadcast = form.allowPaidBroadcast,
    messageEffectId = form.messageEffectId,
    suggestedPostParameters = form.suggestedPostParameters,
    replyParameters = form.replyParameters,
    replyMarkup = form.replyMarkup
)

public suspend fun TelegramBotApi.sendVideo(form: SendVideoForm): TelegramResponse<Message> = sendVideo(
    businessConnectionId = form.businessConnectionId,
    chatId = form.chatId,
    messageThreadId = form.messageThreadId,
    directMessagesTopicId = form.directMessagesTopicId,
    video = form.video,
    duration = form.duration,
    width = form.width,
    height = form.height,
    thumbnail = form.thumbnail,
    cover = form.cover,
    startTimestamp = form.startTimestamp,
    caption = form.caption,
    parseMode = form.parseMode,
    captionEntities = form.captionEntities,
    showCaptionAboveMedia = form.showCaptionAboveMedia,
    hasSpoiler = form.hasSpoiler,
    supportsStreaming = form.supportsStreaming,
    disableNotification = form.disableNotification,
    protectContent = form.protectContent,
    allowPaidBroadcast = form.allowPaidBroadcast,
    messageEffectId = form.messageEffectId,
    suggestedPostParameters = form.suggestedPostParameters,
    replyParameters = form.replyParameters,
    replyMarkup = form.replyMarkup
)

public suspend fun TelegramBotApi.sendAnimation(form: SendAnimationForm): TelegramResponse<Message> = sendAnimation(
    businessConnectionId = form.businessConnectionId,
    chatId = form.chatId,
    messageThreadId = form.messageThreadId,
    directMessagesTopicId = form.directMessagesTopicId,
    animation = form.animation,
    duration = form.duration,
    width = form.width,
    height = form.height,
    thumbnail = form.thumbnail,
    caption = form.caption,
    parseMode = form.parseMode,
    captionEntities = form.captionEntities,
    showCaptionAboveMedia = form.showCaptionAboveMedia,
    hasSpoiler = form.hasSpoiler,
    disableNotification = form.disableNotification,
    protectContent = form.protectContent,
    allowPaidBroadcast = form.allowPaidBroadcast,
    messageEffectId = form.messageEffectId,
    suggestedPostParameters = form.suggestedPostParameters,
    replyParameters = form.replyParameters,
    replyMarkup = form.replyMarkup
)

public suspend fun TelegramBotApi.sendVoice(form: SendVoiceForm): TelegramResponse<Message> = sendVoice(
    businessConnectionId = form.businessConnectionId,
    chatId = form.chatId,
    messageThreadId = form.messageThreadId,
    directMessagesTopicId = form.directMessagesTopicId,
    voice = form.voice,
    caption = form.caption,
    parseMode = form.parseMode,
    captionEntities = form.captionEntities,
    duration = form.duration,
    disableNotification = form.disableNotification,
    protectContent = form.protectContent,
    allowPaidBroadcast = form.allowPaidBroadcast,
    messageEffectId = form.messageEffectId,
    suggestedPostParameters = form.suggestedPostParameters,
    replyParameters = form.replyParameters,
    replyMarkup = form.replyMarkup
)

public suspend fun TelegramBotApi.sendVideoNote(form: SendVideoNoteForm): TelegramResponse<Message> = sendVideoNote(
    businessConnectionId = form.businessConnectionId,
    chatId = form.chatId,
    messageThreadId = form.messageThreadId,
    directMessagesTopicId = form.directMessagesTopicId,
    videoNote = form.videoNote,
    duration = form.duration,
    length = form.length,
    thumbnail = form.thumbnail,
    disableNotification = form.disableNotification,
    protectContent = form.protectContent,
    allowPaidBroadcast = form.allowPaidBroadcast,
    messageEffectId = form.messageEffectId,
    suggestedPostParameters = form.suggestedPostParameters,
    replyParameters = form.replyParameters,
    replyMarkup = form.replyMarkup
)

public suspend fun TelegramBotApi.sendMediaGroup(form: SendMediaGroupForm): TelegramResponse<List<Message>> = sendMediaGroup(
    businessConnectionId = form.businessConnectionId,
    chatId = form.chatId,
    messageThreadId = form.messageThreadId,
    directMessagesTopicId = form.directMessagesTopicId,
    media = form.media,
    disableNotification = form.disableNotification,
    protectContent = form.protectContent,
    allowPaidBroadcast = form.allowPaidBroadcast,
    messageEffectId = form.messageEffectId,
    replyParameters = form.replyParameters
)

public suspend fun TelegramBotApi.setChatPhoto(form: SetChatPhotoForm): TelegramResponse<Boolean> = setChatPhoto(
    chatId = form.chatId,
    photo = form.photo
)

public suspend fun TelegramBotApi.editMessageMedia(form: EditMessageMediaForm): TelegramResponse<Boolean> = editMessageMedia(
    businessConnectionId = form.businessConnectionId,
    chatId = form.chatId,
    messageId = form.messageId,
    inlineMessageId = form.inlineMessageId,
    media = form.media,
    replyMarkup = form.replyMarkup
)

public suspend fun TelegramBotApi.sendSticker(form: SendStickerForm): TelegramResponse<Message> = sendSticker(
    businessConnectionId = form.businessConnectionId,
    chatId = form.chatId,
    messageThreadId = form.messageThreadId,
    directMessagesTopicId = form.directMessagesTopicId,
    sticker = form.sticker,
    emoji = form.emoji,
    disableNotification = form.disableNotification,
    protectContent = form.protectContent,
    allowPaidBroadcast = form.allowPaidBroadcast,
    messageEffectId = form.messageEffectId,
    suggestedPostParameters = form.suggestedPostParameters,
    replyParameters = form.replyParameters,
    replyMarkup = form.replyMarkup
)

public suspend fun TelegramBotApi.uploadStickerFile(form: UploadStickerFileForm): TelegramResponse<File> = uploadStickerFile(
    userId = form.userId,
    sticker = form.sticker,
    stickerFormat = form.stickerFormat
)

public suspend fun TelegramBotApi.setStickerSetThumbnail(form: SetStickerSetThumbnailForm): TelegramResponse<Boolean> = setStickerSetThumbnail(
    name = form.name,
    userId = form.userId,
    thumbnail = form.thumbnail,
    format = form.format
)
