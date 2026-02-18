package com.hiczp.telegram.bot.protocol.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

private val logger = KotlinLogging.logger {}

/**
 * Unit tests for [TelegramErrorResponseException]
 */
class TelegramErrorResponseExceptionTest {
    private val json = Json

    @Test
    fun testSerializationAndDeserialization() = runTest {
        val originalException = TelegramErrorResponseException(
            description = "Bad Request: chat not found",
            errorCode = 400
        )
        logger.info { "Original exception: $originalException" }
        // Serialize to JSON string
        val jsonString = json.encodeToString(originalException)
        // Deserialize back to object
        val deserializedException = json.decodeFromString<TelegramErrorResponseException>(jsonString)
        logger.info { "Deserialized exception: $deserializedException" }
        assertEquals(originalException.description, deserializedException.description)
        assertEquals(originalException.errorCode, deserializedException.errorCode)
        assertEquals(originalException.message, deserializedException.message)
    }

    @Test
    fun testThrowAfterDeserialization() = runTest {
        val exceptionData = TelegramErrorResponseException(
            description = "Unauthorized: invalid token",
            errorCode = 401
        )
        // Serialize and deserialize
        val jsonString = json.encodeToString(exceptionData)
        val deserializedException = json.decodeFromString<TelegramErrorResponseException>(jsonString)
        // Verify the exception can be thrown and has the expected properties
        val thrown = assertFailsWith<TelegramErrorResponseException> {
            throw deserializedException
        }
        assertEquals(exceptionData.description, thrown.description)
        assertEquals(exceptionData.errorCode, thrown.errorCode)
        assertEquals(exceptionData.message, thrown.message)
    }

    @Test
    fun testEmptyDescription() = runTest {
        val exception = TelegramErrorResponseException(
            description = "",
            errorCode = 0
        )
        val jsonString = json.encodeToString(exception)
        val deserialized = json.decodeFromString<TelegramErrorResponseException>(jsonString)
        assertEquals("", deserialized.description)
        assertEquals(0, deserialized.errorCode)
        assertEquals("", deserialized.message)
    }
}
