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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import kotlin.random.Random

/**
 * A high-level Telegram Bot API client that wraps [TelegramBotApi] with sensible defaults.
 *
 * This client provides:
 * - Automatic JSON serialization/deserialization using kotlinx.serialization
 * - Built-in retry logic for transient failures and rate limiting
 * - Support for Telegram's test environment
 * - Configurable error handling modes
 * - Long polling optimization
 * - File download support
 *
 * Example usage:
 * ```kotlin
 * val client = TelegramBotClient(
 *     botToken = "123456:ABC-DEF1234ghIkl-zyx57W2v1u123ew11"
 * )
 *
 * val result = client.getMe().getOrThrow()
 * println("Bot username: ${result.username}")
 * ```
 *
 * Note: For some [HttpClientEngine] implementations, you may need to explicitly close the
 * [httpClient] to release resources (e.g., threads, connections) when the client is no longer needed.
 *
 * @see TelegramBotApi
 * @see TelegramErrorResponseException
 */
class TelegramBotClient private constructor(
    /** The underlying [HttpClient] used for network requests. */
    val httpClient: HttpClient,
    api: TelegramBotApi
) : TelegramBotApi by api {
    companion object {
        /**
         * Creates a new [TelegramBotClient] instance.
         *
         * @param botToken The Telegram bot token obtained from BotFather.
         * @param httpClientEngine The Ktor [HttpClientEngine] to use for network requests.
         *   Choose an engine appropriate for your platform (e.g., CIO for JVM, Darwin for iOS).
         * @param baseUrl The base URL for the Telegram Bot API. Defaults to "https://api.telegram.org".
         *   Can be customized for local bot server.
         * @param useTestEnvironment Whether to use Telegram's test environment.
         *   See https://core.telegram.org/bots/features#creating-a-bot-in-the-test-environment
         * @param throwOnErrorResponse When `true` (default), throws [TelegramErrorResponseException]
         *   when the API returns an error response. When `false`, error responses are returned
         *   as normal [com.hiczp.telegram.bot.protocol.type.TelegramResponse] objects with `ok = false`.
         * @param coroutineDispatcher The [CoroutineDispatcher] for coroutine execution.
         *   Defaults to `null`.
         * @param additionalConfiguration Additional configuration block for the underlying [HttpClient].
         *   Use this to add custom plugins, interceptors, or modify default settings.
         */
        operator fun invoke(
            botToken: String,
            httpClientEngine: HttpClientEngine? = null,
            baseUrl: String = "https://api.telegram.org",
            useTestEnvironment: Boolean = false,
            throwOnErrorResponse: Boolean = true,
            coroutineDispatcher: CoroutineDispatcher? = null,
            additionalConfiguration: HttpClientConfig<*>.() -> Unit = {},
        ): TelegramBotClient {
            val baseUrl = URLBuilder(baseUrl).apply {
                appendPathSegments("bot${botToken}/")
                if (useTestEnvironment) appendPathSegments("test/")
            }.buildString()
            val config: HttpClientConfig<*>.() -> Unit = {
                engine {
                    if (coroutineDispatcher != null) {
                        this.dispatcher = coroutineDispatcher
                    }
                }
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
                            TelegramErrorResponseException.isRetryable(statusCode)
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
            val httpClient = if (httpClientEngine == null) {
                HttpClient(config)
            } else {
                HttpClient(httpClientEngine, config)
            }
            val api = Ktorfit.Builder().httpClient(httpClient)
                .baseUrl(baseUrl)
                .build().createTelegramBotApi()
            return TelegramBotClient(httpClient, api)
        }
    }
}
