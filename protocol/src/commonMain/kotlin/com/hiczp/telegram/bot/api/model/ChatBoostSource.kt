// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * This object describes the source of a chat boost. It can be one of
 * ChatBoostSourcePremium ChatBoostSourceGiftCode ChatBoostSourceGiveaway
 */
@Serializable
@JsonClassDiscriminator("source")
@OptIn(ExperimentalSerializationApi::class)
public sealed interface ChatBoostSource

/**
 * The boost was obtained by subscribing to Telegram Premium or by gifting a Telegram Premium subscription to another user.
 */
@Serializable
@SerialName("premium")
public data class ChatBoostSourcePremium(
    /**
     * User that boosted the chat
     */
    public val user: User,
) : ChatBoostSource

/**
 * The boost was obtained by the creation of Telegram Premium gift codes to boost a chat. Each such code boosts the chat 4 times for the duration of the corresponding Telegram Premium subscription.
 */
@Serializable
@SerialName("gift_code")
public data class ChatBoostSourceGiftCode(
    /**
     * User for which the gift code was created
     */
    public val user: User,
) : ChatBoostSource

/**
 * The boost was obtained by the creation of a Telegram Premium or a Telegram Star giveaway. This boosts the chat 4 times for the duration of the corresponding Telegram Premium subscription for Telegram Premium giveaways and prize_star_count / 500 times for one year for Telegram Star giveaways.
 */
@Serializable
@SerialName("giveaway")
public data class ChatBoostSourceGiveaway(
    /**
     * Identifier of a message in the chat with the giveaway; the message could have been deleted already. May be 0 if the message isn't sent yet.
     */
    @SerialName("giveaway_message_id")
    public val giveawayMessageId: Long,
    /**
     * *Optional*. User that won the prize in the giveaway if any; for Telegram Premium giveaways only
     */
    public val user: User? = null,
    /**
     * *Optional*. The number of Telegram Stars to be split between giveaway winners; for Telegram Star giveaways only
     */
    @SerialName("prize_star_count")
    public val prizeStarCount: Long? = null,
    /**
     * *Optional*. *True*, if the giveaway was completed, but there was no user to win the prize
     */
    @SerialName("is_unclaimed")
    public val isUnclaimed: Boolean? = null,
) : ChatBoostSource
