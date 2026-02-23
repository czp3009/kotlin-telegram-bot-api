package com.hiczp.telegram.bot.application.test

import io.ktor.client.engine.*
import io.ktor.client.engine.darwin.*

actual fun createKtorEngine(): HttpClientEngine {
    return Darwin.create()
}
