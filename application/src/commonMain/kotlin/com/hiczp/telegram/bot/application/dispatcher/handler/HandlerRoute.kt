package com.hiczp.telegram.bot.application.dispatcher.handler

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.context.castOrNull
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlinx.coroutines.coroutineScope

/**
 * Build-phase route constructor for the handler DSL.
 *
 * This class is used during route configuration (build phase) to construct
 * the routing tree. All methods are synchronous and non-suspending because
 * route configuration is a pure in-memory operation.
 *
 * Routes form a tree structure where each node can:
 * - Match events using selectors
 * - Contain child routes for nested matching
 * - Have a handler that processes matched events
 *
 * The [TelegramBotDsl] annotation prevents implicit access to outer DSL scopes,
 * ensuring clean separation between different route levels.
 *
 * Example:
 * ```kotlin
 * handling {
 *     onMessageEvent {
 *         onText("ping") {
 *             handle {
 *                 client.sendMessage(event.message.chat.id.toString(), "pong")
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * @param T The event type this route handles.
 */
@TelegramBotDsl
class HandlerRoute<T : TelegramBotEvent>(internal val node: RouteNode) {
    /**
     * Registers a handler for this route.
     *
     * The handler is invoked when an event reaches this route and no child
     * route has consumed it. The handler receives a [HandlerBotCall] as its
     * receiver, providing access to the event, client, and coroutine scope.
     *
     * Only one handler can be registered per route; subsequent calls will
     * replace the previous handler.
     *
     * Example:
     * ```kotlin
     * onMessageEvent {
     *     handle {
     *         // `this` is HandlerBotCall<MessageEvent>
     *         client.sendMessage(event.message.chat.id.toString(), "Hello!")
     *     }
     * }
     * ```
     *
     * @param handler The handler function with [HandlerBotCall] receiver.
     */
    fun handle(handler: suspend HandlerBotCall<T>.() -> Unit) {
        node.handler = { context ->
            coroutineScope {
                @Suppress("UNCHECKED_CAST")
                val call = DefaultHandlerBotCall(
                    delegate = context as TelegramBotEventContext<T>,
                    scope = this
                )
                handler(call)
            }
        }
    }

    /**
     * Creates a child route with a custom selector.
     *
     * This is the core routing primitive. The selector determines whether
     * an event should be routed to the child branch. If the selector returns
     * a non-null context, the event is passed to the child; otherwise, the
     * child is skipped.
     *
     * @param R The target event type for the child route.
     * @param selector A function that receives the parent context and returns
     *        a context with the target event type, or null if the event should
     *        not be routed to this child.
     * @param build A builder lambda for configuring the child [HandlerRoute].
     */
    fun <R : TelegramBotEvent> select(
        selector: suspend (TelegramBotEventContext<T>) -> TelegramBotEventContext<R>?,
        build: HandlerRoute<R>.() -> Unit
    ) {
        val childNode = RouteNode { context ->
            @Suppress("UNCHECKED_CAST")
            selector(context as TelegramBotEventContext<T>)
        }
        node.children.add(childNode)
        HandlerRoute<R>(childNode).build()
    }

    /**
     * Creates a child route for a specific event subtype.
     *
     * This is a convenience method that uses [castOrNull] to safely match
     * events by type. Events that are not of type [R] are not routed to
     * the child branch.
     *
     * Example:
     * ```kotlin
     * handling {
     *     onMessageEvent {
     *         onText("hello") { ... }
     *     }
     *     onCallbackQueryEvent {
     *         onCallbackData("confirm") { ... }
     *     }
     * }
     * ```
     *
     * @param R The specific event subtype to handle.
     * @param build A builder lambda for configuring the child route.
     */
    inline fun <reified R : T> on(noinline build: HandlerRoute<R>.() -> Unit) {
        select({ it.castOrNull<R>() }, build)
    }
}

/**
 * Creates a child [RouteNode] with the given selector and attaches it to the parent node.
 *
 * @param parentNode The parent node to attach the child to.
 * @param selector A function that receives a context and returns a transformed context, or null.
 * @return The created child [RouteNode].
 */
internal inline fun createChildNode(
    parentNode: RouteNode,
    crossinline selector: suspend (TelegramBotEventContext<*>) -> TelegramBotEventContext<*>?
): RouteNode {
    val childNode = RouteNode { context ->
        selector(context)
    }
    parentNode.children.add(childNode)
    return childNode
}
