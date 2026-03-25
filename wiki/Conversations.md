# Conversations

Multi-turn conversations allow bots to collect information across multiple messages.

## Setup

Install the conversation interceptor

```kotlin
import com.hiczp.telegram.bot.application.interceptor.builtin.conversation.conversationInterceptor

val app = TelegramBotApplication.longPolling(
    botToken = "YOUR_TOKEN",
    interceptors = listOf(conversationInterceptor()),
    eventDispatcher = dispatcher
)
```

## Basic Conversation

```kotlin
import com.hiczp.telegram.bot.application.interceptor.builtin.conversation.startConversation
import kotlin.time.Duration.Companion.minutes

commandEndpoint("survey") {
    startConversation(
        timeout = 5.minutes,
        onTimeout = { sendMessage("Survey timed out.") },
        onCancel = { sendMessage("Survey cancelled.") }
    ) {
        send("What is your name?")
        val name = awaitText()

        reply("Hello, $name! How old are you?")
        val age = awaitText()

        reply("Thanks! You are $age years old.")
    }
}
```

## Messaging Methods

### send(text)

Sends a message without replying to any specific message

```kotlin
send("Hello!")  // Sends to conversation's chat
```

### reply(text, replyToMessageId?)

Sends a message as a reply

```kotlin
reply("Got it!")  // Replies to last awaited message
reply("Response", replyToMessageId = 123)  // Replies to specific message
```

## Await Methods

| Method                 | Description                          |
|------------------------|--------------------------------------|
| `awaitEvent<T>()`      | Awaits any event of type T           |
| `awaitMessage()`       | Awaits the next message event        |
| `awaitText()`          | Awaits the next text message         |
| `awaitCommand()`       | Awaits the next command              |
| `awaitCallbackQuery()` | Awaits the next callback query event |
| `awaitReply(msgId?)`   | Awaits a reply to a specific message |

### Example: Callback Query

```kotlin
send("Choose an option:")
val callback = awaitCallbackQuery()
reply("You selected: ${callback.data}")
```

### Example: Reply Waiting

```kotlin
send("Please reply to this message with your answer.")
val reply = awaitReply()  // Waits for reply to last sent message
```

## Conversation ID

Conversations are identified by `ConversationId(chatId, userId?, threadId?)`

| Pattern                            | Scope                          |
|------------------------------------|--------------------------------|
| `ConversationId(chatId)`           | Whole chat shares conversation |
| `ConversationId(chatId, userId)`   | Per-user in group chat         |
| `ConversationId(chatId, threadId)` | Per-thread in forum            |

## Custom Intercept Predicate

By default, conversations intercept `MessageEvent`, `BusinessMessageEvent`, and `CallbackQueryEvent`

```kotlin
startConversation(
    interceptPredicate = { context ->
        context.event is MessageEvent
    }
) {
    // Only messages are intercepted
}
```

## Coroutine Support

`ConversationScope` implements `CoroutineScope`

```kotlin
startConversation {
    launch { task1() }
    launch { task2() }
    // Both complete before conversation ends
}
```

## Properties

| Property               | Description                                              |
|------------------------|----------------------------------------------------------|
| `lastAwaitedMessageId` | Updated after await methods, used by `reply()`           |
| `lastSentMessageId`    | Updated after `send()`/`reply()`, used by `awaitReply()` |
| `id`                   | The `ConversationId`                                     |
| `channel`              | Channel receiving events for this conversation           |

## Next Steps

- [Handler DSL](Handler-DSL) - Event routing
- [Interceptors](Interceptors) - Middleware pipeline
