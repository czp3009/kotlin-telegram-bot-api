@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.PreCheckoutQueryEvent
import com.hiczp.telegram.bot.protocol.event.PurchasedPaidMediaEvent
import com.hiczp.telegram.bot.protocol.event.ShippingQueryEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlin.jvm.JvmName

// ==================== ShippingQueryEvent Filtered Matchers ====================

// --- onShippingQueryEventFromUser (stackable) ---

@JvmName("onShippingQueryEventFromUserShippingQueryEvent")
fun HandlerRoute<ShippingQueryEvent>.onShippingQueryEventFromUser(
    userId: Long,
    build: HandlerRoute<ShippingQueryEvent>.() -> Unit
) = select({ if (it.event.shippingQuery.from.id == userId) it else null }, build)

@JvmName("onShippingQueryEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onShippingQueryEventFromUser(
    userId: Long,
    build: HandlerRoute<ShippingQueryEvent>.() -> Unit
) = onShippingQueryEvent { onShippingQueryEventFromUser(userId, build) }

// --- whenShippingQueryEventFromUser (terminal) ---

@JvmName("whenShippingQueryEventFromUserShippingQueryEvent")
fun HandlerRoute<ShippingQueryEvent>.whenShippingQueryEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<ShippingQueryEvent>.() -> Unit
) = select({ if (it.event.shippingQuery.from.id == userId) it else null }) { handle(handler) }

@JvmName("whenShippingQueryEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenShippingQueryEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<ShippingQueryEvent>.() -> Unit
) = onShippingQueryEvent { whenShippingQueryEventFromUser(userId, handler) }

// --- onShippingQueryEventWithPayload (stackable) ---

@JvmName("onShippingQueryEventWithPayloadShippingQueryEvent")
fun HandlerRoute<ShippingQueryEvent>.onShippingQueryEventWithPayload(
    invoicePayload: String,
    build: HandlerRoute<ShippingQueryEvent>.() -> Unit
) = select({ if (it.event.shippingQuery.invoicePayload == invoicePayload) it else null }, build)

@JvmName("onShippingQueryEventWithPayloadTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onShippingQueryEventWithPayload(
    invoicePayload: String,
    build: HandlerRoute<ShippingQueryEvent>.() -> Unit
) = onShippingQueryEvent { onShippingQueryEventWithPayload(invoicePayload, build) }

// --- whenShippingQueryEventWithPayload (terminal) ---

@JvmName("whenShippingQueryEventWithPayloadShippingQueryEvent")
fun HandlerRoute<ShippingQueryEvent>.whenShippingQueryEventWithPayload(
    invoicePayload: String,
    handler: suspend HandlerBotCall<ShippingQueryEvent>.() -> Unit
) = select({ if (it.event.shippingQuery.invoicePayload == invoicePayload) it else null }) { handle(handler) }

@JvmName("whenShippingQueryEventWithPayloadTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenShippingQueryEventWithPayload(
    invoicePayload: String,
    handler: suspend HandlerBotCall<ShippingQueryEvent>.() -> Unit
) = onShippingQueryEvent { whenShippingQueryEventWithPayload(invoicePayload, handler) }

// ==================== PreCheckoutQueryEvent Filtered Matchers ====================

// --- onPreCheckoutQueryEventFromUser (stackable) ---

@JvmName("onPreCheckoutQueryEventFromUserPreCheckoutQueryEvent")
fun HandlerRoute<PreCheckoutQueryEvent>.onPreCheckoutQueryEventFromUser(
    userId: Long,
    build: HandlerRoute<PreCheckoutQueryEvent>.() -> Unit
) = select({ if (it.event.preCheckoutQuery.from.id == userId) it else null }, build)

@JvmName("onPreCheckoutQueryEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onPreCheckoutQueryEventFromUser(
    userId: Long,
    build: HandlerRoute<PreCheckoutQueryEvent>.() -> Unit
) = onPreCheckoutQueryEvent { onPreCheckoutQueryEventFromUser(userId, build) }

// --- whenPreCheckoutQueryEventFromUser (terminal) ---

@JvmName("whenPreCheckoutQueryEventFromUserPreCheckoutQueryEvent")
fun HandlerRoute<PreCheckoutQueryEvent>.whenPreCheckoutQueryEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<PreCheckoutQueryEvent>.() -> Unit
) = select({ if (it.event.preCheckoutQuery.from.id == userId) it else null }) { handle(handler) }

@JvmName("whenPreCheckoutQueryEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenPreCheckoutQueryEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<PreCheckoutQueryEvent>.() -> Unit
) = onPreCheckoutQueryEvent { whenPreCheckoutQueryEventFromUser(userId, handler) }

// --- onPreCheckoutQueryEventWithPayload (stackable) ---

@JvmName("onPreCheckoutQueryEventWithPayloadPreCheckoutQueryEvent")
fun HandlerRoute<PreCheckoutQueryEvent>.onPreCheckoutQueryEventWithPayload(
    invoicePayload: String,
    build: HandlerRoute<PreCheckoutQueryEvent>.() -> Unit
) = select({ if (it.event.preCheckoutQuery.invoicePayload == invoicePayload) it else null }, build)

@JvmName("onPreCheckoutQueryEventWithPayloadTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onPreCheckoutQueryEventWithPayload(
    invoicePayload: String,
    build: HandlerRoute<PreCheckoutQueryEvent>.() -> Unit
) = onPreCheckoutQueryEvent { onPreCheckoutQueryEventWithPayload(invoicePayload, build) }

// --- whenPreCheckoutQueryEventWithPayload (terminal) ---

@JvmName("whenPreCheckoutQueryEventWithPayloadPreCheckoutQueryEvent")
fun HandlerRoute<PreCheckoutQueryEvent>.whenPreCheckoutQueryEventWithPayload(
    invoicePayload: String,
    handler: suspend HandlerBotCall<PreCheckoutQueryEvent>.() -> Unit
) = select({ if (it.event.preCheckoutQuery.invoicePayload == invoicePayload) it else null }) { handle(handler) }

@JvmName("whenPreCheckoutQueryEventWithPayloadTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenPreCheckoutQueryEventWithPayload(
    invoicePayload: String,
    handler: suspend HandlerBotCall<PreCheckoutQueryEvent>.() -> Unit
) = onPreCheckoutQueryEvent { whenPreCheckoutQueryEventWithPayload(invoicePayload, handler) }

// --- onPreCheckoutQueryEventWithCurrency (stackable) ---

@JvmName("onPreCheckoutQueryEventWithCurrencyPreCheckoutQueryEvent")
fun HandlerRoute<PreCheckoutQueryEvent>.onPreCheckoutQueryEventWithCurrency(
    currency: String,
    build: HandlerRoute<PreCheckoutQueryEvent>.() -> Unit
) = select({ if (it.event.preCheckoutQuery.currency == currency) it else null }, build)

@JvmName("onPreCheckoutQueryEventWithCurrencyTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onPreCheckoutQueryEventWithCurrency(
    currency: String,
    build: HandlerRoute<PreCheckoutQueryEvent>.() -> Unit
) = onPreCheckoutQueryEvent { onPreCheckoutQueryEventWithCurrency(currency, build) }

// --- whenPreCheckoutQueryEventWithCurrency (terminal) ---

@JvmName("whenPreCheckoutQueryEventWithCurrencyPreCheckoutQueryEvent")
fun HandlerRoute<PreCheckoutQueryEvent>.whenPreCheckoutQueryEventWithCurrency(
    currency: String,
    handler: suspend HandlerBotCall<PreCheckoutQueryEvent>.() -> Unit
) = select({ if (it.event.preCheckoutQuery.currency == currency) it else null }) { handle(handler) }

@JvmName("whenPreCheckoutQueryEventWithCurrencyTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenPreCheckoutQueryEventWithCurrency(
    currency: String,
    handler: suspend HandlerBotCall<PreCheckoutQueryEvent>.() -> Unit
) = onPreCheckoutQueryEvent { whenPreCheckoutQueryEventWithCurrency(currency, handler) }

// ==================== PurchasedPaidMediaEvent Filtered Matchers ====================

// --- onPurchasedPaidMediaEventFromUser (stackable) ---

@JvmName("onPurchasedPaidMediaEventFromUserPurchasedPaidMediaEvent")
fun HandlerRoute<PurchasedPaidMediaEvent>.onPurchasedPaidMediaEventFromUser(
    userId: Long,
    build: HandlerRoute<PurchasedPaidMediaEvent>.() -> Unit
) = select({ if (it.event.purchasedPaidMedia.from.id == userId) it else null }, build)

@JvmName("onPurchasedPaidMediaEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onPurchasedPaidMediaEventFromUser(
    userId: Long,
    build: HandlerRoute<PurchasedPaidMediaEvent>.() -> Unit
) = onPurchasedPaidMediaEvent { onPurchasedPaidMediaEventFromUser(userId, build) }

// --- whenPurchasedPaidMediaEventFromUser (terminal) ---

@JvmName("whenPurchasedPaidMediaEventFromUserPurchasedPaidMediaEvent")
fun HandlerRoute<PurchasedPaidMediaEvent>.whenPurchasedPaidMediaEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<PurchasedPaidMediaEvent>.() -> Unit
) = select({ if (it.event.purchasedPaidMedia.from.id == userId) it else null }) { handle(handler) }

@JvmName("whenPurchasedPaidMediaEventFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenPurchasedPaidMediaEventFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<PurchasedPaidMediaEvent>.() -> Unit
) = onPurchasedPaidMediaEvent { whenPurchasedPaidMediaEventFromUser(userId, handler) }
