package com.hiczp.telegram.bot.application.test

import io.ktor.client.engine.*
import io.ktor.client.engine.js.*

actual fun createKtorEngine(): HttpClientEngine {
    return Js.create()
}
