// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object describes the symbol shown on the pattern of a unique gift.
 */
@Serializable
public data class UniqueGiftSymbol(
    /**
     * Name of the symbol
     */
    public val name: String,
    /**
     * The sticker that represents the unique gift
     */
    public val sticker: Sticker,
    /**
     * The number of unique gifts that receive this model for every 1000 gifts upgraded
     */
    @SerialName("rarity_per_mille")
    public val rarityPerMille: Long,
)
