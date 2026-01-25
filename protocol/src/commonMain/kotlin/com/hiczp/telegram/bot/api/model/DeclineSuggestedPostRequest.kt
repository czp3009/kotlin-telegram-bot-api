// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class DeclineSuggestedPostRequest(
    /**
     * Unique identifier for the target direct messages chat
     */
    @SerialName("chat_id")
    public val chatId: Long,
    /**
     * Identifier of a suggested post message to decline
     */
    @SerialName("message_id")
    public val messageId: Long,
    /**
     * Comment for the creator of the suggested post; 0-128 characters
     */
    public val comment: String? = null,
)
