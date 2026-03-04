/**
 * Command Bot Sample
 *
 * A Telegram bot demonstrating advanced command handling features including typed arguments,
 * subcommands, and access control. This sample uses [HandlerTelegramEventDispatcher] with
 * the handler DSL for type-safe routing.
 *
 * ## Usage
 *
 * Run with bot token as command line argument:
 * ```
 * ./gradlew :sample:basic:jvmRun --args="YOUR_BOT_TOKEN" -DmainClass="com.hiczp.telegram.bot.sample.basic.CommandBotKt" --quiet
 * ```
 *
 * ## Features Demonstrated
 *
 * - Simple command handling with [commandEndpoint]
 * - Typed command arguments using [BotArguments]
 * - Enum argument parsing with [enumArgument]
 * - Nested subcommands with [subCommand] and [subCommandEndpoint]
 * - Access control with custom DSL ([requireAuth])
 * - Request logging via [loggingInterceptor]
 *
 * ## Available Commands
 *
 * - `/start` - Show help message
 * - `/ping` - Health check
 * - `/echo <message> [count]` - Echo a message (with optional repeat count)
 * - `/calc <a> <op> <b>` - Calculator (op: ADD, SUB, MUL, DIV)
 * - `/admin` - Admin commands (restricted access)
 *
 * @see HandlerTelegramEventDispatcher
 * @see handling
 * @see command
 * @see commandEndpoint
 */
package com.hiczp.telegram.bot.sample.command

import com.hiczp.telegram.bot.application.TelegramBotApplication
import com.hiczp.telegram.bot.application.context.action.replyMessage
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerTelegramEventDispatcher
import com.hiczp.telegram.bot.application.dispatcher.handler.command.*
import com.hiczp.telegram.bot.application.dispatcher.handler.handling
import com.hiczp.telegram.bot.application.interceptor.builtin.logging.loggingInterceptor
import com.hiczp.telegram.bot.sample.dsl.MockAuthService
import com.hiczp.telegram.bot.sample.dsl.requireAuth

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
 * - Access control with [requireAuth]
 */
private suspend fun runCommandBot(botToken: String) {
    // Mock authentication service (in a real app, this would be a database/API service)
    val authService = MockAuthService()

    // Performance note: The handler uses short-circuit evaluation for efficiency.
    // Each request stops at the first matching handler without traversing the entire tree.
    // This means the framework cannot provide a command tree at runtime.
    //
    // If you need to dynamically list available subcommands at each level (e.g., for
    // interactive help or autocomplete), you should maintain a separate command tree
    // structure outside the handler DSL, then generate handlers from it programmatically.
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

        // Command with typed arguments (using default error handling - sends help message on error)
        command("echo", ::EchoArgs) {
            repeat(arguments.count) {
                replyMessage(arguments.message)
            }
        }

        // Command with enum argument (using default error handling)
        command("calc", ::CalcArgs) {
            val result = when (arguments.operation) {
                Operation.ADD -> arguments.a + arguments.b
                Operation.SUB -> arguments.a - arguments.b
                Operation.MUL -> arguments.a * arguments.b
                Operation.DIV -> arguments.a / arguments.b
            }
            replyMessage("${arguments.a} ${arguments.operation} ${arguments.b} = $result")
        }

        // Admin commands with access control using requireAuth DSL
        // Use onCommand to scope auth checks to command messages only
        // This prevents requireAuth from rejecting non-command text messages
        onCommand {
            requireAuth(
                authService = authService,
                onRejected = { replyMessage("Unauthorized: Admin access required") }
            ) {
                command("admin") {
                    handle {
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

                    subCommandEndpoint("status") {
                        replyMessage(
                            """
                            System status: OK
                            Uptime: 1234 seconds
                            Memory: 256MB used
                            """.trimIndent()
                        )
                    }

                    // Subcommand with typed arguments (using default error handling)
                    subCommand("ban", ::BanArgs) {
                        val reason = arguments.reason ?: "No reason provided"
                        replyMessage("Banned user ${arguments.userId}. Reason: $reason")
                    }

                    subCommand("user") {
                        handle {
                            replyMessage("User subcommands: list, info")
                        }

                        subCommandEndpoint("list") {
                            replyMessage(
                                """
                                Users:
                                - User1 (ID: 123)
                                - User2 (ID: 456)
                                - User3 (ID: 789)
                                """.trimIndent()
                            )
                        }

                        // Subcommand with typed arguments (using default error handling)
                        subCommand("info", ::UserInfoArgs) {
                            replyMessage("User ${arguments.userId}: Status=Active, Joined=2024-01-01")
                        }
                    }
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
