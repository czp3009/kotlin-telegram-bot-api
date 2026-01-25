// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public data class SetStickerMaskPositionRequest(
    /**
     * File identifier of the sticker
     */
    public val sticker: String,
    /**
     * A JSON-serialized object with the position where the mask should be placed on faces. Omit the parameter to remove the mask position.
     */
    @SerialName("mask_position")
    public val maskPosition: JsonElement? = null,
)
