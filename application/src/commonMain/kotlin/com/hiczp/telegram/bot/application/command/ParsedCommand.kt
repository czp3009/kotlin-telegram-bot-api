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
 * If the parsed command has a username, it will only match if [username] is provided and matches,
 * or if [username] is null (meaning any bot username is acceptable).
 * If the parsed command does not have a username, it always matches regardless of [username].
 *
 * @param command The expected command name (without leading `/`), case-insensitive.
 * @param username The expected bot username, or null to accept any username. Case-insensitive.
 * @return `true` if the command and username (if applicable) match, `false` otherwise.
 */
fun ParsedCommand.matchesCommand(command: String, username: String? = null): Boolean {
    val parsedCommand = this
    val commandMatched = parsedCommand.command.equals(command, ignoreCase = true)
    val usernameMatched = if (parsedCommand.username == null) {
        //no username in command
        true
    } else {
        if (username == null) {
            //don't need to check username
            true
        } else {
            parsedCommand.username.equals(username, ignoreCase = true)
        }
    }
    return commandMatched && usernameMatched
}

fun MessageEvent.matchesCommand(command: String, username: String? = null): Boolean {
    val parsedCommand = CommandParser.parse(this.message.text) ?: return false
    return parsedCommand.matchesCommand(command, username)
}

fun BusinessMessageEvent.matchesCommand(command: String, username: String? = null): Boolean {
    val parsedCommand = CommandParser.parse(this.businessMessage.text) ?: return false
    return parsedCommand.matchesCommand(command, username)
}
