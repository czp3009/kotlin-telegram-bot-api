package com.hiczp.telegram.bot.application.dispatcher.handler.command

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
 * command("greet", ::GreetArgs) { ... }
 * command("greet", { GreetArgs() }) { ... }
 *
 * // Incorrect: reuses the same instance, causing state pollution
 * val sharedArgs = GreetArgs()
 * command("greet", { sharedArgs }) { ... }
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

    private var alreadyParsed = false

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
     * Note: This method can only be called once per instance. If called multiple times,
     * an [IllegalStateException] will be thrown. This is to prevent accidental reuse
     * of [BotArguments] instances, which are stateful.
     *
     * @param arguments The positional arguments to parse (typically from [TelegramBotCommandContext.unconsumedArguments]).
     * @param commandPath The command path for error messages.
     * @param context The command context for error responses, or null if not available.
     * @throws CommandParseException if parsing or validation fails.
     * @throws IllegalStateException if [parse] has already been called on this instance.
     */
    fun parse(arguments: List<String>, commandPath: String, context: TelegramBotCommandContext? = null) {
        check(!alreadyParsed) {
            "${::parse.name}() can only be called once per instance, please create a new instance for each command invocation"
        }
        alreadyParsed = true
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
    operator fun provideDelegate(
        thisRef: BotArguments,
        property: kotlin.reflect.KProperty<*>
    ): kotlin.properties.ReadOnlyProperty<BotArguments, T> {
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
        return kotlin.properties.ReadOnlyProperty { ref, _ -> ref.parsedValues[property.name] as T }
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
