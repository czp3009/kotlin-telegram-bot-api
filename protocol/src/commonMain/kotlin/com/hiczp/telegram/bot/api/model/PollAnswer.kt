// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * This object represents an answer of a user in a non-anonymous poll.
 */
@Serializable
public data class PollAnswer(
    /**
     * Unique poll identifier
     */
    @SerialName("poll_id")
    public val pollId: String,
    /**
     * *Optional*. The chat that changed the answer to the poll, if the voter is anonymous
     */
    @SerialName("voter_chat")
    public val voterChat: JsonElement? = null,
    /**
     * *Optional*. The user that changed the answer to the poll, if the voter isn't anonymous
     */
    public val user: JsonElement? = null,
    /**
     * 0-based identifiers of chosen answer options. May be empty if the vote was retracted.
     */
    @SerialName("option_ids")
    public val optionIds: List<Long>,
)
