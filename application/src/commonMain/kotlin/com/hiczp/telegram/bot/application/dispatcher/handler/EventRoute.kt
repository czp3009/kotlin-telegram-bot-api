package com.hiczp.telegram.bot.application.dispatcher.handler

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.context.castOrNull
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent

/**
 * A typed routing node that defines event selection and handling logic.
 *
 * EventRoute provides a type-safe DSL for building event routing trees.
 * Each route is parameterized by an event type [T] and can selectively
 * route to child routes based on event characteristics.
 *
 * The framework handles unchecked casts internally, providing type safety
 * at the user level while maintaining runtime efficiency.
 *
 * @param T The type of Telegram event this route handles.
 * @property node The underlying [RouteNode] that executes the routing logic.
 */
@TelegramBotDsl
class EventRoute<T : TelegramBotEvent>(
    val node: RouteNode
) {
    /**
     * Core routing primitive that creates a child route with a custom selector.
     *
     * The selector function determines whether an incoming event should be
     * routed to this child branch. If the selector returns null, the event
     * is not routed to this branch.
     *
     * Example usage:
     * ```kotlin
     * handling {
     *     select({ ctx ->
     *         ctx.castOrNull<MessageEvent>()?.takeIf {
     *             it.event.message.chat.id == -100123L
     *         }
     *     }) {
     *         handle { ctx -> println(ctx.event.message.text) }
     *     }
     * }
     * ```
     *
     * @param R The target event type for the child route.
     * @param selector A function that receives the parent context and returns
     *        a context with the target event type, or null if the event should
     *        not be routed to this child.
     * @param build A builder lambda for configuring the child route.
     * @return The created child [EventRoute].
     */
    fun <R : TelegramBotEvent> select(
        selector: suspend (TelegramBotEventContext<T>) -> TelegramBotEventContext<R>?,
        build: EventRoute<R>.() -> Unit
    ): EventRoute<R> {
        val childNode = RouteNode { context ->
            @Suppress("UNCHECKED_CAST")
            selector(context as TelegramBotEventContext<T>)
        }
        node.children.add(childNode)
        val childRoute = EventRoute<R>(childNode)
        childRoute.build()
        return childRoute
    }

    /**
     * Registers a handler function for this route.
     *
     * The handler is invoked when an event reaches this route and no child
     * route has consumed it. Only one handler can be registered per route;
     * subsequent calls will replace the previous handler.
     *
     * Once a handler is invoked, the event is considered consumed and routing stops.
     *
     * @param handler A suspending function that receives the event context.
     */
    fun handle(handler: suspend (TelegramBotEventContext<T>) -> Unit) {
        node.handler = { context ->
            @Suppress("UNCHECKED_CAST")
            handler(context as TelegramBotEventContext<T>)
        }
    }

    /**
     * Creates a child route for a specific event subtype.
     *
     * This is a convenience method that uses [castOrNull] to safely filter
     * events by type. Events that are not of type [R] are not routed to
     * the child branch.
     *
     * Example usage:
     * ```kotlin
     * handling {
     *     // First cast to the target type
     *     on<MessageEvent> {
     *         // Then filter by group
     *         filter({ it.event.message.chat.id == -100123L }) {
     *             command("admin") { ... }
     *             text("ping") { ... }
     *         }
     *     }
     * }
     * ```
     *
     * @param R The specific event subtype to handle.
     * @param builder A builder lambda for configuring the child route.
     * @return The created child [EventRoute].
     */
    inline fun <reified R : T> on(
        noinline builder: EventRoute<R>.() -> Unit
    ): EventRoute<R> = select({ it.castOrNull<R>() }, builder)

    /**
     * Creates a child route for a specific event subtype with a predicate filter.
     *
     * This is a convenience method that combines type casting and filtering in one step.
     * Events that are not of type [R] or do not satisfy the predicate are not routed
     * to the child branch.
     *
     * Inside the predicate lambda, `it.event` is automatically inferred as type [R].
     *
     * Example usage:
     * ```kotlin
     * handling {
     *     // Type casting + conditional filtering in one step!
     *     // Note: it.event is automatically inferred as MessageEvent
     *     on<MessageEvent>({ it.event.message.chat.id == -100123L }) {
     *         command("admin") { ... }
     *         text("ping") { ... }
     *     }
     * }
     * ```
     *
     * @param R The specific event subtype to handle.
     * @param predicate A suspending function that filters events based on custom conditions.
     * @param build A builder lambda for configuring the child route.
     * @return The created child [EventRoute].
     */
    inline fun <reified R : T> on(
        noinline predicate: suspend (TelegramBotEventContext<R>) -> Boolean,
        noinline build: EventRoute<R>.() -> Unit
    ): EventRoute<R> = on<R> {
        filter(predicate, build)
    }
}
