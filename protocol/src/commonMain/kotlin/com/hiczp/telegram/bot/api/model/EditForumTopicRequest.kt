// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class EditForumTopicRequest(
    /**
     * Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
     */
    @SerialName("chat_id")
    public val chatId: String,
    /**
     * Unique identifier for the target message thread of the forum topic
     */
    @SerialName("message_thread_id")
    public val messageThreadId: Long,
    /**
     * New topic name, 0-128 characters. If not specified or empty, the current name of the topic will be kept
     */
    public val name: String? = null,
    /**
     * New unique identifier of the custom emoji shown as the topic icon. Use [getForumTopicIconStickers](https://core.telegram.org/bots/api#getforumtopiciconstickers) to get all allowed custom emoji identifiers. Pass an empty string to remove the icon. If not specified, the current icon will be kept
     */
    @SerialName("icon_custom_emoji_id")
    public val iconCustomEmojiId: String? = null,
)
