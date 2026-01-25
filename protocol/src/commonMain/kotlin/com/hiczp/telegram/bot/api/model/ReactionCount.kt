// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Represents a reaction added to a message along with the number of times it was added.
 */
@Serializable
public data class ReactionCount(
    /**
     * Type of the reaction
     */
    public val type: JsonElement?,
    /**
     * Number of times the reaction was added
     */
    @SerialName("total_count")
    public val totalCount: Long,
)
