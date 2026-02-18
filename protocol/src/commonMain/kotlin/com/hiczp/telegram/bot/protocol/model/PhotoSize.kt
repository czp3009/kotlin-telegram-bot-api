// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents one size of a photo or a file / sticker thumbnail.
 */
@Serializable
public data class PhotoSize(
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
     * Photo width
     */
    public val width: Long,
    /**
     * Photo height
     */
    public val height: Long,
    /**
     * *Optional*. File size in bytes
     */
    @SerialName("file_size")
    public val fileSize: Long? = null,
)
