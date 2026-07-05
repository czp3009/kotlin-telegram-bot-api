// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

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
     * Unique identifier of the option, persistent on option addition and deletion
     */
    @SerialName("persistent_id")
    public val persistentId: String,
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
     * *Optional*. Media added to the poll option
     */
    public val media: PollMedia? = null,
    /**
     * Number of users who voted for this option; may be 0 if unknown
     */
    @SerialName("voter_count")
    public val voterCount: Long,
    /**
     * *Optional*. User who added the option; omitted if the option wasn't added by a user after poll creation
     */
    @SerialName("added_by_user")
    public val addedByUser: User? = null,
    /**
     * *Optional*. Chat that added the option; omitted if the option wasn't added by a chat after poll creation
     */
    @SerialName("added_by_chat")
    public val addedByChat: Chat? = null,
    /**
     * *Optional*. Point in time (Unix timestamp) when the option was added; omitted if the option existed in the original poll
     */
    @SerialName("addition_date")
    public val additionDate: Long? = null,
)
