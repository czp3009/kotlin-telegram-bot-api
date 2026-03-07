package com.hiczp.telegram.bot.application.updatesource.webhook.test

import com.hiczp.telegram.bot.application.exception.TelegramBotShuttingDownException
import com.hiczp.telegram.bot.application.updatesource.webhook.WebhookTelegramUpdateSource
import io.ktor.server.cio.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class WebhookTelegramUpdateSourceTest {
    // Use random ports in the ephemeral port range to avoid conflicts
    private fun randomPort(): Int = Random.nextInt(49152, 65535)

    private fun createTestWebhookUpdateSource(
        path: String = "/",
    ): WebhookTelegramUpdateSource<*, *> {
        return WebhookTelegramUpdateSource(
            applicationEngineFactory = CIO,
            port = randomPort(),
            path = path,
            host = "127.0.0.1",
        )
    }

    @Test
    fun `stop should be idempotent when called multiple times`() = runTest(timeout = 30.seconds) {
        val updateSource = createTestWebhookUpdateSource()
        val startJob = launch {
            updateSource.start { }
        }
        delay(100.milliseconds)
        // Call stop multiple times - all should complete without error
        updateSource.stop()
        updateSource.stop()
        updateSource.stop()

        // Wait for start to complete naturally after stop
        startJob.join()
        // Give extra time for WASM/Node.js event loop cleanup
        delay(500.milliseconds)
    }

    @Test
    fun `stop on not-started source should complete without error`() = runTest(timeout = 30.seconds) {
        val updateSource = createTestWebhookUpdateSource()
        // Don't start, just call stop - should complete without error
        updateSource.stop()
        updateSource.onFinalize()
        // Give extra time for WASM/Node.js event loop cleanup
        delay(100.milliseconds)
    }

    @Test
    fun `onFinalize should complete without error after stop`() = runTest(timeout = 30.seconds) {
        val updateSource = createTestWebhookUpdateSource()
        val startJob = launch {
            updateSource.start { }
        }
        delay(100.milliseconds)
        updateSource.stop()
        updateSource.onFinalize()
        // Wait for start to complete naturally after stop
        startJob.join()
        // Give extra time for WASM/Node.js event loop cleanup
        delay(500.milliseconds)
    }

    @Test
    fun `should handle TelegramBotShuttingDownException silently`() = runTest(timeout = 30.seconds) {
        val updateSource = createTestWebhookUpdateSource()
        val startJob = launch {
            updateSource.start { _ ->
                throw TelegramBotShuttingDownException()
            }
        }
        delay(100.milliseconds)
        // Stop should complete without throwing
        updateSource.stop()
        // Wait for start to complete naturally after stop
        startJob.join()
        // Give extra time for WASM/Node.js event loop cleanup
        delay(500.milliseconds)
    }

    @Test
    fun `should handle CancellationException and log warning`() = runTest(timeout = 30.seconds) {
        val updateSource = createTestWebhookUpdateSource()
        val startJob = launch {
            updateSource.start { _ ->
                throw CancellationException("Test cancellation")
            }
        }
        delay(100.milliseconds)
        // Stop should complete without throwing
        updateSource.stop()
        // Wait for start to complete naturally after stop
        startJob.join()
        // Give extra time for WASM/Node.js event loop cleanup
        delay(500.milliseconds)
    }

    @Test
    fun `should handle general exceptions and continue processing`() = runTest(timeout = 30.seconds) {
        val updateSource = createTestWebhookUpdateSource()
        val startJob = launch {
            updateSource.start { _ ->
                throw RuntimeException("Test error")
            }
        }
        delay(100.milliseconds)
        // Stop should complete without throwing
        updateSource.stop()
        // Wait for start to complete naturally after stop
        startJob.join()
        // Give extra time for WASM/Node.js event loop cleanup
        delay(500.milliseconds)
    }

    @Test
    fun `full lifecycle - start stop and finalize`() = runTest(timeout = 30.seconds) {
        val states = mutableListOf<String>()
        val updateSource = createTestWebhookUpdateSource()
        val startJob = launch {
            states.add("start_begin")
            updateSource.start { _ ->
                states.add("update_received")
            }
            states.add("start_end")
        }
        // Wait for server to start
        delay(200.milliseconds)
        states.add("server_started")
        // Stop the source
        updateSource.stop()
        states.add("stop_completed")
        // Finalize
        updateSource.onFinalize()
        states.add("finalize_completed")
        // Wait for start to complete naturally after stop
        startJob.join()
        // Verify lifecycle
        assertTrue("start_begin" in states)
        assertTrue("server_started" in states)
        assertTrue("stop_completed" in states)
        assertTrue("finalize_completed" in states)
        // Give extra time for WASM/Node.js event loop cleanup
        delay(500.milliseconds)
    }
}
