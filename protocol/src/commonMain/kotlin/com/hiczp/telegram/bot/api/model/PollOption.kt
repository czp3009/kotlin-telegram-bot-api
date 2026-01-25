// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object contains information about one answer option in a poll.
 */
@Serializable
public data class PollOption(
    /**
     * Option text, 1-100 characters
     */
    public val text: String,
    /**
     * *Optional*. Special entities that appear in the option *text*. Currently, only custom emoji entities are allowed in poll option texts
     */
    @SerialName("text_entities")
    public val textEntities: List<MessageEntity>? = null,
    /**
     * Number of users that voted for this option
     */
    @SerialName("voter_count")
    public val voterCount: Long,
)
