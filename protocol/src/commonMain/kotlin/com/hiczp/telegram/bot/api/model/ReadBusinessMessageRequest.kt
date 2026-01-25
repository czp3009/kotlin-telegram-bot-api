// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ReadBusinessMessageRequest(
    /**
     * Unique identifier of the business connection on behalf of which to read the message
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String,
    /**
     * Unique identifier of the chat in which the message was received. The chat must have been active in the last 24 hours.
     */
    @SerialName("chat_id")
    public val chatId: Long,
    /**
     * Unique identifier of the message to mark as read
     */
    @SerialName("message_id")
    public val messageId: Long,
)
