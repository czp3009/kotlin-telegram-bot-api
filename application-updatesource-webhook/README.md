# Webhook Update Source

A webhook-based update source module for the Telegram Bot Application framework. This module provides an
implementation of `TelegramUpdateSource` that receives updates via HTTP webhook from Telegram's servers.

## Features

- **Embedded Ktor Server**: Uses Ktor's embedded server to receive webhook requests
- **Flexible Engine Support**: Works with any Ktor application engine (Netty, CIO, Jetty, etc.)
- **Graceful Shutdown**: Proper lifecycle management with graceful shutdown support
- **Multiplatform**: Supports JVM, Android, JS (Node.js), WASM (Node.js), and native targets

## Installation

This module extends the [application](../application) module. First add the application dependency, then add this
module:

```kotlin
implementation("com.hiczp.telegram.bot:application:$version")
implementation("com.hiczp.telegram.bot:application-updatesource-webhook:$version")
```

You also need to add a Ktor server engine dependency.
See [Ktor Server Engines](https://ktor.io/docs/server-engines.html) for available options.

## Usage

### Basic Setup

```kotlin
val updateSource = WebhookTelegramUpdateSource(
    applicationEngineFactory = CIO,
    path = "/webhook",
    configureEngine = {
        connector {
            host = "0.0.0.0"
            port = 8080
        }
    }
)

val app = TelegramBotApplication(
    client = client,
    updateSource = updateSource,
    eventDispatcher = dispatcher
)

app.start()
app.join() // Suspend until stopped
// app.stop(5.seconds) for graceful shutdown
```

### SSL Configuration

Telegram requires HTTPS for webhooks. The recommended approach is to use a reverse proxy like nginx to handle SSL
termination:

```
[Telegram] --HTTPS--> [nginx] --HTTP--> [Your Bot :8080]
```

Example nginx configuration:

```nginx
server {
    listen 443 ssl;
    server_name your-domain.com;

    ssl_certificate /path/to/fullchain.pem;
    ssl_certificate_key /path/to/privkey.pem;

    location /webhook {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

With this setup, your bot can use plain HTTP:

```kotlin
val updateSource = WebhookTelegramUpdateSource(
    applicationEngineFactory = CIO,
    path = "/webhook",
    configureEngine = {
        connector {
            host = "127.0.0.1"
            port = 8080
        }
    }
)
```

If you prefer to handle SSL directly in your application:

```kotlin
val updateSource = WebhookTelegramUpdateSource(
    applicationEngineFactory = CIO,
    path = "/webhook",
    configureEngine = {
        connector {
            host = "0.0.0.0"
            port = 8443
        }
        sslConnector(
            keyStore = KeyStore.getInstance("PKCS12"),
            keyAlias = "myKeyAlias",
            keyStorePassword = { "keystorePassword".toCharArray() },
            privateKeyPassword = { "privateKeyPassword".toCharArray() }
        ) {
            port = 8443
            keyStorePath = File("/path/to/keystore.p12")
        }
    }
)
```

For more SSL configuration options, see [Ktor SSL documentation](https://ktor.io/docs/server-ssl.html).

### Custom Application Configuration

You can customize the Ktor application using the `configureApplication` parameter:

```kotlin
val updateSource = WebhookTelegramUpdateSource(
    applicationEngineFactory = CIO,
    path = "/webhook",
    configureEngine = {
        connector {
            host = "0.0.0.0"
            port = 8080
        }
    },
    configureApplication = {
        // Add custom Ktor application configuration
        install(CallLogging) {
            level = Level.INFO
        }
    }
)
```

### Setting the Webhook URL

After starting your bot, you need to register your webhook URL with Telegram:

```kotlin
// Register the webhook with Telegram
client.setWebhook(
    url = "https://your-domain.com:8443/webhook"
).getOrThrow()
```

To delete the webhook (e.g., when switching back to long polling):

```kotlin
client.deleteWebhook().getOrThrow()
```

For more details on webhook requirements and options,
see [Telegram Webhooks Guide](https://core.telegram.org/bots/webhooks).

## Platform Support

| Platform       | Support |
|----------------|---------|
| JVM            | ✅       |
| Android        | ✅       |
| JS             | ✅       |
| WASM           | ✅       |
| Linux          | ✅       |
| macOS          | ✅       |
| Windows        | ✅       |
| iOS            | ✅       |
| watchOS        | ✅       |
| tvOS           | ✅       |
| Android Native | ✅       |

**Note**: The JS and WASM targets only support Node.js runtime (not browser) since HTTP server functionality is not
available in browsers.

## Exception Handling

The webhook update source handles exceptions as follows:

- **CancellationException**: Re-thrown to propagate coroutine cancellation
- **TelegramBotShuttingDownException**: Re-thrown during graceful shutdown
- **Other exceptions**: Logged and result in a 500 Internal Server Error response

Telegram will retry failed webhook requests, so ensure your handlers are idempotent if needed.

## Comparison with Long Polling

| Feature           | Webhook                      | Long Polling              |
|-------------------|------------------------------|---------------------------|
| Server Required   | Yes (public HTTPS endpoint)  | No                        |
| Latency           | Lower (push-based)           | Higher (poll-based)       |
| Firewall Friendly | Requires inbound connections | Only outbound connections |
| SSL Certificate   | Required                     | Not required              |
| Resource Usage    | Lower when idle              | Continuous polling        |
| Setup Complexity  | Higher                       | Lower                     |

Choose webhook when:

- You need the lowest possible latency
- You have a server with a public IP and valid SSL certificate
- You want to reduce unnecessary API calls

Choose long polling when:

- You're behind a firewall or NAT
- You don't have an SSL certificate
- You want a simpler setup

## Related Documentation

- [Telegram Webhooks Guide](https://core.telegram.org/bots/webhooks)
- [Ktor Server Engines](https://ktor.io/docs/server-engines.html)
- [Application Module](../application) - Main application framework
