// Auto-generated from Swagger specification, do not modify this file manuallyExtension functions for Telegram Bot API multipart operations.
//
// This file provides two types of convenient functions for operations that require
// multipart form data with file uploads:
//
// 1. **Scatter functions**: Accept individual parameters as named arguments
//    Example: `api.sendPhoto(chatId = 123, photo = inputFile)`
//
// 2. **Form functions**: Accept pre-constructed form wrapper classes
//    Example: `api.sendPhoto(form = SendPhotoForm(...))`
@file:Suppress(
    "RedundantVisibilityModifier",
    "unused",
    "DuplicatedCode",
)

package com.hiczp.telegram.bot.protocol.form

import com.hiczp.telegram.bot.protocol.TelegramBotApi
import com.hiczp.telegram.bot.protocol.model.File
import com.hiczp.telegram.bot.protocol.model.InlineKeyboardMarkup
import com.hiczp.telegram.bot.protocol.model.InputMedia
import com.hiczp.telegram.bot.protocol.model.InputPaidMedia
import com.hiczp.telegram.bot.protocol.model.InputProfilePhoto
import com.hiczp.telegram.bot.protocol.model.InputSticker
import com.hiczp.telegram.bot.protocol.model.InputStoryContent
import com.hiczp.telegram.bot.protocol.model.Message
import com.hiczp.telegram.bot.protocol.model.MessageEntity
import com.hiczp.telegram.bot.protocol.model.ReplyMarkup
import com.hiczp.telegram.bot.protocol.model.ReplyParameters
import com.hiczp.telegram.bot.protocol.model.Story
import com.hiczp.telegram.bot.protocol.model.StoryArea
import com.hiczp.telegram.bot.protocol.model.SuggestedPostParameters
import com.hiczp.telegram.bot.protocol.type.InputFile
import com.hiczp.telegram.bot.protocol.type.TelegramResponse
import com.hiczp.telegram.bot.protocol.type.toFormPart
import io.ktor.client.request.forms.ChannelProvider
import io.ktor.client.request.forms.FormPart
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlinx.serialization.json.Json

/**
 * Use this method to specify a URL and receive incoming updates via an outgoing webhook. Whenever there is an update for the bot, we will send an HTTPS POST request to the specified URL, containing a JSON-serialized Update. In case of an unsuccessful request (a request with response HTTP status code different from 2XY), we will repeat the request and give up after a reasonable amount of attempts. Returns True on success.
 * If you'd like to make sure that the webhook was set by you, you can specify secret data in the parameter secret_token. If specified, the request will contain a header “X-Telegram-Bot-Api-Secret-Token” with the secret token as content.
 * Notes 1. You will not be able to receive updates using getUpdates for as long as an outgoing webhook is set up. 2. To use a self-signed certificate, you need to upload your public key certificate using certificate parameter. Please upload as InputFile, sending a String will not work. 3. Ports currently supported for webhooks: 443, 80, 88, 8443. If you're having any trouble setting up webhooks, please check out this amazing guide to webhooks.
 *
 * @param url HTTPS URL to send updates to. Use an empty string to remove webhook integration
 * @param certificate Upload your public key certificate so that the root certificate in use can be checked. See our [self-signed guide](https://core.telegram.org/bots/self-signed) for details.
 * @param ipAddress The fixed IP address which will be used to send webhook requests instead of the IP address resolved through DNS
 * @param maxConnections The maximum allowed number of simultaneous HTTPS connections to the webhook for update delivery, 1-100. Defaults to *40*. Use lower values to limit the load on your bot's server, and higher values to increase your bot's throughput.
 * @param allowedUpdates A JSON-serialized list of the update types you want your bot to receive. For example, specify `["message", "edited_channel_post", "callback_query"]` to only receive updates of these types. See [Update](https://core.telegram.org/bots/api#update) for a complete list of available update types. Specify an empty list to receive all update types except *chat_member*, *message_reaction*, and *message_reaction_count* (default). If not specified, the previous setting will be used.Please note that this parameter doesn't affect updates created before the call to the setWebhook, so unwanted updates may be received for a short period of time.
 * @param dropPendingUpdates Pass *True* to drop all pending updates
 * @param secretToken A secret token to be sent in a header “X-Telegram-Bot-Api-Secret-Token” in every webhook request, 1-256 characters. Only characters `A-Z`, `a-z`, `0-9`, `_` and `-` are allowed. The header is useful to ensure that the request comes from a webhook set by you.
 */
public suspend fun TelegramBotApi.setWebhook(
    url: String,
    certificate: InputFile? = null,
    ipAddress: String? = null,
    maxConnections: Long? = null,
    allowedUpdates: List<String>? = null,
    dropPendingUpdates: Boolean? = null,
    secretToken: String? = null,
): TelegramResponse<Boolean> {
    val formData = MultiPartFormDataContent(formData {
        append("url", url)
        certificate?.let { append(it.toFormPart("certificate")) }
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

/**
 * Use this method to specify a URL and receive incoming updates via an outgoing webhook. Whenever there is an update for the bot, we will send an HTTPS POST request to the specified URL, containing a JSON-serialized Update. In case of an unsuccessful request (a request with response HTTP status code different from 2XY), we will repeat the request and give up after a reasonable amount of attempts. Returns True on success.
 * If you'd like to make sure that the webhook was set by you, you can specify secret data in the parameter secret_token. If specified, the request will contain a header “X-Telegram-Bot-Api-Secret-Token” with the secret token as content.
 * Notes 1. You will not be able to receive updates using getUpdates for as long as an outgoing webhook is set up. 2. To use a self-signed certificate, you need to upload your public key certificate using certificate parameter. Please upload as InputFile, sending a String will not work. 3. Ports currently supported for webhooks: 443, 80, 88, 8443. If you're having any trouble setting up webhooks, please check out this amazing guide to webhooks.
 *
 * @param form Multipart form object for this operation
 */
public suspend fun TelegramBotApi.setWebhook(form: SetWebhookForm): TelegramResponse<Boolean> = setWebhook(
    url = form.url,
    certificate = form.certificate,
    ipAddress = form.ipAddress,
    maxConnections = form.maxConnections,
    allowedUpdates = form.allowedUpdates,
    dropPendingUpdates = form.dropPendingUpdates,
    secretToken = form.secretToken
)

/**
 * Use this method to send photos. On success, the sent Message is returned.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent; required if the message is sent to a direct messages chat
 * @param photo Photo to send. Pass a file_id as String to send a photo that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a photo from the Internet, or upload a new photo using multipart/form-data. The photo must be at most 10 MB in size. The photo's width and height must not exceed 10000 in total. Width and height ratio must be at most 20. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
 * @param caption Photo caption (may also be used when resending photos by *file_id*), 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the photo caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of *parse_mode*
 * @param showCaptionAboveMedia Pass *True*, if the caption must be shown above the message media
 * @param hasSpoiler Pass *True* if the photo needs to be covered with a spoiler animation
 * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass *True* to allow up to 1000 messages per second, ignoring [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once) for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param suggestedPostParameters A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is automatically declined.
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply keyboard or to force a reply from the user
 */
public suspend fun TelegramBotApi.sendPhoto(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    photo: InputFile,
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
        append(photo.toFormPart("photo"))
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

/**
 * Use this method to send photos. On success, the sent Message is returned.
 *
 * @param form Multipart form object for this operation
 */
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

/**
 * Use this method to send audio files, if you want Telegram clients to display them in the music player. Your audio must be in the .MP3 or .M4A format. On success, the sent Message is returned. Bots can currently send audio files of up to 50 MB in size, this limit may be changed in the future.
 * For sending voice messages, use the sendVoice method instead.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent; required if the message is sent to a direct messages chat
 * @param audio Audio file to send. Pass a file_id as String to send an audio file that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get an audio file from the Internet, or upload a new one using multipart/form-data. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
 * @param caption Audio caption, 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the audio caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of *parse_mode*
 * @param duration Duration of the audio in seconds
 * @param performer Performer
 * @param title Track name
 * @param thumbnail Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
 * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass *True* to allow up to 1000 messages per second, ignoring [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once) for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param suggestedPostParameters A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is automatically declined.
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply keyboard or to force a reply from the user
 */
public suspend fun TelegramBotApi.sendAudio(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    audio: InputFile,
    caption: String? = null,
    parseMode: String? = null,
    captionEntities: List<MessageEntity>? = null,
    duration: Long? = null,
    performer: String? = null,
    title: String? = null,
    thumbnail: InputFile? = null,
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
        append(audio.toFormPart("audio"))
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
        thumbnail?.let { append(it.toFormPart("thumbnail")) }
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

/**
 * Use this method to send audio files, if you want Telegram clients to display them in the music player. Your audio must be in the .MP3 or .M4A format. On success, the sent Message is returned. Bots can currently send audio files of up to 50 MB in size, this limit may be changed in the future.
 * For sending voice messages, use the sendVoice method instead.
 *
 * @param form Multipart form object for this operation
 */
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

/**
 * Use this method to send general files. On success, the sent Message is returned. Bots can currently send files of any type of up to 50 MB in size, this limit may be changed in the future.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent; required if the message is sent to a direct messages chat
 * @param document File to send. Pass a file_id as String to send a file that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a file from the Internet, or upload a new one using multipart/form-data. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
 * @param thumbnail Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
 * @param caption Document caption (may also be used when resending documents by *file_id*), 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the document caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of *parse_mode*
 * @param disableContentTypeDetection Disables automatic server-side content type detection for files uploaded using multipart/form-data
 * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass *True* to allow up to 1000 messages per second, ignoring [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once) for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param suggestedPostParameters A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is automatically declined.
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply keyboard or to force a reply from the user
 */
public suspend fun TelegramBotApi.sendDocument(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    document: InputFile,
    thumbnail: InputFile? = null,
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
        append(document.toFormPart("document"))
        thumbnail?.let { append(it.toFormPart("thumbnail")) }
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

/**
 * Use this method to send general files. On success, the sent Message is returned. Bots can currently send files of any type of up to 50 MB in size, this limit may be changed in the future.
 *
 * @param form Multipart form object for this operation
 */
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

/**
 * Use this method to send video files, Telegram clients support MPEG4 videos (other formats may be sent as Document). On success, the sent Message is returned. Bots can currently send video files of up to 50 MB in size, this limit may be changed in the future.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent; required if the message is sent to a direct messages chat
 * @param video Video to send. Pass a file_id as String to send a video that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a video from the Internet, or upload a new video using multipart/form-data. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
 * @param duration Duration of sent video in seconds
 * @param width Video width
 * @param height Video height
 * @param thumbnail Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
 * @param cover Cover for the video in the message. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
 * @param startTimestamp Start timestamp for the video in the message
 * @param caption Video caption (may also be used when resending videos by *file_id*), 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the video caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of *parse_mode*
 * @param showCaptionAboveMedia Pass *True*, if the caption must be shown above the message media
 * @param hasSpoiler Pass *True* if the video needs to be covered with a spoiler animation
 * @param supportsStreaming Pass *True* if the uploaded video is suitable for streaming
 * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass *True* to allow up to 1000 messages per second, ignoring [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once) for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param suggestedPostParameters A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is automatically declined.
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply keyboard or to force a reply from the user
 */
public suspend fun TelegramBotApi.sendVideo(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    video: InputFile,
    duration: Long? = null,
    width: Long? = null,
    height: Long? = null,
    thumbnail: InputFile? = null,
    cover: InputFile? = null,
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
        append(video.toFormPart("video"))
        duration?.let {
            append("duration", it.toString())
        }
        width?.let {
            append("width", it.toString())
        }
        height?.let {
            append("height", it.toString())
        }
        thumbnail?.let { append(it.toFormPart("thumbnail")) }
        cover?.let { append(it.toFormPart("cover")) }
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

/**
 * Use this method to send video files, Telegram clients support MPEG4 videos (other formats may be sent as Document). On success, the sent Message is returned. Bots can currently send video files of up to 50 MB in size, this limit may be changed in the future.
 *
 * @param form Multipart form object for this operation
 */
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

/**
 * Use this method to send animation files (GIF or H.264/MPEG-4 AVC video without sound). On success, the sent Message is returned. Bots can currently send animation files of up to 50 MB in size, this limit may be changed in the future.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent; required if the message is sent to a direct messages chat
 * @param animation Animation to send. Pass a file_id as String to send an animation that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get an animation from the Internet, or upload a new animation using multipart/form-data. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
 * @param duration Duration of sent animation in seconds
 * @param width Animation width
 * @param height Animation height
 * @param thumbnail Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
 * @param caption Animation caption (may also be used when resending animation by *file_id*), 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the animation caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of *parse_mode*
 * @param showCaptionAboveMedia Pass *True*, if the caption must be shown above the message media
 * @param hasSpoiler Pass *True* if the animation needs to be covered with a spoiler animation
 * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass *True* to allow up to 1000 messages per second, ignoring [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once) for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param suggestedPostParameters A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is automatically declined.
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply keyboard or to force a reply from the user
 */
public suspend fun TelegramBotApi.sendAnimation(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    animation: InputFile,
    duration: Long? = null,
    width: Long? = null,
    height: Long? = null,
    thumbnail: InputFile? = null,
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
        append(animation.toFormPart("animation"))
        duration?.let {
            append("duration", it.toString())
        }
        width?.let {
            append("width", it.toString())
        }
        height?.let {
            append("height", it.toString())
        }
        thumbnail?.let { append(it.toFormPart("thumbnail")) }
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

/**
 * Use this method to send animation files (GIF or H.264/MPEG-4 AVC video without sound). On success, the sent Message is returned. Bots can currently send animation files of up to 50 MB in size, this limit may be changed in the future.
 *
 * @param form Multipart form object for this operation
 */
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

/**
 * Use this method to send audio files, if you want Telegram clients to display the file as a playable voice message. For this to work, your audio must be in an .OGG file encoded with OPUS, or in .MP3 format, or in .M4A format (other formats may be sent as Audio or Document). On success, the sent Message is returned. Bots can currently send voice messages of up to 50 MB in size, this limit may be changed in the future.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent; required if the message is sent to a direct messages chat
 * @param voice Audio file to send. Pass a file_id as String to send a file that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a file from the Internet, or upload a new one using multipart/form-data. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
 * @param caption Voice message caption, 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the voice message caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of *parse_mode*
 * @param duration Duration of the voice message in seconds
 * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass *True* to allow up to 1000 messages per second, ignoring [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once) for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param suggestedPostParameters A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is automatically declined.
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply keyboard or to force a reply from the user
 */
public suspend fun TelegramBotApi.sendVoice(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    voice: InputFile,
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
        append(voice.toFormPart("voice"))
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

/**
 * Use this method to send audio files, if you want Telegram clients to display the file as a playable voice message. For this to work, your audio must be in an .OGG file encoded with OPUS, or in .MP3 format, or in .M4A format (other formats may be sent as Audio or Document). On success, the sent Message is returned. Bots can currently send voice messages of up to 50 MB in size, this limit may be changed in the future.
 *
 * @param form Multipart form object for this operation
 */
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

/**
 * As of v.4.0, Telegram clients support rounded square MPEG4 videos of up to 1 minute long. Use this method to send video messages. On success, the sent Message is returned.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent; required if the message is sent to a direct messages chat
 * @param videoNote Video note to send. Pass a file_id as String to send a video note that exists on the Telegram servers (recommended) or upload a new video using multipart/form-data. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files). Sending video notes by a URL is currently unsupported
 * @param duration Duration of sent video in seconds
 * @param length Video width and height, i.e. diameter of the video message
 * @param thumbnail Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
 * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass *True* to allow up to 1000 messages per second, ignoring [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once) for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param suggestedPostParameters A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is automatically declined.
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply keyboard or to force a reply from the user
 */
public suspend fun TelegramBotApi.sendVideoNote(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    videoNote: InputFile,
    duration: Long? = null,
    length: Long? = null,
    thumbnail: InputFile? = null,
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
        append(videoNote.toFormPart("video_note"))
        duration?.let {
            append("duration", it.toString())
        }
        length?.let {
            append("length", it.toString())
        }
        thumbnail?.let { append(it.toFormPart("thumbnail")) }
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

/**
 * As of v.4.0, Telegram clients support rounded square MPEG4 videos of up to 1 minute long. Use this method to send video messages. On success, the sent Message is returned.
 *
 * @param form Multipart form object for this operation
 */
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

/**
 * Use this method to send paid media. On success, the sent Message is returned.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`). If the chat is a channel, all Telegram Star proceeds from this media will be credited to the chat's balance. Otherwise, they will be credited to the bot's balance.
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent; required if the message is sent to a direct messages chat
 * @param starCount The number of Telegram Stars that must be paid to buy access to the media; 1-25000
 * @param media A JSON-serialized array describing the media to be sent; up to 10 items
 * @param payload Bot-defined paid media payload, 0-128 bytes. This will not be displayed to the user, use it for your internal processes.
 * @param caption Media caption, 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the media caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of *parse_mode*
 * @param showCaptionAboveMedia Pass *True*, if the caption must be shown above the message media
 * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass *True* to allow up to 1000 messages per second, ignoring [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once) for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param suggestedPostParameters A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is automatically declined.
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply keyboard or to force a reply from the user
 * @param attachments Additional file attachments referenced via attach://<file_attach_name> in media fields
 */
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

/**
 * Use this method to send paid media. On success, the sent Message is returned.
 *
 * @param form Multipart form object for this operation
 */
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

/**
 * Use this method to send a group of photos, videos, documents or audios as an album. Documents and audio files can be only grouped in an album with messages of the same type. On success, an array of Message objects that were sent is returned.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the messages will be sent; required if the messages are sent to a direct messages chat
 * @param media A JSON-serialized array describing messages to be sent, must include 2-10 items
 * @param disableNotification Sends messages [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent messages from forwarding and saving
 * @param allowPaidBroadcast Pass *True* to allow up to 1000 messages per second, ignoring [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once) for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param replyParameters Description of the message to reply to
 * @param attachments Additional file attachments referenced via attach://<file_attach_name> in media fields
 */
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

/**
 * Use this method to send a group of photos, videos, documents or audios as an album. Documents and audio files can be only grouped in an album with messages of the same type. On success, an array of Message objects that were sent is returned.
 *
 * @param form Multipart form object for this operation
 */
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

/**
 * Use this method to set a new profile photo for the chat. Photos can't be changed for private chats. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param photo New chat photo, uploaded using multipart/form-data
 */
public suspend fun TelegramBotApi.setChatPhoto(chatId: String, photo: InputFile): TelegramResponse<Boolean> {
    val formData = MultiPartFormDataContent(formData {
        append("chat_id", chatId)
        append(photo.toFormPart("photo"))
    })
    return setChatPhoto(formData)
}

/**
 * Use this method to set a new profile photo for the chat. Photos can't be changed for private chats. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
 *
 * @param form Multipart form object for this operation
 */
public suspend fun TelegramBotApi.setChatPhoto(form: SetChatPhotoForm): TelegramResponse<Boolean> = setChatPhoto(
    chatId = form.chatId,
    photo = form.photo
)

/**
 * Changes the profile photo of the bot. Returns True on success.
 *
 * @param photo The new profile photo to set
 * @param attachments Additional file attachments referenced via attach://<file_attach_name> in media fields
 */
public suspend fun TelegramBotApi.setMyProfilePhoto(photo: InputProfilePhoto, attachments: List<FormPart<ChannelProvider>>? = null): TelegramResponse<Boolean> {
    val formData = MultiPartFormDataContent(formData {
        append("photo", Json.encodeToString(photo))
        attachments?.forEach { append(it) }
    })
    return setMyProfilePhoto(formData)
}

/**
 * Changes the profile photo of the bot. Returns True on success.
 *
 * @param form Multipart form object for this operation
 */
public suspend fun TelegramBotApi.setMyProfilePhoto(form: SetMyProfilePhotoForm): TelegramResponse<Boolean> = setMyProfilePhoto(
    photo = form.photo,
    attachments = form.attachments
)

/**
 * Changes the profile photo of a managed business account. Requires the can_edit_profile_photo business bot right. Returns True on success.
 *
 * @param businessConnectionId Unique identifier of the business connection
 * @param photo The new profile photo to set
 * @param isPublic Pass *True* to set the public photo, which will be visible even if the main photo is hidden by the business account's privacy settings. An account can have only one public photo.
 * @param attachments Additional file attachments referenced via attach://<file_attach_name> in media fields
 */
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

/**
 * Changes the profile photo of a managed business account. Requires the can_edit_profile_photo business bot right. Returns True on success.
 *
 * @param form Multipart form object for this operation
 */
public suspend fun TelegramBotApi.setBusinessAccountProfilePhoto(form: SetBusinessAccountProfilePhotoForm): TelegramResponse<Boolean> = setBusinessAccountProfilePhoto(
    businessConnectionId = form.businessConnectionId,
    photo = form.photo,
    isPublic = form.isPublic,
    attachments = form.attachments
)

/**
 * Posts a story on behalf of a managed business account. Requires the can_manage_stories business bot right. Returns Story on success.
 *
 * @param businessConnectionId Unique identifier of the business connection
 * @param content Content of the story
 * @param activePeriod Period after which the story is moved to the archive, in seconds; must be one of `6 * 3600`, `12 * 3600`, `86400`, or `2 * 86400`
 * @param caption Caption of the story, 0-2048 characters after entities parsing
 * @param parseMode Mode for parsing entities in the story caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of *parse_mode*
 * @param areas A JSON-serialized list of clickable areas to be shown on the story
 * @param postToChatPage Pass *True* to keep the story accessible after it expires
 * @param protectContent Pass *True* if the content of the story must be protected from forwarding and screenshotting
 * @param attachments Additional file attachments referenced via attach://<file_attach_name> in media fields
 */
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

/**
 * Posts a story on behalf of a managed business account. Requires the can_manage_stories business bot right. Returns Story on success.
 *
 * @param form Multipart form object for this operation
 */
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

/**
 * Edits a story previously posted by the bot on behalf of a managed business account. Requires the can_manage_stories business bot right. Returns Story on success.
 *
 * @param businessConnectionId Unique identifier of the business connection
 * @param storyId Unique identifier of the story to edit
 * @param content Content of the story
 * @param caption Caption of the story, 0-2048 characters after entities parsing
 * @param parseMode Mode for parsing entities in the story caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of *parse_mode*
 * @param areas A JSON-serialized list of clickable areas to be shown on the story
 * @param attachments Additional file attachments referenced via attach://<file_attach_name> in media fields
 */
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

/**
 * Edits a story previously posted by the bot on behalf of a managed business account. Requires the can_manage_stories business bot right. Returns Story on success.
 *
 * @param form Multipart form object for this operation
 */
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

/**
 * Use this method to edit animation, audio, document, photo, or video messages, or to add media to text messages. If a message is part of a message album, then it can be edited only to an audio for audio albums, only to a document for document albums and to a photo or a video otherwise. When an inline message is edited, a new file can't be uploaded; use a previously uploaded file via its file_id or specify a URL. On success, if the edited message is not an inline message, the edited Message is returned, otherwise True is returned. Note that business messages that were not sent by the bot and do not contain an inline keyboard can only be edited within 48 hours from the time they were sent.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to be edited was sent
 * @param chatId Required if *inline_message_id* is not specified. Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageId Required if *inline_message_id* is not specified. Identifier of the message to edit
 * @param inlineMessageId Required if *chat_id* and *message_id* are not specified. Identifier of the inline message
 * @param media A JSON-serialized object for a new media content of the message
 * @param replyMarkup A JSON-serialized object for a new [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards).
 * @param attachments Additional file attachments referenced via attach://<file_attach_name> in media fields
 */
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

/**
 * Use this method to edit animation, audio, document, photo, or video messages, or to add media to text messages. If a message is part of a message album, then it can be edited only to an audio for audio albums, only to a document for document albums and to a photo or a video otherwise. When an inline message is edited, a new file can't be uploaded; use a previously uploaded file via its file_id or specify a URL. On success, if the edited message is not an inline message, the edited Message is returned, otherwise True is returned. Note that business messages that were not sent by the bot and do not contain an inline keyboard can only be edited within 48 hours from the time they were sent.
 *
 * @param form Multipart form object for this operation
 */
public suspend fun TelegramBotApi.editMessageMedia(form: EditMessageMediaForm): TelegramResponse<Boolean> = editMessageMedia(
    businessConnectionId = form.businessConnectionId,
    chatId = form.chatId,
    messageId = form.messageId,
    inlineMessageId = form.inlineMessageId,
    media = form.media,
    replyMarkup = form.replyMarkup,
    attachments = form.attachments
)

/**
 * Use this method to send static .WEBP, animated .TGS, or video .WEBM stickers. On success, the sent Message is returned.
 *
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent; required if the message is sent to a direct messages chat
 * @param sticker Sticker to send. Pass a file_id as String to send a file that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a .WEBP sticker from the Internet, or upload a new .WEBP, .TGS, or .WEBM sticker using multipart/form-data. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files). Video and animated stickers can't be sent via an HTTP URL.
 * @param emoji Emoji associated with the sticker; only for just uploaded stickers
 * @param disableNotification Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass *True* to allow up to 1000 messages per second, ignoring [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once) for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param suggestedPostParameters A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is automatically declined.
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply keyboard or to force a reply from the user
 */
public suspend fun TelegramBotApi.sendSticker(
    businessConnectionId: String? = null,
    chatId: String,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    sticker: InputFile,
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
        append(sticker.toFormPart("sticker"))
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

/**
 * Use this method to send static .WEBP, animated .TGS, or video .WEBM stickers. On success, the sent Message is returned.
 *
 * @param form Multipart form object for this operation
 */
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

/**
 * Use this method to upload a file with a sticker for later use in the createNewStickerSet, addStickerToSet, or replaceStickerInSet methods (the file can be used multiple times). Returns the uploaded File on success.
 *
 * @param userId User identifier of sticker file owner
 * @param sticker A file with the sticker in .WEBP, .PNG, .TGS, or .WEBM format. See [](https://core.telegram.org/stickers)[https://core.telegram.org/stickers](https://core.telegram.org/stickers) for technical requirements. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
 * @param stickerFormat Format of the sticker, must be one of “static”, “animated”, “video”
 */
public suspend fun TelegramBotApi.uploadStickerFile(
    userId: Long,
    sticker: InputFile,
    stickerFormat: String,
): TelegramResponse<File> {
    val formData = MultiPartFormDataContent(formData {
        append("user_id", userId.toString())
        append(sticker.toFormPart("sticker"))
        append("sticker_format", stickerFormat)
    })
    return uploadStickerFile(formData)
}

/**
 * Use this method to upload a file with a sticker for later use in the createNewStickerSet, addStickerToSet, or replaceStickerInSet methods (the file can be used multiple times). Returns the uploaded File on success.
 *
 * @param form Multipart form object for this operation
 */
public suspend fun TelegramBotApi.uploadStickerFile(form: UploadStickerFileForm): TelegramResponse<File> = uploadStickerFile(
    userId = form.userId,
    sticker = form.sticker,
    stickerFormat = form.stickerFormat
)

/**
 * Use this method to create a new sticker set owned by a user. The bot will be able to edit the sticker set thus created. Returns True on success.
 *
 * @param userId User identifier of created sticker set owner
 * @param name Short name of sticker set, to be used in `t.me/addstickers/` URLs (e.g., *animals*). Can contain only English letters, digits and underscores. Must begin with a letter, can't contain consecutive underscores and must end in `"_by_<bot_username>"`. `<bot_username>` is case insensitive. 1-64 characters.
 * @param title Sticker set title, 1-64 characters
 * @param stickers A JSON-serialized list of 1-50 initial stickers to be added to the sticker set
 * @param stickerType Type of stickers in the set, pass “regular”, “mask”, or “custom_emoji”. By default, a regular sticker set is created.
 * @param needsRepainting Pass *True* if stickers in the sticker set must be repainted to the color of text when used in messages, the accent color if used as emoji status, white on chat photos, or another appropriate color based on context; for custom emoji sticker sets only
 * @param attachments Additional file attachments referenced via attach://<file_attach_name> in media fields
 */
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

/**
 * Use this method to create a new sticker set owned by a user. The bot will be able to edit the sticker set thus created. Returns True on success.
 *
 * @param form Multipart form object for this operation
 */
public suspend fun TelegramBotApi.createNewStickerSet(form: CreateNewStickerSetForm): TelegramResponse<Boolean> = createNewStickerSet(
    userId = form.userId,
    name = form.name,
    title = form.title,
    stickers = form.stickers,
    stickerType = form.stickerType,
    needsRepainting = form.needsRepainting,
    attachments = form.attachments
)

/**
 * Use this method to add a new sticker to a set created by the bot. Emoji sticker sets can have up to 200 stickers. Other sticker sets can have up to 120 stickers. Returns True on success.
 *
 * @param userId User identifier of sticker set owner
 * @param name Sticker set name
 * @param sticker A JSON-serialized object with information about the added sticker. If exactly the same sticker had already been added to the set, then the set isn't changed.
 * @param attachments Additional file attachments referenced via attach://<file_attach_name> in media fields
 */
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

/**
 * Use this method to add a new sticker to a set created by the bot. Emoji sticker sets can have up to 200 stickers. Other sticker sets can have up to 120 stickers. Returns True on success.
 *
 * @param form Multipart form object for this operation
 */
public suspend fun TelegramBotApi.addStickerToSet(form: AddStickerToSetForm): TelegramResponse<Boolean> = addStickerToSet(
    userId = form.userId,
    name = form.name,
    sticker = form.sticker,
    attachments = form.attachments
)

/**
 * Use this method to replace an existing sticker in a sticker set with a new one. The method is equivalent to calling deleteStickerFromSet, then addStickerToSet, then setStickerPositionInSet. Returns True on success.
 *
 * @param userId User identifier of the sticker set owner
 * @param name Sticker set name
 * @param oldSticker File identifier of the replaced sticker
 * @param sticker A JSON-serialized object with information about the added sticker. If exactly the same sticker had already been added to the set, then the set remains unchanged.
 * @param attachments Additional file attachments referenced via attach://<file_attach_name> in media fields
 */
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

/**
 * Use this method to replace an existing sticker in a sticker set with a new one. The method is equivalent to calling deleteStickerFromSet, then addStickerToSet, then setStickerPositionInSet. Returns True on success.
 *
 * @param form Multipart form object for this operation
 */
public suspend fun TelegramBotApi.replaceStickerInSet(form: ReplaceStickerInSetForm): TelegramResponse<Boolean> = replaceStickerInSet(
    userId = form.userId,
    name = form.name,
    oldSticker = form.oldSticker,
    sticker = form.sticker,
    attachments = form.attachments
)

/**
 * Use this method to set the thumbnail of a regular or mask sticker set. The format of the thumbnail file must match the format of the stickers in the set. Returns True on success.
 *
 * @param name Sticker set name
 * @param userId User identifier of the sticker set owner
 * @param thumbnail A **.WEBP** or **.PNG** image with the thumbnail, must be up to 128 kilobytes in size and have a width and height of exactly 100px, or a **.TGS** animation with a thumbnail up to 32 kilobytes in size (see [](https://core.telegram.org/stickers#animation-requirements)[https://core.telegram.org/stickers#animation-requirements](https://core.telegram.org/stickers#animation-requirements) for animated sticker technical requirements), or a **.WEBM** video with the thumbnail up to 32 kilobytes in size; see [](https://core.telegram.org/stickers#video-requirements)[https://core.telegram.org/stickers#video-requirements](https://core.telegram.org/stickers#video-requirements) for video sticker technical requirements. Pass a *file_id* as a String to send a file that already exists on the Telegram servers, pass an HTTP URL as a String for Telegram to get a file from the Internet, or upload a new one using multipart/form-data. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files). Animated and video sticker set thumbnails can't be uploaded via HTTP URL. If omitted, then the thumbnail is dropped and the first sticker is used as the thumbnail.
 * @param format Format of the thumbnail, must be one of “static” for a **.WEBP** or **.PNG** image, “animated” for a **.TGS** animation, or “video” for a **.WEBM** video
 */
public suspend fun TelegramBotApi.setStickerSetThumbnail(
    name: String,
    userId: Long,
    thumbnail: InputFile? = null,
    format: String,
): TelegramResponse<Boolean> {
    val formData = MultiPartFormDataContent(formData {
        append("name", name)
        append("user_id", userId.toString())
        thumbnail?.let { append(it.toFormPart("thumbnail")) }
        append("format", format)
    })
    return setStickerSetThumbnail(formData)
}

/**
 * Use this method to set the thumbnail of a regular or mask sticker set. The format of the thumbnail file must match the format of the stickers in the set. Returns True on success.
 *
 * @param form Multipart form object for this operation
 */
public suspend fun TelegramBotApi.setStickerSetThumbnail(form: SetStickerSetThumbnailForm): TelegramResponse<Boolean> = setStickerSetThumbnail(
    name = form.name,
    userId = form.userId,
    thumbnail = form.thumbnail,
    format = form.format
)
