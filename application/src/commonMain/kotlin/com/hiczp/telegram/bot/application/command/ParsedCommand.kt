package com.hiczp.telegram.bot.application.command

import com.hiczp.telegram.bot.protocol.event.BusinessMessageEvent
import com.hiczp.telegram.bot.protocol.event.MessageEvent

/**
 * Represents a parsed Telegram bot command.
 *
 * Telegram commands follow the format `/command[@username] [args]`, for example:
 * - `/start` - A simple command
 * - `/help@mybot` - A command addressed to a specific bot
 * - `/search hello world` - A command with arguments
 *
 * @property text The original text that was parsed.
 * @property command The command name without the leading `/` (e.g., "start", "help").
 * @property username The bot username if the command was addressed to a specific bot (e.g., "mybot" from `/help@mybot`), or null if not specified.
 * @property rawArgs The raw argument string after the command, or null if no arguments were provided.
 * @property args The list of whitespace-separated arguments parsed from [rawArgs]. Empty if no arguments.
 */
data class ParsedCommand(
    val text: String,
    val command: String,
    val username: String?,
    val rawArgs: String?,
    val args: List<String>,
)

/**
 * Checks if this parsed command matches the expected command and optional bot username.
 *
 * The comparison is case-insensitive for both command and username.
 * - If [username] is `null`, only the command name is checked.
 * - If [username] is provided and the parsed command has a username, they must match.
 * - If [username] is provided but the parsed command has no username, it always matches.
 *
 * @param command The expected command name (without leading `/`), case-insensitive.
 * @param username The expected bot username, or `null` to skip username validation. Case-insensitive.
 * @return `true` if the command and username (if applicable) match, `false` otherwise.
 */
fun ParsedCommand.matchesCommand(command: String, username: String? = null): Boolean {
    val commandMatched = this.command.equals(command, ignoreCase = true)
    val usernameMatched = if (username == null) {
        true
    } else {
        this.usernameMatched(username)
    }
    return commandMatched && usernameMatched
}

/**
 * Checks if the bot username in this parsed command matches the expected username.
 *
 * This function handles two scenarios:
 * 1. If the command was sent without a username (e.g., `/start`), it always returns `true`
 *    because the command is addressed to any bot in the chat.
 * 2. If the command includes a username (e.g., `/start@mybot`), it performs
 *    a case-insensitive comparison with the provided [username].
 *
 * @param username The expected bot username to match against.
 * @return `true` if the username matches or the command has no username, `false` otherwise.
 */
fun ParsedCommand.usernameMatched(username: String): Boolean =
    if (this.username == null) {
        //no username in command
        true
    } else {
        this.username.equals(username, ignoreCase = true)
    }

fun MessageEvent.parseCommand(): ParsedCommand? = CommandParser.parse(this.message.text)

fun MessageEvent.matchesCommand(command: String, username: String? = null): Boolean {
    val parsedCommand = this.parseCommand() ?: return false
    return parsedCommand.matchesCommand(command, username)
}

fun BusinessMessageEvent.parseCommand(): ParsedCommand? = CommandParser.parse(this.businessMessage.text)

fun BusinessMessageEvent.matchesCommand(command: String, username: String? = null): Boolean {
    val parsedCommand = this.parseCommand() ?: return false
    return parsedCommand.matchesCommand(command, username)
}
