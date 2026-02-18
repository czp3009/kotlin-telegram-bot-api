// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes an interval of time during which a business is open.
 */
@Serializable
public data class BusinessOpeningHoursInterval(
    /**
     * The minute's sequence number in a week, starting on Monday, marking the start of the time interval during which the business is open; 0 - 7 * 24 * 60
     */
    @SerialName("opening_minute")
    public val openingMinute: Long,
    /**
     * The minute's sequence number in a week, starting on Monday, marking the end of the time interval during which the business is open; 0 - 8 * 24 * 60
     */
    @SerialName("closing_minute")
    public val closingMinute: Long,
)
