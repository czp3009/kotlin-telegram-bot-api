package com.hiczp.telegram.bot.application.exception

import kotlinx.coroutines.CancellationException

class TelegramBotShuttingDownException : CancellationException("Bot is shutting down, rejecting new updates")
