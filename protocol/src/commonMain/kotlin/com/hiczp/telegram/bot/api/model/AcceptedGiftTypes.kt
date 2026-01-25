// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object describes the types of gifts that can be gifted to a user or a chat.
 */
@Serializable
public data class AcceptedGiftTypes(
    /**
     * *True*, if unlimited regular gifts are accepted
     */
    @SerialName("unlimited_gifts")
    public val unlimitedGifts: Boolean,
    /**
     * *True*, if limited regular gifts are accepted
     */
    @SerialName("limited_gifts")
    public val limitedGifts: Boolean,
    /**
     * *True*, if unique gifts or gifts that can be upgraded to unique for free are accepted
     */
    @SerialName("unique_gifts")
    public val uniqueGifts: Boolean,
    /**
     * *True*, if a Telegram Premium subscription is accepted
     */
    @SerialName("premium_subscription")
    public val premiumSubscription: Boolean,
    /**
     * *True*, if transfers of unique gifts from channels are accepted
     */
    @SerialName("gifts_from_channels")
    public val giftsFromChannels: Boolean,
)
