// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public data class EditMessageReplyMarkupRequest(
    /**
     * Unique identifier of the business connection on behalf of which the message to be edited was sent
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String? = null,
    /**
     * Required if *inline_message_id* is not specified. Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    @SerialName("chat_id")
    public val chatId: String? = null,
    /**
     * Required if *inline_message_id* is not specified. Identifier of the message to edit
     */
    @SerialName("message_id")
    public val messageId: Long? = null,
    /**
     * Required if *chat_id* and *message_id* are not specified. Identifier of the inline message
     */
    @SerialName("inline_message_id")
    public val inlineMessageId: String? = null,
    /**
     * A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    @SerialName("reply_markup")
    public val replyMarkup: JsonElement? = null,
)
