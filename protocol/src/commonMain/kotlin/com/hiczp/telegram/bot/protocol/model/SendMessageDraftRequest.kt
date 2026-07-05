// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SendMessageDraftRequest(
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
     * Text of the message to be sent, 0-4096 characters after entities parsing. Pass an empty text to show a “Thinking…” placeholder.
     */
    public val text: String? = null,
    /**
     * Mode for parsing entities in the message text. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
     */
    @SerialName("parse_mode")
    public val parseMode: String? = null,
    /**
     * A JSON-serialized list of special entities that appear in message text, which can be specified instead of *parse_mode*
     */
    public val entities: List<MessageEntity>? = null,
)
