@file:OptIn(ExperimentalForeignApi::class)

package com.hiczp.telegram.bot.api

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

actual fun getBotToken(): String? {
    return getenv(EnvVars.BOT_TOKEN)?.toKString()
}

actual fun getTestChatId(): String? {
    return getenv(EnvVars.TEST_CHAT_ID)?.toKString()
}
