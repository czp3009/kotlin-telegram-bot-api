// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents a shipping address.
 */
@Serializable
public data class ShippingAddress(
    /**
     * Two-letter [ISO 3166-1 alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) country code
     */
    @SerialName("country_code")
    public val countryCode: String,
    /**
     * State, if applicable
     */
    public val state: String,
    /**
     * City
     */
    public val city: String,
    /**
     * First line for the address
     */
    @SerialName("street_line1")
    public val streetLine1: String,
    /**
     * Second line for the address
     */
    @SerialName("street_line2")
    public val streetLine2: String,
    /**
     * Address post code
     */
    @SerialName("post_code")
    public val postCode: String,
)
