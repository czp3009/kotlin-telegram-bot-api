package com.hiczp.telegram.bot.application.test

import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*

actual fun createKtorEngine(): HttpClientEngine {
    return CIO.create()
}
