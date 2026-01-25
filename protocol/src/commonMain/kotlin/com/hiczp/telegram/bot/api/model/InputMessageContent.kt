// Auto-generated from Swagger specificationDo not modify this file manuallyWARNING: This sealed interface does not have a clear discriminator field
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * This object represents the content of a message to be sent as a result of an inline query. Telegram clients currently support the following 5 types:
 * InputTextMessageContent InputLocationMessageContent InputVenueMessageContent InputContactMessageContent InputInvoiceMessageContent
 */
@Serializable
public sealed interface InputMessageContent

/**
 * Represents the content of a text message to be sent as the result of an inline query.
 */
@Serializable
public data class InputTextMessageContent(
    /**
     * Text of the message to be sent, 1-4096 characters
     */
    @SerialName("message_text")
    public val messageText: String,
    /**
     * *Optional*. Mode for parsing entities in the message text. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
     */
    @SerialName("parse_mode")
    public val parseMode: String? = null,
    /**
     * *Optional*. List of special entities that appear in message text, which can be specified instead of *parse_mode*
     */
    public val entities: List<MessageEntity>? = null,
    /**
     * *Optional*. Link preview generation options for the message
     */
    @SerialName("link_preview_options")
    public val linkPreviewOptions: JsonElement? = null,
) : InputMessageContent

/**
 * Represents the content of a location message to be sent as the result of an inline query.
 */
@Serializable
public data class InputLocationMessageContent(
    /**
     * Latitude of the location in degrees
     */
    public val latitude: Double,
    /**
     * Longitude of the location in degrees
     */
    public val longitude: Double,
    /**
     * *Optional*. The radius of uncertainty for the location, measured in meters; 0-1500
     */
    @SerialName("horizontal_accuracy")
    public val horizontalAccuracy: Double? = null,
    /**
     * *Optional*. Period in seconds during which the location can be updated, should be between 60 and 86400, or 0x7FFFFFFF for live locations that can be edited indefinitely.
     */
    @SerialName("live_period")
    public val livePeriod: Long? = null,
    /**
     * *Optional*. For live locations, a direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
     */
    public val heading: Long? = null,
    /**
     * *Optional*. For live locations, a maximum distance for proximity alerts about approaching another chat member, in meters. Must be between 1 and 100000 if specified.
     */
    @SerialName("proximity_alert_radius")
    public val proximityAlertRadius: Long? = null,
) : InputMessageContent

/**
 * Represents the content of a venue message to be sent as the result of an inline query.
 */
@Serializable
public data class InputVenueMessageContent(
    /**
     * Latitude of the venue in degrees
     */
    public val latitude: Double,
    /**
     * Longitude of the venue in degrees
     */
    public val longitude: Double,
    /**
     * Name of the venue
     */
    public val title: String,
    /**
     * Address of the venue
     */
    public val address: String,
    /**
     * *Optional*. Foursquare identifier of the venue, if known
     */
    @SerialName("foursquare_id")
    public val foursquareId: String? = null,
    /**
     * *Optional*. Foursquare type of the venue, if known. (For example, “arts_entertainment/default”, “arts_entertainment/aquarium” or “food/icecream”.)
     */
    @SerialName("foursquare_type")
    public val foursquareType: String? = null,
    /**
     * *Optional*. Google Places identifier of the venue
     */
    @SerialName("google_place_id")
    public val googlePlaceId: String? = null,
    /**
     * *Optional*. Google Places type of the venue. (See [supported types](https://developers.google.com/places/web-service/supported_types).)
     */
    @SerialName("google_place_type")
    public val googlePlaceType: String? = null,
) : InputMessageContent

/**
 * Represents the content of a contact message to be sent as the result of an inline query.
 */
@Serializable
public data class InputContactMessageContent(
    /**
     * Contact's phone number
     */
    @SerialName("phone_number")
    public val phoneNumber: String,
    /**
     * Contact's first name
     */
    @SerialName("first_name")
    public val firstName: String,
    /**
     * *Optional*. Contact's last name
     */
    @SerialName("last_name")
    public val lastName: String? = null,
    /**
     * *Optional*. Additional data about the contact in the form of a [vCard](https://en.wikipedia.org/wiki/VCard), 0-2048 bytes
     */
    public val vcard: String? = null,
) : InputMessageContent

/**
 * Represents the content of an invoice message to be sent as the result of an inline query.
 */
@Serializable
public data class InputInvoiceMessageContent(
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
     * *Optional*. Payment provider token, obtained via [@BotFather](https://t.me/botfather). Pass an empty string for payments in [Telegram Stars](https://t.me/BotNews/90).
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
     * *Optional*. The maximum accepted amount for tips in the *smallest units* of the currency (integer, **not** float/double). For example, for a maximum tip of `US$ 1.45` pass `max_tip_amount = 145`. See the *exp* parameter in [currencies.json](https://core.telegram.org/bots/payments/currencies.json), it shows the number of digits past the decimal point for each currency (2 for the majority of currencies). Defaults to 0. Not supported for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    @SerialName("max_tip_amount")
    public val maxTipAmount: Long? = null,
    /**
     * *Optional*. A JSON-serialized array of suggested amounts of tip in the *smallest units* of the currency (integer, **not** float/double). At most 4 suggested tip amounts can be specified. The suggested tip amounts must be positive, passed in a strictly increased order and must not exceed *max_tip_amount*.
     */
    @SerialName("suggested_tip_amounts")
    public val suggestedTipAmounts: List<Long>? = null,
    /**
     * *Optional*. A JSON-serialized object for data about the invoice, which will be shared with the payment provider. A detailed description of the required fields should be provided by the payment provider.
     */
    @SerialName("provider_data")
    public val providerData: String? = null,
    /**
     * *Optional*. URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service.
     */
    @SerialName("photo_url")
    public val photoUrl: String? = null,
    /**
     * *Optional*. Photo size in bytes
     */
    @SerialName("photo_size")
    public val photoSize: Long? = null,
    /**
     * *Optional*. Photo width
     */
    @SerialName("photo_width")
    public val photoWidth: Long? = null,
    /**
     * *Optional*. Photo height
     */
    @SerialName("photo_height")
    public val photoHeight: Long? = null,
    /**
     * *Optional*. Pass *True* if you require the user's full name to complete the order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    @SerialName("need_name")
    public val needName: Boolean? = null,
    /**
     * *Optional*. Pass *True* if you require the user's phone number to complete the order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    @SerialName("need_phone_number")
    public val needPhoneNumber: Boolean? = null,
    /**
     * *Optional*. Pass *True* if you require the user's email address to complete the order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    @SerialName("need_email")
    public val needEmail: Boolean? = null,
    /**
     * *Optional*. Pass *True* if you require the user's shipping address to complete the order. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    @SerialName("need_shipping_address")
    public val needShippingAddress: Boolean? = null,
    /**
     * *Optional*. Pass *True* if the user's phone number should be sent to the provider. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    @SerialName("send_phone_number_to_provider")
    public val sendPhoneNumberToProvider: Boolean? = null,
    /**
     * *Optional*. Pass *True* if the user's email address should be sent to the provider. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    @SerialName("send_email_to_provider")
    public val sendEmailToProvider: Boolean? = null,
    /**
     * *Optional*. Pass *True* if the final price depends on the shipping method. Ignored for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    @SerialName("is_flexible")
    public val isFlexible: Boolean? = null,
) : InputMessageContent
