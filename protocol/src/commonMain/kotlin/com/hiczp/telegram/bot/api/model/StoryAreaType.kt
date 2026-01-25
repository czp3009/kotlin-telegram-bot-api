// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonElement

/**
 * Describes the type of a clickable area on a story. Currently, it can be one of
 * StoryAreaTypeLocation StoryAreaTypeSuggestedReaction StoryAreaTypeLink StoryAreaTypeWeather StoryAreaTypeUniqueGift
 */
@Serializable
@JsonClassDiscriminator("type")
@OptIn(ExperimentalSerializationApi::class)
public sealed interface StoryAreaType

/**
 * Describes a story area pointing to a location. Currently, a story can have up to 10 location areas.
 */
@Serializable
@SerialName("location")
public data class StoryAreaTypeLocation(
    /**
     * Location latitude in degrees
     */
    public val latitude: Double,
    /**
     * Location longitude in degrees
     */
    public val longitude: Double,
    /**
     * *Optional*. Address of the location
     */
    public val address: JsonElement? = null,
) : StoryAreaType

/**
 * Describes a story area pointing to a suggested reaction. Currently, a story can have up to 5 suggested reaction areas.
 */
@Serializable
@SerialName("suggested_reaction")
public data class StoryAreaTypeSuggestedReaction(
    /**
     * Type of the reaction
     */
    @SerialName("reaction_type")
    public val reactionType: JsonElement?,
    /**
     * *Optional*. Pass *True* if the reaction area has a dark background
     */
    @SerialName("is_dark")
    public val isDark: Boolean? = null,
    /**
     * *Optional*. Pass *True* if reaction area corner is flipped
     */
    @SerialName("is_flipped")
    public val isFlipped: Boolean? = null,
) : StoryAreaType

/**
 * Describes a story area pointing to an HTTP or tg:// link. Currently, a story can have up to 3 link areas.
 */
@Serializable
@SerialName("link")
public data class StoryAreaTypeLink(
    /**
     * HTTP or tg:// URL to be opened when the area is clicked
     */
    public val url: String,
) : StoryAreaType

/**
 * Describes a story area containing weather information. Currently, a story can have up to 3 weather areas.
 */
@Serializable
@SerialName("weather")
public data class StoryAreaTypeWeather(
    /**
     * Temperature, in degree Celsius
     */
    public val temperature: Double,
    /**
     * Emoji representing the weather
     */
    public val emoji: String,
    /**
     * A color of the area background in the ARGB format
     */
    @SerialName("background_color")
    public val backgroundColor: Long,
) : StoryAreaType

/**
 * Describes a story area pointing to a unique gift. Currently, a story can have at most 1 unique gift area.
 */
@Serializable
@SerialName("unique_gift")
public data class StoryAreaTypeUniqueGift(
    /**
     * Unique name of the gift
     */
    public val name: String,
) : StoryAreaType
