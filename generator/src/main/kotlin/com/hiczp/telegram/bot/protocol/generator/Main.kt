package com.hiczp.telegram.bot.protocol.generator

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode

private val mergedObject = mutableMapOf<String, Set<String>>()

fun main() {
    val doc = Jsoup.connect("https://core.telegram.org/bots/api").get()
    val pageContent = doc.select("#dev_page_content").first()
    checkNotNull(pageContent)
    val bodyElements = pageContent.childNodes().filterIsInstance<Element>()
    val gettingUpdatesNode = bodyElements.first {
        it.tagName() == "h3" && it.firstElementChild()?.attr("name") == "getting-updates"
    }
    val docElements = bodyElements.subList(gettingUpdatesNode.elementSiblingIndex(), bodyElements.count())
    val docSections = sequence {
        var previousH3 = 0
        docElements.forEachIndexed { index, it ->
            if (previousH3 != index && it.tagName() in listOf("h3", "h4")) {
                yield(docElements.subList(previousH3, index))
                previousH3 = index
            }
        }
    }.filter {
        it.first().tagName() == "h4"
    }
    docSections.forEach {
        val text = it.first().childNodes().filterIsInstance<TextNode>().first().text()
        when {
            text.contains(" ") -> println("Skip '$text'")
            text.first().isUpperCase() -> parseObject(text, it)
            text.startsWith("get") -> parseGetRequest(text, it)
            else -> parsePostRequest(text, it)
        }
    }
    println("done")
}

private fun parseObject(name: String, section: List<Element>) {
    println("Parsing Object '$name'")
    val (tableSections, nonTableSections) = section.partition { it.tagName() == "table" }
    if (tableSections.isEmpty()) {
        val (h4, p) = section
        assert(h4.tagName() == "h4")
        assert(p.tagName() == "p")
        if (section.count() >= 3 && section[2].tagName() == "ul") {
            val list = section[2].childNodes().filterIsInstance<Element>().map { it.text() }
            println("Merge $list to $name")
            mergedObject[name] = list.toSet()
        } else {
            assert(p.text().endsWith("no information.", ignoreCase = true))
            println("Skip '$name'")
        }
        return
    }
    val tableSection = tableSections.first()
    val documentText = nonTableSections.joinToString(separator = "\n") { it.wholeText() }
    parseTable(tableSection, documentText)
}

private fun parseGetRequest(name: String, section: List<Element>) {
    //println("Parsing GET request '$name'")
}

private fun parsePostRequest(name: String, section: List<Element>) {
    //println("Parsing POST request '$name'")
}

private fun parseTable(element: Element, description: String) {
    assert(element.tagName() == "table")
    val tableElements = element.childNodes().filterIsInstance<Element>()
    val theadElement = tableElements.first()
    assert(theadElement.tagName() == "thead")
    val thElements = theadElement.childNodes().filterIsInstance<Element>().first()
        .childNodes().filterIsInstance<Element>()
    thElements.forEach { assert(it.tagName() == "th") }
    when (thElements.first().text()) {
        "Field" -> {
            assert(thElements.count() == 3)
            println("Parsing Field table")

        }

        "Parameter" -> {
            assert(thElements.count() == 4)
            println("Parsing Parameter table")

        }

        else -> throw IllegalStateException("Unrecognized table:\n$tableElements")
    }
}
