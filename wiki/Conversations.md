# Conversations

Conversations let a handler start a multi-turn flow and receive later events through a `ConversationScope`.

## Setup

Install `conversationInterceptor()` once. It is an application interceptor, so it sits above the dispatcher.

```kotlin
val app = TelegramBotApplication.longPolling(
    botToken = "YOUR_TOKEN",
    interceptors = listOf(conversationInterceptor()),
    eventDispatcher = dispatcher
)
```

## Basic Conversation

Start conversations from runtime handler code.

```kotlin
command("survey") {
    handle {
        startConversation(
            timeout = 5.minutes,
            onTimeout = {
                val chatId = event.extractChatId()
                if (chatId != null) {
                    client.sendMessage(chatId.toString(), "Survey timed out.")
                }
            },
            onCancel = {
                val chatId = event.extractChatId()
                if (chatId != null) {
                    client.sendMessage(chatId.toString(), "Survey cancelled.")
                }
            }
        ) {
            send("What is your name?")
            val name = awaitText()

            reply("Hello, $name! How old are you?")
            val age = awaitText()

            reply("Thanks! You are $age years old.")
        }
    }
}
```

`startConversation` launches the conversation in `applicationScope` and returns a `Job`.

## Messaging Methods

### `send(text, replyMarkup?)`

Sends a message to the conversation chat. It returns `TelegramResponse<Message>` and updates `lastSentMessageId` on
success.

```kotlin
send("Hello!")
send("Choose:", replyMarkup = ForceReply(forceReply = true))
```

### `reply(text, replyToMessageId?)`

Sends a reply using this priority:

1. `replyToMessageId`, when provided
2. `lastAwaitedMessageId`, when available
3. regular `send()` behavior

It returns `TelegramResponse<Message>` and updates `lastSentMessageId` on success.

```kotlin
reply("Got it!")
reply("Response", replyToMessageId = 123)
```

## Await Methods

| Method                 | Description                                                       | Updates `lastAwaitedMessageId` |
|------------------------|-------------------------------------------------------------------|--------------------------------|
| `awaitEvent()`         | Awaits the next raw event                                         | No                             |
| `awaitEvent<T>()`      | Awaits the next event of type `T`                                 | No                             |
| `awaitMessage()`       | Awaits the next `MessageEvent` and returns its `Message`          | Yes                            |
| `awaitText()`          | Awaits the next message with text and returns the text            | Yes                            |
| `awaitCommand()`       | Awaits a command intended for this bot                            | Yes                            |
| `awaitCallbackQuery()` | Awaits the next callback query event                              | Yes, when callback has message |
| `awaitReply(msgId?)`   | Awaits a reply to `msgId`, or to `lastSentMessageId` when omitted | Yes                            |

`awaitCommand()` needs the surrounding `TelegramBotEventContext` so it can validate `/command@bot_username`. This is
available inside handler blocks.

```kotlin
send("Type /yes or /no to confirm:")
val command = awaitCommand()
reply("You typed: /${command.name}")
```

## Callback Query Example

```kotlin
send("Choose an option:")
val callback = awaitCallbackQuery()
reply("You selected: ${callback.callbackQuery.data}")
```

## Reply Waiting

```kotlin
send("Please reply to this message with your answer.")
val replyMessage = awaitReply()
reply("You answered: ${replyMessage.text}")
```

## Conversation ID

Conversations are keyed by `ConversationId`.

```kotlin
data class ConversationId(
    val chatId: Long,
    val threadId: Long? = null,
    val userId: Long? = null,
)
```

| Pattern                                             | Scope                     |
|-----------------------------------------------------|---------------------------|
| `ConversationId(chatId)`                            | Whole chat                |
| `ConversationId(chatId, threadId = threadId)`       | Whole forum topic/thread  |
| `ConversationId(chatId, userId = userId)`           | One user in a chat        |
| `ConversationId(chatId, threadId, userId = userId)` | One user in a forum topic |

The default id uses the triggering event's `chatId`, `threadId`, and `userId`.

## Intercept Predicate

By default, active conversations intercept `MessageEvent`, `BusinessMessageEvent`, and `CallbackQueryEvent`.

```kotlin
startConversation(
    interceptPredicate = { context ->
        context.event is MessageEvent
    }
) {
    // Only message events enter this conversation
}
```

## Cancel Predicate

By default, a message matching `/cancel` or `/cancel@bot_username` cancels the conversation.

```kotlin
startConversation(
    cancelPredicate = { context ->
        val event = context.event
        event is MessageEvent && event.message.text == "/quit"
    }
) {
    // Cancels on /quit
}
```

## Channel Capacity

The conversation channel buffers events routed to the active conversation.

| Capacity             | Behavior                                          |
|----------------------|---------------------------------------------------|
| `Channel.UNLIMITED`  | Default; buffers all matching events              |
| `Channel.BUFFERED`   | Bounded buffer; events are dropped when full      |
| `Channel.RENDEZVOUS` | Capacity 0; receives only while actively awaiting |

```kotlin
startConversation(capacity = Channel.BUFFERED) {
    val text = awaitText()
}
```

## Coroutine Support

`ConversationScope` implements `CoroutineScope`. Child coroutines are tied to the conversation lifecycle.

```kotlin
startConversation {
    launch { task1() }
    launch { task2() }
}
```

## Properties

| Property               | Description                                              |
|------------------------|----------------------------------------------------------|
| `id`                   | Conversation id                                          |
| `channel`              | Incoming event channel                                   |
| `cancelPredicate`      | Predicate used by await methods to detect cancellation   |
| `lastAwaitedMessageId` | Updated by message await methods and used by `reply()`   |
| `lastSentMessageId`    | Updated by `send()`/`reply()` and used by `awaitReply()` |

`lastAwaitedMessageId` and `lastSentMessageId` are mutable for advanced use cases, but normal code should let the
conversation framework maintain them.

## Error Handling

| Exception                        | Behavior                              |
|----------------------------------|---------------------------------------|
| `TimeoutCancellationException`   | Calls `onTimeout`                     |
| `ConversationCancelledException` | Calls `onCancel`                      |
| `CancellationException`          | Re-thrown                             |
| Other `Throwable`                | Logged and conversation is cleaned up |

## Notes

- Events routed to a conversation are not passed to normal handlers.
- Unconsumed buffered events are dropped when the conversation ends.
- Conversation state is in memory only and is lost on process restart.
- From upper interceptors and update sources, routing an event into a conversation completes immediately; the active
  conversation continues in `applicationScope`.
