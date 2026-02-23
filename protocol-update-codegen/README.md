# Protocol Update Codegen

KSP (Kotlin Symbol Processing) processor that generates event classes from Telegram Bot API update containers.

## Purpose

This module processes data classes annotated with `@IncomingUpdateContainer` and generates:

1. **TelegramBotEvents.kt** - A sealed interface `TelegramBotEvent` and corresponding event data classes
2. **Updates.kt** - Extension function `toTelegramBotEvent()` to convert update objects to event types

## How It Works

Given a data class like:

```kotlin
@IncomingUpdateContainer
@Serializable
data class Update(
    @SerialName("update_id")
    val updateId: Long,
    @SerialName("message")
    val message: Message? = null,
    @SerialName("edited_message")
    val editedMessage: Message? = null,
    // ... more optional fields
)
```

The processor generates:

```kotlin
// TelegramBotEvents.kt
sealed interface TelegramBotEvent {
    val updateId: Long
}

data class MessageEvent(override val updateId: Long, val message: Message) : TelegramBotEvent
data class EditedMessageEvent(override val updateId: Long, val editedMessage: Message) : TelegramBotEvent
// ... more event classes

// Updates.kt
fun Update.toTelegramBotEvent(): TelegramBotEvent {
    message?.let { return MessageEvent(updateId, it) }
    editedMessage?.let { return EditedMessageEvent(updateId, it) }
    // ... more branches
    throw UnrecognizedUpdateException(this)
}
```

## Dependencies

- **KSP API** - Symbol processing infrastructure
- **KotlinPoet** - Kotlin code generation
- **kotlinpoet-ksp** - KSP support for KotlinPoet
- **protocol-annotation** - Provides `@IncomingUpdateContainer` annotation

## Usage

This processor is automatically applied to projects that depend on `protocol-annotation` and have this KSP processor in
their processor classpath. No manual configuration required.

## Supported Platforms

JVM only (KSP processors run on JVM).

## Related Modules

- `:protocol-annotation` - Provides the `@IncomingUpdateContainer` annotation
- `:protocol` - Contains the generated `Update` class
- `:client` - Uses generated events for type-safe update handling
- `:application` - Uses generated events in the event dispatching system
