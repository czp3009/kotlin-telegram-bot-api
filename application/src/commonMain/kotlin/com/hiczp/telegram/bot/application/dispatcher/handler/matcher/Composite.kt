@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlin.jvm.JvmName

// --- allOf (stackable) ---

@JvmName("allOfTelegramBotEvent")
fun <T : TelegramBotEvent> HandlerRoute<T>.allOf(
    vararg predicates: suspend (TelegramBotEventContext<T>) -> Boolean,
    build: HandlerRoute<T>.() -> Unit
) = select({ ctx -> if (predicates.all { it(ctx) }) ctx else null }, build)

// --- whenAllOf (terminal) ---

@JvmName("whenAllOfTelegramBotEvent")
fun <T : TelegramBotEvent> HandlerRoute<T>.whenAllOf(
    vararg predicates: suspend (TelegramBotEventContext<T>) -> Boolean,
    handler: suspend HandlerBotCall<T>.() -> Unit
) = allOf(*predicates) { handle(handler) }

// --- anyOf (stackable) ---

@JvmName("anyOfTelegramBotEvent")
fun <T : TelegramBotEvent> HandlerRoute<T>.anyOf(
    vararg predicates: suspend (TelegramBotEventContext<T>) -> Boolean,
    build: HandlerRoute<T>.() -> Unit
) = select({ ctx -> if (predicates.any { it(ctx) }) ctx else null }, build)

// --- whenAnyOf (terminal) ---

@JvmName("whenAnyOfTelegramBotEvent")
fun <T : TelegramBotEvent> HandlerRoute<T>.whenAnyOf(
    vararg predicates: suspend (TelegramBotEventContext<T>) -> Boolean,
    handler: suspend HandlerBotCall<T>.() -> Unit
) = anyOf(*predicates) { handle(handler) }

// --- not (stackable) ---

@JvmName("notTelegramBotEvent")
fun <T : TelegramBotEvent> HandlerRoute<T>.not(
    predicate: suspend (TelegramBotEventContext<T>) -> Boolean,
    build: HandlerRoute<T>.() -> Unit
) = select({ ctx -> if (!predicate(ctx)) ctx else null }, build)

// --- whenNot (terminal) ---

@JvmName("whenNotTelegramBotEvent")
fun <T : TelegramBotEvent> HandlerRoute<T>.whenNot(
    predicate: suspend (TelegramBotEventContext<T>) -> Boolean,
    handler: suspend HandlerBotCall<T>.() -> Unit
) = not(predicate) { handle(handler) }
