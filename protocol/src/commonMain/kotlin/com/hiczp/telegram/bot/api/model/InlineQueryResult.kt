// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents one result of an inline query. Telegram clients currently support results of the following 20 types:
 * InlineQueryResultCachedAudio InlineQueryResultCachedDocument InlineQueryResultCachedGif InlineQueryResultCachedMpeg4Gif InlineQueryResultCachedPhoto InlineQueryResultCachedSticker InlineQueryResultCachedVideo InlineQueryResultCachedVoice InlineQueryResultArticle InlineQueryResultAudio InlineQueryResultContact InlineQueryResultGame InlineQueryResultDocument InlineQueryResultGif InlineQueryResultLocation InlineQueryResultMpeg4Gif InlineQueryResultPhoto InlineQueryResultVenue InlineQueryResultVideo InlineQueryResultVoice
 * Note: All URLs passed in inline query results will be available to end users and therefore must be assumed to be public.
 */
@Serializable
public sealed interface InlineQueryResult

/**
 * Represents a link to an MP3 audio file stored on the Telegram servers. By default, this audio file will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the audio.
 */
@Serializable
@SerialName("audio")
public data class InlineQueryResultCachedAudio(
    public val type: String = "audio",
    /**
     * Unique identifier for this result, 1-64 bytes
     */
    public val id: String,
    /**
     * A valid file identifier for the audio file
     */
    @SerialName("audio_file_id")
    public val audioFileId: String,
    /**
     * *Optional*. Caption, 0-1024 characters after entities parsing
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
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the audio
     */
    @SerialName("input_message_content")
    public val inputMessageContent: InputMessageContent? = null,
)

/**
 * Represents a link to a file stored on the Telegram servers. By default, this file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the file.
 */
@Serializable
@SerialName("document")
public data class InlineQueryResultCachedDocument(
    public val type: String = "document",
    /**
     * Unique identifier for this result, 1-64 bytes
     */
    public val id: String,
    /**
     * Title for the result
     */
    public val title: String,
    /**
     * A valid file identifier for the file
     */
    @SerialName("document_file_id")
    public val documentFileId: String,
    /**
     * *Optional*. Short description of the result
     */
    public val description: String? = null,
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
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the file
     */
    @SerialName("input_message_content")
    public val inputMessageContent: InputMessageContent? = null,
)

/**
 * Represents a link to an animated GIF file stored on the Telegram servers. By default, this animated GIF file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with specified content instead of the animation.
 */
@Serializable
@SerialName("gif")
public data class InlineQueryResultCachedGif(
    public val type: String = "gif",
    /**
     * Unique identifier for this result, 1-64 bytes
     */
    public val id: String,
    /**
     * A valid file identifier for the GIF file
     */
    @SerialName("gif_file_id")
    public val gifFileId: String,
    /**
     * *Optional*. Title for the result
     */
    public val title: String? = null,
    /**
     * *Optional*. Caption of the GIF file to be sent, 0-1024 characters after entities parsing
     */
    public val caption: String? = null,
    /**
     * *Optional*. Mode for parsing entities in the caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
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
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the GIF animation
     */
    @SerialName("input_message_content")
    public val inputMessageContent: InputMessageContent? = null,
)

/**
 * Represents a link to a video animation (H.264/MPEG-4 AVC video without sound) stored on the Telegram servers. By default, this animated MPEG-4 file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the animation.
 */
@Serializable
@SerialName("mpeg4_gif")
public data class InlineQueryResultCachedMpeg4Gif(
    public val type: String = "mpeg4_gif",
    /**
     * Unique identifier for this result, 1-64 bytes
     */
    public val id: String,
    /**
     * A valid file identifier for the MPEG4 file
     */
    @SerialName("mpeg4_file_id")
    public val mpeg4FileId: String,
    /**
     * *Optional*. Title for the result
     */
    public val title: String? = null,
    /**
     * *Optional*. Caption of the MPEG-4 file to be sent, 0-1024 characters after entities parsing
     */
    public val caption: String? = null,
    /**
     * *Optional*. Mode for parsing entities in the caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
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
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the video animation
     */
    @SerialName("input_message_content")
    public val inputMessageContent: InputMessageContent? = null,
)

/**
 * Represents a link to a photo stored on the Telegram servers. By default, this photo will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the photo.
 */
@Serializable
@SerialName("photo")
public data class InlineQueryResultCachedPhoto(
    public val type: String = "photo",
    /**
     * Unique identifier for this result, 1-64 bytes
     */
    public val id: String,
    /**
     * A valid file identifier of the photo
     */
    @SerialName("photo_file_id")
    public val photoFileId: String,
    /**
     * *Optional*. Title for the result
     */
    public val title: String? = null,
    /**
     * *Optional*. Short description of the result
     */
    public val description: String? = null,
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
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the photo
     */
    @SerialName("input_message_content")
    public val inputMessageContent: InputMessageContent? = null,
)

/**
 * Represents a link to a sticker stored on the Telegram servers. By default, this sticker will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the sticker.
 */
@Serializable
@SerialName("sticker")
public data class InlineQueryResultCachedSticker(
    public val type: String = "sticker",
    /**
     * Unique identifier for this result, 1-64 bytes
     */
    public val id: String,
    /**
     * A valid file identifier of the sticker
     */
    @SerialName("sticker_file_id")
    public val stickerFileId: String,
    /**
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the sticker
     */
    @SerialName("input_message_content")
    public val inputMessageContent: InputMessageContent? = null,
)

/**
 * Represents a link to a video file stored on the Telegram servers. By default, this video file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the video.
 */
@Serializable
@SerialName("video")
public data class InlineQueryResultCachedVideo(
    public val type: String = "video",
    /**
     * Unique identifier for this result, 1-64 bytes
     */
    public val id: String,
    /**
     * A valid file identifier for the video file
     */
    @SerialName("video_file_id")
    public val videoFileId: String,
    /**
     * Title for the result
     */
    public val title: String,
    /**
     * *Optional*. Short description of the result
     */
    public val description: String? = null,
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
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the video
     */
    @SerialName("input_message_content")
    public val inputMessageContent: InputMessageContent? = null,
)

/**
 * Represents a link to a voice message stored on the Telegram servers. By default, this voice message will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the voice message.
 */
@Serializable
@SerialName("voice")
public data class InlineQueryResultCachedVoice(
    public val type: String = "voice",
    /**
     * Unique identifier for this result, 1-64 bytes
     */
    public val id: String,
    /**
     * A valid file identifier for the voice message
     */
    @SerialName("voice_file_id")
    public val voiceFileId: String,
    /**
     * Voice message title
     */
    public val title: String,
    /**
     * *Optional*. Caption, 0-1024 characters after entities parsing
     */
    public val caption: String? = null,
    /**
     * *Optional*. Mode for parsing entities in the voice message caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
     */
    @SerialName("parse_mode")
    public val parseMode: String? = null,
    /**
     * *Optional*. List of special entities that appear in the caption, which can be specified instead of *parse_mode*
     */
    @SerialName("caption_entities")
    public val captionEntities: List<MessageEntity>? = null,
    /**
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the voice message
     */
    @SerialName("input_message_content")
    public val inputMessageContent: InputMessageContent? = null,
)

/**
 * Represents a link to an article or web page.
 */
@Serializable
@SerialName("article")
public data class InlineQueryResultArticle(
    public val type: String = "article",
    /**
     * Unique identifier for this result, 1-64 Bytes
     */
    public val id: String,
    /**
     * Title of the result
     */
    public val title: String,
    /**
     * Content of the message to be sent
     */
    @SerialName("input_message_content")
    public val inputMessageContent: InputMessageContent,
    /**
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. URL of the result
     */
    public val url: String? = null,
    /**
     * *Optional*. Short description of the result
     */
    public val description: String? = null,
    /**
     * *Optional*. Url of the thumbnail for the result
     */
    @SerialName("thumbnail_url")
    public val thumbnailUrl: String? = null,
    /**
     * *Optional*. Thumbnail width
     */
    @SerialName("thumbnail_width")
    public val thumbnailWidth: Long? = null,
    /**
     * *Optional*. Thumbnail height
     */
    @SerialName("thumbnail_height")
    public val thumbnailHeight: Long? = null,
)

/**
 * Represents a link to an MP3 audio file. By default, this audio file will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the audio.
 */
@Serializable
@SerialName("audio")
public data class InlineQueryResultAudio(
    public val type: String = "audio",
    /**
     * Unique identifier for this result, 1-64 bytes
     */
    public val id: String,
    /**
     * A valid URL for the audio file
     */
    @SerialName("audio_url")
    public val audioUrl: String,
    /**
     * Title
     */
    public val title: String,
    /**
     * *Optional*. Caption, 0-1024 characters after entities parsing
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
     * *Optional*. Performer
     */
    public val performer: String? = null,
    /**
     * *Optional*. Audio duration in seconds
     */
    @SerialName("audio_duration")
    public val audioDuration: Long? = null,
    /**
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the audio
     */
    @SerialName("input_message_content")
    public val inputMessageContent: InputMessageContent? = null,
)

/**
 * Represents a contact with a phone number. By default, this contact will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the contact.
 */
@Serializable
@SerialName("contact")
public data class InlineQueryResultContact(
    public val type: String = "contact",
    /**
     * Unique identifier for this result, 1-64 Bytes
     */
    public val id: String,
    /**
     * Contact's phone number
     */
    @SerialName("phone_number")
    public val phoneNumber: String,
    /**
     * Contact's first name
     */
    @SerialName("first_name")
    public val firstName: String,
    /**
     * *Optional*. Contact's last name
     */
    @SerialName("last_name")
    public val lastName: String? = null,
    /**
     * *Optional*. Additional data about the contact in the form of a [vCard](https://en.wikipedia.org/wiki/VCard), 0-2048 bytes
     */
    public val vcard: String? = null,
    /**
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the contact
     */
    @SerialName("input_message_content")
    public val inputMessageContent: InputMessageContent? = null,
    /**
     * *Optional*. Url of the thumbnail for the result
     */
    @SerialName("thumbnail_url")
    public val thumbnailUrl: String? = null,
    /**
     * *Optional*. Thumbnail width
     */
    @SerialName("thumbnail_width")
    public val thumbnailWidth: Long? = null,
    /**
     * *Optional*. Thumbnail height
     */
    @SerialName("thumbnail_height")
    public val thumbnailHeight: Long? = null,
)

/**
 * Represents a Game.
 */
@Serializable
@SerialName("game")
public data class InlineQueryResultGame(
    public val type: String = "game",
    /**
     * Unique identifier for this result, 1-64 bytes
     */
    public val id: String,
    /**
     * Short name of the game
     */
    @SerialName("game_short_name")
    public val gameShortName: String,
    /**
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
)

/**
 * Represents a link to a file. By default, this file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the file. Currently, only .PDF and .ZIP files can be sent using this method.
 */
@Serializable
@SerialName("document")
public data class InlineQueryResultDocument(
    public val type: String = "document",
    /**
     * Unique identifier for this result, 1-64 bytes
     */
    public val id: String,
    /**
     * Title for the result
     */
    public val title: String,
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
     * A valid URL for the file
     */
    @SerialName("document_url")
    public val documentUrl: String,
    /**
     * MIME type of the content of the file, either “application/pdf” or “application/zip”
     */
    @SerialName("mime_type")
    public val mimeType: String,
    /**
     * *Optional*. Short description of the result
     */
    public val description: String? = null,
    /**
     * *Optional*. Inline keyboard attached to the message
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the file
     */
    @SerialName("input_message_content")
    public val inputMessageContent: InputMessageContent? = null,
    /**
     * *Optional*. URL of the thumbnail (JPEG only) for the file
     */
    @SerialName("thumbnail_url")
    public val thumbnailUrl: String? = null,
    /**
     * *Optional*. Thumbnail width
     */
    @SerialName("thumbnail_width")
    public val thumbnailWidth: Long? = null,
    /**
     * *Optional*. Thumbnail height
     */
    @SerialName("thumbnail_height")
    public val thumbnailHeight: Long? = null,
)

/**
 * Represents a link to an animated GIF file. By default, this animated GIF file will be sent by the user with optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the animation.
 */
@Serializable
@SerialName("gif")
public data class InlineQueryResultGif(
    public val type: String = "gif",
    /**
     * Unique identifier for this result, 1-64 bytes
     */
    public val id: String,
    /**
     * A valid URL for the GIF file
     */
    @SerialName("gif_url")
    public val gifUrl: String,
    /**
     * *Optional*. Width of the GIF
     */
    @SerialName("gif_width")
    public val gifWidth: Long? = null,
    /**
     * *Optional*. Height of the GIF
     */
    @SerialName("gif_height")
    public val gifHeight: Long? = null,
    /**
     * *Optional*. Duration of the GIF in seconds
     */
    @SerialName("gif_duration")
    public val gifDuration: Long? = null,
    /**
     * URL of the static (JPEG or GIF) or animated (MPEG4) thumbnail for the result
     */
    @SerialName("thumbnail_url")
    public val thumbnailUrl: String,
    /**
     * *Optional*. MIME type of the thumbnail, must be one of “image/jpeg”, “image/gif”, or “video/mp4”. Defaults to “image/jpeg”
     */
    @SerialName("thumbnail_mime_type")
    public val thumbnailMimeType: String? = null,
    /**
     * *Optional*. Title for the result
     */
    public val title: String? = null,
    /**
     * *Optional*. Caption of the GIF file to be sent, 0-1024 characters after entities parsing
     */
    public val caption: String? = null,
    /**
     * *Optional*. Mode for parsing entities in the caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
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
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the GIF animation
     */
    @SerialName("input_message_content")
    public val inputMessageContent: InputMessageContent? = null,
)

/**
 * Represents a location on a map. By default, the location will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the location.
 */
@Serializable
@SerialName("location")
public data class InlineQueryResultLocation(
    public val type: String = "location",
    /**
     * Unique identifier for this result, 1-64 Bytes
     */
    public val id: String,
    /**
     * Location latitude in degrees
     */
    public val latitude: Double,
    /**
     * Location longitude in degrees
     */
    public val longitude: Double,
    /**
     * Location title
     */
    public val title: String,
    /**
     * *Optional*. The radius of uncertainty for the location, measured in meters; 0-1500
     */
    @SerialName("horizontal_accuracy")
    public val horizontalAccuracy: Double? = null,
    /**
     * *Optional*. Period in seconds during which the location can be updated, should be between 60 and 86400, or 0x7FFFFFFF for live locations that can be edited indefinitely.
     */
    @SerialName("live_period")
    public val livePeriod: Long? = null,
    /**
     * *Optional*. For live locations, a direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
     */
    public val heading: Long? = null,
    /**
     * *Optional*. For live locations, a maximum distance for proximity alerts about approaching another chat member, in meters. Must be between 1 and 100000 if specified.
     */
    @SerialName("proximity_alert_radius")
    public val proximityAlertRadius: Long? = null,
    /**
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the location
     */
    @SerialName("input_message_content")
    public val inputMessageContent: InputMessageContent? = null,
    /**
     * *Optional*. Url of the thumbnail for the result
     */
    @SerialName("thumbnail_url")
    public val thumbnailUrl: String? = null,
    /**
     * *Optional*. Thumbnail width
     */
    @SerialName("thumbnail_width")
    public val thumbnailWidth: Long? = null,
    /**
     * *Optional*. Thumbnail height
     */
    @SerialName("thumbnail_height")
    public val thumbnailHeight: Long? = null,
)

/**
 * Represents a link to a video animation (H.264/MPEG-4 AVC video without sound). By default, this animated MPEG-4 file will be sent by the user with optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the animation.
 */
@Serializable
@SerialName("mpeg4_gif")
public data class InlineQueryResultMpeg4Gif(
    public val type: String = "mpeg4_gif",
    /**
     * Unique identifier for this result, 1-64 bytes
     */
    public val id: String,
    /**
     * A valid URL for the MPEG4 file
     */
    @SerialName("mpeg4_url")
    public val mpeg4Url: String,
    /**
     * *Optional*. Video width
     */
    @SerialName("mpeg4_width")
    public val mpeg4Width: Long? = null,
    /**
     * *Optional*. Video height
     */
    @SerialName("mpeg4_height")
    public val mpeg4Height: Long? = null,
    /**
     * *Optional*. Video duration in seconds
     */
    @SerialName("mpeg4_duration")
    public val mpeg4Duration: Long? = null,
    /**
     * URL of the static (JPEG or GIF) or animated (MPEG4) thumbnail for the result
     */
    @SerialName("thumbnail_url")
    public val thumbnailUrl: String,
    /**
     * *Optional*. MIME type of the thumbnail, must be one of “image/jpeg”, “image/gif”, or “video/mp4”. Defaults to “image/jpeg”
     */
    @SerialName("thumbnail_mime_type")
    public val thumbnailMimeType: String? = null,
    /**
     * *Optional*. Title for the result
     */
    public val title: String? = null,
    /**
     * *Optional*. Caption of the MPEG-4 file to be sent, 0-1024 characters after entities parsing
     */
    public val caption: String? = null,
    /**
     * *Optional*. Mode for parsing entities in the caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
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
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the video animation
     */
    @SerialName("input_message_content")
    public val inputMessageContent: InputMessageContent? = null,
)

/**
 * Represents a link to a photo. By default, this photo will be sent by the user with optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the photo.
 */
@Serializable
@SerialName("photo")
public data class InlineQueryResultPhoto(
    public val type: String = "photo",
    /**
     * Unique identifier for this result, 1-64 bytes
     */
    public val id: String,
    /**
     * A valid URL of the photo. Photo must be in **JPEG** format. Photo size must not exceed 5MB
     */
    @SerialName("photo_url")
    public val photoUrl: String,
    /**
     * URL of the thumbnail for the photo
     */
    @SerialName("thumbnail_url")
    public val thumbnailUrl: String,
    /**
     * *Optional*. Width of the photo
     */
    @SerialName("photo_width")
    public val photoWidth: Long? = null,
    /**
     * *Optional*. Height of the photo
     */
    @SerialName("photo_height")
    public val photoHeight: Long? = null,
    /**
     * *Optional*. Title for the result
     */
    public val title: String? = null,
    /**
     * *Optional*. Short description of the result
     */
    public val description: String? = null,
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
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the photo
     */
    @SerialName("input_message_content")
    public val inputMessageContent: InputMessageContent? = null,
)

/**
 * Represents a venue. By default, the venue will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the venue.
 */
@Serializable
@SerialName("venue")
public data class InlineQueryResultVenue(
    public val type: String = "venue",
    /**
     * Unique identifier for this result, 1-64 Bytes
     */
    public val id: String,
    /**
     * Latitude of the venue location in degrees
     */
    public val latitude: Double,
    /**
     * Longitude of the venue location in degrees
     */
    public val longitude: Double,
    /**
     * Title of the venue
     */
    public val title: String,
    /**
     * Address of the venue
     */
    public val address: String,
    /**
     * *Optional*. Foursquare identifier of the venue if known
     */
    @SerialName("foursquare_id")
    public val foursquareId: String? = null,
    /**
     * *Optional*. Foursquare type of the venue, if known. (For example, “arts_entertainment/default”, “arts_entertainment/aquarium” or “food/icecream”.)
     */
    @SerialName("foursquare_type")
    public val foursquareType: String? = null,
    /**
     * *Optional*. Google Places identifier of the venue
     */
    @SerialName("google_place_id")
    public val googlePlaceId: String? = null,
    /**
     * *Optional*. Google Places type of the venue. (See [supported types](https://developers.google.com/places/web-service/supported_types).)
     */
    @SerialName("google_place_type")
    public val googlePlaceType: String? = null,
    /**
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the venue
     */
    @SerialName("input_message_content")
    public val inputMessageContent: InputMessageContent? = null,
    /**
     * *Optional*. Url of the thumbnail for the result
     */
    @SerialName("thumbnail_url")
    public val thumbnailUrl: String? = null,
    /**
     * *Optional*. Thumbnail width
     */
    @SerialName("thumbnail_width")
    public val thumbnailWidth: Long? = null,
    /**
     * *Optional*. Thumbnail height
     */
    @SerialName("thumbnail_height")
    public val thumbnailHeight: Long? = null,
)

/**
 * Represents a link to a page containing an embedded video player or a video file. By default, this video file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the video.
 * If an InlineQueryResultVideo message contains an embedded video (e.g., YouTube), you must replace its content using input_message_content.
 */
@Serializable
@SerialName("video")
public data class InlineQueryResultVideo(
    public val type: String = "video",
    /**
     * Unique identifier for this result, 1-64 bytes
     */
    public val id: String,
    /**
     * A valid URL for the embedded video player or video file
     */
    @SerialName("video_url")
    public val videoUrl: String,
    /**
     * MIME type of the content of the video URL, “text/html” or “video/mp4”
     */
    @SerialName("mime_type")
    public val mimeType: String,
    /**
     * URL of the thumbnail (JPEG only) for the video
     */
    @SerialName("thumbnail_url")
    public val thumbnailUrl: String,
    /**
     * Title for the result
     */
    public val title: String,
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
    @SerialName("video_width")
    public val videoWidth: Long? = null,
    /**
     * *Optional*. Video height
     */
    @SerialName("video_height")
    public val videoHeight: Long? = null,
    /**
     * *Optional*. Video duration in seconds
     */
    @SerialName("video_duration")
    public val videoDuration: Long? = null,
    /**
     * *Optional*. Short description of the result
     */
    public val description: String? = null,
    /**
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the video. This field is **required** if InlineQueryResultVideo is used to send an HTML-page as a result (e.g., a YouTube video).
     */
    @SerialName("input_message_content")
    public val inputMessageContent: InputMessageContent? = null,
)

/**
 * Represents a link to a voice recording in an .OGG container encoded with OPUS. By default, this voice recording will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the the voice message.
 */
@Serializable
@SerialName("voice")
public data class InlineQueryResultVoice(
    public val type: String = "voice",
    /**
     * Unique identifier for this result, 1-64 bytes
     */
    public val id: String,
    /**
     * A valid URL for the voice recording
     */
    @SerialName("voice_url")
    public val voiceUrl: String,
    /**
     * Recording title
     */
    public val title: String,
    /**
     * *Optional*. Caption, 0-1024 characters after entities parsing
     */
    public val caption: String? = null,
    /**
     * *Optional*. Mode for parsing entities in the voice message caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
     */
    @SerialName("parse_mode")
    public val parseMode: String? = null,
    /**
     * *Optional*. List of special entities that appear in the caption, which can be specified instead of *parse_mode*
     */
    @SerialName("caption_entities")
    public val captionEntities: List<MessageEntity>? = null,
    /**
     * *Optional*. Recording duration in seconds
     */
    @SerialName("voice_duration")
    public val voiceDuration: Long? = null,
    /**
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the voice recording
     */
    @SerialName("input_message_content")
    public val inputMessageContent: InputMessageContent? = null,
)
