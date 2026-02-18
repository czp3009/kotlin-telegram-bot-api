// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Double
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes the position of a clickable area within a story.
 */
@Serializable
public data class StoryAreaPosition(
    /**
     * The abscissa of the area's center, as a percentage of the media width
     */
    @SerialName("x_percentage")
    public val xPercentage: Double,
    /**
     * The ordinate of the area's center, as a percentage of the media height
     */
    @SerialName("y_percentage")
    public val yPercentage: Double,
    /**
     * The width of the area's rectangle, as a percentage of the media width
     */
    @SerialName("width_percentage")
    public val widthPercentage: Double,
    /**
     * The height of the area's rectangle, as a percentage of the media height
     */
    @SerialName("height_percentage")
    public val heightPercentage: Double,
    /**
     * The clockwise rotation angle of the rectangle, in degrees; 0-360
     */
    @SerialName("rotation_angle")
    public val rotationAngle: Double,
    /**
     * The radius of the rectangle corner rounding, as a percentage of the media width
     */
    @SerialName("corner_radius_percentage")
    public val cornerRadiusPercentage: Double,
)
