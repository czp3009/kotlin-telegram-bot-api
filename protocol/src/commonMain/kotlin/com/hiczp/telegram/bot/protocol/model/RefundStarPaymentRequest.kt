// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RefundStarPaymentRequest(
    /**
     * Identifier of the user whose payment will be refunded
     */
    @SerialName("user_id")
    public val userId: Long,
    /**
     * Telegram payment identifier
     */
    @SerialName("telegram_payment_charge_id")
    public val telegramPaymentChargeId: String,
)
