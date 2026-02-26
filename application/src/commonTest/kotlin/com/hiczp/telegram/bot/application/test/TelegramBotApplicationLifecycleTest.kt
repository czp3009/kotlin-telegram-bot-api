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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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

        // Give it a moment to start
        delay(50.milliseconds)

        // Stop the app
        app.stop(100.milliseconds).join()
        channel.close()

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
        delay(50.milliseconds)

        // Call stop multiple times
        val stopJob1 = app.stop(100.milliseconds)
        val stopJob2 = app.stop(100.milliseconds)
        val stopJob3 = app.stop(100.milliseconds)

        // All should complete without error
        stopJob1.join()
        stopJob2.join()
        stopJob3.join()

        channel.close()
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
        /**
         * A MockTelegramUpdateSource that tracks whether onFinalize() was called.
         */
        class FinalizeTrackingMockUpdateSource(
            channel: Channel<Update>
        ) : MockTelegramUpdateSource(channel) {
            var finalizeCalled = false
                private set

            override suspend fun onFinalize() {
                finalizeCalled = true
            }
        }

        val channel = Channel<Update>(Channel.UNLIMITED)
        val updateSource = FinalizeTrackingMockUpdateSource(channel)
        val dispatcher = SimpleTelegramEventDispatcher { }

        val app = TelegramBotApplication(
            client = fakeClient,
            updateSource = updateSource,
            interceptors = emptyList(),
            eventDispatcher = dispatcher
        )

        app.start()
        delay(50.milliseconds)

        assertFalse(updateSource.finalizeCalled)

        app.stop(100.milliseconds).join()
        channel.close()

        assertTrue(updateSource.finalizeCalled)
    }

    @Test
    fun `graceful shutdown should cancel long running handler after timeout`() = runTest {
        var handlerStarted = false
        var handlerCompleted = false

        val channel = Channel<Update>(Channel.UNLIMITED)
        val updateSource = MockTelegramUpdateSource(channel)

        val dispatcher = SimpleTelegramEventDispatcher {
            handlerStarted = true
            // Use a very long delay
            delay(60.seconds)
            handlerCompleted = true
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
        while (!handlerStarted) {
            delay(10.milliseconds)
        }

        // Small delay to ensure handler is fully in delay()
        delay(100.milliseconds)

        // Verify handler hasn't completed yet
        assertFalse(handlerCompleted)

        // Initiate graceful shutdown with very short timeout
        val stopJob = app.stop(200.milliseconds)
        stopJob.join()
        channel.close()

        // After shutdown, handler should NOT have completed normally
        // (it was cancelled due to timeout)
        assertFalse(handlerCompleted)
    }

    @Test
    fun `should process updates correctly`() = runTest {
        val channel = Channel<Update>(Channel.UNLIMITED)
        val updateSource = MockTelegramUpdateSource(channel)

        val processedTexts = mutableListOf<String>()

        val dispatcher = SimpleTelegramEventDispatcher { context ->
            val text = (context.event as MessageEvent).message.text ?: ""
            processedTexts.add(text)
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
        while (processedTexts.size < 3) {
            delay(10.milliseconds)
        }

        app.stop(100.milliseconds).join()
        channel.close()

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

        var joinCompleted = false

        val joinJob = launch {
            app.join()
            joinCompleted = true
        }

        delay(100.milliseconds)

        // join should not have completed yet
        assertFalse(joinCompleted)

        // Stop the app
        app.stop(100.milliseconds).join()
        channel.close()

        // Now join should complete
        joinJob.join()
        assertTrue(joinCompleted)
    }

    @Test
    fun `interceptors should be applied in order`() = runTest {
        val channel = Channel<Update>(Channel.UNLIMITED)
        val updateSource = MockTelegramUpdateSource(channel)

        val interceptorLog = mutableListOf<String>()

        val interceptor1: TelegramEventInterceptor = { context ->
            interceptorLog.add("before1")
            try {
                this.process(context)
            } finally {
                interceptorLog.add("after1")
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
        while (interceptorLog.size < 5) {
            delay(10.milliseconds)
        }

        app.stop(100.milliseconds).join()
        channel.close()

        // Interceptors should be applied in onion model order
        assertEquals(
            listOf("before1", "before2", "handler", "after2", "after1"),
            interceptorLog
        )
    }

    @Test
    fun `interceptor can short-circuit processing`() = runTest {
        val channel = Channel<Update>(Channel.UNLIMITED)
        val updateSource = MockTelegramUpdateSource(channel)

        val interceptorLog = mutableListOf<String>()

        // Interceptor that doesn't call process(), short-circuiting the pipeline
        val shortCircuitInterceptor: TelegramEventInterceptor = {
            interceptorLog.add("short-circuit")
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
        while (interceptorLog.isEmpty()) {
            delay(10.milliseconds)
        }

        app.stop(100.milliseconds).join()
        channel.close()

        // Only the interceptor should have run, not the handler
        assertEquals(listOf("short-circuit"), interceptorLog)
    }
}
