// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class AnswerChatJoinRequestQueryRequest(
    /**
     * Unique identifier of the join request query
     */
    @SerialName("chat_join_request_query_id")
    public val chatJoinRequestQueryId: String,
    /**
     * Result of the query. Must be either “approve” to allow the user to join the chat, “decline” to disallow the user to join the chat, or “queue” to leave the decision to other administrators.
     */
    public val result: String,
)
