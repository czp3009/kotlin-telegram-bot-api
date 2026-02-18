// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object contains information about a chat that was shared with the bot using a KeyboardButtonRequestChat button.
 */
@Serializable
public data class ChatShared(
    /**
     * Identifier of the request
     */
    @SerialName("request_id")
    public val requestId: Long,
    /**
     * Identifier of the shared chat. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier. The bot may not have access to the chat and could be unable to use this identifier, unless the chat is already known to the bot by some other means.
     */
    @SerialName("chat_id")
    public val chatId: Long,
    /**
     * *Optional*. Title of the chat, if the title was requested by the bot.
     */
    public val title: String? = null,
    /**
     * *Optional*. Username of the chat, if the username was requested by the bot and available.
     */
    public val username: String? = null,
    /**
     * *Optional*. Available sizes of the chat photo, if the photo was requested by the bot
     */
    public val photo: List<PhotoSize>? = null,
)
