package com.hiczp.telegram.bot.application.test

import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.http.*
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

@OptIn(ExperimentalForeignApi::class)
actual fun createKtorEngine(): HttpClientEngine {
    return CIO.create {
        val httpProxy = getenv(EnvVars.HTTPS_PROXY)?.toKString() ?: getenv(EnvVars.HTTP_PROXY)?.toKString()
        if (httpProxy != null) {
            proxy = ProxyBuilder.http(Url(httpProxy))
        }
    }
}
