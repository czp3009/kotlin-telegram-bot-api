package com.hiczp.telegram.bot.application.updatesource.webhook

import com.hiczp.telegram.bot.application.exception.TelegramBotShuttingDownException
import com.hiczp.telegram.bot.application.updatesource.TelegramUpdateSource
import com.hiczp.telegram.bot.protocol.model.Update
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json
import kotlin.time.Duration

private val logger = KotlinLogging.logger {}

/**
 * Webhook update source that receives updates via HTTP webhook.
 *
 * This implementation starts an embedded Ktor server to receive webhook requests from Telegram.
 * Updates are processed synchronously within the HTTP request handler.
 *
 * Exception semantics:
 * - [CancellationException]: Re-thrown to propagate coroutine cancellation.
 * - [Exception]: Business exceptions are logged and a 200 OK is returned to Telegram to prevent retries.
 *
 * ## Usage Example
 *
 * ```kotlin
 * val updateSource = WebhookTelegramUpdateSource(
 *     applicationEngineFactory = CIO,
 *     path = "/webhook",
 *     configureEngine = {
 *         connector {
 *             host = "0.0.0.0"
 *             port = 8080
 *         }
 *     }
 * )
 * ```
 *
 * @param TEngine The type of application engine.
 * @param TConfiguration The type of engine configuration.
 * @param applicationEngineFactory The Ktor application engine factory to use (e.g., Netty, CIO).
 * @param path The webhook endpoint path. Defaults to "/".
 * @param configureEngine Configuration lambda for the application engine. Use this to configure port,
 *   host, SSL, connection settings, and other engine-specific options.
 * @param configureApplication Additional configuration for the Ktor application.
 * @see <a href="https://ktor.io/docs/server-engines.html">Ktor Server Engines documentation</a>
 */
open class WebhookTelegramUpdateSource<out TEngine : ApplicationEngine, TConfiguration : ApplicationEngine.Configuration>(
    private val applicationEngineFactory: ApplicationEngineFactory<TEngine, TConfiguration>,
    private val path: String = "/",
    private val configureEngine: TConfiguration.() -> Unit = {},
    private val configureApplication: Application.() -> Unit = {},
) : TelegramUpdateSource {
    private val isRunning = atomic(false)
    private var server: EmbeddedServer<TEngine, TConfiguration>? = null

    /**
     * Start the webhook server.
     *
     * The [consume] callback processes each update synchronously within the HTTP request handler.
     *
     * This method can only be called once per instance.
     *
     * @param consume Suspended function to process each update.
     * @throws IllegalStateException if this method has already been called.
     */
    override suspend fun start(consume: suspend (Update) -> Unit) {
        check(isRunning.compareAndSet(expect = false, update = true)) {
            "${this::class.simpleName} already started"
        }

        logger.debug { "${this::class.simpleName} started on path $path" }

        val embeddedServer = embeddedServer(
            factory = applicationEngineFactory,
            configure = configureEngine,
        ) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                    explicitNulls = false
                })
            }
            routing {
                post(path) {
                    val update = try {
                        call.receive<Update>()
                    } catch (e: ContentTransformationException) {
                        logger.error(e) { "Failed to parse Update from webhook" }
                        return@post call.respond(HttpStatusCode.BadRequest)
                    }

                    try {
                        consume(update)
                    } catch (e: TelegramBotShuttingDownException) {
                        throw e
                    } catch (e: CancellationException) {
                        if (isActive) {
                            logger.warn(e) { "Update processing cancelled: $update" }
                        }
                        throw e
                    } catch (e: Throwable) {
                        logger.error(e) { "Update process error: $update" }
                        throw e
                    }
                    call.respond(HttpStatusCode.OK)
                }
            }
            configureApplication()
        }
        server = embeddedServer
        embeddedServer.startSuspend(wait = true)
    }

    /**
     * Trigger source shutdown.
     *
     * Stops the embedded server. All pending handlers will be cancelled via Ktor's lifecycle.
     * Called by [com.hiczp.telegram.bot.application.TelegramBotApplication.stop] before waiting for handlers.
     *
     * @param gracePeriod The grace period for graceful shutdown. Passed to Ktor's stop method
     *   as both gracePeriodMillis and timeoutMillis.
     */
    override suspend fun stop(gracePeriod: Duration) {
        logger.debug { "Received stop signal, shutting down webhook server..." }
        isRunning.value = false
        val gracePeriodMillis = gracePeriod.inWholeMilliseconds
        server?.stop(gracePeriodMillis = gracePeriodMillis, timeoutMillis = gracePeriodMillis)
    }

    /**
     * Perform final cleanup.
     *
     * For webhook mode, no additional cleanup is needed since Telegram will stop sending
     * updates when the server is stopped.
     */
    override suspend fun onFinalize() {
        logger.debug { "onFinalize invoked" }
    }
}
