package com.hiczp.telegram.bot.api

import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.http.*

actual fun getBotToken(): String? {
    return System.getenv(EnvVars.BOT_TOKEN)
}

actual fun getTestChatId(): String? {
    return System.getenv(EnvVars.TEST_CHAT_ID)
}

actual fun createKtorEngine(): HttpClientEngine {
    return CIO.create {
        val httpProxy = System.getenv(EnvVars.HTTPS_PROXY) ?: System.getenv(EnvVars.HTTP_PROXY)
        if (httpProxy != null) {
            proxy = ProxyBuilder.http(Url(httpProxy))
        }
    }
}
