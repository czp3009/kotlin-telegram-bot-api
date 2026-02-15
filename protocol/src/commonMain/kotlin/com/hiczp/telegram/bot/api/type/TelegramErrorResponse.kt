package com.hiczp.telegram.bot.api.type

import com.hiczp.telegram.bot.api.model.ResponseParameters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//for avoidance to deserialization "result" if server returns success response
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
