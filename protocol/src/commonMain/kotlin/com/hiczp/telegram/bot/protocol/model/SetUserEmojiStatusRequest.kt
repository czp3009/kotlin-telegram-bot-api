// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SetUserEmojiStatusRequest(
    /**
     * Unique identifier of the target user
     */
    @SerialName("user_id")
    public val userId: Long,
    /**
     * Custom emoji identifier of the emoji status to set. Pass an empty string to remove the status.
     */
    @SerialName("emoji_status_custom_emoji_id")
    public val emojiStatusCustomEmojiId: String? = null,
    /**
     * Expiration date of the emoji status, if any
     */
    @SerialName("emoji_status_expiration_date")
    public val emojiStatusExpirationDate: Long? = null,
)
