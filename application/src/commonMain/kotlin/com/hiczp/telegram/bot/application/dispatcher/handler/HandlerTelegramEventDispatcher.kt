package com.hiczp.telegram.bot.application.dispatcher.handler

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.TelegramEventDispatcher
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * A dispatcher implementation that routes events through a tree of [RouteNode]s.
 *
 * This dispatcher works with the routing DSL provided by [handling] to create
 * a type-safe, hierarchical event handling system. Events are dispatched to
 * the root node, which uses depth-first short-circuit evaluation to find a
 * matching handler.
 *
 * There are two ways to handle unmatched events:
 *
 * 1. **Dead letter handler in routing DSL** (recommended): Add a root-level `handle`
 *    at the end of your routing configuration to catch all unhandled events:
 *    ```kotlin
 *    handling {
 *        commandEndpoint("start") { /* ... */ }
 *        // ... other routes ...
 *        handle { ctx -> /* catches all unhandled events */ }
 *    }
 *    ```
 *
 * 2. **Override [deadLetter] method**: Subclass this dispatcher and override
 *    the [deadLetter] method for custom handling when no route matches:
 *    ```kotlin
 *    class MyDispatcher(rootNode: RouteNode) : HandlerTelegramEventDispatcher(rootNode) {
 *        override suspend fun deadLetter(context: TelegramBotEventContext<TelegramBotEvent>) {
 *            // Custom handling, e.g., send to dead letter queue
 *            myDeadLetterQueue.send(context.event)
 *        }
 *    }
 *    ```
 *
 * Example usage:
 * ```kotlin
 * // Create dispatcher using the handling DSL with dead letter handler
 * val dispatcher = HandlerTelegramEventDispatcher(handling {
 *     commandEndpoint("start") { ctx ->
 *         ctx.client.sendMessage(ctx.event.message.chat.id, "Welcome!")
 *     }
 *
 *     // Dead letter handler (recommended)
 *     handle { ctx ->
 *         ctx.client.sendMessage(ctx.event.extractChatId()!!, "Unknown command")
 *     }
 * })
 * ```
 *
 * @param rootNode The root [RouteNode] of the event routing tree, typically
 *        created by the [handling] function.
 */
open class HandlerTelegramEventDispatcher(
    private val rootNode: RouteNode
) : TelegramEventDispatcher {
    /**
     * Dispatches an event through the routing tree.
     *
     * The event is passed to the root node, which attempts to match and handle it
     * using depth-first traversal. If no route in the tree consumes the event,
     * it is passed to [deadLetter].
     *
     * @param context The event context to dispatch.
     */
    override suspend fun dispatch(context: TelegramBotEventContext<TelegramBotEvent>) {
        if (!rootNode.execute(context)) {
            deadLetter(context)
        }
    }

    /**
     * Handles the event not consumed by any route in the tree.
     *
     * This method is called when:
     * - No handler in the routing tree matches the event
     * - No root-level `handle` is configured as a fallback
     *
     * By default, this method logs the unhandled event at WARN level.
     * Subclasses can override this method to implement custom dead letter handling,
     * such as sending to a dead letter queue or metrics collection.
     *
     * **Note**: The recommended approach for most use cases is to use a root-level
     * `handle` in the routing DSL instead of overriding this method, as it provides
     * more flexibility and access to the full handler context (including [CoroutineScope]
     * for structured concurrency).
     *
     * @param context The event context containing the unhandled event.
     */
    open suspend fun deadLetter(context: TelegramBotEventContext<TelegramBotEvent>) {
        logger.warn { "Unhandled event: ${context.event}" }
    }
}
