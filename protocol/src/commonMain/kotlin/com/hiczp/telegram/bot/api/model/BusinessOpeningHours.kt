// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes the opening hours of a business.
 */
@Serializable
public data class BusinessOpeningHours(
    /**
     * Unique name of the time zone for which the opening hours are defined
     */
    @SerialName("time_zone_name")
    public val timeZoneName: String,
    /**
     * List of time intervals describing business opening hours
     */
    @SerialName("opening_hours")
    public val openingHours: List<BusinessOpeningHoursInterval>,
)
