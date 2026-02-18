// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SendChecklistRequest(
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
     * A JSON-serialized object for the checklist to send
     */
    public val checklist: InputChecklist,
    /**
     * Sends the message silently. Users will receive a notification with no sound.
     */
    @SerialName("disable_notification")
    public val disableNotification: Boolean? = null,
    /**
     * Protects the contents of the sent message from forwarding and saving
     */
    @SerialName("protect_content")
    public val protectContent: Boolean? = null,
    /**
     * Unique identifier of the message effect to be added to the message
     */
    @SerialName("message_effect_id")
    public val messageEffectId: String? = null,
    /**
     * A JSON-serialized object for description of the message to reply to
     */
    @SerialName("reply_parameters")
    public val replyParameters: ReplyParameters? = null,
    /**
     * A JSON-serialized object for an inline keyboard
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
)
