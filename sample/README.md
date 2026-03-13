# Sample Bots

This directory contains example bot implementations demonstrating various features of the kotlin-telegram-bot-api
library.

## Running Samples

All samples require a bot token from [@BotFather](https://t.me/botfather). Run them using Gradle:

```bash
# JVM (recommended)
./gradlew :sample:jvmRun --args="YOUR_BOT_TOKEN" -DmainClass="fully.qualified.MainKt" --quiet

# Example: Run EchoBot
./gradlew :sample:jvmRun --args="YOUR_BOT_TOKEN" -DmainClass="com.hiczp.telegram.bot.sample.basic.echo.EchoBotKt" --quiet
```

## Basic Samples

### [EchoBot](src/commonMain/kotlin/com/hiczp/telegram/bot/sample/basic/echo/EchoBot.kt)

A minimal bot that echoes back text messages in private chats.

**Demonstrates:**

- Long polling setup with `TelegramBotApplication.longPolling`
- Simple event dispatching with `SimpleTelegramEventDispatcher`
- Filtering by chat type
- Basic message sending

```bash
./gradlew :sample:jvmRun --args="YOUR_BOT_TOKEN" -DmainClass="com.hiczp.telegram.bot.sample.basic.echo.EchoBotKt" --quiet
```

### [CommandBot](src/commonMain/kotlin/com/hiczp/telegram/bot/sample/basic/command/CommandBot.kt)

A comprehensive command handling demo with typed arguments and subcommands.

**Demonstrates:**

- Handler DSL with `HandlerTelegramEventDispatcher`
- Simple commands with `commandEndpoint`
- Typed command arguments using `BotArguments`
- Enum argument parsing
- Nested subcommands
- Access control with custom `requireAuth` DSL
- Request logging via `loggingInterceptor`

**Available Commands:**

- `/start` - Show help message
- `/ping` - Health check
- `/echo <message> [count]` - Echo a message
- `/calc <a> <op> <b>` - Calculator
- `/admin` - Admin commands (restricted)

```bash
./gradlew :sample:jvmRun --args="YOUR_BOT_TOKEN" -DmainClass="com.hiczp.telegram.bot.sample.basic.command.CommandBotKt" --quiet
```

## Advanced Samples

### [FileBot](src/commonMain/kotlin/com/hiczp/telegram/bot/sample/advanced/file/FileBot.kt)

A bot demonstrating file upload and download capabilities.

**Demonstrates:**

- Uploading local files with `InputFile.binary`
- Referencing files by `file_id` with `InputFile.reference`
- Downloading files from Telegram
- Handling photo and sticker messages

**Available Commands:**

- `/start` - Show help
- `/photo` - Send a sample photo from local file
- `/sticker` - Send a sticker to get its image echoed back

**Auto-echo:**

- Photo -> echo photo

```bash
./gradlew :sample:jvmRun --args="YOUR_BOT_TOKEN" -DmainClass="com.hiczp.telegram.bot.sample.advanced.file.FileBotKt" --quiet
```

### [ConversationBot](src/commonMain/kotlin/com/hiczp/telegram/bot/sample/advanced/conversation/ConversationBot.kt)

A bot demonstrating multi-turn conversation support for interactive flows like surveys, quizzes, and wizards.

**Demonstrates:**

- Installing `conversationInterceptor` for conversation support
- Starting conversations with `startConversation`
- Using `send` and `reply` for messaging within conversations
- Awaiting user input with `awaitText()` and `awaitCallbackQuery()`
- Handling conversation timeout with `onTimeout`
- Handling user cancellation with `onCancel`
- Using inline keyboard buttons in conversations

**Available Commands:**

- `/start` - Show help message
- `/survey` - Multi-question survey (name, age, favorite color)
- `/quiz` - Interactive quiz game with inline buttons (3 questions with scoring)
- `/register` - Registration wizard with validation

**During conversations:**

- Type `/cancel` to cancel any conversation
- Conversations timeout after 2-3 minutes of inactivity

```bash
./gradlew :sample:jvmRun --args="YOUR_BOT_TOKEN" -DmainClass="com.hiczp.telegram.bot.sample.advanced.conversation.ConversationBotKt" --quiet
```

## DSL Utilities

### [AuthDsl.kt](src/commonMain/kotlin/com/hiczp/telegram/bot/sample/dsl/AuthDsl.kt)

A reusable authentication DSL built on top of `middleware`. This is not a standalone bot but a utility used by
CommandBot.

**Demonstrates:**

- Creating custom DSL with async predicates
- Using `middleware` for authorization guards
- Separating authentication logic from business logic

## Project Structure

```
sample/
└── src/commonMain/kotlin/com/hiczp/telegram/bot/sample/
    ├── basic/
    │   ├── echo/
    │   │   └── EchoBot.kt        # Minimal echo bot
    │   └── command/
    │       └── CommandBot.kt     # Command handling demo
    ├── advanced/
    │   ├── file/
    │   │   └── FileBot.kt        # File operations demo
    │   └── conversation/
    │       └── ConversationBot.kt # Multi-turn conversation demo
    └── dsl/
        └── AuthDsl.kt            # Reusable auth DSL
```
