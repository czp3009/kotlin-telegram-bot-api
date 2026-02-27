package com.hiczp.telegram.bot.application.test

import com.hiczp.telegram.bot.application.TelegramBotApplication
import com.hiczp.telegram.bot.application.dispatcher.SimpleTelegramEventDispatcher
import com.hiczp.telegram.bot.application.interceptor.TelegramEventInterceptor
import com.hiczp.telegram.bot.application.updatesource.MockTelegramUpdateSource
import com.hiczp.telegram.bot.client.TelegramBotClient
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.model.Chat
import com.hiczp.telegram.bot.protocol.model.Message
import com.hiczp.telegram.bot.protocol.model.Update
import com.hiczp.telegram.bot.protocol.model.User
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.*
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class TelegramBotApplicationLifecycleTest {
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
        app.stop(100.milliseconds).join()
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
        val stopJob1 = app.stop(100.milliseconds)
        val stopJob2 = app.stop(100.milliseconds)
        val stopJob3 = app.stop(100.milliseconds)

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
        val stopJob = app.stop(100.milliseconds)

        // Should complete immediately
        assertTrue(stopJob.isCompleted)
        channel.close()
    }

    @Test
    fun `stop should call updateSource onFinalize`() = runTest {
        val finalizeCalled = CompletableDeferred<Boolean>()
        val sourceStarted = CompletableDeferred<Unit>()

        /**
         * A MockTelegramUpdateSource that tracks whether onFinalize() was called.
         */
        class FinalizeTrackingMockUpdateSource(
            channel: Channel<Update>,
            private val startedSignal: CompletableDeferred<Unit>,
            private val finalizeSignal: CompletableDeferred<Boolean>
        ) : MockTelegramUpdateSource(channel) {
            override suspend fun start(consume: suspend (Update) -> Unit) {
                startedSignal.complete(Unit)
                super.start(consume)
            }

            override suspend fun onFinalize() {
                finalizeSignal.complete(true)
            }
        }

        val channel = Channel<Update>(Channel.UNLIMITED)
        val updateSource = FinalizeTrackingMockUpdateSource(channel, sourceStarted, finalizeCalled)
        val dispatcher = SimpleTelegramEventDispatcher { }

        val app = TelegramBotApplication(
            client = fakeClient,
            updateSource = updateSource,
            interceptors = emptyList(),
            eventDispatcher = dispatcher
        )

        app.start()

        // Wait for the update source to actually start
        sourceStarted.await()

        // Use stopSuspend to ensure complete shutdown
        app.stopSuspend(100.milliseconds)
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
        val stopJob = app.stop(100.milliseconds)
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
        app.stopSuspend(100.milliseconds)

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
        app.stop(100.milliseconds).join()
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
        app.stopSuspend(100.milliseconds)

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
            app.stopSuspend(1.seconds)
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
        app.stopSuspend(100.milliseconds)

        // Only the interceptor should have run, not the handler
        assertEquals(listOf("short-circuit"), interceptorLog)
    }
}
