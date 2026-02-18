// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Double
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * This object describes a profile photo to set. Currently, it can be one of
 * InputProfilePhotoStatic InputProfilePhotoAnimated
 */
@Serializable
@JsonClassDiscriminator("type")
@OptIn(ExperimentalSerializationApi::class)
public sealed interface InputProfilePhoto

/**
 * A static profile photo in the .JPG format.
 */
@Serializable
@SerialName("static")
public data class InputProfilePhotoStatic(
    /**
     * The static profile photo. Profile photos can't be reused and can only be uploaded as a new file, so you can pass “attach://<file_attach_name>” if the photo was uploaded using multipart/form-data under <file_attach_name>. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
     */
    public val photo: String,
) : InputProfilePhoto

/**
 * An animated profile photo in the MPEG4 format.
 */
@Serializable
@SerialName("animated")
public data class InputProfilePhotoAnimated(
    /**
     * The animated profile photo. Profile photos can't be reused and can only be uploaded as a new file, so you can pass “attach://<file_attach_name>” if the photo was uploaded using multipart/form-data under <file_attach_name>. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
     */
    public val animation: String,
    /**
     * *Optional*. Timestamp in seconds of the frame that will be used as the static profile photo. Defaults to 0.0.
     */
    @SerialName("main_frame_timestamp")
    public val mainFrameTimestamp: Double? = null,
) : InputProfilePhoto
