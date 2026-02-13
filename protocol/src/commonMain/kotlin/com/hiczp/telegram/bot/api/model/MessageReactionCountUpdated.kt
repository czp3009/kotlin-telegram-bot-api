// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.model

import com.hiczp.telegram.bot.api.type.IncomingUpdate
import kotlin.Long
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents reaction changes on a message with anonymous reactions.
 */
@Serializable
public data class MessageReactionCountUpdated(
    /**
     * The chat containing the message
     */
    public val chat: Chat,
    /**
     * Unique message identifier inside the chat
     */
    @SerialName("message_id")
    public val messageId: Long,
    /**
     * Date of the change in Unix time
     */
    public val date: Long,
    /**
     * List of reactions that are present on the message
     */
    public val reactions: List<ReactionCount>,
) : IncomingUpdate
