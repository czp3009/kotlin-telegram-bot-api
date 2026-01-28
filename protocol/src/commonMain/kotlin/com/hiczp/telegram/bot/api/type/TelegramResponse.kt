package com.hiczp.telegram.bot.api.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Generic wrapper for Telegram Bot API responses
 */
@Serializable
data class TelegramResponse<T>(
    /**
     * True if the request was successful
     */
    val ok: Boolean,
    /**
     * The result of the request, if successful
     */
    val result: T? = null,
    /**
     * Human-readable description of the result or error
     */
    val description: String? = null,
    /**
     * Error code, if the request failed
     */
    @SerialName("error_code")
    val errorCode: Int? = null,
)
