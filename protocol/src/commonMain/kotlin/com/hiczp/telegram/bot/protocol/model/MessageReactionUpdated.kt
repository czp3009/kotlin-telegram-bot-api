// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import com.hiczp.telegram.bot.protocol.`annotation`.IncomingUpdate
import kotlin.Long
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents a change of a reaction on a message performed by a user.
 */
@Serializable
public data class MessageReactionUpdated(
    /**
     * The chat containing the message the user reacted to
     */
    public val chat: Chat,
    /**
     * Unique identifier of the message inside the chat
     */
    @SerialName("message_id")
    public val messageId: Long,
    /**
     * *Optional*. The user that changed the reaction, if the user isn't anonymous
     */
    public val user: User? = null,
    /**
     * *Optional*. The chat on behalf of which the reaction was changed, if the user is anonymous
     */
    @SerialName("actor_chat")
    public val actorChat: Chat? = null,
    /**
     * Date of the change in Unix time
     */
    public val date: Long,
    /**
     * Previous list of reaction types that were set by the user
     */
    @SerialName("old_reaction")
    public val oldReaction: List<ReactionType>,
    /**
     * New list of reaction types that have been set by the user
     */
    @SerialName("new_reaction")
    public val newReaction: List<ReactionType>,
) : IncomingUpdate
