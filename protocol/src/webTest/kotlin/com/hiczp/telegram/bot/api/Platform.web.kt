package com.hiczp.telegram.bot.api

import io.ktor.client.engine.HttpClientEngineFactory
import kotlin.js.ExperimentalWasmJsInterop

@OptIn(ExperimentalWasmJsInterop::class)
actual fun getBotToken(): String? {
    TODO()
}

actual fun ktorEngine(): HttpClientEngineFactory<*> {
    TODO("Not yet implemented")
}
