package com.hiczp.telegram.bot.application.test.dispatcher.handler

import com.hiczp.telegram.bot.application.context.ProvidedUserTelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.HandlerTelegramEventDispatcher
import com.hiczp.telegram.bot.application.dispatcher.handler.command.*
import com.hiczp.telegram.bot.application.dispatcher.handler.handling
import com.hiczp.telegram.bot.client.TelegramBotClient
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.model.Chat
import com.hiczp.telegram.bot.protocol.model.Message
import com.hiczp.telegram.bot.protocol.model.User
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.*
import kotlin.time.Clock

class StructuredCommandTest {
    private val testBotUser = User(
        id = 123456789L,
        isBot = true,
        firstName = "TestBot",
        username = "test_bot",
    )
    private val testChat = Chat(id = 100L, type = "private")
    private val testUser = User(id = 1L, isBot = false, firstName = "TestUser")

    private val mockClient = TelegramBotClient(botToken = "xxx")
    private val testScope = TestScope()

    private val invokedHandlers = mutableListOf<String>()
    private val capturedArguments = mutableListOf<BotArguments>()

    @BeforeTest
    fun resetTracking() {
        invokedHandlers.clear()
        capturedArguments.clear()
    }

    private fun createContext(text: String): ProvidedUserTelegramBotEventContext<MessageEvent> {
        return ProvidedUserTelegramBotEventContext(
            client = mockClient,
            event = MessageEvent(
                updateId = 1L,
                message = Message(
                    messageId = 1L,
                    date = Clock.System.now().epochSeconds,
                    chat = testChat,
                    from = testUser,
                    text = text
                )
            ),
            applicationScope = testScope,
            botUser = testBotUser,
        )
    }


    class SimpleArgs : BotArguments("A simple command") {
        val name: String by requireArgument("The name parameter")
    }

    class OptionalArgs : BotArguments("Command with optional params") {
        val required: String by requireArgument("Required parameter")
        val optional: String? by optionalArgument("Optional parameter")
    }

    class DefaultArgs : BotArguments("Command with defaults") {
        val name: String by requireArgument("Name")
        val count: Int by optionalArgument("Count", default = 1)
    }

    class ValidatedArgs : BotArguments("Command with validation") {
        val age: Int by requireArgument<Int>("Age")
            .validate { require(it >= 0) { "Age must be non-negative" } }
    }

    @Suppress("unused")
    enum class Color { RED, GREEN, BLUE }

    class EnumArgs : BotArguments("Command with enum") {
        val color: Color by enumArgument("Color choice")
    }

    class MultipleArgs : BotArguments("Command with multiple params") {
        val first: String by requireArgument("First parameter")
        val second: Int by requireArgument("Second parameter")
        val third: Double? by optionalArgument("Third parameter")
    }


    @Test
    fun `parse should populate required arguments`() {
        val args = SimpleArgs()
        args.parse(listOf("Alice"), "test")

        assertEquals("Alice", args.name)
    }

    @Test
    fun `parse should throw on missing required argument`() {
        val args = SimpleArgs()

        val exception = assertFailsWith<CommandParseException> {
            args.parse(emptyList(), "test")
        }

        assertEquals("Missing required parameter", exception.message)
        assertEquals("name", exception.argumentName)
    }

    @Test
    fun `parse should populate optional arguments when provided`() {
        val args = OptionalArgs()
        args.parse(listOf("required_value", "optional_value"), "test")

        assertEquals("required_value", args.required)
        assertEquals("optional_value", args.optional)
    }

    @Test
    fun `parse should set null for missing optional arguments`() {
        val args = OptionalArgs()
        args.parse(listOf("required_value"), "test")

        assertEquals("required_value", args.required)
        assertNull(args.optional)
    }

    @Test
    fun `parse should use default value for missing optional arguments`() {
        val args = DefaultArgs()
        args.parse(listOf("Alice"), "test")

        assertEquals("Alice", args.name)
        assertEquals(1, args.count)
    }

    @Test
    fun `parse should override default when provided`() {
        val args = DefaultArgs()
        args.parse(listOf("Alice", "5"), "test")

        assertEquals("Alice", args.name)
        assertEquals(5, args.count)
    }

    @Test
    fun `parse should run validators and pass valid values`() {
        val args = ValidatedArgs()
        args.parse(listOf("25"), "test")

        assertEquals(25, args.age)
    }

    @Test
    fun `parse should run validators and fail invalid values`() {
        val args = ValidatedArgs()

        val exception = assertFailsWith<InvalidArgumentValueException> {
            args.parse(listOf("-5"), "test")
        }

        assertTrue(exception.message.contains("Age must be non-negative"))
    }

    @Test
    fun `parse should handle enum arguments case insensitively`() {
        val args = EnumArgs()
        args.parse(listOf("red"), "test")

        assertEquals(Color.RED, args.color)
    }

    @Test
    fun `parse should fail on invalid enum value`() {
        val args = EnumArgs()

        val exception = assertFailsWith<InvalidArgumentValueException> {
            args.parse(listOf("yellow"), "test")
        }

        assertTrue(exception.message.contains("Must be one of:"))
    }

    @Test
    fun `parse should handle multiple arguments in order`() {
        val args = MultipleArgs()
        args.parse(listOf("hello", "42", "3.14"), "test")

        assertEquals("hello", args.first)
        assertEquals(42, args.second)
        assertEquals(3.14, args.third)
    }

    @Test
    fun `parse should throw on too many arguments`() {
        val args = SimpleArgs()

        val exception = assertFailsWith<CommandParseException> {
            args.parse(listOf("Alice", "extra"), "test")
        }

        assertTrue(exception.message.contains("Too many parameters"))
    }

    @Test
    fun `parse should throw on second call to prevent reuse`() {
        val args = SimpleArgs()
        args.parse(listOf("Alice"), "test")
        assertEquals("Alice", args.name)

        // Second call should throw IllegalStateException
        val exception = assertFailsWith<IllegalStateException> {
            args.parse(listOf("Bob"), "test")
        }
        assertTrue(exception.message!!.contains("can only be called once per instance"))
    }


    @Test
    fun `parseValue should parse String`() {
        assertEquals("hello", parseValue<String>("hello"))
    }

    @Test
    fun `parseValue should parse Int`() {
        assertEquals(42, parseValue<Int>("42"))
    }

    @Test
    fun `parseValue should fail on invalid Int`() {
        assertFailsWith<InvalidArgumentValueException> {
            parseValue<Int>("not_a_number")
        }
    }

    @Test
    fun `parseValue should parse Long`() {
        assertEquals(12345678901234L, parseValue<Long>("12345678901234"))
    }

    @Test
    fun `parseValue should parse Double`() {
        assertEquals(3.14159, parseValue<Double>("3.14159"))
    }

    @Test
    fun `parseValue should parse Boolean true values`() {
        assertTrue(parseValue<Boolean>("true"))
        assertTrue(parseValue<Boolean>("TRUE"))
        assertTrue(parseValue<Boolean>("1"))
        assertTrue(parseValue<Boolean>("yes"))
        assertTrue(parseValue<Boolean>("on"))
    }

    @Test
    fun `parseValue should parse Boolean false values`() {
        assertFalse(parseValue<Boolean>("false"))
        assertFalse(parseValue<Boolean>("FALSE"))
        assertFalse(parseValue<Boolean>("0"))
        assertFalse(parseValue<Boolean>("no"))
        assertFalse(parseValue<Boolean>("off"))
    }


    @Test
    fun `generateHelpText should include error message`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "",
            schema = emptyList(),
            argumentName = null,
            message = "Something went wrong"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("❌ Something went wrong"))
    }

    @Test
    fun `generateHelpText should include usage line`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "admin user add",
            commandDescription = "",
            schema = listOf(
                ArgumentDefinition("name", "", false, null, "String", { it }, emptyList()),
                ArgumentDefinition("role", "", true, "member", "String", { it }, emptyList())
            ),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("/admin user add <name> [role]"))
    }

    @Test
    fun `generateHelpText should include command description`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "This is a test command",
            schema = emptyList(),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("This is a test command"))
    }

    @Test
    fun `generateHelpText should include parameter details`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "",
            schema = listOf(
                ArgumentDefinition("name", "The user name", false, null, "String", { it }, emptyList()),
                ArgumentDefinition("count", "Number of times", true, 1, "Int", { it.toInt() }, emptyList())
            ),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("name (String) - The user name"))
        assertTrue(help.contains("count (Int) - Number of times (Default: 1)"))
    }


    @Test
    fun `root command without arguments should match`() = runTest {
        val routeNode = handling {
            commandEndpoint("ping") { invokedHandlers.add("ping") }
        }

        assertTrue(routeNode.execute(createContext("/ping")))
        assertEquals(listOf("ping"), invokedHandlers)
    }

    @Test
    fun `root command with direct handler should work`() = runTest {
        val routeNode = handling {
            commandEndpoint("ping") { invokedHandlers.add("ping:${commandPath}") }
        }

        assertTrue(routeNode.execute(createContext("/ping")))
        assertEquals(listOf("ping:ping"), invokedHandlers)
    }

    @Test
    fun `root command with bot username should match`() = runTest {
        val routeNode = handling {
            commandEndpoint("ping") { invokedHandlers.add("ping") }
        }

        assertTrue(routeNode.execute(createContext("/ping@test_bot")))
        assertEquals(listOf("ping"), invokedHandlers)
    }

    @Test
    fun `root command with different bot username should not match`() = runTest {
        val routeNode = handling {
            commandEndpoint("ping") { invokedHandlers.add("ping") }
        }

        assertFalse(routeNode.execute(createContext("/ping@other_bot")))
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `root command with typed arguments should parse and invoke`() = runTest {
        val routeNode = handling {
            command("greet", ::SimpleArgs) { invokedHandlers.add("greet:${arguments.name}") }
        }

        assertTrue(routeNode.execute(createContext("/greet Alice")))
        assertEquals(listOf("greet:Alice"), invokedHandlers)
    }

    @Test
    fun `root command case insensitive should match`() = runTest {
        val routeNode = handling {
            commandEndpoint("PING") { invokedHandlers.add("ping") }
        }

        assertTrue(routeNode.execute(createContext("/ping")))
        assertEquals(listOf("ping"), invokedHandlers)
    }


    @Test
    fun `subcommand without arguments should match`() = runTest {
        val routeNode = handling {
            command("tool") {
                subCommandEndpoint("execute") { invokedHandlers.add("tool:execute") }
            }
        }

        assertTrue(routeNode.execute(createContext("/tool execute")))
        assertEquals(listOf("tool:execute"), invokedHandlers)
    }

    @Test
    fun `subcommand with direct handler should work`() = runTest {
        val routeNode = handling {
            command("tool") {
                subCommandEndpoint("execute") { invokedHandlers.add("tool:execute:${commandPath}") }
            }
        }

        assertTrue(routeNode.execute(createContext("/tool execute")))
        assertEquals(listOf("tool:execute:tool execute"), invokedHandlers)
    }

    @Test
    fun `subcommand with arguments should parse and invoke`() = runTest {
        val routeNode = handling {
            command("tool") {
                subCommand("rename", ::SimpleArgs) { invokedHandlers.add("tool:rename:${arguments.name}") }
            }
        }

        assertTrue(routeNode.execute(createContext("/tool rename new_name")))
        assertEquals(listOf("tool:rename:new_name"), invokedHandlers)
    }

    @Test
    fun `subcommand case insensitive should match`() = runTest {
        val routeNode = handling {
            command("tool") {
                subCommandEndpoint("EXECUTE") { invokedHandlers.add("tool:execute") }
            }
        }

        assertTrue(routeNode.execute(createContext("/tool execute")))
        assertEquals(listOf("tool:execute"), invokedHandlers)
    }

    @Test
    fun `nested subcommands should work`() = runTest {
        val routeNode = handling {
            command("admin") {
                subCommand("user") {
                    subCommandEndpoint("add") { invokedHandlers.add("admin:user:add") }
                    subCommandEndpoint("remove") { invokedHandlers.add("admin:user:remove") }
                }
            }
        }

        assertTrue(routeNode.execute(createContext("/admin user add")))
        assertEquals(listOf("admin:user:add"), invokedHandlers)

        resetTracking()
        assertTrue(routeNode.execute(createContext("/admin user remove")))
        assertEquals(listOf("admin:user:remove"), invokedHandlers)
    }

    @Test
    fun `nested subcommand with arguments should parse correctly`() = runTest {
        val routeNode = handling {
            command("admin") {
                subCommand("user") {
                    subCommand("ban", ::SimpleArgs) { invokedHandlers.add("admin:user:ban:${arguments.name}") }
                }
            }
        }

        assertTrue(routeNode.execute(createContext("/admin user ban john_doe")))
        assertEquals(listOf("admin:user:ban:john_doe"), invokedHandlers)
    }

    @Test
    fun `parent command handle should work when no subcommand matches`() = runTest {
        val routeNode = handling {
            command("tool") {
                handle { invokedHandlers.add("tool:root") }
                subCommandEndpoint("execute") { invokedHandlers.add("tool:execute") }
            }
        }

        assertTrue(routeNode.execute(createContext("/tool execute")))
        assertEquals(listOf("tool:execute"), invokedHandlers)

        resetTracking()
        assertTrue(routeNode.execute(createContext("/tool")))
        assertEquals(listOf("tool:root"), invokedHandlers)
    }


    @Test
    fun `TelegramBotCommandContext should expose parsed command`() = runTest {
        var capturedPath: String? = null
        var capturedArgs: List<String>? = null

        val routeNode = handling {
            command("test") {
                handle {
                    capturedPath = commandPath
                    capturedArgs = unconsumedArguments
                }
            }
        }

        routeNode.execute(createContext("/test arg1 arg2"))

        assertEquals("test", capturedPath)
        assertEquals(listOf("arg1", "arg2"), capturedArgs)
    }

    @Test
    fun `subcommand should update command path and unconsumed arguments`() = runTest {
        var capturedPath: String? = null
        var capturedArgs: List<String>? = null

        val routeNode = handling {
            command("admin") {
                subCommandEndpoint("user") {
                    capturedPath = commandPath
                    capturedArgs = unconsumedArguments
                }
            }
        }

        routeNode.execute(createContext("/admin user arg1 arg2"))

        assertEquals("admin user", capturedPath)
        assertEquals(listOf("arg1", "arg2"), capturedArgs)
    }


    @Test
    fun `multiple root commands should route correctly`() = runTest {
        val routeNode = handling {
            commandEndpoint("ping") { invokedHandlers.add("ping") }
            command("echo", ::SimpleArgs) { invokedHandlers.add("echo:${arguments.name}") }
            command("admin") {
                subCommandEndpoint("status") { invokedHandlers.add("admin:status") }
            }
        }

        assertTrue(routeNode.execute(createContext("/ping")))
        assertEquals(listOf("ping"), invokedHandlers)

        resetTracking()
        assertTrue(routeNode.execute(createContext("/echo hello")))
        assertEquals(listOf("echo:hello"), invokedHandlers)

        resetTracking()
        assertTrue(routeNode.execute(createContext("/admin status")))
        assertEquals(listOf("admin:status"), invokedHandlers)
    }


    @Test
    fun `first matching command should short-circuit`() = runTest {
        val routeNode = handling {
            commandEndpoint("test") { invokedHandlers.add("first") }
            commandEndpoint("test") { invokedHandlers.add("second") }
        }

        routeNode.execute(createContext("/test"))

        assertEquals(listOf("first"), invokedHandlers)
    }


    @Test
    fun `non-matching command should return false`() = runTest {
        val routeNode = handling {
            commandEndpoint("start") { invokedHandlers.add("start") }
        }

        assertFalse(routeNode.execute(createContext("/unknown")))
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `partial command match should not invoke handler`() = runTest {
        val routeNode = handling {
            command("admin") {
                subCommandEndpoint("user") { invokedHandlers.add("admin:user") }
            }
        }

        // Only /admin without subcommand - no handler at admin level
        assertFalse(routeNode.execute(createContext("/admin")))
        assertTrue(invokedHandlers.isEmpty())
    }


    @Test
    fun `structured command context should expose typed arguments`() = runTest {
        class TestArgs : BotArguments() {
            val value: Int by requireArgument("A number")
        }

        var capturedValue: Int? = null

        val routeNode = handling {
            command("number", ::TestArgs) { capturedValue = arguments.value }
        }

        assertTrue(routeNode.execute(createContext("/number 42")))
        assertEquals(42, capturedValue)
    }

    @Test
    fun `validation failure should throw exception by default`() = runTest {
        class StrictArgs : BotArguments() {
            val value: Int by requireArgument<Int>("Value")
                .validate { require(it in 1..10) { "Value must be 1-10" } }
        }

        val routeNode = handling {
            command("strict", ::StrictArgs, sendHelpOnError = false) {
                invokedHandlers.add("strict:${arguments.value}")
            }
        }

        // Valid value should work
        assertTrue(routeNode.execute(createContext("/strict 5")))
        assertEquals(listOf("strict:5"), invokedHandlers)

        // Invalid value should throw CommandParseException
        resetTracking()
        val exception = assertFailsWith<CommandParseException> {
            routeNode.execute(createContext("/strict 100"))
        }
        assertTrue(exception.message.contains("Value must be 1-10"))
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `launch in handler should complete before execute returns`() = runTest {
        val handlerMainCompleted = CompletableDeferred<Unit>()
        val launchCompleted = CompletableDeferred<Unit>()

        val routeNode = handling {
            commandEndpoint("test") {
                launch {
                    handlerMainCompleted.await()
                    launchCompleted.complete(Unit)
                }
                handlerMainCompleted.complete(Unit)
            }
        }

        assertTrue(routeNode.execute(createContext("/test")))
        assertTrue(launchCompleted.isCompleted)
    }

    @Test
    fun `launch in typed args handler should complete before execute returns`() = runTest {
        val handlerMainCompleted = CompletableDeferred<Unit>()
        val launchCompleted = CompletableDeferred<Unit>()

        val routeNode = handling {
            command("greet", ::SimpleArgs) {
                val args = arguments
                launch {
                    handlerMainCompleted.await()
                    invokedHandlers.add("greet:${args.name}")
                    launchCompleted.complete(Unit)
                }
                handlerMainCompleted.complete(Unit)
            }
        }

        assertTrue(routeNode.execute(createContext("/greet Alice")))
        assertTrue(launchCompleted.isCompleted)
        assertEquals(listOf("greet:Alice"), invokedHandlers)
    }

    // ==================== sendHelpOnError Tests ====================

    @Test
    fun `sendHelpOnError=false should throw on missing required argument`() = runTest {
        val routeNode = handling {
            command("test", ::SimpleArgs, sendHelpOnError = false) {
                invokedHandlers.add("test:${arguments.name}")
            }
        }

        val exception = assertFailsWith<CommandParseException> {
            routeNode.execute(createContext("/test"))
        }

        assertEquals("Missing required parameter", exception.message)
        assertEquals("name", exception.argumentName)
    }

    @Test
    fun `subCommand with sendHelpOnError=false should throw on error`() = runTest {
        val routeNode = handling {
            command("admin") {
                subCommand("user", ::SimpleArgs, sendHelpOnError = false) {
                    invokedHandlers.add("admin:user:${arguments.name}")
                }
            }
        }

        val exception = assertFailsWith<CommandParseException> {
            routeNode.execute(createContext("/admin user"))
        }

        assertEquals("Missing required parameter", exception.message)
    }

    // ==================== Handler-level Exception Catching Tests ====================

    @Test
    fun `handler can catch CommandParseException with sendHelpOnError=false`() = runTest {
        var capturedException: CommandParseException? = null

        val routeNode = handling {
            command("test") {
                handle {
                    try {
                        val args = SimpleArgs()
                        args.parse(unconsumedArguments, commandPath, this)
                        invokedHandlers.add("test:${args.name}")
                    } catch (e: CommandParseException) {
                        capturedException = e
                        invokedHandlers.add("error:${e.message}")
                    }
                }
            }
        }

        assertTrue(routeNode.execute(createContext("/test")))
        assertNotNull(capturedException)
        assertEquals("Missing required parameter", capturedException.message)
        assertEquals(listOf("error:Missing required parameter"), invokedHandlers)
    }

    @Test
    fun `handler can access context from CommandParseException`() = runTest {
        var capturedContext: TelegramBotCommandContext? = null

        val routeNode = handling {
            command("test") {
                handle {
                    try {
                        val args = SimpleArgs()
                        args.parse(unconsumedArguments, commandPath, this)
                        invokedHandlers.add("test:${args.name}")
                    } catch (e: CommandParseException) {
                        capturedContext = e.context
                        invokedHandlers.add("error")
                    }
                }
            }
        }

        assertTrue(routeNode.execute(createContext("/test")))
        assertNotNull(capturedContext)
        assertEquals("test", capturedContext!!.commandPath)
        assertEquals(listOf("error"), invokedHandlers)
    }

    // ==================== Interceptor-level Exception Catching Tests ====================

    @Test
    fun `interceptor pattern can catch CommandParseException`() = runTest {
        var capturedException: CommandParseException? = null

        val routeNode = handling {
            command("test", ::SimpleArgs, sendHelpOnError = false) {
                invokedHandlers.add("test:${arguments.name}")
            }
        }

        val dispatcher = HandlerTelegramEventDispatcher(routeNode)

        try {
            dispatcher.dispatch(createContext("/test"))
        } catch (e: CommandParseException) {
            capturedException = e
        }

        assertNotNull(capturedException)
        assertEquals("Missing required parameter", capturedException.message)
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `interceptor can send custom error message using exception context`() = runTest {
        var sentMessage: String? = null

        val routeNode = handling {
            command("test", ::SimpleArgs, sendHelpOnError = false) {
                invokedHandlers.add("test:${arguments.name}")
            }
        }

        val dispatcher = HandlerTelegramEventDispatcher(routeNode)

        try {
            dispatcher.dispatch(createContext("/test"))
        } catch (e: CommandParseException) {
            sentMessage = "Custom error for command /${e.commandPath}: ${e.message}"
        }

        assertEquals("Custom error for command /test: Missing required parameter", sentMessage)
    }

    @Test
    fun `interceptor can use exception to generate help text`() = runTest {
        var generatedHelp: String? = null

        val routeNode = handling {
            command("test", ::SimpleArgs, sendHelpOnError = false) {
                invokedHandlers.add("test:${arguments.name}")
            }
        }

        val dispatcher = HandlerTelegramEventDispatcher(routeNode)

        try {
            dispatcher.dispatch(createContext("/test"))
        } catch (e: CommandParseException) {
            generatedHelp = e.generateHelpText()
        }

        assertNotNull(generatedHelp)
        assertTrue(generatedHelp.contains("❌ Missing required parameter"))
        assertTrue(generatedHelp.contains("/test <name>"))
    }

    @Test
    fun `interceptor can access bot context from CommandParseException`() = runTest {
        var capturedChatId: Long? = null

        val routeNode = handling {
            command("test", ::SimpleArgs, sendHelpOnError = false) {
                invokedHandlers.add("test:${arguments.name}")
            }
        }

        val dispatcher = HandlerTelegramEventDispatcher(routeNode)

        try {
            dispatcher.dispatch(createContext("/test"))
        } catch (e: CommandParseException) {
            capturedChatId = e.context?.event?.message?.chat?.id
        }

        assertEquals(100L, capturedChatId)
    }

    @Test
    fun `interceptor can swallow exception to continue processing`() = runTest {
        val routeNode = handling {
            command("test", ::SimpleArgs, sendHelpOnError = false) {
                invokedHandlers.add("test:${arguments.name}")
            }
        }

        val dispatcher = HandlerTelegramEventDispatcher(routeNode)

        try {
            dispatcher.dispatch(createContext("/test"))
        } catch (_: CommandParseException) {
            // Swallow
        }

        assertTrue(invokedHandlers.isEmpty())
    }
}
