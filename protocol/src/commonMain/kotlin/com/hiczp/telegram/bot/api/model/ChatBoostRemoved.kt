// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * This object represents a boost removed from a chat.
 */
@Serializable
public data class ChatBoostRemoved(
    /**
     * Chat which was boosted
     */
    public val chat: JsonElement?,
    /**
     * Unique identifier of the boost
     */
    @SerialName("boost_id")
    public val boostId: String,
    /**
     * Point in time (Unix timestamp) when the boost was removed
     */
    @SerialName("remove_date")
    public val removeDate: Long,
    /**
     * Source of the removed boost
     */
    public val source: JsonElement?,
)
