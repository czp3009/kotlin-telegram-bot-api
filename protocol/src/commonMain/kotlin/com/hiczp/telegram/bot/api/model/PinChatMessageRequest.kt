// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class PinChatMessageRequest(
    /**
     * Unique identifier of the business connection on behalf of which the message will be pinned
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String? = null,
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    @SerialName("chat_id")
    public val chatId: String,
    /**
     * Identifier of a message to pin
     */
    @SerialName("message_id")
    public val messageId: Long,
    /**
     * Pass *True* if it is not necessary to send a notification to all chat members about the new pinned message. Notifications are always disabled in channels and private chats.
     */
    @SerialName("disable_notification")
    public val disableNotification: Boolean? = null,
)
