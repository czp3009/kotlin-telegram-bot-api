// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Describes a service message about a regular gift that was sent or received.
 */
@Serializable
public data class GiftInfo(
    /**
     * Information about the gift
     */
    public val gift: JsonElement?,
    /**
     * *Optional*. Unique identifier of the received gift for the bot; only present for gifts received on behalf of business accounts
     */
    @SerialName("owned_gift_id")
    public val ownedGiftId: String? = null,
    /**
     * *Optional*. Number of Telegram Stars that can be claimed by the receiver by converting the gift; omitted if conversion to Telegram Stars is impossible
     */
    @SerialName("convert_star_count")
    public val convertStarCount: Long? = null,
    /**
     * *Optional*. Number of Telegram Stars that were prepaid for the ability to upgrade the gift
     */
    @SerialName("prepaid_upgrade_star_count")
    public val prepaidUpgradeStarCount: Long? = null,
    /**
     * *Optional*. *True*, if the gift's upgrade was purchased after the gift was sent
     */
    @SerialName("is_upgrade_separate")
    public val isUpgradeSeparate: Boolean? = null,
    /**
     * *Optional*. *True*, if the gift can be upgraded to a unique gift
     */
    @SerialName("can_be_upgraded")
    public val canBeUpgraded: Boolean? = null,
    /**
     * *Optional*. Text of the message that was added to the gift
     */
    public val text: String? = null,
    /**
     * *Optional*. Special entities that appear in the text
     */
    public val entities: List<MessageEntity>? = null,
    /**
     * *Optional*. *True*, if the sender and gift text are shown only to the gift receiver; otherwise, everyone will be able to see them
     */
    @SerialName("is_private")
    public val isPrivate: Boolean? = null,
    /**
     * *Optional*. Unique number reserved for this gift when upgraded. See the *number* field in [UniqueGift](https://core.telegram.org/bots/api#uniquegift)
     */
    @SerialName("unique_gift_number")
    public val uniqueGiftNumber: Long? = null,
)
