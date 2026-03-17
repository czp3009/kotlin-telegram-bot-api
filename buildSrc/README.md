# buildSrc

This directory contains custom Gradle tasks and build logic for the Telegram Bot API project.

## Overview

The `buildSrc` module provides:

- **Code generation tasks** - Generate Kotlin API bindings from the OpenAPI specification
- **Multiplatform configuration** - Shared configuration for all Kotlin multiplatform targets

## Gradle Tasks

### `downloadSwagger`

Downloads the latest Telegram Bot API OpenAPI/Swagger specification from GitHub.

**Group:** `swagger`

**Usage:**

```bash
./gradlew downloadSwagger
```

**Configuration:**

| Property     | Default                                         | Description                               |
|--------------|-------------------------------------------------|-------------------------------------------|
| `repository` | `czp3009/telegram-bot-api-swagger`              | GitHub repository in `owner/repo` format  |
| `fileName`   | `telegram-bot-api.json`                         | File name to download from release assets |
| `outputFile` | `build/generated/swagger/telegram-bot-api.json` | Destination file path                     |

**Output:** Downloads the OpenAPI specification to `build/generated/swagger/telegram-bot-api.json`

---

### `generateKtorfitInterfaces`

Generates Kotlin code from the Telegram Bot API OpenAPI/Swagger specification.

**Group:** `codegen`

**Usage:**

```bash
./gradlew generateKtorfitInterfaces
```

**Configuration:**

| Property      | Default                 | Description                                    |
|---------------|-------------------------|------------------------------------------------|
| `swaggerFile` | (required)              | Path to the OpenAPI/Swagger JSON specification |
| `outputDir`   | `src/commonMain/kotlin` | Output directory for generated code            |

**Generated Artifacts:**

| Directory/File      | Description                                                                       |
|---------------------|-----------------------------------------------------------------------------------|
| `model/`            | Serializable data classes with `@SerialName` for snake_case mapping               |
| `model/Bodies.kt`   | Extension functions for POST operations with scattered parameters                 |
| `form/`             | Multipart form wrappers for file uploads                                          |
| `form/Forms.kt`     | Extension functions that accept `InputFile` types                                 |
| `query/Queries.kt`  | Extension functions for JSON-serialized GET parameters                            |
| `TelegramBotApi.kt` | Ktorfit interface with `@GET`, `@POST` annotations                                |
| `union/`            | Generic sealed classes for response union types (e.g., `Union<Message, Boolean>`) |

**Special Type Handling:**

- **ReplyMarkup** - Collected types implement a sealed interface with `@JsonClassDiscriminator("type")`
- **IncomingUpdate** - Types in `Update`'s optional fields implement `IncomingUpdate` interface
- **MaybeInaccessibleMessage** - Uses `Message` type for deserialization (field-overlapping union)
- **Response Unions** - Generic `Union<T1, T2>`, `Union3<T1, T2, T3>` classes for mixed-type responses

**Preserved Directory:**

The `type/` directory contains handwritten code and is preserved during regeneration:

- `TelegramResponse.kt` - Response wrapper with success/error handling
- `InputFile.kt` - File upload sealed type

---

## Typical Workflow

To regenerate all protocol code from the latest Telegram Bot API specification:

```bash
# Step 1: Download the latest OpenAPI spec
./gradlew downloadSwagger

# Step 2: Generate Ktorfit interfaces and models
./gradlew generateKtorfitInterfaces
```

## Build Dependencies

The `buildSrc` module uses:

- **Jackson Databind** - JSON parsing for OpenAPI specification
- **KotlinPoet** - Kotlin code generation

## Multiplatform Extension

The `MultiplatformExtension.kt` provides a `configureAllTargets(namespace)` extension function that configures all
supported Kotlin multiplatform targets:

- **JVM** (toolchain 17)
- **Android Library** (minSdk 34, compileSdk 36)
- **JS** (browser, nodejs)
- **WASM** (browser, nodejs, d8)
- **Native**: Windows, Linux, macOS, iOS, watchOS, tvOS, Android Native

**Usage in module `build.gradle.kts`:**

```kotlin
kotlin {
    configureAllTargets("com.example.myapp")
}
```
