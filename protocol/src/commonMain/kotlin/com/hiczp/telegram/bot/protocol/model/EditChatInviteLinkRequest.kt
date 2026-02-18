// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class EditChatInviteLinkRequest(
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
    /**
     * Point in time (Unix timestamp) when the link will expire
     */
    @SerialName("expire_date")
    public val expireDate: Long? = null,
    /**
     * The maximum number of users that can be members of the chat simultaneously after joining the chat via this invite link; 1-99999
     */
    @SerialName("member_limit")
    public val memberLimit: Long? = null,
    /**
     * *True*, if users joining the chat via the link need to be approved by chat administrators. If *True*, *member_limit* can't be specified
     */
    @SerialName("creates_join_request")
    public val createsJoinRequest: Boolean? = null,
)
