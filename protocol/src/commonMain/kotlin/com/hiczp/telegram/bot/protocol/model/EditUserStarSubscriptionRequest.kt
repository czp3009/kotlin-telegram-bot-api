// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class EditUserStarSubscriptionRequest(
    /**
     * Identifier of the user whose subscription will be edited
     */
    @SerialName("user_id")
    public val userId: Long,
    /**
     * Telegram payment identifier for the subscription
     */
    @SerialName("telegram_payment_charge_id")
    public val telegramPaymentChargeId: String,
    /**
     * Pass *True* to cancel extension of the user subscription; the subscription must be active up to the end of the current subscription period. Pass *False* to allow the user to re-enable a subscription that was previously canceled by the bot.
     */
    @SerialName("is_canceled")
    public val isCanceled: Boolean,
)
