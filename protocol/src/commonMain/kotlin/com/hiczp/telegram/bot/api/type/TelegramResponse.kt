// Auto-generated from Swagger specificationDo not modify this file manually
package com.hiczp.telegram.bot.api.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Generic wrapper for Telegram Bot API responses
 */
@Serializable
public data class TelegramResponse<T>(
    /**
     * True if the request was successful
     */
    public val ok: Boolean,
    /**
     * The result of the request, if successful
     */
    public val result: T? = null,
    /**
     * Human-readable description of the result or error
     */
    public val description: String? = null,
    /**
     * Error code, if the request failed
     */
    @SerialName("error_code")
    public val errorCode: Int? = null,
)
