// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object contains information about one answer option in a poll to be sent.
 */
@Serializable
public data class InputPollOption(
    /**
     * Option text, 1-100 characters
     */
    public val text: String,
    /**
     * *Optional*. Mode for parsing entities in the text. See [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details. Currently, only custom emoji entities are allowed
     */
    @SerialName("text_parse_mode")
    public val textParseMode: String? = null,
    /**
     * *Optional*. A JSON-serialized list of special entities that appear in the poll option text. It can be specified instead of *text_parse_mode*
     */
    @SerialName("text_entities")
    public val textEntities: List<MessageEntity>? = null,
)
