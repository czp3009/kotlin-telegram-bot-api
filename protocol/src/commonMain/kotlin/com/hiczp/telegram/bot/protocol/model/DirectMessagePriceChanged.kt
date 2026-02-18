// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Boolean
import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes a service message about a change in the price of direct messages sent to a channel chat.
 */
@Serializable
public data class DirectMessagePriceChanged(
    /**
     * *True*, if direct messages are enabled for the channel chat; false otherwise
     */
    @SerialName("are_direct_messages_enabled")
    public val areDirectMessagesEnabled: Boolean,
    /**
     * *Optional*. The new number of Telegram Stars that must be paid by users for each direct message sent to the channel. Does not apply to users who have been exempted by administrators. Defaults to 0.
     */
    @SerialName("direct_message_star_count")
    public val directMessageStarCount: Long? = null,
)
