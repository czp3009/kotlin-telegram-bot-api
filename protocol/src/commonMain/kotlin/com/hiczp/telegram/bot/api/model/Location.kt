// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Double
import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents a point on the map.
 */
@Serializable
public data class Location(
    /**
     * Latitude as defined by the sender
     */
    public val latitude: Double,
    /**
     * Longitude as defined by the sender
     */
    public val longitude: Double,
    /**
     * *Optional*. The radius of uncertainty for the location, measured in meters; 0-1500
     */
    @SerialName("horizontal_accuracy")
    public val horizontalAccuracy: Double? = null,
    /**
     * *Optional*. Time relative to the message sending date, during which the location can be updated; in seconds. For active live locations only.
     */
    @SerialName("live_period")
    public val livePeriod: Long? = null,
    /**
     * *Optional*. The direction in which user is moving, in degrees; 1-360. For active live locations only.
     */
    public val heading: Long? = null,
    /**
     * *Optional*. The maximum distance for proximity alerts about approaching another chat member, in meters. For sent live locations only.
     */
    @SerialName("proximity_alert_radius")
    public val proximityAlertRadius: Long? = null,
)
