// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RevokeChatInviteLinkRequest(
    /**
     * Unique identifier of the target chat or username of the target channel (in the format `@channelusername`)
     */
    @SerialName("chat_id")
    public val chatId: String,
    /**
     * The invite link to revoke
     */
    @SerialName("invite_link")
    public val inviteLink: String,
)
