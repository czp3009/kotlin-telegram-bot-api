// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.model

import com.hiczp.telegram.bot.api.type.IncomingUpdate
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    public val from: User,
    /**
     * Bot-specified invoice payload
     */
    @SerialName("invoice_payload")
    public val invoicePayload: String,
    /**
     * User specified shipping address
     */
    @SerialName("shipping_address")
    public val shippingAddress: ShippingAddress,
) : IncomingUpdate
