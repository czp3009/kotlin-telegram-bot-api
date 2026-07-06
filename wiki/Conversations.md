# Conversations

Conversations let a bot run a multi-turn flow in a linear coroutine style.

## Setup

Install `conversationInterceptor()` once. Active conversations are restored by this interceptor before normal handler
routing, so a running conversation receives matching events before ordinary handlers can consume them.

```kotlin
val app = TelegramBotApplication.longPolling(
    botToken = "YOUR_TOKEN",
    interceptors = listOf(conversationInterceptor()),
    eventDispatcher = dispatcher
)
```

## Command Starter

Use `conversationCommand` for the common case where a command starts a conversation.

```kotlin
val dispatcher = HandlerTelegramEventDispatcher(handling {
    message {
        privateChat {
            conversationCommand(
                name = "survey",
                timeout = 5.minutes,
                onTimeout = {
                    replyMessage("Survey timed out.")
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
})
```

The command is only the start condition. After the conversation starts, later messages do not need to match the command;
they are routed to the active conversation by `conversationInterceptor()`. Parent filters such as `message` and
`privateChat` only constrain the starting event.

## Full Starter

Use `conversation` when the start condition is not a simple command or when the conversation needs custom scoping.

```kotlin
message {
    privateChat {
        conversation(
            id = {
                ConversationId(
                    chatId = event.message.chat.id,
                    threadId = event.message.messageThreadId,
                    userId = event.message.from?.id
                )
            },
            start = {
                event.message.text == "/survey"
            },
            receive = { context ->
                context.event is MessageEvent || context.event is CallbackQueryEvent
            },
            timeout = 5.minutes
        ) {
            send("What is your name?")
            val name = awaitText()
            reply("Hello, $name")
        }
    }
}
```

`conversation` and `conversationCommand` are handler route starters. Put them under ordinary route filters such as
`message`, `privateChat`, or `fromUser` when those filters should restrict which event can start the conversation.
Those route filters are not re-applied to later conversation events.

## Routing Model

Incoming events are processed in this order:

1. `conversationInterceptor()` checks active conversations.
2. If the event matches an active conversation's `ConversationId` and `receive` predicate, it is sent to that
   conversation and normal handler routing is skipped.
3. If no active conversation matches, the handler DSL runs normally.
4. A matching `conversation` or `conversationCommand` starter creates a new active conversation and consumes the event.
5. If no conversation starter or handler matches, normal dead-letter behavior applies.

This means a conversation started by `/survey` will receive the user's later `"Alice"` or `"18"` messages even though
those messages are not `/survey` commands.

The starting event still follows normal handler DSL order, including backtracking. Put conversation starters before
broad fallback handlers in the same route scope when those fallbacks would otherwise consume the start event.

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

```kotlin
send("Type /yes or /no to confirm:")
val command = awaitCommand()
reply("You typed: /${command.command}")
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

## Receive Predicate

By default, active conversations receive `MessageEvent`, `BusinessMessageEvent`, and `CallbackQueryEvent`.

```kotlin
conversationCommand(
    name = "quiz",
    receive = { context ->
        context.event is MessageEvent || context.event is CallbackQueryEvent
    }
) {
    // Only message and callback query events enter this conversation
}
```

## Cancel Predicate

By default, a message matching `/cancel` or `/cancel@bot_username` cancels the conversation.

```kotlin
conversationCommand(
    name = "survey",
    cancel = { context ->
        val currentEvent = context.event
        currentEvent is MessageEvent && currentEvent.message.text == "/quit"
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

`Channel.UNLIMITED` is the default because this library primarily targets low-traffic bots, where preserving user input
is usually more useful than applying back pressure. For high-traffic bots or untrusted chats, choose a bounded capacity
and handle dropped events as part of the conversation design.

```kotlin
conversationCommand(name = "survey", capacity = Channel.BUFFERED) {
    val text = awaitText()
}
```

## Coroutine Support

`ConversationScope` implements `CoroutineScope`. Child coroutines are tied to the conversation lifecycle.

```kotlin
conversationCommand("work") {
    launch { task1() }
    launch { task2() }
}
```

The conversation body runs in a `supervisorScope`. A failed child launched from the conversation does not automatically
fail the main conversation flow. Await or join child work explicitly if its failure should stop the conversation.

## Properties

| Property               | Description                                              |
|------------------------|----------------------------------------------------------|
| `id`                   | Conversation id                                          |
| `channel`              | Incoming event channel                                   |
| `cancelPredicate`      | Predicate used by await methods to detect cancellation   |
| `client`               | Telegram API client from the starting event context      |
| `applicationScope`     | Application coroutine scope                              |
| `attributes`           | Shared attributes from the starting event context        |
| `startEvent`           | Event that started this conversation                     |
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

Any uncaught `TimeoutCancellationException` from inside the conversation block is treated as a conversation timeout.
This is intentional: user code can end a conversation early by letting such an exception escape. If a timeout is only
local to one step and the conversation should continue, catch it inside the block.

## Notes

- Events routed to a conversation are not passed to normal handlers.
- Unconsumed buffered events are dropped when the conversation ends.
- Conversation state is in memory only and is lost on process restart.
- Conversation progress is represented by coroutine execution state and channel contents. It cannot be serialized,
  snapshotted, restored after a crash, or shared across distributed bot instances.
- From upper interceptors and update sources, routing an event into a conversation completes immediately; the active
  conversation continues in `applicationScope`.
