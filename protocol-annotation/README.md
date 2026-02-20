# Protocol Annotation

Kotlin Multiplatform annotation module for the Telegram Bot API.

This module provides annotations and interfaces used by the code generation pipeline for handling Telegram updates.

## Components

### IncomingUpdate

A marker interface for types that can appear as fields in an update container.

Types used in `Update`'s optional non-primitive fields (such as `Message`, `CallbackQuery`, `Poll`, etc.) implement this
interface for polymorphic update handling.

```kotlin
interface IncomingUpdate
```

### IncomingUpdateContainer

An annotation to mark a data class as an incoming update container. Classes annotated with this will be processed by the
KSP processor (`:protocol-update-codegen`) to generate event classes.

```kotlin
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class IncomingUpdateContainer
```

## Usage

The `@IncomingUpdateContainer` annotation is automatically applied to the generated `Update` data class by the code
generator (`GenerateKtorfitInterfacesTask`). The KSP processor then reads this annotation and generates:

- Event classes for each update type
- Sealed interface hierarchy for type-safe event handling

Example generated `Update` class:

```kotlin
@IncomingUpdateContainer
@Serializable
data class Update(
    val updateId: Long,
    val message: Message? = null,
    val callbackQuery: CallbackQuery? = null,
    // ... other optional fields
)
```

Where `Message`, `CallbackQuery`, etc. implement `IncomingUpdate`:

```kotlin
@Serializable
data class Message(/* ... */) : IncomingUpdate
```

## Related Modules

- `:protocol` - Contains the generated `Update` class and other API models
- `:protocol-update-codegen` - KSP processor that generates event classes from `@IncomingUpdateContainer`
- `:application` - High-level application framework using generated events
