// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.form

import com.hiczp.telegram.bot.protocol.model.InputSticker
import io.ktor.client.request.forms.ChannelProvider
import io.ktor.client.request.forms.FormPart
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName

public data class CreateNewStickerSetForm(
    /**
     * User identifier of created sticker set owner
     */
    @SerialName("user_id")
    public val userId: Long,
    /**
     * Short name of sticker set, to be used in `t.me/addstickers/` URLs (e.g., *animals*). Can contain only English letters, digits and underscores. Must begin with a letter, can't contain consecutive underscores and must end in `"_by_<bot_username>"`. `<bot_username>` is case insensitive. 1-64 characters.
     */
    public val name: String,
    /**
     * Sticker set title, 1-64 characters
     */
    public val title: String,
    /**
     * A JSON-serialized list of 1-50 initial stickers to be added to the sticker set
     */
    public val stickers: List<InputSticker>,
    /**
     * Type of stickers in the set, pass “regular”, “mask”, or “custom_emoji”. By default, a regular sticker set is created.
     */
    @SerialName("sticker_type")
    public val stickerType: String? = null,
    /**
     * Pass *True* if stickers in the sticker set must be repainted to the color of text when used in messages, the accent color if used as emoji status, white on chat photos, or another appropriate color based on context; for custom emoji sticker sets only
     */
    @SerialName("needs_repainting")
    public val needsRepainting: Boolean? = null,
    /**
     * Additional file attachments referenced via attach://<file_attach_name> in media fields
     */
    public val attachments: List<FormPart<ChannelProvider>>? = null,
)
