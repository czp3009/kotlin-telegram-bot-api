// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class PromoteChatMemberRequest(
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    @SerialName("chat_id")
    public val chatId: String,
    /**
     * Unique identifier of the target user
     */
    @SerialName("user_id")
    public val userId: Long,
    /**
     * Pass *True* if the administrator's presence in the chat is hidden
     */
    @SerialName("is_anonymous")
    public val isAnonymous: Boolean? = null,
    /**
     * Pass *True* if the administrator can access the chat event log, get boost list, see hidden supergroup and channel members, report spam messages, ignore slow mode, and send messages to the chat without paying Telegram Stars. Implied by any other administrator privilege.
     */
    @SerialName("can_manage_chat")
    public val canManageChat: Boolean? = null,
    /**
     * Pass *True* if the administrator can delete messages of other users
     */
    @SerialName("can_delete_messages")
    public val canDeleteMessages: Boolean? = null,
    /**
     * Pass *True* if the administrator can manage video chats
     */
    @SerialName("can_manage_video_chats")
    public val canManageVideoChats: Boolean? = null,
    /**
     * Pass *True* if the administrator can restrict, ban or unban chat members, or access supergroup statistics. For backward compatibility, defaults to *True* for promotions of channel administrators
     */
    @SerialName("can_restrict_members")
    public val canRestrictMembers: Boolean? = null,
    /**
     * Pass *True* if the administrator can add new administrators with a subset of their own privileges or demote administrators that they have promoted, directly or indirectly (promoted by administrators that were appointed by him)
     */
    @SerialName("can_promote_members")
    public val canPromoteMembers: Boolean? = null,
    /**
     * Pass *True* if the administrator can change chat title, photo and other settings
     */
    @SerialName("can_change_info")
    public val canChangeInfo: Boolean? = null,
    /**
     * Pass *True* if the administrator can invite new users to the chat
     */
    @SerialName("can_invite_users")
    public val canInviteUsers: Boolean? = null,
    /**
     * Pass *True* if the administrator can post stories to the chat
     */
    @SerialName("can_post_stories")
    public val canPostStories: Boolean? = null,
    /**
     * Pass *True* if the administrator can edit stories posted by other users, post stories to the chat page, pin chat stories, and access the chat's story archive
     */
    @SerialName("can_edit_stories")
    public val canEditStories: Boolean? = null,
    /**
     * Pass *True* if the administrator can delete stories posted by other users
     */
    @SerialName("can_delete_stories")
    public val canDeleteStories: Boolean? = null,
    /**
     * Pass *True* if the administrator can post messages in the channel, approve suggested posts, or access channel statistics; for channels only
     */
    @SerialName("can_post_messages")
    public val canPostMessages: Boolean? = null,
    /**
     * Pass *True* if the administrator can edit messages of other users and can pin messages; for channels only
     */
    @SerialName("can_edit_messages")
    public val canEditMessages: Boolean? = null,
    /**
     * Pass *True* if the administrator can pin messages; for supergroups only
     */
    @SerialName("can_pin_messages")
    public val canPinMessages: Boolean? = null,
    /**
     * Pass *True* if the user is allowed to create, rename, close, and reopen forum topics; for supergroups only
     */
    @SerialName("can_manage_topics")
    public val canManageTopics: Boolean? = null,
    /**
     * Pass *True* if the administrator can manage direct messages within the channel and decline suggested posts; for channels only
     */
    @SerialName("can_manage_direct_messages")
    public val canManageDirectMessages: Boolean? = null,
)
