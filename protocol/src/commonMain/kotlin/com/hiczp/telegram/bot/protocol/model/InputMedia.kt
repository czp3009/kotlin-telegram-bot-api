// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * This object represents the content of a media message to be sent. It should be one of
 * InputMediaAnimation InputMediaAudio InputMediaDocument InputMediaLivePhoto InputMediaPhoto InputMediaVideo
 */
@Serializable
@JsonClassDiscriminator("type")
@OptIn(ExperimentalSerializationApi::class)
public sealed interface InputMedia {
    public val caption: String?

    /**
     * *Optional*. List of special entities that appear in the caption, which can be specified instead of *parse_mode*
     */
    public val captionEntities: List<MessageEntity>?

    public val media: String

    public val parseMode: String?
}
