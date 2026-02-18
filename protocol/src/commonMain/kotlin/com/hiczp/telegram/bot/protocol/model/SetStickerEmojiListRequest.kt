// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SetStickerEmojiListRequest(
    /**
     * File identifier of the sticker
     */
    public val sticker: String,
    /**
     * A JSON-serialized list of 1-20 emoji associated with the sticker
     */
    @SerialName("emoji_list")
    public val emojiList: List<String>,
)
