package com.hiczp.telegram.bot.api.plugin

import de.jensklingenberg.ktorfit.annotations
import io.ktor.client.plugins.api.*

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TelegramFileDownload

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
