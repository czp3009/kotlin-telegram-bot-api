// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SendGiftRequest(
    /**
     * Required if *chat_id* is not specified. Unique identifier of the target user who will receive the gift.
     */
    @SerialName("user_id")
    public val userId: Long? = null,
    /**
     * Required if *user_id* is not specified. Unique identifier for the chat or username of the channel (in the format `@channelusername`) that will receive the gift.
     */
    @SerialName("chat_id")
    public val chatId: String? = null,
    /**
     * Identifier of the gift; limited gifts can't be sent to channel chats
     */
    @SerialName("gift_id")
    public val giftId: String,
    /**
     * Pass *True* to pay for the gift upgrade from the bot's balance, thereby making the upgrade free for the receiver
     */
    @SerialName("pay_for_upgrade")
    public val payForUpgrade: Boolean? = null,
    /**
     * Text that will be shown along with the gift; 0-128 characters
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
