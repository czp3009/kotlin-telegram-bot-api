package com.hiczp.telegram.bot.protocol.plugin

import com.hiczp.telegram.bot.protocol.type.TelegramErrorResponse
import io.ktor.client.call.*
import io.ktor.client.plugins.api.*
import io.ktor.http.*

/**
 * A Ktor client plugin that converts Telegram Bot API error responses into exceptions.
 *
 * ## Purpose
 *
 * This plugin enables a try-catch error handling paradigm by automatically parsing Telegram Bot API
 * error responses and throwing [com.hiczp.telegram.bot.protocol.exception.TelegramErrorResponseException].
 * Without this plugin, you would need to manually check the `ok` field in every response and handle
 * errors imperatively.
 *
 * ## Usage
 *
 * ```kotlin
 * val client = HttpClient(CIO) {
 *     install(TelegramServerErrorPlugin)
 * }
 *
 * // Errors are automatically thrown as exceptions
 * try {
 *     val response = client.sendMessage(chatId = 123, text = "Hello")
 *     // Handle successful response
 * } catch (e: TelegramErrorResponseException) {
 *     // Handle error (e.g., log error, notify user)
 *     println("Error: ${e.description}")
 * }
 * ```
 *
 * ## Interaction with Ktor's `expectSuccess`
 *
 * **Do not enable Ktor's `expectSuccess` option when using this plugin.**
 *
 * When `expectSuccess = true` is enabled, Ktor throws [io.ktor.client.plugins.ResponseException]
 * for non-2xx HTTP status codes before this plugin can process the response. This would require
 * you to catch both `ResponseException` and `TelegramErrorResponseException`, which defeats the
 * purpose of this plugin.
 *
 * Telegram Bot API returns errors with HTTP 200 OK and a JSON body containing error details.
 * This plugin is designed to handle that pattern by parsing the JSON response and converting
 * errors to exceptions.
 *
 * ## Retry Behavior
 *
 * This plugin throws exceptions in the `onResponse` phase (after the HTTP response is received).
 * This has important implications when using Ktor's [io.ktor.client.plugins.HttpRequestRetry] plugin:
 *
 * - The `HttpResponse` in `HttpRetryDelayContext` will be `null` because the exception is thrown
 *   after response processing.
 * - The `cause` will contain the [com.hiczp.telegram.bot.protocol.exception.TelegramErrorResponseException].
 *
 * To properly retry failed requests, configure the retry plugin to check `cause.isRetryable`
 * and use the `retryAfter` value from the error response for rate limiting errors (HTTP 429):
 *
 * ```kotlin
 * val client = HttpClient(CIO) {
 *     install(TelegramServerErrorPlugin)
 *     install(HttpRequestRetry) {
 *         retryOnExceptionIf { request, response, cause ->
 *             cause is TelegramErrorResponseException && cause.isRetryable
 *         }
 *         delayMillis {
 *             (cause as? TelegramErrorResponseException)?.parameters?.retryAfter?.times(1000) ?: 1_000
 *         }
 *     }
 * }
 * ```
 *
 * @see com.hiczp.telegram.bot.protocol.exception.TelegramErrorResponseException
 * @see com.hiczp.telegram.bot.protocol.type.TelegramResponse
 * @see io.ktor.client.plugins.HttpRequestRetry
 */
val TelegramServerErrorPlugin = createClientPlugin("TelegramServerErrorPlugin") {
    onResponse { response ->
        if (response.contentType()?.match(ContentType.Application.Json) != true) return@onResponse
        val telegramErrorResponse = response.body<TelegramErrorResponse>()
        val exception = telegramErrorResponse.toTelegramResponse().exceptionOrNull()
        if (exception != null) throw exception
    }
}
