// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes a service message about the chat owner leaving the chat.
 */
@Serializable
public data class ChatOwnerLeft(
    /**
     * *Optional*. The user which will be the new owner of the chat if the previous owner does not return to the chat
     */
    @SerialName("new_owner")
    public val newOwner: User? = null,
)
