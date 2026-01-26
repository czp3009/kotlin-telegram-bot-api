// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes a service message about a unique gift that was sent or received.
 */
@Serializable
public data class UniqueGiftInfo(
    /**
     * Information about the gift
     */
    public val gift: UniqueGift,
    /**
     * Origin of the gift. Currently, either “upgrade” for gifts upgraded from regular gifts, “transfer” for gifts transferred from other users or channels, “resale” for gifts bought from other users, “gifted_upgrade” for upgrades purchased after the gift was sent, or “offer” for gifts bought or sold through gift purchase offers
     */
    public val origin: String,
    /**
     * *Optional*. For gifts bought from other users, the currency in which the payment for the gift was done. Currently, one of “XTR” for Telegram Stars or “TON” for toncoins.
     */
    @SerialName("last_resale_currency")
    public val lastResaleCurrency: String? = null,
    /**
     * *Optional*. For gifts bought from other users, the price paid for the gift in either Telegram Stars or nanotoncoins
     */
    @SerialName("last_resale_amount")
    public val lastResaleAmount: Long? = null,
    /**
     * *Optional*. Unique identifier of the received gift for the bot; only present for gifts received on behalf of business accounts
     */
    @SerialName("owned_gift_id")
    public val ownedGiftId: String? = null,
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
)
