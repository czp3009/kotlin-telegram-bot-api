package com.hiczp.telegram.bot.application.dispatcher.handler.command

import com.hiczp.telegram.bot.application.command.matchesCommand
import com.hiczp.telegram.bot.application.command.parseCommand
import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.CommandBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.application.dispatcher.handler.RouteNode
import com.hiczp.telegram.bot.application.dispatcher.handler.StructuredCommandBotCall
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlin.jvm.JvmName

// ==================== Root Command Registration ====================

/**
 * Registers a root command with routing capabilities.
 *
 * Use this overload when you need subcommands or want to use [CommandRoute.handle]
 * for parameterless commands. For simple commands without subcommands, prefer [commandEndpoint].
 *
 * The command matches messages starting with `/name` or `/name@bot_username`.
 *
 * Example:
 * ```kotlin
 * command("admin") {
 *     handle { /* Show admin help */ }
 *     subCommandEndpoint("stats") { /* ... */ }
 *     subCommandEndpoint("reload") { /* ... */ }
 * }
 * ```
 *
 * @param name The command name (without leading `/`).
 * @param build A lambda to configure the [CommandRoute].
 */
fun HandlerRoute<MessageEvent>.command(name: String, build: CommandRoute.() -> Unit) {
    val childNode = RouteNode { context ->
        @Suppress("UNCHECKED_CAST")
        val typedContext = context as TelegramBotEventContext<MessageEvent>
        val parsed = typedContext.event.parseCommand()
        if (parsed != null && parsed.matchesCommand(name, typedContext.me().username)) {
            DefaultTelegramBotCommandContext(typedContext, parsed, name, parsed.args)
        } else {
            null
        }
    }
    node.children.add(childNode)
    CommandRoute(childNode).build()
}

/**
 * Registers a root command on any event route, automatically narrowing to [MessageEvent].
 *
 * Convenience overload that wraps [HandlerRoute.on] and the [MessageEvent]-specific [command].
 *
 * @param name The command name (without leading `/`).
 * @param build A lambda to configure the [CommandRoute].
 */
@JvmName("commandOnEvent")
fun HandlerRoute<TelegramBotEvent>.command(name: String, build: CommandRoute.() -> Unit) =
    on<MessageEvent> { command(name, build) }

/**
 * Registers a root command with typed arguments (terminal mode).
 *
 * Use this overload for commands that take typed arguments without subcommands.
 * This is a convenience that combines [command] with [CommandRoute.executeEndpoint].
 *
 * Example:
 * ```kotlin
 * class EchoArgs : BotArguments("Echo a message") {
 *     val message: String by requireArgument("The message to echo")
 *     val count: Int by optionalArgument("Number of times", default = 1)
 * }
 *
 * onMessageEvent {
 *     command("echo", ::EchoArgs) {
 *         repeat(arguments.count) {
 *             client.sendMessage(event.message.chat.id, arguments.message)
 *         }
 *     }
 * }
 * ```
 *
 * @param A The type of [BotArguments].
 * @param name The command name (without leading `/`).
 * @param argumentsFactory A function that creates a new [BotArguments] instance for each invocation.
 *        Use a constructor reference (e.g., `::EchoArgs`) or a lambda (e.g., `{ EchoArgs() }`).
 *        Do not reuse the same instance across invocations.
 * @param sendHelpOnError Whether to automatically send a help message on parsing errors.
 *        If `false` (the default), throws [CommandParseException] instead, allowing custom
 *        error handling in interceptors or outer handlers.
 * @param handler The handler to invoke with the structured context.
 */
fun <A : BotArguments> HandlerRoute<MessageEvent>.command(
    name: String,
    argumentsFactory: () -> A,
    sendHelpOnError: Boolean = false,
    handler: suspend StructuredCommandBotCall<A>.() -> Unit
) = command(name, build = { executeEndpoint(argumentsFactory, sendHelpOnError, handler) })

/**
 * Registers a root command with typed arguments on any event route.
 *
 * Convenience overload that wraps [HandlerRoute.on] and the [MessageEvent]-specific [command].
 *
 * @param A The type of [BotArguments].
 * @param name The command name (without leading `/`).
 * @param argumentsFactory A function that creates a new [BotArguments] instance for each invocation.
 *        Use a constructor reference (e.g., `::EchoArgs`) or a lambda (e.g., `{ EchoArgs() }`).
 *        Do not reuse the same instance across invocations.
 * @param sendHelpOnError Whether to automatically send a help message on parsing errors.
 *        If `false` (the default), throws [CommandParseException] instead, allowing custom
 *        error handling in interceptors or outer handlers.
 * @param handler The handler to invoke with the structured context.
 */
@JvmName("commandWithArgsOnEvent")
fun <A : BotArguments> HandlerRoute<TelegramBotEvent>.command(
    name: String,
    argumentsFactory: () -> A,
    sendHelpOnError: Boolean = false,
    handler: suspend StructuredCommandBotCall<A>.() -> Unit
) = on<MessageEvent> { command(name, build = { executeEndpoint(argumentsFactory, sendHelpOnError, handler) }) }

/**
 * Registers a root command with a handler (terminal mode).
 *
 * Use this overload for simple commands that don't need subcommands.
 * The handler is invoked directly without needing an explicit [CommandRoute.handle] call.
 *
 * For commands with subcommands, use [command] instead:
 * ```kotlin
 * // Simple command - use commandEndpoint
 * commandEndpoint("ping") {
 *     replyMessage("Pong!")
 * }
 *
 * // Command with subcommands - use command
 * command("admin") {
 *     handle { /* Show admin help */ }
 *     subCommandEndpoint("status") { /* ... */ }
 * }
 * ```
 *
 * @param name The command name (without leading `/`).
 * @param handler The handler to invoke.
 */
fun HandlerRoute<MessageEvent>.commandEndpoint(
    name: String,
    handler: suspend CommandBotCall.() -> Unit
) = command(name) { handle(handler) }

/**
 * Registers a root command with a handler on any event route (terminal mode).
 *
 * Convenience overload that wraps [HandlerRoute.on] and the [MessageEvent]-specific [commandEndpoint].
 *
 * @param name The command name (without leading `/`).
 * @param handler The handler to invoke.
 */
@JvmName("commandEndpointOnEvent")
fun HandlerRoute<TelegramBotEvent>.commandEndpoint(
    name: String,
    handler: suspend CommandBotCall.() -> Unit
) = on<MessageEvent> { commandEndpoint(name, handler) }

// ==================== Subcommand Registration ====================

/**
 * Registers a subcommand with routing capabilities.
 *
 * Use this overload when the subcommand has nested subcommands or uses [CommandRoute.handle].
 * For simple subcommands without further nesting, prefer [subCommandEndpoint].
 * The subcommand name is matched case-insensitively against the first unconsumed argument.
 *
 * Example:
 * ```kotlin
 * command("db") {
 *     subCommandEndpoint("migrate") { /* Run migrations */ }
 *     subCommand("status") {
 *         subCommandEndpoint("check", ::CheckArgs) { /* Check status */ }
 *     }
 * }
 * ```
 *
 * @param name The subcommand name.
 * @param build A lambda to configure the [CommandRoute].
 */
fun CommandRoute.subCommand(name: String, build: CommandRoute.() -> Unit) {
    select({ context ->
        if (context.unconsumedArguments.firstOrNull()?.equals(name, ignoreCase = true) == true) {
            DefaultTelegramBotCommandContext(
                context,
                context.parsedCommand,
                "${context.commandPath} $name",
                context.unconsumedArguments.drop(1)
            )
        } else {
            null
        }
    }, build)
}

/**
 * Registers a subcommand with typed arguments (terminal mode).
 *
 * Use this overload for subcommands that take typed arguments without further nesting.
 * This is a convenience that combines [subCommand] with [CommandRoute.executeEndpoint].
 *
 * Example:
 * ```kotlin
 * class BanArgs : BotArguments("Ban a user") {
 *     val userId: Long by requireArgument("User ID to ban")
 *     val reason: String? by optionalArgument("Ban reason")
 * }
 *
 * command("mod") {
 *     subCommand("ban", ::BanArgs) {
 *         client.banChatMember(
 *             chatId = event.message.chat.id,
 *             userId = arguments.userId
 *         )
 *     }
 * }
 * ```
 *
 * @param A The type of [BotArguments].
 * @param name The subcommand name.
 * @param argumentsFactory A function that creates a new [BotArguments] instance for each invocation.
 *        Use a constructor reference (e.g., `::BanArgs`) or a lambda (e.g., `{ BanArgs() }`).
 * @param sendHelpOnError Whether to automatically send a help message on parsing errors.
 *        If `false` (the default), throws [CommandParseException] instead.
 * @param handler The handler to invoke with the structured context.
 */
fun <A : BotArguments> CommandRoute.subCommand(
    name: String,
    argumentsFactory: () -> A,
    sendHelpOnError: Boolean = false,
    handler: suspend StructuredCommandBotCall<A>.() -> Unit
) = subCommand(name) { executeEndpoint(argumentsFactory, sendHelpOnError, handler) }

/**
 * Registers a subcommand with a handler (terminal mode).
 *
 * Use this overload for simple subcommands that don't need further nesting.
 * The handler is invoked directly without needing an explicit [CommandRoute.handle] call.
 *
 * Example:
 * ```kotlin
 * command("admin") {
 *     subCommandEndpoint("status") {
 *         replyMessage("System status: OK")
 *     }
 * }
 * ```
 *
 * @param name The subcommand name.
 * @param handler The handler to invoke.
 */
fun CommandRoute.subCommandEndpoint(
    name: String,
    handler: suspend CommandBotCall.() -> Unit
) = subCommand(name) { handle(handler) }
