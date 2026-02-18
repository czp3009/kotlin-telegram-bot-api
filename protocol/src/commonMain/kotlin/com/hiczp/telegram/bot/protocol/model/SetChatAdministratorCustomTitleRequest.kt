// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SetChatAdministratorCustomTitleRequest(
    /**
     * Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
     */
    @SerialName("chat_id")
    public val chatId: String,
    /**
     * Unique identifier of the target user
     */
    @SerialName("user_id")
    public val userId: Long,
    /**
     * New custom title for the administrator; 0-16 characters, emoji are not allowed
     */
    @SerialName("custom_title")
    public val customTitle: String,
)
