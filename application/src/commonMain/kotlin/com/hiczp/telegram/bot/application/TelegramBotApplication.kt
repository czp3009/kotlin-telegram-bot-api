package com.hiczp.telegram.bot.application

import com.hiczp.telegram.bot.application.dispatcher.TelegramEventDispatcher
import com.hiczp.telegram.bot.application.interceptor.TelegramEventInterceptor
import com.hiczp.telegram.bot.application.pipeline.TelegramEventPipeline
import com.hiczp.telegram.bot.application.updatesource.TelegramUpdateSource
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import com.hiczp.telegram.bot.protocol.event.toTelegramBotEvent
import com.hiczp.telegram.bot.protocol.exception.UnrecognizedUpdateException
import com.hiczp.telegram.bot.protocol.model.Update
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.job

private val logger = KotlinLogging.logger {}

/**
 * The top-level entry point for a Telegram bot application.
 *
 * This class orchestrates the bot lifecycle using an "onion model" architecture:
 * - [updateSource] fetches updates from Telegram
 * - [interceptors] form middleware layers around the dispatcher
 * - [eventDispatcher] routes events to business logic handlers
 *
 * The update flows through: updateSource -> Update-to-Event conversion -> interceptors (outer to inner) -> eventDispatcher
 *
 * Key features:
 * - Structured concurrency: Uses `coroutineScope` to tie the bot lifecycle
 *   to the caller's coroutine scope
 * - Sequential processing: Updates are processed one at a time, preserving
 *   order for user conversations
 * - Error isolation: Business logic exceptions don't crash the update source
 * - Graceful shutdown: Ensures in-flight updates complete before exiting
 * - Unrecognized update handling: Updates that cannot be converted to events are skipped
 *
 * Example usage:
 * ```kotlin
 * val client = TelegramBotClient(engine, "BOT_TOKEN")
 * val updateSource = LongPollingTelegramUpdateSource(client)
 * val eventDispatcher = TelegramEventDispatcher()
 *
 * val interceptors = listOf<TelegramEventInterceptor> { event ->
 *     println("Received: ${event.updateId}")
 *     this.process(event)
 * }
 *
 * val app = TelegramBotApplication(updateSource, interceptors, eventDispatcher)
 *
 * // Add business logic
 * eventDispatcher.addHandler { event ->
 *     println("Processing: ${event.updateId}")
 *     true
 * }
 *
 * // Start the bot (suspends until shutdown)
 * app.start()
 * ```
 *
 * @param updateSource The update source for fetching updates.
 * @param interceptors List of interceptors to apply, in order from outermost to innermost.
 * @param eventDispatcher The dispatcher that routes events to business logic handlers.
 */
class TelegramBotApplication(
    updateSource: TelegramUpdateSource,
    interceptors: List<TelegramEventInterceptor>,
    eventDispatcher: TelegramEventDispatcher,
) {
    private val source = updateSource
    private val pipeline = TelegramEventPipeline(interceptors, eventDispatcher)

    private var job: Job? = null
    private var isRunning: Boolean = false

    /**
     * Whether the application is currently running.
     */
    val running: Boolean
        get() = isRunning

    /**
     * Start the bot application.
     *
     * This function suspends until [shutdown] is called or the enclosing
     * coroutine scope is cancelled. Updates are processed sequentially
     * to maintain ordering guarantees.
     *
     * If called while already running, returns immediately.
     */
    suspend fun start(): Unit = coroutineScope {
        if (isRunning) return@coroutineScope
        isRunning = true
        job = coroutineContext.job
        try {
            source.start { update: Update ->
                val event: TelegramBotEvent = try {
                    update.toTelegramBotEvent()
                } catch (e: UnrecognizedUpdateException) {
                    logger.warn { "Skipping unrecognized update: ${update.updateId}" }
                    return@start
                }
                try {
                    pipeline.process(event)
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    // Business exception barrier: prevent single event failures
                    // from crashing the update source
                }
            }
        } finally {
            isRunning = false
        }
    }

    /**
     * Gracefully shut down the bot application.
     *
     * This performs a two-phase shutdown:
     * 1. Signals the source to stop fetching new updates and send final ACK
     * 2. Waits for any currently processing update to complete
     *
     * If the application is not running, returns immediately.
     */
    suspend fun shutdown() {
        if (!isRunning) return
        // Phase 1: Signal source to stop fetching and send final ACK
        source.stop()
        // Phase 2: Wait for in-flight update to complete
        job?.join()
    }
}
