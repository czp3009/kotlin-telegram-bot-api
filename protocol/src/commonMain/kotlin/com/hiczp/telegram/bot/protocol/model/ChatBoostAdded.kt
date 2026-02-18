// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about a user boosting a chat.
 */
@Serializable
public data class ChatBoostAdded(
    /**
     * Number of boosts added by the user
     */
    @SerialName("boost_count")
    public val boostCount: Long,
)
