// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Boolean
import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about the completion of a giveaway without public winners.
 */
@Serializable
public data class GiveawayCompleted(
    /**
     * Number of winners in the giveaway
     */
    @SerialName("winner_count")
    public val winnerCount: Long,
    /**
     * *Optional*. Number of undistributed prizes
     */
    @SerialName("unclaimed_prize_count")
    public val unclaimedPrizeCount: Long? = null,
    /**
     * *Optional*. Message with the giveaway that was completed, if it wasn't deleted
     */
    @SerialName("giveaway_message")
    public val giveawayMessage: Message? = null,
    /**
     * *Optional*. *True*, if the giveaway is a Telegram Star giveaway. Otherwise, currently, the giveaway is a Telegram Premium giveaway.
     */
    @SerialName("is_star_giveaway")
    public val isStarGiveaway: Boolean? = null,
)
