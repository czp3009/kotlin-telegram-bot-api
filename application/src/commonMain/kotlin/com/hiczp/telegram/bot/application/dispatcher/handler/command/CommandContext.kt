package com.hiczp.telegram.bot.application.dispatcher.handler.command

import com.hiczp.telegram.bot.application.command.ParsedCommand
import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.protocol.event.MessageEvent

/**
 * Context for command execution, providing access to the parsed command,
 * the current command path, and remaining unconsumed positional arguments.
 *
 * The [unconsumedArguments] list contains positional arguments that can be further consumed
 * by subcommands or parsed by [BotArguments].
 *
 * @see TelegramBotStructuredCommandContext for typed argument access
 */
interface TelegramBotCommandContext : TelegramBotEventContext<MessageEvent> {
    /** The original parsed command from the message. */
    val parsedCommand: ParsedCommand

    /** The full command path, e.g., "admin" for root or "admin user" for nested subcommands. */
    val commandPath: String

    /** Positional arguments not yet consumed by subcommands. Consumed by [BotArguments.parse]. */
    val unconsumedArguments: List<String>
}

/**
 * Default implementation of [TelegramBotCommandContext].
 */
class DefaultTelegramBotCommandContext(
    private val delegate: TelegramBotEventContext<MessageEvent>,
    override val parsedCommand: ParsedCommand,
    override val commandPath: String,
    override val unconsumedArguments: List<String>
) : TelegramBotCommandContext, TelegramBotEventContext<MessageEvent> by delegate

/**
 * Context for structured command execution with typed arguments.
 *
 * Combines [TelegramBotCommandContext] with a typed [arguments] object,
 * providing access to both the execution context and parsed parameter values.
 *
 * @param A The type of [BotArguments] containing the parsed values.
 * @see BotArguments
 */
interface TelegramBotStructuredCommandContext<out A : BotArguments> : TelegramBotCommandContext {
    /** The typed arguments object containing parsed parameter values. */
    val arguments: A
}

/**
 * Default implementation of [TelegramBotStructuredCommandContext].
 */
class DefaultTelegramBotStructuredCommandContext<out A : BotArguments>(
    private val delegate: TelegramBotCommandContext,
    override val arguments: A
) : TelegramBotStructuredCommandContext<A>, TelegramBotCommandContext by delegate
