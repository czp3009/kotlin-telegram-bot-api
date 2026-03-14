/**
 * Exception Catching Interceptor
 *
 * An interceptor that catches all exceptions during event processing
 * and invokes a custom error handler.
 *
 * ## Usage
 *
 * ```kotlin
 * val interceptors = listOf(
 *     exceptionCatchingInterceptor(
 *         messageFilter = { event -> event is MessageEvent }
 *     ) { context, error ->
 *         val chatId = context.event.extractChatId()
 *         if (chatId != null) {
 *             context.client.sendMessage(
 *                 chatId = chatId.toString(),
 *                 text = "An error occurred: ${error.message}"
 *             )
 *         }
 *     }
 * )
 * ```
 *
 * @see Throwable
 */
package com.hiczp.telegram.bot.sample.basic.interceptor

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.interceptor.TelegramEventInterceptor
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CancellationException

private val logger = KotlinLogging.logger("ExceptionCatchingInterceptor")

/**
 * Creates an interceptor that catches all exceptions during event processing
 * and invokes a custom error handler.
 *
 * This interceptor catches all [Throwable] exceptions (except [CancellationException]
 * which is used for coroutine cancellation), logs them, and invokes the [onError] callback.
 *
 * The interceptor should typically be placed as one of the outermost interceptors
 * to catch exceptions from all inner layers.
 *
 * @param messageFilter A filter function that determines which events should be wrapped with exception catching.
 *                      Return `true` to catch exceptions for this event, `false` to skip exception catching.
 * @param onError Callback invoked when an exception is caught.
 *                Receives the context and the exception. Use this to send error messages
 *                to users, perform cleanup, or take other custom actions.
 * @return A [TelegramEventInterceptor] that catches and handles exceptions.
 */
fun exceptionCatchingInterceptor(
    messageFilter: (TelegramBotEvent) -> Boolean,
    onError: suspend (TelegramBotEventContext<TelegramBotEvent>, Throwable) -> Unit,
): TelegramEventInterceptor = Interceptor@{ context ->
    // Check if this event should be filtered
    if (!messageFilter(context.event)) {
        process(context)
        return@Interceptor
    }

    try {
        process(context)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        logger.error(e) { "Exception during event processing: updateId=${context.event.updateId}" }
        onError(context, e)
    }
}
