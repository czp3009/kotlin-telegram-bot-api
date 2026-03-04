package com.hiczp.telegram.bot.application.dispatcher.handler.command

import com.hiczp.telegram.bot.application.context.action.replyMessage
import com.hiczp.telegram.bot.application.dispatcher.handler.CommandBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.DefaultCommandBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.DefaultStructuredCommandBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.RouteNode
import com.hiczp.telegram.bot.application.dispatcher.handler.StructuredCommandBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.TelegramBotDsl
import kotlinx.coroutines.coroutineScope

/**
 * Build-phase route constructor for command DSL.
 *
 * This class is isolated from [HandlerRoute] by the [TelegramBotDsl] annotation,
 * ensuring that users cannot accidentally call handler matchers like `onText`
 * inside a command block.
 *
 * Inside a [CommandRoute], you can only:
 * - Register a handler with [handle]
 * - Create subcommands with [subCommand] or [subCommandEndpoint]
 *
 * Example:
 * ```kotlin
 * command("admin") {
 *     // `this` is CommandRoute
 *     handle { /* Show admin help */ }
 *
 *     subCommand("user") {
 *         subCommandEndpoint("add", ::AddUserArgs) { ctx ->
 *             // Handle /admin user add <name>
 *         }
 *     }
 * }
 * ```
 */
@TelegramBotDsl
class CommandRoute(internal val node: RouteNode) {
    /**
     * Registers a handler for this command route.
     *
     * The handler receives a [CommandBotCall] with access to the event,
     * client, command path, and unconsumed arguments.
     *
     * Example:
     * ```kotlin
     * command("admin") {
     *     handle {
     *         // `this` is CommandBotCall
     *         replyMessage("Admin commands: user, status")
     *     }
     * }
     * ```
     *
     * @param handler The handler function with [CommandBotCall] receiver.
     */
    fun handle(handler: suspend CommandBotCall.() -> Unit) {
        node.handler = { context ->
            coroutineScope {
                val cmdContext = context as TelegramBotCommandContext
                val call = DefaultCommandBotCall(cmdContext, this)
                handler(call)
            }
        }
    }

    /**
     * Creates a child route selected by a custom selector function.
     *
     * The selector returns a new context if the route matches, or null otherwise.
     * This enables custom routing logic beyond simple subcommand matching.
     *
     * @param selector A function that receives the current context and returns a modified context
     *   if the route matches, or null if it doesn't.
     * @param build A lambda to configure the child [CommandRoute].
     */
    fun select(
        selector: suspend (TelegramBotCommandContext) -> TelegramBotCommandContext?,
        build: CommandRoute.() -> Unit
    ) {
        val childNode = RouteNode { context ->
            @Suppress("UNCHECKED_CAST")
            selector(context as TelegramBotCommandContext)
        }
        node.children.add(childNode)
        CommandRoute(childNode).build()
    }

    /**
     * Executes a command endpoint with typed arguments.
     *
     * This function bridges unstructured command routing with typed argument parsing:
     * 1. Creates a [BotArguments] instance via [argumentsFactory].
     * 2. Parses [TelegramBotCommandContext.unconsumedArguments] against the schema.
     * 3. Creates a [StructuredCommandBotCall] with the parsed arguments.
     * 4. Invokes the handler with the structured context.
     *
     * When parsing fails, the [onError] handler is invoked with the [CommandParseException].
     * By default, a formatted help message is sent to the user via [replyMessage].
     *
     * Example with custom error handler:
     * ```kotlin
     * command("ban", ::BanArgs, onError = { e ->
     *     replyMessage("Error: ${e.message}")
     * }) { ... }
     * ```
     *
     * Example using default behavior:
     * ```kotlin
     * command("ban", ::BanArgs) { ... }
     * ```
     *
     * @param A The type of [BotArguments].
     * @param argumentsFactory A function that creates a new [BotArguments] instance.
     * @param onError An error handler invoked when argument parsing fails.
     *        Defaults to sending a formatted help message via [replyMessage].
     * @param handler The handler to invoke with the structured context.
     */
    internal fun <A : BotArguments> executeEndpoint(
        argumentsFactory: () -> A,
        onError: suspend CommandBotCall.(CommandParseException) -> Unit = { replyMessage(text = it.generateHelpText()).getOrThrow() },
        handler: suspend StructuredCommandBotCall<A>.() -> Unit
    ) {
        handle {
            val arguments = argumentsFactory()
            try {
                arguments.parse(unconsumedArguments, commandPath, this)
            } catch (e: InvalidArgumentValueException) {
                val parseException = CommandParseException(
                    context = this,
                    commandPath = commandPath,
                    commandDescription = arguments.commandDescription,
                    schema = arguments.schema,
                    argumentName = e.argumentName,
                    message = e.message.ifBlank { null } ?: "Invalid argument",
                    cause = e
                )
                onError(parseException)
                return@handle  // Don't invoke handler on error
            } catch (e: CommandParseException) {
                onError(e)
                return@handle  // Don't invoke handler on error
            }

            val structuredContext = DefaultTelegramBotStructuredCommandContext(this, arguments)
            DefaultStructuredCommandBotCall(structuredContext, this@handle).handler()
        }
    }
}
