package com.hiczp.telegram.bot.application.command

import kotlin.io.encoding.Base64

/**
 * Parser for Telegram bot commands.
 *
 * Parses commands in the format `/command[@username] [arguments]`, supporting:
 * - Simple commands: `/start`
 * - Commands with bot username: `/help@mybot`
 * - Commands with arguments: `/search hello world`
 * - Quoted arguments: `/echo "hello world"`
 * - Escaped characters: `/echo \"quoted\"`
 *
 * @see ParsedCommand
 */
object CommandParser {
    /**
     * Parses a text string into a [ParsedCommand].
     *
     * @param text The text to parse, typically from a Telegram message.
     * @param deepLinkingEncoded When enabled, the first argument is decoded from base64 encoding.
     *   This is useful for handling Telegram deep-linking URLs where parameters are base64 encoded.
     *   For example, `/start aGVsbG8` will decode "aGVsbG8" to "hello".
     *   Note: Only the first argument (the deep linking parameter) is decoded; additional arguments are preserved as-is.
     * @return A [ParsedCommand] if the text is a valid command, or `null` if the text is null, blank, or doesn't start with `/`.
     */
    fun parse(text: String?, deepLinkingEncoded: Boolean = false): ParsedCommand? {
        if (text.isNullOrBlank() || !text.startsWith("/")) return null
        val parts = text.trim().split(Regex("\\s+"), limit = 2)
        val rawCommand = parts.first().trimStart('/')
        val command = rawCommand.substringBefore('@')
        val username = rawCommand.substringAfter('@', "").ifEmpty { null }
        val rawArgs = parts.getOrNull(1)
        val processedRawArgs = if (deepLinkingEncoded && rawArgs != null) {
            // Decode the first argument from base64, preserve any additional arguments
            val argParts = rawArgs.split(Regex("\\s+"), limit = 2)
            val firstArg = argParts.first()
            val remainingArgs = argParts.getOrNull(1)
            val decodedFirstArg = try {
                Base64.decode(firstArg).decodeToString()
            } catch (_: Exception) {
                // If decoding fails, use the original argument
                firstArg
            }
            if (remainingArgs != null) {
                "$decodedFirstArg $remainingArgs"
            } else {
                decodedFirstArg
            }
        } else {
            rawArgs
        }
        val args = processedRawArgs?.let { tokenize(it) } ?: emptyList()
        return ParsedCommand(
            text = text,
            command = command,
            username = username,
            rawArgs = processedRawArgs,
            args = args,
        )
    }

    private fun tokenize(input: String): List<String> {
        val result = mutableListOf<String>()
        val current = StringBuilder()
        var inQuotes = false
        var quoteChar = ' '
        var isEscaped = false

        for (ch in input) {
            when {
                isEscaped -> {
                    current.append(ch)
                    isEscaped = false
                }

                ch == '\\' -> {
                    isEscaped = true
                }

                (ch == '\"' || ch == '\'') -> {
                    if (inQuotes) {
                        if (ch == quoteChar) {
                            inQuotes = false
                        } else {
                            current.append(ch)
                        }
                    } else {
                        inQuotes = true
                        quoteChar = ch
                    }
                }

                ch.isWhitespace() -> {
                    if (inQuotes) {
                        current.append(ch)
                    } else if (current.isNotEmpty()) {
                        result.add(current.toString())
                        current.clear()
                    }
                }

                else -> {
                    current.append(ch)
                }
            }
        }

        if (current.isNotEmpty()) {
            result.add(current.toString())
        }

        return result
    }
}
