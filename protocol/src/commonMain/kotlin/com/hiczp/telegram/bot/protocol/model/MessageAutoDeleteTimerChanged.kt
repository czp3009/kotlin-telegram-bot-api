// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about a change in auto-delete timer settings.
 */
@Serializable
public data class MessageAutoDeleteTimerChanged(
    /**
     * New auto-delete time for messages in the chat; in seconds
     */
    @SerialName("message_auto_delete_time")
    public val messageAutoDeleteTime: Long,
)
