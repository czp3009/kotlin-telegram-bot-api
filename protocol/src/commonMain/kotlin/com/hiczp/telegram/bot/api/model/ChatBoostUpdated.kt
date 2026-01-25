// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * This object represents a boost added to a chat or changed.
 */
@Serializable
public data class ChatBoostUpdated(
    /**
     * Chat which was boosted
     */
    public val chat: JsonElement?,
    /**
     * Information about the chat boost
     */
    public val boost: JsonElement?,
)
