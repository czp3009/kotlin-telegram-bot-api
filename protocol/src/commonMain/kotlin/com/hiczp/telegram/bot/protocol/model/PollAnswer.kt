// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import com.hiczp.telegram.bot.protocol.type.IncomingUpdate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    public val voterChat: Chat? = null,
    /**
     * *Optional*. The user that changed the answer to the poll, if the voter isn't anonymous
     */
    public val user: User? = null,
    /**
     * 0-based identifiers of chosen answer options. May be empty if the vote was retracted.
     */
    @SerialName("option_ids")
    public val optionIds: List<Long>,
) : IncomingUpdate
