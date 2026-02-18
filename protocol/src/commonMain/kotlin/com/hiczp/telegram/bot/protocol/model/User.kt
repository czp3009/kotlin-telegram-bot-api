// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents a Telegram user or bot.
 */
@Serializable
public data class User(
    /**
     * Unique identifier for this user or bot. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier.
     */
    public val id: Long,
    /**
     * *True*, if this user is a bot
     */
    @SerialName("is_bot")
    public val isBot: Boolean,
    /**
     * User's or bot's first name
     */
    @SerialName("first_name")
    public val firstName: String,
    /**
     * *Optional*. User's or bot's last name
     */
    @SerialName("last_name")
    public val lastName: String? = null,
    /**
     * *Optional*. User's or bot's username
     */
    public val username: String? = null,
    /**
     * *Optional*. [IETF language tag](https://en.wikipedia.org/wiki/IETF_language_tag) of the user's language
     */
    @SerialName("language_code")
    public val languageCode: String? = null,
    /**
     * *Optional*. *True*, if this user is a Telegram Premium user
     */
    @SerialName("is_premium")
    public val isPremium: Boolean? = null,
    /**
     * *Optional*. *True*, if this user added the bot to the attachment menu
     */
    @SerialName("added_to_attachment_menu")
    public val addedToAttachmentMenu: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can be invited to groups. Returned only in [getMe](https://core.telegram.org/bots/api#getme).
     */
    @SerialName("can_join_groups")
    public val canJoinGroups: Boolean? = null,
    /**
     * *Optional*. *True*, if [privacy mode](https://core.telegram.org/bots/features#privacy-mode) is disabled for the bot. Returned only in [getMe](https://core.telegram.org/bots/api#getme).
     */
    @SerialName("can_read_all_group_messages")
    public val canReadAllGroupMessages: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot supports inline queries. Returned only in [getMe](https://core.telegram.org/bots/api#getme).
     */
    @SerialName("supports_inline_queries")
    public val supportsInlineQueries: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot can be connected to a Telegram Business account to receive its messages. Returned only in [getMe](https://core.telegram.org/bots/api#getme).
     */
    @SerialName("can_connect_to_business")
    public val canConnectToBusiness: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot has a main Web App. Returned only in [getMe](https://core.telegram.org/bots/api#getme).
     */
    @SerialName("has_main_web_app")
    public val hasMainWebApp: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot has forum topic mode enabled in private chats. Returned only in [getMe](https://core.telegram.org/bots/api#getme).
     */
    @SerialName("has_topics_enabled")
    public val hasTopicsEnabled: Boolean? = null,
    /**
     * *Optional*. *True*, if the bot allows users to create and delete topics in private chats. Returned only in [getMe](https://core.telegram.org/bots/api#getme).
     */
    @SerialName("allows_users_to_create_topics")
    public val allowsUsersToCreateTopics: Boolean? = null,
)
