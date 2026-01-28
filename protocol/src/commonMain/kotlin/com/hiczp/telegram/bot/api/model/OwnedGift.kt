// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * This object describes a gift received and owned by a user or a chat. Currently, it can be one of
 * OwnedGiftRegular OwnedGiftUnique
 */
@Serializable
@JsonClassDiscriminator("type")
@OptIn(ExperimentalSerializationApi::class)
public sealed interface OwnedGift

/**
 * Describes a regular gift owned by a user or a chat.
 */
@Serializable
@SerialName("regular")
public data class OwnedGiftRegular(
    /**
     * Information about the regular gift
     */
    public val gift: Gift,
    /**
     * *Optional*. Unique identifier of the gift for the bot; for gifts received on behalf of business accounts only
     */
    @SerialName("owned_gift_id")
    public val ownedGiftId: String? = null,
    /**
     * *Optional*. Sender of the gift if it is a known user
     */
    @SerialName("sender_user")
    public val senderUser: User? = null,
    /**
     * Date the gift was sent in Unix time
     */
    @SerialName("send_date")
    public val sendDate: Long,
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
     * *Optional*. *True*, if the gift is displayed on the account's profile page; for gifts received on behalf of business accounts only
     */
    @SerialName("is_saved")
    public val isSaved: Boolean? = null,
    /**
     * *Optional*. *True*, if the gift can be upgraded to a unique gift; for gifts received on behalf of business accounts only
     */
    @SerialName("can_be_upgraded")
    public val canBeUpgraded: Boolean? = null,
    /**
     * *Optional*. *True*, if the gift was refunded and isn't available anymore
     */
    @SerialName("was_refunded")
    public val wasRefunded: Boolean? = null,
    /**
     * *Optional*. Number of Telegram Stars that can be claimed by the receiver instead of the gift; omitted if the gift cannot be converted to Telegram Stars; for gifts received on behalf of business accounts only
     */
    @SerialName("convert_star_count")
    public val convertStarCount: Long? = null,
    /**
     * *Optional*. Number of Telegram Stars that were paid for the ability to upgrade the gift
     */
    @SerialName("prepaid_upgrade_star_count")
    public val prepaidUpgradeStarCount: Long? = null,
    /**
     * *Optional*. *True*, if the gift's upgrade was purchased after the gift was sent; for gifts received on behalf of business accounts only
     */
    @SerialName("is_upgrade_separate")
    public val isUpgradeSeparate: Boolean? = null,
    /**
     * *Optional*. Unique number reserved for this gift when upgraded. See the *number* field in [UniqueGift](https://core.telegram.org/bots/api#uniquegift)
     */
    @SerialName("unique_gift_number")
    public val uniqueGiftNumber: Long? = null,
) : OwnedGift

/**
 * Describes a unique gift received and owned by a user or a chat.
 */
@Serializable
@SerialName("unique")
public data class OwnedGiftUnique(
    /**
     * Information about the unique gift
     */
    public val gift: UniqueGift,
    /**
     * *Optional*. Unique identifier of the received gift for the bot; for gifts received on behalf of business accounts only
     */
    @SerialName("owned_gift_id")
    public val ownedGiftId: String? = null,
    /**
     * *Optional*. Sender of the gift if it is a known user
     */
    @SerialName("sender_user")
    public val senderUser: User? = null,
    /**
     * Date the gift was sent in Unix time
     */
    @SerialName("send_date")
    public val sendDate: Long,
    /**
     * *Optional*. *True*, if the gift is displayed on the account's profile page; for gifts received on behalf of business accounts only
     */
    @SerialName("is_saved")
    public val isSaved: Boolean? = null,
    /**
     * *Optional*. *True*, if the gift can be transferred to another owner; for gifts received on behalf of business accounts only
     */
    @SerialName("can_be_transferred")
    public val canBeTransferred: Boolean? = null,
    /**
     * *Optional*. Number of Telegram Stars that must be paid to transfer the gift; omitted if the bot cannot transfer the gift
     */
    @SerialName("transfer_star_count")
    public val transferStarCount: Long? = null,
    /**
     * *Optional*. Point in time (Unix timestamp) when the gift can be transferred. If it is in the past, then the gift can be transferred now
     */
    @SerialName("next_transfer_date")
    public val nextTransferDate: Long? = null,
) : OwnedGift
