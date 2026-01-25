// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class DeleteBusinessMessagesRequest(
    /**
     * Unique identifier of the business connection on behalf of which to delete the messages
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String,
    /**
     * A JSON-serialized list of 1-100 identifiers of messages to delete. All messages must be from the same chat. See [deleteMessage](https://core.telegram.org/bots/api#deletemessage) for limitations on which messages can be deleted
     */
    @SerialName("message_ids")
    public val messageIds: List<Long>,
)
