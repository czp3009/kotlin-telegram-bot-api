package com.hiczp.telegram.bot.application.test.updatesource

import com.hiczp.telegram.bot.application.exception.TelegramBotShuttingDownException
import com.hiczp.telegram.bot.application.updatesource.SimpleTelegramUpdateSource
import com.hiczp.telegram.bot.protocol.model.Chat
import com.hiczp.telegram.bot.protocol.model.Message
import com.hiczp.telegram.bot.protocol.model.Update
import com.hiczp.telegram.bot.protocol.model.User
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds

class SimpleTelegramUpdateSourceTest {
    private fun createUpdate(updateId: Long = 1L, text: String = "test"): Update {
        val testUser = User(id = 1L, isBot = false, firstName = "TestUser")
        val testChat = Chat(id = 100L, type = "private")
        return Update(
            updateId = updateId,
            message = Message(
                messageId = 1L,
                date = Clock.System.now().epochSeconds,
                chat = testChat,
                from = testUser,
                text = text
            )
        )
    }

    @Test
    fun `push should deliver update to consumer`() = runTest {
        val source = SimpleTelegramUpdateSource()
        val processedUpdates = mutableListOf<Update>()

        // Start consuming
        source.start { update ->
            processedUpdates.add(update)
        }

        // Push an update
        val update = createUpdate(1L, "hello")
        source.push(update)

        assertEquals(1, processedUpdates.size)
        assertEquals(1L, processedUpdates[0].updateId)
    }

    @Test
    fun `start should process multiple updates in order`() = runTest {
        val source = SimpleTelegramUpdateSource()
        val processedUpdates = mutableListOf<Long>()

        // Start consuming
        source.start { update ->
            processedUpdates.add(update.updateId)
        }

        // Push multiple updates
        source.push(createUpdate(1L))
        source.push(createUpdate(2L))
        source.push(createUpdate(3L))

        assertEquals(listOf(1L, 2L, 3L), processedUpdates, "updates should be processed in order")
    }

    @Test
    fun `stop should allow restart`() = runTest {
        val source = SimpleTelegramUpdateSource()
        val processedUpdates = mutableListOf<Long>()

        // First cycle
        source.start { update ->
            processedUpdates.add(update.updateId)
        }
        source.push(createUpdate(1L))
        source.stop(100.milliseconds)

        assertEquals(1, processedUpdates.size)

        // Second cycle
        source.start { update ->
            processedUpdates.add(update.updateId)
        }
        source.push(createUpdate(2L))

        assertEquals(2, processedUpdates.size)
    }

    @Test
    fun `start can be called again after stop`() = runTest {
        val source = SimpleTelegramUpdateSource()
        val processedUpdates = mutableListOf<Long>()

        // First cycle
        source.start { update ->
            processedUpdates.add(update.updateId)
        }
        source.push(createUpdate(1L))
        source.stop(100.milliseconds)

        assertEquals(1, processedUpdates.size)

        // Second cycle
        source.start { update ->
            processedUpdates.add(update.updateId)
        }
        source.push(createUpdate(2L))

        assertEquals(2, processedUpdates.size)
    }

    @Test
    fun `start should throw when already running`() = runTest {
        val source = SimpleTelegramUpdateSource()

        // Start first consumer
        source.start { }

        // Try to start consumer again
        val exception = assertFailsWith<IllegalStateException> {
            source.start { }
        }

        assertEquals(exception.message?.contains("already running"), true)
    }

    @Test
    fun `TelegramBotShuttingDownException should propagate to caller`() = runTest {
        val source = SimpleTelegramUpdateSource()

        // Start consuming that throws TelegramBotShuttingDownException
        source.start {
            throw TelegramBotShuttingDownException()
        }

        // Push should propagate the exception
        val exception = assertFailsWith<TelegramBotShuttingDownException> {
            source.push(createUpdate(1L))
        }
        assertNotNull(exception)
    }

    @Test
    fun `concurrent push should be thread-safe`() = runTest {
        val source = SimpleTelegramUpdateSource()
        val processedCount = atomic(0)

        // Start consuming
        source.start {
            processedCount.incrementAndGet()
        }

        // Push from multiple coroutines concurrently
        val pushJobs = (1..100).map { i ->
            launch {
                source.push(createUpdate(i.toLong()))
            }
        }

        pushJobs.joinAll()

        assertEquals(100, processedCount.value)
    }

    @Test
    fun `push should throw if not started`() = runTest {
        val source = SimpleTelegramUpdateSource()

        // Push without starting should throw
        val exception = assertFailsWith<IllegalStateException> {
            source.push(createUpdate(1L))
        }
        assertEquals(exception.message?.contains("not running"), true)
    }

    @Test
    fun `business exception should propagate to caller`() = runTest {
        val source = SimpleTelegramUpdateSource()

        // Start consuming that throws
        source.start {
            throw RuntimeException("Simulated error")
        }

        // Push should propagate the exception
        val exception = assertFailsWith<RuntimeException> {
            source.push(createUpdate(1L))
        }
        assertEquals("Simulated error", exception.message)
    }

    @Test
    fun `CancellationException should propagate to caller`() = runTest {
        val source = SimpleTelegramUpdateSource()

        // Start consuming that throws CancellationException
        source.start {
            throw CancellationException("Simulated cancellation")
        }

        // Push should propagate the exception
        val exception = assertFailsWith<CancellationException> {
            source.push(createUpdate(1L))
        }
        assertEquals("Simulated cancellation", exception.message)
    }

    @Test
    fun `push should throw after stop`() = runTest {
        val source = SimpleTelegramUpdateSource()

        // Start and stop
        source.start { }
        source.stop(100.milliseconds)

        // Push should throw
        val exception = assertFailsWith<IllegalStateException> {
            source.push(createUpdate(1L))
        }
        assertEquals(exception.message?.contains("not running"), true)
    }

    @Test
    fun `onFinalize should be a no-op`() = runTest {
        val source = SimpleTelegramUpdateSource()

        // onFinalize should not throw and should be a no-op
        source.onFinalize()

        // Source should still be usable after onFinalize (since it's a no-op)
        source.start { }
        source.push(createUpdate(1L))
    }
}
