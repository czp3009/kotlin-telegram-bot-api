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

// --- onShippingQueryFromUser (stackable) ---

@JvmName("onShippingQueryFromUserShippingQueryEvent")
fun HandlerRoute<ShippingQueryEvent>.onShippingQueryFromUser(
    userId: Long,
    build: HandlerRoute<ShippingQueryEvent>.() -> Unit
) = select({ if (it.event.shippingQuery.from.id == userId) it else null }, build)

@JvmName("onShippingQueryFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onShippingQueryFromUser(
    userId: Long,
    build: HandlerRoute<ShippingQueryEvent>.() -> Unit
) = onShippingQueryEvent { onShippingQueryFromUser(userId, build) }

// --- whenShippingQueryFromUser (terminal) ---

@JvmName("whenShippingQueryFromUserShippingQueryEvent")
fun HandlerRoute<ShippingQueryEvent>.whenShippingQueryFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<ShippingQueryEvent>.() -> Unit
) = select({ if (it.event.shippingQuery.from.id == userId) it else null }) { handle(handler) }

@JvmName("whenShippingQueryFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenShippingQueryFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<ShippingQueryEvent>.() -> Unit
) = onShippingQueryEvent { whenShippingQueryFromUser(userId, handler) }

// --- onShippingQueryWithPayload (stackable) ---

@JvmName("onShippingQueryWithPayloadShippingQueryEvent")
fun HandlerRoute<ShippingQueryEvent>.onShippingQueryWithPayload(
    invoicePayload: String,
    build: HandlerRoute<ShippingQueryEvent>.() -> Unit
) = select({ if (it.event.shippingQuery.invoicePayload == invoicePayload) it else null }, build)

@JvmName("onShippingQueryWithPayloadTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onShippingQueryWithPayload(
    invoicePayload: String,
    build: HandlerRoute<ShippingQueryEvent>.() -> Unit
) = onShippingQueryEvent { onShippingQueryWithPayload(invoicePayload, build) }

// --- whenShippingQueryWithPayload (terminal) ---

@JvmName("whenShippingQueryWithPayloadShippingQueryEvent")
fun HandlerRoute<ShippingQueryEvent>.whenShippingQueryWithPayload(
    invoicePayload: String,
    handler: suspend HandlerBotCall<ShippingQueryEvent>.() -> Unit
) = select({ if (it.event.shippingQuery.invoicePayload == invoicePayload) it else null }) { handle(handler) }

@JvmName("whenShippingQueryWithPayloadTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenShippingQueryWithPayload(
    invoicePayload: String,
    handler: suspend HandlerBotCall<ShippingQueryEvent>.() -> Unit
) = onShippingQueryEvent { whenShippingQueryWithPayload(invoicePayload, handler) }

// ==================== PreCheckoutQueryEvent Filtered Matchers ====================

// --- onPreCheckoutQueryFromUser (stackable) ---

@JvmName("onPreCheckoutQueryFromUserPreCheckoutQueryEvent")
fun HandlerRoute<PreCheckoutQueryEvent>.onPreCheckoutQueryFromUser(
    userId: Long,
    build: HandlerRoute<PreCheckoutQueryEvent>.() -> Unit
) = select({ if (it.event.preCheckoutQuery.from.id == userId) it else null }, build)

@JvmName("onPreCheckoutQueryFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onPreCheckoutQueryFromUser(
    userId: Long,
    build: HandlerRoute<PreCheckoutQueryEvent>.() -> Unit
) = onPreCheckoutQueryEvent { onPreCheckoutQueryFromUser(userId, build) }

// --- whenPreCheckoutQueryFromUser (terminal) ---

@JvmName("whenPreCheckoutQueryFromUserPreCheckoutQueryEvent")
fun HandlerRoute<PreCheckoutQueryEvent>.whenPreCheckoutQueryFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<PreCheckoutQueryEvent>.() -> Unit
) = select({ if (it.event.preCheckoutQuery.from.id == userId) it else null }) { handle(handler) }

@JvmName("whenPreCheckoutQueryFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenPreCheckoutQueryFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<PreCheckoutQueryEvent>.() -> Unit
) = onPreCheckoutQueryEvent { whenPreCheckoutQueryFromUser(userId, handler) }

// --- onPreCheckoutQueryWithPayload (stackable) ---

@JvmName("onPreCheckoutQueryWithPayloadPreCheckoutQueryEvent")
fun HandlerRoute<PreCheckoutQueryEvent>.onPreCheckoutQueryWithPayload(
    invoicePayload: String,
    build: HandlerRoute<PreCheckoutQueryEvent>.() -> Unit
) = select({ if (it.event.preCheckoutQuery.invoicePayload == invoicePayload) it else null }, build)

@JvmName("onPreCheckoutQueryWithPayloadTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onPreCheckoutQueryWithPayload(
    invoicePayload: String,
    build: HandlerRoute<PreCheckoutQueryEvent>.() -> Unit
) = onPreCheckoutQueryEvent { onPreCheckoutQueryWithPayload(invoicePayload, build) }

// --- whenPreCheckoutQueryWithPayload (terminal) ---

@JvmName("whenPreCheckoutQueryWithPayloadPreCheckoutQueryEvent")
fun HandlerRoute<PreCheckoutQueryEvent>.whenPreCheckoutQueryWithPayload(
    invoicePayload: String,
    handler: suspend HandlerBotCall<PreCheckoutQueryEvent>.() -> Unit
) = select({ if (it.event.preCheckoutQuery.invoicePayload == invoicePayload) it else null }) { handle(handler) }

@JvmName("whenPreCheckoutQueryWithPayloadTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenPreCheckoutQueryWithPayload(
    invoicePayload: String,
    handler: suspend HandlerBotCall<PreCheckoutQueryEvent>.() -> Unit
) = onPreCheckoutQueryEvent { whenPreCheckoutQueryWithPayload(invoicePayload, handler) }

// --- onPreCheckoutQueryWithCurrency (stackable) ---

@JvmName("onPreCheckoutQueryWithCurrencyPreCheckoutQueryEvent")
fun HandlerRoute<PreCheckoutQueryEvent>.onPreCheckoutQueryWithCurrency(
    currency: String,
    build: HandlerRoute<PreCheckoutQueryEvent>.() -> Unit
) = select({ if (it.event.preCheckoutQuery.currency == currency) it else null }, build)

@JvmName("onPreCheckoutQueryWithCurrencyTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onPreCheckoutQueryWithCurrency(
    currency: String,
    build: HandlerRoute<PreCheckoutQueryEvent>.() -> Unit
) = onPreCheckoutQueryEvent { onPreCheckoutQueryWithCurrency(currency, build) }

// --- whenPreCheckoutQueryWithCurrency (terminal) ---

@JvmName("whenPreCheckoutQueryWithCurrencyPreCheckoutQueryEvent")
fun HandlerRoute<PreCheckoutQueryEvent>.whenPreCheckoutQueryWithCurrency(
    currency: String,
    handler: suspend HandlerBotCall<PreCheckoutQueryEvent>.() -> Unit
) = select({ if (it.event.preCheckoutQuery.currency == currency) it else null }) { handle(handler) }

@JvmName("whenPreCheckoutQueryWithCurrencyTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenPreCheckoutQueryWithCurrency(
    currency: String,
    handler: suspend HandlerBotCall<PreCheckoutQueryEvent>.() -> Unit
) = onPreCheckoutQueryEvent { whenPreCheckoutQueryWithCurrency(currency, handler) }

// ==================== PurchasedPaidMediaEvent Filtered Matchers ====================

// --- onPurchasedPaidMediaFromUser (stackable) ---

@JvmName("onPurchasedPaidMediaFromUserPurchasedPaidMediaEvent")
fun HandlerRoute<PurchasedPaidMediaEvent>.onPurchasedPaidMediaFromUser(
    userId: Long,
    build: HandlerRoute<PurchasedPaidMediaEvent>.() -> Unit
) = select({ if (it.event.purchasedPaidMedia.from.id == userId) it else null }, build)

@JvmName("onPurchasedPaidMediaFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.onPurchasedPaidMediaFromUser(
    userId: Long,
    build: HandlerRoute<PurchasedPaidMediaEvent>.() -> Unit
) = onPurchasedPaidMediaEvent { onPurchasedPaidMediaFromUser(userId, build) }

// --- whenPurchasedPaidMediaFromUser (terminal) ---

@JvmName("whenPurchasedPaidMediaFromUserPurchasedPaidMediaEvent")
fun HandlerRoute<PurchasedPaidMediaEvent>.whenPurchasedPaidMediaFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<PurchasedPaidMediaEvent>.() -> Unit
) = select({ if (it.event.purchasedPaidMedia.from.id == userId) it else null }) { handle(handler) }

@JvmName("whenPurchasedPaidMediaFromUserTelegramBotEvent")
fun HandlerRoute<TelegramBotEvent>.whenPurchasedPaidMediaFromUser(
    userId: Long,
    handler: suspend HandlerBotCall<PurchasedPaidMediaEvent>.() -> Unit
) = onPurchasedPaidMediaEvent { whenPurchasedPaidMediaFromUser(userId, handler) }
