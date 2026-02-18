// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents an audio file to be treated as music by the Telegram clients.
 */
@Serializable
public data class Audio(
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
     * Duration of the audio in seconds as defined by the sender
     */
    public val duration: Long,
    /**
     * *Optional*. Performer of the audio as defined by the sender or by audio tags
     */
    public val performer: String? = null,
    /**
     * *Optional*. Title of the audio as defined by the sender or by audio tags
     */
    public val title: String? = null,
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
    /**
     * *Optional*. Thumbnail of the album cover to which the music file belongs
     */
    public val thumbnail: PhotoSize? = null,
)
