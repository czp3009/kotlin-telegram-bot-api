// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public data class SavePreparedInlineMessageRequest(
    /**
     * Unique identifier of the target user that can use the prepared message
     */
    @SerialName("user_id")
    public val userId: Long,
    /**
     * A JSON-serialized object describing the message to be sent
     */
    public val result: JsonElement?,
    /**
     * Pass *True* if the message can be sent to private chats with users
     */
    @SerialName("allow_user_chats")
    public val allowUserChats: Boolean? = null,
    /**
     * Pass *True* if the message can be sent to private chats with bots
     */
    @SerialName("allow_bot_chats")
    public val allowBotChats: Boolean? = null,
    /**
     * Pass *True* if the message can be sent to group and supergroup chats
     */
    @SerialName("allow_group_chats")
    public val allowGroupChats: Boolean? = null,
    /**
     * Pass *True* if the message can be sent to channel chats
     */
    @SerialName("allow_channel_chats")
    public val allowChannelChats: Boolean? = null,
)
