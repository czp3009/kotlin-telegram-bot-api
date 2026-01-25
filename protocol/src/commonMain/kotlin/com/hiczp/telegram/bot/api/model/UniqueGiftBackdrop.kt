// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * This object describes the backdrop of a unique gift.
 */
@Serializable
public data class UniqueGiftBackdrop(
    /**
     * Name of the backdrop
     */
    public val name: String,
    /**
     * Colors of the backdrop
     */
    public val colors: JsonElement?,
    /**
     * The number of unique gifts that receive this backdrop for every 1000 gifts upgraded
     */
    @SerialName("rarity_per_mille")
    public val rarityPerMille: Long,
)
