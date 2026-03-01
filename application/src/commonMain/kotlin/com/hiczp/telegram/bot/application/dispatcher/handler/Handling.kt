package com.hiczp.telegram.bot.application.dispatcher.handler

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent

/**
 * Top-level entry point for the routing DSL.
 *
 * Builds and returns a root [RouteNode] that can be used directly for unit testing
 * or passed to [HandlerTelegramEventDispatcher].
 *
 * Example usage:
 * ```kotlin
 * val rootNode = handling {
 *     commandEndpoint("start") {
 *         client.sendMessage(event.message.chat.id, "Welcome!")
 *     }
 *     onCallbackQueryEvent {
 *         onCallbackData("confirm") {
 *             // Handle callback
 *         }
 *     }
 *     // Dead letter handler for unhandled events
 *     handle { println("Unhandled: ${event.updateId}") }
 * }
 *
 * // Use directly
 * rootNode.execute(context)
 *
 * // Or create a dispatcher
 * val dispatcher = HandlerTelegramEventDispatcher(rootNode)
 * ```
 *
 * @param build A builder lambda for configuring the root route.
 * @return The assembled root [RouteNode].
 */
fun handling(build: HandlerRoute<TelegramBotEvent>.() -> Unit): RouteNode {
    val rootNode = RouteNode { it }
    HandlerRoute<TelegramBotEvent>(rootNode).build()
    return rootNode
}

/**
 * Mounts a pre-built [RouteNode] as a child of the current route.
 *
 * Similar to Ktor's routing include mechanism, this allows modularizing routes
 * across multiple files or modules. The included node becomes a direct child
 * and will be evaluated during event dispatching.
 *
 * Example usage:
 * ```kotlin
 * val adminRoutes = handling {
 *     commandEndpoint("admin") { /* ... */ }
 * }
 *
 * handling {
 *     include(adminRoutes)
 *     commandEndpoint("start") { /* ... */ }
 * }
 * ```
 *
 * @param other The [RouteNode] to include as a child of this route.
 */
fun HandlerRoute<*>.include(other: RouteNode) {
    node.children.add(other)
}

/**
 * Creates a child route that only accepts events matching a predicate.
 *
 * The predicate is evaluated for each event. If it returns true, the event
 * is routed to the child branch; otherwise, the event is not routed.
 *
 * Example usage:
 * ```kotlin
 * handling {
 *     onMessageEvent {
 *         match({ it.event.message.text?.startsWith("/") == true }) {
 *             handle { println("Received command: ${event.message.text}") }
 *         }
 *     }
 * }
 * ```
 *
 * @param T The event type of the parent route.
 * @param predicate A suspending function that determines if the event should be routed.
 * @param build A builder lambda for configuring the child route.
 */
fun <T : TelegramBotEvent> HandlerRoute<T>.match(
    predicate: suspend (TelegramBotEventContext<T>) -> Boolean,
    build: HandlerRoute<T>.() -> Unit
) = select({ if (predicate(it)) it else null }, build)

/**
 * Creates a conditional handler that only executes when the predicate returns true.
 *
 * This is a convenience function that combines matching and handling in one step.
 * If the predicate returns true, the handler is invoked immediately; otherwise,
 * the event is not consumed and routing continues to sibling nodes.
 *
 * Example usage:
 * ```kotlin
 * handling {
 *     onMessageEvent {
 *         whenMatch({ it.event.message.text?.length ?: 0 > 10 }) {
 *             client.sendMessage(event.message.chat.id, "Long message!")
 *         }
 *     }
 * }
 * ```
 *
 * @param T The event type of the parent route.
 * @param predicate A suspending function that determines if the event should be handled.
 * @param handler A suspending function with [HandlerBotCall] receiver that handles the event.
 */
fun <T : TelegramBotEvent> HandlerRoute<T>.whenMatch(
    predicate: suspend (TelegramBotEventContext<T>) -> Boolean,
    handler: suspend HandlerBotCall<T>.() -> Unit
) = select({ if (predicate(it)) it else null }) { handle(handler) }
