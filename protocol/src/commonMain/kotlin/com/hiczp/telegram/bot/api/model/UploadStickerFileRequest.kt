// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public data class UploadStickerFileRequest(
    /**
     * User identifier of sticker file owner
     */
    @SerialName("user_id")
    public val userId: Long,
    /**
     * A file with the sticker in .WEBP, .PNG, .TGS, or .WEBM format. See [](https://core.telegram.org/stickers)[https://core.telegram.org/stickers](https://core.telegram.org/stickers) for technical requirements. [More information on Sending Files ](https://core.telegram.org/bots/api#sending-files)
     */
    public val sticker: JsonElement?,
    /**
     * Format of the sticker, must be one of “static”, “animated”, “video”
     */
    @SerialName("sticker_format")
    public val stickerFormat: String,
)
