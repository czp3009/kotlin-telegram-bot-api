package com.hiczp.telegram.bot.protocol.exception

import com.hiczp.telegram.bot.protocol.model.ResponseParameters
import kotlinx.serialization.Serializable

/**
 * Exception representing an error received from the Telegram server.
 * This exception can be serialized for transmission to other microservices.
 */
@Serializable
data class TelegramErrorResponseException(
    val description: String,
    val errorCode: Int,
    val parameters: ResponseParameters? = null,
) : RuntimeException() {
    override val message: String
        get() = description
}

val TelegramErrorResponseException.isRetryable get() = errorCode == 429 || errorCode in 500..599
