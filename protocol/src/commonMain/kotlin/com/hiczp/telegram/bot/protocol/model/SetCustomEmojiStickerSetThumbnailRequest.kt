// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SetCustomEmojiStickerSetThumbnailRequest(
    /**
     * Sticker set name
     */
    public val name: String,
    /**
     * Custom emoji identifier of a sticker from the sticker set; pass an empty string to drop the thumbnail and use the first sticker as the thumbnail.
     */
    @SerialName("custom_emoji_id")
    public val customEmojiId: String? = null,
)
