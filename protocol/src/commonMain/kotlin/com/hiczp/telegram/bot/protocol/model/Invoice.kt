// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object contains basic information about an invoice.
 */
@Serializable
public data class Invoice(
    /**
     * Product name
     */
    public val title: String,
    /**
     * Product description
     */
    public val description: String,
    /**
     * Unique bot deep-linking parameter that can be used to generate this invoice
     */
    @SerialName("start_parameter")
    public val startParameter: String,
    /**
     * Three-letter ISO 4217 [currency](https://core.telegram.org/bots/payments#supported-currencies) code, or “XTR” for payments in [Telegram Stars](https://t.me/BotNews/90)
     */
    public val currency: String,
    /**
     * Total price in the *smallest units* of the currency (integer, **not** float/double). For example, for a price of `US$ 1.45` pass `amount = 145`. See the *exp* parameter in [currencies.json](https://core.telegram.org/bots/payments/currencies.json), it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
     */
    @SerialName("total_amount")
    public val totalAmount: Long,
)
