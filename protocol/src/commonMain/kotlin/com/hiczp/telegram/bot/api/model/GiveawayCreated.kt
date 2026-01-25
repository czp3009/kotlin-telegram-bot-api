// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about the creation of a scheduled giveaway.
 */
@Serializable
public data class GiveawayCreated(
    /**
     * *Optional*. The number of Telegram Stars to be split between giveaway winners; for Telegram Star giveaways only
     */
    @SerialName("prize_star_count")
    public val prizeStarCount: Long? = null,
)
