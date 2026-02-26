package com.hiczp.telegram.bot.application.command

/**
 * Parser for Telegram bot commands.
 *
 * Parses commands in the format `/command[@username] [args]`, supporting:
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
     * @return A [ParsedCommand] if the text is a valid command, or `null` if the text is null, blank, or doesn't start with `/`.
     */
    fun parse(text: String?): ParsedCommand? {
        if (text.isNullOrBlank() || !text.startsWith("/")) return null
        val parts = text.trim().split(Regex("\\s+"), limit = 2)
        val rawCommand = parts.first().trimStart('/')
        val command = rawCommand.substringBefore('@')
        val username = rawCommand.substringAfter('@', "").ifEmpty { null }
        val rawArgs = parts.getOrNull(1)
        val args = rawArgs?.let { tokenize(it) } ?: emptyList()
        return ParsedCommand(
            text = text,
            command = command,
            username = username,
            rawArgs = rawArgs,
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
