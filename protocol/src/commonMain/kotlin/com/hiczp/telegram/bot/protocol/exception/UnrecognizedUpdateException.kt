package com.hiczp.telegram.bot.protocol.exception

import com.hiczp.telegram.bot.protocol.model.Update
import kotlinx.serialization.Serializable

/**
 * Exception thrown when an [Update] cannot be converted to a [TelegramBotEvent][com.hiczp.telegram.bot.protocol.event.TelegramBotEvent].
 *
 * This exception is thrown by [Update.toTelegramBotEvent()][com.hiczp.telegram.bot.protocol.event.toTelegramBotEvent]
 * when the update contains no recognizable event payload. This can happen when:
 * - Telegram introduces new update types that are not yet supported by the library
 * - The update is a special system update with no corresponding event field
 *
 * Note: This exception is serializable to support transmission to other microservices
 * in distributed systems.
 *
 * Example handling:
 * ```kotlin
 * try {
 *     val event = update.toTelegramBotEvent()
 *     // Process event...
 * } catch (e: UnrecognizedUpdateException) {
 *     logger.warn { "Received unrecognized update: ${e.update}" }
 * }
 * ```
 *
 * @property update The original [Update] that could not be recognized.
 * @see com.hiczp.telegram.bot.protocol.event.toTelegramBotEvent
 */
@Serializable
data class UnrecognizedUpdateException(
    val update: Update,
) : IllegalStateException() {
    override val message
        get() = "Unrecognized update: $update"
}
