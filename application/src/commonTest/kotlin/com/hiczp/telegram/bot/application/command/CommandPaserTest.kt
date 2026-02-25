package com.hiczp.telegram.bot.application.command

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CommandPaserTest {
    @Test
    fun `parse simple command without arguments`() {
        val result = CommandPaser.parse("/start")

        assertNotNull(result)
        assertEquals("start", result.command)
        assertNull(result.username)
        assertNull(result.rawArgs)
        assertEquals(emptyList(), result.args)
    }

    @Test
    fun `parse command with bot username and multiple arguments`() {
        val result = CommandPaser.parse("/search@my_bot   kotlin   tutorial ")

        assertNotNull(result)
        assertEquals("search", result.command)
        assertEquals("my_bot", result.username)
        assertEquals("kotlin   tutorial", result.rawArgs)
        assertEquals(listOf("kotlin", "tutorial"), result.args)
    }

    @Test
    fun `parse command with quoted argument containing space`() {
        val result = CommandPaser.parse("/remind \"buy milk\" 18:00")

        assertNotNull(result)
        assertEquals("remind", result.command)
        assertNull(result.username)
        assertEquals("\"buy milk\" 18:00", result.rawArgs)
        assertEquals(listOf("buy milk", "18:00"), result.args)
    }

    @Test
    fun `parse command with escaped quotes inside quoted argument`() {
        val result = CommandPaser.parse(
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
        val result = CommandPaser.parse(null)
        assertNull(result)
    }

    @Test
    fun `return null for blank input`() {
        val result = CommandPaser.parse("   ")
        assertNull(result)
    }

    @Test
    fun `return null for text not starting with slash`() {
        val result = CommandPaser.parse("start")
        assertNull(result)
    }
}
