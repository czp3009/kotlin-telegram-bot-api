# Handler DSL

The Handler DSL provides a type-safe, declarative way to route Telegram events to handlers.

## Overview

```kotlin
val routes = handling {
    onMessageEvent {
        whenMessageEventText("hello") { replyMessage("Hi!") }
    }
}

val dispatcher = HandlerTelegramEventDispatcher(routes)
```

## Core Functions

### handling {}

Creates the root route node.

```kotlin
val routes = handling {
    // Define routes here
}
```

### handle {}

Catch-all handler (dead letter).

```kotlin
handling {
    commandEndpoint("start") { /* ... */ }

    // Fallback for unhandled events
    handle { println("Unhandled: $event") }
}
```

When a root-level `handle` is present, it acts as the fallback for all unhandled events, replacing the default behavior
(log unhandled events at WARN level).

## Event Type Scoping

Two equivalent ways to scope to an event type:

```kotlin
handling {
    // Generic reified type parameter (works at any nesting level)
    on<MessageEvent> { /* ... */ }
    on<CallbackQueryEvent> { /* ... */ }

    // Convenience shorthands (root level only)
    onMessageEvent { /* ... */ }
    onCallbackQueryEvent { /* ... */ }
}
```

Inside an already-scoped route, use `on<T>` for further type narrowing:

```kotlin
onMessageEvent {
    on<MessageEvent> { /* further narrowing */ }
}
```

## Command Handling

### Simple Commands

```kotlin
handling {
    commandEndpoint("start") { replyMessage("Welcome!") }
    commandEndpoint("ping") { replyMessage("Pong!") }
}
```

`commandEndpoint` is a terminal operation that matches the `/command` and `/command@bot_username` formats.

### Commands with Typed Arguments

```kotlin
class EchoArgs : BotArguments("Echo a message") {
    val message: String by requireArgument("The message to echo")
    val count: Int by optionalArgument("Repeat count", default = 1)
}

handling {
    command("echo", ::EchoArgs) {
        repeat(arguments.count) {
            replyMessage(arguments.message)
        }
    }
}
```

Supported argument delegates:

| Delegate                  | Description                               | Example                                                                           |
|---------------------------|-------------------------------------------|-----------------------------------------------------------------------------------|
| `requireArgument<T>`      | Required positional argument              | `val name: String by requireArgument("Name")`                                     |
| `optionalArgument<T>`     | Optional positional argument              | `val age: Int by optionalArgument("Age", default = 0)`                            |
| `enumArgument<T>`         | Required enum argument (case-insensitive) | `val op: Operation by enumArgument("Operation")`                                  |
| `optionalEnumArgument<T>` | Optional enum argument (case-insensitive) | `val op: Operation? by optionalEnumArgument("Operation")`                         |
| `optionalEnumArgument<T>` | Optional enum argument with default       | `val op: Operation by optionalEnumArgument("Operation", default = Operation.ADD)` |

Supported types for `requireArgument` and `optionalArgument`: `String`, `Int`, `Long`, `Double`, `Boolean`
(accepts "true"/"false", "yes"/"no", "on"/"off", "1"/"0", case-insensitive).

Use `.validate { }` on any argument to add custom validation:

```kotlin
val port: Int by requireArgument<Int>("Port").validate {
    require(it in 1..65535) { "Port must be between 1 and 65535" }
}
```

### Command with Error Handling

```kotlin
class KickArgs : BotArguments("Kick a user") {
    val username: String by requireArgument("User to kick")
}

command("kick", ::KickArgs, onError = { e ->
    replyMessage("Error: ${e.message}")
}) {
    val username = arguments.username
}
```

Default error handler calls `replyMessage(text = it.generateHelpText()).getOrThrow()`.

### Subcommands

```kotlin
handling {
    command("admin") {
        handle { replyMessage("Admin help") }
        subCommandEndpoint("status") { replyMessage("System status") }
        subCommand("user") {
            subCommandEndpoint("list") { listUsers() }
            subCommandEndpoint("add") { addUser() }
        }
    }
}
```

### onCommand Scope

Use `onCommand` to scope routes to command messages only. This is useful for scoping authentication middleware to
commands only, preventing rejection of non-command text messages:

```kotlin
handling {
    onCommand {
        // Only command messages reach here
        command("admin") {
            handle { /* ... */ }
        }
    }
}
```

## Message Matchers

All matchers come in two variants: composable (without `when` prefix, takes a build lambda) and terminal (with `when`
prefix, takes a handler directly).

### Text Matchers

```kotlin
onMessageEvent {
    whenMessageEventText("hello") { /* exact match */ }
    whenMessageEventTextRegex(Regex("(?i)^hello")) { /* regex match */ }
    whenMessageEventTextContains("help", ignoreCase = true) { /* contains */ }
    whenMessageEventTextStartsWith("/admin") { /* starts with */ }
    whenMessageEventTextEndsWith("!!") { /* ends with */ }
}
```

### Media Matchers

```kotlin
onMessageEvent {
    whenMessageEventPhoto { /* photo */ }
    whenMessageEventVideo { /* video */ }
    whenMessageEventDocument { /* document */ }
    whenMessageEventAudio { /* audio */ }
    whenMessageEventSticker { /* sticker */ }
    whenMessageEventVoice { /* voice */ }
    whenMessageEventVideoNote { /* video note */ }
    whenMessageEventAnimation { /* animation (GIF) */ }
    whenMessageEventContact { /* contact */ }
    whenMessageEventLocation { /* location */ }
    whenMessageEventVenue { /* venue */ }
    whenMessageEventPoll { /* poll */ }
    whenMessageEventDice { /* dice */ }
    whenMessageEventDiceWithEmoji("\uD83C\uDFB2") { /* specific dice emoji */ }
}
```

### User/Chat Filters

```kotlin
onMessageEvent {
    whenMessageEventFromUser(123456789L) { /* from specific user */ }
    whenMessageEventFromUsers(setOf(111L, 222L)) { /* from any of these users */ }
    whenMessageEventInChat(-1001234567890L) { /* in specific chat */ }
    whenMessageEventInChats(setOf(-100L, -200L)) { /* in any of these chats */ }
}
```

### Reply and Forward Matchers

```kotlin
onMessageEvent {
    whenMessageEventReply { /* is a reply */ }
    whenMessageEventReplyTo(messageId = 42L) { /* reply to specific message */ }
    whenMessageEventForwarded { /* forwarded message */ }
    whenMessageEventForwardedFromUser(123L) { /* forwarded from user */ }
    whenMessageEventForwardedFromChat(-100L) { /* forwarded from chat */ }
    whenMessageEventForwardedHiddenUser { /* forwarded from hidden user */ }
}
```

### Chat Type Filters

```kotlin
onMessageEvent {
    whenMessageEventPrivateChat { /* private chat */ }
    whenMessageEventGroupChat { /* group chat */ }
    whenMessageEventSupergroupChat { /* supergroup */ }
    whenMessageEventChannel { /* channel */ }
}
```

### Service Message Matchers

```kotlin
onMessageEvent {
    whenMessageEventNewChatMembers { /* new members joined */ }
    whenMessageEventLeftChatMember { /* member left */ }
    whenMessageEventNewChatTitle { /* chat title changed */ }
    whenMessageEventNewChatPhoto { /* chat photo changed */ }
    whenMessageEventDeleteChatPhoto { /* chat photo deleted */ }
    whenMessageEventPinnedMessage { /* message pinned */ }
    whenMessageEventGroupCreated { /* group created */ }
    whenMessageEventSupergroupCreated { /* supergroup created */ }
    whenMessageEventChannelCreated { /* channel created */ }
    whenMessageEventVideoChatStarted { /* video chat started */ }
    whenMessageEventVideoChatEnded { /* video chat ended */ }
    whenMessageEventVideoChatScheduled { /* video chat scheduled */ }
    whenMessageEventVideoChatParticipantsInvited { /* participants invited */ }
    whenMessageEventForumTopicCreated { /* forum topic created */ }
    whenMessageEventForumTopicEdited { /* forum topic edited */ }
    whenMessageEventForumTopicClosed { /* forum topic closed */ }
    whenMessageEventForumTopicReopened { /* forum topic reopened */ }
    whenMessageEventGeneralForumTopicHidden { /* general topic hidden */ }
    whenMessageEventGeneralForumTopicUnhidden { /* general topic unhidden */ }
    whenMessageEventGiveawayCreated { /* giveaway created */ }
    whenMessageEventGiveawayCompleted { /* giveaway completed */ }
    whenMessageEventBoostAdded { /* boost added */ }
    whenMessageEventMigrateToSupergroup { /* migrated to supergroup */ }
    whenMessageEventMigrateFromGroup { /* migrated from group */ }
    whenMessageEventMessageAutoDeleteTimerChanged { /* auto-delete timer changed */ }
    whenMessageEventWebAppData { /* web app data received */ }
}
```

## Callback Query Matchers

```kotlin
onCallbackQueryEvent {
    whenCallbackQueryEventData("confirm") { /* exact data match */ }
    whenCallbackQueryEventDataRegex(Regex("action_\\d+")) { /* regex match */ }
    whenCallbackQueryEventDataContains("yes", ignoreCase = true) { /* contains */ }
    whenCallbackQueryEventDataStartsWith("action_") { /* starts with */ }
    whenCallbackQueryEventFromUser(123L) { /* from specific user */ }
    whenCallbackQueryEventInChat(-100L) { /* in specific chat */ }
}
```

## Inline Query Matchers

```kotlin
onInlineQueryEvent {
    whenInlineQueryEventQuery("search") { /* exact query */ }
    whenInlineQueryEventQueryRegex(Regex("\\d+")) { /* regex */ }
    whenInlineQueryEventQueryContains("help") { /* contains */ }
    whenInlineQueryEventQueryStartsWith("find:") { /* starts with */ }
    whenInlineQueryEventFromUser(123L) { /* from user */ }
}

onChosenInlineResultEvent {
    whenChosenInlineResultEventMatchAny { /* any chosen inline result */ }
    whenChosenInlineResultEventFromUser(123L) { /* from specific user */ }
}
```

## Edited Message Matchers

```kotlin
onEditedMessageEvent {
    whenEditedMessageEventText("edited text") { /* exact text match */ }
    whenEditedMessageEventTextRegex(Regex("edited")) { /* regex */ }
    whenEditedMessageEventTextContains("edit") { /* contains */ }
    whenEditedMessageEventTextStartsWith("[edit]") { /* starts with */ }
    whenEditedMessageEventTextEndsWith("...") { /* ends with */ }
    whenEditedMessageEventPhoto { /* edited photo */ }
    whenEditedMessageEventVideo { /* edited video */ }
    whenEditedMessageEventDocument { /* edited document */ }
    whenEditedMessageEventAudio { /* edited audio */ }
    whenEditedMessageEventFromUser(123L) { /* from specific user */ }
    whenEditedMessageEventInChat(-100L) { /* in specific chat */ }
}
```

## Edited Channel Post Matchers

```kotlin
onEditedChannelPostEvent {
    whenEditedChannelPostEventText("updated text") { /* exact text match */ }
    whenEditedChannelPostEventTextRegex(Regex("updated")) { /* regex match */ }
    whenEditedChannelPostEventFromChat(-100L) { /* from specific channel */ }
}
```

## Poll and Reaction Matchers

```kotlin
onPollEvent {
    whenPollEventPollId("poll_123") { /* specific poll */ }
}

onPollAnswerEvent {
    whenPollAnswerEventPollId("poll_123") { /* answer for specific poll */ }
    whenPollAnswerEventFromUser(123L) { /* answer from specific user */ }
}

onMessageReactionEvent {
    whenMessageReactionEventInChat(-100L) { /* reaction in specific chat */ }
    whenMessageReactionEventToMessage(42L) { /* reaction to specific message */ }
}

onMessageReactionCountEvent {
    whenMessageReactionCountEventInChat(-100L) { /* reaction count in specific chat */ }
}
```

## Chat Event Matchers

```kotlin
onChatJoinRequestEvent {
    whenChatJoinRequestEventFromChat(-100L) { /* join request from chat */ }
    whenChatJoinRequestEventFromUser(123L) { /* join request from user */ }
}

onChatMemberEvent {
    whenChatMemberEventInChat(-100L) { /* member change in chat */ }
}

onMyChatMemberEvent {
    whenMyChatMemberEventInChat(-100L) { /* bot member change in chat */ }
}

onChatBoostEvent {
    whenChatBoostEventInChat(-100L) { /* boost in chat */ }
}

onRemovedChatBoostEvent {
    whenRemovedChatBoostEventInChat(-100L) { /* removed boost in chat */ }
}
```

## Payment Matchers

```kotlin
onShippingQueryEvent {
    whenShippingQueryEventFromUser(123L) { /* from user */ }
    whenShippingQueryEventWithPayload("shipping") { /* with payload */ }
}

onPreCheckoutQueryEvent {
    whenPreCheckoutQueryEventFromUser(123L) { /* from user */ }
    whenPreCheckoutQueryEventWithPayload("order_123") { /* with payload */ }
    whenPreCheckoutQueryEventWithCurrency("USD") { /* with currency */ }
}

onPurchasedPaidMediaEvent {
    whenPurchasedPaidMediaEventFromUser(123L) { /* from user */ }
}
```

## Business Matchers

```kotlin
onBusinessConnectionEvent {
    whenBusinessConnectionEventFromUser(123L) { /* from user */ }
}

onBusinessMessageEvent {
    whenBusinessMessageEventFromUser(123L) { /* from user */ }
    whenBusinessMessageEventInChat(-100L) { /* in chat */ }
}

onEditedBusinessMessageEvent {
    whenEditedBusinessMessageEventFromUser(123L) { /* from user */ }
    whenEditedBusinessMessageEventInChat(-100L) { /* in chat */ }
}

onDeletedBusinessMessagesEvent {
    whenDeletedBusinessMessagesEventInChat(-100L) { /* in chat */ }
}
```

## Conditional Routing

### match / whenMatch

Custom predicate-based routing.

```kotlin
onMessageEvent {
    // Composable: creates a child route for further nesting
    match({ it.event.message.text?.length ?: 0 > 10 }) {
        // Further nested matchers
    }

    // Terminal: takes a handler directly
    whenMatch({ it.event.message.text?.length ?: 0 > 10 }) {
        client.sendMessage(event.message.chat.id, "Long message!")
    }
}
```

## Composite Matchers

```kotlin
onMessageEvent {
    // All predicates must match
    whenAllOf(
        { it.event.message.text != null },
        { it.event.message.chat.id == 100L }
    ) { println("Text message in chat 100") }

    // Any predicate must match
    whenAnyOf(
        { it.event.message.photo != null },
        { it.event.message.video != null }
    ) { println("Photo or video") }

    // Predicate must NOT match
    whenNot({ it.event.message.text?.startsWith("/") == true }) {
        println("Not a command")
    }

    // Composable versions (for nesting)
    allOf(
        { it.event.message.text != null },
        { it.event.message.chat.id == 100L }
    ) {
        // Further nested matchers
    }
}
```

## Middleware

Conditional execution with predicate. The middleware passes when the predicate returns `true`, and calls `onRejected`
when it returns `false`.

### middleware {}

Composable version that creates a guarded scope for child routes:

```kotlin
onMessageEvent {
    middleware(
        predicate = { it.event.message.from?.id in adminUserIds },
        onRejected = { replyMessage("Unauthorized") }
    ) {
        handle { /* protected handler */ }
    }
}
```

### whenMiddleware()

Terminal version that combines middleware with a single handler:

```kotlin
onMessageEvent {
    whenMiddleware(
        predicate = { it.event.message.from?.id in adminUserIds },
        onRejected = { replyMessage("Unauthorized") }
    ) { replyMessage("Welcome, admin!") }
}
```

### Custom Reusable Middleware DSL

```kotlin
// Define a reusable authentication middleware
fun HandlerRoute<MessageEvent>.requireAuth(
    authService: AuthService,
    onRejected: suspend HandlerBotCall<MessageEvent>.() -> Unit,
    build: HandlerRoute<MessageEvent>.() -> Unit
) = middleware(
    predicate = { context -> authService.isAdmin(context.event.message.from?.id) },
    onRejected = onRejected,
    build = build
)

// Usage
onCommand {
    requireAuth(
        authService = authService,
        onRejected = { replyMessage("Unauthorized") }
    ) {
        command("admin") {
            handle { replyMessage("Admin panel") }
            subCommandEndpoint("status") { replyMessage("OK") }
        }
    }
}
```

## Including Routes

Include pre-built route nodes from other modules.

```kotlin
val adminRoutes = handling {
    commandEndpoint("admin") { /* ... */ }
}

handling {
    include(adminRoutes)
    commandEndpoint("start") { /* ... */ }
}
```

## Naming Convention

- Functions starting with `when` (e.g., `whenMessageEventText`) are **terminal** -- they take a handler directly and
  cannot be nested further.
- Functions without `when` prefix (e.g., `match`, `allOf`, `anyOf`, `not`) are **composable** -- they take a build
  lambda for further nesting.

## Context Extensions

When inside a `MessageEvent` scope (e.g., `commandEndpoint`, `onMessageEvent`), the following helper functions are
available:

### Sending Messages

```kotlin
commandEndpoint("demo") {
    sendMessage("Hello!")              // Send to current chat
    replyMessage("This is a reply")    // Reply to current message
    val chatId = event.extractChatId() // Extract chat ID from event
    val userId = event.extractUserId() // Extract user ID from event
}
```

### Available Send/Reply Helpers

Each media type has both `send*` and `reply*` variants:

| Type        | Send Method      | Reply Method      |
|-------------|------------------|-------------------|
| Text        | `sendMessage`    | `replyMessage`    |
| Photo       | `sendPhoto`      | `replyPhoto`      |
| Audio       | `sendAudio`      | `replyAudio`      |
| Document    | `sendDocument`   | `replyDocument`   |
| Video       | `sendVideo`      | `replyVideo`      |
| Animation   | `sendAnimation`  | `replyAnimation`  |
| Voice       | `sendVoice`      | `replyVoice`      |
| Video Note  | `sendVideoNote`  | `replyVideoNote`  |
| Media Group | `sendMediaGroup` | `replyMediaGroup` |
| Location    | `sendLocation`   | `replyLocation`   |
| Venue       | `sendVenue`      | `replyVenue`      |
| Contact     | `sendContact`    | `replyContact`    |
| Poll        | `sendPoll`       | `replyPoll`       |
| Dice        | `sendDice`       | `replyDice`       |
| Game        | `sendGame`       | `replyGame`       |

Additional helpers:

- `sendChatAction(action)` - Send typing/upload action
- `sendMessageWithKeyboard(text, keyboard)` - Send text with inline keyboard DSL
- `replyMessageWithKeyboard(text, keyboard)` - Reply with inline keyboard DSL
- `sendPhotoWithKeyboard(photo, text, keyboard)` - Send photo with inline keyboard DSL
- `replyPhotoWithKeyboard(photo, text, keyboard)` - Reply with inline keyboard DSL

## Next Steps

- [Conversations](Conversations) - Multi-turn interactions
- [Interceptors](Interceptors) - Middleware pipeline
