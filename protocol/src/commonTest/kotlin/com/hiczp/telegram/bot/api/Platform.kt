package com.hiczp.telegram.bot.api

import io.ktor.client.engine.*

expect fun getBotToken(): String?

expect fun createKtorEngine(): HttpClientEngine
