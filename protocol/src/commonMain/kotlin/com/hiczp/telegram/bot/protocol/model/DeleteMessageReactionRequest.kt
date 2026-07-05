// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class DeleteMessageReactionRequest(
    /**
     * Unique identifier for the target chat or username of the target supergroup in the format `@username`
     */
    @SerialName("chat_id")
    public val chatId: String,
    /**
     * Identifier of the target message
     */
    @SerialName("message_id")
    public val messageId: Long,
    /**
     * Identifier of the user whose reaction will be removed, if the reaction was added by a user
     */
    @SerialName("user_id")
    public val userId: Long? = null,
    /**
     * Identifier of the chat whose reaction will be removed, if the reaction was added by a chat
     */
    @SerialName("actor_chat_id")
    public val actorChatId: Long? = null,
)
