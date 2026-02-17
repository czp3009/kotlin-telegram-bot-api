// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.api.form

import com.hiczp.telegram.bot.api.type.InputFile
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName

public data class UploadStickerFileForm(
    /**
     * User identifier of sticker file owner
     */
    @SerialName("user_id")
    public val userId: Long,
    /**
     * A file with the sticker in .WEBP, .PNG, .TGS, or .WEBM format. See [](https://core.telegram.org/stickers)[https://core.telegram.org/stickers](https://core.telegram.org/stickers) for technical requirements. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
     */
    public val sticker: InputFile,
    /**
     * Format of the sticker, must be one of “static”, “animated”, “video”
     */
    @SerialName("sticker_format")
    public val stickerFormat: String,
)
