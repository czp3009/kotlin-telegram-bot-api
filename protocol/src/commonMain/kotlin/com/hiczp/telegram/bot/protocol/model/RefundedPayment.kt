// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object contains basic information about a refunded payment.
 */
@Serializable
public data class RefundedPayment(
    /**
     * Three-letter ISO 4217 [currency](https://core.telegram.org/bots/payments#supported-currencies) code, or “XTR” for payments in [Telegram Stars](https://t.me/BotNews/90). Currently, always “XTR”
     */
    public val currency: String,
    /**
     * Total refunded price in the *smallest units* of the currency (integer, **not** float/double). For example, for a price of `US$ 1.45`, `total_amount = 145`. See the *exp* parameter in [currencies.json](https://core.telegram.org/bots/payments/currencies.json), it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
     */
    @SerialName("total_amount")
    public val totalAmount: Long,
    /**
     * Bot-specified invoice payload
     */
    @SerialName("invoice_payload")
    public val invoicePayload: String,
    /**
     * Telegram payment identifier
     */
    @SerialName("telegram_payment_charge_id")
    public val telegramPaymentChargeId: String,
    /**
     * *Optional*. Provider payment identifier
     */
    @SerialName("provider_payment_charge_id")
    public val providerPaymentChargeId: String? = null,
)
