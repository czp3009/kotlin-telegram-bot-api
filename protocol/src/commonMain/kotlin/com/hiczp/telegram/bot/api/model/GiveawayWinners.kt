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
 * This object represents a message about the completion of a giveaway with public winners.
 */
@Serializable
public data class GiveawayWinners(
    /**
     * The chat that created the giveaway
     */
    public val chat: JsonElement?,
    /**
     * Identifier of the message with the giveaway in the chat
     */
    @SerialName("giveaway_message_id")
    public val giveawayMessageId: Long,
    /**
     * Point in time (Unix timestamp) when winners of the giveaway were selected
     */
    @SerialName("winners_selection_date")
    public val winnersSelectionDate: Long,
    /**
     * Total number of winners in the giveaway
     */
    @SerialName("winner_count")
    public val winnerCount: Long,
    /**
     * List of up to 100 winners of the giveaway
     */
    public val winners: List<User>,
    /**
     * *Optional*. The number of other chats the user had to join in order to be eligible for the giveaway
     */
    @SerialName("additional_chat_count")
    public val additionalChatCount: Long? = null,
    /**
     * *Optional*. The number of Telegram Stars that were split between giveaway winners; for Telegram Star giveaways only
     */
    @SerialName("prize_star_count")
    public val prizeStarCount: Long? = null,
    /**
     * *Optional*. The number of months the Telegram Premium subscription won from the giveaway will be active for; for Telegram Premium giveaways only
     */
    @SerialName("premium_subscription_month_count")
    public val premiumSubscriptionMonthCount: Long? = null,
    /**
     * *Optional*. Number of undistributed prizes
     */
    @SerialName("unclaimed_prize_count")
    public val unclaimedPrizeCount: Long? = null,
    /**
     * *Optional*. *True*, if only users who had joined the chats after the giveaway started were eligible to win
     */
    @SerialName("only_new_members")
    public val onlyNewMembers: Boolean? = null,
    /**
     * *Optional*. *True*, if the giveaway was canceled because the payment for it was refunded
     */
    @SerialName("was_refunded")
    public val wasRefunded: Boolean? = null,
    /**
     * *Optional*. Description of additional giveaway prize
     */
    @SerialName("prize_description")
    public val prizeDescription: String? = null,
)
