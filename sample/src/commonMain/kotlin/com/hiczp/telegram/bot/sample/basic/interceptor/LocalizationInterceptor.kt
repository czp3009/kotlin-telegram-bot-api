/**
 * Localization Interceptor
 *
 * An interceptor that extracts the user's language code from incoming events,
 * caches it in a thread-safe map, and stores it in the context attributes for
 * later use in handlers.
 *
 * ## Features
 *
 * - **Thread-safe caching**: Uses Ktor's `ConcurrentMap` to cache language codes per user
 * - **Automatic updates**: Updates the cache whenever a new language code is detected
 * - **Fallback to cache**: Uses cached language code if current event doesn't contain one
 *
 * ## Usage
 *
 * ```kotlin
 * val interceptors = listOf(localizationInterceptor())
 *
 * // In handler, access via extension property:
 * commandEndpoint("greet") {
 *     val lang = languageCode ?: "en"
 *     val greeting = when (lang) {
 *         "zh" -> "你好"
 *         "ja" -> "こんにちは"
 *         "es" -> "Hola"
 *         else -> "Hello"
 *     }
 *     replyMessage(greeting)
 * }
 * ```
 *
 * @see languageCode
 * @see TelegramBotEvent.extractLanguageCode
 */
package com.hiczp.telegram.bot.sample.basic.interceptor

import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.context.extractLanguageCode
import com.hiczp.telegram.bot.application.context.extractUserId
import com.hiczp.telegram.bot.application.interceptor.TelegramEventInterceptor
import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.util.*
import io.ktor.util.collections.*

private val logger = KotlinLogging.logger("LocalizationInterceptor")

/**
 * Thread-safe cache mapping user IDs to their language codes.
 *
 * This cache is updated whenever a new language code is detected from incoming events,
 * and serves as a fallback when subsequent events don't contain language information.
 */
private val userLanguageCache: ConcurrentMap<Long, String> = ConcurrentMap()

/**
 * Attribute key for storing language code in context attributes.
 */
private val LanguageCodeAttributeKey = AttributeKey<String>("LanguageCode")

/**
 * Extension property to access language code from context attributes.
 *
 * Returns the user's language code (e.g., "en", "zh", "ja") if available,
 * or null if the language code was not detected or cached.
 */
val TelegramBotEventContext<*>.languageCode: String?
    get() = attributes.getOrNull(LanguageCodeAttributeKey)

/**
 * Creates an interceptor that extracts the user's language code from events,
 * caches it, and stores it in context attributes.
 *
 * This interceptor:
 * 1. Extracts the `language_code` from the user object in events using [TelegramBotEvent.extractLanguageCode]
 * 2. Updates the thread-safe cache with the new language code
 * 3. Stores the language code (from event or cache) in the context's attribute storage
 *    for later use in handlers via the [languageCode] extension property
 *
 * @return A [TelegramEventInterceptor] that extracts, caches, and stores language code.
 */
fun localizationInterceptor(): TelegramEventInterceptor = Interceptor@{ context ->
    val userId = context.event.extractUserId()
    val languageCodeFromEvent = context.event.extractLanguageCode()

    // Update the cache if we have a new language code from the event
    if (userId != null && languageCodeFromEvent != null) {
        userLanguageCache[userId] = languageCodeFromEvent
        logger.debug { "Updated language code cache for user $userId: $languageCodeFromEvent" }
    }

    // Use event's language code or fall back to cached value
    val languageCode = languageCodeFromEvent ?: userId?.let { userLanguageCache[it] }

    if (languageCode != null) {
        context.attributes.put(LanguageCodeAttributeKey, languageCode)
        logger.debug { "Detected language code: $languageCode (from ${if (languageCodeFromEvent != null) "event" else "cache"})" }
    }

    process(context)
}
