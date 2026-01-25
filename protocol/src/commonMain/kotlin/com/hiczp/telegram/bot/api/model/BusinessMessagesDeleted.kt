// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * This object is received when messages are deleted from a connected business account.
 */
@Serializable
public data class BusinessMessagesDeleted(
    /**
     * Unique identifier of the business connection
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String,
    /**
     * Information about a chat in the business account. The bot may not have access to the chat or the corresponding user.
     */
    public val chat: JsonElement?,
    /**
     * The list of identifiers of deleted messages in the chat of the business account
     */
    @SerialName("message_ids")
    public val messageIds: List<Long>,
)
