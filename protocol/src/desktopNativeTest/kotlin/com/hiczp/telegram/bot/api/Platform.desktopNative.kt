package com.hiczp.telegram.bot.api

import io.ktor.client.engine.curl.*

actual fun ktorEngine(): io.ktor.client.engine.HttpClientEngineFactory<*> {
    return Curl
}
