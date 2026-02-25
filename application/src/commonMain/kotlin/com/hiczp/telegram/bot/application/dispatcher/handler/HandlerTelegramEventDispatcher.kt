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
 * matching handler. If no handler consumes the event, it is passed to
 * [deadLetter] for logging or custom handling.
 *
 * Example usage:
 * ```kotlin
 * // Create dispatcher using the handling DSL
 * val dispatcher = HandlerTelegramEventDispatcher(handling {
 *     on<MessageEvent> {
 *         command("start") { ctx ->
 *             ctx.client.sendMessage(ctx.event.message.chat.id, "Welcome!")
 *         }
 *     }
 * })
 *
 * // Or create the dispatcher from a pre-built root node
 * val rootNode = handling {
 *     command("help") { ctx -> /* ... */ }
 * }
 * val dispatcher = HandlerTelegramEventDispatcher(rootNode)
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
     * By default, this method logs the unhandled event at WARN level.
     * Subclasses can override this method to implement custom dead letter handling,
     * such as sending a default response or metrics collection.
     *
     * @param context The event context containing the unhandled event.
     */
    open suspend fun deadLetter(context: TelegramBotEventContext<TelegramBotEvent>) {
        logger.warn { "Unhandled event: ${context.event}" }
    }
}
