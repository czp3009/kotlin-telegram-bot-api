// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ReplaceStickerInSetRequest(
    /**
     * User identifier of the sticker set owner
     */
    @SerialName("user_id")
    public val userId: Long,
    /**
     * Sticker set name
     */
    public val name: String,
    /**
     * File identifier of the replaced sticker
     */
    @SerialName("old_sticker")
    public val oldSticker: String,
    /**
     * A JSON-serialized object with information about the added sticker. If exactly the same sticker had already been added to the set, then the set remains unchanged.
     */
    public val sticker: InputSticker,
)
