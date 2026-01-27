package com.hiczp.telegram.bot.api

import io.ktor.client.engine.*
import io.ktor.client.engine.js.*
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

@OptIn(ExperimentalWasmJsInterop::class)
actual fun getBotToken(): String? = js("process.env.${BOT_TOKEN_ENV_VAR}")

actual fun ktorEngine(): HttpClientEngineFactory<*> {
    return Js
}
