// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Describes a clickable area on a story media.
 */
@Serializable
public data class StoryArea(
    /**
     * Position of the area
     */
    public val position: JsonElement?,
    /**
     * Type of the area
     */
    public val type: JsonElement?,
)
