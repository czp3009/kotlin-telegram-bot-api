package com.hiczp.telegram.bot.protocol.generator

class ParsedObject(
    val fields: MutableList<Field> = mutableListOf(),
    val description: String,
) {
    data class Field(
        val field: String,
        val type: String,
        val optional: Boolean,
        val description: String,
    )
}
