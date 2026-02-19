package com.hiczp.telegram.bot.client.test

import io.ktor.client.engine.*

expect fun getBotToken(): String?

expect fun createKtorEngine(): HttpClientEngine
