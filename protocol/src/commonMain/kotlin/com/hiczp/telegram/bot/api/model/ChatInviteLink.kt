// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an invite link for a chat.
 */
@Serializable
public data class ChatInviteLink(
    /**
     * The invite link. If the link was created by another chat administrator, then the second part of the link will be replaced with “…”.
     */
    @SerialName("invite_link")
    public val inviteLink: String,
    /**
     * Creator of the link
     */
    public val creator: User,
    /**
     * *True*, if users joining the chat via the link need to be approved by chat administrators
     */
    @SerialName("creates_join_request")
    public val createsJoinRequest: Boolean,
    /**
     * *True*, if the link is primary
     */
    @SerialName("is_primary")
    public val isPrimary: Boolean,
    /**
     * *True*, if the link is revoked
     */
    @SerialName("is_revoked")
    public val isRevoked: Boolean,
    /**
     * *Optional*. Invite link name
     */
    public val name: String? = null,
    /**
     * *Optional*. Point in time (Unix timestamp) when the link will expire or has been expired
     */
    @SerialName("expire_date")
    public val expireDate: Long? = null,
    /**
     * *Optional*. The maximum number of users that can be members of the chat simultaneously after joining the chat via this invite link; 1-99999
     */
    @SerialName("member_limit")
    public val memberLimit: Long? = null,
    /**
     * *Optional*. Number of pending join requests created using this link
     */
    @SerialName("pending_join_request_count")
    public val pendingJoinRequestCount: Long? = null,
    /**
     * *Optional*. The number of seconds the subscription will be active for before the next payment
     */
    @SerialName("subscription_period")
    public val subscriptionPeriod: Long? = null,
    /**
     * *Optional*. The amount of Telegram Stars a user must pay initially and after each subsequent subscription period to be a member of the chat using the link
     */
    @SerialName("subscription_price")
    public val subscriptionPrice: Long? = null,
)
