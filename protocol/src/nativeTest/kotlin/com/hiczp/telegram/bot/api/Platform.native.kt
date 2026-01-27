package com.hiczp.telegram.bot.api

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

@OptIn(ExperimentalForeignApi::class)
actual fun getBotToken(): String? {
    return getenv(BOT_TOKEN_ENV_VAR)?.toKString()
}
