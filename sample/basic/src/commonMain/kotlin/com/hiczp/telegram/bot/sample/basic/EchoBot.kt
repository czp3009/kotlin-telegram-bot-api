package com.hiczp.telegram.bot.sample.basic

import com.hiczp.telegram.bot.application.TelegramBotApplication
import com.hiczp.telegram.bot.application.dispatcher.SimpleTelegramEventDispatcher
import com.hiczp.telegram.bot.protocol.constant.ChatType
import com.hiczp.telegram.bot.protocol.event.MessageEvent
import com.hiczp.telegram.bot.protocol.model.sendMessage

suspend fun runEchoBot(botToken: String) {
    val eventDispatcher = SimpleTelegramEventDispatcher { context ->
        val event = context.event
        if (event is MessageEvent && event.message.chat.type == ChatType.PRIVATE) {
            val text = event.message.text
            val chatId = event.message.chat.id

            if (text != null) {
                println("Received message: $text from user ${event.message.chat.username}")
                // Echo the message back
                context.client.sendMessage(
                    chatId = chatId.toString(),
                    text = text
                )
            }
        }
    }

    val app = TelegramBotApplication.longPolling(
        botToken = botToken,
        eventDispatcher = eventDispatcher
    )

    println("Starting echo bot...")
    app.start()
    app.join()
}

suspend fun main(args: Array<String>) {
    val botToken = args.firstOrNull() ?: error("Bot token is required")
    runEchoBot(botToken)
}
