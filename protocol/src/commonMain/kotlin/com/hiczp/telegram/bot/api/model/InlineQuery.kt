// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * This object represents an incoming inline query. When the user sends an empty query, your bot could return some default or trending results.
 */
@Serializable
public data class InlineQuery(
    /**
     * Unique identifier for this query
     */
    public val id: String,
    /**
     * Sender
     */
    public val from: JsonElement?,
    /**
     * Text of the query (up to 256 characters)
     */
    public val query: String,
    /**
     * Offset of the results to be returned, can be controlled by the bot
     */
    public val offset: String,
    /**
     * *Optional*. Type of the chat from which the inline query was sent. Can be either “sender” for a private chat with the inline query sender, “private”, “group”, “supergroup”, or “channel”. The chat type should be always known for requests sent from official clients and most third-party clients, unless the request was sent from a secret chat
     */
    @SerialName("chat_type")
    public val chatType: String? = null,
    /**
     * *Optional*. Sender location, only for bots that request user location
     */
    public val location: JsonElement? = null,
)
