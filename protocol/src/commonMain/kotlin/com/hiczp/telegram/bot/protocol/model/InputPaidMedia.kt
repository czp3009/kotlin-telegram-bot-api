// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * This object describes the paid media to be sent. Currently, it can be one of
 * InputPaidMediaPhoto InputPaidMediaVideo
 */
@Serializable
@JsonClassDiscriminator("type")
@OptIn(ExperimentalSerializationApi::class)
public sealed interface InputPaidMedia

/**
 * The paid media to send is a photo.
 */
@Serializable
@SerialName("photo")
public data class InputPaidMediaPhoto(
    /**
     * File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
     */
    public val media: String,
) : InputPaidMedia

/**
 * The paid media to send is a video.
 */
@Serializable
@SerialName("video")
public data class InputPaidMediaVideo(
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
) : InputPaidMedia
