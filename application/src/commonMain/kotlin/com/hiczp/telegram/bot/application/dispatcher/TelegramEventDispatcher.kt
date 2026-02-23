package com.hiczp.telegram.bot.application.dispatcher

import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent

/**
 * Interface for the innermost layer of the event processing pipeline.
 *
 * The dispatcher is responsible for routing events to specific business logic handlers.
 * Implementations may use different strategies for event routing:
 * - Simple delegation to a single handler function
 * - Pattern matching on event types
 * - Chain of responsibility with multiple handlers
 */
interface TelegramEventDispatcher {
    /**
     * Dispatch a Telegram event to the appropriate handler.
     *
     * @param event The event to dispatch.
     */
    suspend fun dispatch(event: TelegramBotEvent)
}
