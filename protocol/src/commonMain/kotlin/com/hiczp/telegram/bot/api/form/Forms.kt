// Auto-generated from Swagger specification, do not modify this file manuallyExtension functions for Telegram Bot API multipart operations.
//
// This file provides three types of convenient functions for operations that require
// multipart form data with file uploads:
//
// 1. **Scatter functions**: Accept individual parameters as named arguments
//    Example: `api.sendPhoto(chatId = 123, photo = fileChannel)`
//
// 2. **Bridge functions**: Accept String file refs and convert to FormPart
//    Example: `api.sendPhoto(chatId = 123, photo = "fileId")`
//
// 3. **Form functions**: Accept pre-constructed form wrapper classes
//    Example: `api.sendPhoto(form = SendPhotoForm(...))`
@file:Suppress(
    "RedundantVisibilityModifier",
    "unused",
    "DuplicatedCode",
)

package com.hiczp.telegram.bot.api.form

import com.hiczp.telegram.bot.api.TelegramBotApi
import com.hiczp.telegram.bot.api.model.File
import com.hiczp.telegram.bot.api.model.InlineKeyboardMarkup
import com.hiczp.telegram.bot.api.model.InputMedia
import com.hiczp.telegram.bot.api.model.InputPaidMedia
import com.hiczp.telegram.bot.api.model.InputProfilePhoto
import com.hiczp.telegram.bot.api.model.InputSticker
import com.hiczp.telegram.bot.api.model.InputStoryContent
import com.hiczp.telegram.bot.api.model.Message
import com.hiczp.telegram.bot.api.model.MessageEntity
import com.hiczp.telegram.bot.api.model.ReplyMarkup
import com.hiczp.telegram.bot.api.model.ReplyParameters
import com.hiczp.telegram.bot.api.model.Story
import com.hiczp.telegram.bot.api.model.StoryArea
import com.hiczp.telegram.bot.api.model.SuggestedPostParameters
import com.hiczp.telegram.bot.api.type.TelegramResponse
import io.ktor.client.request.forms.ChannelProvider
import io.ktor.client.request.forms.FormPart
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.utils.io.ByteReadChannel
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlinx.serialization.json.Json

public suspend fun TelegramBotApi.setWebhook(
    url: String,
    certificate: FormPart<ChannelProvider>? = null,
    ipAddress: String? = null,
    maxConnections: Long? = null,
    allowedUpdates: List<String>? = null,
    dropPendingUpdates: Boolean? = null,
    secretToken: String? = null,
): TelegramResponse<Boolean> {
    val formData = MultiPartFormDataContent(formData {
        append("url", url)
        certificate?.let { append(it) }
        ipAddress?.let {
            append("ip_address", it)
        }
        maxConnections?.let {
            append("max_connections", it.toString())
        }
        allowedUpdates?.let {
            append("allowed_updates", Json.encodeToString(it))
        }
        dropPendingUpdates?.let {
            append("drop_pending_updates", it.toString())
        }
        secretToken?.let {
            append("secret_token", it)
        }
    })
    return setWebhook(formData)
}

public suspend fun TelegramBotApi.setWebhook(
    url: String,
    certificate: String? = null,
    ipAddress: String? = null,
    maxConnections: Long? = null,
    allowedUpdates: List<String>? = null,
    dropPendingUpdates: Boolean? = null,
    secretToken: String? = null,
): TelegramResponse<Boolean> = setWebhook(
    url = url,
    certificate = certificate?.let { FormPart("certificate", ChannelProvider { ByteReadChannel(it) }) },
    ipAddress = ipAddress,
    maxConnections = maxConnections,
    allowedUpdates = allowedUpdates,
    dropPendingUpdates = dropPendingUpdates,
    secretToken = secretToken
)

public suspend fun TelegramBotApi.setWebhook(form: SetWebhookForm): TelegramResponse<Boolean> = setWebhook(
    url = form.url,
    certificate = form.certificate,
    ipAddress = form.ipAddress,
    maxConnections = form.maxConnections,
    allowedUpdates = form.allowedUpdates,
    dropPendingUpdates = form.dropPendingUpdates,
    secretToken = form.secretToken
)

public suspend fun TelegramBotApi.sendPhoto(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    photo: FormPart<ChannelProvider>,
    caption: String? = null,
    parseMode: String? = null,
    captionEntities: List<MessageEntity>? = null,
    showCaptionAboveMedia: Boolean? = null,
    hasSpoiler: Boolean? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> {
    val formData = MultiPartFormDataContent(formData {
        businessConnectionId?.let {
            append("business_connection_id", it)
        }
        append("chat_id", chatId)
        messageThreadId?.let {
            append("message_thread_id", it.toString())
        }
        directMessagesTopicId?.let {
            append("direct_messages_topic_id", it.toString())
        }
        append(photo)
        caption?.let {
            append("caption", it)
        }
        parseMode?.let {
            append("parse_mode", it)
        }
        captionEntities?.let {
            append("caption_entities", Json.encodeToString(it))
        }
        showCaptionAboveMedia?.let {
            append("show_caption_above_media", it.toString())
        }
        hasSpoiler?.let {
            append("has_spoiler", it.toString())
        }
        disableNotification?.let {
            append("disable_notification", it.toString())
        }
        protectContent?.let {
            append("protect_content", it.toString())
        }
        allowPaidBroadcast?.let {
            append("allow_paid_broadcast", it.toString())
        }
        messageEffectId?.let {
            append("message_effect_id", it)
        }
        suggestedPostParameters?.let {
            append("suggested_post_parameters", Json.encodeToString(it))
        }
        replyParameters?.let {
            append("reply_parameters", Json.encodeToString(it))
        }
        replyMarkup?.let {
            append("reply_markup", Json.encodeToString(it))
        }
    })
    return sendPhoto(formData)
}

public suspend fun TelegramBotApi.sendPhoto(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    photo: String,
    caption: String? = null,
    parseMode: String? = null,
    captionEntities: List<MessageEntity>? = null,
    showCaptionAboveMedia: Boolean? = null,
    hasSpoiler: Boolean? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> = sendPhoto(
    businessConnectionId = businessConnectionId,
    chatId = chatId,
    messageThreadId = messageThreadId,
    directMessagesTopicId = directMessagesTopicId,
    photo = FormPart("photo", ChannelProvider { ByteReadChannel(photo) }),
    caption = caption,
    parseMode = parseMode,
    captionEntities = captionEntities,
    showCaptionAboveMedia = showCaptionAboveMedia,
    hasSpoiler = hasSpoiler,
    disableNotification = disableNotification,
    protectContent = protectContent,
    allowPaidBroadcast = allowPaidBroadcast,
    messageEffectId = messageEffectId,
    suggestedPostParameters = suggestedPostParameters,
    replyParameters = replyParameters,
    replyMarkup = replyMarkup
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

public suspend fun TelegramBotApi.sendAudio(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    audio: FormPart<ChannelProvider>,
    caption: String? = null,
    parseMode: String? = null,
    captionEntities: List<MessageEntity>? = null,
    duration: Long? = null,
    performer: String? = null,
    title: String? = null,
    thumbnail: FormPart<ChannelProvider>? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> {
    val formData = MultiPartFormDataContent(formData {
        businessConnectionId?.let {
            append("business_connection_id", it)
        }
        append("chat_id", chatId)
        messageThreadId?.let {
            append("message_thread_id", it.toString())
        }
        directMessagesTopicId?.let {
            append("direct_messages_topic_id", it.toString())
        }
        append(audio)
        caption?.let {
            append("caption", it)
        }
        parseMode?.let {
            append("parse_mode", it)
        }
        captionEntities?.let {
            append("caption_entities", Json.encodeToString(it))
        }
        duration?.let {
            append("duration", it.toString())
        }
        performer?.let {
            append("performer", it)
        }
        title?.let {
            append("title", it)
        }
        thumbnail?.let { append(it) }
        disableNotification?.let {
            append("disable_notification", it.toString())
        }
        protectContent?.let {
            append("protect_content", it.toString())
        }
        allowPaidBroadcast?.let {
            append("allow_paid_broadcast", it.toString())
        }
        messageEffectId?.let {
            append("message_effect_id", it)
        }
        suggestedPostParameters?.let {
            append("suggested_post_parameters", Json.encodeToString(it))
        }
        replyParameters?.let {
            append("reply_parameters", Json.encodeToString(it))
        }
        replyMarkup?.let {
            append("reply_markup", Json.encodeToString(it))
        }
    })
    return sendAudio(formData)
}

public suspend fun TelegramBotApi.sendAudio(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    audio: String,
    caption: String? = null,
    parseMode: String? = null,
    captionEntities: List<MessageEntity>? = null,
    duration: Long? = null,
    performer: String? = null,
    title: String? = null,
    thumbnail: String? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> = sendAudio(
    businessConnectionId = businessConnectionId,
    chatId = chatId,
    messageThreadId = messageThreadId,
    directMessagesTopicId = directMessagesTopicId,
    audio = FormPart("audio", ChannelProvider { ByteReadChannel(audio) }),
    caption = caption,
    parseMode = parseMode,
    captionEntities = captionEntities,
    duration = duration,
    performer = performer,
    title = title,
    thumbnail = thumbnail?.let { FormPart("thumbnail", ChannelProvider { ByteReadChannel(it) }) },
    disableNotification = disableNotification,
    protectContent = protectContent,
    allowPaidBroadcast = allowPaidBroadcast,
    messageEffectId = messageEffectId,
    suggestedPostParameters = suggestedPostParameters,
    replyParameters = replyParameters,
    replyMarkup = replyMarkup
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

public suspend fun TelegramBotApi.sendDocument(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    document: FormPart<ChannelProvider>,
    thumbnail: FormPart<ChannelProvider>? = null,
    caption: String? = null,
    parseMode: String? = null,
    captionEntities: List<MessageEntity>? = null,
    disableContentTypeDetection: Boolean? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> {
    val formData = MultiPartFormDataContent(formData {
        businessConnectionId?.let {
            append("business_connection_id", it)
        }
        append("chat_id", chatId)
        messageThreadId?.let {
            append("message_thread_id", it.toString())
        }
        directMessagesTopicId?.let {
            append("direct_messages_topic_id", it.toString())
        }
        append(document)
        thumbnail?.let { append(it) }
        caption?.let {
            append("caption", it)
        }
        parseMode?.let {
            append("parse_mode", it)
        }
        captionEntities?.let {
            append("caption_entities", Json.encodeToString(it))
        }
        disableContentTypeDetection?.let {
            append("disable_content_type_detection", it.toString())
        }
        disableNotification?.let {
            append("disable_notification", it.toString())
        }
        protectContent?.let {
            append("protect_content", it.toString())
        }
        allowPaidBroadcast?.let {
            append("allow_paid_broadcast", it.toString())
        }
        messageEffectId?.let {
            append("message_effect_id", it)
        }
        suggestedPostParameters?.let {
            append("suggested_post_parameters", Json.encodeToString(it))
        }
        replyParameters?.let {
            append("reply_parameters", Json.encodeToString(it))
        }
        replyMarkup?.let {
            append("reply_markup", Json.encodeToString(it))
        }
    })
    return sendDocument(formData)
}

public suspend fun TelegramBotApi.sendDocument(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    document: String,
    thumbnail: String? = null,
    caption: String? = null,
    parseMode: String? = null,
    captionEntities: List<MessageEntity>? = null,
    disableContentTypeDetection: Boolean? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> = sendDocument(
    businessConnectionId = businessConnectionId,
    chatId = chatId,
    messageThreadId = messageThreadId,
    directMessagesTopicId = directMessagesTopicId,
    document = FormPart("document", ChannelProvider { ByteReadChannel(document) }),
    thumbnail = thumbnail?.let { FormPart("thumbnail", ChannelProvider { ByteReadChannel(it) }) },
    caption = caption,
    parseMode = parseMode,
    captionEntities = captionEntities,
    disableContentTypeDetection = disableContentTypeDetection,
    disableNotification = disableNotification,
    protectContent = protectContent,
    allowPaidBroadcast = allowPaidBroadcast,
    messageEffectId = messageEffectId,
    suggestedPostParameters = suggestedPostParameters,
    replyParameters = replyParameters,
    replyMarkup = replyMarkup
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

public suspend fun TelegramBotApi.sendVideo(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    video: FormPart<ChannelProvider>,
    duration: Long? = null,
    width: Long? = null,
    height: Long? = null,
    thumbnail: FormPart<ChannelProvider>? = null,
    cover: FormPart<ChannelProvider>? = null,
    startTimestamp: Long? = null,
    caption: String? = null,
    parseMode: String? = null,
    captionEntities: List<MessageEntity>? = null,
    showCaptionAboveMedia: Boolean? = null,
    hasSpoiler: Boolean? = null,
    supportsStreaming: Boolean? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> {
    val formData = MultiPartFormDataContent(formData {
        businessConnectionId?.let {
            append("business_connection_id", it)
        }
        append("chat_id", chatId)
        messageThreadId?.let {
            append("message_thread_id", it.toString())
        }
        directMessagesTopicId?.let {
            append("direct_messages_topic_id", it.toString())
        }
        append(video)
        duration?.let {
            append("duration", it.toString())
        }
        width?.let {
            append("width", it.toString())
        }
        height?.let {
            append("height", it.toString())
        }
        thumbnail?.let { append(it) }
        cover?.let { append(it) }
        startTimestamp?.let {
            append("start_timestamp", it.toString())
        }
        caption?.let {
            append("caption", it)
        }
        parseMode?.let {
            append("parse_mode", it)
        }
        captionEntities?.let {
            append("caption_entities", Json.encodeToString(it))
        }
        showCaptionAboveMedia?.let {
            append("show_caption_above_media", it.toString())
        }
        hasSpoiler?.let {
            append("has_spoiler", it.toString())
        }
        supportsStreaming?.let {
            append("supports_streaming", it.toString())
        }
        disableNotification?.let {
            append("disable_notification", it.toString())
        }
        protectContent?.let {
            append("protect_content", it.toString())
        }
        allowPaidBroadcast?.let {
            append("allow_paid_broadcast", it.toString())
        }
        messageEffectId?.let {
            append("message_effect_id", it)
        }
        suggestedPostParameters?.let {
            append("suggested_post_parameters", Json.encodeToString(it))
        }
        replyParameters?.let {
            append("reply_parameters", Json.encodeToString(it))
        }
        replyMarkup?.let {
            append("reply_markup", Json.encodeToString(it))
        }
    })
    return sendVideo(formData)
}

public suspend fun TelegramBotApi.sendVideo(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    video: String,
    duration: Long? = null,
    width: Long? = null,
    height: Long? = null,
    thumbnail: String? = null,
    cover: String? = null,
    startTimestamp: Long? = null,
    caption: String? = null,
    parseMode: String? = null,
    captionEntities: List<MessageEntity>? = null,
    showCaptionAboveMedia: Boolean? = null,
    hasSpoiler: Boolean? = null,
    supportsStreaming: Boolean? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> = sendVideo(
    businessConnectionId = businessConnectionId,
    chatId = chatId,
    messageThreadId = messageThreadId,
    directMessagesTopicId = directMessagesTopicId,
    video = FormPart("video", ChannelProvider { ByteReadChannel(video) }),
    duration = duration,
    width = width,
    height = height,
    thumbnail = thumbnail?.let { FormPart("thumbnail", ChannelProvider { ByteReadChannel(it) }) },
    cover = cover?.let { FormPart("cover", ChannelProvider { ByteReadChannel(it) }) },
    startTimestamp = startTimestamp,
    caption = caption,
    parseMode = parseMode,
    captionEntities = captionEntities,
    showCaptionAboveMedia = showCaptionAboveMedia,
    hasSpoiler = hasSpoiler,
    supportsStreaming = supportsStreaming,
    disableNotification = disableNotification,
    protectContent = protectContent,
    allowPaidBroadcast = allowPaidBroadcast,
    messageEffectId = messageEffectId,
    suggestedPostParameters = suggestedPostParameters,
    replyParameters = replyParameters,
    replyMarkup = replyMarkup
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

public suspend fun TelegramBotApi.sendAnimation(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    animation: FormPart<ChannelProvider>,
    duration: Long? = null,
    width: Long? = null,
    height: Long? = null,
    thumbnail: FormPart<ChannelProvider>? = null,
    caption: String? = null,
    parseMode: String? = null,
    captionEntities: List<MessageEntity>? = null,
    showCaptionAboveMedia: Boolean? = null,
    hasSpoiler: Boolean? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> {
    val formData = MultiPartFormDataContent(formData {
        businessConnectionId?.let {
            append("business_connection_id", it)
        }
        append("chat_id", chatId)
        messageThreadId?.let {
            append("message_thread_id", it.toString())
        }
        directMessagesTopicId?.let {
            append("direct_messages_topic_id", it.toString())
        }
        append(animation)
        duration?.let {
            append("duration", it.toString())
        }
        width?.let {
            append("width", it.toString())
        }
        height?.let {
            append("height", it.toString())
        }
        thumbnail?.let { append(it) }
        caption?.let {
            append("caption", it)
        }
        parseMode?.let {
            append("parse_mode", it)
        }
        captionEntities?.let {
            append("caption_entities", Json.encodeToString(it))
        }
        showCaptionAboveMedia?.let {
            append("show_caption_above_media", it.toString())
        }
        hasSpoiler?.let {
            append("has_spoiler", it.toString())
        }
        disableNotification?.let {
            append("disable_notification", it.toString())
        }
        protectContent?.let {
            append("protect_content", it.toString())
        }
        allowPaidBroadcast?.let {
            append("allow_paid_broadcast", it.toString())
        }
        messageEffectId?.let {
            append("message_effect_id", it)
        }
        suggestedPostParameters?.let {
            append("suggested_post_parameters", Json.encodeToString(it))
        }
        replyParameters?.let {
            append("reply_parameters", Json.encodeToString(it))
        }
        replyMarkup?.let {
            append("reply_markup", Json.encodeToString(it))
        }
    })
    return sendAnimation(formData)
}

public suspend fun TelegramBotApi.sendAnimation(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    animation: String,
    duration: Long? = null,
    width: Long? = null,
    height: Long? = null,
    thumbnail: String? = null,
    caption: String? = null,
    parseMode: String? = null,
    captionEntities: List<MessageEntity>? = null,
    showCaptionAboveMedia: Boolean? = null,
    hasSpoiler: Boolean? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> = sendAnimation(
    businessConnectionId = businessConnectionId,
    chatId = chatId,
    messageThreadId = messageThreadId,
    directMessagesTopicId = directMessagesTopicId,
    animation = FormPart("animation", ChannelProvider { ByteReadChannel(animation) }),
    duration = duration,
    width = width,
    height = height,
    thumbnail = thumbnail?.let { FormPart("thumbnail", ChannelProvider { ByteReadChannel(it) }) },
    caption = caption,
    parseMode = parseMode,
    captionEntities = captionEntities,
    showCaptionAboveMedia = showCaptionAboveMedia,
    hasSpoiler = hasSpoiler,
    disableNotification = disableNotification,
    protectContent = protectContent,
    allowPaidBroadcast = allowPaidBroadcast,
    messageEffectId = messageEffectId,
    suggestedPostParameters = suggestedPostParameters,
    replyParameters = replyParameters,
    replyMarkup = replyMarkup
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

public suspend fun TelegramBotApi.sendVoice(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    voice: FormPart<ChannelProvider>,
    caption: String? = null,
    parseMode: String? = null,
    captionEntities: List<MessageEntity>? = null,
    duration: Long? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> {
    val formData = MultiPartFormDataContent(formData {
        businessConnectionId?.let {
            append("business_connection_id", it)
        }
        append("chat_id", chatId)
        messageThreadId?.let {
            append("message_thread_id", it.toString())
        }
        directMessagesTopicId?.let {
            append("direct_messages_topic_id", it.toString())
        }
        append(voice)
        caption?.let {
            append("caption", it)
        }
        parseMode?.let {
            append("parse_mode", it)
        }
        captionEntities?.let {
            append("caption_entities", Json.encodeToString(it))
        }
        duration?.let {
            append("duration", it.toString())
        }
        disableNotification?.let {
            append("disable_notification", it.toString())
        }
        protectContent?.let {
            append("protect_content", it.toString())
        }
        allowPaidBroadcast?.let {
            append("allow_paid_broadcast", it.toString())
        }
        messageEffectId?.let {
            append("message_effect_id", it)
        }
        suggestedPostParameters?.let {
            append("suggested_post_parameters", Json.encodeToString(it))
        }
        replyParameters?.let {
            append("reply_parameters", Json.encodeToString(it))
        }
        replyMarkup?.let {
            append("reply_markup", Json.encodeToString(it))
        }
    })
    return sendVoice(formData)
}

public suspend fun TelegramBotApi.sendVoice(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    voice: String,
    caption: String? = null,
    parseMode: String? = null,
    captionEntities: List<MessageEntity>? = null,
    duration: Long? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> = sendVoice(
    businessConnectionId = businessConnectionId,
    chatId = chatId,
    messageThreadId = messageThreadId,
    directMessagesTopicId = directMessagesTopicId,
    voice = FormPart("voice", ChannelProvider { ByteReadChannel(voice) }),
    caption = caption,
    parseMode = parseMode,
    captionEntities = captionEntities,
    duration = duration,
    disableNotification = disableNotification,
    protectContent = protectContent,
    allowPaidBroadcast = allowPaidBroadcast,
    messageEffectId = messageEffectId,
    suggestedPostParameters = suggestedPostParameters,
    replyParameters = replyParameters,
    replyMarkup = replyMarkup
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

public suspend fun TelegramBotApi.sendVideoNote(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    videoNote: FormPart<ChannelProvider>,
    duration: Long? = null,
    length: Long? = null,
    thumbnail: FormPart<ChannelProvider>? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> {
    val formData = MultiPartFormDataContent(formData {
        businessConnectionId?.let {
            append("business_connection_id", it)
        }
        append("chat_id", chatId)
        messageThreadId?.let {
            append("message_thread_id", it.toString())
        }
        directMessagesTopicId?.let {
            append("direct_messages_topic_id", it.toString())
        }
        append(videoNote)
        duration?.let {
            append("duration", it.toString())
        }
        length?.let {
            append("length", it.toString())
        }
        thumbnail?.let { append(it) }
        disableNotification?.let {
            append("disable_notification", it.toString())
        }
        protectContent?.let {
            append("protect_content", it.toString())
        }
        allowPaidBroadcast?.let {
            append("allow_paid_broadcast", it.toString())
        }
        messageEffectId?.let {
            append("message_effect_id", it)
        }
        suggestedPostParameters?.let {
            append("suggested_post_parameters", Json.encodeToString(it))
        }
        replyParameters?.let {
            append("reply_parameters", Json.encodeToString(it))
        }
        replyMarkup?.let {
            append("reply_markup", Json.encodeToString(it))
        }
    })
    return sendVideoNote(formData)
}

public suspend fun TelegramBotApi.sendVideoNote(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    videoNote: String,
    duration: Long? = null,
    length: Long? = null,
    thumbnail: String? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> = sendVideoNote(
    businessConnectionId = businessConnectionId,
    chatId = chatId,
    messageThreadId = messageThreadId,
    directMessagesTopicId = directMessagesTopicId,
    videoNote = FormPart("video_note", ChannelProvider { ByteReadChannel(videoNote) }),
    duration = duration,
    length = length,
    thumbnail = thumbnail?.let { FormPart("thumbnail", ChannelProvider { ByteReadChannel(it) }) },
    disableNotification = disableNotification,
    protectContent = protectContent,
    allowPaidBroadcast = allowPaidBroadcast,
    messageEffectId = messageEffectId,
    suggestedPostParameters = suggestedPostParameters,
    replyParameters = replyParameters,
    replyMarkup = replyMarkup
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

public suspend fun TelegramBotApi.sendPaidMedia(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    starCount: Long,
    media: List<InputPaidMedia>,
    payload: String? = null,
    caption: String? = null,
    parseMode: String? = null,
    captionEntities: List<MessageEntity>? = null,
    showCaptionAboveMedia: Boolean? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
    attachments: List<FormPart<ChannelProvider>>? = null,
): TelegramResponse<Message> {
    val formData = MultiPartFormDataContent(formData {
        businessConnectionId?.let {
            append("business_connection_id", it)
        }
        append("chat_id", chatId)
        messageThreadId?.let {
            append("message_thread_id", it.toString())
        }
        directMessagesTopicId?.let {
            append("direct_messages_topic_id", it.toString())
        }
        append("star_count", starCount.toString())
        append("media", Json.encodeToString(media))
        payload?.let {
            append("payload", it)
        }
        caption?.let {
            append("caption", it)
        }
        parseMode?.let {
            append("parse_mode", it)
        }
        captionEntities?.let {
            append("caption_entities", Json.encodeToString(it))
        }
        showCaptionAboveMedia?.let {
            append("show_caption_above_media", it.toString())
        }
        disableNotification?.let {
            append("disable_notification", it.toString())
        }
        protectContent?.let {
            append("protect_content", it.toString())
        }
        allowPaidBroadcast?.let {
            append("allow_paid_broadcast", it.toString())
        }
        suggestedPostParameters?.let {
            append("suggested_post_parameters", Json.encodeToString(it))
        }
        replyParameters?.let {
            append("reply_parameters", Json.encodeToString(it))
        }
        replyMarkup?.let {
            append("reply_markup", Json.encodeToString(it))
        }
        attachments?.forEach { append(it) }
    })
    return sendPaidMedia(formData)
}

public suspend fun TelegramBotApi.sendPaidMedia(form: SendPaidMediaForm): TelegramResponse<Message> = sendPaidMedia(
    businessConnectionId = form.businessConnectionId,
    chatId = form.chatId,
    messageThreadId = form.messageThreadId,
    directMessagesTopicId = form.directMessagesTopicId,
    starCount = form.starCount,
    media = form.media,
    payload = form.payload,
    caption = form.caption,
    parseMode = form.parseMode,
    captionEntities = form.captionEntities,
    showCaptionAboveMedia = form.showCaptionAboveMedia,
    disableNotification = form.disableNotification,
    protectContent = form.protectContent,
    allowPaidBroadcast = form.allowPaidBroadcast,
    suggestedPostParameters = form.suggestedPostParameters,
    replyParameters = form.replyParameters,
    replyMarkup = form.replyMarkup,
    attachments = form.attachments
)

public suspend fun TelegramBotApi.sendMediaGroup(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    media: List<InputMedia>,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    replyParameters: ReplyParameters? = null,
    attachments: List<FormPart<ChannelProvider>>? = null,
): TelegramResponse<List<Message>> {
    val formData = MultiPartFormDataContent(formData {
        businessConnectionId?.let {
            append("business_connection_id", it)
        }
        append("chat_id", chatId)
        messageThreadId?.let {
            append("message_thread_id", it.toString())
        }
        directMessagesTopicId?.let {
            append("direct_messages_topic_id", it.toString())
        }
        append("media", Json.encodeToString(media))
        disableNotification?.let {
            append("disable_notification", it.toString())
        }
        protectContent?.let {
            append("protect_content", it.toString())
        }
        allowPaidBroadcast?.let {
            append("allow_paid_broadcast", it.toString())
        }
        messageEffectId?.let {
            append("message_effect_id", it)
        }
        replyParameters?.let {
            append("reply_parameters", Json.encodeToString(it))
        }
        attachments?.forEach { append(it) }
    })
    return sendMediaGroup(formData)
}

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
    replyParameters = form.replyParameters,
    attachments = form.attachments
)

public suspend fun TelegramBotApi.setChatPhoto(chatId: String, photo: FormPart<ChannelProvider>): TelegramResponse<Boolean> {
    val formData = MultiPartFormDataContent(formData {
        append("chat_id", chatId)
        append(photo)
    })
    return setChatPhoto(formData)
}

public suspend fun TelegramBotApi.setChatPhoto(chatId: String, photo: String): TelegramResponse<Boolean> = setChatPhoto(
    chatId = chatId,
    photo = FormPart("photo", ChannelProvider { ByteReadChannel(photo) })
)

public suspend fun TelegramBotApi.setChatPhoto(form: SetChatPhotoForm): TelegramResponse<Boolean> = setChatPhoto(
    chatId = form.chatId,
    photo = form.photo
)

public suspend fun TelegramBotApi.setMyProfilePhoto(photo: InputProfilePhoto, attachments: List<FormPart<ChannelProvider>>? = null): TelegramResponse<Boolean> {
    val formData = MultiPartFormDataContent(formData {
        append("photo", Json.encodeToString(photo))
        attachments?.forEach { append(it) }
    })
    return setMyProfilePhoto(formData)
}

public suspend fun TelegramBotApi.setMyProfilePhoto(form: SetMyProfilePhotoForm): TelegramResponse<Boolean> = setMyProfilePhoto(
    photo = form.photo,
    attachments = form.attachments
)

public suspend fun TelegramBotApi.setBusinessAccountProfilePhoto(
    businessConnectionId: String,
    photo: InputProfilePhoto,
    isPublic: Boolean? = null,
    attachments: List<FormPart<ChannelProvider>>? = null,
): TelegramResponse<Boolean> {
    val formData = MultiPartFormDataContent(formData {
        append("business_connection_id", businessConnectionId)
        append("photo", Json.encodeToString(photo))
        isPublic?.let {
            append("is_public", it.toString())
        }
        attachments?.forEach { append(it) }
    })
    return setBusinessAccountProfilePhoto(formData)
}

public suspend fun TelegramBotApi.setBusinessAccountProfilePhoto(form: SetBusinessAccountProfilePhotoForm): TelegramResponse<Boolean> = setBusinessAccountProfilePhoto(
    businessConnectionId = form.businessConnectionId,
    photo = form.photo,
    isPublic = form.isPublic,
    attachments = form.attachments
)

public suspend fun TelegramBotApi.postStory(
    businessConnectionId: String,
    content: InputStoryContent,
    activePeriod: Long,
    caption: String? = null,
    parseMode: String? = null,
    captionEntities: List<MessageEntity>? = null,
    areas: List<StoryArea>? = null,
    postToChatPage: Boolean? = null,
    protectContent: Boolean? = null,
    attachments: List<FormPart<ChannelProvider>>? = null,
): TelegramResponse<Story> {
    val formData = MultiPartFormDataContent(formData {
        append("business_connection_id", businessConnectionId)
        append("content", Json.encodeToString(content))
        append("active_period", activePeriod.toString())
        caption?.let {
            append("caption", it)
        }
        parseMode?.let {
            append("parse_mode", it)
        }
        captionEntities?.let {
            append("caption_entities", Json.encodeToString(it))
        }
        areas?.let {
            append("areas", Json.encodeToString(it))
        }
        postToChatPage?.let {
            append("post_to_chat_page", it.toString())
        }
        protectContent?.let {
            append("protect_content", it.toString())
        }
        attachments?.forEach { append(it) }
    })
    return postStory(formData)
}

public suspend fun TelegramBotApi.postStory(form: PostStoryForm): TelegramResponse<Story> = postStory(
    businessConnectionId = form.businessConnectionId,
    content = form.content,
    activePeriod = form.activePeriod,
    caption = form.caption,
    parseMode = form.parseMode,
    captionEntities = form.captionEntities,
    areas = form.areas,
    postToChatPage = form.postToChatPage,
    protectContent = form.protectContent,
    attachments = form.attachments
)

public suspend fun TelegramBotApi.editStory(
    businessConnectionId: String,
    storyId: Long,
    content: InputStoryContent,
    caption: String? = null,
    parseMode: String? = null,
    captionEntities: List<MessageEntity>? = null,
    areas: List<StoryArea>? = null,
    attachments: List<FormPart<ChannelProvider>>? = null,
): TelegramResponse<Story> {
    val formData = MultiPartFormDataContent(formData {
        append("business_connection_id", businessConnectionId)
        append("story_id", storyId.toString())
        append("content", Json.encodeToString(content))
        caption?.let {
            append("caption", it)
        }
        parseMode?.let {
            append("parse_mode", it)
        }
        captionEntities?.let {
            append("caption_entities", Json.encodeToString(it))
        }
        areas?.let {
            append("areas", Json.encodeToString(it))
        }
        attachments?.forEach { append(it) }
    })
    return editStory(formData)
}

public suspend fun TelegramBotApi.editStory(form: EditStoryForm): TelegramResponse<Story> = editStory(
    businessConnectionId = form.businessConnectionId,
    storyId = form.storyId,
    content = form.content,
    caption = form.caption,
    parseMode = form.parseMode,
    captionEntities = form.captionEntities,
    areas = form.areas,
    attachments = form.attachments
)

public suspend fun TelegramBotApi.editMessageMedia(
    businessConnectionId: String? = null,
    chatId: String? = null,
    messageId: Long? = null,
    inlineMessageId: String? = null,
    media: InputMedia,
    replyMarkup: InlineKeyboardMarkup? = null,
    attachments: List<FormPart<ChannelProvider>>? = null,
): TelegramResponse<Boolean> {
    val formData = MultiPartFormDataContent(formData {
        businessConnectionId?.let {
            append("business_connection_id", it)
        }
        chatId?.let {
            append("chat_id", it)
        }
        messageId?.let {
            append("message_id", it.toString())
        }
        inlineMessageId?.let {
            append("inline_message_id", it)
        }
        append("media", Json.encodeToString(media))
        replyMarkup?.let {
            append("reply_markup", Json.encodeToString(it))
        }
        attachments?.forEach { append(it) }
    })
    return editMessageMedia(formData)
}

public suspend fun TelegramBotApi.editMessageMedia(form: EditMessageMediaForm): TelegramResponse<Boolean> = editMessageMedia(
    businessConnectionId = form.businessConnectionId,
    chatId = form.chatId,
    messageId = form.messageId,
    inlineMessageId = form.inlineMessageId,
    media = form.media,
    replyMarkup = form.replyMarkup,
    attachments = form.attachments
)

public suspend fun TelegramBotApi.sendSticker(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    sticker: FormPart<ChannelProvider>,
    emoji: String? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> {
    val formData = MultiPartFormDataContent(formData {
        businessConnectionId?.let {
            append("business_connection_id", it)
        }
        append("chat_id", chatId)
        messageThreadId?.let {
            append("message_thread_id", it.toString())
        }
        directMessagesTopicId?.let {
            append("direct_messages_topic_id", it.toString())
        }
        append(sticker)
        emoji?.let {
            append("emoji", it)
        }
        disableNotification?.let {
            append("disable_notification", it.toString())
        }
        protectContent?.let {
            append("protect_content", it.toString())
        }
        allowPaidBroadcast?.let {
            append("allow_paid_broadcast", it.toString())
        }
        messageEffectId?.let {
            append("message_effect_id", it)
        }
        suggestedPostParameters?.let {
            append("suggested_post_parameters", Json.encodeToString(it))
        }
        replyParameters?.let {
            append("reply_parameters", Json.encodeToString(it))
        }
        replyMarkup?.let {
            append("reply_markup", Json.encodeToString(it))
        }
    })
    return sendSticker(formData)
}

public suspend fun TelegramBotApi.sendSticker(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    sticker: String,
    emoji: String? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> = sendSticker(
    businessConnectionId = businessConnectionId,
    chatId = chatId,
    messageThreadId = messageThreadId,
    directMessagesTopicId = directMessagesTopicId,
    sticker = FormPart("sticker", ChannelProvider { ByteReadChannel(sticker) }),
    emoji = emoji,
    disableNotification = disableNotification,
    protectContent = protectContent,
    allowPaidBroadcast = allowPaidBroadcast,
    messageEffectId = messageEffectId,
    suggestedPostParameters = suggestedPostParameters,
    replyParameters = replyParameters,
    replyMarkup = replyMarkup
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

public suspend fun TelegramBotApi.uploadStickerFile(
    userId: Long,
    sticker: FormPart<ChannelProvider>,
    stickerFormat: String,
): TelegramResponse<File> {
    val formData = MultiPartFormDataContent(formData {
        append("user_id", userId.toString())
        append(sticker)
        append("sticker_format", stickerFormat)
    })
    return uploadStickerFile(formData)
}

public suspend fun TelegramBotApi.uploadStickerFile(
    userId: Long,
    sticker: String,
    stickerFormat: String,
): TelegramResponse<File> = uploadStickerFile(
    userId = userId,
    sticker = FormPart("sticker", ChannelProvider { ByteReadChannel(sticker) }),
    stickerFormat = stickerFormat
)

public suspend fun TelegramBotApi.uploadStickerFile(form: UploadStickerFileForm): TelegramResponse<File> = uploadStickerFile(
    userId = form.userId,
    sticker = form.sticker,
    stickerFormat = form.stickerFormat
)

public suspend fun TelegramBotApi.createNewStickerSet(
    userId: Long,
    name: String,
    title: String,
    stickers: List<InputSticker>,
    stickerType: String? = null,
    needsRepainting: Boolean? = null,
    attachments: List<FormPart<ChannelProvider>>? = null,
): TelegramResponse<Boolean> {
    val formData = MultiPartFormDataContent(formData {
        append("user_id", userId.toString())
        append("name", name)
        append("title", title)
        append("stickers", Json.encodeToString(stickers))
        stickerType?.let {
            append("sticker_type", it)
        }
        needsRepainting?.let {
            append("needs_repainting", it.toString())
        }
        attachments?.forEach { append(it) }
    })
    return createNewStickerSet(formData)
}

public suspend fun TelegramBotApi.createNewStickerSet(form: CreateNewStickerSetForm): TelegramResponse<Boolean> = createNewStickerSet(
    userId = form.userId,
    name = form.name,
    title = form.title,
    stickers = form.stickers,
    stickerType = form.stickerType,
    needsRepainting = form.needsRepainting,
    attachments = form.attachments
)

public suspend fun TelegramBotApi.addStickerToSet(
    userId: Long,
    name: String,
    sticker: InputSticker,
    attachments: List<FormPart<ChannelProvider>>? = null,
): TelegramResponse<Boolean> {
    val formData = MultiPartFormDataContent(formData {
        append("user_id", userId.toString())
        append("name", name)
        append("sticker", Json.encodeToString(sticker))
        attachments?.forEach { append(it) }
    })
    return addStickerToSet(formData)
}

public suspend fun TelegramBotApi.addStickerToSet(form: AddStickerToSetForm): TelegramResponse<Boolean> = addStickerToSet(
    userId = form.userId,
    name = form.name,
    sticker = form.sticker,
    attachments = form.attachments
)

public suspend fun TelegramBotApi.replaceStickerInSet(
    userId: Long,
    name: String,
    oldSticker: String,
    sticker: InputSticker,
    attachments: List<FormPart<ChannelProvider>>? = null,
): TelegramResponse<Boolean> {
    val formData = MultiPartFormDataContent(formData {
        append("user_id", userId.toString())
        append("name", name)
        append("old_sticker", oldSticker)
        append("sticker", Json.encodeToString(sticker))
        attachments?.forEach { append(it) }
    })
    return replaceStickerInSet(formData)
}

public suspend fun TelegramBotApi.replaceStickerInSet(form: ReplaceStickerInSetForm): TelegramResponse<Boolean> = replaceStickerInSet(
    userId = form.userId,
    name = form.name,
    oldSticker = form.oldSticker,
    sticker = form.sticker,
    attachments = form.attachments
)

public suspend fun TelegramBotApi.setStickerSetThumbnail(
    name: String,
    userId: Long,
    thumbnail: FormPart<ChannelProvider>? = null,
    format: String,
): TelegramResponse<Boolean> {
    val formData = MultiPartFormDataContent(formData {
        append("name", name)
        append("user_id", userId.toString())
        thumbnail?.let { append(it) }
        append("format", format)
    })
    return setStickerSetThumbnail(formData)
}

public suspend fun TelegramBotApi.setStickerSetThumbnail(
    name: String,
    userId: Long,
    thumbnail: String? = null,
    format: String,
): TelegramResponse<Boolean> = setStickerSetThumbnail(
    name = name,
    userId = userId,
    thumbnail = thumbnail?.let { FormPart("thumbnail", ChannelProvider { ByteReadChannel(it) }) },
    format = format
)

public suspend fun TelegramBotApi.setStickerSetThumbnail(form: SetStickerSetThumbnailForm): TelegramResponse<Boolean> = setStickerSetThumbnail(
    name = form.name,
    userId = form.userId,
    thumbnail = form.thumbnail,
    format = form.format
)
