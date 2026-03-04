package com.hiczp.telegram.bot.protocol.annotation

/**
 * Indicates the version of the Telegram Bot API that the annotated component was generated from.
 *
 * This annotation is automatically applied to generated code during the build process,
 * with the version value extracted from the `info.version` field of the Telegram Bot API
 * OpenAPI specification.
 *
 * Example usage on generated code:
 * ```kotlin
 * @TelegramBotApiVersion("7.11")
 * interface TelegramBotApi { ... }
 * ```
 *
 * @property value The version string of the Telegram Bot API specification (e.g., "7.11").
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class TelegramBotApiVersion(val value: String)
