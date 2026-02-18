// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents a chat.
 */
@Serializable
public data class Chat(
    /**
     * Unique identifier for this chat. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
     */
    public val id: Long,
    /**
     * Type of the chat, can be either “private”, “group”, “supergroup” or “channel”
     */
    public val type: String,
    /**
     * *Optional*. Title, for supergroups, channels and group chats
     */
    public val title: String? = null,
    /**
     * *Optional*. Username, for private chats, supergroups and channels if available
     */
    public val username: String? = null,
    /**
     * *Optional*. First name of the other party in a private chat
     */
    @SerialName("first_name")
    public val firstName: String? = null,
    /**
     * *Optional*. Last name of the other party in a private chat
     */
    @SerialName("last_name")
    public val lastName: String? = null,
    /**
     * *Optional*. *True*, if the supergroup chat is a forum (has [topics](https://telegram.org/blog/topics-in-groups-collectible-usernames#topics-in-groups) enabled)
     */
    @SerialName("is_forum")
    public val isForum: Boolean? = null,
    /**
     * *Optional*. *True*, if the chat is the direct messages chat of a channel
     */
    @SerialName("is_direct_messages")
    public val isDirectMessages: Boolean? = null,
)
