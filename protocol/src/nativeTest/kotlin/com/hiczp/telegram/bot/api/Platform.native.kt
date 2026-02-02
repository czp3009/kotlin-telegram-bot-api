package com.hiczp.telegram.bot.api

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

@OptIn(ExperimentalForeignApi::class)
actual fun getBotToken(): String? {
    return getenv(EnvVars.BOT_TOKEN)?.toKString()
}
