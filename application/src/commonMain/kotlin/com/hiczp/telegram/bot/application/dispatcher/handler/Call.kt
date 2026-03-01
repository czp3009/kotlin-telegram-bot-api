package com.hiczp.telegram.bot.application.dispatcher.handler

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.command.BotArguments
import com.hiczp.telegram.bot.application.dispatcher.handler.command.TelegramBotCommandContext
import com.hiczp.telegram.bot.application.dispatcher.handler.command.TelegramBotStructuredCommandContext
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlinx.coroutines.CoroutineScope

/**
 * DSL marker annotation for the Telegram bot event routing DSL.
 *
 * This annotation prevents implicit receiver access across different DSL scopes,
 * ensuring type-safe builder patterns when constructing event routes.
 */
@DslMarker
annotation class TelegramBotDsl

/**
 * Execution-phase context for handler invocation.
 *
 * This interface combines [TelegramBotEventContext] with [CoroutineScope],
 * providing handlers with access to event data, the Telegram API client,
 * and structured concurrency capabilities.
 *
 * Handlers receive this context as their receiver, enabling direct access to
 * `event`, `client`, `attributes`, and coroutine builders like `launch`.
 *
 * Example:
 * ```kotlin
 * onText("ping") {
 *     // `this` is HandlerBotCall<MessageEvent>
 *     client.sendMessage(event.message.chat.id.toString(), "pong")
 *
 *     // Can launch concurrent operations
 *     launch {
 *         someAsyncOperation()
 *     }
 * }
 * ```
 *
 * @param T The type of Telegram event being processed.
 */
@TelegramBotDsl
interface HandlerBotCall<out T : TelegramBotEvent> : TelegramBotEventContext<T>, CoroutineScope

/**
 * Default implementation of [HandlerBotCall].
 *
 * @param T The type of Telegram event being processed.
 * @param delegate The underlying event context.
 * @param scope The coroutine scope for structured concurrency.
 */
class DefaultHandlerBotCall<out T : TelegramBotEvent>(
    private val delegate: TelegramBotEventContext<T>,
    private val scope: CoroutineScope
) : HandlerBotCall<T>, TelegramBotEventContext<T> by delegate, CoroutineScope by scope

/**
 * Execution-phase context for command handlers.
 *
 * Extends [HandlerBotCall] with command-specific properties like parsed arguments
 * and command path. This context is provided to command handlers registered via
 * [command] or [commandEndpoint].
 *
 * Example:
 * ```kotlin
 * command("admin") {
 *     // `this` is CommandBotCall
 *     println("Command path: $commandPath")
 *     println("Arguments: $unconsumedArguments")
 * }
 * ```
 */
@TelegramBotDsl
interface CommandBotCall : HandlerBotCall<MessageEvent>, TelegramBotCommandContext

/**
 * Default implementation of [CommandBotCall].
 */
class DefaultCommandBotCall(
    private val delegate: TelegramBotCommandContext,
    private val scope: CoroutineScope
) : CommandBotCall, TelegramBotCommandContext by delegate, CoroutineScope by scope

/**
 * Execution-phase context for structured command handlers with typed arguments.
 *
 * Combines [CommandBotCall] with typed [arguments] access, providing type-safe
 * parameter retrieval for commands that use [BotArguments].
 *
 * Example:
 * ```kotlin
 * class BanArgs : BotArguments("Ban a user") {
 *     val username: String by requireArgument("User to ban")
 * }
 *
 * command("ban", ::BanArgs) {
 *     // `this` is StructuredCommandBotCall<BanArgs>
 *     val user = arguments.username
 *     replyMessage("Banned user: $user")
 * }
 * ```
 *
 * @param A The type of [BotArguments] containing parsed values.
 */
@TelegramBotDsl
interface StructuredCommandBotCall<out A : BotArguments> : CommandBotCall, TelegramBotStructuredCommandContext<A>

/**
 * Default implementation of [StructuredCommandBotCall].
 */
class DefaultStructuredCommandBotCall<out A : BotArguments>(
    private val delegate: TelegramBotStructuredCommandContext<A>,
    private val scope: CoroutineScope
) : StructuredCommandBotCall<A>, TelegramBotStructuredCommandContext<A> by delegate, CoroutineScope by scope
