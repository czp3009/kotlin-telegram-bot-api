// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about an edited forum topic.
 */
@Serializable
public data class ForumTopicEdited(
    /**
     * *Optional*. New name of the topic, if it was edited
     */
    public val name: String? = null,
    /**
     * *Optional*. New identifier of the custom emoji shown as the topic icon, if it was edited; an empty string if the icon was removed
     */
    @SerialName("icon_custom_emoji_id")
    public val iconCustomEmojiId: String? = null,
)
