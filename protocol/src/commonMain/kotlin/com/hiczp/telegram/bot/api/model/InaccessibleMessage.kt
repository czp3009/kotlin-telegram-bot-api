// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object describes a message that was deleted or is otherwise inaccessible to the bot.
 */
@Serializable
public data class InaccessibleMessage(
    /**
     * Chat the message belonged to
     */
    public val chat: Chat,
    /**
     * Unique message identifier inside the chat
     */
    @SerialName("message_id")
    public val messageId: Long,
    /**
     * Always 0. The field can be used to differentiate regular and inaccessible messages.
     */
    public val date: Long,
)
