@file:OptIn(ExperimentalForeignApi::class)

package com.hiczp.telegram.bot.api

import io.ktor.client.engine.*
import io.ktor.client.engine.curl.*
import io.ktor.http.*
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

actual fun getBotToken(): String? {
    return getenv(EnvVars.BOT_TOKEN)?.toKString()
}

actual fun getTestChatId(): String? {
    return getenv(EnvVars.TEST_CHAT_ID)?.toKString()
}

actual fun createKtorEngine(): HttpClientEngine {
    return Curl.create {
        val httpProxy = getenv(EnvVars.HTTPS_PROXY)?.toKString() ?: getenv(EnvVars.HTTP_PROXY)?.toKString()
        if (httpProxy != null) {
            proxy = ProxyBuilder.http(Url(httpProxy))
        }
        sslVerify = false
    }
}
