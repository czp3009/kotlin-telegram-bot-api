package com.hiczp.telegram.bot.client

import com.hiczp.telegram.bot.protocol.TelegramBotApi
import com.hiczp.telegram.bot.protocol.createTelegramBotApi
import com.hiczp.telegram.bot.protocol.exception.TelegramErrorResponseException
import com.hiczp.telegram.bot.protocol.exception.isRetryable
import com.hiczp.telegram.bot.protocol.plugin.TelegramFileDownloadPlugin
import com.hiczp.telegram.bot.protocol.plugin.TelegramLongPollingPlugin
import com.hiczp.telegram.bot.protocol.plugin.TelegramServerErrorPlugin
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.utils.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json
import kotlin.random.Random

/**
 * A high-level Telegram Bot API client that wraps [TelegramBotApi] with sensible defaults.
 *
 * This client provides:
 * - Automatic JSON serialization/derialization using kotlinx.serialization
 * - Built-in retry logic for transient failures and rate limiting
 * - Support for Telegram's test environment
 * - Configurable error handling modes
 * - Long polling optimization
 * - File download support
 *
 * Example usage:
 * ```kotlin
 * val client = TelegramBotClient(
 *     ktorEngine = CIO.create(),
 *     botToken = "123456:ABC-DEF1234ghIkl-zyx57W2v1u123ew11"
 * )
 *
 * val response = client.getMe()
 * if (response.ok) {
 *     println("Bot username: ${response.result.username}")
 * }
 * ```
 *
 * @param ktorEngine The Ktor [HttpClientEngine] to use for network requests.
 *   Choose an engine appropriate for your platform (e.g., CIO for JVM, Darwin for iOS).
 * @param botToken The Telegram bot token obtained from BotFather.
 * @param baseUrl The base URL for the Telegram Bot API. Defaults to "https://api.telegram.org".
 *   Can be customized for local bot server.
 * @param useTestEnvironment Whether to use Telegram's test environment.
 *   See https://core.telegram.org/bots/features#creating-a-bot-in-the-test-environment
 * @param throwOnErrorResponse When `true` (default), throws [TelegramErrorResponseException]
 *   when the API returns an error response. When `false`, error responses are returned
 *   as normal [com.hiczp.telegram.bot.protocol.type.TelegramResponse] objects with `ok = false`.
 * @param additionalConfiguration Additional configuration block for the underlying [HttpClient].
 *   Use this to add custom plugins, interceptors, or modify default settings.
 *
 * @see TelegramBotApi
 * @see TelegramErrorResponseException
 */
class TelegramBotClient(
    ktorEngine: HttpClientEngine,
    botToken: String,
    baseUrl: String = "https://api.telegram.org",
    useTestEnvironment: Boolean = false,
    throwOnErrorResponse: Boolean = true,
    additionalConfiguration: HttpClientConfig<*>.() -> Unit = {},
) : TelegramBotApi by buildTelegramBotApi(
    ktorEngine,
    botToken,
    baseUrl,
    useTestEnvironment,
    throwOnErrorResponse,
    additionalConfiguration
) {
    companion object {
        private fun buildTelegramBotApi(
            ktorEngine: HttpClientEngine,
            botToken: String,
            baseUrl: String,
            useTestEnvironment: Boolean = false,
            throwOnErrorResponse: Boolean = true,
            additionalConfiguration: HttpClientConfig<*>.() -> Unit,
        ): TelegramBotApi {
            val baseUrl = URLBuilder(baseUrl).apply {
                appendPathSegments("bot${botToken}/")
                if (useTestEnvironment) appendPathSegments("test/")
            }.buildString()
            val httpClient = HttpClient(ktorEngine) {
                install(ContentNegotiation) {
                    json(Json(DefaultJson) {
                        ignoreUnknownKeys = true
                        encodeDefaults = true
                        explicitNulls = false
                    })
                }
                install(DefaultRequest) {
                    headers.appendIfNameAbsent(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                }
                if (throwOnErrorResponse) {
                    install(HttpRequestRetry) {
                        retryOnExceptionIf(maxRetries = 3) { _, throwable ->
                            val cause = throwable.unwrapCancellationException()
                            if (cause is HttpRequestTimeoutException || cause is ConnectTimeoutException || cause is SocketTimeoutException) return@retryOnExceptionIf true
                            if (cause is TelegramErrorResponseException) return@retryOnExceptionIf cause.isRetryable
                            if (throwable is CancellationException) return@retryOnExceptionIf false
                            true
                        }
                        delayMillis {
                            (cause as? TelegramErrorResponseException)?.parameters?.retryAfter?.times(1000)
                                ?: (1_000 + Random.nextLong(1_000L))
                        }
                    }
                    install(TelegramServerErrorPlugin)
                } else {
                    install(HttpRequestRetry) {
                        retryIf(maxRetries = 3) { _, response ->
                            val statusCode = response.status.value
                            statusCode == 429 || statusCode in 500..599
                        }
                        retryOnException(maxRetries = 3, retryOnTimeout = true)
                        //Telegram server always returns Retry-After header
                        constantDelay(millis = 1_000, respectRetryAfterHeader = true)
                    }
                }
                install(HttpTimeout) {
                    requestTimeoutMillis = 15_000
                    connectTimeoutMillis = 10_000
                    socketTimeoutMillis = 10_000
                }
                install(TelegramLongPollingPlugin)
                install(TelegramFileDownloadPlugin)
                additionalConfiguration()
            }
            return Ktorfit.Builder().httpClient(httpClient)
                .baseUrl(baseUrl)
                .build().createTelegramBotApi()
        }
    }
}
