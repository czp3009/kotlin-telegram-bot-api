// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a reaction added to a message along with the number of times it was added.
 */
@Serializable
public data class ReactionCount(
    /**
     * Type of the reaction
     */
    public val type: ReactionType,
    /**
     * Number of times the reaction was added
     */
    @SerialName("total_count")
    public val totalCount: Long,
)
