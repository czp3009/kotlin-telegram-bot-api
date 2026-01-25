// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * This object represents a video file.
 */
@Serializable
public data class Video(
    /**
     * Identifier for this file, which can be used to download or reuse the file
     */
    @SerialName("file_id")
    public val fileId: String,
    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
     */
    @SerialName("file_unique_id")
    public val fileUniqueId: String,
    /**
     * Video width as defined by the sender
     */
    public val width: Long,
    /**
     * Video height as defined by the sender
     */
    public val height: Long,
    /**
     * Duration of the video in seconds as defined by the sender
     */
    public val duration: Long,
    /**
     * *Optional*. Video thumbnail
     */
    public val thumbnail: JsonElement? = null,
    /**
     * *Optional*. Available sizes of the cover of the video in the message
     */
    public val cover: List<PhotoSize>? = null,
    /**
     * *Optional*. Timestamp in seconds from which the video will play in the message
     */
    @SerialName("start_timestamp")
    public val startTimestamp: Long? = null,
    /**
     * *Optional*. Original filename as defined by the sender
     */
    @SerialName("file_name")
    public val fileName: String? = null,
    /**
     * *Optional*. MIME type of the file as defined by the sender
     */
    @SerialName("mime_type")
    public val mimeType: String? = null,
    /**
     * *Optional*. File size in bytes. It can be bigger than 2^31 and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this value.
     */
    @SerialName("file_size")
    public val fileSize: Long? = null,
)
