// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Boolean
import kotlin.Double
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * This object describes the content of a story to post. Currently, it can be one of
 * InputStoryContentPhoto InputStoryContentVideo
 */
@Serializable
@JsonClassDiscriminator("type")
@OptIn(ExperimentalSerializationApi::class)
public sealed interface InputStoryContent

/**
 * Describes a photo to post as a story.
 */
@Serializable
@SerialName("photo")
public data class InputStoryContentPhoto(
    /**
     * The photo to post as a story. The photo must be of the size 1080x1920 and must not exceed 10 MB. The photo can't be reused and can only be uploaded as a new file, so you can pass “attach://<file_attach_name>” if the photo was uploaded using multipart/form-data under <file_attach_name>. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
     */
    public val photo: String,
) : InputStoryContent

/**
 * Describes a video to post as a story.
 */
@Serializable
@SerialName("video")
public data class InputStoryContentVideo(
    /**
     * The video to post as a story. The video must be of the size 720x1280, streamable, encoded with H.265 codec, with key frames added each second in the MPEG4 format, and must not exceed 30 MB. The video can't be reused and can only be uploaded as a new file, so you can pass “attach://<file_attach_name>” if the video was uploaded using multipart/form-data under <file_attach_name>. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
     */
    public val video: String,
    /**
     * *Optional*. Precise duration of the video in seconds; 0-60
     */
    public val duration: Double? = null,
    /**
     * *Optional*. Timestamp in seconds of the frame that will be used as the static cover for the story. Defaults to 0.0.
     */
    @SerialName("cover_frame_timestamp")
    public val coverFrameTimestamp: Double? = null,
    /**
     * *Optional*. Pass *True* if the video has no sound
     */
    @SerialName("is_animation")
    public val isAnimation: Boolean? = null,
) : InputStoryContent
