package com.hiczp.telegram.bot.application.updatesource.webhook.test

import com.hiczp.telegram.bot.application.updatesource.webhook.WebhookTelegramUpdateSource
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Duration.Companion.seconds

/**
 * Unit tests for [WebhookTelegramUpdateSource] lifecycle management.
 *
 * Tests verify:
 * - State machine transitions: NEW -> RUNNING -> STOPPED
 * - Single start/stop semantics
 * - Graceful shutdown when stop is called during server startup
 */
class WebhookTelegramUpdateSourceTest {
    // ==================== State Machine Tests ====================

    @Test
    fun `start can only be called once`() = runTest {
        val updateSource = createTestUpdateSource()

        // First start should succeed in a separate coroutine
        val firstStartJob = launch {
            updateSource.start { /* no-op */ }
        }

        // Give some time for the server to start transitioning state
        yield()

        // Second start should fail with IllegalStateException
        val exception = assertFailsWith<IllegalStateException> {
            updateSource.start { /* no-op */ }
        }
        assertEquals(exception.message?.contains("can only be started once"), true)

        // Cleanup
        firstStartJob.cancel()
        updateSource.stop(1.seconds)
    }

    @Test
    fun `stop without start should return immediately without error`() = runTest {
        val updateSource = createTestUpdateSource()

        // stop() without start() should not throw and return immediately
        updateSource.stop(1.seconds)
        // If we reach here, the test passed
    }

    @Test
    fun `stop can only be effective once`() = runTest {
        val updateSource = createTestUpdateSource()

        // Start the server
        val startJob = launch {
            updateSource.start { /* no-op */ }
        }

        // Wait for server to be ready by making a request
        // Since we can't easily make HTTP requests in common test, we rely on the
        // fact that stop() waits for startedSignal, which is completed when server starts

        // First stop should work
        updateSource.stop(1.seconds)

        // Second stop should be a no-op (state is already STOPPED)
        updateSource.stop(1.seconds)

        // Cleanup
        startJob.cancel()
    }

    // ==================== Startup/Shutdown Timing Tests ====================

    @Test
    fun `stop should wait for server to start before shutting down`() = runTest {
        val updateSource = createTestUpdateSource()
        val stopCompleted = CompletableDeferred<Unit>()

        // Start the server in a coroutine
        val startJob = launch {
            updateSource.start { /* no-op */ }
        }

        // Immediately call stop (before server fully starts)
        // This should wait for the server to start, then stop it
        val stopJob = launch {
            updateSource.stop(1.seconds)
            stopCompleted.complete(Unit)
        }

        // Wait for stop to complete
        stopCompleted.await()

        // Cleanup
        startJob.cancel()
        stopJob.cancel()
    }

    @Test
    fun `start then stop should complete successfully`() = runTest {
        val updateSource = createTestUpdateSource()

        // Start the server
        val startJob = launch {
            updateSource.start { /* no-op */ }
        }

        // Wait a bit for server to start
        yield()

        // Stop the server
        updateSource.stop(1.seconds)

        // Cleanup
        startJob.cancel()
    }

    // ==================== Concurrent Access Tests ====================

    @Test
    fun `concurrent stop calls should be safe`() = runTest {
        val updateSource = createTestUpdateSource()

        // Start the server
        val startJob = launch {
            updateSource.start { /* no-op */ }
        }

        yield()

        // Launch multiple concurrent stop calls
        val stopJobs = List(5) {
            async {
                updateSource.stop(1.seconds)
            }
        }

        // All should complete without exception
        stopJobs.awaitAll()

        // Cleanup
        startJob.cancel()
    }

    @Test
    fun `start after stop should fail`() = runTest {
        val updateSource = createTestUpdateSource()

        // Start the server
        val startJob = launch {
            updateSource.start { /* no-op */ }
        }

        yield()

        // Stop the server
        updateSource.stop(1.seconds)

        // Try to start again - should fail
        val exception = assertFailsWith<IllegalStateException> {
            updateSource.start { /* no-op */ }
        }
        assertEquals(exception.message?.contains("can only be started once"), true)

        // Cleanup
        startJob.cancel()
    }

    // ==================== Helper Functions ====================

    private fun createTestUpdateSource(
        port: Int = 0, // Use port 0 to let OS assign an available port
    ): WebhookTelegramUpdateSource<CIOApplicationEngine, CIOApplicationEngine.Configuration> {
        return WebhookTelegramUpdateSource(
            applicationEngineFactory = CIO,
            path = "/webhook",
            configureEngine = {
                connector {
                    this.port = port
                    this.host = "127.0.0.1"
                }
            }
        )
    }
}
