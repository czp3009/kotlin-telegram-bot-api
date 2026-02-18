// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents a video message (available in Telegram apps as of v.4.0).
 */
@Serializable
public data class VideoNote(
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
     * Video width and height (diameter of the video message) as defined by the sender
     */
    public val length: Long,
    /**
     * Duration of the video in seconds as defined by the sender
     */
    public val duration: Long,
    /**
     * *Optional*. Video thumbnail
     */
    public val thumbnail: PhotoSize? = null,
    /**
     * *Optional*. File size in bytes
     */
    @SerialName("file_size")
    public val fileSize: Long? = null,
)
