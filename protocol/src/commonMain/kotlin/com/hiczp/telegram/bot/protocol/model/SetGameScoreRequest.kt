// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SetGameScoreRequest(
    /**
     * User identifier
     */
    @SerialName("user_id")
    public val userId: Long,
    /**
     * New score, must be non-negative
     */
    public val score: Long,
    /**
     * Pass *True* if the high score is allowed to decrease. This can be useful when fixing mistakes or banning cheaters
     */
    public val force: Boolean? = null,
    /**
     * Pass *True* if the game message should not be automatically edited to include the current scoreboard
     */
    @SerialName("disable_edit_message")
    public val disableEditMessage: Boolean? = null,
    /**
     * Required if *inline_message_id* is not specified. Unique identifier for the target chat
     */
    @SerialName("chat_id")
    public val chatId: Long? = null,
    /**
     * Required if *inline_message_id* is not specified. Identifier of the sent message
     */
    @SerialName("message_id")
    public val messageId: Long? = null,
    /**
     * Required if *chat_id* and *message_id* are not specified. Identifier of the inline message
     */
    @SerialName("inline_message_id")
    public val inlineMessageId: String? = null,
)
