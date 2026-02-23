package com.hiczp.telegram.bot.application.test

import io.ktor.client.engine.*
import io.ktor.client.engine.curl.*

actual fun createKtorEngine(): HttpClientEngine {
    return Curl.create()
}
