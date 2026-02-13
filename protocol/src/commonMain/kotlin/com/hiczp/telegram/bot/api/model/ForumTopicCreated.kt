// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about a new forum topic created in the chat.
 */
@Serializable
public data class ForumTopicCreated(
    /**
     * Name of the topic
     */
    public val name: String,
    /**
     * Color of the topic icon in RGB format
     */
    @SerialName("icon_color")
    public val iconColor: Long,
    /**
     * *Optional*. Unique identifier of the custom emoji shown as the topic icon
     */
    @SerialName("icon_custom_emoji_id")
    public val iconCustomEmojiId: String? = null,
    /**
     * *Optional*. *True*, if the name of the topic wasn't specified explicitly by its creator and likely needs to be changed by the bot
     */
    @SerialName("is_name_implicit")
    public val isNameImplicit: Boolean? = null,
)
