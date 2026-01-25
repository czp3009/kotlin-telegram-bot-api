// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public data class EditMessageTextRequest(
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
     * New text of the message, 1-4096 characters after entities parsing
     */
    public val text: String,
    /**
     * Mode for parsing entities in the message text. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
     */
    @SerialName("parse_mode")
    public val parseMode: String? = null,
    /**
     * A JSON-serialized list of special entities that appear in message text, which can be specified instead of *parse_mode*
     */
    public val entities: List<MessageEntity>? = null,
    /**
     * Link preview generation options for the message
     */
    @SerialName("link_preview_options")
    public val linkPreviewOptions: JsonElement? = null,
    /**
     * A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    @SerialName("reply_markup")
    public val replyMarkup: JsonElement? = null,
)
