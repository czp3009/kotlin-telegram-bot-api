// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class AnswerWebAppQueryRequest(
    /**
     * Unique identifier for the query to be answered
     */
    @SerialName("web_app_query_id")
    public val webAppQueryId: String,
    /**
     * A JSON-serialized object describing the message to be sent
     */
    public val result: InlineQueryResult,
)
