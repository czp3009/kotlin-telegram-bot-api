// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents an inline button that switches the current user to inline mode in a chosen chat, with an optional default inline query.
 */
@Serializable
public data class SwitchInlineQueryChosenChat(
    /**
     * *Optional*. The default inline query to be inserted in the input field. If left empty, only the bot's username will be inserted
     */
    public val query: String? = null,
    /**
     * *Optional*. *True*, if private chats with users can be chosen
     */
    @SerialName("allow_user_chats")
    public val allowUserChats: Boolean? = null,
    /**
     * *Optional*. *True*, if private chats with bots can be chosen
     */
    @SerialName("allow_bot_chats")
    public val allowBotChats: Boolean? = null,
    /**
     * *Optional*. *True*, if group and supergroup chats can be chosen
     */
    @SerialName("allow_group_chats")
    public val allowGroupChats: Boolean? = null,
    /**
     * *Optional*. *True*, if channel chats can be chosen
     */
    @SerialName("allow_channel_chats")
    public val allowChannelChats: Boolean? = null,
)
