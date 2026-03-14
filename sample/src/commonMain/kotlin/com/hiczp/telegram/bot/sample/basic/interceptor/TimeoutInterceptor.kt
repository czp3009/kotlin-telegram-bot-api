/**
 * Timeout Interceptor
 *
 * An interceptor that enforces a maximum processing time for events.
 * If event processing exceeds the timeout, a [TimeoutCancellationException] is thrown.
 *
 * ## Usage
 *
 * ```kotlin
 * val interceptors = listOf(
 *     timeoutInterceptor(
 *         timeout = 30.seconds,
 *         messageFilter = { event -> event is MessageEvent },
 *         onTimeout = { context, _ ->
 *             val chatId = context.event.extractChatId()
 *             if (chatId != null) {
 *                 context.client.sendMessage(chatId.toString(), "Operation timed out!")
 *             }
 *         }
 *     )
 * )
 * ```
 *
 * @see withTimeout
 */
package com.hiczp.telegram.bot.sample.basic.interceptor

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.interceptor.TelegramEventInterceptor
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration

private val logger = KotlinLogging.logger("TimeoutInterceptor")

/**
 * Creates an interceptor that enforces a timeout on event processing.
 *
 * If event processing exceeds the specified [timeout], a [TimeoutCancellationException]
 * is thrown. You can provide an [onTimeout] callback to handle the timeout
 * (e.g., send a notification to the user) before the exception propagates.
 *
 * Note: This interceptor should typically be placed before [exceptionCatchingInterceptor]
 * if you want to catch timeout exceptions and send user-friendly error messages.
 *
 * @param timeout Maximum duration allowed for processing each event.
 * @param messageFilter A filter function that determines which events should be checked for timeout.
 *                      Return `true` to apply timeout to this event, `false` to skip timeout check.
 * @param onTimeout Optional callback invoked when timeout occurs. Receives the context
 *                  and the exception. The callback runs before the exception is re-thrown.
 * @return A [TelegramEventInterceptor] that enforces timeout.
 */
fun timeoutInterceptor(
    timeout: Duration,
    messageFilter: (TelegramBotEvent) -> Boolean,
    onTimeout: (suspend (TelegramBotEventContext<TelegramBotEvent>, TimeoutCancellationException) -> Unit)? = null,
): TelegramEventInterceptor = Interceptor@{ context ->
    // Check if this event should be filtered
    if (!messageFilter(context.event)) {
        process(context)
        return@Interceptor
    }

    try {
        withTimeout(timeout) {
            process(context)
        }
    } catch (e: TimeoutCancellationException) {
        logger.warn { "Event processing timed out after $timeout: updateId=${context.event.updateId}" }
        onTimeout?.invoke(context, e)
        throw e
    }
}
