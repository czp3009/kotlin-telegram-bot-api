package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.EventRoute
import com.hiczp.telegram.bot.application.dispatcher.handler.match
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlinx.coroutines.CoroutineScope
import kotlin.jvm.JvmName

@JvmName("allOfTelegramBotEvent")
fun <T : TelegramBotEvent> EventRoute<T>.allOf(
    vararg predicates: suspend (TelegramBotEventContext<T>) -> Boolean,
    build: EventRoute<T>.() -> Unit
): EventRoute<T> = match({ ctx -> predicates.all { it(ctx) } }, build)

@JvmName("whenAllOfTelegramBotEvent")
fun <T : TelegramBotEvent> EventRoute<T>.whenAllOf(
    vararg predicates: suspend (TelegramBotEventContext<T>) -> Boolean,
    handler: suspend CoroutineScope.(TelegramBotEventContext<T>) -> Unit
) = allOf(*predicates) { handle(handler) }

@JvmName("anyOfTelegramBotEvent")
fun <T : TelegramBotEvent> EventRoute<T>.anyOf(
    vararg predicates: suspend (TelegramBotEventContext<T>) -> Boolean,
    build: EventRoute<T>.() -> Unit
): EventRoute<T> = match({ ctx -> predicates.any { it(ctx) } }, build)

@JvmName("whenAnyOfTelegramBotEvent")
fun <T : TelegramBotEvent> EventRoute<T>.whenAnyOf(
    vararg predicates: suspend (TelegramBotEventContext<T>) -> Boolean,
    handler: suspend CoroutineScope.(TelegramBotEventContext<T>) -> Unit
) = anyOf(*predicates) { handle(handler) }

@JvmName("notTelegramBotEvent")
fun <T : TelegramBotEvent> EventRoute<T>.not(
    predicate: suspend (TelegramBotEventContext<T>) -> Boolean,
    build: EventRoute<T>.() -> Unit
): EventRoute<T> = match({ ctx -> !predicate(ctx) }, build)

@JvmName("whenNotTelegramBotEvent")
fun <T : TelegramBotEvent> EventRoute<T>.whenNot(
    predicate: suspend (TelegramBotEventContext<T>) -> Boolean,
    handler: suspend CoroutineScope.(TelegramBotEventContext<T>) -> Unit
) = not(predicate) { handle(handler) }
