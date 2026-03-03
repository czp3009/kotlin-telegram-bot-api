package com.hiczp.telegram.bot.sample.basic

import com.hiczp.telegram.bot.application.TelegramBotApplication
import com.hiczp.telegram.bot.application.context.action.replyMessage
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerTelegramEventDispatcher
import com.hiczp.telegram.bot.application.dispatcher.handler.command.*
import com.hiczp.telegram.bot.application.dispatcher.handler.handling
import com.hiczp.telegram.bot.application.interceptor.builtin.logging.loggingInterceptor

// Enum for calc command - must be defined at top level (not local)
private enum class Operation {
    ADD, SUB, MUL, DIV;

    override fun toString() = when (this) {
        ADD -> "+"
        SUB -> "-"
        MUL -> "*"
        DIV -> "/"
    }
}

// Argument classes for typed commands - defined at top level
private class EchoArgs : BotArguments("Echo a message back to the chat") {
    val message: String by requireArgument("The message to echo")
    val count: Int by optionalArgument("Number of times to repeat", default = 1)
}

private class BanArgs : BotArguments("Ban a user from the chat") {
    val userId: Long by requireArgument("User ID to ban")
    val reason: String? by optionalArgument("Ban reason")
}

private class CalcArgs : BotArguments("Perform a calculation") {
    val a: Double by requireArgument<Double>("First number")
    val operation: Operation by enumArgument("Operation to perform (ADD, SUB, MUL, DIV)")
    val b: Double by requireArgument("Second number")
}

private class UserInfoArgs : BotArguments("Show user information") {
    val userId: Long by requireArgument("User ID to look up")
}

/**
 * CommandBot demonstrates various command handling features:
 * - Simple command with [commandEndpoint]
 * - Command with typed arguments using [BotArguments]
 * - Nested subcommands
 * - Automatic help generation on argument errors
 */
private suspend fun runCommandBot(botToken: String) {
    // Admin user IDs for access control (replace with actual admin user IDs)
    val adminUserIds = setOf(196664407L)

    val eventDispatcher = HandlerTelegramEventDispatcher(handling {
        // Simple command without arguments
        commandEndpoint("start") {
            replyMessage(
                """
                Welcome to CommandBot!
                
                Available commands:
                /start - Show this help message
                /echo <message> [count] - Echo a message
                /ping - Check if bot is alive
                /calc <a> <op> <b> - Calculate (op: ADD, SUB, MUL, DIV)
                /admin - Admin commands (restricted)
                """.trimIndent()
            )
        }

        // Simple command
        commandEndpoint("ping") {
            replyMessage("Pong!")
        }

        // Command with typed arguments
        command("echo", ::EchoArgs, sendHelpOnError = true) {
            repeat(arguments.count) {
                replyMessage(arguments.message)
            }
        }

        // Command with enum argument
        command("calc", ::CalcArgs, sendHelpOnError = true) {
            val result = when (arguments.operation) {
                Operation.ADD -> arguments.a + arguments.b
                Operation.SUB -> arguments.a - arguments.b
                Operation.MUL -> arguments.a * arguments.b
                Operation.DIV -> arguments.a / arguments.b
            }
            replyMessage("${arguments.a} ${arguments.operation} ${arguments.b} = $result")
        }

        // Nested subcommands with access control in handler
        command("admin") {
            // Default handler for /admin with access control
            handle {
                if (event.message.from?.id !in adminUserIds) {
                    replyMessage("Unauthorized: Admin access required.")
                    return@handle
                }
                replyMessage(
                    """
                    Admin Commands:
                    /admin status - Show system status
                    /admin ban <userId> [reason] - Ban a user
                    /admin user list - List users
                    /admin user info <userId> - Show user info
                    """.trimIndent()
                )
            }

            // Simple subcommand with access control
            subCommandEndpoint("status") {
                if (event.message.from?.id !in adminUserIds) {
                    replyMessage("Unauthorized: Admin access required.")
                    return@subCommandEndpoint
                }
                replyMessage("System status: OK\nUptime: 1234 seconds\nMemory: 256MB used")
            }

            // Subcommand with typed arguments and access control
            subCommand("ban", ::BanArgs, sendHelpOnError = true) {
                if (event.message.from?.id !in adminUserIds) {
                    replyMessage("Unauthorized: Admin access required.")
                    return@subCommand
                }
                val reason = arguments.reason ?: "No reason provided"
                replyMessage("Banned user ${arguments.userId}. Reason: $reason")
                // In real bot, you would call: client.banChatMember(chatId, arguments.userId)
            }

            // Nested subcommands (two levels deep)
            subCommand("user") {
                handle {
                    if (event.message.from?.id !in adminUserIds) {
                        replyMessage("Unauthorized: Admin access required.")
                        return@handle
                    }
                    replyMessage("User subcommands: list, info")
                }

                subCommandEndpoint("list") {
                    if (event.message.from?.id !in adminUserIds) {
                        replyMessage("Unauthorized: Admin access required.")
                        return@subCommandEndpoint
                    }
                    replyMessage("Users:\n- User1 (ID: 123)\n- User2 (ID: 456)\n- User3 (ID: 789)")
                }

                subCommand("info", ::UserInfoArgs, sendHelpOnError = true) {
                    if (event.message.from?.id !in adminUserIds) {
                        replyMessage("Unauthorized: Admin access required.")
                        return@subCommand
                    }
                    replyMessage("User ${arguments.userId}: Status=Active, Joined=2024-01-01")
                }
            }
        }

        // Dead letter handler for unhandled events
        handle {
            println("Unhandled event: $event")
        }
    })

    val app = TelegramBotApplication.longPolling(
        botToken = botToken,
        eventDispatcher = eventDispatcher,
        interceptors = listOf(loggingInterceptor())
    )

    println("Starting command bot...")
    println("Send /start to see available commands.")
    app.start()
    app.join()
}

suspend fun main(args: Array<String>) {
    val botToken = args.firstOrNull() ?: error("Bot token is required")
    runCommandBot(botToken)
}
