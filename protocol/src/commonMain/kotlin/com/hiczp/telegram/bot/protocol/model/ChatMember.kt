// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * This object contains information about one member of a chat. Currently, the following 6 types of chat members are supported:
 * ChatMemberOwner ChatMemberAdministrator ChatMemberMember ChatMemberRestricted ChatMemberLeft ChatMemberBanned
 */
@Serializable
@JsonClassDiscriminator("status")
@OptIn(ExperimentalSerializationApi::class)
public sealed interface ChatMember

/**
 * Represents a chat member that owns the chat and has all administrator privileges.
 */
@Serializable
@SerialName("creator")
public data class ChatMemberOwner(
    /**
     * Information about the user
     */
    public val user: User,
    /**
     * *True*, if the user's presence in the chat is hidden
     */
    @SerialName("is_anonymous")
    public val isAnonymous: Boolean,
    /**
     * *Optional*. Custom title for this user
     */
    @SerialName("custom_title")
    public val customTitle: String? = null,
) : ChatMember

/**
 * Represents a chat member that has some additional privileges.
 */
@Serializable
@SerialName("administrator")
public data class ChatMemberAdministrator(
    /**
     * Information about the user
     */
    public val user: User,
    /**
     * *True*, if the bot is allowed to edit administrator privileges of that user
     */
    @SerialName("can_be_edited")
    public val canBeEdited: Boolean,
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
    /**
     * *Optional*. Custom title for this user
     */
    @SerialName("custom_title")
    public val customTitle: String? = null,
) : ChatMember

/**
 * Represents a chat member that has no additional privileges or restrictions.
 */
@Serializable
@SerialName("member")
public data class ChatMemberMember(
    /**
     * Information about the user
     */
    public val user: User,
    /**
     * *Optional*. Date when the user's subscription will expire; Unix time
     */
    @SerialName("until_date")
    public val untilDate: Long? = null,
) : ChatMember

/**
 * Represents a chat member that is under certain restrictions in the chat. Supergroups only.
 */
@Serializable
@SerialName("restricted")
public data class ChatMemberRestricted(
    /**
     * Information about the user
     */
    public val user: User,
    /**
     * *True*, if the user is a member of the chat at the moment of the request
     */
    @SerialName("is_member")
    public val isMember: Boolean,
    /**
     * *True*, if the user is allowed to send text messages, contacts, giveaways, giveaway winners, invoices, locations and venues
     */
    @SerialName("can_send_messages")
    public val canSendMessages: Boolean,
    /**
     * *True*, if the user is allowed to send audios
     */
    @SerialName("can_send_audios")
    public val canSendAudios: Boolean,
    /**
     * *True*, if the user is allowed to send documents
     */
    @SerialName("can_send_documents")
    public val canSendDocuments: Boolean,
    /**
     * *True*, if the user is allowed to send photos
     */
    @SerialName("can_send_photos")
    public val canSendPhotos: Boolean,
    /**
     * *True*, if the user is allowed to send videos
     */
    @SerialName("can_send_videos")
    public val canSendVideos: Boolean,
    /**
     * *True*, if the user is allowed to send video notes
     */
    @SerialName("can_send_video_notes")
    public val canSendVideoNotes: Boolean,
    /**
     * *True*, if the user is allowed to send voice notes
     */
    @SerialName("can_send_voice_notes")
    public val canSendVoiceNotes: Boolean,
    /**
     * *True*, if the user is allowed to send polls and checklists
     */
    @SerialName("can_send_polls")
    public val canSendPolls: Boolean,
    /**
     * *True*, if the user is allowed to send animations, games, stickers and use inline bots
     */
    @SerialName("can_send_other_messages")
    public val canSendOtherMessages: Boolean,
    /**
     * *True*, if the user is allowed to add web page previews to their messages
     */
    @SerialName("can_add_web_page_previews")
    public val canAddWebPagePreviews: Boolean,
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
     * *True*, if the user is allowed to pin messages
     */
    @SerialName("can_pin_messages")
    public val canPinMessages: Boolean,
    /**
     * *True*, if the user is allowed to create forum topics
     */
    @SerialName("can_manage_topics")
    public val canManageTopics: Boolean,
    /**
     * Date when restrictions will be lifted for this user; Unix time. If 0, then the user is restricted forever
     */
    @SerialName("until_date")
    public val untilDate: Long,
) : ChatMember

/**
 * Represents a chat member that isn't currently a member of the chat, but may join it themselves.
 */
@Serializable
@SerialName("left")
public data class ChatMemberLeft(
    /**
     * Information about the user
     */
    public val user: User,
) : ChatMember

/**
 * Represents a chat member that was banned in the chat and can't return to the chat or view chat messages.
 */
@Serializable
@SerialName("kicked")
public data class ChatMemberBanned(
    /**
     * Information about the user
     */
    public val user: User,
    /**
     * Date when restrictions will be lifted for this user; Unix time. If 0, then the user is banned forever
     */
    @SerialName("until_date")
    public val untilDate: Long,
) : ChatMember
