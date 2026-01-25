// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object contains information about the color scheme for a user's name, message replies and link previews based on a unique gift.
 */
@Serializable
public data class UniqueGiftColors(
    /**
     * Custom emoji identifier of the unique gift's model
     */
    @SerialName("model_custom_emoji_id")
    public val modelCustomEmojiId: String,
    /**
     * Custom emoji identifier of the unique gift's symbol
     */
    @SerialName("symbol_custom_emoji_id")
    public val symbolCustomEmojiId: String,
    /**
     * Main color used in light themes; RGB format
     */
    @SerialName("light_theme_main_color")
    public val lightThemeMainColor: Long,
    /**
     * List of 1-3 additional colors used in light themes; RGB format
     */
    @SerialName("light_theme_other_colors")
    public val lightThemeOtherColors: List<Long>,
    /**
     * Main color used in dark themes; RGB format
     */
    @SerialName("dark_theme_main_color")
    public val darkThemeMainColor: Long,
    /**
     * List of 1-3 additional colors used in dark themes; RGB format
     */
    @SerialName("dark_theme_other_colors")
    public val darkThemeOtherColors: List<Long>,
)
