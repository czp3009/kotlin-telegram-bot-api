package com.hiczp.telegram.bot.application.dispatcher.handler

import com.hiczp.telegram.bot.application.command.ParsedCommand
import com.hiczp.telegram.bot.application.command.matchesCommand
import com.hiczp.telegram.bot.application.command.parseCommand
import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.context.action.replyMessage
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlinx.coroutines.CoroutineScope
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

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

/**
 * Thrown when an argument value fails validation or transformation.
 *
 * This exception is caught by [executeEndpoint] and converted to a help message.
 *
 * @property argumentName The name of the argument that failed validation, or null if not available.
 */
class InvalidArgumentValueException(
    override val message: String,
    val argumentName: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause)

/**
 * Thrown when command argument parsing fails, containing all information needed
 * to generate contextual help text or send an error response.
 *
 * @property context The command context, providing access to the client and event for sending responses.
 * @property commandPath The full command path (e.g., "admin user add").
 * @property commandDescription The description of the command from [BotArguments.commandDescription].
 * @property schema The list of [ArgumentDefinition]s describing expected parameters.
 * @property argumentName The name of the argument that caused the error, or null for general errors.
 */
class CommandParseException(
    val context: TelegramBotCommandContext?,
    val commandPath: String,
    val commandDescription: String,
    val schema: List<ArgumentDefinition<*>>,
    val argumentName: String?,
    override val message: String,
    cause: Throwable? = null,
) : Exception(message, cause)

/**
 * Context provided to argument validators.
 *
 * Provides the [fail] method for reporting validation errors, which throws [InvalidArgumentValueException].
 * Use [require] for simple condition checks with lazy error messages.
 *
 * Example:
 * ```kotlin
 * class UserArgs : BotArguments() {
 *     val age: Int by requireArgument<Int>()
 *         .validate { require(it >= 0) { "Age must be non-negative" } }
 * }
 * ```
 */
interface ArgumentTransformContext {
    /** The name of the argument being validated. */
    val name: String

    /**
     * Throws an [InvalidArgumentValueException] with the given message.
     */
    fun fail(message: String): Nothing {
        throw InvalidArgumentValueException(message, argumentName = name)
    }

    /**
     * Validates that [condition] is true, throwing [InvalidArgumentValueException] otherwise.
     *
     * @param condition The condition to check.
     * @param lazyMessage A lambda producing the error message if validation fails.
     */
    fun require(condition: Boolean, lazyMessage: () -> String = { "Invalid value" }) {
        if (!condition) {
            fail(lazyMessage())
        }
    }
}

/**
 * Implementation of [ArgumentTransformContext] for use during parsing.
 */
class DefaultArgumentTransformContext(override val name: String) : ArgumentTransformContext

/**
 * Definition of a command argument, including its metadata, parsing logic, and validators.
 *
 * Captures all information needed to parse a single positional argument and generate help text.
 *
 * @param T The type of the parsed value.
 * @property name The parameter name, derived from the property name via delegate.
 * @property description Help text for the parameter.
 * @property isOptional Whether the parameter is optional.
 * @property defaultValue The default value if [isOptional] is true and no value is provided.
 * @property typeName The display name of the type (e.g., "Int", "String") for help text.
 * @property parser A function that converts a raw string to type [T].
 * @property validators List of validation functions to run on parsed values.
 */
class ArgumentDefinition<T>(
    val name: String,
    val description: String,
    val isOptional: Boolean,
    val defaultValue: T?,
    val typeName: String,
    val parser: (String) -> T,
    val validators: List<ArgumentTransformContext.(T) -> Unit>
)

/**
 * Base class for defining typed command arguments using property delegates.
 *
 * Extend this class and define arguments as delegated properties:
 *
 * ```kotlin
 * class GreetArgs : BotArguments("Send a greeting to a user") {
 *     val name: String by requireArgument("The user to greet")
 *     val count: Int by optionalArgument("Number of greetings", default = 1)
 *         .validate { require(it > 0) { "Count must be positive" } }
 * }
 * ```
 *
 * Arguments are positional (not named like CLI options), matching Telegram's command syntax.
 * The order of the property declaration determines the expected argument order.
 *
 * **Important**: Instances of [BotArguments] are stateful and mutable during [parse].
 * Always create a new instance for each command invocation. Use a factory function
 * (e.g., `::GreetArgs` or `{ GreetArgs() }`) when registering commands:
 *
 * ```kotlin
 * // Correct: creates a new instance for each invocation
 * command("greet", ::GreetArgs) { ctx -> ... }
 * command("greet", { GreetArgs() }) { ctx -> ... }
 *
 * // Incorrect: reuses the same instance, causing state pollution
 * val sharedArgs = GreetArgs()
 * command("greet", { sharedArgs }) { ctx -> ... }
 * ```
 *
 * @property commandDescription A description of the command for help text generation.
 * @see requireArgument for required positional arguments
 * @see optionalArgument for optional positional arguments
 * @see enumArgument for enum arguments
 */
abstract class BotArguments(val commandDescription: String = "") {
    /**
     * The schema of argument definitions, populated automatically by delegate providers.
     * Order matches the order of property declaration.
     */
    val schema = mutableListOf<ArgumentDefinition<*>>()

    /**
     * Map of parsed values, keyed by property name.
     * Populated by [parse] and accessed via delegate property getters.
     */
    val parsedValues = mutableMapOf<String, Any?>()

    /**
     * Parses the given positional arguments against this schema.
     *
     * This method:
     * 1. Iterates through [schema] in declaration order
     * 2. Parses each argument using the registered [ArgumentDefinition.parser]
     * 3. Runs all validators on parsed values
     * 4. Applies defaults for missing optional arguments
     * 5. Throws [CommandParseException] on any error
     *
     * @param arguments The positional arguments to parse (typically from [TelegramBotCommandContext.unconsumedArguments]).
     * @param commandPath The command path for error messages.
     * @param context The command context for error responses, or null if not available.
     * @throws CommandParseException if parsing or validation fails.
     */
    fun parse(arguments: List<String>, commandPath: String, context: TelegramBotCommandContext? = null) {
        parsedValues.clear()
        var index = 0
        for (definition in schema) {
            val rawValue = arguments.getOrNull(index)
            if (rawValue == null) {
                if (definition.isOptional) {
                    parsedValues[definition.name] = definition.defaultValue
                    continue
                }
                throw CommandParseException(
                    context = context,
                    commandPath = commandPath,
                    commandDescription = commandDescription,
                    schema = schema,
                    argumentName = definition.name,
                    message = "Missing required parameter"
                )
            }

            @Suppress("UNCHECKED_CAST")
            val typedDefinition = definition as ArgumentDefinition<Any?>
            try {
                val parsed = typedDefinition.parser(rawValue)
                val transformContext = DefaultArgumentTransformContext(typedDefinition.name)
                typedDefinition.validators.forEach { validator ->
                    validator(transformContext, parsed)
                }
                parsedValues[typedDefinition.name] = parsed
                index++
            } catch (e: InvalidArgumentValueException) {
                throw InvalidArgumentValueException(
                    message = e.message,
                    argumentName = e.argumentName ?: typedDefinition.name,
                    cause = e
                )
            } catch (e: Exception) {
                throw CommandParseException(
                    context = context,
                    commandPath = commandPath,
                    commandDescription = commandDescription,
                    schema = schema,
                    argumentName = typedDefinition.name,
                    message = e.message ?: "Invalid format",
                    cause = e
                )
            }
        }

        if (index < arguments.size) {
            throw CommandParseException(
                context = context,
                commandPath = commandPath,
                commandDescription = commandDescription,
                schema = schema,
                argumentName = null,
                message = "Too many parameters. Maximum expected: ${schema.size}, received: ${arguments.size}."
            )
        }
    }
}

/**
 * Provider for argument definitions, enabling delegate property syntax.
 *
 * Supports chained configuration through [validate].
 *
 * Example:
 * ```kotlin
 * val port: Int by requireArgument<Int>("Server port")
 *     .validate { require(it in 1..65535) { "Port must be between 1 and 65535" } }
 * ```
 *
 * @param T The type of the argument value.
 * @property description Help text for the argument.
 * @property isOptional Whether the argument is optional.
 * @property defaultValue The default value for optional arguments.
 * @property typeName The display name of the type for help text.
 * @property parser Function to convert raw string to type [T].
 * @property validators Mutable list of validation functions.
 */
class ArgumentProvider<T>(
    val description: String,
    val isOptional: Boolean,
    val defaultValue: T?,
    val typeName: String,
    val parser: (String) -> T,
    val validators: MutableList<ArgumentTransformContext.(T) -> Unit> = mutableListOf()
) {
    /**
     * Adds a validation function to be called during parsing.
     *
     * The validator receives an [ArgumentTransformContext] receiver, providing access to
     * [ArgumentTransformContext.fail] and [ArgumentTransformContext.require].
     *
     * @param validator A validation function that receives the context and parsed value.
     * @return This provider for chaining.
     */
    fun validate(validator: ArgumentTransformContext.(T) -> Unit): ArgumentProvider<T> {
        validators.add(validator)
        return this
    }

    /**
     * Creates the delegate property that registers the argument definition and provides access to parsed values.
     *
     * Called automatically by Kotlin's property delegation mechanism.
     */
    operator fun provideDelegate(thisRef: BotArguments, property: KProperty<*>): ReadOnlyProperty<BotArguments, T> {
        val definition = ArgumentDefinition(
            name = property.name,
            description = description,
            isOptional = isOptional,
            defaultValue = defaultValue,
            typeName = typeName,
            parser = parser,
            validators = validators,
        )
        thisRef.schema.add(definition)
        @Suppress("UNCHECKED_CAST")
        return ReadOnlyProperty { ref, _ -> ref.parsedValues[property.name] as T }
    }
}

/**
 * Parses a raw string value into the specified reified type.
 *
 * Supports the following types:
 * - [String]: Returns the raw value unchanged.
 * - [Int]: Parses as integer, throws on invalid input.
 * - [Long]: Parses as long, throws on invalid input.
 * - [Double]: Parses as double, throws on invalid input.
 * - [Boolean]: Accepts "true"/"false", "yes"/"no", "on"/"off", "1"/"0" (case-insensitive).
 *
 * @param T The target type, which must be one of the supported types.
 * @param raw The raw string value to parse.
 * @return The parsed value of type [T].
 * @throws InvalidArgumentValueException if the value cannot be parsed or the type is unsupported.
 */
inline fun <reified T> parseValue(raw: String): T {
    return when (T::class) {
        String::class -> raw as T
        Int::class -> {
            val value = raw.toIntOrNull() ?: throw InvalidArgumentValueException("Must be an integer")
            value as T
        }

        Long::class -> {
            val value = raw.toLongOrNull() ?: throw InvalidArgumentValueException("Must be a number")
            value as T
        }

        Double::class -> {
            val value = raw.toDoubleOrNull() ?: throw InvalidArgumentValueException("Must be a decimal")
            value as T
        }

        Boolean::class -> when (raw.lowercase()) {
            "true", "1", "yes", "y", "on" -> true as T
            "false", "0", "no", "n", "off" -> false as T
            else -> throw InvalidArgumentValueException("Must be true/false or yes/no")
        }

        else -> throw InvalidArgumentValueException("Unsupported type: ${T::class.simpleName}. If this is an Enum, use enumArgument() instead.")
    }
}

/**
 * Defines a required positional argument.
 *
 * The argument value is parsed using [parseValue] and must be provided by the user.
 *
 * Example:
 * ```kotlin
 * class EchoArgs : BotArguments() {
 *     val message: String by requireArgument("The message to echo")
 * }
 * ```
 *
 * @param T The type of the argument. Must be supported by [parseValue].
 * @param description Help text for the argument.
 * @return An [ArgumentProvider] for property delegation.
 */
inline fun <reified T> BotArguments.requireArgument(description: String = ""): ArgumentProvider<T> {
    val parser: (String) -> T = { parseValue<T>(it) }
    return ArgumentProvider(description, false, null, T::class.simpleName ?: "Value", parser)
}

/**
 * Defines an optional positional argument without a default value.
 *
 * The property type should be nullable ([T]?). If the argument is not provided,
 * the value will be `null`.
 *
 * Example:
 * ```kotlin
 * class SearchArgs : BotArguments() {
 *     val query: String by requireArgument("Search query")
 *     val limit: Int? by optionalArgument("Maximum results")
 * }
 * ```
 *
 * @param T The type of the argument. Must be supported by [parseValue].
 * @param description Help text for the argument.
 * @return An [ArgumentProvider] for property delegation.
 */
inline fun <reified T> BotArguments.optionalArgument(description: String = ""): ArgumentProvider<T?> {
    val parser: (String) -> T? = { parseValue<T>(it) }
    return ArgumentProvider(description, true, null, T::class.simpleName ?: "Value", parser)
}

/**
 * Defines an optional positional argument with a default value.
 *
 * If the argument is not provided, [default] will be used.
 *
 * Example:
 * ```kotlin
 * class ListArgs : BotArguments() {
 *     val page: Int by optionalArgument("Page number", default = 1)
 * }
 * ```
 *
 * @param T The type of the argument. Must be supported by [parseValue].
 * @param description Help text for the argument.
 * @param default The default value if the argument is not provided.
 * @return An [ArgumentProvider] for property delegation.
 */
inline fun <reified T> BotArguments.optionalArgument(description: String = "", default: T): ArgumentProvider<T> {
    val parser: (String) -> T = { parseValue<T>(it) }
    return ArgumentProvider(description, true, default, T::class.simpleName ?: "Value", parser)
}

@PublishedApi
internal inline fun <reified T : Enum<T>> enumValueParser(): (String) -> T {
    return { raw ->
        try {
            enumValueOf<T>(raw.uppercase())
        } catch (e: Exception) {
            throw InvalidArgumentValueException(
                "Must be one of: " + enumValues<T>().joinToString { it.name },
                cause = e
            )
        }
    }
}

/**
 * Defines a required enum argument.
 *
 * Input is matched case-insensitively against enum constant names.
 * Uses Kotlin's [enumValueOf] for KMP compatibility without reflection.
 *
 * Example:
 * ```kotlin
 * enum class Color { RED, GREEN, BLUE }
 *
 * class ColorArgs : BotArguments() {
 *     val color: Color by enumArgument("The color to use")
 * }
 * ```
 *
 * @param T The enum type.
 * @param description Help text for the argument.
 * @return An [ArgumentProvider] for property delegation.
 */
inline fun <reified T : Enum<T>> BotArguments.enumArgument(description: String = ""): ArgumentProvider<T> {
    val parser = enumValueParser<T>()
    return ArgumentProvider(description, false, null, T::class.simpleName ?: "Enum", parser)
}

/**
 * Defines an optional enum argument without a default value.
 *
 * Input is matched case-insensitively. If not provided, the value will be `null`.
 *
 * @param T The enum type.
 * @param description Help text for the argument.
 * @return An [ArgumentProvider] for property delegation.
 * @see enumArgument
 */
@Suppress("unused")
inline fun <reified T : Enum<T>> BotArguments.optionalEnumArgument(description: String = ""): ArgumentProvider<T?> {
    val nonNullParser = enumValueParser<T>()
    val parser: (String) -> T? = { raw -> nonNullParser(raw) }
    return ArgumentProvider(description, true, null, T::class.simpleName ?: "Enum", parser)
}

/**
 * Defines an optional enum argument with a default value.
 *
 * Input is matched case-insensitively. If not provided, [default] will be used.
 *
 * @param T The enum type.
 * @param description Help text for the argument.
 * @param default The default value if the argument is not provided.
 * @return An [ArgumentProvider] for property delegation.
 * @see enumArgument
 */
@Suppress("unused")
inline fun <reified T : Enum<T>> BotArguments.optionalEnumArgument(
    description: String = "",
    default: T
): ArgumentProvider<T> {
    val parser = enumValueParser<T>()
    return ArgumentProvider(description, true, default, T::class.simpleName ?: "Enum", parser)
}

/**
 * DSL scope for defining command routes and handlers.
 *
 * Enables building nested command structures:
 * - Use [handle] to register a handler for the current command path.
 * - Use [subCommandEndpoint] for simple subcommands without further nesting.
 * - Use [subCommand] to create nested subcommands with multiple levels.
 * - Use [select] for custom routing logic.
 *
 * Example:
 * ```kotlin
 * handling {
 *     command("admin") {
 *         subCommandEndpoint("status") { ctx ->
 *             ctx.client.sendMessage(ctx.event.message.chat.id, "System OK")
 *         }
 *         subCommand("user") {
 *             subCommand("add", ::AddUserArgs) { ctx ->
 *                 // Handle /admin user add <name> <email>
 *             }
 *         }
 *     }
 * }
 * ```
 */
@TelegramBotDsl
class CommandRoute(private val node: RouteNode) {
    /**
     * Registers a handler for this command route.
     *
     * The handler receives a [TelegramBotCommandContext] with access to the event,
     * client, and unconsumed arguments.
     *
     * For simple commands without subcommands, prefer [commandEndpoint] instead:
     * ```kotlin
     * // Simple command - use commandEndpoint
     * commandEndpoint("ping") { ctx ->
     *     ctx.client.sendMessage(ctx.event.message.chat.id, "Pong!")
     * }
     *
     * // Command with subcommands - use command with handle
     * command("admin") {
     *     handle { ctx -> /* Show admin help */ }
     *     subCommandEndpoint("status") { /* ... */ }
     * }
     * ```
     */
    fun handle(handler: suspend CoroutineScope.(TelegramBotCommandContext) -> Unit) {
        node.handler = { context ->
            @Suppress("UNCHECKED_CAST")
            handler(context as TelegramBotCommandContext)
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
}

/**
 * Generates formatted help text for this parse exception.
 *
 * Produces a message with:
 * - Error indicator with the error message
 * - Usage line showing the command path and parameter syntax
 * - Command description (if provided)
 * - Parameter list with types, descriptions, and defaults
 *
 * Example output:
 * ```
 * ❌ Missing required parameter
 *
 * 📝 Usage:
 * /admin user add <name> <email> [role]
 *
 * Add a new user to the system
 *
 * ⚙️ Parameters:
 *   name (String) - The user's display name
 *   email (String) - The user's email address
 *   role (String) (Default: member) - The user's role
 * ```
 */
fun CommandParseException.generateHelpText(): String {
    val stringBuilder = StringBuilder().append("❌ ").append(message).append("\n\n")
    stringBuilder.append("📝 Usage:\n/").append(commandPath)
    schema.forEach { argument ->
        if (argument.isOptional) {
            stringBuilder.append(" [").append(argument.name).append("]")
        } else {
            stringBuilder.append(" <").append(argument.name).append(">")
        }
    }
    stringBuilder.append("\n\n")

    if (commandDescription.isNotBlank()) {
        stringBuilder.append(commandDescription).append("\n\n")
    }

    if (schema.isNotEmpty()) {
        stringBuilder.append("⚙️ Parameters:\n")
        schema.forEach { argument ->
            val isErrorArg = argument.name == argumentName
            val prefix = if (isErrorArg) "  ⚠️ " else "  "
            stringBuilder.append(prefix).append(argument.name).append(" (").append(argument.typeName)
                .append(")")
            if (argument.description.isNotBlank()) {
                stringBuilder.append(" - ").append(argument.description)
            }
            if (argument.isOptional && argument.defaultValue != null) {
                stringBuilder.append(" (Default: ").append(argument.defaultValue).append(")")
            } else if (argument.isOptional) {
                stringBuilder.append(" (Optional)")
            }
            stringBuilder.append("\n")
        }
    }
    return stringBuilder.toString().trimEnd()
}

private suspend fun TelegramBotCommandContext.sendHelpMessage(exception: CommandParseException) =
    replyMessage(text = exception.generateHelpText()).getOrThrow()

/**
 * Executes a command endpoint with typed arguments.
 *
 * This function bridges unstructured command routing with typed argument parsing:
 * 1. Creates a [BotArguments] instance via [argumentsFactory].
 * 2. Parses [TelegramBotCommandContext.unconsumedArguments] against the schema.
 * 3. Creates a [TelegramBotStructuredCommandContext] with the parsed arguments.
 * 4. Invokes the handler with the structured context.
 *
 * When parsing fails and [sendHelpOnError] is `true`, a help message is automatically
 * sent to the user. When `false` (the default), a [CommandParseException] is thrown
 * instead, allowing custom error handling in interceptors or outer handlers.
 *
 * @param A The type of [BotArguments].
 * @param argumentsFactory A function that creates a new [BotArguments] instance.
 * @param sendHelpOnError Whether to automatically send a help message on parsing errors.
 *        If `false`, throws [CommandParseException] instead.
 * @param handler The handler to invoke with the structured context. Receives [CoroutineScope]
 *        as receiver for structured concurrency within the dispatch lifecycle.
 */
private fun <A : BotArguments> CommandRoute.executeEndpoint(
    argumentsFactory: () -> A,
    sendHelpOnError: Boolean,
    handler: suspend CoroutineScope.(TelegramBotStructuredCommandContext<A>) -> Unit
) {
    handle { context ->
        val arguments = argumentsFactory()
        try {
            arguments.parse(context.unconsumedArguments, context.commandPath, context)
        } catch (e: InvalidArgumentValueException) {
            val parseException = CommandParseException(
                context = context,
                commandPath = context.commandPath,
                commandDescription = arguments.commandDescription,
                schema = arguments.schema,
                argumentName = e.argumentName,
                message = e.message.ifBlank { null } ?: "Invalid argument",
                cause = e
            )
            if (sendHelpOnError) {
                context.sendHelpMessage(parseException)
                return@handle
            } else {
                throw parseException
            }
        } catch (e: CommandParseException) {
            if (sendHelpOnError) {
                context.sendHelpMessage(e)
                return@handle
            } else {
                throw e
            }
        }

        val structuredContext = DefaultTelegramBotStructuredCommandContext(context, arguments)
        handler(structuredContext)
    }
}

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
 *     handle { ctx -> /* Show admin help */ }
 *     subCommandEndpoint("stats") { /* ... */ }
 *     subCommandEndpoint("reload") { /* ... */ }
 * }
 * ```
 *
 * @param name The command name (without leading `/`).
 * @param build A lambda to configure the [CommandRoute].
 */
fun EventRoute<MessageEvent>.command(name: String, build: CommandRoute.() -> Unit) {
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
    this.node.children.add(childNode)
    CommandRoute(childNode).build()
}

/**
 * Registers a root command on any event route, automatically narrowing to [MessageEvent].
 *
 * Convenience overload that wraps [EventRoute.on] and the [MessageEvent]-specific [command].
 *
 * @param name The command name (without leading `/`).
 * @param build A lambda to configure the [CommandRoute].
 */
fun EventRoute<TelegramBotEvent>.command(name: String, build: CommandRoute.() -> Unit) =
    on<MessageEvent> { command(name, build) }

/**
 * Registers a root command with typed arguments on any event route (terminal mode).
 *
 * Convenience overload that wraps [EventRoute.on] and the [MessageEvent]-specific [command].
 *
 * @param A The type of [BotArguments].
 * @param name The command name (without leading `/`).
 * @param argumentsFactory A function that creates a new [BotArguments] instance for each invocation.
 *        Use a constructor reference (e.g., `::EchoArgs`) or a lambda (e.g., `{ EchoArgs() }`).
 *        Do not reuse the same instance across invocations.
 * @param sendHelpOnError Whether to automatically send a help message on parsing errors.
 *        If `false` (the default), throws [CommandParseException] instead, allowing custom
 *        error handling in interceptors or outer handlers.
 * @param handler The handler to invoke with the structured context. Receives [CoroutineScope]
 *        as receiver for structured concurrency within the dispatch lifecycle.
 */
fun <A : BotArguments> EventRoute<TelegramBotEvent>.command(
    name: String,
    argumentsFactory: () -> A,
    sendHelpOnError: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotStructuredCommandContext<A>) -> Unit
) = on<MessageEvent> { command(name, build = { executeEndpoint(argumentsFactory, sendHelpOnError, handler) }) }

/**
 * Registers a root command with typed arguments (terminal mode).
 *
 * Use this overload for commands that take typed arguments without subcommands.
 * This is a convenience that combines [command] with [executeEndpoint].
 *
 * Example:
 * ```kotlin
 * class EchoArgs : BotArguments("Echo a message") {
 *     val message: String by requireArgument("The message to echo")
 *     val count: Int by optionalArgument("Number of times", default = 1)
 * }
 *
 * on<MessageEvent> {
 *     command("echo", ::EchoArgs) { ctx ->
 *         repeat(ctx.arguments.count) {
 *             ctx.client.sendMessage(ctx.event.message.chat.id, ctx.arguments.message)
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
 * @param handler The handler to invoke with the structured context. Receives [CoroutineScope]
 *        as receiver for structured concurrency within the dispatch lifecycle.
 */
fun <A : BotArguments> EventRoute<MessageEvent>.command(
    name: String,
    argumentsFactory: () -> A,
    sendHelpOnError: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotStructuredCommandContext<A>) -> Unit
) = command(name, build = { executeEndpoint(argumentsFactory, sendHelpOnError, handler) })

/**
 * Registers a root command with a handler (terminal mode).
 *
 * Use this overload for simple commands that don't need subcommands.
 * The handler is invoked directly without needing an explicit [CommandRoute.handle] call.
 *
 * For commands with subcommands, use [command] instead:
 * ```kotlin
 * // Simple command - use commandEndpoint
 * commandEndpoint("ping") { ctx ->
 *     ctx.client.sendMessage(ctx.event.message.chat.id, "Pong!")
 * }
 *
 * // Command with subcommands - use command
 * command("admin") {
 *     handle { ctx -> /* Show admin help */ }
 *     subCommandEndpoint("status") { /* ... */ }
 * }
 * ```
 *
 * @param name The command name (without leading `/`).
 * @param handler The handler to invoke. Receives [CoroutineScope] as receiver
 *        for structured concurrency within the dispatch lifecycle.
 */
fun EventRoute<MessageEvent>.commandEndpoint(
    name: String,
    handler: suspend CoroutineScope.(TelegramBotCommandContext) -> Unit
) {
    command(name) {
        handle(handler)
    }
}

/**
 * Registers a root command with a handler on any event route (terminal mode).
 *
 * Convenience overload that wraps [EventRoute.on] and the [MessageEvent]-specific [commandEndpoint].
 *
 * @param name The command name (without leading `/`).
 * @param handler The handler to invoke. Receives [CoroutineScope] as receiver
 *        for structured concurrency within the dispatch lifecycle.
 */
fun EventRoute<TelegramBotEvent>.commandEndpoint(
    name: String,
    handler: suspend CoroutineScope.(TelegramBotCommandContext) -> Unit
) = on<MessageEvent> { commandEndpoint(name, handler) }

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
 *         subCommandEndpoint("check", ::CheckArgs) { ctx -> /* Check status */ }
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
 * This is a convenience that combines [subCommand] with [executeEndpoint].
 *
 * Example:
 * ```kotlin
 * class BanArgs : BotArguments("Ban a user") {
 *     val userId: Long by requireArgument("User ID to ban")
 *     val reason: String? by optionalArgument("Ban reason")
 * }
 *
 * command("mod") {
 *     subCommand("ban", ::BanArgs) { ctx ->
 *         ctx.client.banChatMember(
 *             chatId = ctx.event.message.chat.id,
 *             userId = ctx.arguments.userId
 *         )
 *     }
 * }
 * ```
 *
 * @param A The type of [BotArguments].
 * @param name The subcommand name.
 * @param argumentsFactory A function that creates a new [BotArguments] instance for each invocation.
 *        Use a constructor reference (e.g., `::BanArgs`) or a lambda (e.g., `{ BanArgs() }`).
 *        Do not reuse the same instance across invocations.
 * @param sendHelpOnError Whether to automatically send a help message on parsing errors.
 *        If `false` (the default), throws [CommandParseException] instead, allowing custom
 *        error handling in interceptors or outer handlers.
 * @param handler The handler to invoke with the structured context. Receives [CoroutineScope]
 *        as receiver for structured concurrency within the dispatch lifecycle.
 */
fun <A : BotArguments> CommandRoute.subCommand(
    name: String,
    argumentsFactory: () -> A,
    sendHelpOnError: Boolean = false,
    handler: suspend CoroutineScope.(TelegramBotStructuredCommandContext<A>) -> Unit
) {
    subCommand(name, build = { executeEndpoint(argumentsFactory, sendHelpOnError, handler) })
}

/**
 * Registers a subcommand with a handler (terminal mode).
 *
 * Use this overload for simple subcommands that don't need further nesting.
 * The handler is invoked directly without needing an explicit [CommandRoute.handle] call.
 *
 * For subcommands with nested subcommands, use [subCommand] instead:
 * ```kotlin
 * command("admin") {
 *     // Simple subcommand - use subCommandEndpoint
 *     subCommandEndpoint("status") { ctx ->
 *         ctx.client.sendMessage(ctx.event.message.chat.id, "System OK")
 *     }
 *
 *     // Subcommand with nested subcommands - use subCommand
 *     subCommand("user") {
 *         subCommandEndpoint("list") { /* ... */ }
 *     }
 * }
 * ```
 *
 * @param name The subcommand name.
 * @param handler The handler to invoke. Receives [CoroutineScope] as receiver
 *        for structured concurrency within the dispatch lifecycle.
 */
fun CommandRoute.subCommandEndpoint(
    name: String,
    handler: suspend CoroutineScope.(TelegramBotCommandContext) -> Unit
) {
    subCommand(name) {
        handle(handler)
    }
}
