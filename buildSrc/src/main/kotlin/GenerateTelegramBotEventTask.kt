import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * Gradle task that generates TelegramBotEvent sealed interface and event classes from the Update model.
 *
 * This task reads the generated Update data class (annotated with [com.hiczp.telegram.bot.protocol.type.IncomingUpdateContainer])
 * and generates a type-safe event hierarchy for handling incoming Telegram updates.
 *
 * ## Generated Files
 *
 * ### TelegramBotEvents.kt
 * Contains:
 * - `TelegramBotEvent` sealed interface with a common `updateId: Long` property
 * - Event data classes for each optional field in Update (e.g., `MessageEvent`, `EditedMessageEvent`)
 *
 * ### Updates.kt
 * Contains:
 * - `Update.toTelegramBotEvent()` extension function that converts an `Update` to the appropriate
 *   `TelegramBotEvent` subtype by checking each nullable field. Throws an error if no field is set.
 *
 * ## Configuration
 *
 * The task requires:
 * - `protocolSourceDir`: Directory containing the generated protocol source code (to find classes annotated with @IncomingUpdateContainer)
 * - `outputDir`: Directory where generated event code will be written
 */
abstract class GenerateTelegramBotEventTask : DefaultTask() {
    companion object {
        private const val EVENT_PACKAGE = "com.hiczp.telegram.bot.client.event"
        private const val PROTOCOL_MODEL_PACKAGE = "com.hiczp.telegram.bot.protocol.model"
        private const val INCOMING_UPDATE_CONTAINER_ANNOTATION = "com.hiczp.telegram.bot.protocol.type.IncomingUpdateContainer"
        private const val TELEGRAM_BOT_EVENTS_FILE = "TelegramBotEvents"
        private const val UPDATES_FILE = "Updates"
    }

    /**
     * Input directory containing the generated protocol source code.
     * The task will scan for classes annotated with @IncomingUpdateContainer.
     */
    @get:InputDirectory
    abstract val protocolSourceDir: DirectoryProperty

    /**
     * Output directory for generated event code.
     * Generated files will be written to the event package directory.
     */
    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    init {
        group = "codegen"
        description = "Generate TelegramBotEvent sealed interface and event classes from Update model"
    }

    private data class UpdateField(
        val propertyName: String,
        val originalName: String,
        val typeName: String,
        val isNullable: Boolean,
        val description: String?
    )

    @TaskAction
    fun generate() {
        val protocolDir = protocolSourceDir.get().asFile
        val outputDirectory = outputDir.get().asFile
        logger.lifecycle("Scanning protocol source directory: ${protocolDir.absolutePath}")
        val containerFile = findIncomingUpdateContainerFile(protocolDir)
        if (containerFile == null) {
            logger.warn("No class annotated with @$INCOMING_UPDATE_CONTAINER_ANNOTATION found. Skipping event generation.")
            return
        }
        logger.lifecycle("Found IncomingUpdateContainer at: ${containerFile.absolutePath}")
        val updateFields = parseUpdateFile(containerFile)
        if (updateFields.isEmpty()) {
            logger.warn("No fields found in the container file. Skipping event generation.")
            return
        }
        val requiredFields = updateFields.filter { !it.isNullable }
        val optionalFields = updateFields.filter { it.isNullable }
        if (requiredFields.isEmpty()) {
            logger.warn("No required field (update_id) found. Skipping event generation.")
            return
        }
        val updateIdField = requiredFields.first { it.originalName == "update_id" }
        logger.lifecycle("Found ${optionalFields.size} optional fields for event generation")
        val outputPackageDir = File(outputDirectory, EVENT_PACKAGE.replace('.', '/'))
        outputPackageDir.deleteRecursively()
        outputPackageDir.mkdirs()
        generateTelegramBotEventsFile(outputPackageDir, optionalFields, updateIdField)
        logger.lifecycle("Successfully generated TelegramBotEvents to ${outputPackageDir.absolutePath}")
    }

    private fun findIncomingUpdateContainerFile(protocolDir: File): File? {
        // Search for both fully qualified name and simple name
        val fullPattern = "@$INCOMING_UPDATE_CONTAINER_ANNOTATION"
        val simplePattern = "@IncomingUpdateContainer"
        fun searchInDirectory(dir: File): File? {
            if (!dir.exists()) return null
            dir.listFiles()?.forEach { file ->
                if (file.isDirectory) {
                    val result = searchInDirectory(file)
                    if (result != null) return result
                } else if (file.extension == "kt") {
                    val content = file.readText()
                    if (content.contains(fullPattern) || content.contains(simplePattern)) {
                        return file
                    }
                }
            }
            return null
        }
        return searchInDirectory(protocolDir)
    }

    private fun parseUpdateFile(file: File): List<UpdateField> {
        val content = file.readText()
        val fields = mutableListOf<UpdateField>()
        val lines = content.lines()
        var i = 0
        var pendingDescription: String? = null
        while (i < lines.size) {
            val line = lines[i].trim()
            if (line.startsWith("/**")) {
                val descBuilder = StringBuilder()
                if (!line.startsWith("/** ") && !line.startsWith("/**\n")) {
                    val singleLine = line.removePrefix("/**").removeSuffix("*/").trim()
                    if (singleLine.isNotEmpty()) {
                        descBuilder.append(singleLine)
                    }
                    pendingDescription = descBuilder.toString().trim().ifEmpty { null }
                    i++
                    continue
                }
                var j = i + 1
                while (j < lines.size) {
                    val commentLine = lines[j].trim()
                    if (commentLine == "*/" || commentLine.endsWith("*/")) {
                        break
                    }
                    val cleanedLine = commentLine.removePrefix("*").trim()
                    if (cleanedLine.isNotEmpty()) {
                        if (descBuilder.isNotEmpty()) {
                            descBuilder.append(" ")
                        }
                        descBuilder.append(cleanedLine)
                    }
                    j++
                }
                pendingDescription = descBuilder.toString().trim().ifEmpty { null }
                i = j + 1
                continue
            }
            if (line.startsWith("@SerialName(\"")) {
                i++
                continue
            }
            if (line.startsWith("public val ")) {
                var serialName: String? = null
                var k = i - 1
                while (k >= 0) {
                    val prevLine = lines[k].trim()
                    if (prevLine.startsWith("@SerialName(\"")) {
                        serialName = prevLine.substringAfter("@SerialName(\"").substringBefore("\")")
                        break
                    }
                    if (prevLine.startsWith("public val ") || prevLine == ")") {
                        break
                    }
                    k--
                }
                val withoutPublic = line.removePrefix("public val ")
                val colonIndex = withoutPublic.indexOf(':')
                if (colonIndex > 0) {
                    val propertyName = withoutPublic.substring(0, colonIndex).trim()
                    val typeAndDefault = withoutPublic.substring(colonIndex + 1).trim()
                    val typeStr = typeAndDefault.substringBefore("=").substringBefore(",").trim()
                    val isNullable = typeStr.endsWith("?")
                    val typeName = typeStr.removeSuffix("?").trim()
                    fields.add(UpdateField(
                        propertyName = propertyName,
                        originalName = serialName ?: propertyName,
                        typeName = typeName,
                        isNullable = isNullable,
                        description = pendingDescription
                    ))
                    pendingDescription = null
                }
            }
            i++
        }
        return fields
    }

    private fun snakeToPascalCase(s: String): String {
        return s.split("_").joinToString("") { part ->
            part.replaceFirstChar { it.uppercase() }
        }
    }

    private fun generateTelegramBotEventsFile(
        outputDir: File,
        optionalFields: List<UpdateField>,
        updateIdField: UpdateField
    ) {
        // Generate TelegramBotEvents.kt with sealed interface and event classes
        val eventsFileSpec = FileSpec.builder(EVENT_PACKAGE, TELEGRAM_BOT_EVENTS_FILE)
            .indent("    ")
            .addFileComment("Auto-generated from the Update model, do not modify this file manually")
        val updateIdType = parseTypeName(updateIdField.typeName)
        val interfaceBuilder = TypeSpec.interfaceBuilder("TelegramBotEvent")
            .addModifiers(KModifier.SEALED)
            .addProperty(PropertySpec.builder(updateIdField.propertyName, updateIdType).build())
        eventsFileSpec.addType(interfaceBuilder.build())
        for (field in optionalFields) {
            val eventClassName = "${snakeToPascalCase(field.originalName)}Event"
            val fieldType = parseTypeName(field.typeName)
            val constructorBuilder = FunSpec.constructorBuilder()
                .addParameter(updateIdField.propertyName, updateIdType)
                .addParameter(field.propertyName, fieldType)
            val classBuilder = TypeSpec.classBuilder(eventClassName)
                .addModifiers(KModifier.DATA)
                .addSuperinterface(ClassName(EVENT_PACKAGE, "TelegramBotEvent"))
                .primaryConstructor(constructorBuilder.build())
                .addProperty(
                    PropertySpec.builder(updateIdField.propertyName, updateIdType)
                        .initializer(updateIdField.propertyName)
                        .addModifiers(KModifier.OVERRIDE)
                        .build()
                )
                .addProperty(
                    PropertySpec.builder(field.propertyName, fieldType)
                        .initializer(field.propertyName)
                        .build()
                )
            if (!field.description.isNullOrBlank()) {
                classBuilder.addKdoc(field.description)
            }
            eventsFileSpec.addType(classBuilder.build())
        }
        eventsFileSpec.build().writeToWithUnixLineEndings(outputDir)
        // Generate Updates.kt with toTelegramBotEvent() extension function
        val updatesFileSpec = FileSpec.builder(EVENT_PACKAGE, UPDATES_FILE)
            .indent("    ")
            .addFileComment("Auto-generated from Update model, do not modify this file manually")
        val updateType = ClassName(PROTOCOL_MODEL_PACKAGE, "Update")
        val funBuilder = FunSpec.builder("toTelegramBotEvent")
            .receiver(updateType)
            .returns(ClassName(EVENT_PACKAGE, "TelegramBotEvent"))
            .addCode(buildToEventCodeBlock(optionalFields, updateIdField))
        updatesFileSpec.addFunction(funBuilder.build())
        updatesFileSpec.build().writeToWithUnixLineEndings(outputDir)
    }

    private fun buildToEventCodeBlock(optionalFields: List<UpdateField>, updateIdField: UpdateField): CodeBlock {
        val builder = CodeBlock.builder()
        for (field in optionalFields) {
            val eventClassName = "${snakeToPascalCase(field.originalName)}Event"
            builder.addStatement("${field.propertyName}?.let {")
            builder.addStatement("    return $eventClassName(${updateIdField.propertyName}, it)")
            builder.addStatement("}")
        }
        builder.addStatement("error(\"Unrecognized Update: \$this\")")
        return builder.build()
    }

    private fun parseTypeName(typeStr: String): TypeName {
        val trimmed = typeStr.trim()
        if (trimmed.contains("<")) {
            val baseName = trimmed.substringBefore("<")
            val genericPart = trimmed.substringAfter("<").removeSuffix(">")
            val innerType = parseTypeName(genericPart)
            return ClassName("kotlin.collections", baseName).parameterizedBy(innerType)
        }
        return when (trimmed) {
            "String" -> ClassName("kotlin", "String")
            "Int" -> ClassName("kotlin", "Int")
            "Long" -> ClassName("kotlin", "Long")
            "Boolean" -> ClassName("kotlin", "Boolean")
            "Double" -> ClassName("kotlin", "Double")
            "Float" -> ClassName("kotlin", "Float")
            "Unit" -> ClassName("kotlin", "Unit")
            "Any" -> ClassName("kotlin", "Any")
            else -> ClassName(PROTOCOL_MODEL_PACKAGE, trimmed)
        }
    }

    private fun FileSpec.writeToWithUnixLineEndings(directory: File) {
        val stringBuilder = StringBuilder()
        writeTo(stringBuilder)
        val content = stringBuilder.toString().replace("\r\n", "\n")
        directory.mkdirs()
        val outputFile = directory.resolve("$name.kt")
        outputFile.writeText(content, Charsets.UTF_8)
    }
}
