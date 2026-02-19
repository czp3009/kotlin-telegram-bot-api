package com.hiczp.telegram.bot.protocol.test

import io.ktor.client.engine.*

expect fun getBotToken(): String?

expect fun getTestChatId(): String?

expect fun createKtorEngine(): HttpClientEngine

expect fun sleepMillis(millis: Long)
