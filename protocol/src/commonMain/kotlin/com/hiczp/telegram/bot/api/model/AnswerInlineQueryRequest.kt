// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class AnswerInlineQueryRequest(
    /**
     * Unique identifier for the answered query
     */
    @SerialName("inline_query_id")
    public val inlineQueryId: String,
    /**
     * A JSON-serialized array of results for the inline query
     */
    public val results: List<InlineQueryResult>,
    /**
     * The maximum amount of time in seconds that the result of the inline query may be cached on the server. Defaults to 300.
     */
    @SerialName("cache_time")
    public val cacheTime: Long? = null,
    /**
     * Pass *True* if results may be cached on the server side only for the user that sent the query. By default, results may be returned to any user who sends the same query.
     */
    @SerialName("is_personal")
    public val isPersonal: Boolean? = null,
    /**
     * Pass the offset that a client should send in the next query with the same text to receive more results. Pass an empty string if there are no more results or if you don't support pagination. Offset length can't exceed 64 bytes.
     */
    @SerialName("next_offset")
    public val nextOffset: String? = null,
    /**
     * A JSON-serialized object describing a button to be shown above inline query results
     */
    public val button: InlineQueryResultsButton? = null,
)
