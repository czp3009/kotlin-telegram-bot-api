package com.hiczp.telegram.bot.application.dispatcher.handler.matcher

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.EventRoute
import com.hiczp.telegram.bot.application.dispatcher.handler.match
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlinx.coroutines.CoroutineScope

/**
 * Creates a route that matches when all predicates are satisfied.
 *
 * All predicates must return true for the event to be routed to the child branch.
 *
 * Example usage:
 * ```kotlin
 * handling {
 *     on<MessageEvent> {
 *         allOf(
 *             { it.event.message.text != null },
 *             { it.event.message.chat.type == ChatType.PRIVATE }
 *         ) {
 *             handle { ctx -> println("Private text message") }
 *         }
 *     }
 * }
 * ```
 *
 * @param T The event type of the parent route.
 * @param predicates The predicates that must all be satisfied.
 * @param build A builder lambda for configuring the child route.
 * @return The created child [com.hiczp.telegram.bot.application.dispatcher.handler.EventRoute].
 */
fun <T : TelegramBotEvent> EventRoute<T>.allOf(
    vararg predicates: suspend (TelegramBotEventContext<T>) -> Boolean,
    build: EventRoute<T>.() -> Unit
): EventRoute<T> = match({ ctx -> predicates.all { it(ctx) } }, build)

/**
 * Creates a handler that matches when all predicates are satisfied.
 *
 * Combines [allOf] with [handle] in a single call.
 *
 * @param T The event type of the parent route.
 * @param predicates The predicates that must all be satisfied.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun <T : TelegramBotEvent> EventRoute<T>.whenAllOf(
    vararg predicates: suspend (TelegramBotEventContext<T>) -> Boolean,
    handler: suspend CoroutineScope.(TelegramBotEventContext<T>) -> Unit
) = allOf(*predicates) { handle(handler) }

/**
 * Creates a route that matches when any predicate is satisfied.
 *
 * At least one predicate must return true for the event to be routed to the child branch.
 *
 * Example usage:
 * ```kotlin
 * handling {
 *     on<MessageEvent> {
 *         anyOf(
 *             { it.event.message.photo != null },
 *             { it.event.message.video != null }
 *         ) {
 *             handle { ctx -> println("Photo or video") }
 *         }
 *     }
 * }
 * ```
 *
 * @param T The event type of the parent route.
 * @param predicates The predicates where at least one must be satisfied.
 * @param build A builder lambda for configuring the child route.
 * @return The created child [EventRoute].
 */
fun <T : TelegramBotEvent> EventRoute<T>.anyOf(
    vararg predicates: suspend (TelegramBotEventContext<T>) -> Boolean,
    build: EventRoute<T>.() -> Unit
): EventRoute<T> = match({ ctx -> predicates.any { it(ctx) } }, build)

/**
 * Creates a handler that matches when any predicate is satisfied.
 *
 * Combines [anyOf] with [handle] in a single call.
 *
 * @param T The event type of the parent route.
 * @param predicates The predicates where at least one must be satisfied.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun <T : TelegramBotEvent> EventRoute<T>.whenAnyOf(
    vararg predicates: suspend (TelegramBotEventContext<T>) -> Boolean,
    handler: suspend CoroutineScope.(TelegramBotEventContext<T>) -> Unit
) = anyOf(*predicates) { handle(handler) }

/**
 * Creates a route that matches when the predicate is not satisfied.
 *
 * The event is routed only when the predicate returns false.
 *
 * Example usage:
 * ```kotlin
 * handling {
 *     on<MessageEvent> {
 *         not({ it.event.message.text?.startsWith("/") == true }) {
 *             handle { ctx -> println("Not a command") }
 *         }
 *     }
 * }
 * ```
 *
 * @param T The event type of the parent route.
 * @param predicate The predicate that must not be satisfied.
 * @param build A builder lambda for configuring the child route.
 * @return The created child [EventRoute].
 */
fun <T : TelegramBotEvent> EventRoute<T>.not(
    predicate: suspend (TelegramBotEventContext<T>) -> Boolean,
    build: EventRoute<T>.() -> Unit
): EventRoute<T> = match({ ctx -> !predicate(ctx) }, build)

/**
 * Creates a handler that matches when the predicate is not satisfied.
 *
 * Combines [not] with [handle] in a single call.
 *
 * @param T The event type of the parent route.
 * @param predicate The predicate that must not be satisfied.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun <T : TelegramBotEvent> EventRoute<T>.whenNot(
    predicate: suspend (TelegramBotEventContext<T>) -> Boolean,
    handler: suspend CoroutineScope.(TelegramBotEventContext<T>) -> Unit
) = not(predicate) { handle(handler) }
