package com.hiczp.telegram.bot.api.plugin

import io.ktor.client.plugins.*
import io.ktor.client.plugins.api.*

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
