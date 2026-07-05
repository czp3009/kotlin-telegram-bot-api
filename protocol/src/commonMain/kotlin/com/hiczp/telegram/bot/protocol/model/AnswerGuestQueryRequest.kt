// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class AnswerGuestQueryRequest(
    /**
     * Unique identifier for the query to be answered
     */
    @SerialName("guest_query_id")
    public val guestQueryId: String,
    /**
     * A JSON-serialized object describing the message to be sent
     */
    public val result: InlineQueryResult,
)
