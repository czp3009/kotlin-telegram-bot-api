package com.hiczp.telegram.bot.application.dispatcher.handler

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

/**
 * Internal routing node that executes depth-first short-circuit matching.
 *
 * This class is the engine behind [HandlerRoute] and [CommandRoute],
 * handling the actual event dispatching logic. It is stripped of generic type constraints
 * for maximum runtime efficiency.
 *
 * The matching algorithm:
 * First, the selector is applied to match and transform the incoming context.
 * If the selector returns null, this node does not match and returns false.
 * If the selector succeeds, child nodes are tried in order (depth-first).
 * If no child consumes the event, this node's handler is invoked if present.
 *
 * @property selector A function that receives an event context and returns
 *         a transformed context if the event matches, or null otherwise.
 */
class RouteNode(
    val selector: suspend (TelegramBotEventContext<*>) -> TelegramBotEventContext<*>?
) {
    /**
     * Child nodes to try after this node's selector matches.
     */
    val children = mutableListOf<RouteNode>()

    /**
     * The handler to invoke if no child node consumes the event.
     * The handler receives a [CoroutineScope] as its receiver, allowing
     * structured concurrency with `launch` and `async` inside the handler.
     */
    var handler: (suspend CoroutineScope.(TelegramBotEventContext<*>) -> Unit)? = null

    /**
     * Executes this node's matching logic against the given context.
     *
     * @param context The event context to match against.
     * @return true if the event was consumed by this node or a child, false otherwise.
     */
    suspend fun execute(context: TelegramBotEventContext<*>): Boolean {
        // Selector matching and type conversion
        val matchedContext = selector(context) ?: return false

        // Depth-first traversal of child nodes
        for (child in children) {
            if (child.execute(matchedContext)) return true
        }

        // Leaf node handling
        val currentHandler = handler
        if (currentHandler != null) {
            coroutineScope {
                currentHandler.invoke(this, matchedContext)
            }
            return true
        }

        return false
    }
}
