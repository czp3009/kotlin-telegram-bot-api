package com.hiczp.telegram.bot.application.interceptor.builtin.logging

import com.hiczp.telegram.bot.application.interceptor.TelegramEventInterceptor
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import kotlin.time.Duration
import kotlin.time.TimeSource

private val logger = KotlinLogging.logger("LoggingInterceptor")

/**
 * Creates an interceptor that logs all received Telegram events.
 *
 * This interceptor logs each event before and after processing, including:
 * - The event type and update ID
 * - Processing duration
 *
 * Example usage:
 * ```kotlin
 * val app = TelegramBotApplication.longPolling(
 *     botToken = "YOUR_TOKEN",
 *     interceptors = listOf(loggingInterceptor()),
 *     eventDispatcher = eventDispatcher
 * )
 * ```
 *
 * @param level The log level to use. Defaults to [Level.INFO].
 * @param formatter A function that formats the event for logging. Defaults to a simple format showing updateId and event type.
 * @return A [TelegramEventInterceptor] that logs all events.
 */
fun loggingInterceptor(
    level: Level = Level.INFO,
    formatter: (TelegramBotEvent) -> String = { event -> "$event" },
): TelegramEventInterceptor = Interceptor@{ context ->
    val formattedEvent = formatter(context.event)
    val startTime = TimeSource.Monotonic.markNow()

    logger.at(level) { message = "Received event: $formattedEvent" }

    try {
        process(context)
        val duration = startTime.elapsedNow()
        logger.at(level) { message = "Processed event: $formattedEvent (took ${formatDuration(duration)})" }
    } catch (e: Exception) {
        val duration = startTime.elapsedNow()
        logger.error(e) { "Failed to process event: $formattedEvent (took ${formatDuration(duration)})" }
        throw e
    }
}

private fun formatDuration(duration: Duration): String {
    return if (duration.inWholeMilliseconds >= 1) {
        "${duration.inWholeMilliseconds}ms"
    } else {
        "${duration.inWholeMicroseconds}µs"
    }
}
