// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object contains basic information about a successful payment. Note that if the buyer initiates a chargeback with the relevant payment provider following this transaction, the funds may be debited from your balance. This is outside of Telegram's control.
 */
@Serializable
public data class SuccessfulPayment(
    /**
     * Three-letter ISO 4217 [currency](https://core.telegram.org/bots/payments#supported-currencies) code, or “XTR” for payments in [Telegram Stars](https://t.me/BotNews/90)
     */
    public val currency: String,
    /**
     * Total price in the *smallest units* of the currency (integer, **not** float/double). For example, for a price of `US$ 1.45` pass `amount = 145`. See the *exp* parameter in [currencies.json](https://core.telegram.org/bots/payments/currencies.json), it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
     */
    @SerialName("total_amount")
    public val totalAmount: Long,
    /**
     * Bot-specified invoice payload
     */
    @SerialName("invoice_payload")
    public val invoicePayload: String,
    /**
     * *Optional*. Expiration date of the subscription, in Unix time; for recurring payments only
     */
    @SerialName("subscription_expiration_date")
    public val subscriptionExpirationDate: Long? = null,
    /**
     * *Optional*. *True*, if the payment is a recurring payment for a subscription
     */
    @SerialName("is_recurring")
    public val isRecurring: Boolean? = null,
    /**
     * *Optional*. *True*, if the payment is the first payment for a subscription
     */
    @SerialName("is_first_recurring")
    public val isFirstRecurring: Boolean? = null,
    /**
     * *Optional*. Identifier of the shipping option chosen by the user
     */
    @SerialName("shipping_option_id")
    public val shippingOptionId: String? = null,
    /**
     * *Optional*. Order information provided by the user
     */
    @SerialName("order_info")
    public val orderInfo: OrderInfo? = null,
    /**
     * Telegram payment identifier
     */
    @SerialName("telegram_payment_charge_id")
    public val telegramPaymentChargeId: String,
    /**
     * Provider payment identifier
     */
    @SerialName("provider_payment_charge_id")
    public val providerPaymentChargeId: String,
)
