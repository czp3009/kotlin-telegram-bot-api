package com.hiczp.telegram.bot.application.test

import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.http.*

actual fun createKtorEngine(): HttpClientEngine {
    return CIO.create {
        val httpProxy = System.getenv(EnvVars.HTTPS_PROXY) ?: System.getenv(EnvVars.HTTP_PROXY)
        if (httpProxy != null) {
            proxy = ProxyBuilder.http(Url(httpProxy))
        }
    }
}
