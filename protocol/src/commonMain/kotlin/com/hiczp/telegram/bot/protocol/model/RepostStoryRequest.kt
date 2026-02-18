// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RepostStoryRequest(
    /**
     * Unique identifier of the business connection
     */
    @SerialName("business_connection_id")
    public val businessConnectionId: String,
    /**
     * Unique identifier of the chat which posted the story that should be reposted
     */
    @SerialName("from_chat_id")
    public val fromChatId: Long,
    /**
     * Unique identifier of the story that should be reposted
     */
    @SerialName("from_story_id")
    public val fromStoryId: Long,
    /**
     * Period after which the story is moved to the archive, in seconds; must be one of `6 * 3600`, `12 * 3600`, `86400`, or `2 * 86400`
     */
    @SerialName("active_period")
    public val activePeriod: Long,
    /**
     * Pass *True* to keep the story accessible after it expires
     */
    @SerialName("post_to_chat_page")
    public val postToChatPage: Boolean? = null,
    /**
     * Pass *True* if the content of the story must be protected from forwarding and screenshotting
     */
    @SerialName("protect_content")
    public val protectContent: Boolean? = null,
)
