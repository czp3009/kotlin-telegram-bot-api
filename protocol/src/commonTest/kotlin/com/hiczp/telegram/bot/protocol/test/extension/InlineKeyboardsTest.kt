package com.hiczp.telegram.bot.protocol.test.extension

import com.hiczp.telegram.bot.protocol.constant.InlineKeyboardButtonStyle
import com.hiczp.telegram.bot.protocol.extension.inlineKeyboard
import com.hiczp.telegram.bot.protocol.extension.inlineKeyboardOf
import com.hiczp.telegram.bot.protocol.model.InlineKeyboardButton
import com.hiczp.telegram.bot.protocol.model.InlineKeyboardMarkup
import com.hiczp.telegram.bot.protocol.model.LoginUrl
import com.hiczp.telegram.bot.protocol.model.SwitchInlineQueryChosenChat
import kotlin.test.*

class InlineKeyboardsTest {

    // ========== inlineKeyboard builder tests ==========

    @Test
    fun emptyKeyboard() {
        val keyboard = inlineKeyboard {}
        assertIs<InlineKeyboardMarkup>(keyboard)
        assertTrue(keyboard.inlineKeyboard.isEmpty())
    }

    @Test
    fun singleCallbackButton() {
        val keyboard = inlineKeyboard {
            callbackButton("Click me", "click_data")
        }

        assertEquals(1, keyboard.inlineKeyboard.size)
        assertEquals(1, keyboard.inlineKeyboard[0].size)

        val button = keyboard.inlineKeyboard[0][0]
        assertEquals("Click me", button.text)
        assertEquals("click_data", button.callbackData)
        assertNull(button.style)
    }

    @Test
    fun singleRowWithMultipleButtons() {
        val keyboard = inlineKeyboard {
            row {
                callbackButton("Yes", "yes")
                callbackButton("No", "no")
            }
        }

        assertEquals(1, keyboard.inlineKeyboard.size)
        assertEquals(2, keyboard.inlineKeyboard[0].size)

        assertEquals("Yes", keyboard.inlineKeyboard[0][0].text)
        assertEquals("yes", keyboard.inlineKeyboard[0][0].callbackData)
        assertEquals("No", keyboard.inlineKeyboard[0][1].text)
        assertEquals("no", keyboard.inlineKeyboard[0][1].callbackData)
    }

    @Test
    fun multipleRows() {
        val keyboard = inlineKeyboard {
            row {
                callbackButton("Row1-A", "r1a")
                callbackButton("Row1-B", "r1b")
            }
            row {
                callbackButton("Row2-A", "r2a")
            }
        }

        assertEquals(2, keyboard.inlineKeyboard.size)
        assertEquals(2, keyboard.inlineKeyboard[0].size)
        assertEquals(1, keyboard.inlineKeyboard[1].size)
    }

    @Test
    fun callbackRowWithPairs() {
        val keyboard = inlineKeyboard {
            callbackRow("Yes" to "yes", "No" to "no", "Maybe" to "maybe")
        }

        assertEquals(1, keyboard.inlineKeyboard.size)
        assertEquals(3, keyboard.inlineKeyboard[0].size)

        assertEquals("Yes", keyboard.inlineKeyboard[0][0].text)
        assertEquals("yes", keyboard.inlineKeyboard[0][0].callbackData)
        assertEquals("No", keyboard.inlineKeyboard[0][1].text)
        assertEquals("no", keyboard.inlineKeyboard[0][1].callbackData)
        assertEquals("Maybe", keyboard.inlineKeyboard[0][2].text)
        assertEquals("maybe", keyboard.inlineKeyboard[0][2].callbackData)
    }

    @Test
    fun callbackRowWithStyle() {
        val keyboard = inlineKeyboard {
            callbackRow(InlineKeyboardButtonStyle.DANGER, "Delete" to "delete", "Remove" to "remove")
        }

        assertEquals(1, keyboard.inlineKeyboard.size)
        assertEquals(2, keyboard.inlineKeyboard[0].size)

        assertEquals("Delete", keyboard.inlineKeyboard[0][0].text)
        assertEquals("danger", keyboard.inlineKeyboard[0][0].style)
        assertEquals("Remove", keyboard.inlineKeyboard[0][1].text)
        assertEquals("danger", keyboard.inlineKeyboard[0][1].style)
    }

    // ========== Button type tests ==========

    @Test
    fun urlButton() {
        val keyboard = inlineKeyboard {
            urlButton("Open Link", "https://example.com")
        }

        val button = keyboard.inlineKeyboard[0][0]
        assertEquals("Open Link", button.text)
        assertEquals("https://example.com", button.url)
        assertNull(button.callbackData)
    }

    @Test
    fun webAppButton() {
        val keyboard = inlineKeyboard {
            webAppButton("Open App", "https://app.example.com")
        }

        val button = keyboard.inlineKeyboard[0][0]
        assertEquals("Open App", button.text)
        assertNotNull(button.webApp)
        assertEquals("https://app.example.com", button.webApp.url)
    }

    @Test
    fun loginButton() {
        val loginUrl = LoginUrl(url = "https://login.example.com")
        val keyboard = inlineKeyboard {
            loginButton("Login", loginUrl)
        }

        val button = keyboard.inlineKeyboard[0][0]
        assertEquals("Login", button.text)
        assertEquals(loginUrl, button.loginUrl)
    }

    @Test
    fun switchInlineButton() {
        val keyboard = inlineKeyboard {
            switchInlineButton("Search", "query ")
        }

        val button = keyboard.inlineKeyboard[0][0]
        assertEquals("Search", button.text)
        assertEquals("query ", button.switchInlineQuery)
    }

    @Test
    fun switchInlineButtonEmptyQuery() {
        val keyboard = inlineKeyboard {
            switchInlineButton("Search")
        }

        val button = keyboard.inlineKeyboard[0][0]
        assertEquals("Search", button.text)
        assertEquals("", button.switchInlineQuery)
    }

    @Test
    fun switchInlineCurrentChatButton() {
        val keyboard = inlineKeyboard {
            switchInlineCurrentChatButton("Search Here", "search ")
        }

        val button = keyboard.inlineKeyboard[0][0]
        assertEquals("Search Here", button.text)
        assertEquals("search ", button.switchInlineQueryCurrentChat)
    }

    @Test
    fun switchInlineChosenChatButton() {
        val chosenChat = SwitchInlineQueryChosenChat(query = "test")
        val keyboard = inlineKeyboard {
            switchInlineChosenChatButton("Choose Chat", chosenChat)
        }

        val button = keyboard.inlineKeyboard[0][0]
        assertEquals("Choose Chat", button.text)
        assertEquals(chosenChat, button.switchInlineQueryChosenChat)
    }

    @Test
    fun copyTextButton() {
        val keyboard = inlineKeyboard {
            copyTextButton("Copy Code", "ABC123")
        }

        val button = keyboard.inlineKeyboard[0][0]
        assertEquals("Copy Code", button.text)
        assertNotNull(button.copyText)
        assertEquals("ABC123", button.copyText.text)
    }

    @Test
    fun payButton() {
        val keyboard = inlineKeyboard {
            payButton()
        }

        val button = keyboard.inlineKeyboard[0][0]
        assertEquals("Pay", button.text)
        assertEquals(true, button.pay)
    }

    @Test
    fun payButtonWithCustomText() {
        val keyboard = inlineKeyboard {
            payButton("Buy Now")
        }

        val button = keyboard.inlineKeyboard[0][0]
        assertEquals("Buy Now", button.text)
        assertEquals(true, button.pay)
    }

    @Test
    fun genericButton() {
        val customButton = InlineKeyboardButton(
            text = "Custom",
            callbackData = "custom_data",
            style = InlineKeyboardButtonStyle.PRIMARY
        )
        val keyboard = inlineKeyboard {
            row {
                button(customButton)
            }
        }

        val button = keyboard.inlineKeyboard[0][0]
        assertEquals("Custom", button.text)
        assertEquals("custom_data", button.callbackData)
        assertEquals(InlineKeyboardButtonStyle.PRIMARY, button.style)
    }

    // ========== Style tests ==========

    @Test
    fun buttonWithDangerStyle() {
        val keyboard = inlineKeyboard {
            callbackButton("Delete", "delete", InlineKeyboardButtonStyle.DANGER)
        }

        val button = keyboard.inlineKeyboard[0][0]
        assertEquals("danger", button.style)
    }

    @Test
    fun buttonWithSuccessStyle() {
        val keyboard = inlineKeyboard {
            callbackButton("Confirm", "confirm", InlineKeyboardButtonStyle.SUCCESS)
        }

        val button = keyboard.inlineKeyboard[0][0]
        assertEquals("success", button.style)
    }

    @Test
    fun buttonWithPrimaryStyle() {
        val keyboard = inlineKeyboard {
            callbackButton("Primary", "primary", InlineKeyboardButtonStyle.PRIMARY)
        }

        val button = keyboard.inlineKeyboard[0][0]
        assertEquals("primary", button.style)
    }

    @Test
    fun rowWithMixedStyles() {
        val keyboard = inlineKeyboard {
            row {
                callbackButton("Safe", "safe", InlineKeyboardButtonStyle.SUCCESS)
                callbackButton("Danger", "danger", InlineKeyboardButtonStyle.DANGER)
            }
        }

        assertEquals("success", keyboard.inlineKeyboard[0][0].style)
        assertEquals("danger", keyboard.inlineKeyboard[0][1].style)
    }

    // ========== inlineKeyboardOf tests ==========

    @Test
    fun inlineKeyboardOfSingleRow() {
        val keyboard = inlineKeyboardOf(listOf("A" to "a", "B" to "b"))

        assertEquals(1, keyboard.inlineKeyboard.size)
        assertEquals(2, keyboard.inlineKeyboard[0].size)
        assertEquals("A", keyboard.inlineKeyboard[0][0].text)
        assertEquals("a", keyboard.inlineKeyboard[0][0].callbackData)
    }

    @Test
    fun inlineKeyboardOfMultipleRows() {
        val keyboard = inlineKeyboardOf(
            listOf("A" to "a", "B" to "b"),
            listOf("C" to "c")
        )

        assertEquals(2, keyboard.inlineKeyboard.size)
        assertEquals(2, keyboard.inlineKeyboard[0].size)
        assertEquals(1, keyboard.inlineKeyboard[1].size)
    }

    @Test
    fun inlineKeyboardOfEmpty() {
        val keyboard = inlineKeyboardOf()

        assertTrue(keyboard.inlineKeyboard.isEmpty())
    }

    // ========== Complex keyboard tests ==========

    @Test
    fun complexKeyboard() {
        val keyboard = inlineKeyboard {
            // First row: callback buttons
            row {
                callbackButton("Option 1", "opt1")
                callbackButton("Option 2", "opt2")
            }
            // Second row: URL button
            urlButton("Help", "https://help.example.com")
            // Third row: callback row with style
            callbackRow(InlineKeyboardButtonStyle.DANGER, "Cancel" to "cancel")
        }

        assertEquals(3, keyboard.inlineKeyboard.size)

        // First row
        assertEquals(2, keyboard.inlineKeyboard[0].size)
        assertNull(keyboard.inlineKeyboard[0][0].style)

        // Second row
        assertEquals(1, keyboard.inlineKeyboard[1].size)
        assertNotNull(keyboard.inlineKeyboard[1][0].url)

        // Third row
        assertEquals(1, keyboard.inlineKeyboard[2].size)
        assertEquals("danger", keyboard.inlineKeyboard[2][0].style)
    }

    @Test
    fun mixedButtonTypesInRow() {
        val keyboard = inlineKeyboard {
            row {
                callbackButton("Callback", "data")
                urlButton("URL", "https://example.com")
                webAppButton("App", "https://app.example.com")
            }
        }

        assertEquals(1, keyboard.inlineKeyboard.size)
        assertEquals(3, keyboard.inlineKeyboard[0].size)

        assertNotNull(keyboard.inlineKeyboard[0][0].callbackData)
        assertNotNull(keyboard.inlineKeyboard[0][1].url)
        assertNotNull(keyboard.inlineKeyboard[0][2].webApp)
    }

    // ========== Constant tests ==========

    @Test
    fun constantValues() {
        assertEquals("danger", InlineKeyboardButtonStyle.DANGER)
        assertEquals("success", InlineKeyboardButtonStyle.SUCCESS)
        assertEquals("primary", InlineKeyboardButtonStyle.PRIMARY)
    }
}
