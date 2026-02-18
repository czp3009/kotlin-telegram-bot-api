// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes a service message about an ownership change in the chat.
 */
@Serializable
public data class ChatOwnerChanged(
    /**
     * The new owner of the chat
     */
    @SerialName("new_owner")
    public val newOwner: User,
)
