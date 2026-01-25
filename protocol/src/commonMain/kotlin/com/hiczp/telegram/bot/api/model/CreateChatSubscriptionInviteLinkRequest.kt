// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CreateChatSubscriptionInviteLinkRequest(
    /**
     * Unique identifier for the target channel chat or username of the target channel (in the format `@channelusername`)
     */
    @SerialName("chat_id")
    public val chatId: String,
    /**
     * Invite link name; 0-32 characters
     */
    public val name: String? = null,
    /**
     * The number of seconds the subscription will be active for before the next payment. Currently, it must always be 2592000 (30 days).
     */
    @SerialName("subscription_period")
    public val subscriptionPeriod: Long,
    /**
     * The amount of Telegram Stars a user must pay initially and after each subsequent subscription period to be a member of the chat; 1-10000
     */
    @SerialName("subscription_price")
    public val subscriptionPrice: Long,
)
