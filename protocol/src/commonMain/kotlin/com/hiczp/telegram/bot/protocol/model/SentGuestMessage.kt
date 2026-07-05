// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes an inline message sent by a guest bot.
 */
@Serializable
public data class SentGuestMessage(
    /**
     * Identifier of the sent inline message
     */
    @SerialName("inline_message_id")
    public val inlineMessageId: String,
)
