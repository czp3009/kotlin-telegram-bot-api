// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * This object contains information about an incoming shipping query.
 */
@Serializable
public data class ShippingQuery(
    /**
     * Unique query identifier
     */
    public val id: String,
    /**
     * User who sent the query
     */
    public val from: JsonElement?,
    /**
     * Bot-specified invoice payload
     */
    @SerialName("invoice_payload")
    public val invoicePayload: String,
    /**
     * User specified shipping address
     */
    @SerialName("shipping_address")
    public val shippingAddress: JsonElement?,
)
