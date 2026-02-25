package com.hiczp.telegram.bot.application.command

import com.hiczp.telegram.bot.protocol.event.BusinessMessageEvent
import com.hiczp.telegram.bot.protocol.event.MessageEvent

data class ParsedCommand(
    val text: String,
    val command: String,
    val username: String?,
    val rawArgs: String?,
    val args: List<String>,
)

fun ParsedCommand.matchesCommand(command: String, username: String? = null): Boolean {
    val parsedCommand = this
    val commandMatch = parsedCommand.command.equals(command, ignoreCase = true)
    val usernameMatch = if (username == null) {
        true
    } else {
        parsedCommand.username.equals(username, ignoreCase = true)
    }
    return commandMatch && usernameMatch
}

fun MessageEvent.matchesCommand(command: String, username: String? = null): Boolean {
    val parsedCommand = CommandPaser.parse(this.message.text) ?: return false
    return parsedCommand.matchesCommand(command, username)
}

fun BusinessMessageEvent.matchesCommand(command: String, username: String? = null): Boolean {
    val parsedCommand = CommandPaser.parse(this.businessMessage.text) ?: return false
    return parsedCommand.matchesCommand(command, username)
}
