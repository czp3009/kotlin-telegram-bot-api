package com.hiczp.telegram.bot.application.dispatcher.handler

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

/**
 * Internal route tree node.
 *
 * Matching is depth-first with full backtracking. If a deep branch does not match
 * or does not consume the event, the miss bubbles up until the nearest ancestor
 * with an untested sibling branch. A handler consumes the event and stops matching.
 */
class RouteNode(
    val filter: suspend (TelegramBotEventContext<*>) -> TelegramBotEventContext<*>?
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
        val matchedContext = filter(context) ?: return false

        for (child in children) {
            if (child.execute(matchedContext)) return true
        }

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
