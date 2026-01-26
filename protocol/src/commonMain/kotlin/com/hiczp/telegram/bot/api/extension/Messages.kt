package com.hiczp.telegram.bot.api.extension

import com.hiczp.telegram.bot.api.model.Message

val Message.isInaccessible get() = date == 0L
