// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object describes the rating of a user based on their Telegram Star spendings.
 */
@Serializable
public data class UserRating(
    /**
     * Current level of the user, indicating their reliability when purchasing digital goods and services. A higher level suggests a more trustworthy customer; a negative level is likely reason for concern.
     */
    public val level: Long,
    /**
     * Numerical value of the user's rating; the higher the rating, the better
     */
    public val rating: Long,
    /**
     * The rating value required to get the current level
     */
    @SerialName("current_level_rating")
    public val currentLevelRating: Long,
    /**
     * *Optional*. The rating value required to get to the next level; omitted if the maximum level was reached
     */
    @SerialName("next_level_rating")
    public val nextLevelRating: Long? = null,
)
