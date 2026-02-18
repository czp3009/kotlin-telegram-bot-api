// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes a service message about a successful payment for a suggested post.
 */
@Serializable
public data class SuggestedPostPaid(
    /**
     * *Optional*. Message containing the suggested post. Note that the [Message](https://core.telegram.org/bots/api#message) object in this field will not contain the *reply_to_message* field even if it itself is a reply.
     */
    @SerialName("suggested_post_message")
    public val suggestedPostMessage: Message? = null,
    /**
     * Currency in which the payment was made. Currently, one of “XTR” for Telegram Stars or “TON” for toncoins
     */
    public val currency: String,
    /**
     * *Optional*. The amount of the currency that was received by the channel in nanotoncoins; for payments in toncoins only
     */
    public val amount: Long? = null,
    /**
     * *Optional*. The amount of Telegram Stars that was received by the channel; for payments in Telegram Stars only
     */
    @SerialName("star_amount")
    public val starAmount: StarAmount? = null,
)
