package com.hiczp.telegram.bot.application.dispatcher.handler

import com.hiczp.telegram.bot.application.command.matchesCommand
import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.protocol.constant.ChatType
import com.hiczp.telegram.bot.protocol.event.CallbackQueryEvent
import com.hiczp.telegram.bot.protocol.event.InlineQueryEvent
import com.hiczp.telegram.bot.protocol.event.MessageEvent
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
 *     command("admin") { ctx -> /* ... */ }
 * }
 *
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
 * @param handler A suspending function with [CoroutineScope] receiver that handles the command.
 */
fun EventRoute<MessageEvent>.command(
    command: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.matchesCommand(command, it.me().username)) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a command handler directly at the root level.
 *
 * @param command The command name without the leading slash.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the command.
 */
fun EventRoute<TelegramBotEvent>.command(
    command: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { command(command, handler) }

/**
 * Registers a handler for messages with exact text match.
 *
 * @param exact The exact text string to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching message.
 */
fun EventRoute<MessageEvent>.text(
    exact: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.text == exact) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a text handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.text(
    exact: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { text(exact, handler) }

/**
 * Registers a handler for messages matching a regular expression.
 *
 * @param pattern The regular expression pattern to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching message.
 */
fun EventRoute<MessageEvent>.textRegex(
    pattern: Regex,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.text?.matches(pattern) == true) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a text regex handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.textRegex(
    pattern: Regex,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { textRegex(pattern, handler) }

/**
 * Registers a handler for messages that contain a photo.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the photo message.
 */
fun EventRoute<MessageEvent>.photo(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.photo != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a photo handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.photo(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { photo(handler) }

/**
 * Registers a handler for messages that contain a video.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the video message.
 */
fun EventRoute<MessageEvent>.video(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.video != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a video handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.video(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { video(handler) }

/**
 * Registers a handler for messages that contain an audio file.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the audio message.
 */
fun EventRoute<MessageEvent>.audio(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.audio != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers an audio handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.audio(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { audio(handler) }

/**
 * Registers a handler for messages that contain a document.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the document message.
 */
fun EventRoute<MessageEvent>.document(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.document != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a document handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.document(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { document(handler) }

/**
 * Registers a handler for messages that contain a sticker.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the sticker message.
 */
fun EventRoute<MessageEvent>.sticker(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.sticker != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a sticker handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.sticker(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { sticker(handler) }

/**
 * Registers a handler for messages that contain a voice note.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the voice message.
 */
fun EventRoute<MessageEvent>.voice(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.voice != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a voice handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.voice(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { voice(handler) }

/**
 * Registers a handler for messages that contain a video note (rounded video).
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the video note message.
 */
fun EventRoute<MessageEvent>.videoNote(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.videoNote != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a video note handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.videoNote(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { videoNote(handler) }

/**
 * Registers a handler for messages that contain an animation (GIF).
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the animation message.
 */
fun EventRoute<MessageEvent>.animation(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.animation != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers an animation handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.animation(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { animation(handler) }

/**
 * Registers a handler for messages that contain a contact.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the contact message.
 */
fun EventRoute<MessageEvent>.contact(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.contact != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a contact handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.contact(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { contact(handler) }

/**
 * Registers a handler for messages that contain a location.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the location message.
 */
fun EventRoute<MessageEvent>.location(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.location != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a location handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.location(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { location(handler) }

/**
 * Registers a handler for messages that contain a venue.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the venue message.
 */
fun EventRoute<MessageEvent>.venue(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.venue != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a venue handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.venue(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { venue(handler) }

/**
 * Registers a handler for messages that contain a poll.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the poll message.
 */
fun EventRoute<MessageEvent>.poll(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.poll != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a poll handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.poll(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { poll(handler) }

/**
 * Registers a handler for messages that contain a dice.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the dice message.
 */
fun EventRoute<MessageEvent>.dice(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.dice != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a dice handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.dice(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { dice(handler) }

/**
 * Registers a handler for messages from private chats only.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the private chat message.
 */
fun EventRoute<MessageEvent>.privateChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.chat.type == ChatType.PRIVATE) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a private chat handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.privateChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { privateChat(handler) }

/**
 * Registers a handler for messages from group chats only.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the group chat message.
 */
fun EventRoute<MessageEvent>.groupChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.chat.type == ChatType.GROUP) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a group chat handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.groupChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { groupChat(handler) }

/**
 * Registers a handler for messages from supergroup chats only.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the supergroup chat message.
 */
fun EventRoute<MessageEvent>.supergroupChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.chat.type == ChatType.SUPERGROUP) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a supergroup chat handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.supergroupChat(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { supergroupChat(handler) }

/**
 * Registers a handler for messages from channels only.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the channel message.
 */
fun EventRoute<MessageEvent>.channel(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.chat.type == ChatType.CHANNEL) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a channel handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.channel(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { channel(handler) }

/**
 * Registers a handler for messages that are replies to another message.
 *
 * @param handler A suspending function with [CoroutineScope] receiver that handles the reply message.
 */
fun EventRoute<MessageEvent>.reply(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.replyToMessage != null) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a reply handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.reply(
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { reply(handler) }

/**
 * Registers a handler for messages that are replies to a specific message.
 *
 * @param messageId The ID of the message that this message is replying to.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the reply message.
 */
fun EventRoute<MessageEvent>.replyTo(
    messageId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) {
    select({ if (it.event.message.replyToMessage?.messageId == messageId) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a replyTo handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.replyTo(
    messageId: Long,
    handler: suspend CoroutineScope.(TelegramBotEventContext<MessageEvent>) -> Unit
) = on<MessageEvent> { replyTo(messageId, handler) }

/**
 * Registers a handler for callback queries with specific data.
 *
 * @param data The callback data string to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching callback.
 */
fun EventRoute<CallbackQueryEvent>.callbackData(
    data: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<CallbackQueryEvent>) -> Unit
) {
    select({ if (it.event.callbackQuery.data == data) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a callback data handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.callbackData(
    data: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<CallbackQueryEvent>) -> Unit
) = on<CallbackQueryEvent> { callbackData(data, handler) }

/**
 * Registers a handler for callback queries whose data matches a regular expression.
 *
 * @param pattern The regular expression pattern to match against callback data.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching callback.
 */
fun EventRoute<CallbackQueryEvent>.callbackDataRegex(
    pattern: Regex,
    handler: suspend CoroutineScope.(TelegramBotEventContext<CallbackQueryEvent>) -> Unit
) {
    select({ if (it.event.callbackQuery.data?.matches(pattern) == true) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers a callback data regex handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.callbackDataRegex(
    pattern: Regex,
    handler: suspend CoroutineScope.(TelegramBotEventContext<CallbackQueryEvent>) -> Unit
) = on<CallbackQueryEvent> { callbackDataRegex(pattern, handler) }

/**
 * Registers a handler for inline queries with specific query text.
 *
 * @param query The query text to match.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching inline query.
 */
fun EventRoute<InlineQueryEvent>.inlineQuery(
    query: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) {
    select({ if (it.event.inlineQuery.query == query) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers an inline query handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.inlineQuery(
    query: String,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) = on<InlineQueryEvent> { inlineQuery(query, handler) }

/**
 * Registers a handler for inline queries whose text matches a regular expression.
 *
 * @param pattern The regular expression pattern to match against query text.
 * @param handler A suspending function with [CoroutineScope] receiver that handles the matching inline query.
 */
fun EventRoute<InlineQueryEvent>.inlineQueryRegex(
    pattern: Regex,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) {
    select({ if (it.event.inlineQuery.query.matches(pattern)) it else null }) {
        handle(handler)
    }
}

/**
 * Convenience extension that registers an inline query regex handler directly at the root level.
 */
fun EventRoute<TelegramBotEvent>.inlineQueryRegex(
    pattern: Regex,
    handler: suspend CoroutineScope.(TelegramBotEventContext<InlineQueryEvent>) -> Unit
) = on<InlineQueryEvent> { inlineQueryRegex(pattern, handler) }
