package com.hiczp.telegram.bot.application.compose.message

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.interceptor.TelegramEventInterceptor
import com.hiczp.telegram.bot.protocol.event.CallbackQueryEvent
import com.hiczp.telegram.bot.protocol.model.answerCallbackQuery
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger("ComposeMessageInterceptor")

/**
 * Creates an interceptor that handles callback query events for Compose message sessions.
 *
 * This interceptor must be installed in the application pipeline for button onClick
 * callbacks to work. It routes callback query events to the appropriate session's
 * handler and answers the callback query.
 *
 * Example usage:
 * ```kotlin
 * val app = TelegramBotApplication.longPolling(
 *     botToken = "YOUR_TOKEN",
 *     interceptors = listOf(composeMessageInterceptor()),
 *     eventDispatcher = eventDispatcher
 * )
 * ```
 *
 * @param onSessionNotFound Optional callback invoked when a callback query is received
 *        for a message that has no active Compose session (e.g., message expired).
 *        Defaults to answering with an alert saying the message has expired.
 * @return A [TelegramEventInterceptor] that handles Compose message callbacks.
 */
fun composeMessageInterceptor(
    onSessionNotFound: (suspend TelegramBotEventContext<CallbackQueryEvent>.() -> Unit)? = null,
): TelegramEventInterceptor = Interceptor@{ context ->
    val event = context.event
    if (event !is CallbackQueryEvent) {
        process(context)
        return@Interceptor
    }

    val callbackQuery = event.callbackQuery
    val message = callbackQuery.message
    val data = callbackQuery.data

    if (message == null || data == null) {
        process(context)
        return@Interceptor
    }

    val chatId = message.chat.id
    val messageId = message.messageId

    val session = ComposeMessageRegistry.get(chatId, messageId)
    if (session != null) {
        // Route callback to session
        session.handleCallback(data)

        // Answer the callback query to remove the loading state
        context.client.answerCallbackQuery(callbackQuery.id).getOrThrow()
        // Don't process further - the session handled it
    } else {
        // Session not found
        if (onSessionNotFound != null) {
            @Suppress("UNCHECKED_CAST")
            (onSessionNotFound as suspend TelegramBotEventContext<*>.() -> Unit)(context)
        } else {
            // Default: answer with expired message
            context.client.answerCallbackQuery(
                callbackQueryId = callbackQuery.id,
                text = "This message has expired",
                showAlert = true
            ).getOrThrow()
            logger.debug { "Callback query for expired session: $chatId/$messageId" }
        }
    }
}
