// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes a service message about an option deleted from a poll.
 */
@Serializable
public data class PollOptionDeleted(
    /**
     * *Optional*. Message containing the poll from which the option was deleted, if known. Note that the [Message](https://core.telegram.org/bots/api#message) object in this field will not contain the *reply_to_message* field even if it itself is a reply.
     */
    @SerialName("poll_message")
    public val pollMessage: Message? = null,
    /**
     * Unique identifier of the deleted option
     */
    @SerialName("option_persistent_id")
    public val optionPersistentId: String,
    /**
     * Option text
     */
    @SerialName("option_text")
    public val optionText: String,
    /**
     * *Optional*. Special entities that appear in the *option_text*
     */
    @SerialName("option_text_entities")
    public val optionTextEntities: List<MessageEntity>? = null,
)
