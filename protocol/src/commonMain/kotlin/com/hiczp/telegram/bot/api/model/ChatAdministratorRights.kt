// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the rights of an administrator in a chat.
 */
@Serializable
public data class ChatAdministratorRights(
    /**
     * *True*, if the user's presence in the chat is hidden
     */
    @SerialName("is_anonymous")
    public val isAnonymous: Boolean,
    /**
     * *True*, if the administrator can access the chat event log, get boost list, see hidden supergroup and channel members, report spam messages, ignore slow mode, and send messages to the chat without paying Telegram Stars. Implied by any other administrator privilege.
     */
    @SerialName("can_manage_chat")
    public val canManageChat: Boolean,
    /**
     * *True*, if the administrator can delete messages of other users
     */
    @SerialName("can_delete_messages")
    public val canDeleteMessages: Boolean,
    /**
     * *True*, if the administrator can manage video chats
     */
    @SerialName("can_manage_video_chats")
    public val canManageVideoChats: Boolean,
    /**
     * *True*, if the administrator can restrict, ban or unban chat members, or access supergroup statistics
     */
    @SerialName("can_restrict_members")
    public val canRestrictMembers: Boolean,
    /**
     * *True*, if the administrator can add new administrators with a subset of their own privileges or demote administrators that they have promoted, directly or indirectly (promoted by administrators that were appointed by the user)
     */
    @SerialName("can_promote_members")
    public val canPromoteMembers: Boolean,
    /**
     * *True*, if the user is allowed to change the chat title, photo and other settings
     */
    @SerialName("can_change_info")
    public val canChangeInfo: Boolean,
    /**
     * *True*, if the user is allowed to invite new users to the chat
     */
    @SerialName("can_invite_users")
    public val canInviteUsers: Boolean,
    /**
     * *True*, if the administrator can post stories to the chat
     */
    @SerialName("can_post_stories")
    public val canPostStories: Boolean,
    /**
     * *True*, if the administrator can edit stories posted by other users, post stories to the chat page, pin chat stories, and access the chat's story archive
     */
    @SerialName("can_edit_stories")
    public val canEditStories: Boolean,
    /**
     * *True*, if the administrator can delete stories posted by other users
     */
    @SerialName("can_delete_stories")
    public val canDeleteStories: Boolean,
    /**
     * *Optional*. *True*, if the administrator can post messages in the channel, approve suggested posts, or access channel statistics; for channels only
     */
    @SerialName("can_post_messages")
    public val canPostMessages: Boolean? = null,
    /**
     * *Optional*. *True*, if the administrator can edit messages of other users and can pin messages; for channels only
     */
    @SerialName("can_edit_messages")
    public val canEditMessages: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to pin messages; for groups and supergroups only
     */
    @SerialName("can_pin_messages")
    public val canPinMessages: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to create, rename, close, and reopen forum topics; for supergroups only
     */
    @SerialName("can_manage_topics")
    public val canManageTopics: Boolean? = null,
    /**
     * *Optional*. *True*, if the administrator can manage direct messages of the channel and decline suggested posts; for channels only
     */
    @SerialName("can_manage_direct_messages")
    public val canManageDirectMessages: Boolean? = null,
)
