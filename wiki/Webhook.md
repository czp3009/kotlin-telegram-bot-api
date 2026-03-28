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

## SSL Configuration

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
        host = "0.0.0.0",
        port = 8443,
        keyStorePath = Path.of("keystore.jks"),
        keyStorePassword = "changeit"
    )
}
```

## Registering Webhook

```bash
curl -X POST "https://api.telegram.org/bot<TOKEN>/setWebhook" \
     -d "url=https://bot.example.com:8443/webhook"
```

## Long Polling vs Webhook

| Aspect    | Long Polling            | Webhook                     |
|-----------|-------------------------|-----------------------------|
| Server    | Not required            | Required (public IP, HTTPS) |
| Latency   | Polling interval        | Near real-time              |
| Resources | Continuous polling      | Event-driven                |
| Use case  | Development, small bots | Production, high-traffic    |

## Related Links

- [Telegram Webhook Guide](https://core.telegram.org/bots/webhooks)
- [Ktor Server Documentation](https://ktor.io/docs/server.html)
