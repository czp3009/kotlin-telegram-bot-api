// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.form

import com.hiczp.telegram.bot.protocol.model.InlineKeyboardMarkup
import com.hiczp.telegram.bot.protocol.model.InputMedia
import io.ktor.client.request.forms.*
import kotlinx.serialization.SerialName

public data class EditMessageMediaForm(
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
     * A JSON-serialized object for a new media content of the message
     */
    public val media: InputMedia,
    /**
     * A JSON-serialized object for a new [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
    /**
     * Additional file attachments referenced via attach://<file_attach_name> in media fields
     */
    public val attachments: List<FormPart<ChannelProvider>>? = null,
)
