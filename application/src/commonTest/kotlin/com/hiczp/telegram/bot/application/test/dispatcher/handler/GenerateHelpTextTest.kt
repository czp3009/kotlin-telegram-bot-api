package com.hiczp.telegram.bot.application.test.dispatcher.handler

import com.hiczp.telegram.bot.application.dispatcher.handler.command.ArgumentDefinition
import com.hiczp.telegram.bot.application.dispatcher.handler.command.CommandParseException
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GenerateHelpTextTest {

    // ==================== Error Message Tests ====================

    @Test
    fun `should include error indicator with message`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "",
            schema = emptyList(),
            argumentName = null,
            message = "Something went wrong"
        )

        val help = exception.generateHelpText()

        assertTrue(help.startsWith("❌ Something went wrong"))
    }

    @Test
    fun `should handle empty error message`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "",
            schema = emptyList(),
            argumentName = null,
            message = ""
        )

        val help = exception.generateHelpText()

        assertTrue(help.startsWith("❌ "))
    }

    @Test
    fun `should handle multiline error message`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "",
            schema = emptyList(),
            argumentName = null,
            message = "Multiple errors:\n- Error 1\n- Error 2"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("❌ Multiple errors:"))
        assertTrue(help.contains("- Error 1"))
        assertTrue(help.contains("- Error 2"))
    }

    // ==================== Usage Line Tests ====================

    @Test
    fun `should include usage line with command path`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "ping",
            commandDescription = "",
            schema = emptyList(),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("📝 Usage:"))
        assertTrue(help.contains("/ping"))
    }

    @Test
    fun `should include nested command path in usage`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "admin user add",
            commandDescription = "",
            schema = emptyList(),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("/admin user add"))
    }

    @Test
    fun `should show required arguments with angle brackets`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "greet",
            commandDescription = "",
            schema = listOf(
                ArgumentDefinition(
                    name = "name",
                    description = "User name",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "String",
                    parser = { it },
                    validators = emptyList()
                )
            ),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("/greet <name>"))
    }

    @Test
    fun `should show optional arguments with square brackets`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "search",
            commandDescription = "",
            schema = listOf(
                ArgumentDefinition(
                    name = "query",
                    description = "Search query",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "String",
                    parser = { it },
                    validators = emptyList()
                ),
                ArgumentDefinition(
                    name = "limit",
                    description = "Max results",
                    isOptional = true,
                    defaultValue = 10,
                    typeName = "Int",
                    parser = { it.toInt() },
                    validators = emptyList()
                )
            ),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("/search <query> [limit]"))
    }

    @Test
    fun `should show multiple required arguments in order`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "ban",
            commandDescription = "",
            schema = listOf(
                ArgumentDefinition(
                    name = "userId",
                    description = "User to ban",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "Long",
                    parser = { it.toLong() },
                    validators = emptyList()
                ),
                ArgumentDefinition(
                    name = "reason",
                    description = "Ban reason",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "String",
                    parser = { it },
                    validators = emptyList()
                )
            ),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("/ban <userId> <reason>"))
    }

    @Test
    fun `should show mixed required and optional arguments`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "send",
            commandDescription = "",
            schema = listOf(
                ArgumentDefinition(
                    name = "to",
                    description = "Recipient",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "String",
                    parser = { it },
                    validators = emptyList()
                ),
                ArgumentDefinition(
                    name = "message",
                    description = "Message body",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "String",
                    parser = { it },
                    validators = emptyList()
                ),
                ArgumentDefinition(
                    name = "priority",
                    description = "Priority level",
                    isOptional = true,
                    defaultValue = "normal",
                    typeName = "String",
                    parser = { it },
                    validators = emptyList()
                ),
                ArgumentDefinition(
                    name = "silent",
                    description = "Silent mode",
                    isOptional = true,
                    defaultValue = false,
                    typeName = "Boolean",
                    parser = { it.toBoolean() },
                    validators = emptyList()
                )
            ),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("/send <to> <message> [priority] [silent]"))
    }

    // ==================== Command Description Tests ====================

    @Test
    fun `should include command description when provided`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "This is a test command for demonstration",
            schema = emptyList(),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("This is a test command for demonstration"))
    }

    @Test
    fun `should not include empty command description`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "",
            schema = emptyList(),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        // Check that there's no extra blank line where description would be
        assertFalse(help.contains("Error\n\n\n"))
    }

    @Test
    fun `should handle multiline command description`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "A complex command.\nSupports multiple lines.\nUse with caution.",
            schema = emptyList(),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("A complex command."))
        assertTrue(help.contains("Supports multiple lines."))
        assertTrue(help.contains("Use with caution."))
    }

    // ==================== Parameter Details Tests ====================

    @Test
    fun `should include parameter section when schema is not empty`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "",
            schema = listOf(
                ArgumentDefinition(
                    name = "name",
                    description = "The name",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "String",
                    parser = { it },
                    validators = emptyList()
                )
            ),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("⚙️ Parameters:"))
    }

    @Test
    fun `should not include parameter section when schema is empty`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "",
            schema = emptyList(),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertFalse(help.contains("⚙️ Parameters:"))
    }

    @Test
    fun `should show parameter name and type`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "",
            schema = listOf(
                ArgumentDefinition(
                    name = "count",
                    description = "Number of items",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "Int",
                    parser = { it.toInt() },
                    validators = emptyList()
                )
            ),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("count (Int)"))
    }

    @Test
    fun `should show parameter description when provided`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "",
            schema = listOf(
                ArgumentDefinition(
                    name = "name",
                    description = "The user's display name",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "String",
                    parser = { it },
                    validators = emptyList()
                )
            ),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("name (String) - The user's display name"))
    }

    @Test
    fun `should show default value for optional parameters`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "",
            schema = listOf(
                ArgumentDefinition(
                    name = "count",
                    description = "Number of times",
                    isOptional = true,
                    defaultValue = 5,
                    typeName = "Int",
                    parser = { it.toInt() },
                    validators = emptyList()
                )
            ),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("(Default: 5)"))
    }

    @Test
    fun `should show Optional label for nullable parameters without default`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "",
            schema = listOf(
                ArgumentDefinition(
                    name = "nickname",
                    description = "Optional nickname",
                    isOptional = true,
                    defaultValue = null,
                    typeName = "String",
                    parser = { it },
                    validators = emptyList()
                )
            ),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("(Optional)"))
        assertFalse(help.contains("(Default:"))
    }

    @Test
    fun `should mark error argument with warning indicator`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "",
            schema = listOf(
                ArgumentDefinition(
                    name = "name",
                    description = "User name",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "String",
                    parser = { it },
                    validators = emptyList()
                ),
                ArgumentDefinition(
                    name = "age",
                    description = "User age",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "Int",
                    parser = { it.toInt() },
                    validators = emptyList()
                )
            ),
            argumentName = "age",  // This is the argument that caused the error
            message = "Invalid value"
        )

        val help = exception.generateHelpText()

        // The 'age' parameter should have the warning indicator
        assertTrue(help.contains("⚠️ age (Int)"))
        // The 'name' parameter should not have the indicator
        assertFalse(help.contains("⚠️ name"))
    }

    @Test
    fun `should not mark any parameter when argumentName is null`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "",
            schema = listOf(
                ArgumentDefinition(
                    name = "name",
                    description = "User name",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "String",
                    parser = { it },
                    validators = emptyList()
                ),
                ArgumentDefinition(
                    name = "age",
                    description = "User age",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "Int",
                    parser = { it.toInt() },
                    validators = emptyList()
                )
            ),
            argumentName = null,
            message = "Too many parameters"
        )

        val help = exception.generateHelpText()

        assertFalse(help.contains("⚠️"))
    }

    @Test
    fun `should handle parameter without description`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "",
            schema = listOf(
                ArgumentDefinition(
                    name = "value",
                    description = "",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "String",
                    parser = { it },
                    validators = emptyList()
                )
            ),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("value (String)"))
        // Should not have " - " when description is blank
        val valueLine = help.lines().find { it.contains("value (String)") }
        assertTrue(valueLine != null)
        assertFalse(valueLine.contains(" - "))
    }

    // ==================== Type Display Tests ====================

    @Test
    fun `should display String type`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "",
            schema = listOf(
                ArgumentDefinition(
                    name = "text",
                    description = "Text value",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "String",
                    parser = { it },
                    validators = emptyList()
                )
            ),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("text (String)"))
    }

    @Test
    fun `should display Int type`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "",
            schema = listOf(
                ArgumentDefinition(
                    name = "number",
                    description = "Integer value",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "Int",
                    parser = { it.toInt() },
                    validators = emptyList()
                )
            ),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("number (Int)"))
    }

    @Test
    fun `should display Long type`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "",
            schema = listOf(
                ArgumentDefinition(
                    name = "id",
                    description = "User ID",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "Long",
                    parser = { it.toLong() },
                    validators = emptyList()
                )
            ),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("id (Long)"))
    }

    @Test
    fun `should display Double type`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "",
            schema = listOf(
                ArgumentDefinition(
                    name = "price",
                    description = "Item price",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "Double",
                    parser = { it.toDouble() },
                    validators = emptyList()
                )
            ),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("price (Double)"))
    }

    @Test
    fun `should display Boolean type`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "",
            schema = listOf(
                ArgumentDefinition(
                    name = "enabled",
                    description = "Enable flag",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "Boolean",
                    parser = { it.toBoolean() },
                    validators = emptyList()
                )
            ),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("enabled (Boolean)"))
    }

    @Test
    fun `should display custom type name for enums`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "",
            schema = listOf(
                ArgumentDefinition(
                    name = "color",
                    description = "Color choice",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "Color",
                    parser = { it },
                    validators = emptyList()
                )
            ),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("color (Color)"))
    }

    // ==================== Integration Tests ====================

    @Test
    fun `should generate complete help text for complex command`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "admin user create",
            commandDescription = "Create a new user in the system with specified roles and permissions",
            schema = listOf(
                ArgumentDefinition(
                    name = "username",
                    description = "The unique username for the new user",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "String",
                    parser = { it },
                    validators = emptyList()
                ),
                ArgumentDefinition(
                    name = "email",
                    description = "The user's email address",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "String",
                    parser = { it },
                    validators = emptyList()
                ),
                ArgumentDefinition(
                    name = "role",
                    description = "The user's role",
                    isOptional = true,
                    defaultValue = "member",
                    typeName = "String",
                    parser = { it },
                    validators = emptyList()
                ),
                ArgumentDefinition(
                    name = "sendWelcome",
                    description = "Send welcome email",
                    isOptional = true,
                    defaultValue = true,
                    typeName = "Boolean",
                    parser = { it.toBoolean() },
                    validators = emptyList()
                )
            ),
            argumentName = "email",
            message = "Invalid email format"
        )

        val help = exception.generateHelpText()

        // Check all parts are present
        assertTrue(help.contains("❌ Invalid email format"))
        assertTrue(help.contains("📝 Usage:"))
        assertTrue(help.contains("/admin user create <username> <email> [role] [sendWelcome]"))
        assertTrue(help.contains("Create a new user in the system with specified roles and permissions"))
        assertTrue(help.contains("⚙️ Parameters:"))
        assertTrue(help.contains("username (String) - The unique username for the new user"))
        assertTrue(help.contains("⚠️ email (String) - The user's email address"))
        assertTrue(help.contains("role (String) - The user's role (Default: member)"))
        assertTrue(help.contains("sendWelcome (Boolean) - Send welcome email (Default: true)"))
    }

    @Test
    fun `should generate minimal help text for simple command`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "ping",
            commandDescription = "",
            schema = emptyList(),
            argumentName = null,
            message = "Unknown error"
        )

        val help = exception.generateHelpText()

        // Should only have error and usage
        assertTrue(help.contains("❌ Unknown error"))
        assertTrue(help.contains("📝 Usage:"))
        assertTrue(help.contains("/ping"))
        // Should not have description or parameters
        assertFalse(help.contains("⚙️ Parameters:"))
    }

    @Test
    fun `should generate help text for missing required parameter error`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "ban",
            commandDescription = "Ban a user from the server",
            schema = listOf(
                ArgumentDefinition(
                    name = "userId",
                    description = "The ID of the user to ban",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "Long",
                    parser = { it.toLong() },
                    validators = emptyList()
                ),
                ArgumentDefinition(
                    name = "reason",
                    description = "The reason for the ban",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "String",
                    parser = { it },
                    validators = emptyList()
                ),
                ArgumentDefinition(
                    name = "duration",
                    description = "Ban duration in hours",
                    isOptional = true,
                    defaultValue = null,
                    typeName = "Int",
                    parser = { it.toInt() },
                    validators = emptyList()
                )
            ),
            argumentName = "reason",
            message = "Missing required parameter"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("❌ Missing required parameter"))
        assertTrue(help.contains("/ban <userId> <reason> [duration]"))
        assertTrue(help.contains("Ban a user from the server"))
        assertTrue(help.contains("userId (Long) - The ID of the user to ban"))
        assertTrue(help.contains("⚠️ reason (String) - The reason for the ban"))
        assertTrue(help.contains("duration (Int) - Ban duration in hours (Optional)"))
    }

    @Test
    fun `should generate help text for too many parameters error`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "echo",
            commandDescription = "Echo a message back",
            schema = listOf(
                ArgumentDefinition(
                    name = "message",
                    description = "The message to echo",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "String",
                    parser = { it },
                    validators = emptyList()
                )
            ),
            argumentName = null,
            message = "Too many parameters. Maximum expected: 1, received: 3."
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("❌ Too many parameters. Maximum expected: 1, received: 3."))
        assertTrue(help.contains("/echo <message>"))
    }

    @Test
    fun `should generate help text for validation failure`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "setPort",
            commandDescription = "Set the server port",
            schema = listOf(
                ArgumentDefinition(
                    name = "port",
                    description = "Port number (1-65535)",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "Int",
                    parser = { it.toInt() },
                    validators = emptyList()
                )
            ),
            argumentName = "port",
            message = "Port must be between 1 and 65535"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("❌ Port must be between 1 and 65535"))
        assertTrue(help.contains("/setPort <port>"))
        assertTrue(help.contains("⚠️ port (Int) - Port number (1-65535)"))
    }

    @Test
    fun `should generate help text for type parsing error`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "add",
            commandDescription = "Add two numbers",
            schema = listOf(
                ArgumentDefinition(
                    name = "a",
                    description = "First number",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "Int",
                    parser = { it.toInt() },
                    validators = emptyList()
                ),
                ArgumentDefinition(
                    name = "b",
                    description = "Second number",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "Int",
                    parser = { it.toInt() },
                    validators = emptyList()
                )
            ),
            argumentName = "b",
            message = "Invalid format: For input string: \"abc\""
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("❌ Invalid format: For input string: \"abc\""))
        assertTrue(help.contains("/add <a> <b>"))
        assertTrue(help.contains("a (Int) - First number"))
        assertTrue(help.contains("⚠️ b (Int) - Second number"))
    }

    // ==================== Formatting Tests ====================

    @Test
    fun `should trim trailing whitespace`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "Test command",
            schema = listOf(
                ArgumentDefinition(
                    name = "arg",
                    description = "Test arg",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "String",
                    parser = { it },
                    validators = emptyList()
                )
            ),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertFalse(help.endsWith("\n"))
        assertFalse(help.endsWith("  "))
    }

    @Test
    fun `should maintain consistent formatting structure`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "A test",
            schema = listOf(
                ArgumentDefinition(
                    name = "arg",
                    description = "An arg",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "String",
                    parser = { it },
                    validators = emptyList()
                )
            ),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()
        val lines = help.lines()

        // First line should be error indicator
        assertTrue(lines[0].startsWith("❌ "))

        // Find Usage section
        val usageIndex = lines.indexOfFirst { it.startsWith("📝 Usage:") }
        assertTrue(usageIndex >= 0)

        // Command usage should follow Usage header
        assertTrue(lines[usageIndex + 1].startsWith("/"))

        // Description should be after a blank line
        val descIndex = lines.indexOfFirst { it == "A test" }
        assertTrue(descIndex > usageIndex + 1)

        // Parameters section should be present
        val paramsIndex = lines.indexOfFirst { it.startsWith("⚙️ Parameters:") }
        assertTrue(paramsIndex > descIndex)
    }

    // ==================== Edge Cases ====================

    @Test
    fun `should handle very long command path`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "admin user management permission group create",
            commandDescription = "",
            schema = emptyList(),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("/admin user management permission group create"))
    }

    @Test
    fun `should handle very long description`() {
        val longDescription =
            "This is a very long description that explains in detail what this command does and how it should be used. " +
                    "It contains multiple sentences and provides comprehensive information to the user about the command's purpose and behavior."

        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = longDescription,
            schema = emptyList(),
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains(longDescription))
    }

    @Test
    fun `should handle many parameters`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "configure",
            commandDescription = "",
            schema = (1..10).map { i ->
                ArgumentDefinition(
                    name = "param$i",
                    description = "Parameter $i",
                    isOptional = i > 5,
                    defaultValue = "default$i",
                    typeName = "String",
                    parser = { it },
                    validators = emptyList()
                )
            },
            argumentName = null,
            message = "Error"
        )

        val help = exception.generateHelpText()

        // Check usage line has all parameters
        assertTrue(help.contains("<param1>"))
        assertTrue(help.contains("<param5>"))
        assertTrue(help.contains("[param6]"))
        assertTrue(help.contains("[param10]"))

        // Check parameters section has all parameters
        (1..10).forEach { i ->
            assertTrue(help.contains("param$i (String)"))
        }
    }

    @Test
    fun `should handle special characters in description`() {
        val exception = CommandParseException(
            context = null,
            commandPath = "test",
            commandDescription = "Use <angle> brackets, \"quotes\", and 'apostrophes' carefully!",
            schema = listOf(
                ArgumentDefinition(
                    name = "arg",
                    description = "Value like <placeholder>",
                    isOptional = false,
                    defaultValue = null,
                    typeName = "String",
                    parser = { it },
                    validators = emptyList()
                )
            ),
            argumentName = null,
            message = "Error: value cannot be empty"
        )

        val help = exception.generateHelpText()

        assertTrue(help.contains("Use <angle> brackets, \"quotes\", and 'apostrophes' carefully!"))
        assertTrue(help.contains("Value like <placeholder>"))
    }
}
