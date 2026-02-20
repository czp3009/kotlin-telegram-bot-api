package com.hiczp.telegram.bot.application.updatesource

import com.hiczp.telegram.bot.protocol.model.Update

/**
 * Interface for sources that fetch Telegram updates.
 *
 * Implementations are responsible for:
 * - Fetching updates from Telegram servers
 * - Managing polling rhythm
 * - Responding to graceful shutdown signals
 */
interface TelegramUpdateSource {
    /**
     * Start fetching updates and deliver them to the provided callback.
     *
     * This is a suspending function that typically runs indefinitely until
     * [stop] is called. Each update should be delivered to the callback
     * and fully processed before the next update is fetched.
     *
     * @param onUpdate Callback to invoke for each received update.
     */
    suspend fun start(onUpdate: suspend (Update) -> Unit)

    /**
     * Signal the source to stop fetching updates.
     *
     * This should cause [start] to complete gracefully, ensuring any
     * in-progress update has finished processing and the last offset
     * is acknowledged.
     */
    fun stop()
}
