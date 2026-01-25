// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

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
    public val giveawayMessage: JsonElement? = null,
    /**
     * *Optional*. *True*, if the giveaway is a Telegram Star giveaway. Otherwise, currently, the giveaway is a Telegram Premium giveaway.
     */
    @SerialName("is_star_giveaway")
    public val isStarGiveaway: Boolean? = null,
)
