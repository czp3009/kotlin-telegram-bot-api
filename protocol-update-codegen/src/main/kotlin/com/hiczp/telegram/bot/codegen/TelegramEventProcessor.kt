package com.hiczp.telegram.bot.codegen

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.validate
import com.hiczp.telegram.bot.protocol.annotation.IncomingUpdateContainer
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * KSP Processor that generates TelegramBotEvent sealed interface and event classes
 * from data classes annotated with @IncomingUpdateContainer.
 *
 * ## Generated Files
 *
 * ### TelegramBotEvents.kt
 * Contains:
 * - `TelegramBotEvent` sealed interface with a common `updateId: Long` property
 * - Event data classes for each optional field in the source class (e.g., `MessageEvent`, `EditedMessageEvent`)
 *
 * ### Updates.kt
 * Contains:
 * - Extension function that converts the source class to the appropriate `TelegramBotEvent` subtype
 */
class TelegramEventProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    companion object {
        private const val EVENT_PACKAGE = "com.hiczp.telegram.bot.protocol.event"
        private const val TELEGRAM_BOT_EVENTS_FILE = "TelegramBotEvents"
        private const val UPDATES_FILE = "Updates"
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(IncomingUpdateContainer::class.qualifiedName!!)
        val (valid, invalid) = symbols.partition { it.validate() }

        if (valid.isEmpty()) {
            return invalid
        }

        val classDeclarations = valid.filterIsInstance<KSClassDeclaration>()

        for (classDecl in classDeclarations) {
            processClassDeclaration(classDecl)
        }

        return invalid
    }

    private fun processClassDeclaration(classDecl: KSClassDeclaration) {
        logger.info("Processing @${IncomingUpdateContainer::class.simpleName} class: ${classDecl.qualifiedName?.asString()}")

        // Get all properties from the primary constructor
        val primaryConstructor = classDecl.primaryConstructor
        if (primaryConstructor == null) {
            logger.warn("Class ${classDecl.simpleName.asString()} has no primary constructor, skipping")
            return
        }

        val properties = primaryConstructor.parameters.mapNotNull { param ->
            val name = param.name?.asString() ?: return@mapNotNull null
            val type = param.type.resolve()
            val isNullable = type.isMarkedNullable

            // Get SerialName annotation value if present
            val serialName = param.annotations
                .firstOrNull { it.shortName.asString() == "SerialName" }
                ?.arguments
                ?.firstOrNull()
                ?.value as? String

            // Get KDoc description from the property
            val description = getPropertyDescription(classDecl, name)

            PropertyInfo(
                propertyName = name,
                originalName = serialName ?: name,
                type = type,
                isNullable = isNullable,
                description = description
            )
        }

        val requiredFields = properties.filter { !it.isNullable }
        val optionalFields = properties.filter { it.isNullable }

        if (requiredFields.isEmpty()) {
            logger.warn("No required fields found in ${classDecl.simpleName.asString()}, skipping")
            return
        }

        // Find the update_id field (should be the first required field with Long type)
        val updateIdField = requiredFields.firstOrNull {
            it.originalName == "update_id"
        } ?: requiredFields.first()

        logger.info("Found ${optionalFields.size} optional fields for event generation")

        generateTelegramBotEventsFile(classDecl, optionalFields, updateIdField)
        generateUpdatesExtensionFile(classDecl, optionalFields, updateIdField)
    }

    private fun getPropertyDescription(classDecl: KSClassDeclaration, propertyName: String): String? {
        // Try to find KDoc for the property by looking at the property declaration
        val property = classDecl.declarations
            .filterIsInstance<KSPropertyDeclaration>()
            .firstOrNull { it.simpleName.asString() == propertyName }

        // KSP doesn't directly expose KDoc, but we can get docString in newer versions
        return property?.docString
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun generateTelegramBotEventsFile(
        sourceClass: KSClassDeclaration,
        optionalFields: List<PropertyInfo>,
        updateIdField: PropertyInfo
    ) {
        val fileSpec = FileSpec.builder(EVENT_PACKAGE, TELEGRAM_BOT_EVENTS_FILE)
            .indent("    ")
            .addFileComment("Auto-generated by KSP from @${IncomingUpdateContainer::class.simpleName}, do not modify this file manually")

        // Build sealed interface TelegramBotEvent with @Serializable and @JsonClassDiscriminator
        val updateIdTypeName = updateIdField.type.toTypeName()
        val interfaceBuilder = TypeSpec.interfaceBuilder("TelegramBotEvent")
            .addModifiers(KModifier.SEALED)
            .addAnnotation(
                AnnotationSpec.builder(ClassName("kotlin", "OptIn"))
                    .addMember("%T::class", ExperimentalSerializationApi::class)
                    .build()
            )
            .addAnnotation(Serializable::class)
            .addAnnotation(
                AnnotationSpec.builder(JsonClassDiscriminator::class)
                    .addMember("%S", "type")
                    .build()
            )
            .addProperty(
                PropertySpec.builder(updateIdField.propertyName, updateIdTypeName)
                    .build()
            )

        fileSpec.addType(interfaceBuilder.build())

        // Generate event classes for each optional field
        for (field in optionalFields) {
            val eventClassName = "${snakeToPascalCase(field.originalName)}Event"
            // SerialName uses the property name directly (e.g., "message" for MessageEvent)
            val serialNameValue = field.propertyName
            // Make the field type non-null in the event class (since the event is only created when the field is present)
            val fieldTypeName = field.type.toTypeName().copy(nullable = false)

            val constructorBuilder = FunSpec.constructorBuilder()
                .addParameter(updateIdField.propertyName, updateIdTypeName)
                .addParameter(field.propertyName, fieldTypeName)

            val classBuilder = TypeSpec.classBuilder(eventClassName)
                .addModifiers(KModifier.DATA)
                .addAnnotation(Serializable::class)
                .addAnnotation(
                    AnnotationSpec.builder(SerialName::class)
                        .addMember("%S", serialNameValue)
                        .build()
                )
                .addSuperinterface(ClassName(EVENT_PACKAGE, "TelegramBotEvent"))
                .primaryConstructor(constructorBuilder.build())
                .addProperty(
                    PropertySpec.builder(updateIdField.propertyName, updateIdTypeName)
                        .initializer(updateIdField.propertyName)
                        .addModifiers(KModifier.OVERRIDE)
                        .build()
                )
                .addProperty(
                    PropertySpec.builder(field.propertyName, fieldTypeName)
                        .initializer(field.propertyName)
                        .build()
                )

            if (!field.description.isNullOrBlank()) {
                classBuilder.addKdoc(field.description)
            }

            fileSpec.addType(classBuilder.build())
        }

        fileSpec.build().writeTo(
            codeGenerator,
            Dependencies(true, sourceClass.containingFile!!)
        )
    }

    private fun generateUpdatesExtensionFile(
        sourceClass: KSClassDeclaration,
        optionalFields: List<PropertyInfo>,
        updateIdField: PropertyInfo
    ) {
        val sourceClassName = sourceClass.toClassName()
        val unrecognizedUpdateException =
            ClassName("com.hiczp.telegram.bot.protocol.exception", "UnrecognizedUpdateException")

        val fileSpec = FileSpec.builder(EVENT_PACKAGE, UPDATES_FILE)
            .indent("    ")
            .addFileComment("Auto-generated by KSP from @${IncomingUpdateContainer::class.simpleName}, do not modify this file manually")

        // Generate toTelegramBotEvent() extension function
        val toEventCodeBlockBuilder = CodeBlock.builder()
        for (field in optionalFields) {
            val eventClassName = "${snakeToPascalCase(field.originalName)}Event"
            toEventCodeBlockBuilder.addStatement("${field.propertyName}?.let {")
            toEventCodeBlockBuilder.addStatement("    return $eventClassName(${updateIdField.propertyName}, it)")
            toEventCodeBlockBuilder.addStatement("}")
        }
        toEventCodeBlockBuilder.addStatement("throw %T(this)", unrecognizedUpdateException)

        val toEventFunBuilder = FunSpec.builder("toTelegramBotEvent")
            .receiver(sourceClassName)
            .returns(ClassName(EVENT_PACKAGE, "TelegramBotEvent"))
            .addCode(toEventCodeBlockBuilder.build())

        fileSpec.addFunction(toEventFunBuilder.build())

        // Generate toUpdate() extension function for each event class
        for (field in optionalFields) {
            val eventClassName = "${snakeToPascalCase(field.originalName)}Event"
            val eventClassTypeName = ClassName(EVENT_PACKAGE, eventClassName)

            val toUpdateCodeBlock = CodeBlock.builder()
                .addStatement(
                    "return %T(${updateIdField.propertyName} = ${updateIdField.propertyName}, ${field.propertyName} = ${field.propertyName})",
                    sourceClassName
                )

            val toUpdateFunBuilder = FunSpec.builder("toUpdate")
                .receiver(eventClassTypeName)
                .returns(sourceClassName)
                .addCode(toUpdateCodeBlock.build())

            fileSpec.addFunction(toUpdateFunBuilder.build())
        }

        fileSpec.build().writeTo(
            codeGenerator,
            Dependencies(true, sourceClass.containingFile!!)
        )
    }

    private fun snakeToPascalCase(s: String): String {
        return s.split("_").joinToString("") { part ->
            part.replaceFirstChar { it.uppercase() }
        }
    }

    private data class PropertyInfo(
        val propertyName: String,
        val originalName: String,
        val type: KSType,
        val isNullable: Boolean,
        val description: String?
    )
}
