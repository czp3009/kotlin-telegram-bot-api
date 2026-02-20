package com.hiczp.telegram.bot.application.updatesource

import com.hiczp.telegram.bot.client.TelegramBotClient
import com.hiczp.telegram.bot.protocol.model.Update
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

/**
 * Long polling implementation of [TelegramUpdateSource].
 *
 * Key features:
 * - Immediate interruption: Checks [isShuttingDown] before processing each update
 * - Precise ACK: Sends a final request with timeout=0 on shutdown to confirm the last offset
 * - Idle reset: Automatically resets offset to 0 if no updates received for 6 days
 *
 * @param client The Telegram bot client for API calls.
 */
class LongPollingTelegramUpdateSource(
    private val client: TelegramBotClient,
) : TelegramUpdateSource {
    private var isShuttingDown: Boolean = false
    private var offset: Long = 0L
    private var latestUpdateTime: Instant = Clock.System.now()

    override suspend fun start(onUpdate: suspend (Update) -> Unit) {
        isShuttingDown = false
        latestUpdateTime = Clock.System.now()
        while (!isShuttingDown) {
            val now = Clock.System.now()
            if (now - latestUpdateTime > IDLE_RESET_THRESHOLD) {
                offset = 0L
            }
            val updates = client.getUpdates(
                offset = offset,
                timeout = TIMEOUT_SECONDS,
            ).getOrThrow()
            if (updates.isNotEmpty()) {
                latestUpdateTime = Clock.System.now()
            }
            for (update in updates) {
                if (isShuttingDown) break
                onUpdate(update)
                offset = update.updateId + 1L
            }
        }
        if (offset > 0) {
            runCatching {
                client.getUpdates(offset = offset, timeout = 0)
            }
        }
    }

    override fun stop() {
        isShuttingDown = true
    }

    companion object {
        private const val TIMEOUT_SECONDS: Long = 50
        private val IDLE_RESET_THRESHOLD = 6.days
    }
}
