package com.hiczp.telegram.bot.application.context

import com.hiczp.telegram.bot.client.TelegramBotClient
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import io.ktor.util.*
import kotlinx.coroutines.CoroutineScope

/**
 * Request-scoped context for processing Telegram bot events.
 *
 * This interface provides access to shared resources and event-specific data
 * during event processing within the application framework.
 *
 * A new context is created for each event execution in the pipeline.
 *
 * @property client The Telegram bot client for making API calls.
 * @property event The Telegram event being processed.
 * @property applicationScope The application's coroutine scope for launching concurrent tasks.
 * @property attributes Type-safe attribute storage for sharing data between interceptors and handlers.
 */
interface TelegramBotEventContext {
    val client: TelegramBotClient
    val event: TelegramBotEvent
    val applicationScope: CoroutineScope
    val attributes: Attributes
}

/**
 * Default implementation of [TelegramBotEventContext].
 */
class DefaultTelegramBotEventContext(
    override val client: TelegramBotClient,
    override val event: TelegramBotEvent,
    override val applicationScope: CoroutineScope,
    override val attributes: Attributes = Attributes(concurrent = true),
) : TelegramBotEventContext
