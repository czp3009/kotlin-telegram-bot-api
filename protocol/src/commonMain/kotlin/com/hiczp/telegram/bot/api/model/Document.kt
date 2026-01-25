// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * This object represents a general file (as opposed to photos, voice messages and audio files).
 */
@Serializable
public data class Document(
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
     * *Optional*. Document thumbnail as defined by the sender
     */
    public val thumbnail: JsonElement? = null,
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
