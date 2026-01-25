// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class EditChatSubscriptionInviteLinkRequest(
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    @SerialName("chat_id")
    public val chatId: String,
    /**
     * The invite link to edit
     */
    @SerialName("invite_link")
    public val inviteLink: String,
    /**
     * Invite link name; 0-32 characters
     */
    public val name: String? = null,
)
