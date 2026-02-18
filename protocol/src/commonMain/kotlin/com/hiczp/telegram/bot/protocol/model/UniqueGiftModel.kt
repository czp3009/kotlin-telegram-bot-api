// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object describes the model of a unique gift.
 */
@Serializable
public data class UniqueGiftModel(
    /**
     * Name of the model
     */
    public val name: String,
    /**
     * The sticker that represents the unique gift
     */
    public val sticker: Sticker,
    /**
     * The number of unique gifts that receive this model for every 1000 gift upgrades. Always 0 for crafted gifts.
     */
    @SerialName("rarity_per_mille")
    public val rarityPerMille: Long,
    /**
     * *Optional*. Rarity of the model if it is a crafted model. Currently, can be “uncommon”, “rare”, “epic”, or “legendary”.
     */
    public val rarity: String? = null,
)
