// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.Serializable

/**
 * This object represents one shipping option.
 */
@Serializable
public data class ShippingOption(
    /**
     * Shipping option identifier
     */
    public val id: String,
    /**
     * Option title
     */
    public val title: String,
    /**
     * List of price portions
     */
    public val prices: List<LabeledPrice>,
)
