// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes the physical address of a location.
 */
@Serializable
public data class LocationAddress(
    /**
     * The two-letter ISO 3166-1 alpha-2 country code of the country where the location is located
     */
    @SerialName("country_code")
    public val countryCode: String,
    /**
     * *Optional*. State of the location
     */
    public val state: String? = null,
    /**
     * *Optional*. City of the location
     */
    public val city: String? = null,
    /**
     * *Optional*. Street address of the location
     */
    public val street: String? = null,
)
