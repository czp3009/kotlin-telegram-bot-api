# Webhook

The webhook update source lives in `:application-updatesource-webhook`. It receives Telegram updates through an
embedded Ktor server.

## Installation

```kotlin
implementation("com.hiczp:telegram-bot-api-application:$version")
implementation("com.hiczp:telegram-bot-api-application-updatesource-webhook:$version")
```

Add a Ktor server engine dependency such as Netty or CIO in the application that runs the bot.

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
    eventDispatcher = dispatcher,
    interceptors = listOf(loggingInterceptor())
)

app.start()
app.join()
```

## Manual Setup

```kotlin
import com.hiczp.telegram.bot.application.TelegramBotApplication
import com.hiczp.telegram.bot.application.updatesource.webhook.WebhookTelegramUpdateSource
import com.hiczp.telegram.bot.client.TelegramBotClient
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

## Factory Parameters

| Parameter                  | Description                                      |
|----------------------------|--------------------------------------------------|
| `botToken`                 | Telegram bot token                               |
| `applicationEngineFactory` | Ktor engine factory                              |
| `path`                     | Webhook endpoint path, default `"/"`             |
| `configureEngine`          | Engine configuration lambda                      |
| `configureApplication`     | Additional Ktor application configuration        |
| `eventDispatcher`          | Dispatcher used by the application               |
| `interceptors`             | Application interceptors                         |
| `coroutineDispatcher`      | Optional dispatcher for application/client scope |
| `onUnrecognizedUpdate`     | Optional callback for unknown update types       |

## Registering The Webhook

After the server is reachable from the public internet, register its URL with Telegram.

```kotlin
client.setWebhook(
    url = "https://bot.example.com/webhook"
).getOrThrow()
```

Delete the webhook before switching back to long polling:

```kotlin
client.deleteWebhook().getOrThrow()
```

## HTTPS

Telegram requires HTTPS for webhooks. The recommended setup is TLS termination in a reverse proxy.

```nginx
server {
    listen 443 ssl;
    server_name bot.example.com;

    ssl_certificate /path/to/fullchain.pem;
    ssl_certificate_key /path/to/privkey.pem;

    location /webhook {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

The bot can then listen on local HTTP:

```kotlin
val updateSource = WebhookTelegramUpdateSource(
    applicationEngineFactory = Netty,
    path = "/webhook",
    configureEngine = {
        connector {
            host = "127.0.0.1"
            port = 8080
        }
    }
)
```

You can also configure SSL directly in the selected Ktor engine when that is more appropriate for your deployment.

## Lifecycle

- `start()` starts the embedded server and handles webhook requests.
- `stop(gracePeriod)` waits for startup if needed and then stops the embedded server.
- `onFinalize()` is a no-op for webhook mode.

`WebhookTelegramUpdateSource` can only be started once per instance.

## Exception Semantics

- Invalid request body: logs and responds `400 Bad Request`.
- `TelegramBotShuttingDownException`: re-thrown for framework shutdown.
- `CancellationException`: re-thrown for coroutine cancellation.
- Other handler exceptions: logged and re-thrown.

Because handler exceptions are re-thrown, Telegram can retry failed webhook requests. Make handlers idempotent when a
retry could repeat a side effect.

## Long Polling Vs Webhook

| Aspect      | Long polling                     | Webhook                                   |
|-------------|----------------------------------|-------------------------------------------|
| Server      | Not required                     | Public HTTPS endpoint required            |
| Latency     | Poll interval and network timing | Push-based                                |
| Setup       | Simple                           | Requires HTTPS and routing                |
| Scaling     | Usually one poller               | Can sit behind HTTP infrastructure        |
| Development | Easiest default                  | Useful with tunnels or production ingress |

## Related Links

- [Telegram Webhook Guide](https://core.telegram.org/bots/webhooks)
- [Ktor Server Engines](https://ktor.io/docs/server-engines.html)
