// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * This object represents changes in the status of a chat member.
 */
@Serializable
public data class ChatMemberUpdated(
    /**
     * Chat the user belongs to
     */
    public val chat: JsonElement?,
    /**
     * Performer of the action, which resulted in the change
     */
    public val from: JsonElement?,
    /**
     * Date the change was done in Unix time
     */
    public val date: Long,
    /**
     * Previous information about the chat member
     */
    @SerialName("old_chat_member")
    public val oldChatMember: JsonElement?,
    /**
     * New information about the chat member
     */
    @SerialName("new_chat_member")
    public val newChatMember: JsonElement?,
    /**
     * *Optional*. Chat invite link, which was used by the user to join the chat; for joining by invite link events only.
     */
    @SerialName("invite_link")
    public val inviteLink: JsonElement? = null,
    /**
     * *Optional*. *True*, if the user joined the chat after sending a direct join request without using an invite link and being approved by an administrator
     */
    @SerialName("via_join_request")
    public val viaJoinRequest: Boolean? = null,
    /**
     * *Optional*. *True*, if the user joined the chat via a chat folder invite link
     */
    @SerialName("via_chat_folder_invite_link")
    public val viaChatFolderInviteLink: Boolean? = null,
)
