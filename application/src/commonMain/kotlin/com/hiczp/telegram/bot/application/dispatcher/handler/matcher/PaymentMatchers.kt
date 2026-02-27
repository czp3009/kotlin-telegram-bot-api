package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.EventRoute
import com.hiczp.telegram.bot.protocol.event.PreCheckoutQueryEvent
import com.hiczp.telegram.bot.protocol.event.PurchasedPaidMediaEvent
import com.hiczp.telegram.bot.protocol.event.ShippingQueryEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlinx.coroutines.CoroutineScope

/**
 * Registers a handler for shipping queries.
 *
 * Triggered when a user chooses a shipping option during checkout.
 * Only for invoices with flexible price.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.shippingQuery(
    handler: suspend CoroutineScope.(TelegramBotEventContext<ShippingQueryEvent>) -> Unit
) = on<ShippingQueryEvent> { handle(handler) }

/**
 * Registers a handler for shipping queries from a specific user.
 *
 * @param userId The Telegram user ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.shippingQueryFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<ShippingQueryEvent>) -> Unit
) = on<ShippingQueryEvent> {
    select({ if (it.event.shippingQuery.from.id == userId) it else null }) {
        handle(handler)
    }
}

/**
 * Registers a handler for shipping queries with a specific invoice payload.
 *
 * @param invoicePayload The invoice payload to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.shippingQueryWithPayload(
    invoicePayload: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<ShippingQueryEvent>) -> Unit
) = on<ShippingQueryEvent> {
    select({ if (it.event.shippingQuery.invoicePayload == invoicePayload) it else null }) {
        handle(handler)
    }
}


/**
 * Registers a handler for pre-checkout queries.
 *
 * Triggered when a user confirms payment, before the transaction is completed.
 * Contains full information about checkout. The bot must respond within 10 seconds.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.preCheckoutQuery(
    handler: suspend CoroutineScope.(TelegramBotEventContext<PreCheckoutQueryEvent>) -> Unit
) = on<PreCheckoutQueryEvent> { handle(handler) }

/**
 * Registers a handler for pre-checkout queries from a specific user.
 *
 * @param userId The Telegram user ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.preCheckoutQueryFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<PreCheckoutQueryEvent>) -> Unit
) = on<PreCheckoutQueryEvent> {
    select({ if (it.event.preCheckoutQuery.from.id == userId) it else null }) {
        handle(handler)
    }
}

/**
 * Registers a handler for pre-checkout queries with a specific invoice payload.
 *
 * @param invoicePayload The invoice payload to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.preCheckoutQueryWithPayload(
    invoicePayload: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<PreCheckoutQueryEvent>) -> Unit
) = on<PreCheckoutQueryEvent> {
    select({ if (it.event.preCheckoutQuery.invoicePayload == invoicePayload) it else null }) {
        handle(handler)
    }
}

/**
 * Registers a handler for pre-checkout queries with a specific currency.
 *
 * @param currency The currency code to match (e.g., "USD").
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.preCheckoutQueryWithCurrency(
    currency: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<PreCheckoutQueryEvent>) -> Unit
) = on<PreCheckoutQueryEvent> {
    select({ if (it.event.preCheckoutQuery.currency == currency) it else null }) {
        handle(handler)
    }
}


/**
 * Registers a handler for purchased paid media events.
 *
 * Triggered when a user purchases paid media with a non-empty payload
 * sent by the bot in a non-channel chat.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.purchasedPaidMedia(
    handler: suspend CoroutineScope.(TelegramBotEventContext<PurchasedPaidMediaEvent>) -> Unit
) = on<PurchasedPaidMediaEvent> { handle(handler) }

/**
 * Registers a handler for purchased paid media events from a specific user.
 *
 * @param userId The Telegram user ID to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun EventRoute<TelegramBotEvent>.purchasedPaidMediaFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<PurchasedPaidMediaEvent>) -> Unit
) = on<PurchasedPaidMediaEvent> {
    select({ if (it.event.purchasedPaidMedia.from.id == userId) it else null }) {
        handle(handler)
    }
}
