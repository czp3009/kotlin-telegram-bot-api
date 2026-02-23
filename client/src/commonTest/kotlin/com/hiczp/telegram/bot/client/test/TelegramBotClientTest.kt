package com.hiczp.telegram.bot.client.test

import com.hiczp.telegram.bot.client.TelegramBotClient
import com.hiczp.telegram.bot.protocol.exception.TelegramErrorResponseException
import io.ktor.client.*
import io.ktor.client.plugins.logging.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class TelegramBotClientTest {
    private val engine by lazy { createKtorEngine() }
    private val botToken by lazy { getBotToken() ?: error("Bot token not found") }
    private val additionalConfiguration: HttpClientConfig<*>.() -> Unit = {
        install(Logging) {
            level = LogLevel.ALL
        }
    }
    private val defaultClient by lazy {
        TelegramBotClient(
            httpClientEngine = engine,
            botToken = botToken,
            additionalConfiguration = additionalConfiguration,
        )
    }
    private val notThrowClient by lazy {
        TelegramBotClient(
            httpClientEngine = engine,
            botToken = botToken,
            throwOnErrorResponse = false,
            additionalConfiguration = additionalConfiguration,
        )
    }

    @Test
    fun getMe() = runTest {
        defaultClient.getMe()
    }

    @Test
    fun throwOnErrorResponse() = runTest {
        val result = runCatching { defaultClient.getChat("xxx") }
        assertFailsWith<TelegramErrorResponseException> {
            result.getOrThrow()
        }
    }

    @Test
    fun notThrowOnErrorResponse() = runTest {
        val result = notThrowClient.getChat("xxx")
        assertFailsWith<TelegramErrorResponseException> {
            result.getOrThrow()
        }
    }
}
