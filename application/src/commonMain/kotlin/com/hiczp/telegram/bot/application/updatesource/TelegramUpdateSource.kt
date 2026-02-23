package com.hiczp.telegram.bot.application.updatesource

import com.hiczp.telegram.bot.protocol.model.Update

/**
 * Source of Telegram updates using the callback-based pattern.
 *
 * This design leverages Kotlin's CancellationException for graceful shutdown:
 * - [start] suspends and feeds updates to the consumer callback
 * - [stop] triggers source shutdown (cuts off network polling/closes port)
 * - [onFinalize] performs final cleanup (e.g., sending Final ACK)
 *
 * The consumer throws [com.hiczp.telegram.bot.application.exception.TelegramBotShuttingDownException] to signal shutdown.
 * In sequential mode, this instantly breaks the processing loop.
 * In concurrent mode (launched with `launch`), the exception is handled silently by the coroutine framework.
 *
 * @see LongPollingTelegramUpdateSource
 */
interface TelegramUpdateSource {
    /**
     * Start fetching updates and pass them to the consumer.
     *
     * The [consume] callback processes each update. If the application is shutting down,
     * [consume] should throw [com.hiczp.telegram.bot.application.exception.TelegramBotShuttingDownException],
     * which will instantly terminate the processing flow in sequential mode,
     * or silently cancel individual tasks in concurrent mode.
     *
     * @param consume Suspended function to process each update.
     */
    suspend fun start(consume: suspend (Update) -> Unit)

    /**
     * Trigger source shutdown.
     *
     * Immediately cuts off the source (stops network polling/closes port).
     * Called by [com.hiczp.telegram.bot.application.TelegramBotApplication.stop] before waiting for handlers.
     */
    suspend fun stop()

    /**
     * Perform final cleanup work.
     *
     * Called after all handlers have completed. Use this for operations like
     * sending Final ACK to confirm processed updates.
     */
    suspend fun onFinalize()
}
