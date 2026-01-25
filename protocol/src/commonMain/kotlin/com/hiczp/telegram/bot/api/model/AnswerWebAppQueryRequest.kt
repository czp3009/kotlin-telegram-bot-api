// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

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
    public val result: JsonElement?,
)
