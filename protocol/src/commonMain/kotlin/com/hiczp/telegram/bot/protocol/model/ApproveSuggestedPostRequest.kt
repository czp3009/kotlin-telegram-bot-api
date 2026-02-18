// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ApproveSuggestedPostRequest(
    /**
     * Unique identifier for the target direct messages chat
     */
    @SerialName("chat_id")
    public val chatId: Long,
    /**
     * Identifier of a suggested post message to approve
     */
    @SerialName("message_id")
    public val messageId: Long,
    /**
     * Point in time (Unix timestamp) when the post is expected to be published; omit if the date has already been specified when the suggested post was created. If specified, then the date must be not more than 2678400 seconds (30 days) in the future
     */
    @SerialName("send_date")
    public val sendDate: Long? = null,
)
