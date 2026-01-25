// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * This object represents the content of a media message to be sent. It should be one of
 * InputMediaAnimation InputMediaDocument InputMediaAudio InputMediaPhoto InputMediaVideo
 */
@Serializable
@JsonClassDiscriminator("type")
@OptIn(ExperimentalSerializationApi::class)
public sealed interface InputMedia

/**
 * Represents an animation file (GIF or H.264/MPEG-4 AVC video without sound) to be sent.
 */
@Serializable
@SerialName("animation")
public data class InputMediaAnimation(
    /**
     * File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
     */
    public val media: String,
    /**
     * *Optional*. Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
     */
    public val thumbnail: String? = null,
    /**
     * *Optional*. Caption of the animation to be sent, 0-1024 characters after entities parsing
     */
    public val caption: String? = null,
    /**
     * *Optional*. Mode for parsing entities in the animation caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
     */
    @SerialName("parse_mode")
    public val parseMode: String? = null,
    /**
     * *Optional*. List of special entities that appear in the caption, which can be specified instead of *parse_mode*
     */
    @SerialName("caption_entities")
    public val captionEntities: List<MessageEntity>? = null,
    /**
     * *Optional*. Pass *True*, if the caption must be shown above the message media
     */
    @SerialName("show_caption_above_media")
    public val showCaptionAboveMedia: Boolean? = null,
    /**
     * *Optional*. Animation width
     */
    public val width: Long? = null,
    /**
     * *Optional*. Animation height
     */
    public val height: Long? = null,
    /**
     * *Optional*. Animation duration in seconds
     */
    public val duration: Long? = null,
    /**
     * *Optional*. Pass *True* if the animation needs to be covered with a spoiler animation
     */
    @SerialName("has_spoiler")
    public val hasSpoiler: Boolean? = null,
) : InputMedia

/**
 * Represents a general file to be sent.
 */
@Serializable
@SerialName("document")
public data class InputMediaDocument(
    /**
     * File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
     */
    public val media: String,
    /**
     * *Optional*. Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
     */
    public val thumbnail: String? = null,
    /**
     * *Optional*. Caption of the document to be sent, 0-1024 characters after entities parsing
     */
    public val caption: String? = null,
    /**
     * *Optional*. Mode for parsing entities in the document caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
     */
    @SerialName("parse_mode")
    public val parseMode: String? = null,
    /**
     * *Optional*. List of special entities that appear in the caption, which can be specified instead of *parse_mode*
     */
    @SerialName("caption_entities")
    public val captionEntities: List<MessageEntity>? = null,
    /**
     * *Optional*. Disables automatic server-side content type detection for files uploaded using multipart/form-data. Always *True*, if the document is sent as part of an album.
     */
    @SerialName("disable_content_type_detection")
    public val disableContentTypeDetection: Boolean? = null,
) : InputMedia

/**
 * Represents an audio file to be treated as music to be sent.
 */
@Serializable
@SerialName("audio")
public data class InputMediaAudio(
    /**
     * File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
     */
    public val media: String,
    /**
     * *Optional*. Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
     */
    public val thumbnail: String? = null,
    /**
     * *Optional*. Caption of the audio to be sent, 0-1024 characters after entities parsing
     */
    public val caption: String? = null,
    /**
     * *Optional*. Mode for parsing entities in the audio caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
     */
    @SerialName("parse_mode")
    public val parseMode: String? = null,
    /**
     * *Optional*. List of special entities that appear in the caption, which can be specified instead of *parse_mode*
     */
    @SerialName("caption_entities")
    public val captionEntities: List<MessageEntity>? = null,
    /**
     * *Optional*. Duration of the audio in seconds
     */
    public val duration: Long? = null,
    /**
     * *Optional*. Performer of the audio
     */
    public val performer: String? = null,
    /**
     * *Optional*. Title of the audio
     */
    public val title: String? = null,
) : InputMedia

/**
 * Represents a photo to be sent.
 */
@Serializable
@SerialName("photo")
public data class InputMediaPhoto(
    /**
     * File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
     */
    public val media: String,
    /**
     * *Optional*. Caption of the photo to be sent, 0-1024 characters after entities parsing
     */
    public val caption: String? = null,
    /**
     * *Optional*. Mode for parsing entities in the photo caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
     */
    @SerialName("parse_mode")
    public val parseMode: String? = null,
    /**
     * *Optional*. List of special entities that appear in the caption, which can be specified instead of *parse_mode*
     */
    @SerialName("caption_entities")
    public val captionEntities: List<MessageEntity>? = null,
    /**
     * *Optional*. Pass *True*, if the caption must be shown above the message media
     */
    @SerialName("show_caption_above_media")
    public val showCaptionAboveMedia: Boolean? = null,
    /**
     * *Optional*. Pass *True* if the photo needs to be covered with a spoiler animation
     */
    @SerialName("has_spoiler")
    public val hasSpoiler: Boolean? = null,
) : InputMedia

/**
 * Represents a video to be sent.
 */
@Serializable
@SerialName("video")
public data class InputMediaVideo(
    /**
     * File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
     */
    public val media: String,
    /**
     * *Optional*. Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
     */
    public val thumbnail: String? = null,
    /**
     * *Optional*. Cover for the video in the message. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
     */
    public val cover: String? = null,
    /**
     * *Optional*. Start timestamp for the video in the message
     */
    @SerialName("start_timestamp")
    public val startTimestamp: Long? = null,
    /**
     * *Optional*. Caption of the video to be sent, 0-1024 characters after entities parsing
     */
    public val caption: String? = null,
    /**
     * *Optional*. Mode for parsing entities in the video caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
     */
    @SerialName("parse_mode")
    public val parseMode: String? = null,
    /**
     * *Optional*. List of special entities that appear in the caption, which can be specified instead of *parse_mode*
     */
    @SerialName("caption_entities")
    public val captionEntities: List<MessageEntity>? = null,
    /**
     * *Optional*. Pass *True*, if the caption must be shown above the message media
     */
    @SerialName("show_caption_above_media")
    public val showCaptionAboveMedia: Boolean? = null,
    /**
     * *Optional*. Video width
     */
    public val width: Long? = null,
    /**
     * *Optional*. Video height
     */
    public val height: Long? = null,
    /**
     * *Optional*. Video duration in seconds
     */
    public val duration: Long? = null,
    /**
     * *Optional*. Pass *True* if the uploaded video is suitable for streaming
     */
    @SerialName("supports_streaming")
    public val supportsStreaming: Boolean? = null,
    /**
     * *Optional*. Pass *True* if the video needs to be covered with a spoiler animation
     */
    @SerialName("has_spoiler")
    public val hasSpoiler: Boolean? = null,
) : InputMedia
