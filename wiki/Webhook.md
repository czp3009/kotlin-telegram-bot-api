# Webhook

The webhook module (`:application-updatesource-webhook`) provides webhook-based update receiving.

## Installation

```kotlin
implementation("com.hiczp:telegram-bot-api-application-updatesource-webhook:$version")
```

## Quick Start

```kotlin
import com.hiczp.telegram.bot.application.TelegramBotApplication
import com.hiczp.telegram.bot.application.updatesource.webhook.webhook
import io.ktor.server.netty.Netty

val app = TelegramBotApplication.webhook(
    botToken = "YOUR_TOKEN",
    applicationEngineFactory = Netty,
    path = "/webhook",
    configureEngine = {
        connector {
            host = "0.0.0.0"
            port = 8443
        }
    },
    eventDispatcher = dispatcher
)

app.start()
app.join()
```

## Manual Setup

For more control, create the components separately.

```kotlin
import com.hiczp.telegram.bot.client.TelegramBotClient
import com.hiczp.telegram.bot.application.TelegramBotApplication
import com.hiczp.telegram.bot.application.updatesource.webhook.WebhookTelegramUpdateSource
import io.ktor.server.netty.Netty

val client = TelegramBotClient(botToken = "YOUR_TOKEN")

val updateSource = WebhookTelegramUpdateSource(
    applicationEngineFactory = Netty,
    path = "/webhook",
    configureEngine = {
        connector {
            host = "0.0.0.0"
            port = 8443
        }
    }
)

val app = TelegramBotApplication(
    client = client,
    updateSource = updateSource,
    interceptors = emptyList(),
    eventDispatcher = dispatcher
)
```

## Parameters

### webhook() Factory

| Parameter                  | Type                                             | Default       | Description                            |
|----------------------------|--------------------------------------------------|---------------|----------------------------------------|
| `botToken`                 | `String`                                         | required      | Telegram bot token                     |
| `applicationEngineFactory` | `ApplicationEngineFactory`                       | required      | Ktor engine (Netty, CIO, etc.)         |
| `path`                     | `String`                                         | `"/"`         | Webhook endpoint path                  |
| `configureEngine`          | `TConfiguration.() -> Unit`                      | `{}`          | Engine configuration (host, port, SSL) |
| `configureApplication`     | `Application.() -> Unit`                         | `{}`          | Additional Ktor application config     |
| `eventDispatcher`          | `TelegramEventDispatcher`                        | required      | Event dispatcher                       |
| `interceptors`             | `List<TelegramEventInterceptor>`                 | `emptyList()` | Interceptors                           |
| `coroutineDispatcher`      | `CoroutineDispatcher?`                           | `null`        | Coroutine dispatcher                   |
| `onUnrecognizedUpdate`     | `(suspend (TelegramBotClient, Update) -> Unit)?` | `null`        | Custom unrecognized update handler     |

### WebhookTelegramUpdateSource Constructor

```kotlin
open class WebhookTelegramUpdateSource<TEngine, TConfiguration>(
    applicationEngineFactory: ApplicationEngineFactory<TEngine, TConfiguration>,
    path: String = "/",
    configureEngine: TConfiguration.() -> Unit = {},
    configureApplication: Application.() -> Unit = {},
)
```

## SSL Configuration

Telegram requires HTTPS. Use a reverse proxy for SSL termination, or configure SSL directly.

### Option 1: Reverse Proxy (Recommended)

```nginx
server {
    listen 443 ssl;
    server_name bot.example.com;

    ssl_certificate /path/to/cert.pem;
    ssl_certificate_key /path/to/key.pem;

    location /webhook {
        proxy_pass http://localhost:8080;
    }
}
```

### Option 2: Self-signed Certificate

```kotlin
configureEngine = {
    sslConnector(
        keyStorePath = Path.of("keystore.jks"),
        keyStorePassword = "changeit"
    ) {
        host = "0.0.0.0"
        port = 8443
    }
}
```

## Registering Webhook

After starting your bot, register the webhook URL with Telegram.

```bash
curl -X POST "https://api.telegram.org/bot<TOKEN>/setWebhook" \
     -d "url=https://bot.example.com:8443/webhook"
```

## Lifecycle

The `WebhookTelegramUpdateSource` manages an embedded Ktor server:

- `start()` - Starts the Ktor server and begins receiving updates
- `stop()` - Stops the Ktor server
- `onFinalize()` - Cleanup after shutdown

## Long Polling vs Webhook

| Aspect    | Long Polling            | Webhook                     |
|-----------|-------------------------|-----------------------------|
| Server    | Not required            | Required (public IP, HTTPS) |
| Latency   | Polling interval        | Near real-time              |
| Resources | Continuous polling      | Event-driven                |
| Setup     | Simple, no config       | Requires HTTPS, domain      |
| Use case  | Development, small bots | Production, high-traffic    |
| Scaling   | One instance            | Load balancer possible      |

## Related Links

- [Telegram Webhook Guide](https://core.telegram.org/bots/webhooks)
- [Ktor Server Documentation](https://ktor.io/docs/server.html)
