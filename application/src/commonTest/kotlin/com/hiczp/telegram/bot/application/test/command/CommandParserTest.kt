package com.hiczp.telegram.bot.application.test.command

import com.hiczp.telegram.bot.application.command.CommandParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CommandParserTest {
    @Test
    fun `parse simple command without arguments`() {
        val result = CommandParser.parse("/start")

        assertNotNull(result)
        assertEquals("start", result.command)
        assertNull(result.username)
        assertNull(result.rawArgs)
        assertEquals(emptyList(), result.args)
    }

    @Test
    fun `parse command with bot username and multiple arguments`() {
        val result = CommandParser.parse("/search@my_bot   kotlin   tutorial ")

        assertNotNull(result)
        assertEquals("search", result.command)
        assertEquals("my_bot", result.username)
        assertEquals("kotlin   tutorial", result.rawArgs)
        assertEquals(listOf("kotlin", "tutorial"), result.args)
    }

    @Test
    fun `parse command with quoted argument containing space`() {
        val result = CommandParser.parse("/remind \"buy milk\" 18:00")

        assertNotNull(result)
        assertEquals("remind", result.command)
        assertNull(result.username)
        assertEquals("\"buy milk\" 18:00", result.rawArgs)
        assertEquals(listOf("buy milk", "18:00"), result.args)
    }

    @Test
    fun `parse command with escaped quotes inside quoted argument`() {
        val result = CommandParser.parse(
            """
            /note "He said \"Hello\""
            """.trimIndent()
        )

        assertNotNull(result)
        assertEquals("note", result.command)
        assertNull(result.username)
        assertEquals("\"He said \\\"Hello\\\"\"", result.rawArgs)
        assertEquals(listOf("He said \"Hello\""), result.args)
    }

    @Test
    fun `return null for null input`() {
        val result = CommandParser.parse(null)
        assertNull(result)
    }

    @Test
    fun `return null for blank input`() {
        val result = CommandParser.parse("   ")
        assertNull(result)
    }

    @Test
    fun `return null for text not starting with slash`() {
        val result = CommandParser.parse("start")
        assertNull(result)
    }

    // Deep linking tests
    @Test
    fun `parse deep linking command with deepLinkingEncoded disabled`() {
        val result = CommandParser.parse("/start arg1_arg2", deepLinkingEncoded = false)

        assertNotNull(result)
        assertEquals("start", result.command)
        assertEquals("arg1_arg2", result.rawArgs)
        assertEquals(listOf("arg1_arg2"), result.args)
    }

    @Test
    fun `parse deep linking command with base64 encoded argument`() {
        // "hello" encoded in base64 is "aGVsbG8="
        val result = CommandParser.parse("/start aGVsbG8=", deepLinkingEncoded = true)

        assertNotNull(result)
        assertEquals("start", result.command)
        assertEquals("aGVsbG8=", result.rawArgs)
        assertEquals(listOf("hello"), result.args)
    }

    @Test
    fun `parse deep linking command with base64 encoded json`() {
        // {"hello":"world"} encoded in base64 is "eyJoZWxsbyI6IndvcmxkIn0="
        // Note: tokenize() will interpret the quotes in the decoded JSON, so the result
        // will be a single argument without the quotes (treated as a quoted string)
        val result = CommandParser.parse("/start eyJoZWxsbyI6IndvcmxkIn0=", deepLinkingEncoded = true)

        assertNotNull(result)
        assertEquals("start", result.command)
        assertEquals("eyJoZWxsbyI6IndvcmxkIn0=", result.rawArgs)
        assertEquals(listOf("{hello:world}"), result.args)
    }

    @Test
    fun `parse deep linking command with invalid base64 preserves original`() {
        // Invalid base64 string should be preserved as-is
        val result = CommandParser.parse("/start not_valid_base64!!!", deepLinkingEncoded = true)

        assertNotNull(result)
        assertEquals("start", result.command)
        assertEquals("not_valid_base64!!!", result.rawArgs)
        assertEquals(listOf("not_valid_base64!!!"), result.args)
    }

    @Test
    fun `parse deep linking command with additional arguments`() {
        // "hello" encoded in base64 is "aGVsbG8=", additional arg preserved
        val result = CommandParser.parse("/start aGVsbG8= extra", deepLinkingEncoded = true)

        assertNotNull(result)
        assertEquals("start", result.command)
        assertEquals("aGVsbG8= extra", result.rawArgs)
        assertEquals(listOf("hello", "extra"), result.args)
    }

    @Test
    fun `default deepLinkingEncoded is false`() {
        val result = CommandParser.parse("/start aGVsbG8=")

        assertNotNull(result)
        assertEquals(listOf("aGVsbG8="), result.args)
    }
}
