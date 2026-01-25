import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class GenerateKtorfitInterfacesTask : DefaultTask() {

    @get:InputFile
    abstract val swaggerFile: RegularFileProperty

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    init {
        group = "codegen"
        description = "Generate Ktorfit interfaces from Swagger/OpenAPI specification"
        outputDir.convention(project.layout.projectDirectory.dir("src/commonMain/kotlin"))
    }

    private lateinit var allSchemas: Map<String, JsonNode>
    private val processedSchemas = mutableSetOf<String>()
    private val generatedRequestBodies = mutableMapOf<String, String>() // operationId -> className

    private data class MultipartOperationInfo(val schema: JsonNode, val returnType: TypeName)

    private val multipartOperations = mutableMapOf<String, MultipartOperationInfo>() // operationId -> info
    private val replyMarkupTypes = mutableSetOf<String>() // Collected ReplyMarkup types

    @TaskAction
    fun generate() {
        val swagger = ObjectMapper().readTree(swaggerFile.get().asFile)
        val outputDirectory = outputDir.get().asFile

        logger.lifecycle("Generating Ktorfit interfaces from ${swaggerFile.get().asFile.name}")

        // Clean output directory
        val apiDir = File(outputDirectory, "com/hiczp/telegram/bot/api")
        val modelDir = File(apiDir, "model")
        val typeDir = File(apiDir, "type")
        apiDir.deleteRecursively()
        modelDir.mkdirs()
        typeDir.mkdirs()

        // Store all schemas for reference
        allSchemas = swagger.get("components")?.get("schemas")?.fields()?.asSequence()
            ?.associate { it.key to it.value } ?: emptyMap()

        // Collect all ReplyMarkup types from the swagger spec
        collectReplyMarkupTypes(swagger)
        logger.lifecycle("Detected ReplyMarkup types: $replyMarkupTypes")

        // Generate TelegramResponse type first
        generateTelegramResponseType(outputDirectory)

        // Generate ReplyMarkup sealed interface if we found any types
        if (replyMarkupTypes.isNotEmpty()) {
            generateReplyMarkupInterface(outputDirectory)
        }

        // Generate models
        generateModels(swagger, outputDirectory)

        // Generate API interface
        generateApiInterface(swagger, outputDirectory)

        logger.lifecycle("Successfully generated Ktorfit interfaces to ${outputDirectory.absolutePath}")
    }

    private fun generateTelegramResponseType(outputDir: File) {
        val packageName = "com.hiczp.telegram.bot.api.type"
        val className = "TelegramResponse"

        val fileSpec = createFileSpec(packageName, className)

        val responseClass = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.DATA)
            .addTypeVariable(TypeVariableName("T"))
            .addAnnotation(
                AnnotationSpec.builder(ClassName("kotlinx.serialization", "Serializable"))
                    .build()
            )
            .addKdoc("Generic wrapper for Telegram Bot API responses")
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("ok", BOOLEAN)
                    .addParameter(
                        ParameterSpec.builder("result", TypeVariableName("T").copy(nullable = true))
                            .defaultValue("null")
                            .build()
                    )
                    .addParameter(
                        ParameterSpec.builder("description", STRING.copy(nullable = true))
                            .defaultValue("null")
                            .build()
                    )
                    .addParameter(
                        ParameterSpec.builder("errorCode", INT.copy(nullable = true))
                            .addAnnotation(
                                AnnotationSpec.builder(ClassName("kotlinx.serialization", "SerialName"))
                                    .addMember("%S", "error_code")
                                    .build()
                            )
                            .defaultValue("null")
                            .build()
                    )
                    .build()
            )
            .addProperty(
                PropertySpec.builder("ok", BOOLEAN)
                    .initializer("ok")
                    .addKdoc("True if the request was successful")
                    .build()
            )
            .addProperty(
                PropertySpec.builder("result", TypeVariableName("T").copy(nullable = true))
                    .initializer("result")
                    .addKdoc("The result of the request, if successful")
                    .build()
            )
            .addProperty(
                PropertySpec.builder("description", STRING.copy(nullable = true))
                    .initializer("description")
                    .addKdoc("Human-readable description of the result or error")
                    .build()
            )
            .addProperty(
                PropertySpec.builder("errorCode", INT.copy(nullable = true))
                    .initializer("errorCode")
                    .addKdoc("Error code, if the request failed")
                    .build()
            )
            .build()

        fileSpec.addType(responseClass)
        fileSpec.build().writeTo(outputDir)
    }

    /**
     * Collect all types that appear in reply_markup fields across the entire swagger spec
     */
    private fun collectReplyMarkupTypes(swagger: JsonNode) {
        val paths = swagger.get("paths") ?: return

        paths.fields().forEach { (_, methods) ->
            methods.fields().forEach { (_, operation) ->
                // Check in request body
                val requestBody = operation.get("requestBody")
                if (requestBody != null) {
                    collectReplyMarkupFromRequestBody(requestBody)
                }
            }
        }

        // Also check in schemas for any reply_markup fields
        allSchemas.values.forEach { schema ->
            val properties = schema.get("properties")
            if (properties != null && properties.has("reply_markup")) {
                val replyMarkupField = properties.get("reply_markup")
                collectReplyMarkupFromField(replyMarkupField)
            }
        }
    }

    private fun collectReplyMarkupFromRequestBody(requestBody: JsonNode) {
        val content = requestBody.get("content") ?: return

        // Check both JSON and multipart content types
        listOf("application/json", "multipart/form-data").forEach { contentType ->
            val contentNode = content.get(contentType)
            if (contentNode != null) {
                val schema = contentNode.get("schema")
                if (schema != null) {
                    val properties = schema.get("properties")
                    if (properties != null && properties.has("reply_markup")) {
                        val replyMarkupField = properties.get("reply_markup")
                        collectReplyMarkupFromField(replyMarkupField)
                    }
                }
            }
        }
    }

    private fun collectReplyMarkupFromField(field: JsonNode) {
        // Check for oneOf
        val oneOf = field.get("oneOf")
        if (oneOf != null && oneOf.isArray) {
            oneOf.forEach { option ->
                val ref = option.get("\$ref")?.asText()
                if (ref != null) {
                    val typeName = ref.substringAfterLast("/")
                    replyMarkupTypes.add(typeName)
                }
            }
        }

        // Check for allOf (single type reference)
        val allOf = field.get("allOf")
        if (allOf != null && allOf.isArray) {
            allOf.forEach { option ->
                val ref = option.get("\$ref")?.asText()
                if (ref != null) {
                    val typeName = ref.substringAfterLast("/")
                    replyMarkupTypes.add(typeName)
                }
            }
        }
    }

    private fun generateReplyMarkupInterface(outputDir: File) {
        val packageName = "com.hiczp.telegram.bot.api.model"
        val className = "ReplyMarkup"

        val fileSpec = createFileSpec(packageName, className, "Sealed interface for reply markup types")

        // Generate KDoc with dynamically collected types
        val typesString = replyMarkupTypes.sorted().joinToString(", ")
        val interfaceBuilder = TypeSpec.interfaceBuilder(className)
            .addModifiers(KModifier.SEALED)
            .addAnnotation(
                AnnotationSpec.builder(ClassName("kotlinx.serialization", "Serializable"))
                    .build()
            )
            .addKdoc("Sealed interface for all reply markup types ($typesString)")

        fileSpec.addType(interfaceBuilder.build())
        fileSpec.build().writeTo(outputDir)
    }

    private fun generateModels(swagger: JsonNode, outputDir: File) {
        val schemas = swagger.get("components")?.get("schemas") ?: return
        val modelPackage = "com.hiczp.telegram.bot.api.model"

        schemas.fields().forEach { (schemaName, schemaNode) ->
            if (schemaName in processedSchemas) return@forEach

            try {
                generateModel(modelPackage, schemaName, schemaNode, outputDir)
            } catch (e: Exception) {
                logger.warn("Failed to generate model for $schemaName: ${e.message}")
            }
        }
    }

    private fun generateModel(packageName: String, className: String, schema: JsonNode, outputDir: File) {
        val description = schema.get("description")?.asText()
        val type = schema.get("type")?.asText()
        val oneOf = schema.get("oneOf")

        // Handle union types
        if (oneOf != null && oneOf.isArray) {
            val unionMembers = oneOf.map { it.get("\$ref")?.asText()?.substringAfterLast("/") }.filterNotNull()

            // Check if this is a field-overlapping case (use largest subclass)
            if (shouldUseLargestSubclass(className, unionMembers)) {
                val largestMember = findLargestSubclass(unionMembers)
                if (largestMember != null) {
                    logger.lifecycle("Using largest subclass $largestMember for $className (field-overlapping union)")
                    // For MaybeInaccessibleMessage: don't generate the union type itself,
                    // but ensure all member types (Message, InaccessibleMessage) are still generated
                    processedSchemas.add(className)

                    // Generate member types if not already processed
                    unionMembers.forEach { memberName ->
                        if (memberName !in processedSchemas) {
                            val memberSchema = allSchemas[memberName]
                            if (memberSchema != null) {
                                generateModel(packageName, memberName, memberSchema, outputDir)
                            }
                        }
                    }
                    return
                }
            }

            // First, check if schema has explicit discriminator definition
            val discriminatorInfo = extractDiscriminatorFromSchema(schema, unionMembers)
                ?: findDiscriminatorInfo(unionMembers)

            if (discriminatorInfo != null) {
                generatePolymorphicSealedInterface(
                    packageName,
                    className,
                    description,
                    unionMembers,
                    discriminatorInfo,
                    outputDir
                )
            } else {
                logger.warn("Cannot determine discriminator for $className with members: $unionMembers")
                generateSealedInterfaceWithoutPolymorphism(packageName, className, description, unionMembers, outputDir)
            }
            processedSchemas.add(className)
            return
        }

        generateRegularClass(packageName, className, schema, outputDir)
        processedSchemas.add(className)
    }

    private fun generateRegularClass(packageName: String, className: String, schema: JsonNode, outputDir: File) {
        val description = schema.get("description")?.asText()
        val type = schema.get("type")?.asText()

        val fileSpec = createFileSpec(packageName, className)

        when (type) {
            "object" -> {
                val properties = schema.get("properties")

                // If no properties, generate a simple object instead of data class
                if (properties == null || !properties.isObject || !properties.fields().hasNext()) {
                    val objectBuilder = TypeSpec.objectBuilder(className)
                        .addAnnotation(
                            AnnotationSpec.builder(ClassName("kotlinx.serialization", "Serializable"))
                                .build()
                        )

                    // Check if this is a ReplyMarkup type
                    if (isReplyMarkupType(className)) {
                        objectBuilder.addSuperinterface(ClassName("com.hiczp.telegram.bot.api.model", "ReplyMarkup"))
                    }

                    if (description != null) {
                        objectBuilder.addKdoc(sanitizeKDoc(description))
                    }
                    fileSpec.addType(objectBuilder.build())
                } else {
                    val classBuilder = TypeSpec.classBuilder(className)
                        .addModifiers(KModifier.DATA)
                        .addAnnotation(
                            AnnotationSpec.builder(ClassName("kotlinx.serialization", "Serializable"))
                                .build()
                        )

                    // Check if this is a ReplyMarkup type
                    if (isReplyMarkupType(className)) {
                        classBuilder.addSuperinterface(ClassName("com.hiczp.telegram.bot.api.model", "ReplyMarkup"))
                    }

                    if (description != null) {
                        classBuilder.addKdoc(sanitizeKDoc(description))
                    }

                    val required = schema.get("required")?.map { it.asText() }?.toSet() ?: emptySet()
                    val constructorBuilder = FunSpec.constructorBuilder()

                    properties.fields().forEach { (propName, propSchema) ->
                        val propType = determinePropertyType(propSchema)
                        val isRequired = required.contains(propName)
                        val finalType = if (isRequired) propType else propType.copy(nullable = true)

                        val propDescription = propSchema.get("description")?.asText()
                        val camelCaseName = snakeToCamelCase(propName)
                        val propertyBuilder = PropertySpec.builder(camelCaseName, finalType)
                            .initializer(camelCaseName)

                        // Add @SerialName if property name differs from snake_case
                        if (camelCaseName != propName) {
                            propertyBuilder.addAnnotation(
                                AnnotationSpec.builder(ClassName("kotlinx.serialization", "SerialName"))
                                    .addMember("%S", propName)
                                    .build()
                            )
                        }

                        if (propDescription != null) {
                            propertyBuilder.addKdoc(sanitizeKDoc(propDescription))
                        }

                        val paramBuilder = ParameterSpec.builder(camelCaseName, finalType)
                        if (!isRequired) {
                            paramBuilder.defaultValue("null")
                        }

                        constructorBuilder.addParameter(paramBuilder.build())
                        classBuilder.addProperty(propertyBuilder.build())
                    }

                    classBuilder.primaryConstructor(constructorBuilder.build())
                    fileSpec.addType(classBuilder.build())
                }
            }

            else -> {
                // For non-object types, create a typealias
                val aliasType = determinePropertyType(schema)
                fileSpec.addTypeAlias(TypeAliasSpec.builder(className, aliasType).build())
            }
        }

        fileSpec.build().writeTo(outputDir)
    }

    private fun isReplyMarkupType(className: String): Boolean {
        // Use the dynamically collected replyMarkupTypes instead of hardcoding
        return className in replyMarkupTypes
    }

    private data class DiscriminatorInfo(
        val fieldName: String,
        val memberValues: Map<String, String> // memberClassName -> discriminatorValue
    )

    private fun shouldUseLargestSubclass(parentName: String, unionMembers: List<String>): Boolean {
        // Special handling for MaybeInaccessibleMessage: use Message as deserialization type
        // but keep InaccessibleMessage as a separate serializable class
        if (parentName == "MaybeInaccessibleMessage") return true

        val memberSchemas = unionMembers.mapNotNull { allSchemas[it] }
        if (memberSchemas.size != unionMembers.size) return false

        // Get all property names from all members
        val allPropertyNames = memberSchemas.flatMap { schema ->
            schema.get("properties")?.fields()?.asSequence()?.map { it.key }?.toList() ?: emptyList()
        }.toSet()

        if (allPropertyNames.isEmpty()) return false

        // Check if properties overlap significantly (>70% overlap)
        val overlaps = memberSchemas.map { schema ->
            val props = schema.get("properties")?.fields()?.asSequence()?.map { it.key }?.toSet() ?: emptySet()
            if (allPropertyNames.isEmpty()) 0.0 else props.intersect(allPropertyNames).size.toDouble() / allPropertyNames.size
        }

        return overlaps.all { it > 0.7 }
    }

    private fun findLargestSubclass(unionMembers: List<String>): String? {
        return unionMembers.maxByOrNull { memberName ->
            val schema = allSchemas[memberName] ?: return@maxByOrNull 0
            schema.get("properties")?.fields()?.asSequence()?.count() ?: 0
        }
    }

    /**
     * Extract discriminator info from OpenAPI discriminator field in schema
     */
    private fun extractDiscriminatorFromSchema(schema: JsonNode, unionMembers: List<String>): DiscriminatorInfo? {
        val discriminator = schema.get("discriminator") ?: return null
        val propertyName = discriminator.get("propertyName")?.asText() ?: return null
        val mapping = discriminator.get("mapping") ?: return null

        if (!mapping.isObject) return null

        val memberValues = mutableMapOf<String, String>()

        // Build reverse mapping: className -> discriminatorValue
        mapping.fields().forEach { (discriminatorValue, ref) ->
            val className = ref.asText().substringAfterLast("/")
            if (className in unionMembers) {
                memberValues[className] = discriminatorValue
            }
        }

        // Check if we have mappings for all members
        if (memberValues.size == unionMembers.size) {
            return DiscriminatorInfo(propertyName, memberValues)
        }

        return null
    }

    private fun findDiscriminatorInfo(unionMembers: List<String>): DiscriminatorInfo? {
        val commonDiscriminators = listOf("type", "source", "kind", "status")

        for (discriminator in commonDiscriminators) {
            val memberValues = mutableMapOf<String, String>()

            // Check if all members have this field
            val allHaveField = unionMembers.all { memberName ->
                val schema = allSchemas[memberName] ?: return@all false
                val properties = schema.get("properties") ?: return@all false
                properties.has(discriminator)
            }

            if (!allHaveField) continue

            // Try to extract discriminator values from each member
            unionMembers.forEach { memberName ->
                val schema = allSchemas[memberName] ?: return@forEach
                val properties = schema.get("properties") ?: return@forEach
                val field = properties.get(discriminator) ?: return@forEach

                // Try to find the constant value in description or enum
                val value = extractDiscriminatorValue(field, memberName)
                if (value != null) {
                    memberValues[memberName] = value
                }
            }

            // If we found values for all members and they're unique, we have a discriminator
            if (memberValues.size == unionMembers.size && memberValues.values.toSet().size == unionMembers.size) {
                return DiscriminatorInfo(discriminator, memberValues)
            }
        }

        return null
    }

    private fun extractDiscriminatorValue(field: JsonNode, memberName: String): String? {
        // Check for enum with single value
        val enumValues = field.get("enum")
        if (enumValues != null && enumValues.isArray && enumValues.size() == 1) {
            return enumValues.get(0).asText()
        }

        // Extract from description patterns like: always "value" or must be "value"
        val description = field.get("description")?.asText() ?: return null

        // Pattern: always "value" or Type of ..., always "value"
        val alwaysPattern = """always\s+[""]([^""]+)[""]""".toRegex()
        val match = alwaysPattern.find(description)
        if (match != null) {
            return match.groupValues[1]
        }

        // Pattern: must be "value"
        val mustBePattern = """must be\s+[""]([^""]+)[""]""".toRegex()
        val mustMatch = mustBePattern.find(description)
        if (mustMatch != null) {
            return mustMatch.groupValues[1]
        }

        // Fallback: try to infer from class name (e.g., BackgroundFillSolid -> "solid")
        return inferDiscriminatorFromClassName(memberName)
    }

    private fun inferDiscriminatorFromClassName(className: String): String? {
        // Pattern: ParentNameValue -> value (e.g., BackgroundFillSolid -> solid)
        val parts = className.split(Regex("(?=[A-Z])")).filter { it.isNotEmpty() }
        if (parts.size >= 2) {
            val lastPart = parts.last().lowercase()
            // Convert camelCase to snake_case
            return lastPart.replace(Regex("([a-z])([A-Z])"), "$1_$2").lowercase()
        }
        return null
    }

    private fun generatePolymorphicSealedInterface(
        packageName: String,
        interfaceName: String,
        description: String?,
        unionMembers: List<String>,
        discriminatorInfo: DiscriminatorInfo,
        outputDir: File
    ) {
        val fileSpec = createFileSpec(packageName, interfaceName)

        val camelDiscriminator = snakeToCamelCase(discriminatorInfo.fieldName)

        // Create sealed interface
        val interfaceBuilder = TypeSpec.interfaceBuilder(interfaceName)
            .addModifiers(KModifier.SEALED)
            .addAnnotation(
                AnnotationSpec.builder(ClassName("kotlinx.serialization", "Serializable"))
                    .build()
            )
            .addAnnotation(
                AnnotationSpec.builder(ClassName("kotlinx.serialization.json", "JsonClassDiscriminator"))
                    .addMember("%S", discriminatorInfo.fieldName)
                    .build()
            )
            .addAnnotation(
                AnnotationSpec.builder(ClassName("kotlin", "OptIn"))
                    .addMember("%T::class", ClassName("kotlinx.serialization", "ExperimentalSerializationApi"))
                    .build()
            )

        if (description != null) {
            interfaceBuilder.addKdoc(sanitizeKDoc(description))
        }

        // Don't add discriminator property to the interface when using @JsonClassDiscriminator
        // The serialization library will handle it automatically

        fileSpec.addType(interfaceBuilder.build())

        // Generate subclasses in the same file
        unionMembers.forEach { memberName ->
            val memberSchema = allSchemas[memberName]
            if (memberSchema != null) {
                val memberClass =
                    generateSubclass(packageName, memberName, memberSchema, interfaceName, discriminatorInfo)
                if (memberClass != null) {
                    fileSpec.addType(memberClass)
                    processedSchemas.add(memberName)
                }
            }
        }

        fileSpec.build().writeTo(outputDir)
    }

    private fun generateSubclass(
        packageName: String,
        className: String,
        schema: JsonNode,
        parentInterface: String,
        discriminatorInfo: DiscriminatorInfo
    ): TypeSpec? {
        val description = schema.get("description")?.asText()
        val properties = schema.get("properties") ?: return null
        val required = schema.get("required")?.map { it.asText() }?.toSet() ?: emptySet()

        val discriminatorValue = discriminatorInfo.memberValues[className] ?: return null

        val classBuilder = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.DATA)
            .addSuperinterface(ClassName(packageName, parentInterface))
            .addAnnotation(
                AnnotationSpec.builder(ClassName("kotlinx.serialization", "Serializable"))
                    .build()
            )
            .addAnnotation(
                AnnotationSpec.builder(ClassName("kotlinx.serialization", "SerialName"))
                    .addMember("%S", discriminatorValue)
                    .build()
            )

        if (description != null) {
            classBuilder.addKdoc(sanitizeKDoc(description))
        }

        val constructorBuilder = FunSpec.constructorBuilder()

        properties.fields().forEach { (propName, propSchema) ->
            // Skip the discriminator field - @JsonClassDiscriminator handles it automatically
            if (propName == discriminatorInfo.fieldName) {
                return@forEach
            }

            val propType = determinePropertyType(propSchema)
            val isRequired = required.contains(propName)
            val finalType = if (isRequired) propType else propType.copy(nullable = true)

            val propDescription = propSchema.get("description")?.asText()
            val camelCaseName = snakeToCamelCase(propName)

            val propertyBuilder = PropertySpec.builder(camelCaseName, finalType)
                .initializer(camelCaseName)

            // Add @SerialName if property name differs from snake_case
            if (camelCaseName != propName) {
                propertyBuilder.addAnnotation(
                    AnnotationSpec.builder(ClassName("kotlinx.serialization", "SerialName"))
                        .addMember("%S", propName)
                        .build()
                )
            }

            if (propDescription != null) {
                propertyBuilder.addKdoc(sanitizeKDoc(propDescription))
            }

            val paramBuilder = ParameterSpec.builder(camelCaseName, finalType)
            if (!isRequired) {
                paramBuilder.defaultValue("null")
            }

            constructorBuilder.addParameter(paramBuilder.build())
            classBuilder.addProperty(propertyBuilder.build())
        }

        // Only add primary constructor if there are parameters
        if (constructorBuilder.parameters.isNotEmpty()) {
            classBuilder.primaryConstructor(constructorBuilder.build())
        } else {
            // If no properties, remove DATA modifier and make it a regular class
            classBuilder.modifiers.remove(KModifier.DATA)
        }
        return classBuilder.build()
    }

    private fun generateSealedInterfaceWithoutPolymorphism(
        packageName: String,
        interfaceName: String,
        description: String?,
        unionMembers: List<String>,
        outputDir: File
    ) {
        val fileSpec = createFileSpec(
            packageName,
            interfaceName,
            "WARNING: This sealed interface does not have a clear discriminator field"
        )

        val interfaceBuilder = TypeSpec.interfaceBuilder(interfaceName)
            .addModifiers(KModifier.SEALED)
            .addAnnotation(
                AnnotationSpec.builder(ClassName("kotlinx.serialization", "Serializable"))
                    .build()
            )

        if (description != null) {
            interfaceBuilder.addKdoc(sanitizeKDoc(description))
        }

        fileSpec.addType(interfaceBuilder.build())

        // Generate subclasses in the same file
        unionMembers.forEach { memberName ->
            val memberSchema = allSchemas[memberName]
            if (memberSchema != null) {
                val memberClass = generateSimpleSubclass(packageName, memberName, memberSchema, interfaceName)
                if (memberClass != null) {
                    fileSpec.addType(memberClass)
                    processedSchemas.add(memberName)
                }
            }
        }

        fileSpec.build().writeTo(outputDir)
    }

    private fun generateSimpleSubclass(
        packageName: String,
        className: String,
        schema: JsonNode,
        parentInterface: String
    ): TypeSpec? {
        val description = schema.get("description")?.asText()
        val properties = schema.get("properties") ?: return null
        val required = schema.get("required")?.map { it.asText() }?.toSet() ?: emptySet()

        val classBuilder = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.DATA)
            .addSuperinterface(ClassName(packageName, parentInterface))
            .addAnnotation(
                AnnotationSpec.builder(ClassName("kotlinx.serialization", "Serializable"))
                    .build()
            )

        if (description != null) {
            classBuilder.addKdoc(sanitizeKDoc(description))
        }

        val constructorBuilder = FunSpec.constructorBuilder()

        properties.fields().forEach { (propName, propSchema) ->
            val propType = determinePropertyType(propSchema)
            val isRequired = required.contains(propName)
            val finalType = if (isRequired) propType else propType.copy(nullable = true)

            val propDescription = propSchema.get("description")?.asText()
            val camelCaseName = snakeToCamelCase(propName)
            val propertyBuilder = PropertySpec.builder(camelCaseName, finalType)
                .initializer(camelCaseName)

            if (camelCaseName != propName) {
                propertyBuilder.addAnnotation(
                    AnnotationSpec.builder(ClassName("kotlinx.serialization", "SerialName"))
                        .addMember("%S", propName)
                        .build()
                )
            }

            if (propDescription != null) {
                propertyBuilder.addKdoc(sanitizeKDoc(propDescription))
            }

            val paramBuilder = ParameterSpec.builder(camelCaseName, finalType)
            if (!isRequired) {
                paramBuilder.defaultValue("null")
            }

            constructorBuilder.addParameter(paramBuilder.build())
            classBuilder.addProperty(propertyBuilder.build())
        }

        // Only add primary constructor if there are parameters
        if (constructorBuilder.parameters.isNotEmpty()) {
            classBuilder.primaryConstructor(constructorBuilder.build())
        } else {
            // If no properties, remove DATA modifier and make it a regular class
            classBuilder.modifiers.remove(KModifier.DATA)
        }
        return classBuilder.build()
    }

    private fun determinePropertyType(schema: JsonNode?): TypeName {
        if (schema == null) return STRING

        val type = schema.get("type")?.asText()
        val format = schema.get("format")?.asText()
        val ref = schema.get("\$ref")?.asText()
        val oneOf = schema.get("oneOf")

        return when {
            ref != null -> {
                val typeName = ref.substringAfterLast("/")
                // Replace MaybeInaccessibleMessage with Message
                if (typeName == "MaybeInaccessibleMessage") {
                    ClassName("com.hiczp.telegram.bot.api.model", "Message")
                } else {
                    ClassName("com.hiczp.telegram.bot.api.model", typeName)
                }
            }

            oneOf != null && oneOf.isArray -> {
                // Check if this is a ReplyMarkup oneOf
                val refs = oneOf.mapNotNull { it.get("\$ref")?.asText()?.substringAfterLast("/") }
                if (isReplyMarkupOneOf(refs)) {
                    ClassName("com.hiczp.telegram.bot.api.model", "ReplyMarkup")
                } else {
                    // Handle oneOf for basic types - choose the "largest" type
                    determineLargestPrimitiveType(oneOf)
                }
            }

            type == "array" -> {
                val items = schema.get("items")
                val itemType = if (items == null || items.isNull) {
                    // If items is not specified, use JsonElement instead of Any?
                    ClassName("kotlinx.serialization.json", "JsonElement")
                } else {
                    val determinedType = determinePropertyType(items)
                    // If the determined type is Any?, replace it with JsonElement
                    if (determinedType == ANY.copy(nullable = true)) {
                        ClassName("kotlinx.serialization.json", "JsonElement")
                    } else {
                        determinedType
                    }
                }
                LIST.parameterizedBy(itemType)
            }

            type == "object" -> {
                // Use JsonElement for dynamic objects instead of Map<String, Any?>
                ClassName("kotlinx.serialization.json", "JsonElement").copy(nullable = true)
            }

            type == "string" -> STRING
            type == "integer" -> {
                when (format) {
                    "int64" -> LONG
                    else -> INT
                }
            }

            type == "number" -> {
                when (format) {
                    "float" -> FLOAT
                    else -> DOUBLE
                }
            }

            type == "boolean" -> BOOLEAN
            else -> ClassName("kotlinx.serialization.json", "JsonElement").copy(nullable = true)
        }
    }

    private fun isReplyMarkupOneOf(refs: List<String>): Boolean {
        // Use the dynamically collected replyMarkupTypes instead of hardcoding
        // Check if all refs are ReplyMarkup types that were found in the swagger spec
        return refs.isNotEmpty() && refs.all { it in replyMarkupTypes }
    }

    /**
     * Determine the largest primitive type from a oneOf array.
     * Type hierarchy: String > Double > Float > Long > Int > Boolean
     */
    private fun determineLargestPrimitiveType(oneOf: JsonNode): TypeName {
        var hasString = false
        var hasDouble = false
        var hasFloat = false
        var hasLong = false
        var hasInt = false
        var hasBoolean = false

        oneOf.forEach { option ->
            val type = option.get("type")?.asText()
            val format = option.get("format")?.asText()

            when (type) {
                "string" -> hasString = true
                "number" -> {
                    when (format) {
                        "float" -> hasFloat = true
                        else -> hasDouble = true
                    }
                }

                "integer" -> {
                    when (format) {
                        "int64" -> hasLong = true
                        else -> hasInt = true
                    }
                }

                "boolean" -> hasBoolean = true
            }
        }

        // Return the largest type found, or JsonElement if no primitive types found
        return when {
            hasString -> STRING
            hasDouble -> DOUBLE
            hasFloat -> FLOAT
            hasLong -> LONG
            hasInt -> INT
            hasBoolean -> BOOLEAN
            else -> ClassName("kotlinx.serialization.json", "JsonElement").copy(nullable = true)
        }
    }

    private fun generateApiInterface(swagger: JsonNode, outputDir: File) {
        val packageName = "com.hiczp.telegram.bot.api"
        val className = "TelegramBotApi"

        val fileSpec = createFileSpec(packageName, className)

        val interfaceBuilder = TypeSpec.interfaceBuilder(className)

        // Parse paths
        val paths = swagger.get("paths") ?: return
        paths.fields().forEach { (path, methods) ->
            methods.fields().forEach { (method, operation) ->
                try {
                    val function = generateFunction(path, method, operation, outputDir)
                    interfaceBuilder.addFunction(function)
                } catch (e: Exception) {
                    logger.warn("Failed to generate function for $method $path: ${e.message}")
                }
            }
        }

        fileSpec.addType(interfaceBuilder.build())
        fileSpec.build().writeTo(outputDir)

        // Generate extension functions for multipart operations
        if (multipartOperations.isNotEmpty()) {
            generateMultipartExtensions(packageName, className, outputDir)
        }
    }

    private fun generateFunction(path: String, method: String, operation: JsonNode, outputDir: File): FunSpec {
        val operationId = operation.get("operationId")?.asText() ?: generateOperationId(method, path)
        val summary = operation.get("summary")?.asText()

        val functionBuilder = FunSpec.builder(operationId)
            .addModifiers(KModifier.ABSTRACT, KModifier.SUSPEND)

        if (summary != null) {
            functionBuilder.addKdoc(sanitizeKDoc(summary))
        }

        // Add HTTP method annotation
        val httpAnnotation = when (method.uppercase()) {
            "GET" -> AnnotationSpec.builder(ClassName("de.jensklingenberg.ktorfit.http", "GET"))
            "POST" -> AnnotationSpec.builder(ClassName("de.jensklingenberg.ktorfit.http", "POST"))
            "PUT" -> AnnotationSpec.builder(ClassName("de.jensklingenberg.ktorfit.http", "PUT"))
            "DELETE" -> AnnotationSpec.builder(ClassName("de.jensklingenberg.ktorfit.http", "DELETE"))
            "PATCH" -> AnnotationSpec.builder(ClassName("de.jensklingenberg.ktorfit.http", "PATCH"))
            "HEAD" -> AnnotationSpec.builder(ClassName("de.jensklingenberg.ktorfit.http", "HEAD"))
            "OPTIONS" -> AnnotationSpec.builder(ClassName("de.jensklingenberg.ktorfit.http", "OPTIONS"))
            else -> throw IllegalArgumentException("Unsupported HTTP method: $method")
        }
        httpAnnotation.addMember("%S", path)
        functionBuilder.addAnnotation(httpAnnotation.build())

        // Add parameters
        val parameters = operation.get("parameters")
        if (parameters != null && parameters.isArray) {
            parameters.forEach { param ->
                addParameter(functionBuilder, param)
            }
        }

        // Determine return type first
        val returnType = determineReturnType(operation)
        functionBuilder.returns(returnType)

        // Add request body parameter (pass returnType for multipart operations)
        val requestBody = operation.get("requestBody")
        if (requestBody != null) {
            addBodyParameter(functionBuilder, requestBody, operationId, returnType, outputDir)
        }

        return functionBuilder.build()
    }

    private fun addParameter(functionBuilder: FunSpec.Builder, param: JsonNode) {
        val name = param.get("name")?.asText() ?: return
        val paramIn = param.get("in")?.asText() ?: return
        val required = param.get("required")?.asBoolean() ?: false
        val schema = param.get("schema")

        val paramType = determinePropertyType(schema)
        val finalType = if (required) paramType else paramType.copy(nullable = true)
        val camelName = snakeToCamelCase(name)

        val paramBuilder = ParameterSpec.builder(camelName, finalType)

        // Add annotation based on parameter location
        when (paramIn) {
            "query" -> {
                val annotation = AnnotationSpec.builder(ClassName("de.jensklingenberg.ktorfit.http", "Query"))
                    .addMember("%S", name)
                    .build()
                paramBuilder.addAnnotation(annotation)
            }

            "path" -> {
                val annotation = AnnotationSpec.builder(ClassName("de.jensklingenberg.ktorfit.http", "Path"))
                    .addMember("%S", name)
                    .build()
                paramBuilder.addAnnotation(annotation)
            }

            "header" -> {
                val annotation = AnnotationSpec.builder(ClassName("de.jensklingenberg.ktorfit.http", "Header"))
                    .addMember("%S", name)
                    .build()
                paramBuilder.addAnnotation(annotation)
            }
        }

        if (!required) {
            paramBuilder.defaultValue("null")
        }

        functionBuilder.addParameter(paramBuilder.build())
    }

    private fun addBodyParameter(
        functionBuilder: FunSpec.Builder,
        requestBody: JsonNode,
        operationId: String,
        returnType: TypeName,
        outputDir: File
    ) {
        val content = requestBody.get("content") ?: return

        // Try to get schema from either application/json or multipart/form-data
        val jsonContent = content.get("application/json")
        val multipartContent = content.get("multipart/form-data")
        val schema = jsonContent?.get("schema") ?: multipartContent?.get("schema") ?: return
        val isMultipart = jsonContent == null && multipartContent != null

        if (isMultipart) {
            // For multipart requests, generate @Part parameters
            addMultipartParameters(functionBuilder, schema, operationId, returnType, outputDir)
        } else {
            // For JSON requests, use @Body parameter
            addJsonBodyParameter(functionBuilder, schema, operationId, outputDir)
        }
    }

    private fun addJsonBodyParameter(
        functionBuilder: FunSpec.Builder,
        schema: JsonNode,
        operationId: String,
        outputDir: File
    ) {
        // Check if schema is an inline object definition (no $ref)
        val ref = schema.get("\$ref")?.asText()
        val bodyType = if (ref != null) {
            // Use existing schema reference
            determinePropertyType(schema)
        } else if (schema.get("type")?.asText() == "object") {
            // Generate a request body class for inline object schema
            val className = generateRequestBodyClass(operationId, schema, outputDir)
            ClassName("com.hiczp.telegram.bot.api.model", className)
        } else {
            // Fallback to determinePropertyType for other cases
            determinePropertyType(schema)
        }

        val paramBuilder = ParameterSpec.builder("body", bodyType)
        val annotation = AnnotationSpec.builder(ClassName("de.jensklingenberg.ktorfit.http", "Body"))
            .build()
        paramBuilder.addAnnotation(annotation)
        functionBuilder.addParameter(paramBuilder.build())
    }

    private fun addMultipartParameters(
        functionBuilder: FunSpec.Builder,
        schema: JsonNode,
        operationId: String,
        returnType: TypeName,
        outputDir: File
    ) {
        // Add @Multipart annotation to the function
        val multipartAnnotation = AnnotationSpec.builder(ClassName("de.jensklingenberg.ktorfit.http", "Multipart"))
            .build()
        functionBuilder.addAnnotation(multipartAnnotation)

        // Generate request body class for multipart operations
        val requestClassName = generateRequestBodyClass(operationId, schema, outputDir)

        // Store schema and return type for later extension function generation
        multipartOperations[operationId] = MultipartOperationInfo(schema, returnType)

        val properties = schema.get("properties") ?: return
        val required = schema.get("required")?.map { it.asText() }?.toSet() ?: emptySet()

        properties.fields().forEach { (propName, propSchema) ->
            val camelName = snakeToCamelCase(propName)
            val isRequired = required.contains(propName)

            // Determine the property type (will handle ReplyMarkup oneOf correctly)
            val propType = determinePropertyType(propSchema)
            val finalType = if (isRequired) propType else propType.copy(nullable = true)

            addPartParameter(functionBuilder, camelName, propName, finalType, isRequired)
        }
    }

    private fun addPartParameter(
        functionBuilder: FunSpec.Builder,
        camelName: String,
        originalName: String,
        paramType: TypeName,
        isRequired: Boolean
    ) {
        val paramBuilder = ParameterSpec.builder(camelName, paramType)

        // Add @Part annotation
        val partAnnotation = AnnotationSpec.builder(ClassName("de.jensklingenberg.ktorfit.http", "Part"))
            .addMember("%S", originalName)
            .build()
        paramBuilder.addAnnotation(partAnnotation)

        if (!isRequired) {
            paramBuilder.defaultValue("null")
        }

        functionBuilder.addParameter(paramBuilder.build())
    }

    private fun generateRequestBodyClass(operationId: String, schema: JsonNode, outputDir: File): String {
        // Check if we already generated this request body
        generatedRequestBodies[operationId]?.let { return it }

        // Generate class name from operation ID (e.g., sendMessage -> SendMessageRequest)
        val className = operationId.replaceFirstChar { it.uppercase() } + "Request"

        // Generate the request body class
        val packageName = "com.hiczp.telegram.bot.api.model"
        generateRegularClass(packageName, className, schema, outputDir)

        // Mark as processed
        processedSchemas.add(className)
        generatedRequestBodies[operationId] = className

        return className
    }

    private fun determineReturnType(operation: JsonNode): TypeName {
        val responses = operation.get("responses") ?: return ClassName(
            "com.hiczp.telegram.bot.api.type",
            "TelegramResponse"
        ).parameterizedBy(BOOLEAN)
        val successResponse = responses.get("200") ?: responses.get("201") ?: return ClassName(
            "com.hiczp.telegram.bot.api.type",
            "TelegramResponse"
        ).parameterizedBy(BOOLEAN)
        val content = successResponse.get("content") ?: return ClassName(
            "com.hiczp.telegram.bot.api.type",
            "TelegramResponse"
        ).parameterizedBy(BOOLEAN)
        val jsonContent = content.get("application/json") ?: return ClassName(
            "com.hiczp.telegram.bot.api.type",
            "TelegramResponse"
        ).parameterizedBy(BOOLEAN)
        val schema = jsonContent.get("schema") ?: return ClassName(
            "com.hiczp.telegram.bot.api.type",
            "TelegramResponse"
        ).parameterizedBy(BOOLEAN)

        // Check if this is a Telegram API response wrapper (has "ok" and "result" properties)
        val properties = schema.get("properties")
        if (properties != null && properties.has("ok") && properties.has("result")) {
            // Extract the result type
            val resultProperty = properties.get("result")
            val resultType = determinePropertyType(resultProperty)
            return ClassName("com.hiczp.telegram.bot.api.type", "TelegramResponse").parameterizedBy(resultType)
        }

        // Fallback to the schema type directly wrapped in TelegramResponse
        val schemaType = determinePropertyType(schema)
        return ClassName("com.hiczp.telegram.bot.api.type", "TelegramResponse").parameterizedBy(schemaType)
    }

    private fun generateOperationId(method: String, path: String): String {
        val parts = path.split("/").filter { it.isNotEmpty() && !it.startsWith("{") }
        val pathPart = parts.joinToString("") { it.capitalize() }
        return method.lowercase() + pathPart.ifEmpty { "Request" }
    }

    private fun String.capitalize(): String {
        return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }

    /**
     * Create a FileSpec builder with standard configuration (4-space indent and auto-generated comments)
     */
    private fun createFileSpec(
        packageName: String,
        fileName: String,
        vararg additionalComments: String
    ): FileSpec.Builder {
        return FileSpec.builder(packageName, fileName)
            .indent("    ")
            .addFileComment("Auto-generated from Swagger specification")
            .addFileComment("Do not modify this file manually")
            .apply {
                additionalComments.forEach { addFileComment(it) }
            }
    }

    private fun snakeToCamelCase(snakeCase: String): String {
        if (!snakeCase.contains('_')) return snakeCase

        return snakeCase.split('_')
            .mapIndexed { index, part ->
                if (index == 0) part else part.capitalize()
            }
            .joinToString("")
    }

    /**
     * Sanitize KDoc content to avoid KotlinPoet parsing errors.
     * Removes or escapes problematic characters.
     */
    private fun sanitizeKDoc(kdoc: String): String {
        return kdoc
            // Remove special Unicode characters that cause issues
            .replace("»", "")
            .replace("«", "")
            // Escape other problematic characters if needed
            .replace("\\", "\\\\")
            // Remove excessive whitespace
            .trim()
    }

    /**
     * Generate extension functions for multipart operations that accept Request data classes
     */
    private fun generateMultipartExtensions(packageName: String, interfaceName: String, outputDir: File) {
        val fileSpec = createFileSpec(
            packageName,
            "${interfaceName}Extensions",
            "Extension functions for multipart operations"
        )

        multipartOperations.forEach { (operationId, info) ->
            val requestClassName = generatedRequestBodies[operationId]
            if (requestClassName != null) {
                val extensionFunction = generateMultipartExtensionFunction(
                    interfaceName,
                    operationId,
                    requestClassName,
                    info.schema,
                    info.returnType
                )
                fileSpec.addFunction(extensionFunction)
            }
        }

        fileSpec.build().writeTo(outputDir)
    }

    private fun generateMultipartExtensionFunction(
        interfaceName: String,
        operationId: String,
        requestClassName: String,
        schema: JsonNode,
        returnType: TypeName
    ): FunSpec {
        val properties = schema.get("properties")

        val functionBuilder = FunSpec.builder(operationId)
            .addModifiers(KModifier.SUSPEND)
            .receiver(ClassName("com.hiczp.telegram.bot.api", interfaceName))
            .addParameter("request", ClassName("com.hiczp.telegram.bot.api.model", requestClassName))
            .returns(returnType)

        // Build the function call
        val codeBuilder = CodeBlock.builder()
        codeBuilder.add("return %N(\n", operationId)
        codeBuilder.indent()

        val paramsList = mutableListOf<String>()
        properties?.fields()?.forEach { (propName, _) ->
            val camelName = snakeToCamelCase(propName)
            paramsList.add("$camelName = request.$camelName")
        }

        paramsList.forEachIndexed { index, param ->
            codeBuilder.add(param)
            if (index < paramsList.size - 1) {
                codeBuilder.add(",\n")
            } else {
                codeBuilder.add("\n")
            }
        }

        codeBuilder.unindent()
        codeBuilder.add(")")

        functionBuilder.addCode(codeBuilder.build())

        return functionBuilder.build()
    }
}
