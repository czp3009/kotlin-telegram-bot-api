// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object describes the colors of the backdrop of a unique gift.
 */
@Serializable
public data class UniqueGiftBackdropColors(
    /**
     * The color in the center of the backdrop in RGB format
     */
    @SerialName("center_color")
    public val centerColor: Long,
    /**
     * The color on the edges of the backdrop in RGB format
     */
    @SerialName("edge_color")
    public val edgeColor: Long,
    /**
     * The color to be applied to the symbol in RGB format
     */
    @SerialName("symbol_color")
    public val symbolColor: Long,
    /**
     * The color for the text on the backdrop in RGB format
     */
    @SerialName("text_color")
    public val textColor: Long,
)
