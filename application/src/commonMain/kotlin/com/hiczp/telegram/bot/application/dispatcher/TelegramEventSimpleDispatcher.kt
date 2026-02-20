package com.hiczp.telegram.bot.application.dispatcher

import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.coroutines.cancellation.CancellationException

private val logger = KotlinLogging.logger {}

/**
 * A simple dispatcher implementation that delegates event handling to a lambda function.
 *
 * This dispatcher wraps the provided action with error handling, logging any exceptions
 * that occur during event processing.
 *
 * @param action The suspend function to invoke for each event.
 */
open class TelegramEventSimpleDispatcher(
    private val action: suspend (TelegramBotEvent) -> Unit
) : TelegramEventDispatcher {
    override suspend fun dispatch(event: TelegramBotEvent) {
        try {
            action(event)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            logger.error(e) { "Failed to dispatch event: $event" }
        }
    }
}
