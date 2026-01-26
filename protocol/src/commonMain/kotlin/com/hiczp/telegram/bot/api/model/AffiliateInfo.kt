// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Contains information about the affiliate that received a commission via this transaction.
 */
@Serializable
public data class AffiliateInfo(
    /**
     * *Optional*. The bot or the user that received an affiliate commission if it was received by a bot or a user
     */
    @SerialName("affiliate_user")
    public val affiliateUser: User? = null,
    /**
     * *Optional*. The chat that received an affiliate commission if it was received by a chat
     */
    @SerialName("affiliate_chat")
    public val affiliateChat: Chat? = null,
    /**
     * The number of Telegram Stars received by the affiliate for each 1000 Telegram Stars received by the bot from referred users
     */
    @SerialName("commission_per_mille")
    public val commissionPerMille: Long,
    /**
     * Integer amount of Telegram Stars received by the affiliate from the transaction, rounded to 0; can be negative for refunds
     */
    public val amount: Long,
    /**
     * *Optional*. The number of 1/1000000000 shares of Telegram Stars received by the affiliate; from -999999999 to 999999999; can be negative for refunds
     */
    @SerialName("nanostar_amount")
    public val nanostarAmount: Long? = null,
)
