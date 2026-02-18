// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Long
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about a video chat ended in the chat.
 */
@Serializable
public data class VideoChatEnded(
    /**
     * Video chat duration in seconds
     */
    public val duration: Long,
)
