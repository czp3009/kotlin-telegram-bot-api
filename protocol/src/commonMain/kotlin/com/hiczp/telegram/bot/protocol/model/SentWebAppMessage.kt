// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes an inline message sent by a Web App on behalf of a user.
 */
@Serializable
public data class SentWebAppMessage(
    /**
     * *Optional*. Identifier of the sent inline message. Available only if there is an [inline keyboard](https://core.telegram.org/bots/api#inlinekeyboardmarkup) attached to the message.
     */
    @SerialName("inline_message_id")
    public val inlineMessageId: String? = null,
)
