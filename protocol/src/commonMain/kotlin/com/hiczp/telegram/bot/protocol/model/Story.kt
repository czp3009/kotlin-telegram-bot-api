// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Long
import kotlinx.serialization.Serializable

/**
 * This object represents a story.
 */
@Serializable
public data class Story(
    /**
     * Chat that posted the story
     */
    public val chat: Chat,
    /**
     * Unique identifier for the story in the chat
     */
    public val id: Long,
)
