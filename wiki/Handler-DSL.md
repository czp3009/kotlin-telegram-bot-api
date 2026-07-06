# Handler DSL

The handler dispatcher routes events through a tree of async filters and explicit runtime handlers.

```kotlin
val routes = handling {
    message {
        privateChat {
            command("start") {
                handle {
                    replyMessage("Welcome")
                }
            }

            text("ping") {
                handle {
                    replyMessage("pong")
                }
            }
        }
    }

    callbackQuery {
        data("confirm") {
            handle {
                client.answerCallbackQuery(event.callbackQuery.id)
            }
        }
    }

    handle {
        println("Unhandled event: $event")
    }
}
```

## Core Model

- `handling { ... }` builds a `RouteNode` tree once.
- `filter({ ... }) { ... }` enters a child branch when the suspending predicate returns `true`.
- `filter<T> { ... }` enters a child branch when the event is type `T`; the branch sees the narrowed event type.
- Built-in matchers such as `message`, `privateChat`, `command`, `text`, `photo`, and `data` are filter wrappers.
- `handle { ... }` runs runtime business logic and consumes the event.
- If a branch matches but no descendant consumes the event, matching backtracks to the next untested sibling branch.
- A parent `handle` acts as a fallback for that branch after all child branches miss.

Matching is depth-first with full backtracking. For example, if `A -> B -> C` does not consume the event, the dispatcher
can still return to the root and try `D -> E -> F`.

## Build Time And Runtime

Route blocks run when the dispatcher is built, not once per update.

```kotlin
handling {
    command("start") {
        println("runs once while building routes")

        handle {
            println("runs whenever /start is handled")
        }
    }
}
```

Do not put per-update side effects directly in route blocks. Put runtime work in filter predicates or `handle` blocks:

```kotlin
message {
    filter({ authService.isAllowed(event.message.from?.id) }) {
        command("admin") {
            handle {
                replyMessage("admin ok")
            }
        }
    }
}
```

Framework-provided filters are side-effect free. User-defined filters are regular suspending code and may have side
effects, but those side effects are not rolled back when a deeper branch misses.

## Event Type Filters

Root-level event helpers narrow `TelegramBotEvent` to the corresponding generated event type.

| Helper                                                                                      | Event type                |
|---------------------------------------------------------------------------------------------|---------------------------|
| `message`                                                                                   | `MessageEvent`            |
| `channelPost`                                                                               | `ChannelPostEvent`        |
| `editedMessage`                                                                             | `EditedMessageEvent`      |
| `editedChannelPost`                                                                         | `EditedChannelPostEvent`  |
| `callbackQuery`                                                                             | `CallbackQueryEvent`      |
| `inlineQuery`                                                                               | `InlineQueryEvent`        |
| `chosenInlineResult`                                                                        | `ChosenInlineResultEvent` |
| `poll`, `pollAnswer`                                                                        | Poll events               |
| `shippingQuery`, `preCheckoutQuery`, `purchasedPaidMedia`                                   | Payment events            |
| `chatJoinRequest`, `chatMember`, `myChatMember`                                             | Chat membership events    |
| `chatBoost`, `removedChatBoost`                                                             | Boost events              |
| `businessConnection`, `businessMessage`, `editedBusinessMessage`, `deletedBusinessMessages` | Business events           |

You can also use the generic primitive directly:

```kotlin
filter<MessageEvent> {
    text("hello") {
        handle {
            replyMessage("Hi")
        }
    }
}
```

## Message Filters

Message filters are available inside `HandlerRoute<MessageEvent>`.

```kotlin
message {
    text("hello") {
        handle { replyMessage("Hi") }
    }

    text(Regex("(?i)^help")) {
        handle { replyMessage("Help") }
    }

    textContains("admin", ignoreCase = true) {
        handle { replyMessage("admin keyword") }
    }

    photo {
        handle { replyMessage("photo received") }
    }

    privateChat {
        fromUser(123456L) {
            handle { replyMessage("private admin message") }
        }
    }
}
```

Available message filters include:

`text`, `textContains`, `textStartsWith`, `textEndsWith`, `photo`, `video`, `document`, `audio`, `sticker`, `voice`,
`videoNote`, `animation`, `contact`, `location`, `venue`, `poll`, `dice`, `reply`, `replyTo`, `fromUser`,
`fromUsers`, `inChat`, `inChats`, `privateChat`, `groupChat`, `supergroupChat`, `channelChat`, `forwarded`,
`newChatMembers`, `leftChatMember`, and `pinnedMessage`.

## Callback And Inline Filters

```kotlin
callbackQuery {
    data("confirm") {
        handle {
            client.answerCallbackQuery(event.callbackQuery.id)
        }
    }

    dataStartsWith("page:") {
        handle {
            val page = event.callbackQuery.data?.removePrefix("page:")
        }
    }
}

inlineQuery {
    queryStartsWith("find:") {
        handle {
            client.answerInlineQuery(event.inlineQuery.id, results = emptyList())
        }
    }
}
```

Callback query filters: `data`, `dataContains`, `dataStartsWith`, `fromUser`, `inChat`.

Inline filters: `query`, `queryContains`, `queryStartsWith`, `fromUser`. `chosenInlineResult` also has `fromUser`.

## Commands

Commands are filter wrappers too.

```kotlin
command("ping") {
    handle {
        replyMessage("pong")
    }
}
```

`command(name)` matches `/name` and `/name@bot_username` for the current bot. Subcommands and typed arguments are
covered in [Command DSL](Command-DSL).

## Custom Filters

Reusable filters are ordinary extension functions over `HandlerRoute<T>`.

```kotlin
fun HandlerRoute<MessageEvent>.fromAdmin(
    adminIds: Set<Long>,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = filter({ event.message.from?.id in adminIds }, build)

message {
    fromAdmin(setOf(123L)) {
        command("reload") {
            handle {
                replyMessage("reloading")
            }
        }
    }
}
```

Because filters are suspending, they can call databases or services:

```kotlin
message {
    filter({ permissions.canUseBot(event.message.from?.id) }) {
        handle {
            replyMessage("allowed")
        }
    }
}
```

## Include Routes

Use `include` to compose routes from multiple modules:

```kotlin
val adminRoutes = handling {
    command("admin") {
        handle {
            replyMessage("admin")
        }
    }
}

val routes = handling {
    command("start") {
        handle {
            replyMessage("start")
        }
    }

    include(adminRoutes)
}
```

## Structured Concurrency

Handlers receive a `CoroutineScope`. Child coroutines launched inside a handler are awaited before dispatch returns.

```kotlin
command("process") {
    handle {
        launch {
            val result = slowOperation()
            replyMessage("Result: $result")
        }
    }
}
```

Use `applicationScope` for work that should outlive the current handler:

```kotlin
command("background") {
    handle {
        applicationScope.launch {
            delay(10.seconds)
            client.sendMessage(event.message.chat.id.toString(), "Delayed message")
        }
    }
}
```

## Dead Letter Handling

If no route consumes an event, `HandlerTelegramEventDispatcher.deadLetter` logs it by default. A root-level `handle`
is usually the simplest fallback:

```kotlin
handling {
    command("start") {
        handle { replyMessage("start") }
    }

    handle {
        println("Unhandled event: $event")
    }
}
```
