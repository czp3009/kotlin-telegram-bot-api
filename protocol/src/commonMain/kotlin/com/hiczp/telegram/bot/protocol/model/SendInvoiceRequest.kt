// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SendInvoiceRequest(
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    @SerialName("chat_id")
    public val chatId: String,
    /**
     * Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
     */
    @SerialName("message_thread_id")
    public val messageThreadId: Long? = null,
    /**
     * Identifier of the direct messages topic to which the message will be sent; required if the message is sent to a direct messages chat
     */
    @SerialName("direct_messages_topic_id")
    public val directMessagesTopicId: Long? = null,
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
     * Unique deep-linking parameter. If left empty, **forwarded copies** of the sent message will have a *Pay* button, allowing multiple users to pay directly from the forwarded message, using the same invoice. If non-empty, forwarded copies of the sent message will have a *URL* button with a deep link to the bot (instead of a *Pay* button), with the value used as the start parameter
     */
    @SerialName("start_parameter")
    public val startParameter: String? = null,
    /**
     * JSON-serialized data about the invoice, which will be shared with the payment provider. A detailed description of required fields should be provided by the payment provider.
     */
    @SerialName("provider_data")
    public val providerData: String? = null,
    /**
     * URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service. People like it better when they see what they are paying for.
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
    /**
     * Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
     */
    @SerialName("disable_notification")
    public val disableNotification: Boolean? = null,
    /**
     * Protects the contents of the sent message from forwarding and saving
     */
    @SerialName("protect_content")
    public val protectContent: Boolean? = null,
    /**
     * Pass *True* to allow up to 1000 messages per second, ignoring [broadcasting limits](https://core.telegram.org/bots/faq#how-can-i-message-all-of-my-bot-39s-subscribers-at-once) for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
     */
    @SerialName("allow_paid_broadcast")
    public val allowPaidBroadcast: Boolean? = null,
    /**
     * Unique identifier of the message effect to be added to the message; for private chats only
     */
    @SerialName("message_effect_id")
    public val messageEffectId: String? = null,
    /**
     * A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is automatically declined.
     */
    @SerialName("suggested_post_parameters")
    public val suggestedPostParameters: SuggestedPostParameters? = null,
    /**
     * Description of the message to reply to
     */
    @SerialName("reply_parameters")
    public val replyParameters: ReplyParameters? = null,
    /**
     * A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards). If empty, one 'Pay `total price`' button will be shown. If not empty, the first button must be a Pay button.
     */
    @SerialName("reply_markup")
    public val replyMarkup: InlineKeyboardMarkup? = null,
)
