// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import com.hiczp.telegram.bot.protocol.annotation.IncomingUpdate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents a boost removed from a chat.
 */
@Serializable
public data class ChatBoostRemoved(
    /**
     * Chat which was boosted
     */
    public val chat: Chat,
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
    public val source: ChatBoostSource,
) : IncomingUpdate
