package com.hiczp.telegram.bot.api

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory

const val BOT_TOKEN_ENV_VAR = "BOT_TOKEN"

expect fun getBotToken(): String?

expect fun ktorEngine() : HttpClientEngineFactory<*>
