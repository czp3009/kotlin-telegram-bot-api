// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Represents a location to which a chat is connected.
 */
@Serializable
public data class ChatLocation(
    /**
     * The location to which the supergroup is connected. Can't be a live location.
     */
    public val location: JsonElement?,
    /**
     * Location address; 1-64 characters, as defined by the chat owner
     */
    public val address: String,
)
