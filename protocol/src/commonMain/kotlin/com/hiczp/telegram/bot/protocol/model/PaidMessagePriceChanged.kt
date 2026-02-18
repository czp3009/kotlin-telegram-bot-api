// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes a service message about a change in the price of paid messages within a chat.
 */
@Serializable
public data class PaidMessagePriceChanged(
    /**
     * The new number of Telegram Stars that must be paid by non-administrator users of the supergroup chat for each sent message
     */
    @SerialName("paid_message_star_count")
    public val paidMessageStarCount: Long,
)
