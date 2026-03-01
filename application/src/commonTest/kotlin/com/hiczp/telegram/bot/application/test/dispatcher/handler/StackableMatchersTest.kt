package com.hiczp.telegram.bot.application.test.dispatcher.handler

import com.hiczp.telegram.bot.application.context.ProvidedUserTelegramBotEventContext
import com.hiczp.telegram.bot.application.context.TelegramBotEventContext
import com.hiczp.telegram.bot.application.dispatcher.handler.handling
import com.hiczp.telegram.bot.application.dispatcher.handler.matcher.*
import com.hiczp.telegram.bot.client.TelegramBotClient
import com.hiczp.telegram.bot.protocol.constant.ChatType
import com.hiczp.telegram.bot.protocol.event.*
import com.hiczp.telegram.bot.protocol.model.*
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.*
import kotlin.time.Clock

class StackableMatchersTest {
    private val testBotUser = User(
        id = 123456789L,
        isBot = true,
        firstName = "TestBot",
        username = "test_bot",
    )

    private val mockClient = TelegramBotClient(botToken = "xxx")
    private val testScope = TestScope()

    private val invokedHandlers = mutableListOf<String>()

    @BeforeTest
    fun resetTracking() {
        invokedHandlers.clear()
    }

    private fun createContext(
        event: TelegramBotEvent,
    ): TelegramBotEventContext<TelegramBotEvent> {
        return ProvidedUserTelegramBotEventContext(
            client = mockClient,
            event = event,
            applicationScope = testScope,
            botUser = testBotUser,
        )
    }

    private fun createMessageEvent(
        text: String? = null,
        chatId: Long = 100L,
        chatType: String = ChatType.PRIVATE,
        userId: Long = 1L,
        photo: List<PhotoSize>? = null,
        video: Video? = null,
        document: Document? = null,
        audio: Audio? = null,
        sticker: Sticker? = null,
        voice: Voice? = null,
        replyToMessage: Message? = null,
        forwardOrigin: MessageOrigin? = null,
    ): MessageEvent {
        return MessageEvent(
            updateId = 1L,
            message = Message(
                messageId = 1L,
                date = Clock.System.now().epochSeconds,
                chat = Chat(id = chatId, type = chatType),
                from = User(id = userId, isBot = false, firstName = "User"),
                text = text,
                photo = photo,
                video = video,
                document = document,
                audio = audio,
                sticker = sticker,
                voice = voice,
                replyToMessage = replyToMessage,
                forwardOrigin = forwardOrigin,
            )
        )
    }

    private fun createCallbackQueryEvent(data: String): CallbackQueryEvent {
        return CallbackQueryEvent(
            updateId = 1L,
            callbackQuery = CallbackQuery(
                id = "1",
                from = User(id = 1L, isBot = false, firstName = "User"),
                chatInstance = "test_instance",
                data = data
            )
        )
    }

    private fun createInlineQueryEvent(query: String): InlineQueryEvent {
        return InlineQueryEvent(
            updateId = 1L,
            inlineQuery = InlineQuery(
                id = "1",
                from = User(id = 1L, isBot = false, firstName = "User"),
                query = query,
                offset = "0"
            )
        )
    }

    // ==================== Text Matchers ====================

    @Test
    fun `text matcher with nested matchers`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                onText("hello") {
                    handle { invokedHandlers.add("hello") }
                }
                onText("world") {
                    handle { invokedHandlers.add("world") }
                }
            }
        }

        var context = createContext(createMessageEvent(text = "hello"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("hello"), invokedHandlers)

        resetTracking()
        context = createContext(createMessageEvent(text = "world"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("world"), invokedHandlers)
    }

    @Test
    fun `textRegex matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                onTextRegex(Regex("^hello.*")) {
                    handle { invokedHandlers.add("hello_prefix") }
                }
            }
        }

        var context = createContext(createMessageEvent(text = "hello world"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("hello_prefix"), invokedHandlers)

        resetTracking()
        context = createContext(createMessageEvent(text = "hi world"))
        assertFalse(routeNode.execute(context))
    }

    @Test
    fun `textContains matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                onTextContains("hello") {
                    onTextContains("world") {
                        handle { invokedHandlers.add("both") }
                    }
                    handle { invokedHandlers.add("hello_only") }
                }
            }
        }

        var context = createContext(createMessageEvent(text = "hello world"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("both"), invokedHandlers)

        resetTracking()
        context = createContext(createMessageEvent(text = "hello there"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("hello_only"), invokedHandlers)
    }

    @Test
    fun `textStartsWith matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                onTextStartsWith("/admin") {
                    onTextStartsWith("/admin user") {
                        handle { invokedHandlers.add("admin_user") }
                    }
                    handle { invokedHandlers.add("admin") }
                }
            }
        }

        var context = createContext(createMessageEvent(text = "/admin user list"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("admin_user"), invokedHandlers)

        resetTracking()
        context = createContext(createMessageEvent(text = "/admin status"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("admin"), invokedHandlers)
    }

    @Test
    fun `textEndsWith matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                onTextEndsWith("please") {
                    onTextEndsWith("help please") {
                        handle { invokedHandlers.add("help") }
                    }
                    handle { invokedHandlers.add("polite") }
                }
            }
        }

        var context = createContext(createMessageEvent(text = "I need help please"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("help"), invokedHandlers)

        resetTracking()
        context = createContext(createMessageEvent(text = "come here please"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("polite"), invokedHandlers)
    }

    // ==================== Media Matchers ====================

    @Test
    fun `photo matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                onPhoto {
                    onTextContains("caption") {
                        handle { invokedHandlers.add("photo_with_caption") }
                    }
                    handle { invokedHandlers.add("photo_any") }
                }
            }
        }

        var context = createContext(
            createMessageEvent(
                photo = listOf(PhotoSize(fileId = "1", fileUniqueId = "u1", width = 100, height = 100)),
                text = "caption here"
            )
        )
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("photo_with_caption"), invokedHandlers)

        resetTracking()
        context = createContext(
            createMessageEvent(
                photo = listOf(PhotoSize(fileId = "1", fileUniqueId = "u1", width = 100, height = 100))
            )
        )
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("photo_any"), invokedHandlers)
    }

    @Test
    fun `video matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                onVideo {
                    handle { invokedHandlers.add("video") }
                }
            }
        }

        val context = createContext(
            createMessageEvent(
                video = Video(fileId = "1", fileUniqueId = "u1", width = 100, height = 100, duration = 10)
            )
        )
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("video"), invokedHandlers)
    }

    @Test
    fun `document matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                onDocument {
                    handle { invokedHandlers.add("document") }
                }
            }
        }

        val context = createContext(
            createMessageEvent(
                document = Document(fileId = "1", fileUniqueId = "u1")
            )
        )
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("document"), invokedHandlers)
    }

    @Test
    fun `audio matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                onAudio {
                    handle { invokedHandlers.add("audio") }
                }
            }
        }

        val context = createContext(
            createMessageEvent(
                audio = Audio(fileId = "1", fileUniqueId = "u1", duration = 100)
            )
        )
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("audio"), invokedHandlers)
    }

    @Test
    fun `sticker matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                onSticker {
                    handle { invokedHandlers.add("sticker") }
                }
            }
        }

        val context = createContext(
            createMessageEvent(
                sticker = Sticker(
                    fileId = "1",
                    fileUniqueId = "u1",
                    type = "regular",
                    width = 100,
                    height = 100,
                    isAnimated = false,
                    isVideo = false
                )
            )
        )
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("sticker"), invokedHandlers)
    }

    @Test
    fun `voice matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                onVoice {
                    handle { invokedHandlers.add("voice") }
                }
            }
        }

        val context = createContext(
            createMessageEvent(
                voice = Voice(fileId = "1", fileUniqueId = "u1", duration = 10)
            )
        )
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("voice"), invokedHandlers)
    }

    // ==================== Reply Matchers ====================

    @Test
    fun `reply matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                onReply {
                    onReplyTo(42L) {
                        handle { invokedHandlers.add("reply_to_42") }
                    }
                    handle { invokedHandlers.add("reply_any") }
                }
            }
        }

        val replyMessage =
            Message(messageId = 42L, date = Clock.System.now().epochSeconds, chat = Chat(id = 100L, type = "private"))

        var context = createContext(createMessageEvent(text = "response", replyToMessage = replyMessage))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("reply_to_42"), invokedHandlers)

        resetTracking()
        val otherReplyMessage =
            Message(messageId = 99L, date = Clock.System.now().epochSeconds, chat = Chat(id = 100L, type = "private"))
        context = createContext(createMessageEvent(text = "response", replyToMessage = otherReplyMessage))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("reply_any"), invokedHandlers)
    }

    // ==================== User/Chat Matchers ====================

    @Test
    fun `fromUser matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                onFromUser(1L) {
                    onInChat(100L) {
                        handle { invokedHandlers.add("user_in_chat") }
                    }
                    handle { invokedHandlers.add("user_any") }
                }
            }
        }

        var context = createContext(createMessageEvent(text = "hello", userId = 1L, chatId = 100L))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("user_in_chat"), invokedHandlers)

        resetTracking()
        context = createContext(createMessageEvent(text = "hello", userId = 1L, chatId = 200L))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("user_any"), invokedHandlers)
    }

    @Test
    fun `fromUsers matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                onFromUsers(setOf(1L, 2L, 3L)) {
                    handle { invokedHandlers.add("allowed_user") }
                }
            }
        }

        var context = createContext(createMessageEvent(text = "hello", userId = 2L))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("allowed_user"), invokedHandlers)

        resetTracking()
        context = createContext(createMessageEvent(text = "hello", userId = 99L))
        assertFalse(routeNode.execute(context))
        assertTrue(invokedHandlers.isEmpty())
    }

    @Test
    fun `inChat matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                onInChat(100L) {
                    handle { invokedHandlers.add("in_chat_100") }
                }
            }
        }

        var context = createContext(createMessageEvent(text = "hello", chatId = 100L))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("in_chat_100"), invokedHandlers)

        resetTracking()
        context = createContext(createMessageEvent(text = "hello", chatId = 200L))
        assertFalse(routeNode.execute(context))
    }

    @Test
    fun `inChats matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                onInChats(setOf(100L, 200L, 300L)) {
                    handle { invokedHandlers.add("allowed_chat") }
                }
            }
        }

        var context = createContext(createMessageEvent(text = "hello", chatId = 200L))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("allowed_chat"), invokedHandlers)

        resetTracking()
        context = createContext(createMessageEvent(text = "hello", chatId = 999L))
        assertFalse(routeNode.execute(context))
    }

    // ==================== Forwarded Matchers ====================

    @Test
    fun `forwarded matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                onForwarded {
                    handle { invokedHandlers.add("forwarded") }
                }
            }
        }

        val context = createContext(
            createMessageEvent(
                text = "hello",
                forwardOrigin = MessageOriginUser(
                    date = Clock.System.now().epochSeconds,
                    senderUser = User(id = 999L, isBot = false, firstName = "Original")
                )
            )
        )
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("forwarded"), invokedHandlers)
    }

    // ==================== Callback Query Matchers ====================

    @Test
    fun `callbackData matcher should be stackable`() = runTest {
        val routeNode = handling {
            onCallbackQueryEvent {
                onCallbackData("action") {
                    handle { invokedHandlers.add("action") }
                }
                onCallbackData("cancel") {
                    handle { invokedHandlers.add("cancel") }
                }
            }
        }

        var context = createContext(createCallbackQueryEvent(data = "action"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("action"), invokedHandlers)

        resetTracking()
        context = createContext(createCallbackQueryEvent(data = "cancel"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("cancel"), invokedHandlers)
    }

    @Test
    fun `callbackDataRegex matcher should be stackable`() = runTest {
        val routeNode = handling {
            onCallbackQueryEvent {
                onCallbackDataRegex(Regex("^action_.*")) {
                    handle { invokedHandlers.add("action_prefix") }
                }
            }
        }

        var context = createContext(createCallbackQueryEvent(data = "action_123"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("action_prefix"), invokedHandlers)

        resetTracking()
        context = createContext(createCallbackQueryEvent(data = "other_123"))
        assertFalse(routeNode.execute(context))
    }

    // ==================== Inline Query Matchers ====================

    @Test
    fun `inlineQuery matcher should be stackable`() = runTest {
        val routeNode = handling {
            onInlineQueryEvent {
                onInlineQuery("search") {
                    handle { invokedHandlers.add("search") }
                }
            }
        }

        val context = createContext(createInlineQueryEvent(query = "search"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("search"), invokedHandlers)
    }

    @Test
    fun `inlineQueryRegex matcher should be stackable`() = runTest {
        val routeNode = handling {
            onInlineQueryEvent {
                onInlineQueryRegex(Regex("^search:.*")) {
                    handle { invokedHandlers.add("search_prefix") }
                }
            }
        }

        var context = createContext(createInlineQueryEvent(query = "search:something"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("search_prefix"), invokedHandlers)

        resetTracking()
        context = createContext(createInlineQueryEvent(query = "find:something"))
        assertFalse(routeNode.execute(context))
    }

    @Test
    fun `inlineQueryContains matcher should be stackable`() = runTest {
        val routeNode = handling {
            onInlineQueryEvent {
                onInlineQueryContains("hello") {
                    handle { invokedHandlers.add("contains_hello") }
                }
            }
        }

        var context = createContext(createInlineQueryEvent(query = "say hello world"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("contains_hello"), invokedHandlers)

        resetTracking()
        context = createContext(createInlineQueryEvent(query = "hi world"))
        assertFalse(routeNode.execute(context))
    }

    @Test
    fun `inlineQueryStartsWith matcher should be stackable`() = runTest {
        val routeNode = handling {
            onInlineQueryEvent {
                onInlineQueryStartsWith("@") {
                    handle { invokedHandlers.add("mention") }
                }
            }
        }

        var context = createContext(createInlineQueryEvent(query = "@user"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("mention"), invokedHandlers)

        resetTracking()
        context = createContext(createInlineQueryEvent(query = "user@"))
        assertFalse(routeNode.execute(context))
    }

    @Test
    fun `inlineQueryFromUser matcher should be stackable`() = runTest {
        val routeNode = handling {
            onInlineQueryEvent {
                onInlineQueryFromUser(1L) {
                    handle { invokedHandlers.add("from_user_1") }
                }
            }
        }

        val context = createContext(createInlineQueryEvent(query = "search"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("from_user_1"), invokedHandlers)
    }

    // ==================== Chat Type Matchers ====================

    @Test
    fun `privateChat matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                onPrivateChat {
                    handle { invokedHandlers.add("private") }
                }
            }
        }

        val context = createContext(createMessageEvent(text = "hello", chatType = ChatType.PRIVATE))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("private"), invokedHandlers)
    }

    @Test
    fun `groupChat matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                onGroupChat {
                    handle { invokedHandlers.add("group") }
                }
            }
        }

        val context = createContext(createMessageEvent(text = "hello", chatType = ChatType.GROUP))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("group"), invokedHandlers)
    }

    @Test
    fun `supergroupChat matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                onSupergroupChat {
                    handle { invokedHandlers.add("supergroup") }
                }
            }
        }

        val context = createContext(createMessageEvent(text = "hello", chatType = ChatType.SUPERGROUP))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("supergroup"), invokedHandlers)
    }

    @Test
    fun `channel matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                onChannel {
                    handle { invokedHandlers.add("channel") }
                }
            }
        }

        val context = createContext(createMessageEvent(text = "hello", chatType = ChatType.CHANNEL))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("channel"), invokedHandlers)
    }

    // ==================== Poll Matchers ====================

    @Test
    fun `pollUpdate matcher should be stackable`() = runTest {
        val routeNode = handling {
            onPollEvent {
                handle { invokedHandlers.add("poll_update") }
            }
        }

        val context = createContext(
            PollEvent(
                updateId = 1L,
                poll = Poll(
                    id = "poll1",
                    question = "Test?",
                    options = listOf(),
                    totalVoterCount = 0,
                    isClosed = false,
                    isAnonymous = false,
                    type = "regular",
                    allowsMultipleAnswers = false
                )
            )
        )
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("poll_update"), invokedHandlers)
    }

    @Test
    fun `pollAnswer matcher should be stackable`() = runTest {
        val routeNode = handling {
            onPollAnswerEvent {
                handle { invokedHandlers.add("poll_answer") }
            }
        }

        val context = createContext(
            PollAnswerEvent(
                updateId = 1L,
                pollAnswer = PollAnswer(
                    pollId = "poll1",
                    optionIds = listOf(0),
                    user = User(id = 1L, isBot = false, firstName = "User")
                )
            )
        )
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("poll_answer"), invokedHandlers)
    }

    // ==================== Composite Matchers ====================

    @Test
    fun `allOf matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                allOf(
                    { it.event.message.text != null },
                    { it.event.message.chat.id == 100L }
                ) {
                    allOf(
                        { it.event.message.from?.id == 1L }
                    ) {
                        handle { invokedHandlers.add("all_conditions") }
                    }
                }
            }
        }

        val context = createContext(createMessageEvent(text = "hello", chatId = 100L, userId = 1L))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("all_conditions"), invokedHandlers)
    }

    @Test
    fun `anyOf matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                anyOf(
                    { it.event.message.text?.contains("hello") == true },
                    { it.event.message.text?.contains("hi") == true }
                ) {
                    anyOf(
                        { it.event.message.chat.id == 100L }
                    ) {
                        handle { invokedHandlers.add("any_condition") }
                    }
                }
            }
        }

        val context = createContext(createMessageEvent(text = "hello world", chatId = 100L))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("any_condition"), invokedHandlers)
    }

    @Test
    fun `not matcher should be stackable`() = runTest {
        val routeNode = handling {
            onMessageEvent {
                not({ it.event.message.text?.startsWith("/") == true }) {
                    not({ it.event.message.text?.length?.let { l -> l > 100 } == true }) {
                        handle { invokedHandlers.add("not_command_not_long") }
                    }
                }
            }
        }

        val context = createContext(createMessageEvent(text = "hello"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("not_command_not_long"), invokedHandlers)
    }

    // ==================== Root Level Stackable Matchers ====================

    @Test
    fun `root level text matcher should work`() = runTest {
        val routeNode = handling {
            onText("hello") {
                handle { invokedHandlers.add("root_hello") }
            }
        }

        val context = createContext(createMessageEvent(text = "hello"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("root_hello"), invokedHandlers)
    }

    @Test
    fun `root level callbackData matcher should work`() = runTest {
        val routeNode = handling {
            onCallbackData("confirm") {
                handle { invokedHandlers.add("root_confirm") }
            }
        }

        val context = createContext(createCallbackQueryEvent(data = "confirm"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("root_confirm"), invokedHandlers)
    }

    @Test
    fun `root level inlineQuery matcher should work`() = runTest {
        val routeNode = handling {
            onInlineQuery("search") {
                handle { invokedHandlers.add("root_search") }
            }
        }

        val context = createContext(createInlineQueryEvent(query = "search"))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("root_search"), invokedHandlers)
    }

    @Test
    fun `root level privateChat matcher should work`() = runTest {
        val routeNode = handling {
            onPrivateChat {
                handle { invokedHandlers.add("root_private") }
            }
        }

        val context = createContext(createMessageEvent(text = "hello", chatType = ChatType.PRIVATE))
        assertTrue(routeNode.execute(context))
        assertEquals(listOf("root_private"), invokedHandlers)
    }
}
