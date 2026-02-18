// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * This object describes the type of a background. Currently, it can be one of
 * BackgroundTypeFill BackgroundTypeWallpaper BackgroundTypePattern BackgroundTypeChatTheme
 */
@Serializable
@JsonClassDiscriminator("type")
@OptIn(ExperimentalSerializationApi::class)
public sealed interface BackgroundType

/**
 * The background is automatically filled based on the selected colors.
 */
@Serializable
@SerialName("fill")
public data class BackgroundTypeFill(
    /**
     * The background fill
     */
    public val fill: BackgroundFill,
    /**
     * Dimming of the background in dark themes, as a percentage; 0-100
     */
    @SerialName("dark_theme_dimming")
    public val darkThemeDimming: Long,
) : BackgroundType

/**
 * The background is a wallpaper in the JPEG format.
 */
@Serializable
@SerialName("wallpaper")
public data class BackgroundTypeWallpaper(
    /**
     * Document with the wallpaper
     */
    public val document: Document,
    /**
     * Dimming of the background in dark themes, as a percentage; 0-100
     */
    @SerialName("dark_theme_dimming")
    public val darkThemeDimming: Long,
    /**
     * *Optional*. *True*, if the wallpaper is downscaled to fit in a 450x450 square and then box-blurred with radius 12
     */
    @SerialName("is_blurred")
    public val isBlurred: Boolean? = null,
    /**
     * *Optional*. *True*, if the background moves slightly when the device is tilted
     */
    @SerialName("is_moving")
    public val isMoving: Boolean? = null,
) : BackgroundType

/**
 * The background is a .PNG or .TGV (gzipped subset of SVG with MIME type “application/x-tgwallpattern”) pattern to be combined with the background fill chosen by the user.
 */
@Serializable
@SerialName("pattern")
public data class BackgroundTypePattern(
    /**
     * Document with the pattern
     */
    public val document: Document,
    /**
     * The background fill that is combined with the pattern
     */
    public val fill: BackgroundFill,
    /**
     * Intensity of the pattern when it is shown above the filled background; 0-100
     */
    public val intensity: Long,
    /**
     * *Optional*. *True*, if the background fill must be applied only to the pattern itself. All other pixels are black in this case. For dark themes only
     */
    @SerialName("is_inverted")
    public val isInverted: Boolean? = null,
    /**
     * *Optional*. *True*, if the background moves slightly when the device is tilted
     */
    @SerialName("is_moving")
    public val isMoving: Boolean? = null,
) : BackgroundType

/**
 * The background is taken directly from a built-in chat theme.
 */
@Serializable
@SerialName("chat_theme")
public data class BackgroundTypeChatTheme(
    /**
     * Name of the chat theme, which is usually an emoji
     */
    @SerialName("theme_name")
    public val themeName: String,
) : BackgroundType
