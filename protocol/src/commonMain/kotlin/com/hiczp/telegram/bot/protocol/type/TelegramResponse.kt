package com.hiczp.telegram.bot.protocol.type

import com.hiczp.telegram.bot.protocol.exception.TelegramErrorResponseException
import com.hiczp.telegram.bot.protocol.model.ResponseParameters
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
    /**
     * Additional parameters that can help automatically handle the error.
     * Present when the request fails (e.g., for rate limiting with retry_after).
     */
    val parameters: ResponseParameters? = null,
) {
    fun getOrThrow(): T {
        if (ok) return checkResult()
        throw createException()
    }

    fun exceptionOrNull(): Throwable? {
        if (ok) return null
        return createException()
    }

    suspend fun onSuccess(action: suspend (value: T) -> Unit): TelegramResponse<T> {
        if (ok) action(checkResult())
        return this
    }

    suspend fun onError(action: suspend (exception: Throwable) -> Unit): TelegramResponse<T> {
        exceptionOrNull()?.let { action(it) }
        return this
    }

    suspend fun <R> fold(
        onSuccess: suspend (value: T) -> R,
        onError: suspend (exception: TelegramErrorResponseException) -> R,
    ) = if (ok) {
        onSuccess(checkResult())
    } else {
        onError(createException())
    }

    fun toResult() = if (ok) {
        Result.success(checkResult())
    } else {
        Result.failure(createException())
    }

    private fun checkResult() =
        checkNotNull(result) { "Telegram server returned success but result is null" }

    private fun checkDescription() =
        checkNotNull(description) { "Telegram server returned error but description is null" }

    private fun checkErrorCode() =
        checkNotNull(errorCode) { "Telegram server returned error but error code is null" }

    private fun createException() =
        TelegramErrorResponseException(checkDescription(), checkErrorCode(), parameters)
}

val <T> TelegramResponse<T>.isRetryable get() = errorCode == 429 || errorCode in 500..599
