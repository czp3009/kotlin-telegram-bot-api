// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents a message about a scheduled giveaway.
 */
@Serializable
public data class Giveaway(
    /**
     * The list of chats which the user must join to participate in the giveaway
     */
    public val chats: List<Chat>,
    /**
     * Point in time (Unix timestamp) when winners of the giveaway will be selected
     */
    @SerialName("winners_selection_date")
    public val winnersSelectionDate: Long,
    /**
     * The number of users which are supposed to be selected as winners of the giveaway
     */
    @SerialName("winner_count")
    public val winnerCount: Long,
    /**
     * *Optional*. *True*, if only users who join the chats after the giveaway started should be eligible to win
     */
    @SerialName("only_new_members")
    public val onlyNewMembers: Boolean? = null,
    /**
     * *Optional*. *True*, if the list of giveaway winners will be visible to everyone
     */
    @SerialName("has_public_winners")
    public val hasPublicWinners: Boolean? = null,
    /**
     * *Optional*. Description of additional giveaway prize
     */
    @SerialName("prize_description")
    public val prizeDescription: String? = null,
    /**
     * *Optional*. A list of two-letter [ISO 3166-1 alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) country codes indicating the countries from which eligible users for the giveaway must come. If empty, then all users can participate in the giveaway. Users with a phone number that was bought on Fragment can always participate in giveaways.
     */
    @SerialName("country_codes")
    public val countryCodes: List<String>? = null,
    /**
     * *Optional*. The number of Telegram Stars to be split between giveaway winners; for Telegram Star giveaways only
     */
    @SerialName("prize_star_count")
    public val prizeStarCount: Long? = null,
    /**
     * *Optional*. The number of months the Telegram Premium subscription won from the giveaway will be active for; for Telegram Premium giveaways only
     */
    @SerialName("premium_subscription_month_count")
    public val premiumSubscriptionMonthCount: Long? = null,
)
