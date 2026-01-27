package com.hiczp.telegram.bot.api

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO

actual fun getBotToken(): String? {
    return System.getenv(BOT_TOKEN_ENV_VAR)
}

actual fun  ktorEngine(): HttpClientEngineFactory<*> {
    return CIO
}
