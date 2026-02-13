// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.model

import com.hiczp.telegram.bot.api.type.IncomingUpdate
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a result of an inline query that was chosen by the user and sent to their chat partner.
 * Note: It is necessary to enable inline feedback via @BotFather in order to receive these objects in updates.
 */
@Serializable
public data class ChosenInlineResult(
    /**
     * The unique identifier for the result that was chosen
     */
    @SerialName("result_id")
    public val resultId: String,
    /**
     * The user that chose the result
     */
    public val from: User,
    /**
     * *Optional*. Sender location, only for bots that require user location
     */
    public val location: Location? = null,
    /**
     * *Optional*. Identifier of the sent inline message. Available only if there is an [inline keyboard](https://core.telegram.org/bots/api#inlinekeyboardmarkup) attached to the message. Will be also received in [callback queries](https://core.telegram.org/bots/api#callbackquery) and can be used to [edit](https://core.telegram.org/bots/api#updating-messages) the message.
     */
    @SerialName("inline_message_id")
    public val inlineMessageId: String? = null,
    /**
     * The query that was used to obtain the result
     */
    public val query: String,
) : IncomingUpdate
