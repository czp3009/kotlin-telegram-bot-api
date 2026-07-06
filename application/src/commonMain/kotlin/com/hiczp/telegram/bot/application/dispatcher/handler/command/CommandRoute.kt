package com.hiczp.telegram.bot.application.dispatcher.handler.command

import com.hiczp.telegram.bot.application.context.action.replyMessage
import com.hiczp.telegram.bot.application.dispatcher.handler.*
import kotlinx.coroutines.coroutineScope

/**
 * Build-time route constructor for command branches.
 *
 * Route blocks run when the dispatcher is built. Runtime command work belongs in [handle].
 */
@TelegramBotDsl
class CommandRoute(internal val node: RouteNode) {
    fun handle(handler: suspend CommandBotCall.() -> Unit) {
        node.handler = { context ->
            coroutineScope {
                val commandContext = context as TelegramBotCommandContext
                handler(DefaultCommandBotCall(commandContext, this))
            }
        }
    }

    internal fun filterContext(
        selector: suspend TelegramBotCommandContext.() -> TelegramBotCommandContext?,
        build: CommandRoute.() -> Unit
    ) {
        val childNode = RouteNode { context ->
            (context as TelegramBotCommandContext).selector()
        }
        node.children.add(childNode)
        CommandRoute(childNode).build()
    }
}

/**
 * Build-time route constructor for command branches with typed arguments.
 */
@TelegramBotDsl
class StructuredCommandRoute<A : BotArguments> internal constructor(
    internal val node: RouteNode,
    private val argumentsFactory: () -> A,
    private val onError: suspend CommandBotCall.(CommandParseException) -> Unit,
) {
    fun handle(handler: suspend StructuredCommandBotCall<A>.() -> Unit) {
        node.handler = { context ->
            coroutineScope {
                val commandContext = context as TelegramBotCommandContext
                val commandCall = DefaultCommandBotCall(commandContext, this)
                val arguments = argumentsFactory()

                try {
                    arguments.parse(commandContext.unconsumedArguments, commandContext.commandPath, commandCall)
                } catch (e: InvalidArgumentValueException) {
                    commandCall.onError(
                        CommandParseException(
                            context = commandCall,
                            commandPath = commandContext.commandPath,
                            commandDescription = arguments.commandDescription,
                            schema = arguments.schema,
                            argumentName = e.argumentName,
                            message = e.message.ifBlank { null } ?: "Invalid argument",
                            cause = e
                        )
                    )
                    return@coroutineScope
                } catch (e: CommandParseException) {
                    commandCall.onError(e)
                    return@coroutineScope
                }

                val structuredContext = DefaultTelegramBotStructuredCommandContext(commandContext, arguments)
                handler(DefaultStructuredCommandBotCall(structuredContext, this))
            }
        }
    }
}

internal fun defaultCommandErrorHandler(): suspend CommandBotCall.(CommandParseException) -> Unit = {
    replyMessage(text = it.generateHelpText()).getOrThrow()
}
