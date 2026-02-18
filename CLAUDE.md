# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project scope

`kotlin-telegram-bot-api` is a Kotlin Multiplatform Telegram Bot API client.

- Root project includes one main module: `:protocol` (`settings.gradle.kts`).
- Most Telegram API surface is generated from OpenAPI/Swagger.
- Handwritten runtime behavior (error handling, plugins, upload abstractions) lives alongside generated code under
  `protocol/src/commonMain/kotlin/com/hiczp/telegram/bot/protocol/`.

## Commands you will use often

Run from repository root.

```bash
# Full build
./gradlew build

# Protocol module only
./gradlew :protocol:build

# Tests
./gradlew test
./gradlew jvmTest
./gradlew allTests
./gradlew :protocol:jvmTest --tests "com.hiczp.telegram.bot.protocol.TelegramBotApiTest.getUpdates"

# Platform artifacts
./gradlew jvmJar
./gradlew jsJar
./gradlew wasmJsJar

# Clean
./gradlew clean

# Regenerate Telegram API code from latest spec
./gradlew downloadSwagger
./gradlew generateKtorfitInterfaces
```

Integration/live API tests require:

- `BOT_TOKEN`
- `TEST_CHAT_ID`

## Architecture (big picture)

### 1) Codegen-first workflow

Code generation is central to this repository:

1. `DownloadTelegramBotApiSwaggerTask` downloads Telegram Bot API spec JSON.
2. `GenerateKtorfitInterfacesTask` parses that spec and generates Kotlin sources.
3. Kotlin compilations depend on generation (`protocol/build.gradle.kts` wires generation into compilation/KSP test
   tasks).

Generation logic lives in `buildSrc/src/main/kotlin/GenerateKtorfitInterfacesTask.kt`.

### 2) Generated vs handwritten boundary

Under `protocol/src/commonMain/kotlin/com/hiczp/telegram/bot/protocol/`:

- Generated (do not manually edit):
  - `TelegramBotApi.kt`
  - `model/*`
  - `form/*`
  - `query/*`
  - Files marked with `Auto-generated from Swagger specification...`
- Handwritten and preserved:
  - `type/*` (notably `InputFile`, `TelegramResponse`, `IncomingUpdate`)
  - `plugin/*` (`TelegramLongPollingPlugin`, `TelegramServerErrorPlugin`, `TelegramFileDownloadPlugin`)
  - `extension/*`
  - `exception/*`

When API schema changes are needed, regenerate instead of patching generated files.

### 3) Request/response design

- API interface uses Ktorfit annotations (`@GET`, `@POST`, `@Body`, `@Query`).
- Calls return `TelegramResponse<T>` for explicit success/error handling.
- Multipart endpoints use generated `*Form` wrappers and `Forms.kt` helper extensions.
- Query-heavy GET endpoints additionally expose generated `query/Queries.kt` typed extensions that serialize
  non-primitive query parameters before delegation.
- `InputFile` is the upload abstraction used by multipart helpers.

### 4) Runtime plugin model

The library expects Ktor plugins to provide production behavior:

- `TelegramLongPollingPlugin`: adjusts timeouts for `getUpdates`.
- `TelegramServerErrorPlugin`: converts Telegram error payloads into exceptions.
- `TelegramFileDownloadPlugin`: rewrites file download paths.

Important: Telegram can return error payloads with HTTP 200; avoid `expectSuccess` when relying on
`TelegramServerErrorPlugin`.

## Multiplatform/testing layout notes

From `protocol/build.gradle.kts`:

- Toolchain: JVM 21.
- Targets include JVM, JS, WASM, Apple, Linux, Windows, Android Native.
- Test source sets split by runtime availability:
  - `commonTest`
  - `desktopNativeTest` (uses Curl client)
  - `otherNativeTest`
  - `jvmTest` (CIO + logback)
  - `webTest` (JS client)

Use JVM tests for quickest local verification unless task explicitly requires cross-platform coverage.

## Dependency/configuration pointers

- Version catalog: `gradle/libs.versions.toml`.
- Root build is minimal; most behavior is in `protocol/build.gradle.kts` and `buildSrc` custom tasks.
- No Cursor rules (`.cursorrules`, `.cursor/rules`) or Copilot instructions (`.github/copilot-instructions.md`) were
  found in this repository.
