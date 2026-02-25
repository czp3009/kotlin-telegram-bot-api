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
interface TelegramBotEventContext<out T : TelegramBotEvent> {
    val client: TelegramBotClient
    val event: T
    val applicationScope: CoroutineScope
    val attributes: Attributes
}

/**
 * Default implementation of [TelegramBotEventContext].
 */
class DefaultTelegramBotEventContext<out T : TelegramBotEvent>(
    override val client: TelegramBotClient,
    override val event: T,
    override val applicationScope: CoroutineScope,
    override val attributes: Attributes = Attributes(concurrent = true),
) : TelegramBotEventContext<T>

/**
 * Attempts to cast this context to a more specific event type.
 *
 * This function checks if the underlying event is of the specified type [T].
 * If the type matches, it returns the same context instance cast to the typed variant.
 * If the type doesn't match, it returns null.
 *
 * Note: The returned context is the same instance as the receiver, just with a more specific type.
 *
 * Example usage:
 * ```kotlin
 * val messageContext = context.castOrNull<MessageEvent>()
 * if (messageContext != null) {
 *     // Handle message event with type-safe access
 *     val text = messageContext.event.message.text
 * }
 * ```
 *
 * @param T The specific event type to cast to.
 * @return The same context instance cast to [TelegramBotEventContext] with event type [T],
 *         or null if the event is not of type [T].
 */
inline fun <reified T : TelegramBotEvent> TelegramBotEventContext<*>.castOrNull(): TelegramBotEventContext<T>? {
    if (this.event is T) {
        @Suppress("UNCHECKED_CAST")
        return this as TelegramBotEventContext<T>
    }
    return null
}
