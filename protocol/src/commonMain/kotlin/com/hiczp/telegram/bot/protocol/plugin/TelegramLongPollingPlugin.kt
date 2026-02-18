package com.hiczp.telegram.bot.protocol.plugin

import io.ktor.client.plugins.*
import io.ktor.client.plugins.api.*

/**
 * A Ktor client plugin that configures long polling timeout for the Telegram Bot API.
 *
 * This plugin automatically increases the timeout for the [getUpdates][com.hiczp.telegram.bot.protocol.TelegramBotApi.getUpdates]
 * method to 60 seconds. Long polling allows the bot to receive updates in real-time while reducing server load
 * by keeping the connection open until new messages are available or the timeout is reached.
 *
 * **Usage:**
 * ```kotlin
 * val httpClient = HttpClient {
 *     install(TelegramLongPollingPlugin)
 * }
 * ```
 *
 * **Important notes:**
 * - This plugin only affects the `getUpdates` method; all other API requests use the default timeout settings
 * - The 60-second timeout is set for both [requestTimeoutMillis][io.ktor.client.plugins.HttpTimeoutConfig.requestTimeoutMillis]
 *   and [socketTimeoutMillis][io.ktor.client.plugins.HttpTimeoutConfig.socketTimeoutMillis]
 * - Without this plugin, the default timeout may cause the connection to close before Telegram sends updates,
 *   resulting in empty responses and unnecessary polling cycles
 *
 * See the [Telegram Bot API documentation](https://core.telegram.org/bots/api#getupdates)
 * for more information about long polling and the `getUpdates` method.
 */
val TelegramLongPollingPlugin = createClientPlugin("TelegramLongPollingPlugin") {
    on(Send) { request ->
        val methodName = request.url.pathSegments.lastOrNull()
        if (methodName == "getUpdates") {
            request.timeout {
                requestTimeoutMillis = 60_000
                socketTimeoutMillis = 60_000
            }
        }
        proceed(request)
    }
}
