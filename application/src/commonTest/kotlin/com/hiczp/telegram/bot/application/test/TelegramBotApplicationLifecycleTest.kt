package com.hiczp.telegram.bot.application.test

import com.hiczp.telegram.bot.application.TelegramBotApplication
import com.hiczp.telegram.bot.application.dispatcher.SimpleTelegramEventDispatcher
import com.hiczp.telegram.bot.application.interceptor.TelegramEventInterceptor
import com.hiczp.telegram.bot.application.updatesource.LongPollingTelegramUpdateSource
import com.hiczp.telegram.bot.application.updatesource.MockTelegramUpdateSource
import com.hiczp.telegram.bot.application.updatesource.TelegramUpdateSource
import com.hiczp.telegram.bot.client.TelegramBotClient
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.model.Chat
import com.hiczp.telegram.bot.protocol.model.Message
import com.hiczp.telegram.bot.protocol.model.Update
import com.hiczp.telegram.bot.protocol.model.User
import io.github.oshai.kotlinlogging.KotlinLoggingConfiguration
import io.github.oshai.kotlinlogging.Level
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.test.runTest
import kotlin.test.*
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO

class TelegramBotApplicationLifecycleTest {
    init {
        KotlinLoggingConfiguration.direct.logLevel = Level.DEBUG
    }

    // Fake client that doesn't make real API calls
    private val fakeClient = TelegramBotClient(botToken = "xxx")

    private fun createTestUpdate(updateId: Long, text: String): Update {
        val testUser = User(id = 1L, isBot = false, firstName = "TestUser")
        val testChat = Chat(id = 100L, type = "private")
        return Update(
            updateId = updateId,
            message = Message(
                messageId = updateId,
                date = Clock.System.now().epochSeconds,
                chat = testChat,
                from = testUser,
                text = text,
            )
        )
    }

    @Test
    fun `start should transition state from NEW to RUNNING`() = runTest {
        val channel = Channel<Update>(Channel.UNLIMITED)
        val updateSource = MockTelegramUpdateSource(channel)
        val dispatchedEvents = mutableListOf<String>()

        val dispatcher = SimpleTelegramEventDispatcher { context ->
            dispatchedEvents.add((context.event as MessageEvent).message.text ?: "")
        }

        val app = TelegramBotApplication(
            client = fakeClient,
            updateSource = updateSource,
            interceptors = emptyList(),
            eventDispatcher = dispatcher
        )

        app.start()

        // Close channel to let updateSource complete naturally
        channel.close()

        // Wait for the app to complete (it will complete because channel is closed)
        app.join()

        assertTrue(dispatchedEvents.isEmpty()) // No updates were provided
    }

    @Test
    fun `start should throw when called twice`() = runTest {
        val channel = Channel<Update>(Channel.UNLIMITED)
        val updateSource = MockTelegramUpdateSource(channel)
        val dispatcher = SimpleTelegramEventDispatcher { }

        val app = TelegramBotApplication(
            client = fakeClient,
            updateSource = updateSource,
            interceptors = emptyList(),
            eventDispatcher = dispatcher
        )

        app.start()

        val exception = assertFailsWith<IllegalStateException> {
            app.start()
        }

        assertEquals("Bot can only be started once", exception.message)

        // Cleanup
        app.stop(ZERO).join()
        channel.close()
    }

    @Test
    fun `stop should be idempotent when called multiple times`() = runTest {
        val channel = Channel<Update>(Channel.UNLIMITED)
        val updateSource = MockTelegramUpdateSource(channel)
        val dispatcher = SimpleTelegramEventDispatcher { }

        val app = TelegramBotApplication(
            client = fakeClient,
            updateSource = updateSource,
            interceptors = emptyList(),
            eventDispatcher = dispatcher
        )

        app.start()

        // Close channel first to let updateSource complete naturally
        channel.close()

        // Wait for the app to settle
        app.join()

        // Call stop multiple times - all should complete immediately since app already stopped
        val stopJob1 = app.stop(ZERO)
        val stopJob2 = app.stop(ZERO)
        val stopJob3 = app.stop(ZERO)

        // All should complete without error
        stopJob1.join()
        stopJob2.join()
        stopJob3.join()
    }

    @Test
    fun `stop on NEW application should return completed job`() = runTest {
        val channel = Channel<Update>(Channel.UNLIMITED)
        val updateSource = MockTelegramUpdateSource(channel)
        val dispatcher = SimpleTelegramEventDispatcher { }

        val app = TelegramBotApplication(
            client = fakeClient,
            updateSource = updateSource,
            interceptors = emptyList(),
            eventDispatcher = dispatcher
        )

        // Don't start the app, just call stop
        val stopJob = app.stop(ZERO)

        // Should complete immediately
        assertTrue(stopJob.isCompleted)
        channel.close()
    }

    @Test
    fun `stop should call updateSource onFinalize`() = runTest {
        val finalizeCalled = CompletableDeferred<Boolean>()
        val updateConsumed = CompletableDeferred<Unit>()

        /**
         * A MockTelegramUpdateSource that tracks whether onFinalize() was called.
         */
        class FinalizeTrackingMockUpdateSource(
            channel: Channel<Update>,
            private val updateConsumedSignal: CompletableDeferred<Unit>,
            private val finalizeSignal: CompletableDeferred<Boolean>
        ) : MockTelegramUpdateSource(channel) {
            override suspend fun start(consume: suspend (Update) -> Unit) {
                // Call parent's start with wrapped consume that signals when an update is processed
                super.start { update ->
                    updateConsumedSignal.complete(Unit)
                    consume(update)
                }
            }

            override suspend fun onFinalize() {
                finalizeSignal.complete(true)
            }
        }

        val channel = Channel<Update>(Channel.UNLIMITED)
        val updateSource = FinalizeTrackingMockUpdateSource(channel, updateConsumed, finalizeCalled)
        val dispatcher = SimpleTelegramEventDispatcher { }

        val app = TelegramBotApplication(
            client = fakeClient,
            updateSource = updateSource,
            interceptors = emptyList(),
            eventDispatcher = dispatcher
        )

        app.start()

        // Send an update to ensure the source has started and is actively processing
        channel.send(createTestUpdate(1, "test"))

        // Wait for the update to be consumed - this confirms the source is fully running
        updateConsumed.await()

        // Use stopSuspend to ensure complete shutdown
        app.stopSuspend(ZERO)
        channel.close()

        // Verify onFinalize was called
        assertTrue(finalizeCalled.await())
    }

    @Test
    fun `graceful shutdown should cancel long running handler after timeout`() = runTest {
        val handlerStarted = CompletableDeferred<Unit>()
        val handlerCancelled = CompletableDeferred<Unit>()
        var handlerCompletedNormally = false

        val channel = Channel<Update>(Channel.UNLIMITED)
        val updateSource = MockTelegramUpdateSource(channel)

        val dispatcher = SimpleTelegramEventDispatcher {
            handlerStarted.complete(Unit)
            try {
                // Simulate long-running work by waiting on a CompletableDeferred that never completes
                // This is more reliable than delay in tests
                CompletableDeferred<Unit>().await()
                handlerCompletedNormally = true
            } catch (e: CancellationException) {
                handlerCancelled.complete(Unit)
                throw e
            }
        }

        val app = TelegramBotApplication(
            client = fakeClient,
            updateSource = updateSource,
            interceptors = emptyList(),
            eventDispatcher = dispatcher
        )

        app.start()

        // Send an update to trigger the handler
        channel.send(createTestUpdate(1, "test"))

        // Wait for handler to start
        handlerStarted.await()

        // Initiate graceful shutdown with short timeout
        val stopJob = app.stop(ZERO)
        stopJob.join()
        channel.close()

        // Verify handler was cancelled (not completed normally)
        handlerCancelled.await()
        assertFalse(handlerCompletedNormally)
    }

    @Test
    fun `should process updates correctly`() = runTest {
        val channel = Channel<Update>(Channel.UNLIMITED)
        val updateSource = MockTelegramUpdateSource(channel)

        val allProcessed = CompletableDeferred<Unit>()
        val processedTexts = mutableListOf<String>()

        val dispatcher = SimpleTelegramEventDispatcher { context ->
            val text = (context.event as MessageEvent).message.text ?: ""
            processedTexts.add(text)
            if (processedTexts.size == 3) {
                allProcessed.complete(Unit)
            }
        }

        val app = TelegramBotApplication(
            client = fakeClient,
            updateSource = updateSource,
            interceptors = emptyList(),
            eventDispatcher = dispatcher
        )

        app.start()

        // Send updates
        channel.send(createTestUpdate(1, "hello"))
        channel.send(createTestUpdate(2, "world"))
        channel.send(createTestUpdate(3, "test"))

        // Wait for all updates to be processed
        allProcessed.await()

        // Close channel and use stopSuspend to ensure complete shutdown
        channel.close()
        app.stopSuspend(ZERO)

        assertEquals(listOf("hello", "world", "test"), processedTexts)
    }

    @Test
    fun `join should suspend until app stops`() = runTest {
        val channel = Channel<Update>(Channel.UNLIMITED)
        val updateSource = MockTelegramUpdateSource(channel)
        val dispatcher = SimpleTelegramEventDispatcher { }

        val app = TelegramBotApplication(
            client = fakeClient,
            updateSource = updateSource,
            interceptors = emptyList(),
            eventDispatcher = dispatcher
        )

        app.start()

        val joinCompleted = CompletableDeferred<Unit>()
        val joinStarted = CompletableDeferred<Unit>()

        launch {
            joinStarted.complete(Unit)
            app.join()
            joinCompleted.complete(Unit)
        }

        // Wait for the join job to start (it will then be suspended on app.join())
        joinStarted.await()

        // join should not have completed yet (app is still running)
        assertFalse(joinCompleted.isCompleted)

        // Stop the app
        app.stop(ZERO).join()
        channel.close()

        // Now join should complete
        joinCompleted.await()
    }

    @Test
    fun `interceptors should be applied in order`() = runTest {
        val channel = Channel<Update>(Channel.UNLIMITED)
        val updateSource = MockTelegramUpdateSource(channel)

        val allCompleted = CompletableDeferred<Unit>()
        val interceptorLog = mutableListOf<String>()

        val interceptor1: TelegramEventInterceptor = { context ->
            interceptorLog.add("before1")
            try {
                this.process(context)
            } finally {
                interceptorLog.add("after1")
                if (interceptorLog.size == 5) {
                    allCompleted.complete(Unit)
                }
            }
        }

        val interceptor2: TelegramEventInterceptor = { context ->
            interceptorLog.add("before2")
            try {
                this.process(context)
            } finally {
                interceptorLog.add("after2")
            }
        }

        val dispatcher = SimpleTelegramEventDispatcher {
            interceptorLog.add("handler")
        }

        val app = TelegramBotApplication(
            client = fakeClient,
            updateSource = updateSource,
            interceptors = listOf(interceptor1, interceptor2),
            eventDispatcher = dispatcher
        )

        app.start()

        // Send an update
        channel.send(createTestUpdate(1, "test"))

        // Wait for processing
        allCompleted.await()

        // Close channel and use stopSuspend to ensure complete shutdown
        channel.close()
        app.stopSuspend(ZERO)

        // Interceptors should be applied in onion model order
        assertEquals(
            listOf("before1", "before2", "handler", "after2", "after1"),
            interceptorLog
        )
    }

    @Test
    fun `stopSuspend should wait for handler launched coroutines to complete`() = runTest {
        val channel = Channel<Update>(Channel.UNLIMITED)
        val updateSource = MockTelegramUpdateSource(channel)
        val handlerCoroutineStarted = CompletableDeferred<Unit>()
        val handlerCoroutineCanComplete = CompletableDeferred<Unit>()
        val handlerCoroutineCompleted = CompletableDeferred<Unit>()
        val handlerMainCompleted = CompletableDeferred<Unit>()

        val dispatcher = SimpleTelegramEventDispatcher {
            // Launch a child coroutine in the handler scope
            launch {
                handlerCoroutineStarted.complete(Unit)
                // Wait until signaled to complete
                handlerCoroutineCanComplete.await()
                handlerCoroutineCompleted.complete(Unit)
            }
            handlerMainCompleted.complete(Unit)
        }

        val app = TelegramBotApplication(
            client = fakeClient,
            updateSource = updateSource,
            interceptors = emptyList(),
            eventDispatcher = dispatcher
        )

        app.start()

        // Send an update to trigger the handler
        channel.send(createTestUpdate(1, "test"))

        // Wait for handler's child coroutine to start and handler main to complete
        handlerCoroutineStarted.await()
        handlerMainCompleted.await()

        // Close channel so the update source can complete naturally
        channel.close()

        // Start stopSuspend in a separate coroutine
        val stopSuspendCompleted = CompletableDeferred<Unit>()
        launch {
            app.stopSuspend(ZERO)
            stopSuspendCompleted.complete(Unit)
        }

        // At this point, stopSuspend should be waiting for the handler's child coroutine
        // which is blocked on handlerCoroutineCanComplete

        // Allow the handler's child coroutine to complete
        handlerCoroutineCanComplete.complete(Unit)

        // Wait for the child coroutine to actually complete
        handlerCoroutineCompleted.await()

        // Now stopSuspend should complete
        stopSuspendCompleted.await()
    }

    @Test
    fun `interceptor can short-circuit processing`() = runTest {
        val channel = Channel<Update>(Channel.UNLIMITED)
        val updateSource = MockTelegramUpdateSource(channel)

        val interceptorCompleted = CompletableDeferred<Unit>()
        val interceptorLog = mutableListOf<String>()

        // Interceptor that doesn't call process(), short-circuiting the pipeline
        val shortCircuitInterceptor: TelegramEventInterceptor = {
            interceptorLog.add("short-circuit")
            interceptorCompleted.complete(Unit)
            // Not calling this.process(context) means the dispatcher won't be called
        }

        val dispatcher = SimpleTelegramEventDispatcher {
            interceptorLog.add("handler")
        }

        val app = TelegramBotApplication(
            client = fakeClient,
            updateSource = updateSource,
            interceptors = listOf(shortCircuitInterceptor),
            eventDispatcher = dispatcher
        )

        app.start()

        // Send an update
        channel.send(createTestUpdate(1, "test"))

        // Wait for processing
        interceptorCompleted.await()

        // Close channel and use stopSuspend to ensure complete shutdown
        channel.close()
        app.stopSuspend(ZERO)

        // Only the interceptor should have run, not the handler
        assertEquals(listOf("short-circuit"), interceptorLog)
    }

    // ==================== Exception-triggered shutdown tests ====================

    /**
     * Test that when updateSource.start() throws an exception,
     * the application triggers graceful shutdown.
     */
    @Test
    fun `should trigger graceful shutdown when updateSource throws exception`() = runTest {
        // Arrange
        val exceptionToThrow = RuntimeException("Simulated update source failure")
        val stopCalled = atomic(false)
        val onFinalizeCalled = atomic(false)

        val mockSource = object : TelegramUpdateSource {
            override suspend fun start(consume: suspend (Update) -> Unit) {
                throw exceptionToThrow
            }

            override suspend fun stop(gracePeriod: Duration) {
                stopCalled.value = true
            }

            override suspend fun onFinalize() {
                onFinalizeCalled.value = true
            }
        }

        val app = TelegramBotApplication(
            client = fakeClient,
            updateSource = mockSource,
            interceptors = emptyList(),
            eventDispatcher = SimpleTelegramEventDispatcher { },
        )

        // Act
        app.start()

        // Wait for shutdown to complete by waiting on the stop job
        app.stop().join()

        // Assert
        assertTrue(stopCalled.value, "stop() should be called after exception")
        assertTrue(onFinalizeCalled.value, "onFinalize() should be called during shutdown")
    }

    /**
     * Test that when updateSource.start() throws an exception during update processing,
     * the application triggers graceful shutdown.
     */
    @Test
    fun `should trigger graceful shutdown when exception thrown during update processing`() = runTest {
        // Arrange
        val exceptionToThrow = RuntimeException("Simulated processing failure")
        val stopCalled = atomic(false)
        val onFinalizeCalled = atomic(false)
        val updates = Channel<Update>()
        var updateCount = 0

        val mockSource = object : TelegramUpdateSource {
            override suspend fun start(consume: suspend (Update) -> Unit) {
                for (update in updates) {
                    if (updateCount == 1) {
                        throw exceptionToThrow
                    }
                    consume(update)
                    updateCount++
                }
            }

            override suspend fun stop(gracePeriod: Duration) {
                stopCalled.value = true
                updates.close()
            }

            override suspend fun onFinalize() {
                onFinalizeCalled.value = true
            }
        }

        val app = TelegramBotApplication(
            client = fakeClient,
            updateSource = mockSource,
            interceptors = emptyList(),
            eventDispatcher = SimpleTelegramEventDispatcher { },
        )

        // Act
        app.start()

        // Send updates
        updates.send(createTestUpdate(1L, "first"))
        updates.send(createTestUpdate(2L, "second"))

        // Wait for shutdown to complete
        app.stop().join()

        // Assert
        assertTrue(stopCalled.value, "stop() should be called after exception")
        assertTrue(onFinalizeCalled.value, "onFinalize() should be called during shutdown")
    }

    /**
     * Test that calling stop() from within a handler doesn't cause deadlock
     * on a single-threaded dispatcher.
     */
    @Test
    fun `should not deadlock when stop called from handler on single thread dispatcher`() = runTest {
        // Arrange
        val stopCalled = atomic(false)
        val onFinalizeCalled = atomic(false)
        val handlerInvoked = CompletableDeferred<Unit>()
        lateinit var appRef: TelegramBotApplication

        val mockSource = object : TelegramUpdateSource {
            override suspend fun start(consume: suspend (Update) -> Unit) {
                // Send an update directly within start() to ensure the handler is invoked
                consume(createTestUpdate(1L, "test"))
            }

            override suspend fun stop(gracePeriod: Duration) {
                stopCalled.value = true
            }

            override suspend fun onFinalize() {
                onFinalizeCalled.value = true
            }
        }

        val dispatcher = SimpleTelegramEventDispatcher { _ ->
            handlerInvoked.complete(Unit)
            // Call stop from within handler - this should not deadlock
            appRef.stop(ZERO)
        }

        val app = TelegramBotApplication(
            client = fakeClient,
            updateSource = mockSource,
            interceptors = emptyList(),
            eventDispatcher = dispatcher,
        )
        appRef = app

        // Act
        app.start()

        // Wait for handler to be invoked (confirms update was processed)
        handlerInvoked.await()

        // Wait for shutdown to complete
        app.stop().join()

        // Assert
        assertTrue(stopCalled.value, "stop() should be called")
        assertTrue(onFinalizeCalled.value, "onFinalize() should be called")
    }

    /**
     * Test that when updateSource throws CancellationException,
     * graceful shutdown is still triggered.
     */
    @Test
    fun `should trigger graceful shutdown when updateSource throws CancellationException`() = runTest {
        // Arrange
        val stopCalled = atomic(false)
        val onFinalizeCalled = atomic(false)

        val mockSource = object : TelegramUpdateSource {
            override suspend fun start(consume: suspend (Update) -> Unit) {
                throw CancellationException("Simulated cancellation")
            }

            override suspend fun stop(gracePeriod: Duration) {
                stopCalled.value = true
            }

            override suspend fun onFinalize() {
                onFinalizeCalled.value = true
            }
        }

        val app = TelegramBotApplication(
            client = fakeClient,
            updateSource = mockSource,
            interceptors = emptyList(),
            eventDispatcher = SimpleTelegramEventDispatcher { },
        )

        // Act
        app.start()

        // Wait for shutdown to complete
        app.stop().join()

        // Assert - CancellationException should also trigger graceful shutdown
        assertTrue(stopCalled.value, "stop() should be called")
        assertTrue(onFinalizeCalled.value, "onFinalize() should be called")
    }

    /**
     * Test that application state transitions correctly during exception-triggered shutdown.
     */
    @Test
    fun `should transition state correctly during exception shutdown`() = runTest {
        // Arrange
        val states = mutableListOf<String>()
        val exceptionToThrow = RuntimeException("Test exception")

        val mockSource = object : TelegramUpdateSource {
            override suspend fun start(consume: suspend (Update) -> Unit) {
                throw exceptionToThrow
            }

            override suspend fun stop(gracePeriod: Duration) {
                states.add("stop_called")
            }

            override suspend fun onFinalize() {
                states.add("onFinalize_called")
            }
        }

        val app = TelegramBotApplication(
            client = fakeClient,
            updateSource = mockSource,
            interceptors = emptyList(),
            eventDispatcher = SimpleTelegramEventDispatcher { },
        )

        // Act
        app.start()

        // Wait for shutdown to complete
        app.stop().join()

        // Assert
        assertTrue("stop_called" in states, "stop() should be called")
        assertTrue("onFinalize_called" in states, "onFinalize() should be called")
    }

    /**
     * Test that multiple exceptions don't cause issues.
     */
    @Test
    fun `should handle multiple rapid exceptions gracefully`() = runTest {
        // Arrange
        var startCallCount = 0
        val stopCalled = atomic(false)
        val onFinalizeCalled = atomic(false)

        val mockSource = object : TelegramUpdateSource {
            override suspend fun start(consume: suspend (Update) -> Unit) {
                startCallCount++
                throw RuntimeException("Exception $startCallCount")
            }

            override suspend fun stop(gracePeriod: Duration) {
                stopCalled.value = true
            }

            override suspend fun onFinalize() {
                onFinalizeCalled.value = true
            }
        }

        val app = TelegramBotApplication(
            client = fakeClient,
            updateSource = mockSource,
            interceptors = emptyList(),
            eventDispatcher = SimpleTelegramEventDispatcher { },
        )

        // Act
        app.start()

        // Wait for shutdown to complete
        app.stop().join()

        // Assert - start should only be called once (bot can only start once)
        assertEquals(1, startCallCount, "start() should only be called once")
        assertTrue(stopCalled.value, "stop() should be called")
        assertTrue(onFinalizeCalled.value, "onFinalize() should be called")
    }

    // ==================== Race condition tests ====================

    /**
     * Test that concurrent stop() calls on a NEW application all return immediately completed jobs.
     * This tests the race condition fix where multiple stop() calls before start() should all succeed.
     */
    @Test
    fun `concurrent stop calls on NEW application should all return completed jobs`() = runTest {
        val channel = Channel<Update>(Channel.UNLIMITED)
        val updateSource = MockTelegramUpdateSource(channel)
        val dispatcher = SimpleTelegramEventDispatcher { }

        val app = TelegramBotApplication(
            client = fakeClient,
            updateSource = updateSource,
            interceptors = emptyList(),
            eventDispatcher = dispatcher
        )

        // Launch multiple concurrent stop() calls before start()
        val stopJobs = (1..10).map {
            launch {
                app.stop(ZERO)
            }
        }

        // All stop jobs should complete
        stopJobs.joinAll()

        // All should be completed
        assertTrue(stopJobs.all { it.isCompleted }, "All stop jobs should be completed")

        channel.close()
    }

    /**
     * Test that calling start() after stop() on a NEW application should fail.
     * This verifies the state transition NEW -> STOPPING is irreversible.
     */
    @Test
    fun `start after stop on NEW application should throw`() = runTest {
        val channel = Channel<Update>(Channel.UNLIMITED)
        val updateSource = MockTelegramUpdateSource(channel)
        val dispatcher = SimpleTelegramEventDispatcher { }

        val app = TelegramBotApplication(
            client = fakeClient,
            updateSource = updateSource,
            interceptors = emptyList(),
            eventDispatcher = dispatcher
        )

        // Stop before start
        val stopJob = app.stop(ZERO)
        assertTrue(stopJob.isCompleted, "Stop on NEW should return completed job")

        // Now try to start - should fail because state is STOPPING
        val exception = assertFailsWith<IllegalStateException> {
            app.start()
        }

        assertEquals("Bot can only be started once", exception.message)

        channel.close()
    }

    /**
     * Test race condition between start() and stop() calls.
     * Either:
     * - start() wins: state becomes RUNNING, then stop() triggers normal shutdown
     * - stop() wins: state becomes STOPPING, then start() throws exception
     */
    @Test
    fun `race between start and stop should be handled correctly`() = runTest {
        repeat(10) {
            val channel = Channel<Update>(Channel.UNLIMITED)
            val updateSource = MockTelegramUpdateSource(channel)
            val dispatcher = SimpleTelegramEventDispatcher { }

            val app = TelegramBotApplication(
                client = fakeClient,
                updateSource = updateSource,
                interceptors = emptyList(),
                eventDispatcher = dispatcher
            )

            val startResult = CompletableDeferred<Result<Unit>>()
            val stopResult = CompletableDeferred<Job>()

            // Launch start and stop concurrently
            launch {
                try {
                    app.start()
                    startResult.complete(Result.success(Unit))
                } catch (e: IllegalStateException) {
                    startResult.complete(Result.failure(e))
                }
            }

            launch {
                stopResult.complete(app.stop(ZERO))
            }

            val startOutcome = startResult.await()
            val stopJob = stopResult.await()

            if (startOutcome.isSuccess) {
                // start() won: app is running, stop() should trigger normal shutdown
                // Wait for shutdown
                channel.close()
                stopJob.join()
            } else {
                // stop() won: state is STOPPING, start() should have thrown
                assertTrue(startOutcome.isFailure, "start should have failed when stop won the race")
                val exception = startOutcome.exceptionOrNull()
                assertTrue(exception is IllegalStateException, "Should throw IllegalStateException")
                assertEquals("Bot can only be started once", exception.message)
                assertTrue(stopJob.isCompleted, "Stop job should be completed immediately when stop won")
                channel.close()
            }
        }
    }

    /**
     * Test graceful shutdown when LongPollingTelegramUpdateSource fails with fastFail enabled.
     *
     * Uses an invalid bot token which will always fail to authenticate,
     * with fastFail=true to make it throw an exception immediately.
     * This tests the real-world scenario where the update source fails due to
     * network/authentication errors.
     */
    @Test
    fun `should trigger graceful shutdown when real LongPollingTelegramUpdateSource fails with fastFail`() = runTest {
        // Arrange - use invalid bot token that will always fail
        val longPollingSource = LongPollingTelegramUpdateSource(
            client = fakeClient,
            fastFail = true,  // Make it throw exception instead of retrying
            getUpdatesTimeout = 0,  // Short timeout to fail faster
        )

        val app = TelegramBotApplication(
            client = fakeClient,
            updateSource = longPollingSource,
            interceptors = emptyList(),
            eventDispatcher = SimpleTelegramEventDispatcher { },
        )

        // Act
        app.start()

        // Wait for shutdown
        app.join()

        // Assert - the app should have stopped gracefully
        // If we get here without timeout, the shutdown worked correctly
    }
}
