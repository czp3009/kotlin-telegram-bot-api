// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SendRichMessageDraftRequest(
    /**
     * Unique identifier for the target private chat
     */
    @SerialName("chat_id")
    public val chatId: Long,
    /**
     * Unique identifier for the target message thread
     */
    @SerialName("message_thread_id")
    public val messageThreadId: Long? = null,
    /**
     * Unique identifier of the message draft; must be non-zero. Changes to drafts with the same identifier are animated.
     */
    @SerialName("draft_id")
    public val draftId: Long,
    /**
     * The partial message to be streamed
     */
    @SerialName("rich_message")
    public val richMessage: InputRichMessage,
)
