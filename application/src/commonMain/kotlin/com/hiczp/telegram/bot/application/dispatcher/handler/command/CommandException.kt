package com.hiczp.telegram.bot.application.dispatcher.handler.command

/**
 * Thrown when an argument value fails validation or transformation.
 *
 * This exception is caught by [CommandRoute.executeEndpoint] and converted to a help message.
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
) : Exception(message, cause) {
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
    fun generateHelpText() = buildString {
        append("❌ ")
        if (argumentName != null) {
            append("Parameter '$argumentName'").append(" - ")
        }
        append(message)
        append("\n\n")

        append("📝 Usage:\n")
        append("/").append(commandPath)
        schema.forEach { argument ->
            if (argument.isOptional) {
                append(" [").append(argument.name).append("]")
            } else {
                append(" <").append(argument.name).append(">")
            }
        }
        append("\n\n")

        if (commandDescription.isNotBlank()) {
            append(commandDescription).append("\n\n")
        }

        if (schema.isNotEmpty()) {
            append("⚙️ Parameters:\n")
            schema.forEach { argument ->
                val isErrorArg = argument.name == argumentName
                if (isErrorArg) append("  ⚠️ ") else append("  ")
                append(argument.name).append(" (").append(argument.typeName).append(")")
                if (argument.description.isNotBlank()) {
                    append(" - ").append(argument.description)
                }
                if (argument.isOptional && argument.defaultValue != null) {
                    append(" (Default: ").append(argument.defaultValue).append(")")
                } else if (argument.isOptional) {
                    append(" (Optional)")
                }
                append("\n")
            }
        }
    }.trimEnd()
}
