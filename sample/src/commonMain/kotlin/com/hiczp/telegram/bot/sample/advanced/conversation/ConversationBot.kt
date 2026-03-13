/**
 * Conversation Bot Sample
 *
 * A Telegram bot demonstrating multi-turn conversation support using the conversation DSL.
 * This sample shows how to create interactive flows like surveys, wizards, and quizzes.
 *
 * ## Usage
 *
 * Run with bot token as the command line argument:
 * ```
 * ./gradlew :sample:jvmRun --args="YOUR_BOT_TOKEN" -DmainClass="com.hiczp.telegram.bot.sample.advanced.conversation.ConversationBotKt" --quiet
 * ```
 *
 * ## Features Demonstrated
 *
 * - Installing [conversationInterceptor] for conversation support
 * - Starting conversations with [startConversation]
 * - Using [reply] for convenient messaging within conversations
 * - Awaiting user input with `awaitText()`, `awaitMessage()`, and `awaitCommand()`
 * - Handling conversation timeout with `onTimeout`
 * - Handling user cancellation with `onCancel`
 * - Custom [ConversationId] scoping (per-user, per-chat)
 *
 * ## Available Commands
 *
 * - `/start` - Show help message
 * - `/survey` - Start a multi-question survey
 * - `/quiz` - Start a simple quiz game
 * - `/register` - Start a registration wizard
 *
 * @see conversationInterceptor
 * @see startConversation
 * @see ConversationScope
 * @see reply
 */
package com.hiczp.telegram.bot.sample.advanced.conversation

import com.hiczp.telegram.bot.application.TelegramBotApplication
import com.hiczp.telegram.bot.application.context.action.replyMessage
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerTelegramEventDispatcher
import com.hiczp.telegram.bot.application.dispatcher.handler.command.commandEndpoint
import com.hiczp.telegram.bot.application.dispatcher.handler.handling
import com.hiczp.telegram.bot.application.dispatcher.handler.matcher.onMessageEventPrivateChat
import com.hiczp.telegram.bot.application.interceptor.builtin.conversation.*
import com.hiczp.telegram.bot.application.interceptor.builtin.logging.loggingInterceptor
import com.hiczp.telegram.bot.protocol.constant.ParseMode
import com.hiczp.telegram.bot.protocol.extension.inlineKeyboard
import com.hiczp.telegram.bot.protocol.model.answerCallbackQuery
import com.hiczp.telegram.bot.protocol.model.sendMessage
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

/**
 * ConversationBot demonstrates multi-turn conversation capabilities:
 * - Survey: Collect multiple pieces of information in sequence
 * - Quiz: Interactive question-and-answer flow
 * - Registration: Wizard-style form with validation
 */
private suspend fun runConversationBot(botToken: String) {
    val eventDispatcher = HandlerTelegramEventDispatcher(handling {
        onMessageEventPrivateChat {
            // Help command
            commandEndpoint("start") {
                replyMessage(
                    """
                    Welcome to ConversationBot!

                    This bot demonstrates multi-turn conversation support.

                    **Commands:**
                    /start - Show this help
                    /survey - Start a survey (name, age, favorite color)
                    /quiz - Start a quiz game (3 questions)
                    /register - Start a registration wizard

                    **During conversations:**
                    - Type `/cancel` to cancel any conversation
                    - Conversations timeout after 2 minutes of inactivity
                    """.trimIndent(),
                    parseMode = ParseMode.MARKDOWN,
                )
            }

            // Survey conversation - collect multiple pieces of information
            commandEndpoint("survey") {
                startConversation(
                    timeout = 2.minutes,
                    onTimeout = {
                        replyMessage("Survey timed out. Please start again with /survey")
                    },
                    onCancel = {
                        replyMessage("Survey cancelled. Start again anytime with /survey")
                    }
                ) {
                    reply("Let's start a quick survey!")

                    // Question 1: Name
                    send("What is your name?")
                    val name = awaitText()

                    // Question 2: Age
                    send("Nice to meet you, $name! How old are you?")
                    val ageString = awaitText()
                    val age = ageString.toIntOrNull()
                    if (age == null || age < 0 || age > 150) {
                        reply("That doesn't seem like a valid age. Cancel the survey.")
                        return@startConversation
                    }

                    // Question 3: Favorite color
                    send("What is your favorite color?")
                    val color = awaitText()

                    // Summary
                    send(
                        """
                        Thanks for completing the survey!

                        **Your responses:**
                        - Name: $name
                        - Age: $age
                        - Favorite color: $color
                        """.trimIndent()
                    )
                }
            }

            // Quiz conversation - interactive quiz with inline keyboard buttons
            commandEndpoint("quiz") {
                startConversation(
                    timeout = 2.minutes,
                    onTimeout = {
                        replyMessage("Quiz timed out! Try again with /quiz")
                    },
                    onCancel = {
                        replyMessage("Quiz cancelled. Try again anytime with /quiz")
                    }
                ) {
                    var score = 0

                    reply("Welcome to the Quiz! Answer 3 questions by clicking the buttons.\n")

                    // Question 1
                    client.sendMessage(
                        chatId = id.chatId.toString(),
                        text = """
                            **Question 1:** What is the capital of France?
                        """.trimIndent(),
                        replyMarkup = inlineKeyboard {
                            row {
                                callbackButton("A) London", "quiz_q1_a")
                                callbackButton("B) Paris", "quiz_q1_b")
                                callbackButton("C) Berlin", "quiz_q1_c")
                            }
                        }
                    )
                    val answer1 = awaitCallbackQuery()
                    launch { client.answerCallbackQuery(answer1.callbackQuery.id) }
                    if (answer1.callbackQuery.data == "quiz_q1_b") {
                        score++
                        reply("Correct! ✓")
                    } else {
                        reply("Wrong! The answer is Paris.")
                    }

                    // Question 2
                    client.sendMessage(
                        chatId = id.chatId.toString(),
                        text = """
                            **Question 2:** What is 2 + 2?
                        """.trimIndent(),
                        replyMarkup = inlineKeyboard {
                            row {
                                callbackButton("A) 3", "quiz_q2_a")
                                callbackButton("B) 5", "quiz_q2_b")
                                callbackButton("C) 4", "quiz_q2_c")
                            }
                        }
                    )
                    val answer2 = awaitCallbackQuery()
                    launch { client.answerCallbackQuery(answer2.callbackQuery.id) }
                    if (answer2.callbackQuery.data == "quiz_q2_c") {
                        score++
                        reply("Correct! ✓")
                    } else {
                        reply("Wrong! The answer is 4.")
                    }

                    // Question 3
                    client.sendMessage(
                        chatId = id.chatId.toString(),
                        text = """
                            **Question 3:** Which planet is known as the Red Planet?
                        """.trimIndent(),
                        replyMarkup = inlineKeyboard {
                            row {
                                callbackButton("A) Venus", "quiz_q3_a")
                                callbackButton("B) Mars", "quiz_q3_b")
                                callbackButton("C) Jupiter", "quiz_q3_c")
                            }
                        }
                    )
                    val answer3 = awaitCallbackQuery()
                    launch { client.answerCallbackQuery(answer3.callbackQuery.id) }
                    if (answer3.callbackQuery.data == "quiz_q3_b") {
                        score++
                        reply("Correct! ✓")
                    } else {
                        reply("Wrong! The answer is Mars.")
                    }

                    // Final score
                    val emoji = when (score) {
                        3 -> "🎉"
                        2 -> "👍"
                        1 -> "😅"
                        else -> "😢"
                    }
                    send(
                        """
                        **Quiz Complete!**

                        Your score: $score/3 $emoji

                        ${if (score == 3) "Perfect score! You're a genius!" else "Better luck next time!"}
                        """.trimIndent()
                    )
                }
            }

            // Registration wizard - form with validation and retry
            commandEndpoint("register") {
                startConversation(
                    timeout = 3.minutes,
                    onTimeout = {
                        replyMessage("Registration timed out. Please start again with /register")
                    },
                    onCancel = {
                        replyMessage("Registration cancelled. Start again anytime with /register")
                    }
                ) {
                    reply("Let's create your account!")

                    // Username with validation
                    var username: String
                    while (true) {
                        send("Choose a username (3-20 characters, letters and numbers only):")
                        username = awaitText().trim()
                        if (username.length in 3..20 && username.all { it.isLetterOrDigit() }) {
                            break
                        }
                        reply("Invalid username. Please use 3-20 characters (letters and numbers only).")
                    }

                    // Email with basic validation
                    var email: String
                    while (true) {
                        send("Enter your email address:")
                        email = awaitText().trim()
                        if (email.contains("@") && email.contains(".")) {
                            break
                        }
                        reply("Invalid email format. Please enter a valid email address.")
                    }

                    // Confirmation
                    send(
                        """
                        **Please confirm your registration:**

                        Username: $username
                        Email: $email

                        Type "confirm" to complete registration, or "cancel" to abort.
                        """.trimIndent()
                    )

                    val confirmation = awaitText().trim().lowercase()
                    if (confirmation == "confirm") {
                        reply(
                            """
                            ✅ **Registration Successful!**

                            Welcome, $username!
                            Your account has been created.
                            """.trimIndent()
                        )
                    } else {
                        reply("Registration cancelled. No worries, you can register anytime with /register")
                    }
                }
            }

            // Echo for non-command messages during idle state
            handle {
                val text = event.message.text
                if (text != null && !text.startsWith("/")) {
                    replyMessage("I received: $text\n\nStart a conversation with /survey, /quiz, or /register!")
                }
            }
        }
    })

    // Important: conversationInterceptor must be installed for startConversation to work
    val app = TelegramBotApplication.longPolling(
        botToken = botToken,
        eventDispatcher = eventDispatcher,
        interceptors = listOf(
            loggingInterceptor(),
            conversationInterceptor(),  // Required for conversation support
        )
    )

    println("Starting conversation bot...")
    println("Send /start to see available commands.")
    app.start()
    app.join()
}

suspend fun main(args: Array<String>) {
    val botToken = args.firstOrNull() ?: error("Bot token is required")
    runConversationBot(botToken)
}
