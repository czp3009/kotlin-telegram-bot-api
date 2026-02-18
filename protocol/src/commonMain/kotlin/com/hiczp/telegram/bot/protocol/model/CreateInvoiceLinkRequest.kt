// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CreateInvoiceLinkRequest(
    /**
     * Unique identifier of the business connection on behalf of which the link will be created. For payments in [Telegram Stars](https://t.me/BotNews/90) only.
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String? = null,
    /**
     * Product name, 1-32 characters
     */
    public val title: String,
    /**
     * Product description, 1-255 characters
     */
    public val description: String,
    /**
     * Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user, use it for your internal processes.
     */
    public val payload: String,
    /**
     * Payment provider token, obtained via [@BotFather](https://t.me/botfather). Pass an empty string for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    @SerialName("provider_token")
    public val providerToken: String? = null,
    /**
     * Three-letter ISO 4217 currency code, see [more on currencies](https://core.telegram.org/bots/payments#supported-currencies). Pass “XTR” for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    public val currency: String,
    /**
     * Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount, delivery cost, delivery tax, bonus, etc.). Must contain exactly one item for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    public val prices: List<LabeledPrice>,
    /**
     * The number of seconds the subscription will be active for before the next payment. The currency must be set to “XTR” (Telegram Stars) if the parameter is used. Currently, it must always be 2592000 (30 days) if specified. Any number of subscriptions can be active for a given bot at the same time, including multiple concurrent subscriptions from the same user. Subscription price must no exceed 10000 Telegram Stars.
     */
    @SerialName("subscription_period")
    public val subscriptionPeriod: Long? = null,
    /**
     * The maximum accepted amount for tips in the *smallest units* of the currency (integer, **not** float/double). For example, for a maximum tip of `US$ 1.45` pass `max_tip_amount = 145`. See the *exp* parameter in [currencies.json](https://core.telegram.org/bots/payments/currencies.json), it shows the number of digits past the decimal point for each currency (2 for the majority of currencies). Defaults to 0. Not supported for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    @SerialName("max_tip_amount")
    public val maxTipAmount: Long? = null,
    /**
     * A JSON-serialized array of suggested amounts of tips in the *smallest units* of the currency (integer, **not** float/double). At most 4 suggested tip amounts can be specified. The suggested tip amounts must be positive, passed in a strictly increased order and must not exceed *max_tip_amount*.
     */
    @SerialName("suggested_tip_amounts")
    public val suggestedTipAmounts: List<Long>? = null,
    /**
     * JSON-serialized data about the invoice, which will be shared with the payment provider. A detailed description of required fields should be provided by the payment provider.
     */
    @SerialName("provider_data")
    public val providerData: String? = null,
    /**
     * URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service.
     */
    @SerialName("photo_url")
    public val photoUrl: String? = null,
    /**
     * Photo size in bytes
     */
    @SerialName("photo_size")
    public val photoSize: Long? = null,
    /**
     * Photo width
     */
    @SerialName("photo_width")
    public val photoWidth: Long? = null,
    /**
     * Photo height
     */
    @SerialName("photo_height")
    public val photoHeight: Long? = null,
    /**
     * Pass *True* if you require the user's full name to complete the order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    @SerialName("need_name")
    public val needName: Boolean? = null,
    /**
     * Pass *True* if you require the user's phone number to complete the order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    @SerialName("need_phone_number")
    public val needPhoneNumber: Boolean? = null,
    /**
     * Pass *True* if you require the user's email address to complete the order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    @SerialName("need_email")
    public val needEmail: Boolean? = null,
    /**
     * Pass *True* if you require the user's shipping address to complete the order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    @SerialName("need_shipping_address")
    public val needShippingAddress: Boolean? = null,
    /**
     * Pass *True* if the user's phone number should be sent to the provider. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    @SerialName("send_phone_number_to_provider")
    public val sendPhoneNumberToProvider: Boolean? = null,
    /**
     * Pass *True* if the user's email address should be sent to the provider. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    @SerialName("send_email_to_provider")
    public val sendEmailToProvider: Boolean? = null,
    /**
     * Pass *True* if the final price depends on the shipping method. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    @SerialName("is_flexible")
    public val isFlexible: Boolean? = null,
)
