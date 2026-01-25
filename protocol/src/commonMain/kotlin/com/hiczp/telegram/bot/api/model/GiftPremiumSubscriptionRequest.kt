// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class GiftPremiumSubscriptionRequest(
    /**
     * Unique identifier of the target user who will receive a Telegram Premium subscription
     */
    @SerialName("user_id")
    public val userId: Long,
    /**
     * Number of months the Telegram Premium subscription will be active for the user; must be one of 3, 6, or 12
     */
    @SerialName("month_count")
    public val monthCount: Long,
    /**
     * Number of Telegram Stars to pay for the Telegram Premium subscription; must be 1000 for 3 months, 1500 for 6 months, and 2500 for 12 months
     */
    @SerialName("star_count")
    public val starCount: Long,
    /**
     * Text that will be shown along with the service message about the subscription; 0-128 characters
     */
    public val text: String? = null,
    /**
     * Mode for parsing entities in the text. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details. Entities other than “bold”, “italic”, “underline”, “strikethrough”, “spoiler”, and “custom_emoji” are ignored.
     */
    @SerialName("text_parse_mode")
    public val textParseMode: String? = null,
    /**
     * A JSON-serialized list of special entities that appear in the gift text. It can be specified instead of *text_parse_mode*. Entities other than “bold”, “italic”, “underline”, “strikethrough”, “spoiler”, and “custom_emoji” are ignored.
     */
    @SerialName("text_entities")
    public val textEntities: List<MessageEntity>? = null,
)
