# Command DSL

The command DSL is part of the handler route tree. Commands are filters; they consume an event only when a nested
`handle` block runs.

## Basic Commands

```kotlin
handling {
    command("start") {
        handle {
            replyMessage("Welcome!")
        }
    }

    command("ping") {
        handle {
            replyMessage("pong")
        }
    }
}
```

`command("name")` matches `/name` and `/name@bot_username` when the username belongs to the current bot.

Use `command { ... }` to enter a branch for any command addressed to the bot:

```kotlin
command {
    filter({ event.message.chat.id in adminChats }) {
        command("admin") {
            handle {
                replyMessage("admin")
            }
        }
    }
}
```

## Command Context

Inside `command("name")`, `handle` receives `CommandBotCall`.

```kotlin
command("debug") {
    handle {
        replyMessage(
            "path=$commandPath args=${unconsumedArguments.joinToString(" ")}"
        )
    }
}
```

Useful command context properties:

| Property              | Description                                       |
|-----------------------|---------------------------------------------------|
| `parsedCommand`       | Parsed root command                               |
| `commandPath`         | Command path matched so far, such as `admin user` |
| `unconsumedArguments` | Arguments not consumed by subcommands             |

## Subcommands

Subcommands consume the next unconsumed argument.

```kotlin
command("admin") {
    subCommand("status") {
        handle {
            replyMessage("ok")
        }
    }

    subCommand("user") {
        subCommand("list") {
            handle {
                replyMessage("users")
            }
        }
    }

    handle {
        replyMessage("admin help")
    }
}
```

Matching examples:

| Message            | Handler                 |
|--------------------|-------------------------|
| `/admin status`    | `status` handler        |
| `/admin user list` | `user list` handler     |
| `/admin`           | parent fallback handler |
| `/admin unknown`   | parent fallback handler |

## Typed Arguments

Extend `BotArguments` for positional arguments. The property declaration order is the argument order.

```kotlin
class EchoArgs : BotArguments("Echo text") {
    val text: String by requireArgument("Text to echo")
    val count: Int by optionalArgument("Repeat count", default = 1)
}

command("echo", ::EchoArgs) {
    handle {
        repeat(arguments.count) {
            replyMessage(arguments.text)
        }
    }
}
```

Always pass a factory such as `::EchoArgs`. `BotArguments` instances are stateful during parsing and must not be
shared between invocations.

## Supported Argument Types

`requireArgument<T>()` and `optionalArgument<T>()` support:

- `String`
- `Int`
- `Long`
- `Double`
- `Boolean`, accepting `true/false`, `yes/no`, `on/off`, and `1/0`

Enum arguments use dedicated helpers:

```kotlin
enum class Operation {
    ADD, SUB, MUL, DIV
}

class CalcArgs : BotArguments("Perform a calculation") {
    val a: Double by requireArgument("First number")
    val operation: Operation by enumArgument("Operation")
    val b: Double by requireArgument("Second number")
}
```

Optional enum arguments use `optionalEnumArgument`.

## Validation

Argument providers can be chained with `validate`.

```kotlin
class RegisterArgs : BotArguments("Register user") {
    val age: Int by requireArgument<Int>("Age")
        .validate { require(it >= 0) { "Age must be non-negative" } }

    val email: String by requireArgument<String>("Email")
        .validate {
            require(it.contains("@")) { "Invalid email format" }
        }
}
```

Validation errors become `CommandParseException`.

## Error Handling

By default, parse failures send generated help text to the chat.

```kotlin
command("ban", ::BanArgs) {
    handle {
        replyMessage("Banned ${arguments.userId}")
    }
}
```

Override `onError` for custom responses:

```kotlin
command("ban", ::BanArgs, onError = { error ->
    replyMessage("Invalid /ban usage: ${error.message}")
}) {
    handle {
        replyMessage("Banned ${arguments.userId}")
    }
}
```

Rethrow from `onError` if an interceptor should handle parse failures:

```kotlin
command("ban", ::BanArgs, onError = { throw it }) {
    handle {
        replyMessage("Banned ${arguments.userId}")
    }
}
```

## Typed Subcommands

Typed argument parsing also works on nested subcommands.

```kotlin
command("admin") {
    subCommand("ban", ::BanArgs) {
        handle {
            replyMessage("Banned ${arguments.userId}")
        }
    }
}
```

For `/admin ban 123 spam`, the subcommand consumes `ban`; the typed route parses the remaining arguments.

## Build Time Rule

Command route blocks are build-time route declarations.

```kotlin
command("start") {
    println("runs once while routes are built")

    handle {
        println("runs per matching update")
    }
}
```

Runtime side effects belong in `filter` predicates or `handle` blocks.
