// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Boolean
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object describes the access settings of a bot.
 */
@Serializable
public data class BotAccessSettings(
    /**
     * *True*, if only selected users can access the bot. The bot's owner can always access it.
     */
    @SerialName("is_access_restricted")
    public val isAccessRestricted: Boolean,
    /**
     * *Optional*. The list of other users who have access to the bot if the access is restricted
     */
    @SerialName("added_users")
    public val addedUsers: List<User>? = null,
)
