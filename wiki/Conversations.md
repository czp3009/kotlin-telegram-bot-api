# Conversations

Multi-turn conversations allow bots to collect information across multiple messages.

## Setup

Install the conversation interceptor.

```kotlin
import com.hiczp.telegram.bot.application.interceptor.builtin.conversation.conversationInterceptor

val app = TelegramBotApplication.longPolling(
    botToken = "YOUR_TOKEN",
    interceptors = listOf(conversationInterceptor()),
    eventDispatcher = dispatcher
)
```

**Important:** The conversation interceptor must not be installed more than once.

## Basic Conversation

```kotlin
import com.hiczp.telegram.bot.application.interceptor.builtin.conversation.startConversation
import kotlin.time.Duration.Companion.minutes

commandEndpoint("survey") {
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
```

## Messaging Methods

### send(text, replyMarkup?)

Sends a message to the conversation's chat without replying to any specific message. Returns `TelegramResponse<Message>`
and updates `lastSentMessageId` on success.

Requires a `TelegramBotEventContext` context receiver.

```kotlin
send("Hello!")  // Sends to conversation's chat
send("Choose:", replyMarkup = ForceReply(forceReply = true))  // With reply markup
```

### reply(text, replyToMessageId?)

Sends a message as a reply. The reply behavior follows this priority:

1. If `replyToMessageId` is provided, replies to that specific message.
2. If `lastAwaitedMessageId` is available, replies to the most recent user input.
3. If no message ID is available, behaves like `send()`.

Returns `TelegramResponse<Message>` and updates `lastSentMessageId` on success.

Requires a `TelegramBotEventContext` context receiver.

```kotlin
reply("Got it!")  // Replies to last awaited message
reply("Response", replyToMessageId = 123)  // Replies to specific message
```

## Await Methods

| Method                 | Description                                                                                           | Updates `lastAwaitedMessageId` |
|------------------------|-------------------------------------------------------------------------------------------------------|--------------------------------|
| `awaitEvent()`         | Awaits the next event (raw)                                                                           | No                             |
| `awaitEvent<T>()`      | Awaits any event of type T                                                                            | No                             |
| `awaitMessage()`       | Awaits the next message event                                                                         | Yes                            |
| `awaitText()`          | Awaits the next text message                                                                          | Yes                            |
| `awaitCommand()`       | Awaits the next command (validates bot username). Requires `TelegramBotEventContext` context receiver | Yes                            |
| `awaitCallbackQuery()` | Awaits the next callback query event                                                                  | Yes (if message present)       |
| `awaitReply(msgId?)`   | Awaits a reply to a specific message (defaults to `lastSentMessageId`)                                | Yes                            |

### Example: Callback Query

```kotlin
send("Choose an option:")
val callback = awaitCallbackQuery()
reply("You selected: ${callback.callbackQuery.data}")
```

### Example: Reply Waiting

```kotlin
send("Please reply to this message with your answer.")
val replyMessage = awaitReply()  // Waits for reply to last sent message
```

### Example: Command Waiting

Note: `awaitCommand()` requires a `TelegramBotEventContext` context receiver to validate the bot username.

```kotlin
send("Type /yes or /no to confirm:")
val command = awaitCommand()  // Validates against bot username
reply("You typed: /${command.name}")
```

## Conversation ID

Conversations are identified by `ConversationId(chatId, threadId?, userId?)`.

```kotlin
data class ConversationId(
  val chatId: Long,
  val threadId: Long? = null,
  val userId: Long? = null,
)
```

| Pattern                                          | Scope                          |
|--------------------------------------------------|--------------------------------|
| `ConversationId(chatId)`                         | Whole chat shares conversation |
| `ConversationId(chatId, threadId = threadId)`    | Per-thread in forum            |
| `ConversationId(chatId, userId = userId)`        | Per-user in group chat         |
| `ConversationId(chatId, threadId, userId = uid)` | Per-user in specific thread    |

Default: Uses `chatId + threadId + userId` from the triggering event.

## Custom Intercept Predicate

By default, conversations intercept `MessageEvent`, `BusinessMessageEvent`, and `CallbackQueryEvent`.

```kotlin
startConversation(
    interceptPredicate = { context ->
        context.event is MessageEvent
    }
) {
    // Only messages are intercepted
}
```

## Cancel Predicate

By default, conversations cancel when the user sends `/cancel` (also recognizes `/cancel@bot_username`).

```kotlin
startConversation(
  cancelPredicate = { context ->
    val event = context.event
    event is MessageEvent && event.message.text == "/quit"
  }
) {
  // Cancels on /quit instead of /cancel
}
```

## Channel Capacity

The `capacity` parameter controls how events are buffered for the conversation:

| Capacity             | Behavior                                            |
|----------------------|-----------------------------------------------------|
| `Channel.UNLIMITED`  | (default) All matching events are buffered          |
| `Channel.BUFFERED`   | Bounded buffer; new events are dropped when full    |
| `Channel.RENDEZVOUS` | (capacity = 0) Only received when actively awaiting |

```kotlin
startConversation(capacity = Channel.BUFFERED) {
  // Limited buffer - excess events dropped with warning
}
```

## Coroutine Support

`ConversationScope` implements `CoroutineScope`. Child coroutines are tied to the conversation's lifecycle.

```kotlin
startConversation {
    launch { task1() }
    launch { task2() }
    // Both complete before conversation ends
}
```

## Properties

| Property               | Description                                                       |
|------------------------|-------------------------------------------------------------------|
| `lastAwaitedMessageId` | Updated after await methods, used by `reply()` for auto-targeting |
| `lastSentMessageId`    | Updated after `send()`/`reply()`, used by `awaitReply()`          |
| `id`                   | The `ConversationId` identifying this conversation                |
| `channel`              | Channel receiving events for this conversation                    |
| `cancelPredicate`      | The predicate function used for cancellation                      |

## Error Handling

| Exception                        | Behavior                                 |
|----------------------------------|------------------------------------------|
| `TimeoutCancellationException`   | Triggers `onTimeout` callback            |
| `ConversationCancelledException` | Triggers `onCancel` callback             |
| `CancellationException`          | Re-thrown (coroutine cancellation)       |
| Other `Throwable`                | Logged as error, conversation cleaned up |

## Important Notes

- Messages routed to a conversation that are not consumed will be discarded when the conversation ends. They will NOT be
  passed to other handlers.
- Conversation state is in-memory only. States are lost on bot restart.
- From the perspective of upper-layer interceptors, events routed to a conversation appear to be processed immediately
  (the interceptor returns without waiting for the conversation to complete).

## Next Steps

- [Handler DSL](Handler-DSL) - Event routing
- [Interceptors](Interceptors) - Middleware pipeline
