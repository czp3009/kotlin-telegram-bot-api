// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object defines the parameters for the creation of a managed bot. Information about the created bot will be shared with the bot using the update managed_bot and a Message with the field managed_bot_created.
 */
@Serializable
public data class KeyboardButtonRequestManagedBot(
    /**
     * Signed 32-bit identifier of the request. Must be unique within the message.
     */
    @SerialName("request_id")
    public val requestId: Long,
    /**
     * *Optional*. Suggested name for the bot
     */
    @SerialName("suggested_name")
    public val suggestedName: String? = null,
    /**
     * *Optional*. Suggested username for the bot
     */
    @SerialName("suggested_username")
    public val suggestedUsername: String? = null,
)
