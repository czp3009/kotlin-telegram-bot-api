// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * This object describes the way a background is filled based on the selected colors. Currently, it can be one of
 * BackgroundFillSolid BackgroundFillGradient BackgroundFillFreeformGradient
 */
@Serializable
@JsonClassDiscriminator("type")
@OptIn(ExperimentalSerializationApi::class)
public sealed interface BackgroundFill

/**
 * The background is filled using the selected color.
 */
@Serializable
@SerialName("solid")
public data class BackgroundFillSolid(
    /**
     * The color of the background fill in the RGB24 format
     */
    public val color: Long,
) : BackgroundFill

/**
 * The background is a gradient fill.
 */
@Serializable
@SerialName("gradient")
public data class BackgroundFillGradient(
    /**
     * Top color of the gradient in the RGB24 format
     */
    @SerialName("top_color")
    public val topColor: Long,
    /**
     * Bottom color of the gradient in the RGB24 format
     */
    @SerialName("bottom_color")
    public val bottomColor: Long,
    /**
     * Clockwise rotation angle of the background fill in degrees; 0-359
     */
    @SerialName("rotation_angle")
    public val rotationAngle: Long,
) : BackgroundFill

/**
 * The background is a freeform gradient that rotates after every message in the chat.
 */
@Serializable
@SerialName("freeform_gradient")
public data class BackgroundFillFreeformGradient(
    /**
     * A list of the 3 or 4 base colors that are used to generate the freeform gradient in the RGB24 format
     */
    public val colors: List<Long>,
) : BackgroundFill
