// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.form

import com.hiczp.telegram.bot.protocol.model.InputSticker
import io.ktor.client.request.forms.ChannelProvider
import io.ktor.client.request.forms.FormPart
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName

public data class AddStickerToSetForm(
    /**
     * User identifier of sticker set owner
     */
    @SerialName("user_id")
    public val userId: Long,
    /**
     * Sticker set name
     */
    public val name: String,
    /**
     * A JSON-serialized object with information about the added sticker. If exactly the same sticker had already been added to the set, then the set isn't changed.
     */
    public val sticker: InputSticker,
    /**
     * Additional file attachments referenced via attach://<file_attach_name> in media fields
     */
    public val attachments: List<FormPart<ChannelProvider>>? = null,
)
