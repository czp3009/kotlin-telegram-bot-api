package com.hiczp.telegram.bot.application.updatesource.webhook

import com.hiczp.telegram.bot.application.exception.TelegramBotShuttingDownException
import com.hiczp.telegram.bot.application.updatesource.TelegramUpdateSource
import com.hiczp.telegram.bot.protocol.model.Update
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.json.Json

private val logger = KotlinLogging.logger {}

/**
 * Webhook update source that receives updates via HTTP webhook.
 *
 * This implementation starts an embedded Ktor server to receive webhook requests from Telegram.
 * All updates are processed concurrently without waiting for previous ones to complete.
 *
 * Exception semantics:
 * - [TelegramBotShuttingDownException]: Signals framework shutdown. Silently cancels individual tasks.
 * - [CancellationException]: Signals business logic failure (e.g., timeout). The update is skipped.
 * - [Exception]: Normal business exception. The update is skipped to prevent infinite loops.
 *
 * @param TEngine The type of application engine.
 * @param TConfiguration The type of engine configuration.
 * @param applicationEngineFactory The Ktor application engine factory to use (e.g., Netty, CIO).
 * @param port The port to listen on. Defaults to 80.
 * @param path The webhook endpoint path. Defaults to "/".
 * @param host The host address to bind to. Defaults to "0.0.0.1".
 * @param configureEngine Configuration lambda for the application engine. Use this to configure SSL,
 *   connection settings, and other engine-specific options.
 * @param configureApplication Additional configuration for the Ktor application.
 */
open class WebhookTelegramUpdateSource<out TEngine : ApplicationEngine, TConfiguration : ApplicationEngine.Configuration>(
    private val applicationEngineFactory: ApplicationEngineFactory<TEngine, TConfiguration>,
    private val port: Int = 80,
    private val path: String = "/",
    private val host: String = "0.0.0.1",
    private val configureEngine: TConfiguration.() -> Unit = {},
    private val configureApplication: Application.() -> Unit = {},
) : TelegramUpdateSource {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        explicitNulls = false
    }

    private val isRunning = atomic(false)

    // Use Channel instead of CompletableDeferred for better cross-platform support
    private val stopChannel = Channel<Unit>(capacity = 1)
    private var server: EmbeddedServer<TEngine, TConfiguration>? = null
    private var processingScope: CoroutineScope? = null

    /**
     * Start the webhook server.
     *
     * The [consume] callback processes each update concurrently. If the application is shutting down,
     * [consume] should throw [TelegramBotShuttingDownException], which will silently cancel individual tasks.
     *
     * This method can only be called once per instance.
     *
     * @param consume Suspended function to process each update.
     * @throws IllegalStateException if this method has already been called.
     */
    override suspend fun start(consume: suspend (Update) -> Unit) {
        check(isRunning.compareAndSet(expect = false, update = true)) {
            "${this::class.simpleName} can only be started once"
        }

        // Create processing scope lazily when start is called
        processingScope = CoroutineScope(SupervisorJob())

        logger.debug { "${this::class.simpleName} started on $host:$port$path" }

        val scope = processingScope!!
        val updateHandler: suspend (Update) -> Unit = { update ->
            scope.launch {
                try {
                    consume(update)
                } catch (_: TelegramBotShuttingDownException) {
                    // Application is shutting down. Silently cancel this task.
                } catch (e: CancellationException) {
                    if (isActive) {
                        logger.warn(e) { "Update processing cancelled: $update" }
                    }
                    throw e
                } catch (e: Throwable) {
                    logger.error(e) { "Update process error: $update" }
                }
            }
        }

        server = embeddedServer(
            factory = applicationEngineFactory,
            configure = {
                connector {
                    this.port = this@WebhookTelegramUpdateSource.port
                    this.host = this@WebhookTelegramUpdateSource.host
                }
                configureEngine()
            },
        ) {
            configureApplication()
            routing {
                post(path) {
                    try {
                        val body = call.receiveText()
                        val update = json.decodeFromString<Update>(body)
                        updateHandler(update)
                        call.respondText("OK", status = HttpStatusCode.OK)
                    } catch (e: Throwable) {
                        logger.error(e) { "Failed to parse update from webhook" }
                        call.respondText(
                            "Bad Request",
                            status = HttpStatusCode.BadRequest
                        )
                    }
                }
            }
        }.startSuspend(wait = false)

        // Suspend until stop signal is received via channel
        stopChannel.receive()
    }

    /**
     * Trigger source shutdown.
     *
     * Stops the embedded server and cancels the processing scope.
     * Called by [com.hiczp.telegram.bot.application.TelegramBotApplication.stop] before waiting for handlers.
     */
    override suspend fun stop() {
        logger.debug { "Received stop signal, shutting down webhook server..." }
        // Only perform shutdown once
        if (!isRunning.compareAndSet(expect = true, update = false)) {
            return  // Already stopped
        }
        // Cancel and wait for all children in processing scope to complete first
        processingScope?.let { scope ->
            scope.cancel()
            scope.coroutineContext.job.join()
        }
        // Signal start() to complete so it can return
        stopChannel.trySend(Unit)
        stopChannel.close()
        // Stop server last - use grace period for clean shutdown
        // Note: stop() with grace period is non-suspending but we need to give it time
        server?.stop(gracePeriodMillis = 1000, timeoutMillis = 2000)
    }

    /**
     * Perform final cleanup.
     *
     * For webhook mode, no additional cleanup is needed since Telegram will stop sending
     * updates when the server is stopped.
     */
    override suspend fun onFinalize() {
        logger.debug { "Webhook update source finalized" }
    }
}
