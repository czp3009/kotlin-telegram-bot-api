package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.EventRoute
import com.hiczp.telegram.bot.protocol.event.PreCheckoutQueryEvent
import com.hiczp.telegram.bot.protocol.event.PurchasedPaidMediaEvent
import com.hiczp.telegram.bot.protocol.event.ShippingQueryEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlinx.coroutines.CoroutineScope
import kotlin.jvm.JvmName

@JvmName("shippingQueryTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.shippingQuery(
    build: EventRoute<ShippingQueryEvent>.() -> Unit
): EventRoute<ShippingQueryEvent> = on<ShippingQueryEvent> { build() }

@JvmName("whenShippingQueryTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenShippingQuery(
    handler: suspend CoroutineScope.(TelegramBotEventContext<ShippingQueryEvent>) -> Unit
) = shippingQuery { handle(handler) }

@JvmName("shippingQueryFromUserTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.shippingQueryFromUser(
    userId: Long,
    build: EventRoute<ShippingQueryEvent>.() -> Unit
): EventRoute<ShippingQueryEvent> = on<ShippingQueryEvent> {
    select({ if (it.event.shippingQuery.from.id == userId) it else null }, build)
}

@JvmName("whenShippingQueryFromUserTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenShippingQueryFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<ShippingQueryEvent>) -> Unit
) = shippingQueryFromUser(userId) { handle(handler) }

@JvmName("shippingQueryWithPayloadTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.shippingQueryWithPayload(
    invoicePayload: String,
    build: EventRoute<ShippingQueryEvent>.() -> Unit
): EventRoute<ShippingQueryEvent> = on<ShippingQueryEvent> {
    select({ if (it.event.shippingQuery.invoicePayload == invoicePayload) it else null }, build)
}

@JvmName("whenShippingQueryWithPayloadTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenShippingQueryWithPayload(
    invoicePayload: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<ShippingQueryEvent>) -> Unit
) = shippingQueryWithPayload(invoicePayload) { handle(handler) }

@JvmName("preCheckoutQueryTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.preCheckoutQuery(
    build: EventRoute<PreCheckoutQueryEvent>.() -> Unit
): EventRoute<PreCheckoutQueryEvent> = on<PreCheckoutQueryEvent> { build() }

@JvmName("whenPreCheckoutQueryTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenPreCheckoutQuery(
    handler: suspend CoroutineScope.(TelegramBotEventContext<PreCheckoutQueryEvent>) -> Unit
) = preCheckoutQuery { handle(handler) }

@JvmName("preCheckoutQueryFromUserTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.preCheckoutQueryFromUser(
    userId: Long,
    build: EventRoute<PreCheckoutQueryEvent>.() -> Unit
): EventRoute<PreCheckoutQueryEvent> = on<PreCheckoutQueryEvent> {
    select({ if (it.event.preCheckoutQuery.from.id == userId) it else null }, build)
}

@JvmName("whenPreCheckoutQueryFromUserTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenPreCheckoutQueryFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<PreCheckoutQueryEvent>) -> Unit
) = preCheckoutQueryFromUser(userId) { handle(handler) }

@JvmName("preCheckoutQueryWithPayloadTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.preCheckoutQueryWithPayload(
    invoicePayload: String,
    build: EventRoute<PreCheckoutQueryEvent>.() -> Unit
): EventRoute<PreCheckoutQueryEvent> = on<PreCheckoutQueryEvent> {
    select({ if (it.event.preCheckoutQuery.invoicePayload == invoicePayload) it else null }, build)
}

@JvmName("whenPreCheckoutQueryWithPayloadTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenPreCheckoutQueryWithPayload(
    invoicePayload: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<PreCheckoutQueryEvent>) -> Unit
) = preCheckoutQueryWithPayload(invoicePayload) { handle(handler) }

@JvmName("preCheckoutQueryWithCurrencyTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.preCheckoutQueryWithCurrency(
    currency: String,
    build: EventRoute<PreCheckoutQueryEvent>.() -> Unit
): EventRoute<PreCheckoutQueryEvent> = on<PreCheckoutQueryEvent> {
    select({ if (it.event.preCheckoutQuery.currency == currency) it else null }, build)
}

@JvmName("whenPreCheckoutQueryWithCurrencyTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenPreCheckoutQueryWithCurrency(
    currency: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<PreCheckoutQueryEvent>) -> Unit
) = preCheckoutQueryWithCurrency(currency) { handle(handler) }

@JvmName("purchasedPaidMediaTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.purchasedPaidMedia(
    build: EventRoute<PurchasedPaidMediaEvent>.() -> Unit
): EventRoute<PurchasedPaidMediaEvent> = on<PurchasedPaidMediaEvent> { build() }

@JvmName("whenPurchasedPaidMediaTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenPurchasedPaidMedia(
    handler: suspend CoroutineScope.(TelegramBotEventContext<PurchasedPaidMediaEvent>) -> Unit
) = purchasedPaidMedia { handle(handler) }

@JvmName("purchasedPaidMediaFromUserTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.purchasedPaidMediaFromUser(
    userId: Long,
    build: EventRoute<PurchasedPaidMediaEvent>.() -> Unit
): EventRoute<PurchasedPaidMediaEvent> = on<PurchasedPaidMediaEvent> {
    select({ if (it.event.purchasedPaidMedia.from.id == userId) it else null }, build)
}

@JvmName("whenPurchasedPaidMediaFromUserTelegramBotEvent")
fun EventRoute<TelegramBotEvent>.whenPurchasedPaidMediaFromUser(
    userId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<PurchasedPaidMediaEvent>) -> Unit
) = purchasedPaidMediaFromUser(userId) { handle(handler) }
