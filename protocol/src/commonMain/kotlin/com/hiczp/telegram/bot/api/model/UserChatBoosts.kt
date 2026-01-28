// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object represents a list of boosts added to a chat by a user.
 */
@Serializable
public data class UserChatBoosts(
    /**
     * The list of boosts added to the chat by the user
     */
    public val boosts: List<ChatBoost>,
)
