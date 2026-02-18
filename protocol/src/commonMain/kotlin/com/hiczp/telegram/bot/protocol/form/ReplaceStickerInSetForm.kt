// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.form

import com.hiczp.telegram.bot.protocol.model.InputSticker
import io.ktor.client.request.forms.*
import kotlinx.serialization.SerialName

public data class ReplaceStickerInSetForm(
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
    /**
     * Additional file attachments referenced via attach://<file_attach_name> in media fields
     */
    public val attachments: List<FormPart<ChannelProvider>>? = null,
)
