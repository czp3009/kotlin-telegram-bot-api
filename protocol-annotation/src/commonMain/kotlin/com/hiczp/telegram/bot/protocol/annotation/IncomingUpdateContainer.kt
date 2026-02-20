package com.hiczp.telegram.bot.protocol.annotation

/**
 * Annotation to mark a data class as an incoming update container.
 * Classes annotated with this will be processed by the KSP processor
 * to generate event classes.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class IncomingUpdateContainer
