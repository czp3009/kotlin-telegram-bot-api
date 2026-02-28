package com.hiczp.telegram.bot.application.dispatcher.handler

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlinx.coroutines.CoroutineScope

/**
 * Top-level entry point for the routing DSL.
 *
 * Builds and returns a root [RouteNode] that can be used directly for unit testing
 * or passed to [HandlerTelegramEventDispatcher].
 *
 * Example usage:
 * ```kotlin
 * val rootNode = handling {
 *     commandEndpoint("start") { ctx ->
 *         ctx.client.sendMessage(ctx.event.message.chat.id, "Welcome!")
 *     }
 *     on<CallbackQueryEvent> {
 *         callbackData("confirm") { ctx ->
 *             // Handle callback
 *         }
 *     }
 *     commandEndpoint("stop") { /* ... */ }
 *     commandEndpoint("help") { /* ... */ }
 *     // Dead letter handler for unhandled events
 *     handle { ctx -> println("Unhandled: ${ctx.event.updateId}") }
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
inline fun handling(
    build: EventRoute<TelegramBotEvent>.() -> Unit
): RouteNode {
    val rootNode = RouteNode { it }
    EventRoute<TelegramBotEvent>(rootNode).build()
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
fun EventRoute<*>.include(other: RouteNode) {
    this.node.children.add(other)
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
 *     on<MessageEvent> {
 *         match({ it.event.message.text?.startsWith("/") == true }) {
 *             handle { ctx -> println("Received command: ${ctx.event.message.text}") }
 *         }
 *     }
 * }
 * ```
 *
 * @param T The event type of the parent route.
 * @param predicate A suspending function that determines if the event should be routed.
 * @param build A builder lambda for configuring the child route.
 * @return The created child [EventRoute].
 */
fun <T : TelegramBotEvent> EventRoute<T>.match(
    predicate: suspend (TelegramBotEventContext<T>) -> Boolean,
    build: EventRoute<T>.() -> Unit
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
 *     on<MessageEvent> {
 *         whenMatch({ it.event.message.text?.length ?: 0 > 10 }) { ctx ->
 *             ctx.client.sendMessage(ctx.event.message.chat.id, "Long message!")
 *         }
 *     }
 * }
 * ```
 *
 * @param T The event type of the parent route.
 * @param predicate A suspending function that determines if the event should be handled.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the event.
 */
fun <T : TelegramBotEvent> EventRoute<T>.whenMatch(
    predicate: suspend (TelegramBotEventContext<T>) -> Boolean,
    handler: suspend CoroutineScope.(TelegramBotEventContext<T>) -> Unit
) = select({ if (predicate(it)) it else null }) {
    handle(handler)
}
