// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import com.hiczp.telegram.bot.protocol.type.IncomingUpdate
import kotlinx.serialization.Serializable

/**
 * This object represents a boost added to a chat or changed.
 */
@Serializable
public data class ChatBoostUpdated(
    /**
     * Chat which was boosted
     */
    public val chat: Chat,
    /**
     * Information about the chat boost
     */
    public val boost: ChatBoost,
) : IncomingUpdate
