// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents information about an order.
 */
@Serializable
public data class OrderInfo(
    /**
     * *Optional*. User name
     */
    public val name: String? = null,
    /**
     * *Optional*. User's phone number
     */
    @SerialName("phone_number")
    public val phoneNumber: String? = null,
    /**
     * *Optional*. User email
     */
    public val email: String? = null,
    /**
     * *Optional*. User shipping address
     */
    @SerialName("shipping_address")
    public val shippingAddress: ShippingAddress? = null,
)
