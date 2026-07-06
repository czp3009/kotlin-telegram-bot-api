package com.hiczp.telegram.bot.application.dispatcher.handler

import com.hiczp.telegram.bot.protocol.event.TelegramBotEvent

/**
 * Builds a handler route tree.
 *
 * The [build] block runs once when the dispatcher is created. Do not put per-update
 * side effects directly in this block; put them in [HandlerRoute.filter] predicates
 * or [HandlerRoute.handle] blocks.
 */
fun handling(build: HandlerRoute<TelegramBotEvent>.() -> Unit): RouteNode {
    val rootNode = RouteNode { it }
    HandlerRoute<TelegramBotEvent>(rootNode).build()
    return rootNode
}

/**
 * Includes a pre-built route tree as a child of this route.
 */
fun HandlerRoute<*>.include(other: RouteNode) {
    node.children.add(other)
}
