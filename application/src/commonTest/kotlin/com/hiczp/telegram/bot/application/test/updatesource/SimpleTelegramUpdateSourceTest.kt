package com.hiczp.telegram.bot.application.test.updatesource

import com.hiczp.telegram.bot.application.exception.TelegramBotShuttingDownException
import com.hiczp.telegram.bot.application.updatesource.SimpleTelegramUpdateSource
import com.hiczp.telegram.bot.protocol.model.Chat
import com.hiczp.telegram.bot.protocol.model.Message
import com.hiczp.telegram.bot.protocol.model.Update
import com.hiczp.telegram.bot.protocol.model.User
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
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

    /**
     * Helper function to start the source and wait until it's ready.
     * Uses a CompletableDeferred to signal when start() has been called.
     */
    private suspend fun SimpleTelegramUpdateSource.startAndAwait(
        startedSignal: CompletableDeferred<Unit>,
        consume: suspend (Update) -> Unit
    ) {
        startedSignal.complete(Unit)
        start(consume)
    }

    @Test
    fun `push should deliver update to consumer`() = runTest {
        val source = SimpleTelegramUpdateSource()
        val processedUpdates = mutableListOf<Update>()
        val startedSignal = CompletableDeferred<Unit>()

        // Start source in background - it will suspend until stop() is called
        val sourceJob = launch {
            source.startAndAwait(startedSignal) { update ->
                processedUpdates.add(update)
            }
        }

        // Wait for source to be started
        startedSignal.await()
        // Yield to ensure the source is fully ready
        yield()

        // Push an update
        source.push(createUpdate(1L, "hello"))

        assertEquals(1, processedUpdates.size)
        assertEquals(1L, processedUpdates[0].updateId)

        // Cleanup
        source.stop(100.milliseconds)
        sourceJob.join()
    }

    @Test
    fun `start should process multiple updates in order`() = runTest {
        val source = SimpleTelegramUpdateSource()
        val processedUpdates = mutableListOf<Long>()
        val startedSignal = CompletableDeferred<Unit>()

        val sourceJob = launch {
            source.startAndAwait(startedSignal) { update ->
                processedUpdates.add(update.updateId)
            }
        }

        // Wait for source to be started
        startedSignal.await()
        yield()

        // Push multiple updates
        source.push(createUpdate(1L))
        source.push(createUpdate(2L))
        source.push(createUpdate(3L))

        assertEquals(listOf(1L, 2L, 3L), processedUpdates, "updates should be processed in order")

        // Cleanup
        source.stop(100.milliseconds)
        sourceJob.join()
    }

    @Test
    fun `stop should allow restart`() = runTest {
        val source = SimpleTelegramUpdateSource()
        val processedUpdates = mutableListOf<Long>()

        // First cycle
        val startedSignal1 = CompletableDeferred<Unit>()
        val sourceJob1 = launch {
            source.startAndAwait(startedSignal1) { update ->
                processedUpdates.add(update.updateId)
            }
        }

        startedSignal1.await()
        yield()
        source.push(createUpdate(1L))

        source.stop(100.milliseconds)
        sourceJob1.join()

        assertEquals(1, processedUpdates.size)

        // Second cycle
        val startedSignal2 = CompletableDeferred<Unit>()
        val sourceJob2 = launch {
            source.startAndAwait(startedSignal2) { update ->
                processedUpdates.add(update.updateId)
            }
        }

        startedSignal2.await()
        yield()
        source.push(createUpdate(2L))

        source.stop(100.milliseconds)
        sourceJob2.join()

        assertEquals(2, processedUpdates.size)
    }

    @Test
    fun `start should throw when already running`() = runTest {
        val source = SimpleTelegramUpdateSource()
        val startedSignal = CompletableDeferred<Unit>()

        val sourceJob = launch {
            source.startAndAwait(startedSignal) { }
        }

        // Wait for source to be started
        startedSignal.await()
        yield()

        // Try to start consumer again (should fail)
        val exception = assertFailsWith<IllegalStateException> {
            source.start { }
        }

        assertTrue("already running" in (exception.message ?: ""))

        // Cleanup
        source.stop(100.milliseconds)
        sourceJob.join()
    }

    @Test
    fun `TelegramBotShuttingDownException should propagate to caller`() = runTest {
        val source = SimpleTelegramUpdateSource()
        val startedSignal = CompletableDeferred<Unit>()

        val sourceJob = launch {
            source.startAndAwait(startedSignal) {
                throw TelegramBotShuttingDownException()
            }
        }

        // Wait for source to be started
        startedSignal.await()
        yield()

        // Push should propagate the exception
        val exception = assertFailsWith<TelegramBotShuttingDownException> {
            source.push(createUpdate(1L))
        }
        assertNotNull(exception)

        // Cleanup
        source.stop(100.milliseconds)
        sourceJob.join()
    }

    @Test
    fun `concurrent push should be thread-safe`() = runTest {
        val source = SimpleTelegramUpdateSource()
        val processedCount = atomic(0)
        val startedSignal = CompletableDeferred<Unit>()

        val sourceJob = launch {
            source.startAndAwait(startedSignal) {
                processedCount.incrementAndGet()
            }
        }

        // Wait for source to be started
        startedSignal.await()
        yield()

        // First push to verify
        source.push(createUpdate(0L))

        // Push from multiple coroutines concurrently
        coroutineScope {
            (1..100).map { i ->
                async {
                    source.push(createUpdate(i.toLong()))
                }
            }.awaitAll()
        }

        assertEquals(101, processedCount.value) // 1 initial + 100 concurrent

        // Cleanup
        source.stop(100.milliseconds)
        sourceJob.join()
    }

    @Test
    fun `push should throw if not started`() = runTest {
        val source = SimpleTelegramUpdateSource()

        // Push without starting should throw
        val exception = assertFailsWith<IllegalStateException> {
            source.push(createUpdate(1L))
        }
        assertTrue("not running" in (exception.message ?: ""))
    }

    @Test
    fun `business exception should propagate to caller`() = runTest {
        val source = SimpleTelegramUpdateSource()
        val startedSignal = CompletableDeferred<Unit>()

        val sourceJob = launch {
            source.startAndAwait(startedSignal) {
                throw RuntimeException("Simulated error")
            }
        }

        // Wait for source to be started
        startedSignal.await()
        yield()

        // Push should propagate the exception
        val exception = assertFailsWith<RuntimeException> {
            source.push(createUpdate(1L))
        }
        assertEquals("Simulated error", exception.message)

        // Cleanup
        source.stop(100.milliseconds)
        sourceJob.join()
    }

    @Test
    fun `CancellationException should propagate to caller`() = runTest {
        val source = SimpleTelegramUpdateSource()
        val startedSignal = CompletableDeferred<Unit>()

        launch {
            source.startAndAwait(startedSignal) {
                throw CancellationException("Simulated cancellation")
            }
        }

        // Wait for source to be started
        startedSignal.await()
        yield()

        // Push should propagate the exception
        val exception = assertFailsWith<CancellationException> {
            source.push(createUpdate(1L))
        }
        assertEquals("Simulated cancellation", exception.message)

        // Cleanup - note: don't join sourceJob as it may throw CancellationException
        source.stop(100.milliseconds)
    }

    @Test
    fun `push should throw after stop`() = runTest {
        val source = SimpleTelegramUpdateSource()
        val startedSignal = CompletableDeferred<Unit>()

        val sourceJob = launch {
            source.startAndAwait(startedSignal) { }
        }

        // Wait for source to be started
        startedSignal.await()
        yield()

        // Push to ensure source is working
        source.push(createUpdate(0L))

        // Stop the source
        source.stop(100.milliseconds)
        sourceJob.join()

        // Push should throw
        val exception = assertFailsWith<IllegalStateException> {
            source.push(createUpdate(1L))
        }
        assertTrue("not running" in (exception.message ?: ""))
    }

    @Test
    fun `onFinalize should be a no-op`() = runTest {
        val source = SimpleTelegramUpdateSource()

        // onFinalize should not throw and should be a no-op
        source.onFinalize()

        // Source should still be usable after onFinalize (since it's a no-op)
        val startedSignal = CompletableDeferred<Unit>()
        val sourceJob = launch {
            source.startAndAwait(startedSignal) { }
        }

        startedSignal.await()
        yield()
        source.push(createUpdate(1L))

        // Cleanup
        source.stop(100.milliseconds)
        sourceJob.join()
    }
}
