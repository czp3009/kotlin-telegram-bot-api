// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * This object contains information about an incoming pre-checkout query.
 */
@Serializable
public data class PreCheckoutQuery(
    /**
     * Unique query identifier
     */
    public val id: String,
    /**
     * User who sent the query
     */
    public val from: JsonElement?,
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
     * *Optional*. Identifier of the shipping option chosen by the user
     */
    @SerialName("shipping_option_id")
    public val shippingOptionId: String? = null,
    /**
     * *Optional*. Order information provided by the user
     */
    @SerialName("order_info")
    public val orderInfo: JsonElement? = null,
)
