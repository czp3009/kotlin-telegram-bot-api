// Auto-generated from Swagger specificationDo not modify this file manuallyExtension functions for multipart operations
@file:Suppress(
    "RedundantVisibilityModifier",
    "unused",
    "DuplicatedCode",
)

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
import com.hiczp.telegram.bot.api.model.InlineKeyboardMarkup
import com.hiczp.telegram.bot.api.model.InputMedia
import com.hiczp.telegram.bot.api.model.Message
import com.hiczp.telegram.bot.api.model.MessageEntity
import com.hiczp.telegram.bot.api.model.ReplyMarkup
import com.hiczp.telegram.bot.api.model.ReplyParameters
import com.hiczp.telegram.bot.api.model.SuggestedPostParameters
import com.hiczp.telegram.bot.api.type.TelegramResponse
import io.ktor.client.request.forms.FormPart
import io.ktor.client.request.forms.InputProvider
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlinx.serialization.json.Json

public suspend fun TelegramBotApi.setWebhook(
    url: String,
    certificate: InputProvider? = null,
    ipAddress: String? = null,
    maxConnections: Long? = null,
    allowedUpdates: List<String>? = null,
    dropPendingUpdates: Boolean? = null,
    secretToken: String? = null,
): TelegramResponse<Boolean> {
    val formData = MultiPartFormDataContent(formData {
        append("url", url)
        certificate?.let { append(FormPart("certificate", it)) }
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
    photo: InputProvider,
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
        append(FormPart("photo", photo))
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
    audio: InputProvider,
    caption: String? = null,
    parseMode: String? = null,
    captionEntities: List<MessageEntity>? = null,
    duration: Long? = null,
    performer: String? = null,
    title: String? = null,
    thumbnail: InputProvider? = null,
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
        append(FormPart("audio", audio))
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
        thumbnail?.let { append(FormPart("thumbnail", it)) }
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
    document: InputProvider,
    thumbnail: InputProvider? = null,
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
        append(FormPart("document", document))
        thumbnail?.let { append(FormPart("thumbnail", it)) }
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
    video: InputProvider,
    duration: Long? = null,
    width: Long? = null,
    height: Long? = null,
    thumbnail: InputProvider? = null,
    cover: InputProvider? = null,
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
        append(FormPart("video", video))
        duration?.let {
            append("duration", it.toString())
        }
        width?.let {
            append("width", it.toString())
        }
        height?.let {
            append("height", it.toString())
        }
        thumbnail?.let { append(FormPart("thumbnail", it)) }
        cover?.let { append(FormPart("cover", it)) }
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
    animation: InputProvider,
    duration: Long? = null,
    width: Long? = null,
    height: Long? = null,
    thumbnail: InputProvider? = null,
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
        append(FormPart("animation", animation))
        duration?.let {
            append("duration", it.toString())
        }
        width?.let {
            append("width", it.toString())
        }
        height?.let {
            append("height", it.toString())
        }
        thumbnail?.let { append(FormPart("thumbnail", it)) }
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
    voice: InputProvider,
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
        append(FormPart("voice", voice))
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
    videoNote: InputProvider,
    duration: Long? = null,
    length: Long? = null,
    thumbnail: InputProvider? = null,
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
        append(FormPart("video_note", videoNote))
        duration?.let {
            append("duration", it.toString())
        }
        length?.let {
            append("length", it.toString())
        }
        thumbnail?.let { append(FormPart("thumbnail", it)) }
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
    replyParameters = form.replyParameters
)

public suspend fun TelegramBotApi.setChatPhoto(chatId: String, photo: InputProvider): TelegramResponse<Boolean> {
    val formData = MultiPartFormDataContent(formData {
        append("chat_id", chatId)
        append(FormPart("photo", photo))
    })
    return setChatPhoto(formData)
}

public suspend fun TelegramBotApi.setChatPhoto(form: SetChatPhotoForm): TelegramResponse<Boolean> = setChatPhoto(
    chatId = form.chatId,
    photo = form.photo
)

public suspend fun TelegramBotApi.editMessageMedia(
    businessConnectionId: String? = null,
    chatId: String? = null,
    messageId: Long? = null,
    inlineMessageId: String? = null,
    media: InputMedia,
    replyMarkup: InlineKeyboardMarkup? = null,
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
    })
    return editMessageMedia(formData)
}

public suspend fun TelegramBotApi.editMessageMedia(form: EditMessageMediaForm): TelegramResponse<Boolean> = editMessageMedia(
    businessConnectionId = form.businessConnectionId,
    chatId = form.chatId,
    messageId = form.messageId,
    inlineMessageId = form.inlineMessageId,
    media = form.media,
    replyMarkup = form.replyMarkup
)

public suspend fun TelegramBotApi.sendSticker(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    sticker: InputProvider,
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
        append(FormPart("sticker", sticker))
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
    sticker: InputProvider,
    stickerFormat: String,
): TelegramResponse<File> {
    val formData = MultiPartFormDataContent(formData {
        append("user_id", userId.toString())
        append(FormPart("sticker", sticker))
        append("sticker_format", stickerFormat)
    })
    return uploadStickerFile(formData)
}

public suspend fun TelegramBotApi.uploadStickerFile(form: UploadStickerFileForm): TelegramResponse<File> = uploadStickerFile(
    userId = form.userId,
    sticker = form.sticker,
    stickerFormat = form.stickerFormat
)

public suspend fun TelegramBotApi.setStickerSetThumbnail(
    name: String,
    userId: Long,
    thumbnail: InputProvider? = null,
    format: String,
): TelegramResponse<Boolean> {
    val formData = MultiPartFormDataContent(formData {
        append("name", name)
        append("user_id", userId.toString())
        thumbnail?.let { append(FormPart("thumbnail", it)) }
        append("format", format)
    })
    return setStickerSetThumbnail(formData)
}

public suspend fun TelegramBotApi.setStickerSetThumbnail(form: SetStickerSetThumbnailForm): TelegramResponse<Boolean> = setStickerSetThumbnail(
    name = form.name,
    userId = form.userId,
    thumbnail = form.thumbnail,
    format = form.format
)
