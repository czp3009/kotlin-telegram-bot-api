// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonElement

/**
 * This object describes paid media. Currently, it can be one of
 * PaidMediaPreview PaidMediaPhoto PaidMediaVideo
 */
@Serializable
@JsonClassDiscriminator("type")
@OptIn(ExperimentalSerializationApi::class)
public sealed interface PaidMedia

/**
 * The paid media isn't available before the payment.
 */
@Serializable
@SerialName("preview")
public data class PaidMediaPreview(
    /**
     * *Optional*. Media width as defined by the sender
     */
    public val width: Long? = null,
    /**
     * *Optional*. Media height as defined by the sender
     */
    public val height: Long? = null,
    /**
     * *Optional*. Duration of the media in seconds as defined by the sender
     */
    public val duration: Long? = null,
) : PaidMedia

/**
 * The paid media is a photo.
 */
@Serializable
@SerialName("photo")
public data class PaidMediaPhoto(
    /**
     * The photo
     */
    public val photo: List<PhotoSize>,
) : PaidMedia

/**
 * The paid media is a video.
 */
@Serializable
@SerialName("video")
public data class PaidMediaVideo(
    /**
     * The video
     */
    public val video: JsonElement?,
) : PaidMedia
