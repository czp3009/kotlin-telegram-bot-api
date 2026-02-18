// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object contains information about a chat boost.
 */
@Serializable
public data class ChatBoost(
    /**
     * Unique identifier of the boost
     */
    @SerialName("boost_id")
    public val boostId: String,
    /**
     * Point in time (Unix timestamp) when the chat was boosted
     */
    @SerialName("add_date")
    public val addDate: Long,
    /**
     * Point in time (Unix timestamp) when the boost will automatically expire, unless the booster's Telegram Premium subscription is prolonged
     */
    @SerialName("expiration_date")
    public val expirationDate: Long,
    /**
     * Source of the added boost
     */
    public val source: ChatBoostSource,
)
