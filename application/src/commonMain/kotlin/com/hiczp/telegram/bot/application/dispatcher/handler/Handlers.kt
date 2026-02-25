package com.hiczp.telegram.bot.application.dispatcher.handler

import com.hiczp.telegram.bot.application.command.matchesCommand
import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.protocol.event.CallbackQueryEvent
import com.hiczp.telegram.bot.protocol.event.InlineQueryEvent
import com.hiczp.telegram.bot.protocol.event.MessageEvent
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
 *     on<MessageEvent> {
 *         command("start") { ctx ->
 *             ctx.client.sendMessage(ctx.event.message.chat.id, "Welcome!")
 *         }
 *     }
 *     on<CallbackQueryEvent> {
 *         callbackData("confirm") { ctx ->
 *             // Handle callback
 *         }
 *     }
 *     command("stop") { ctx -> ... }
 *     command("help") { ctx -> ... }
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
    // Root node: pass through without any filtering
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
 * // In a separate module
 * val adminRoutes = handling {
 *     command("admin") { ctx -> /* ... */ }
 * }
 *
 * // In the main routing configuration
 * handling {
 *     include(adminRoutes)
 *     command("start") { ctx -> /* ... */ }
 * }
 * ```
 *
 * @param other The [RouteNode] to include as a child of this route.
 */
fun EventRoute<*>.include(other: RouteNode) {
    this.node.children.add(other)
}

/**
 * Creates a filtered child route that only accepts events matching a predicate.
 *
 * The predicate is evaluated for each event. If it returns true, the event
 * is routed to the child branch; otherwise, the event is not routed.
 *
 * Example usage:
 * ```kotlin
 * handling {
 *     on<MessageEvent> {
 *         filter({ it.event.message.text?.startsWith("/") == true }) {
 *             handle { ctx -> println("Received command: ${ctx.event.message.text}") }
 *         }
 *     }
 * }
 * ```
 *
 * @param T The event type of the parent route.
 * @param predicate A suspending function that determines if the event should be routed.
 * @param build A builder lambda for configuring the filtered child route.
 * @return The created child [EventRoute].
 */
fun <T : TelegramBotEvent> EventRoute<T>.filter(
    predicate: suspend (TelegramBotEventContext<T>) -> Boolean,
    build: EventRoute<T>.() -> Unit
) = select({ if (predicate(it)) it else null }, build)

/**
 * Registers a handler for a specific bot command.
 *
 * This extension function must be called within a [MessageEvent] scope.
 * The command matching supports both `/command` and `/command@bot_username` formats,
 * using the bot's username from the context.
 *
 * Example usage:
 * ```kotlin
 * handling {
 *     on<MessageEvent> {
 *         command("start") { ctx ->
 *             ctx.client.sendMessage(ctx.event.message.chat.id, "Welcome!")
 *         }
 *     }
 * }
 * ```
 *
 * @param command The command name without the leading slash (e.g., "start" for "/start").
 * @param handler A suspending function that handles the command.
 */
fun EventRoute<MessageEvent>.command(
    command: String,
    handler: suspend (TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.matchesCommand(command, it.me().username)) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a command handler directly at the root level.
 *
 * This allows registering command handlers without explicitly wrapping them
 * in an `on<MessageEvent>` block. The function internally creates the
 * MessageEvent scope automatically.
 *
 * Example usage:
 * ```kotlin
 * handling {
 *     command("start") { ctx ->
 *         ctx.client.sendMessage(ctx.event.message.chat.id, "Welcome!")
 *     }
 * }
 * ```
 *
 * @param command The command name without the leading slash.
 * @param handler A suspending function that handles the command.
 */
fun EventRoute<TelegramBotEvent>.command(
    command: String,
    handler: suspend (TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { command(command, handler) }

/**
 * Registers a handler for messages with exact text match.
 *
 * This extension function must be called within a [MessageEvent] scope.
 * Only messages whose text content exactly matches [exact] will be handled.
 *
 * Example usage:
 * ```kotlin
 * handling {
 *     on<MessageEvent> {
 *         text("hello") { ctx ->
 *             ctx.client.sendMessage(ctx.event.message.chat.id, "Hi there!")
 *         }
 *     }
 * }
 * ```
 *
 * @param exact The exact text string to match.
 * @param handler A suspending function that handles the matching message.
 */
fun EventRoute<MessageEvent>.text(
    exact: String,
    handler: suspend (TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.text == exact) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a text handler directly at the root level.
 *
 * This allows registering text handlers without explicitly wrapping them
 * in an `on<MessageEvent>` block. The function internally creates the
 * MessageEvent scope automatically.
 *
 * Example usage:
 * ```kotlin
 * handling {
 *     text("hello") { ctx ->
 *         ctx.client.sendMessage(ctx.event.message.chat.id, "Hi there!")
 *     }
 * }
 * ```
 *
 * @param exact The exact text string to match.
 * @param handler A suspending function that handles the matching message.
 */
fun EventRoute<TelegramBotEvent>.text(
    exact: String,
    handler: suspend (TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { text(exact, handler) }

/**
 * Registers a handler for messages matching a regular expression.
 *
 * This extension function must be called within a [MessageEvent] scope.
 * Only messages whose text content matches [pattern] will be handled.
 *
 * Example usage:
 * ```kotlin
 * handling {
 *     on<MessageEvent> {
 *         regex(Regex("(?i)^hello")) { ctx ->
 *             ctx.client.sendMessage(ctx.event.message.chat.id, "Hi there!")
 *         }
 *     }
 * }
 * ```
 *
 * @param pattern The regular expression pattern to match.
 * @param handler A suspending function that handles the matching message.
 */
fun EventRoute<MessageEvent>.regex(
    pattern: Regex,
    handler: suspend (TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.text?.matches(pattern) == true) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a regex handler directly at the root level.
 *
 * This allows registering regex handlers without explicitly wrapping them
 * in an `on<MessageEvent>` block. The function internally creates the
 * MessageEvent scope automatically.
 *
 * Example usage:
 * ```kotlin
 * handling {
 *     regex(Regex("(?i)^hello")) { ctx ->
 *         ctx.client.sendMessage(ctx.event.message.chat.id, "Hi there!")
 *     }
 * }
 * ```
 *
 * @param pattern The regular expression pattern to match.
 * @param handler A suspending function that handles the matching message.
 */
fun EventRoute<TelegramBotEvent>.regex(
    pattern: Regex,
    handler: suspend (TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { regex(pattern, handler) }

/**
 * Registers a handler for callback queries with specific data.
 *
 * This extension function must be called within a [CallbackQueryEvent] scope.
 * Only callbacks whose data exactly matches [data] will be handled.
 *
 * Example usage:
 * ```kotlin
 * handling {
 *     on<CallbackQueryEvent> {
 *         callbackData("confirm_purchase") { ctx ->
 *             // Handle purchase confirmation
 *         }
 *     }
 * }
 * ```
 *
 * @param data The callback data string to match.
 * @param handler A suspending function that handles the matching callback.
 */
fun EventRoute<CallbackQueryEvent>.callbackData(
    data: String,
    handler: suspend (TelegramBotEventContext<CallbackQueryEvent>) -> Unit
) {
    select({ if (it.event.callbackQuery.data == data) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a callback data handler directly at the root level.
 *
 * This allows registering callback handlers without explicitly wrapping them
 * in an `on<CallbackQueryEvent>` block. The function internally creates the
 * CallbackQueryEvent scope automatically.
 *
 * Example usage:
 * ```kotlin
 * handling {
 *     callbackData("confirm") { ctx ->
 *         // Handle callback
 *     }
 * }
 * ```
 *
 * @param data The callback data string to match.
 * @param handler A suspending function that handles the matching callback.
 */
fun EventRoute<TelegramBotEvent>.callbackData(
    data: String,
    handler: suspend (TelegramBotEventContext<CallbackQueryEvent>) -> Unit
) = on<CallbackQueryEvent> { callbackData(data, handler) }

/**
 * Registers a handler for inline queries with specific query text.
 *
 * This extension function must be called within an [InlineQueryEvent] scope.
 * Only inline queries whose text exactly matches [query] will be handled.
 *
 * Example usage:
 * ```kotlin
 * handling {
 *     on<InlineQueryEvent> {
 *         inlineQuery("search") { ctx ->
 *             // Answer the inline query
 *         }
 *     }
 * }
 * ```
 *
 * @param query The query text to match.
 * @param handler A suspending function that handles the matching inline query.
 */
fun EventRoute<InlineQueryEvent>.inlineQuery(
    query: String,
    handler: suspend (TelegramBotEventContext<InlineQueryEvent>) -> Unit
) {
    select({ if (it.event.inlineQuery.query == query) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers an inline query handler directly at the root level.
 *
 * This allows registering inline query handlers without explicitly wrapping them
 * in an `on<InlineQueryEvent>` block. The function internally creates the
 * InlineQueryEvent scope automatically.
 *
 * Example usage:
 * ```kotlin
 * handling {
 *     inlineQuery("search") { ctx ->
 *         // Answer the inline query
 *     }
 * }
 * ```
 *
 * @param query The query text to match.
 * @param handler A suspending function that handles the matching inline query.
 */
fun EventRoute<TelegramBotEvent>.inlineQuery(
    query: String,
    handler: suspend (TelegramBotEventContext<InlineQueryEvent>) -> Unit
) = on<InlineQueryEvent> { inlineQuery(query, handler) }
