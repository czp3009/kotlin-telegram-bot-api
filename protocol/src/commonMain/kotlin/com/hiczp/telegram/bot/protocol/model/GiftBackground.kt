// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object describes the background of a gift.
 */
@Serializable
public data class GiftBackground(
    /**
     * Center color of the background in RGB format
     */
    @SerialName("center_color")
    public val centerColor: Long,
    /**
     * Edge color of the background in RGB format
     */
    @SerialName("edge_color")
    public val edgeColor: Long,
    /**
     * Text color of the background in RGB format
     */
    @SerialName("text_color")
    public val textColor: Long,
)
