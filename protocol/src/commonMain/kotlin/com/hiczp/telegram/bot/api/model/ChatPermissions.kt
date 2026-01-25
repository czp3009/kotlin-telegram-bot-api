// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes actions that a non-administrator user is allowed to take in a chat.
 */
@Serializable
public data class ChatPermissions(
    /**
     * *Optional*. *True*, if the user is allowed to send text messages, contacts, giveaways, giveaway winners, invoices, locations and venues
     */
    @SerialName("can_send_messages")
    public val canSendMessages: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to send audios
     */
    @SerialName("can_send_audios")
    public val canSendAudios: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to send documents
     */
    @SerialName("can_send_documents")
    public val canSendDocuments: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to send photos
     */
    @SerialName("can_send_photos")
    public val canSendPhotos: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to send videos
     */
    @SerialName("can_send_videos")
    public val canSendVideos: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to send video notes
     */
    @SerialName("can_send_video_notes")
    public val canSendVideoNotes: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to send voice notes
     */
    @SerialName("can_send_voice_notes")
    public val canSendVoiceNotes: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to send polls and checklists
     */
    @SerialName("can_send_polls")
    public val canSendPolls: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to send animations, games, stickers and use inline bots
     */
    @SerialName("can_send_other_messages")
    public val canSendOtherMessages: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to add web page previews to their messages
     */
    @SerialName("can_add_web_page_previews")
    public val canAddWebPagePreviews: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to change the chat title, photo and other settings. Ignored in public supergroups
     */
    @SerialName("can_change_info")
    public val canChangeInfo: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to invite new users to the chat
     */
    @SerialName("can_invite_users")
    public val canInviteUsers: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to pin messages. Ignored in public supergroups
     */
    @SerialName("can_pin_messages")
    public val canPinMessages: Boolean? = null,
    /**
     * *Optional*. *True*, if the user is allowed to create forum topics. If omitted defaults to the value of can_pin_messages
     */
    @SerialName("can_manage_topics")
    public val canManageTopics: Boolean? = null,
)
