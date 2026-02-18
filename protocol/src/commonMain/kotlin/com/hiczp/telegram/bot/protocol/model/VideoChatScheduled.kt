// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about a video chat scheduled in the chat.
 */
@Serializable
public data class VideoChatScheduled(
    /**
     * Point in time (Unix timestamp) when the video chat is supposed to be started by a chat administrator
     */
    @SerialName("start_date")
    public val startDate: Long,
)
