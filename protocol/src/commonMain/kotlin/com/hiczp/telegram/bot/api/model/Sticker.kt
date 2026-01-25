// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * This object represents a sticker.
 */
@Serializable
public data class Sticker(
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
     * Type of the sticker, currently one of “regular”, “mask”, “custom_emoji”. The type of the sticker is independent from its format, which is determined by the fields *is_animated* and *is_video*.
     */
    public val type: String,
    /**
     * Sticker width
     */
    public val width: Long,
    /**
     * Sticker height
     */
    public val height: Long,
    /**
     * *True*, if the sticker is [animated](https://telegram.org/blog/animated-stickers)
     */
    @SerialName("is_animated")
    public val isAnimated: Boolean,
    /**
     * *True*, if the sticker is a [video sticker](https://telegram.org/blog/video-stickers-better-reactions)
     */
    @SerialName("is_video")
    public val isVideo: Boolean,
    /**
     * *Optional*. Sticker thumbnail in the .WEBP or .JPG format
     */
    public val thumbnail: JsonElement? = null,
    /**
     * *Optional*. Emoji associated with the sticker
     */
    public val emoji: String? = null,
    /**
     * *Optional*. Name of the sticker set to which the sticker belongs
     */
    @SerialName("set_name")
    public val setName: String? = null,
    /**
     * *Optional*. For premium regular stickers, premium animation for the sticker
     */
    @SerialName("premium_animation")
    public val premiumAnimation: JsonElement? = null,
    /**
     * *Optional*. For mask stickers, the position where the mask should be placed
     */
    @SerialName("mask_position")
    public val maskPosition: JsonElement? = null,
    /**
     * *Optional*. For custom emoji stickers, unique identifier of the custom emoji
     */
    @SerialName("custom_emoji_id")
    public val customEmojiId: String? = null,
    /**
     * *Optional*. *True*, if the sticker must be repainted to a text color in messages, the color of the Telegram Premium badge in emoji status, white color on chat photos, or another appropriate color in other places
     */
    @SerialName("needs_repainting")
    public val needsRepainting: Boolean? = null,
    /**
     * *Optional*. File size in bytes
     */
    @SerialName("file_size")
    public val fileSize: Long? = null,
)
