// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.form

import com.hiczp.telegram.bot.protocol.type.InputFile
import kotlin.String
import kotlinx.serialization.SerialName

public data class SetChatPhotoForm(
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    @SerialName("chat_id")
    public val chatId: String,
    /**
     * New chat photo, uploaded using multipart/form-data
     */
    public val photo: InputFile,
)
