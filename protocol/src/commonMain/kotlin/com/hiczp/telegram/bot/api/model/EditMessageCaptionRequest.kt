// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class EditMessageCaptionRequest(
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
     * New caption of the message, 0-1024 characters after entities parsing
     */
    public val caption: String? = null,
    /**
     * Mode for parsing entities in the message caption. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
     */
    @SerialName("parse_mode")
    public val parseMode: String? = null,
    /**
     * A JSON-serialized list of special entities that appear in the caption, which can be specified instead of *parse_mode*
     */
    @SerialName("caption_entities")
    public val captionEntities: List<MessageEntity>? = null,
    /**
     * Pass *True*, if the caption must be shown above the message media. Supported only for animation, photo and video messages.
     */
    @SerialName("show_caption_above_media")
    public val showCaptionAboveMedia: Boolean? = null,
    /**
     * A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
)
