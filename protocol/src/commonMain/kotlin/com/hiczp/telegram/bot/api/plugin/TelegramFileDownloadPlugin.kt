package com.hiczp.telegram.bot.api.plugin

import de.jensklingenberg.ktorfit.annotations
import io.ktor.client.plugins.api.*

/**
 * Annotation to mark Telegram Bot API methods that download files.
 *
 * When applied to a Ktorfit interface method, this annotation signals to [TelegramFileDownloadPlugin]
 * that the request URL should be modified to include the `/file` path segment for file downloads.
 *
 * @see TelegramFileDownloadPlugin
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
annotation class TelegramFileDownload

/**
 * Ktor client plugin that modifies file download URLs for the Telegram Bot API.
 *
 * This plugin intercepts requests annotated with [TelegramFileDownload] and transforms the URL path
 * by inserting a `/file` segment between the bot token and the file path, converting:
 *
 * ```
 * https://api.telegram.org/bot<token>/<file_path>
 * ```
 *
 * to:
 *
 * ```
 * https://api.telegram.org/file/bot<token>/<file_path>
 * ```
 *
 * ## Usage
 *
 * Install this plugin in your Ktor HTTP client:
 *
 * ```kotlin
 * val client = HttpClient {
 *     install(TelegramFileDownloadPlugin)
 * }
 * ```
 *
 * Then apply the [TelegramFileDownload] annotation to methods that download files:
 *
 * ```kotlin
 * interface TelegramBotApi {
 *     @TelegramFileDownload
 *     @GET("downloadFile")
 *     suspend fun downloadFile(@Query("file_path") filePath: String): HttpStatement
 * }
 * ```
 *
 * ## Requirements
 *
 * **Important:** This plugin requires a correctly configured `baseUrl` in the Ktorfit configuration.
 * The plugin assumes the base URL follows the standard Telegram Bot API format
 * (`https://api.telegram.org/bot<token>/`). Only when the base URL is correct can the plugin
 * properly construct the file download path.
 *
 * @see TelegramFileDownload
 */
val TelegramFileDownloadPlugin = createClientPlugin("TelegramFileDownloadPlugin") {
    onRequest { request, _ ->
        val isFileDownload = request.annotations.any { it is TelegramFileDownload }
        if (isFileDownload) {
            //example: https://api.telegram.org/file/bot<token>/<file_path>
            val originalSegments = request.url.pathSegments
            check(originalSegments.size >= 2) { "Invalid URL path segments: ${originalSegments.joinToString()}" }
            request.url.pathSegments = originalSegments.toMutableList().apply {
                add(1, "file")
            }
        }
    }
}
