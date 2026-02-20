package com.hiczp.telegram.bot.application.updatesource

import com.hiczp.telegram.bot.protocol.model.Update
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * A mock update source that receives updates from an external channel.
 *
 * This is useful for testing and scenarios where updates come from
 * external sources like webhooks or test fixtures.
 *
 * Example usage:
 * ```kotlin
 * val channel = Channel<Update>()
 * val mockSource = MockUpdateSource(channel)
 *
 * // In your test
 * channel.send(update)
 *
 * // The update will be delivered to the onUpdate callback
 * ```
 *
 * @param channel The channel to receive updates from.
 */
class MockUpdateSource(
    private val channel: ReceiveChannel<Update>,
) : TelegramUpdateSource {
    private var isShuttingDown: Boolean = false

    override suspend fun start(onUpdate: suspend (Update) -> Unit) {
        isShuttingDown = false
        try {
            for (update in channel) {
                if (isShuttingDown) break
                onUpdate(update)
            }
        } catch (e: CancellationException) {
            // Channel was cancelled, exit gracefully
        }
    }

    override fun stop() {
        isShuttingDown = true
    }
}
