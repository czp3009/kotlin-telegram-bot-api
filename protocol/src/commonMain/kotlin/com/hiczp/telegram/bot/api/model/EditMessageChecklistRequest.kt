// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public data class EditMessageChecklistRequest(
    /**
     * Unique identifier of the business connection on behalf of which the message will be sent
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String,
    /**
     * Unique identifier for the target chat
     */
    @SerialName("chat_id")
    public val chatId: Long,
    /**
     * Unique identifier for the target message
     */
    @SerialName("message_id")
    public val messageId: Long,
    /**
     * A JSON-serialized object for the new checklist
     */
    public val checklist: JsonElement?,
    /**
     * A JSON-serialized object for the new inline keyboard for the message
     */
    @SerialName("reply_markup")
    public val replyMarkup: JsonElement? = null,
)
