package com.hiczp.telegram.bot.protocol.test

/**
 * Test environment configuration providing lazy-loaded test credentials.
 *
 * This object provides lazy-initialized access to test credentials from environment variables.
 * Values are cached after first access.
 */
object TestEnv {
    /**
     * Telegram bot token from environment variable [EnvVars.BOT_TOKEN].
     * Lazy-initialized and cached after first access.
     */
    val botToken by lazy {
        checkNotNull(getBotToken()) { "Failed to get ${EnvVars.BOT_TOKEN}" }
    }

    /**
     * Test chat ID from environment variable [EnvVars.TEST_CHAT_ID].
     * Lazy-initialized and cached after first access.
     */
    val testChatId by lazy {
        checkNotNull(getTestChatId()) { "Failed to get ${EnvVars.TEST_CHAT_ID}" }
    }
}
