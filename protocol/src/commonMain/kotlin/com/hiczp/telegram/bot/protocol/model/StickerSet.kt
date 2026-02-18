// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents a sticker set.
 */
@Serializable
public data class StickerSet(
    /**
     * Sticker set name
     */
    public val name: String,
    /**
     * Sticker set title
     */
    public val title: String,
    /**
     * Type of stickers in the set, currently one of “regular”, “mask”, “custom_emoji”
     */
    @SerialName("sticker_type")
    public val stickerType: String,
    /**
     * List of all set stickers
     */
    public val stickers: List<Sticker>,
    /**
     * *Optional*. Sticker set thumbnail in the .WEBP, .TGS, or .WEBM format
     */
    public val thumbnail: PhotoSize? = null,
)
