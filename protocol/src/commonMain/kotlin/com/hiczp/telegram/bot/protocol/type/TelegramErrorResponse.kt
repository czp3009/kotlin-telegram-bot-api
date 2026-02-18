package com.hiczp.telegram.bot.protocol.type

import com.hiczp.telegram.bot.protocol.model.ResponseParameters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Internal representation for deserializing Telegram API error responses.
 *
 * This type is used to avoid deserializing the `result` field when the server returns an error response.
 * The regular [TelegramResponse] type always expects a `result` field to be present, but error responses
 * from Telegram don't include this field.
 */
@Serializable
internal data class TelegramErrorResponse(
    val ok: Boolean,
    val description: String? = null,
    @SerialName("error_code")
    val errorCode: Int? = null,
    val parameters: ResponseParameters? = null
) {
    fun toTelegramResponse() = TelegramResponse<Nothing>(
        ok = ok,
        description = description,
        errorCode = errorCode,
        parameters = parameters,
    )
}
