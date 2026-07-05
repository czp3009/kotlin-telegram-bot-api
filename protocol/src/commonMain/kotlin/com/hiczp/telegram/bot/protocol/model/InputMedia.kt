// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.OptIn
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
public sealed interface InputMedia
