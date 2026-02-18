// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object contains information about a user that was shared with the bot using a KeyboardButtonRequestUsers button.
 */
@Serializable
public data class SharedUser(
    /**
     * Identifier of the shared user. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so 64-bit integers or double-precision float types are safe for storing these identifiers. The bot may not have access to the user and could be unable to use this identifier, unless the user is already known to the bot by some other means.
     */
    @SerialName("user_id")
    public val userId: Long,
    /**
     * *Optional*. First name of the user, if the name was requested by the bot
     */
    @SerialName("first_name")
    public val firstName: String? = null,
    /**
     * *Optional*. Last name of the user, if the name was requested by the bot
     */
    @SerialName("last_name")
    public val lastName: String? = null,
    /**
     * *Optional*. Username of the user, if the username was requested by the bot
     */
    public val username: String? = null,
    /**
     * *Optional*. Available sizes of the chat photo, if the photo was requested by the bot
     */
    public val photo: List<PhotoSize>? = null,
)
