package com.hiczp.telegram.bot.application.test.dispatcher.handler

import com.hiczp.telegram.bot.application.context.ProvidedUserTelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.command.*
import com.hiczp.telegram.bot.application.dispatcher.handler.handling
import com.hiczp.telegram.bot.client.TelegramBotClient
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.model.Chat
import com.hiczp.telegram.bot.protocol.model.Message
import com.hiczp.telegram.bot.protocol.model.User
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Clock

class CommandDslTest {
    private val botUser = User(id = 1L, isBot = true, firstName = "Bot", username = "test_bot")
    private val user = User(id = 2L, isBot = false, firstName = "User")
    private val client = TelegramBotClient(botToken = "xxx")
    private val applicationScope = TestScope()

    private fun context(text: String): ProvidedUserTelegramBotEventContext<MessageEvent> =
        ProvidedUserTelegramBotEventContext(
            client = client,
            event = MessageEvent(
                updateId = 1L,
                message = Message(
                    messageId = 1L,
                    date = Clock.System.now().epochSeconds,
                    chat = Chat(id = 100L, type = "private"),
                    from = user,
                    text = text
                )
            ),
            applicationScope = applicationScope,
            botUser = botUser
        )

    class NameArgs : BotArguments("Name command") {
        val name: String by requireArgument("Name")
        val count: Int by optionalArgument("Count", default = 1)
    }

    @Test
    fun `bot arguments should parse required and default values`() {
        val args = NameArgs()
        args.parse(listOf("Alice"), "greet")

        assertEquals("Alice", args.name)
        assertEquals(1, args.count)
    }

    @Test
    fun `root command should match and expose command context`() = runTest {
        val invoked = mutableListOf<String>()
        val route = handling {
            command("ping") {
                handle { invoked.add(commandPath) }
            }
        }

        assertTrue(route.execute(context("/ping@test_bot")))
        assertEquals(listOf("ping"), invoked)
    }

    @Test
    fun `root command should reject other bot username`() = runTest {
        val invoked = mutableListOf<String>()
        val route = handling {
            command("ping") {
                handle { invoked.add("ping") }
            }
        }

        assertFalse(route.execute(context("/ping@other_bot")))
        assertTrue(invoked.isEmpty())
    }

    @Test
    fun `nested subcommand should consume arguments`() = runTest {
        val invoked = mutableListOf<String>()
        val route = handling {
            command("admin") {
                subCommand("user") {
                    subCommand("list") {
                        handle {
                            invoked.add("$commandPath:${unconsumedArguments.joinToString(",")}")
                        }
                    }
                }
            }
        }

        assertTrue(route.execute(context("/admin user list extra")))
        assertEquals(listOf("admin user list:extra"), invoked)
    }

    @Test
    fun `parent command handle should run when children miss`() = runTest {
        val invoked = mutableListOf<String>()
        val route = handling {
            command("tool") {
                subCommand("run") {
                    handle { invoked.add("run") }
                }
                handle { invoked.add("tool") }
            }
        }

        assertTrue(route.execute(context("/tool")))
        assertEquals(listOf("tool"), invoked)
    }

    @Test
    fun `typed command should parse arguments before handle`() = runTest {
        val invoked = mutableListOf<String>()
        val route = handling {
            command("greet", ::NameArgs) {
                handle {
                    repeat(arguments.count) {
                        invoked.add(arguments.name)
                    }
                }
            }
        }

        assertTrue(route.execute(context("/greet Alice 2")))
        assertEquals(listOf("Alice", "Alice"), invoked)
    }

    @Test
    fun `typed command parse error should be handled and consumed`() = runTest {
        val invoked = mutableListOf<String>()
        val route = handling {
            command(
                name = "greet",
                argumentsFactory = ::NameArgs,
                onError = { error: CommandParseException -> invoked.add("error:${error.argumentName}") }
            ) {
                handle { invoked.add(arguments.name) }
            }
        }

        assertTrue(route.execute(context("/greet")))
        assertEquals(listOf("error:name"), invoked)
    }

    @Test
    fun `first matching command should short circuit`() = runTest {
        val invoked = mutableListOf<String>()
        val route = handling {
            command("test") {
                handle { invoked.add("first") }
            }
            command("test") {
                handle { invoked.add("second") }
            }
        }

        assertTrue(route.execute(context("/test")))
        assertEquals(listOf("first"), invoked)
    }
}
