// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * This object represents a venue.
 */
@Serializable
public data class Venue(
    /**
     * Venue location. Can't be a live location
     */
    public val location: JsonElement?,
    /**
     * Name of the venue
     */
    public val title: String,
    /**
     * Address of the venue
     */
    public val address: String,
    /**
     * *Optional*. Foursquare identifier of the venue
     */
    @SerialName("foursquare_id")
    public val foursquareId: String? = null,
    /**
     * *Optional*. Foursquare type of the venue. (For example, “arts_entertainment/default”, “arts_entertainment/aquarium” or “food/icecream”.)
     */
    @SerialName("foursquare_type")
    public val foursquareType: String? = null,
    /**
     * *Optional*. Google Places identifier of the venue
     */
    @SerialName("google_place_id")
    public val googlePlaceId: String? = null,
    /**
     * *Optional*. Google Places type of the venue. (See [supported types](https://developers.google.com/places/web-service/supported_types).)
     */
    @SerialName("google_place_type")
    public val googlePlaceType: String? = null,
)
