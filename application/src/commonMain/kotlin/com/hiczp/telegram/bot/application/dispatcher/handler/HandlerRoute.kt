package com.hiczp.telegram.bot.application.dispatcher.handler

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.context.castOrNull
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlinx.coroutines.coroutineScope

/**
 * Build-time route constructor for the handler DSL.
 *
 * Route blocks are evaluated when the dispatcher is built, not when an update is handled.
 * Keep route blocks declarative: register filters and handlers here. Per-update work belongs
 * in [filter] predicates or [handle] blocks.
 *
 * @param T The event type visible at this route level.
 */
@TelegramBotDsl
class HandlerRoute<T : TelegramBotEvent>(internal val node: RouteNode) {
    /**
     * Registers the runtime handler for this route.
     *
     * The handler is invoked only after this route matches and none of its children consume the event.
     * Registering a handler consumes the event and stops further route matching.
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
     * Adds a child route guarded by an asynchronous predicate.
     *
     * Returning `true` enters the child branch. Returning `false` marks this branch as not matched,
     * and the dispatcher backtracks to the nearest ancestor with an untested sibling branch.
     */
    fun filter(
        predicate: suspend HandlerFilterCall<T>.() -> Boolean,
        build: HandlerRoute<T>.() -> Unit
    ) {
        filterContext(
            selector = {
                if (predicate(this)) context else null
            },
            build = build
        )
    }

    /**
     * Adds a child route that narrows the event type to [R].
     */
    inline fun <reified R : T> filter(noinline build: HandlerRoute<R>.() -> Unit) {
        filterContext({ context.castOrNull<R>() }, build)
    }

    /**
     * Internal child route primitive for filters that need to replace the runtime context
     * while keeping the public DSL centered on [filter].
     */
    @PublishedApi
    internal fun <R : TelegramBotEvent> filterContext(
        selector: suspend HandlerRouteSelection<T>.() -> TelegramBotEventContext<R>?,
        build: HandlerRoute<R>.() -> Unit
    ) {
        val childNode = RouteNode { context ->
            @Suppress("UNCHECKED_CAST")
            val typedContext = context as TelegramBotEventContext<T>
            selector(
                HandlerRouteSelection(
                    context = typedContext,
                    filterCall = DefaultHandlerFilterCall(typedContext)
                )
            )
        }
        node.children.add(childNode)
        HandlerRoute<R>(childNode).build()
    }
}

/**
 * Internal receiver used by filters that can transform the route context.
 */
@PublishedApi
internal class HandlerRouteSelection<T : TelegramBotEvent>(
    @PublishedApi
    internal val context: TelegramBotEventContext<T>,
    private val filterCall: HandlerFilterCall<T>,
) : HandlerFilterCall<T> by filterCall
