@file:Suppress("unused")

package com.hiczp.telegram.bot.application.dispatcher.handler.command

import com.hiczp.telegram.bot.application.command.matchesCommand
import com.hiczp.telegram.bot.application.command.parseCommand
import com.hiczp.telegram.bot.application.command.usernameMatched
import com.hiczp.telegram.bot.application.dispatcher.handler.CommandBotCall
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerRoute
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import kotlin.jvm.JvmName

/**
 * Enters a branch for command messages addressed to this bot.
 */
fun HandlerRoute<MessageEvent>.command(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter(
        predicate = {
            val parsedCommand = event.parseCommand() ?: return@filter false
            val username = me().username ?: return@filter true
            parsedCommand.usernameMatched(username)
        },
        build = build
    )

@JvmName("commandScopeOnEvent")
fun HandlerRoute<TelegramBotEvent>.command(build: HandlerRoute<MessageEvent>.() -> Unit) =
    filter<MessageEvent> { command(build) }

/**
 * Enters a branch for a root command with [name].
 */
fun HandlerRoute<MessageEvent>.command(name: String, build: CommandRoute.() -> Unit) {
    filterContext(
        selector = {
            val parsed = event.parseCommand()
            if (parsed != null && parsed.matchesCommand(name, me().username)) {
                DefaultTelegramBotCommandContext(
                    delegate = context,
                    parsedCommand = parsed,
                    commandPath = name,
                    unconsumedArguments = parsed.args
                )
            } else {
                null
            }
        },
        build = { CommandRoute(node).build() }
    )
}

@JvmName("namedCommandOnEvent")
fun HandlerRoute<TelegramBotEvent>.command(name: String, build: CommandRoute.() -> Unit) =
    filter<MessageEvent> { command(name, build) }

/**
 * Enters a typed root command branch. Call [StructuredCommandRoute.handle] inside [build]
 * to parse arguments and consume the event.
 */
fun <A : BotArguments> HandlerRoute<MessageEvent>.command(
    name: String,
    argumentsFactory: () -> A,
    onError: suspend CommandBotCall.(CommandParseException) -> Unit = defaultCommandErrorHandler(),
    build: StructuredCommandRoute<A>.() -> Unit
) {
    command(name) {
        StructuredCommandRoute(node, argumentsFactory, onError).build()
    }
}

@JvmName("typedCommandOnEvent")
fun <A : BotArguments> HandlerRoute<TelegramBotEvent>.command(
    name: String,
    argumentsFactory: () -> A,
    onError: suspend CommandBotCall.(CommandParseException) -> Unit = defaultCommandErrorHandler(),
    build: StructuredCommandRoute<A>.() -> Unit
) = filter<MessageEvent> { command(name, argumentsFactory, onError, build) }

/**
 * Enters a nested subcommand branch.
 */
fun CommandRoute.subCommand(name: String, build: CommandRoute.() -> Unit) {
    filterContext(
        selector = {
            if (unconsumedArguments.firstOrNull()?.equals(name, ignoreCase = true) == true) {
                DefaultTelegramBotCommandContext(
                    delegate = this,
                    parsedCommand = parsedCommand,
                    commandPath = "$commandPath $name",
                    unconsumedArguments = unconsumedArguments.drop(1)
                )
            } else {
                null
            }
        },
        build = build
    )
}

/**
 * Enters a typed nested subcommand branch. Call [StructuredCommandRoute.handle] inside [build].
 */
fun <A : BotArguments> CommandRoute.subCommand(
    name: String,
    argumentsFactory: () -> A,
    onError: suspend CommandBotCall.(CommandParseException) -> Unit = defaultCommandErrorHandler(),
    build: StructuredCommandRoute<A>.() -> Unit
) {
    subCommand(name) {
        StructuredCommandRoute(node, argumentsFactory, onError).build()
    }
}
