// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents a gift that can be sent by the bot.
 */
@Serializable
public data class Gift(
    /**
     * Unique identifier of the gift
     */
    public val id: String,
    /**
     * The sticker that represents the gift
     */
    public val sticker: Sticker,
    /**
     * The number of Telegram Stars that must be paid to send the sticker
     */
    @SerialName("star_count")
    public val starCount: Long,
    /**
     * *Optional*. The number of Telegram Stars that must be paid to upgrade the gift to a unique one
     */
    @SerialName("upgrade_star_count")
    public val upgradeStarCount: Long? = null,
    /**
     * *Optional*. *True*, if the gift can only be purchased by Telegram Premium subscribers
     */
    @SerialName("is_premium")
    public val isPremium: Boolean? = null,
    /**
     * *Optional*. *True*, if the gift can be used (after being upgraded) to customize a user's appearance
     */
    @SerialName("has_colors")
    public val hasColors: Boolean? = null,
    /**
     * *Optional*. The total number of gifts of this type that can be sent by all users; for limited gifts only
     */
    @SerialName("total_count")
    public val totalCount: Long? = null,
    /**
     * *Optional*. The number of remaining gifts of this type that can be sent by all users; for limited gifts only
     */
    @SerialName("remaining_count")
    public val remainingCount: Long? = null,
    /**
     * *Optional*. The total number of gifts of this type that can be sent by the bot; for limited gifts only
     */
    @SerialName("personal_total_count")
    public val personalTotalCount: Long? = null,
    /**
     * *Optional*. The number of remaining gifts of this type that can be sent by the bot; for limited gifts only
     */
    @SerialName("personal_remaining_count")
    public val personalRemainingCount: Long? = null,
    /**
     * *Optional*. Background of the gift
     */
    public val background: GiftBackground? = null,
    /**
     * *Optional*. The total number of different unique gifts that can be obtained by upgrading the gift
     */
    @SerialName("unique_gift_variant_count")
    public val uniqueGiftVariantCount: Long? = null,
    /**
     * *Optional*. Information about the chat that published the gift
     */
    @SerialName("publisher_chat")
    public val publisherChat: Chat? = null,
)
