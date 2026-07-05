// Auto-generated from Swagger specification, do not modify this file manually
package com.hiczp.telegram.bot.protocol.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * This object represents a rich formatted text. Currently, it can be either a String for plain text, an Array of RichText, or any of the following types:
 * RichTextBold RichTextItalic RichTextUnderline RichTextStrikethrough RichTextSpoiler RichTextDateTime RichTextTextMention RichTextSubscript RichTextSuperscript RichTextMarked RichTextCode RichTextCustomEmoji RichTextMathematicalExpression RichTextUrl RichTextEmailAddress RichTextPhoneNumber RichTextBankCardNumber RichTextMention RichTextHashtag RichTextCashtag RichTextBotCommand RichTextAnchor RichTextAnchorLink RichTextReference RichTextReferenceLink
 */
@Serializable(with = RichTextSerializer::class)
public sealed interface RichText {
    public companion object {
        public fun of(`value`: String): RichText = RichTextString(value)

        public fun of(`value`: List<RichText>): RichText = RichTextArray(value)
    }
}

@Serializable
public data class RichTextString(
    public val `value`: String,
) : RichText

@Serializable
public data class RichTextArray(
    public val `value`: List<RichText>,
) : RichText

/**
 * A bold text.
 */
@Serializable
public data class RichTextBold(
    /**
     * Type of the rich text, always “bold”
     */
    public val type: String = "bold",
    /**
     * The text
     */
    public val text: RichText,
) : RichText

/**
 * An italicized text.
 */
@Serializable
public data class RichTextItalic(
    /**
     * Type of the rich text, always “italic”
     */
    public val type: String = "italic",
    /**
     * The text
     */
    public val text: RichText,
) : RichText

/**
 * An underlined text.
 */
@Serializable
public data class RichTextUnderline(
    /**
     * Type of the rich text, always “underline”
     */
    public val type: String = "underline",
    /**
     * The text
     */
    public val text: RichText,
) : RichText

/**
 * A strikethrough text.
 */
@Serializable
public data class RichTextStrikethrough(
    /**
     * Type of the rich text, always “strikethrough”
     */
    public val type: String = "strikethrough",
    /**
     * The text
     */
    public val text: RichText,
) : RichText

/**
 * A text covered by a spoiler.
 */
@Serializable
public data class RichTextSpoiler(
    /**
     * Type of the rich text, always “spoiler”
     */
    public val type: String = "spoiler",
    /**
     * The text
     */
    public val text: RichText,
) : RichText

/**
 * Formatted date and time.
 */
@Serializable
public data class RichTextDateTime(
    /**
     * Type of the rich text, always “date_time”
     */
    public val type: String = "date_time",
    /**
     * The text
     */
    public val text: RichText,
    /**
     * The Unix time associated with the entity
     */
    @SerialName("unix_time")
    public val unixTime: Long,
    /**
     * The string that defines the formatting of the date and time. See [date-time entity formatting](https://core.telegram.org/bots/api#date-time-entity-formatting) for more details.
     */
    @SerialName("date_time_format")
    public val dateTimeFormat: String,
) : RichText

/**
 * A mention of a Telegram user by their identifier.
 */
@Serializable
public data class RichTextTextMention(
    /**
     * Type of the rich text, always “text_mention”
     */
    public val type: String = "text_mention",
    /**
     * The text
     */
    public val text: RichText,
    /**
     * The mentioned user
     */
    public val user: User,
) : RichText

/**
 * A subscript text.
 */
@Serializable
public data class RichTextSubscript(
    /**
     * Type of the rich text, always “subscript”
     */
    public val type: String = "subscript",
    /**
     * The text
     */
    public val text: RichText,
) : RichText

/**
 * A superscript text.
 */
@Serializable
public data class RichTextSuperscript(
    /**
     * Type of the rich text, always “superscript”
     */
    public val type: String = "superscript",
    /**
     * The text
     */
    public val text: RichText,
) : RichText

/**
 * A marked text.
 */
@Serializable
public data class RichTextMarked(
    /**
     * Type of the rich text, always “marked”
     */
    public val type: String = "marked",
    /**
     * The text
     */
    public val text: RichText,
) : RichText

/**
 * A monowidth text.
 */
@Serializable
public data class RichTextCode(
    /**
     * Type of the rich text, always “code”
     */
    public val type: String = "code",
    /**
     * The text
     */
    public val text: RichText,
) : RichText

/**
 * A custom emoji.
 */
@Serializable
public data class RichTextCustomEmoji(
    /**
     * Type of the rich text, always “custom_emoji”
     */
    public val type: String = "custom_emoji",
    /**
     * Unique identifier of the custom emoji. Use [getCustomEmojiStickers](https://core.telegram.org/bots/api#getcustomemojistickers) to get full information about the sticker.
     */
    @SerialName("custom_emoji_id")
    public val customEmojiId: String,
    /**
     * Alternative emoji for the custom emoji
     */
    @SerialName("alternative_text")
    public val alternativeText: String,
) : RichText

/**
 * A mathematical expression.
 */
@Serializable
public data class RichTextMathematicalExpression(
    /**
     * Type of the rich text, always “mathematical_expression”
     */
    public val type: String = "mathematical_expression",
    /**
     * The expression in LaTeX format
     */
    public val expression: String,
) : RichText

/**
 * A text with a link.
 */
@Serializable
public data class RichTextUrl(
    /**
     * Type of the rich text, always “url”
     */
    public val type: String = "url",
    /**
     * The text
     */
    public val text: RichText,
    /**
     * URL of the link
     */
    public val url: String,
) : RichText

/**
 * A text with an email address.
 */
@Serializable
public data class RichTextEmailAddress(
    /**
     * Type of the rich text, always “email_address”
     */
    public val type: String = "email_address",
    /**
     * The text
     */
    public val text: RichText,
    /**
     * The email address
     */
    @SerialName("email_address")
    public val emailAddress: String,
) : RichText

/**
 * A text with a phone number.
 */
@Serializable
public data class RichTextPhoneNumber(
    /**
     * Type of the rich text, always “phone_number”
     */
    public val type: String = "phone_number",
    /**
     * The text
     */
    public val text: RichText,
    /**
     * The phone number
     */
    @SerialName("phone_number")
    public val phoneNumber: String,
) : RichText

/**
 * A text with a bank card number.
 */
@Serializable
public data class RichTextBankCardNumber(
    /**
     * Type of the rich text, always “bank_card_number”
     */
    public val type: String = "bank_card_number",
    /**
     * The text
     */
    public val text: RichText,
    /**
     * The bank card number
     */
    @SerialName("bank_card_number")
    public val bankCardNumber: String,
) : RichText

/**
 * A mention by a username.
 */
@Serializable
public data class RichTextMention(
    /**
     * Type of the rich text, always “mention”
     */
    public val type: String = "mention",
    /**
     * The text
     */
    public val text: RichText,
    /**
     * The username
     */
    public val username: String,
) : RichText

/**
 * A hashtag.
 */
@Serializable
public data class RichTextHashtag(
    /**
     * Type of the rich text, always “hashtag”
     */
    public val type: String = "hashtag",
    /**
     * The text
     */
    public val text: RichText,
    /**
     * The hashtag
     */
    public val hashtag: String,
) : RichText

/**
 * A cashtag.
 */
@Serializable
public data class RichTextCashtag(
    /**
     * Type of the rich text, always “cashtag”
     */
    public val type: String = "cashtag",
    /**
     * The text
     */
    public val text: RichText,
    /**
     * The cashtag
     */
    public val cashtag: String,
) : RichText

/**
 * A bot command.
 */
@Serializable
public data class RichTextBotCommand(
    /**
     * Type of the rich text, always “bot_command”
     */
    public val type: String = "bot_command",
    /**
     * The text
     */
    public val text: RichText,
    /**
     * The bot command
     */
    @SerialName("bot_command")
    public val botCommand: String,
) : RichText

/**
 * An anchor.
 */
@Serializable
public data class RichTextAnchor(
    /**
     * Type of the rich text, always “anchor”
     */
    public val type: String = "anchor",
    /**
     * The name of the anchor
     */
    public val name: String,
) : RichText

/**
 * A link to an anchor.
 */
@Serializable
public data class RichTextAnchorLink(
    /**
     * Type of the rich text, always “anchor_link”
     */
    public val type: String = "anchor_link",
    /**
     * The link text
     */
    public val text: RichText,
    /**
     * The name of the anchor. If the name is empty, then the link brings back to the top of the message.
     */
    @SerialName("anchor_name")
    public val anchorName: String,
) : RichText

/**
 * A reference.
 */
@Serializable
public data class RichTextReference(
    /**
     * Type of the rich text, always “reference”
     */
    public val type: String = "reference",
    /**
     * Text of the reference
     */
    public val text: RichText,
    /**
     * The name of the reference
     */
    public val name: String,
) : RichText

/**
 * A link to a reference.
 */
@Serializable
public data class RichTextReferenceLink(
    /**
     * Type of the rich text, always “reference_link”
     */
    public val type: String = "reference_link",
    /**
     * The link text
     */
    public val text: RichText,
    /**
     * The name of the reference
     */
    @SerialName("reference_name")
    public val referenceName: String,
) : RichText

@OptIn(InternalSerializationApi::class)
public object RichTextSerializer : KSerializer<RichText> {
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("com.hiczp.telegram.bot.protocol.model.RichText", SerialKind.CONTEXTUAL)

    override fun deserialize(decoder: Decoder): RichText {
        require(decoder is JsonDecoder) { "Only JSON is supported" }
        val jsonElement = decoder.decodeJsonElement()
        if (jsonElement is JsonPrimitive && jsonElement.isString) {
            return RichTextString(jsonElement.content)
        }
        if (jsonElement is JsonArray) {
            return RichTextArray(decoder.json.decodeFromJsonElement(ListSerializer(RichTextSerializer), jsonElement))
        }
        if (jsonElement is JsonObject) {
            val discriminatorValue = (jsonElement["type"] as? JsonPrimitive)?.content
            when (discriminatorValue) {
                "bold" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "bold") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextBold.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "italic" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "italic") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextItalic.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "underline" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "underline") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextUnderline.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "strikethrough" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "strikethrough") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextStrikethrough.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "spoiler" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "spoiler") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextSpoiler.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "date_time" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "date_time") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextDateTime.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "text_mention" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "text_mention") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextTextMention.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "subscript" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "subscript") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextSubscript.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "superscript" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "superscript") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextSuperscript.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "marked" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "marked") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextMarked.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "code" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "code") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextCode.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "custom_emoji" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "custom_emoji") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextCustomEmoji.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "mathematical_expression" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "mathematical_expression") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextMathematicalExpression.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "url" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "url") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextUrl.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "email_address" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "email_address") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextEmailAddress.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "phone_number" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "phone_number") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextPhoneNumber.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "bank_card_number" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "bank_card_number") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextBankCardNumber.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "mention" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "mention") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextMention.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "hashtag" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "hashtag") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextHashtag.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "cashtag" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "cashtag") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextCashtag.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "bot_command" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "bot_command") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextBotCommand.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "anchor" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "anchor") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextAnchor.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "anchor_link" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "anchor_link") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextAnchorLink.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "reference" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "reference") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextReference.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }

                "reference_link" -> {
                    if ((jsonElement["type"] as? JsonPrimitive)?.content == "reference_link") {
                        runCatching {
                            decoder.json.decodeFromJsonElement(
                                RichTextReferenceLink.serializer(),
                                jsonElement
                            )
                        }.getOrNull()?.let { return it }
                    }
                }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "date_time") {
                runCatching {
                    decoder.json.decodeFromJsonElement(
                        RichTextDateTime.serializer(),
                        jsonElement
                    )
                }.getOrNull()?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "text_mention") {
                runCatching {
                    decoder.json.decodeFromJsonElement(
                        RichTextTextMention.serializer(),
                        jsonElement
                    )
                }.getOrNull()?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "custom_emoji") {
                runCatching {
                    decoder.json.decodeFromJsonElement(
                        RichTextCustomEmoji.serializer(),
                        jsonElement
                    )
                }.getOrNull()?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "url") {
                runCatching { decoder.json.decodeFromJsonElement(RichTextUrl.serializer(), jsonElement) }.getOrNull()
                    ?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "email_address") {
                runCatching {
                    decoder.json.decodeFromJsonElement(
                        RichTextEmailAddress.serializer(),
                        jsonElement
                    )
                }.getOrNull()?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "phone_number") {
                runCatching {
                    decoder.json.decodeFromJsonElement(
                        RichTextPhoneNumber.serializer(),
                        jsonElement
                    )
                }.getOrNull()?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "bank_card_number") {
                runCatching {
                    decoder.json.decodeFromJsonElement(
                        RichTextBankCardNumber.serializer(),
                        jsonElement
                    )
                }.getOrNull()?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "mention") {
                runCatching {
                    decoder.json.decodeFromJsonElement(
                        RichTextMention.serializer(),
                        jsonElement
                    )
                }.getOrNull()?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "hashtag") {
                runCatching {
                    decoder.json.decodeFromJsonElement(
                        RichTextHashtag.serializer(),
                        jsonElement
                    )
                }.getOrNull()?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "cashtag") {
                runCatching {
                    decoder.json.decodeFromJsonElement(
                        RichTextCashtag.serializer(),
                        jsonElement
                    )
                }.getOrNull()?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "bot_command") {
                runCatching {
                    decoder.json.decodeFromJsonElement(
                        RichTextBotCommand.serializer(),
                        jsonElement
                    )
                }.getOrNull()?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "anchor_link") {
                runCatching {
                    decoder.json.decodeFromJsonElement(
                        RichTextAnchorLink.serializer(),
                        jsonElement
                    )
                }.getOrNull()?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "reference") {
                runCatching {
                    decoder.json.decodeFromJsonElement(
                        RichTextReference.serializer(),
                        jsonElement
                    )
                }.getOrNull()?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "reference_link") {
                runCatching {
                    decoder.json.decodeFromJsonElement(
                        RichTextReferenceLink.serializer(),
                        jsonElement
                    )
                }.getOrNull()?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "bold") {
                runCatching { decoder.json.decodeFromJsonElement(RichTextBold.serializer(), jsonElement) }.getOrNull()
                    ?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "italic") {
                runCatching { decoder.json.decodeFromJsonElement(RichTextItalic.serializer(), jsonElement) }.getOrNull()
                    ?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "underline") {
                runCatching {
                    decoder.json.decodeFromJsonElement(
                        RichTextUnderline.serializer(),
                        jsonElement
                    )
                }.getOrNull()?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "strikethrough") {
                runCatching {
                    decoder.json.decodeFromJsonElement(
                        RichTextStrikethrough.serializer(),
                        jsonElement
                    )
                }.getOrNull()?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "spoiler") {
                runCatching {
                    decoder.json.decodeFromJsonElement(
                        RichTextSpoiler.serializer(),
                        jsonElement
                    )
                }.getOrNull()?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "subscript") {
                runCatching {
                    decoder.json.decodeFromJsonElement(
                        RichTextSubscript.serializer(),
                        jsonElement
                    )
                }.getOrNull()?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "superscript") {
                runCatching {
                    decoder.json.decodeFromJsonElement(
                        RichTextSuperscript.serializer(),
                        jsonElement
                    )
                }.getOrNull()?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "marked") {
                runCatching { decoder.json.decodeFromJsonElement(RichTextMarked.serializer(), jsonElement) }.getOrNull()
                    ?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "code") {
                runCatching { decoder.json.decodeFromJsonElement(RichTextCode.serializer(), jsonElement) }.getOrNull()
                    ?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "mathematical_expression") {
                runCatching {
                    decoder.json.decodeFromJsonElement(
                        RichTextMathematicalExpression.serializer(),
                        jsonElement
                    )
                }.getOrNull()?.let { return it }
            }
            if ((jsonElement["type"] as? JsonPrimitive)?.content == "anchor") {
                runCatching { decoder.json.decodeFromJsonElement(RichTextAnchor.serializer(), jsonElement) }.getOrNull()
                    ?.let { return it }
            }
        }
        throw SerializationException("Could not deserialize RichText")
    }

    override fun serialize(encoder: Encoder, `value`: RichText) {
        require(encoder is JsonEncoder) { "Only JSON is supported" }
        when (value) {
            is RichTextString -> encoder.encodeString(value.value)
            is RichTextArray -> encoder.encodeSerializableValue(ListSerializer(RichTextSerializer), value.value)
            is RichTextBold -> encoder.encodeSerializableValue(RichTextBold.serializer(), value)
            is RichTextItalic -> encoder.encodeSerializableValue(RichTextItalic.serializer(), value)
            is RichTextUnderline -> encoder.encodeSerializableValue(RichTextUnderline.serializer(), value)
            is RichTextStrikethrough -> encoder.encodeSerializableValue(RichTextStrikethrough.serializer(), value)
            is RichTextSpoiler -> encoder.encodeSerializableValue(RichTextSpoiler.serializer(), value)
            is RichTextDateTime -> encoder.encodeSerializableValue(RichTextDateTime.serializer(), value)
            is RichTextTextMention -> encoder.encodeSerializableValue(RichTextTextMention.serializer(), value)
            is RichTextSubscript -> encoder.encodeSerializableValue(RichTextSubscript.serializer(), value)
            is RichTextSuperscript -> encoder.encodeSerializableValue(RichTextSuperscript.serializer(), value)
            is RichTextMarked -> encoder.encodeSerializableValue(RichTextMarked.serializer(), value)
            is RichTextCode -> encoder.encodeSerializableValue(RichTextCode.serializer(), value)
            is RichTextCustomEmoji -> encoder.encodeSerializableValue(RichTextCustomEmoji.serializer(), value)
            is RichTextMathematicalExpression -> encoder.encodeSerializableValue(
                RichTextMathematicalExpression.serializer(),
                value
            )

            is RichTextUrl -> encoder.encodeSerializableValue(RichTextUrl.serializer(), value)
            is RichTextEmailAddress -> encoder.encodeSerializableValue(RichTextEmailAddress.serializer(), value)
            is RichTextPhoneNumber -> encoder.encodeSerializableValue(RichTextPhoneNumber.serializer(), value)
            is RichTextBankCardNumber -> encoder.encodeSerializableValue(RichTextBankCardNumber.serializer(), value)
            is RichTextMention -> encoder.encodeSerializableValue(RichTextMention.serializer(), value)
            is RichTextHashtag -> encoder.encodeSerializableValue(RichTextHashtag.serializer(), value)
            is RichTextCashtag -> encoder.encodeSerializableValue(RichTextCashtag.serializer(), value)
            is RichTextBotCommand -> encoder.encodeSerializableValue(RichTextBotCommand.serializer(), value)
            is RichTextAnchor -> encoder.encodeSerializableValue(RichTextAnchor.serializer(), value)
            is RichTextAnchorLink -> encoder.encodeSerializableValue(RichTextAnchorLink.serializer(), value)
            is RichTextReference -> encoder.encodeSerializableValue(RichTextReference.serializer(), value)
            is RichTextReferenceLink -> encoder.encodeSerializableValue(RichTextReferenceLink.serializer(), value)
            else -> throw SerializationException("Unsupported RichText implementation")
        }
    }
}
