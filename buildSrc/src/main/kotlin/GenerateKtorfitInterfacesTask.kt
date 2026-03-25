import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.*
import java.io.File

/**
 * Generates Kotlin code from a Telegram Bot API OpenAPI/Swagger specification.
 *
 * This cacheable Gradle task produces type-safe bindings for Telegram's REST API:
 * - **Data models** (`model/`): Serializable data classes with union type support
 * - **Form classes** (`form/`): Multipart form wrappers for file uploads
 * - **Query extensions** (`query/`): Typed helpers for JSON-serialized GET parameters
 * - **JSON body extensions** (`model/Bodies.kt`): Scatter parameter extensions for POST operations
 * - **Ktorfit interface** (`TelegramBotApi.kt`): HTTP interface with all API endpoints
 * - **Union types** (`union/`): Generic sealed classes for response unions (e.g., `Union<Message, Boolean>`)
 *
 * ## Configuration
 * - `swaggerFile`: Path to the OpenAPI/Swagger JSON specification
 * - `outputDir`: Output directory (defaults to `src/commonMain/kotlin`)
 *
 * ## Special Type Handling
 * - **ReplyMarkup**: Collected types implement a sealed interface with `@JsonClassDiscriminator("type")`
 * - **IncomingUpdate**: Types in `Update`'s optional fields implement `IncomingUpdate` interface
 * - **MaybeInaccessibleMessage**: Uses `Message` type for deserialization (field-overlapping union)
 * - **Discriminator extraction**: Multiple strategies (explicit mapping, enum fields, description patterns, name inference)
 */
@CacheableTask
abstract class GenerateKtorfitInterfacesTask : DefaultTask() {
    companion object {
        // Package names
        private const val BASE_PACKAGE = "com.hiczp.telegram.bot.protocol"
        private const val MODEL_PACKAGE = "$BASE_PACKAGE.model"
        private const val FORM_PACKAGE = "$BASE_PACKAGE.form"
        private const val QUERY_PACKAGE = "$BASE_PACKAGE.query"
        private const val TYPE_PACKAGE = "$BASE_PACKAGE.type"
        private const val ANNOTATION_PACKAGE = "$BASE_PACKAGE.annotation"
        private const val PLUGIN_PACKAGE = "$BASE_PACKAGE.plugin"
        private const val UNION_PACKAGE = "$BASE_PACKAGE.union"
        private const val KTORFIT_HTTP_PACKAGE = "de.jensklingenberg.ktorfit.http"
        private const val KTOR_STATEMENT_PACKAGE = "io.ktor.client.statement"

        // Type names
        private const val TELEGRAM_RESPONSE_TYPE = "TelegramResponse"
        private const val REPLY_MARKUP_INTERFACE = "ReplyMarkup"
        private const val INCOMING_UPDATE_INTERFACE = "IncomingUpdate"
        private const val INCOMING_UPDATE_CONTAINER_ANNOTATION = "IncomingUpdateContainer"
        private const val UPDATE_TYPE = "Update"
        private const val INPUT_FILE_TYPE = "InputFile"
        private const val MESSAGE_TYPE = "Message"
        private const val MAYBE_INACCESSIBLE_MESSAGE_TYPE = "MaybeInaccessibleMessage"
        private const val TELEGRAM_FILE_DOWNLOAD_ANNOTATION = "TelegramFileDownload"
        private const val TELEGRAM_BOT_API_VERSION_ANNOTATION = "TelegramBotApiVersion"
        private const val HTTP_STATEMENT_TYPE = "HttpStatement"

        // Ktorfit HTTP annotation names
        private const val GET_ANNOTATION = "GET"
        private const val STREAMING_ANNOTATION = "Streaming"
        private const val PATH_ANNOTATION = "Path"

        // Content types
        private const val CONTENT_TYPE_JSON = "application/json"
        private const val CONTENT_TYPE_MULTIPART = "multipart/form-data"

        // Cached TypeName constants to avoid repeated ClassName creation
        private val JSON_ELEMENT_TYPE = ClassName("kotlinx.serialization.json", "JsonElement")
        private val CHANNEL_PROVIDER_TYPE = ClassName("io.ktor.client.request.forms", "ChannelProvider")
        private val FORM_PART_TYPE = ClassName("io.ktor.client.request.forms", "FormPart")
        private val MULTIPART_FORM_DATA_CONTENT_TYPE =
            ClassName("io.ktor.client.request.forms", "MultiPartFormDataContent")
        private val FORM_DATA_BUILDER_TYPE = ClassName("io.ktor.client.request.forms", "formData")
    }

    /** OpenAPI/Swagger specification file defining the Telegram Bot API structure. */
    @get:InputFile
    @get:PathSensitive(PathSensitivity.ABSOLUTE)
    abstract val swaggerFile: RegularFileProperty

    /**
     * Output directory for generated code.
     *
     * Structure:
     * - `model/` - Data classes and `Bodies.kt`
     * - `form/` - Form wrappers and `Forms.kt`
     * - `query/Queries.kt` - Query extensions
     * - `TelegramBotApi.kt` - Ktorfit interface
     * - `type/` - Preserved (handwritten `InputFile`, `TelegramResponse`)
     */
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

    /** Cache for snake_case to camelCase conversions to avoid repeated string operations */
    private val snakeToCamelCaseCache = mutableMapOf<String, String>()

    /** Information for multipart form operations. */
    private data class MultipartOperationInfo(
        val schema: JsonNode,
        val returnType: TypeName,
        val description: String?
    )

    /** Information for JSON body operations (scatter extension generation). */
    private data class JsonBodyOperationInfo(
        val requestClassName: String,
        val requestSchema: JsonNode,
        val returnType: TypeName,
        val description: String?
    )

    private val multipartOperations = mutableMapOf<String, MultipartOperationInfo>() // operationId -> info
    private val queryStringOperations = mutableMapOf<String, JsonNode>() // operationId -> operation
    private val jsonBodyOperations = mutableMapOf<String, JsonBodyOperationInfo>() // operationId -> info
    private val replyMarkupTypes = mutableSetOf<String>() // Collected ReplyMarkup types
    private val updateFieldTypes = mutableSetOf<String>() // Types used in Update's optional non-primitive fields

    /**
     * Represents a member of a response union type.
     * Can be either a reference to a schema type or a primitive type (boolean, string, number, integer).
     */
    private data class ResponseUnionMember(
        val refTypeName: String?, // Schema reference name (e.g., "Message"), null for primitives
        val primitiveType: String? // Primitive type name (e.g., "boolean", "string"), null for refs
    ) {
        /**
         * Returns the Kotlin type name for this member.
         * For refs: the schema name (e.g., "Message")
         * For primitives: the Kotlin type name (e.g., "Boolean", "String")
         */
        fun kotlinTypeName(): String = when {
            refTypeName != null -> refTypeName
            primitiveType != null -> when (primitiveType) {
                "boolean" -> "Boolean"
                "string" -> "String"
                "number" -> "Double"
                "integer" -> "Long"
                else -> primitiveType.replaceFirstChar { it.uppercase() }
            }

            else -> "Any"
        }

        /**
         * Returns true if this is a primitive type member.
         */
        fun isPrimitive(): Boolean = primitiveType != null
    }

    /**
     * Information about a response union type collected from API response oneOf.
     * Uses generic Union<T1, T2>, Union3<T1, T2, T3>, etc. structures.
     *
     * @property members The list of possible types in this union
     * @property operationIds The list of operation IDs that use this union type
     */
    private data class ResponseUnionInfo(
        val members: List<ResponseUnionMember>,
        val operationIds: MutableList<String> = mutableListOf()
    )

    // Set of unique member type combinations (by size) for generating Union classes
    private val responseUnionSizes = mutableSetOf<Int>()

    /**
     * Main entry point for code generation.
     *
     * Orchestrates the entire process:
     * 1. Parse OpenAPI specification
     * 2. Clean output directories (preserve `type/`)
     * 3. Collect special types (ReplyMarkup, IncomingUpdate, response unions)
     * 4. Generate Union classes, ReplyMarkup interface, data models, API interface
     * 5. Generate query and form extensions
     *
     * @throws Exception if parsing or generation fails
     */
    @TaskAction
    fun generate() {
        val swagger = ObjectMapper().readTree(swaggerFile.get().asFile)
        val outputDirectory = outputDir.get().asFile

        logger.lifecycle("Generating Ktorfit interfaces from ${swaggerFile.get().asFile.name}")

        // Clean output directory (except the type directory which contains handwritten TelegramResponse)
        val apiDir = File(outputDirectory, "com/hiczp/telegram/bot/protocol")
        val modelDir = File(apiDir, "model")
        val formDir = File(apiDir, "form")
        val queryDir = File(apiDir, "query")
        val unionDir = File(apiDir, "union")
        val polymorphicDir = File(apiDir, "polymorphic")

        // Delete model, form, query, union and polymorphic directories, but keep the type directory
        modelDir.deleteRecursively()
        formDir.deleteRecursively()
        queryDir.deleteRecursively()
        unionDir.deleteRecursively()
        polymorphicDir.deleteRecursively()

        // Delete the API interface file
        File(apiDir, "TelegramBotApi.kt").delete()

        // Create directories
        modelDir.mkdirs()
        formDir.mkdirs()
        queryDir.mkdirs()

        // Store all schemas for reference
        allSchemas = swagger.get("components")?.get("schemas")?.properties()
            ?.associate { it.key to it.value } ?: emptyMap()

        // Collect all ReplyMarkup types from the swagger spec
        collectReplyMarkupTypes(swagger)
        logger.lifecycle("Detected ReplyMarkup types: $replyMarkupTypes")

        // Collect all types used in Update's optional non-primitive fields
        collectUpdateFieldTypes()
        logger.lifecycle("Detected Update field types: $updateFieldTypes")

        // Collect all response union types from API paths
        collectResponseUnionTypes(swagger)
        logger.lifecycle("Detected response union sizes: $responseUnionSizes, operations: ${responseUnionOperations.keys}")

        // Generate Union generic classes as needed
        if (responseUnionSizes.isNotEmpty()) {
            generateUnionClasses(outputDirectory, responseUnionSizes)
        }

        // Generate ReplyMarkup sealed interface if we found any types
        if (replyMarkupTypes.isNotEmpty()) {
            generateReplyMarkupInterface(outputDirectory)
        }

        // Generate models
        generateModels(swagger, outputDirectory)

        // Generate API interface
        generateApiInterface(swagger, outputDirectory)

        // Query extension functions are generated inside generateApiInterface()

        logger.lifecycle("Successfully generated Ktorfit interfaces to ${outputDirectory.absolutePath}")
    }


    /** Collects types appearing in `reply_markup` fields from request bodies and schemas. */
    private fun collectReplyMarkupTypes(swagger: JsonNode) {
        val paths = swagger.get("paths") ?: return

        paths.properties().forEach { (_, methods) ->
            methods.properties().forEach { (_, operation) ->
                // Check in the request body
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

    /**
     * Collects all types used in `Update`'s optional non-primitive fields.
     *
     * This method:
     * 1. Locates the `Update` schema in the specification
     * 2. Iterates through all properties, skipping required fields (like `update_id`)
     * 3. For each optional field, extracts the type name from direct $ref or allOf arrays
     * 4. Excludes `MaybeInaccessibleMessage` as it's handled as a field-overlapping union
     *
     * The collected types are stored in `updateFieldTypes` and will implement the `IncomingUpdate` interface
     * for polymorphic handling when deserializing Update objects.
     */
    private fun collectUpdateFieldTypes() {
        val updateSchema = allSchemas[UPDATE_TYPE] ?: return
        val properties = updateSchema.get("properties") ?: return
        val required = updateSchema.get("required")?.map { it.asText() }?.toSet() ?: emptySet()

        properties.properties().forEach { (propName, propSchema) ->
            // Skip required fields (like update_id)
            if (propName in required) return@forEach

            // Extract type from the property schema
            val typeInfo = extractSchemaTypeInfo(propSchema)

            // Handle direct $ref
            if (typeInfo.ref != null) {
                val typeName = typeInfo.ref.substringAfterLast("/")
                // Skip primitive types and special cases
                if (typeName != MAYBE_INACCESSIBLE_MESSAGE_TYPE) {
                    updateFieldTypes.add(typeName)
                }
            }

            // Handle allOf
            if (typeInfo.allOf != null && typeInfo.allOf.isArray) {
                extractTypeNamesFromRefArray(typeInfo.allOf).forEach { typeName ->
                    if (typeName != MAYBE_INACCESSIBLE_MESSAGE_TYPE) {
                        updateFieldTypes.add(typeName)
                    }
                }
            }
        }
    }

    /**
     * Collects all response union types from API paths.
     *
     * This method scans all API paths for response schemas that have a `result` property
     * with a `oneOf` containing mixed types (schema references and primitives).
     *
     * For each unique combination, we store the member count to generate the appropriate
     * Union<T1, T2>, Union3<T1, T2, T3>, etc. classes as needed.
     *
     * The collected information is stored in `responseUnionSizes` and used to:
     * 1. Generate generic Union classes in the `union` package
     * 2. Generate polymorphic serializers in the `union` package
     * 3. Update API method return types to use Union generics
     */
    private fun collectResponseUnionTypes(swagger: JsonNode) {
        val paths = swagger.get("paths") ?: return

        paths.properties().forEach { (_, methods) ->
            methods.properties().forEach { (_, operation) ->
                val operationId = operation.get("operationId")?.asText() ?: return@forEach

                // Get the 200 response
                val response200 = operation.get("responses")?.get("200") ?: return@forEach
                val content = response200.get("content")?.get(CONTENT_TYPE_JSON) ?: return@forEach
                val schema = content.get("schema") ?: return@forEach

                // Check if the result property has oneOf
                val resultProp = schema.get("properties")?.get("result") ?: return@forEach
                val oneOf = resultProp.get("oneOf") ?: return@forEach

                if (!oneOf.isArray || oneOf.isEmpty) return@forEach

                // Extract members from oneOf
                val members = mutableListOf<ResponseUnionMember>()
                oneOf.forEach { option ->
                    val ref = option.get($$"$ref")?.asText()
                    val primitiveType = option.get("type")?.asText()

                    if (ref != null) {
                        val typeName = ref.substringAfterLast("/")
                        members.add(ResponseUnionMember(refTypeName = typeName, primitiveType = null))
                    } else if (primitiveType != null && primitiveType in listOf(
                            "boolean",
                            "string",
                            "number",
                            "integer"
                        )
                    ) {
                        members.add(ResponseUnionMember(refTypeName = null, primitiveType = primitiveType))
                    }
                }

                // Only create union types for mixed ref + primitive combinations
                val hasRefs = members.any { it.refTypeName != null }
                val hasPrimitives = members.any { it.isPrimitive() }

                if (members.size >= 2 && hasRefs && hasPrimitives) {
                    // Store the size for generating OneOf classes
                    responseUnionSizes.add(members.size)

                    // Store the union info for this operation
                    val info = responseUnionOperations.getOrPut(operationId) {
                        ResponseUnionInfo(members = members)
                    }
                    info.operationIds.add(operationId)
                }
            }
        }
    }

    // Map of operationId -> ResponseUnionInfo for response unions
    private val responseUnionOperations = mutableMapOf<String, ResponseUnionInfo>()

    private fun collectReplyMarkupFromRequestBody(requestBody: JsonNode) {
        val content = requestBody.get("content") ?: return

        // Check both JSON and multipart content types
        listOf(CONTENT_TYPE_JSON, CONTENT_TYPE_MULTIPART).forEach { contentType ->
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

    /**
     * Extracts type names from a oneOf or allOf array by extracting $ref values.
     */
    private fun extractTypeNamesFromRefArray(refArray: JsonNode?): List<String> {
        if (refArray == null || !refArray.isArray) return emptyList()

        return refArray.mapNotNull { option ->
            option.get($$"$ref")?.asText()?.substringAfterLast("/")
        }
    }

    private fun collectReplyMarkupFromField(field: JsonNode) {
        // Check for oneOf
        val oneOf = field.get("oneOf")
        extractTypeNamesFromRefArray(oneOf).forEach { typeName ->
            replyMarkupTypes.add(typeName)
        }

        // Check for allOf (single type reference)
        val allOf = field.get("allOf")
        extractTypeNamesFromRefArray(allOf).forEach { typeName ->
            replyMarkupTypes.add(typeName)
        }
    }

    /**
     * Adds @Serializable annotation to a type builder unless it's a Form class.
     *
     * Form classes don't get @Serializable because they may include non-serializable multipart helpers
     * (for example `FormPart<ChannelProvider>` in `attachments`) and are used as request builders.
     */
    private fun TypeSpec.Builder.addSerializableAnnotationIfNeeded(isFormClass: Boolean): TypeSpec.Builder {
        if (!isFormClass) {
            addAnnotation(
                AnnotationSpec.builder(ClassName("kotlinx.serialization", "Serializable"))
                    .build()
            )
        }
        return this
    }

    /**
     * Adds the `ReplyMarkup` superinterface if the class is a ReplyMarkup type.
     *
     * Types are identified as ReplyMarkup types by the `collectReplyMarkupTypes()` method.
     */
    private fun TypeSpec.Builder.addReplyMarkupInterfaceIfNeeded(className: String): TypeSpec.Builder {
        if (isReplyMarkupType(className)) {
            addSuperinterface(ClassName(MODEL_PACKAGE, REPLY_MARKUP_INTERFACE))
        }
        return this
    }

    /**
     * Adds the `IncomingUpdate` superinterface if the class is used in `Update`'s optional fields.
     *
     * Types are identified as IncomingUpdate types by the `collectUpdateFieldTypes()` method.
     */
    private fun TypeSpec.Builder.addIncomingUpdateInterfaceIfNeeded(className: String): TypeSpec.Builder {
        if (isUpdateFieldType(className)) {
            addSuperinterface(ClassName(ANNOTATION_PACKAGE, INCOMING_UPDATE_INTERFACE))
        }
        return this
    }

    /**
     * Adds the `IncomingUpdateContainer` annotation for the generated `Update` type.
     */
    private fun TypeSpec.Builder.addIncomingUpdateContainerAnnotationIfNeeded(className: String): TypeSpec.Builder {
        if (className == UPDATE_TYPE) {
            addAnnotation(
                AnnotationSpec.builder(ClassName(ANNOTATION_PACKAGE, INCOMING_UPDATE_CONTAINER_ANNOTATION))
                    .build()
            )
        }
        return this
    }

    /**
     * Adds KDoc documentation to a type builder if the description is present.
     *
     * The description is sanitized to ensure a valid KDoc format (escaping special characters, etc.).
     */
    private fun TypeSpec.Builder.addKDocIfPresent(description: String?): TypeSpec.Builder {
        if (description != null) {
            addKdoc(sanitizeKDoc(description))
        }
        return this
    }

    /**
     * Configures a type builder with common annotations and documentation.
     *
     * This is a convenience method that applies:
     * - @Serializable annotation (unless it's a Form class)
     * - ReplyMarkup interface inheritance (if applicable)
     * - IncomingUpdate interface inheritance (if applicable)
     * - IncomingUpdateContainer annotation for `Update` (if applicable)
     * - KDoc documentation (if description is present)
     */
    private fun TypeSpec.Builder.configureTypeBuilder(
        className: String,
        description: String?,
        isFormClass: Boolean
    ): TypeSpec.Builder {
        return this
            .addSerializableAnnotationIfNeeded(isFormClass)
            .addReplyMarkupInterfaceIfNeeded(className)
            .addIncomingUpdateInterfaceIfNeeded(className)
            .addIncomingUpdateContainerAnnotationIfNeeded(className)
            .addKDocIfPresent(description)
    }

    /**
     * Data class to hold extracted schema type information from an OpenAPI schema node.
     */
    private data class SchemaTypeInfo(
        val type: String?,
        val format: String?,
        val ref: String?,
        val oneOf: JsonNode?,
        val allOf: JsonNode?
    )

    /**
     * Data class to hold common class generation information extracted from an object schema.
     */
    private data class ClassGenerationInfo(
        val description: String?,
        val properties: JsonNode,
        val required: Set<String>
    )

    /**
     * Extracts common class generation information from an object schema.
     * Returns null if the schema doesn't have properties.
     */
    private fun extractClassGenerationInfo(schema: JsonNode): ClassGenerationInfo? {
        val properties = schema.get("properties") ?: return null
        return ClassGenerationInfo(
            description = schema.get("description")?.asText(),
            properties = properties,
            required = schema.get("required")?.map { it.asText() }?.toSet() ?: emptySet()
        )
    }

    /**
     * Extracts common type information from a schema node into a structured format.
     */
    private fun extractSchemaTypeInfo(schema: JsonNode?): SchemaTypeInfo {
        if (schema == null) {
            return SchemaTypeInfo(null, null, null, null, null)
        }
        return SchemaTypeInfo(
            type = schema.get("type")?.asText(),
            format = schema.get("format")?.asText(),
            ref = schema.get($$"$ref")?.asText(),
            oneOf = schema.get("oneOf"),
            allOf = schema.get("allOf")
        )
    }

    /**
     * Checks if a schema represents an InputFile type or a union containing InputFile.
     *
     * Checks for direct $ref to InputFile, oneOf array containing InputFile, or allOf array containing InputFile.
     */
    private fun isInputFileType(ref: String?, oneOf: JsonNode?, allOf: JsonNode? = null): Boolean {
        val isInputFile = ref?.substringAfterLast("/") == INPUT_FILE_TYPE
        val canBeInputFile = oneOf != null && oneOf.any {
            it.get($$"$ref")?.asText()?.substringAfterLast("/") == INPUT_FILE_TYPE
        }
        val isInputFileViaAllOf = allOf != null && allOf.any {
            it.get($$"$ref")?.asText()?.substringAfterLast("/") == INPUT_FILE_TYPE
        }
        return isInputFile || canBeInputFile || isInputFileViaAllOf
    }

    /**
     * Determines the appropriate Kotlin type name for a oneOf union schema.
     *
     * Strategy:
     * 1. Check if this is a ReplyMarkup union → return ReplyMarkup interface
     * 2. Try to find a parent type in allSchemas that has a oneOf containing these refs
     * 3. Fallback to determining the largest primitive type for basic type unions
     */
    private fun determineOneOfType(oneOf: JsonNode, context: String? = null): TypeName {
        val refs = oneOf.mapNotNull { it.get($$"$ref")?.asText()?.substringAfterLast("/") }

        // Check if this is a ReplyMarkup oneOf
        if (isReplyMarkupOneOf(refs)) {
            return ClassName(MODEL_PACKAGE, REPLY_MARKUP_INTERFACE)
        }

        // Check if this matches a response union type (mixed ref + primitive)
        val responseUnionType = findResponseUnionType(oneOf)
        if (responseUnionType != null) {
            return responseUnionType
        }

        // Try to find a common parent type in allSchemas
        // Look for a schema that has a oneOf containing exactly these refs
        if (refs.isNotEmpty()) {
            val parentType = findParentTypeForOneOf(refs)
            if (parentType != null) {
                return ClassName(MODEL_PACKAGE, parentType)
            }
        }

        // Handle oneOf for basic types - choose the "largest" type
        return determineLargestPrimitiveType(oneOf, context)
    }

    /**
     * Finds a matching response union type for a oneOf schema.
     * Returns the parameterized Union type (e.g., Union<Message, Boolean>) if the oneOf matches a collected response union, null otherwise.
     */
    private fun findResponseUnionType(oneOf: JsonNode): TypeName? {
        // Extract members from this oneOf
        val members = mutableListOf<ResponseUnionMember>()
        oneOf.forEach { option ->
            val ref = option.get($$"$ref")?.asText()
            val primitiveType = option.get("type")?.asText()

            if (ref != null) {
                val typeName = ref.substringAfterLast("/")
                members.add(ResponseUnionMember(refTypeName = typeName, primitiveType = null))
            } else if (primitiveType != null && primitiveType in listOf("boolean", "string", "number", "integer")) {
                members.add(ResponseUnionMember(refTypeName = null, primitiveType = primitiveType))
            }
        }

        // Only match if this is a mixed ref + primitive combination
        val hasRefs = members.any { it.refTypeName != null }
        val hasPrimitives = members.any { it.isPrimitive() }

        if (members.size >= 2 && hasRefs && hasPrimitives) {
            // Build the parameterized Union type
            val unionClassName = if (members.size == 2) "Union" else "Union${members.size}"

            // Map members to Kotlin types
            val typeArgs = members.map { member ->
                when {
                    member.refTypeName != null -> ClassName(MODEL_PACKAGE, member.refTypeName)
                    member.primitiveType != null -> when (member.primitiveType) {
                        "boolean" -> BOOLEAN
                        "string" -> STRING
                        "number" -> DOUBLE
                        "integer" -> LONG
                        else -> STAR
                    }

                    else -> STAR
                }
            }

            return ClassName(UNION_PACKAGE, unionClassName).parameterizedBy(typeArgs)
        }

        return null
    }

    /**
     * Finds a parent type in allSchemas that has a oneOf containing all the given refs.
     * Skips MaybeInaccessibleMessage since it's specially handled and not generated.
     */
    private fun findParentTypeForOneOf(refs: List<String>): String? {
        val refsSet = refs.toSet()

        for ((schemaName, schema) in allSchemas) {
            // Skip MaybeInaccessibleMessage - it's specially handled and shouldn't be used as a parent type
            if (schemaName == MAYBE_INACCESSIBLE_MESSAGE_TYPE) continue

            val schemaOneOf = schema.get("oneOf")
            if (schemaOneOf != null && schemaOneOf.isArray) {
                val schemaRefs = extractTypeNamesFromRefArray(schemaOneOf).toSet()
                // Check if the refs are a subset of the schema's oneOf refs
                if (refsSet.isNotEmpty() && refsSet.all { it in schemaRefs }) {
                    return schemaName
                }
            }
        }
        return null
    }

    /**
     * Creates a property builder with snake_case to camelCase conversion.
     *
     * Adds @SerialName annotation if the camelCase name differs from the original snake_case name.
     * Also adds KDoc documentation if a description is provided.
     */
    private fun createPropertyBuilder(
        propName: String,
        propType: TypeName,
        propDescription: String?
    ): PropertySpec.Builder {
        val camelCaseName = snakeToCamelCase(propName)
        val propertyBuilder = PropertySpec.builder(camelCaseName, propType)
            .initializer(camelCaseName)

        // Add @SerialName if the property name differs from snake_case
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

        return propertyBuilder
    }

    /**
     * Creates a constructor parameter builder with an optional default value.
     *
     * Non-required parameters get a default value of `null`.
     */
    private fun createParameterBuilder(
        propName: String,
        propType: TypeName,
        isRequired: Boolean
    ): ParameterSpec.Builder {
        val camelCaseName = snakeToCamelCase(propName)
        val paramBuilder = ParameterSpec.builder(camelCaseName, propType)
        if (!isRequired) {
            paramBuilder.defaultValue("null")
        }
        return paramBuilder
    }

    /**
     * Creates and adds a property with its constructor parameter to a data class.
     *
     * This overload determines the property type from the schema.
     */
    private fun addPropertyWithParameter(
        classBuilder: TypeSpec.Builder,
        constructorBuilder: FunSpec.Builder,
        propName: String,
        propSchema: JsonNode,
        isRequired: Boolean,
        className: String? = null
    ) {
        val context = className?.let { "$it.$propName" }
        val propType = determinePropertyType(propSchema, context = context)
        val finalType = if (isRequired) propType else propType.copy(nullable = true)
        val propDescription = propSchema.get("description")?.asText()

        val propertyBuilder = createPropertyBuilder(propName, finalType, propDescription)
        val paramBuilder = createParameterBuilder(propName, finalType, isRequired)

        constructorBuilder.addParameter(paramBuilder.build())
        classBuilder.addProperty(propertyBuilder.build())
    }

    /**
     * Creates and adds a property with its constructor parameter to a data class.
     *
     * This overload accepts an already-determined property type.
     */
    private fun addPropertyWithParameter(
        classBuilder: TypeSpec.Builder,
        constructorBuilder: FunSpec.Builder,
        propName: String,
        propType: TypeName,
        propDescription: String?,
        isRequired: Boolean
    ) {
        val finalType = if (isRequired) propType else propType.copy(nullable = true)

        val propertyBuilder = createPropertyBuilder(propName, finalType, propDescription)
        val paramBuilder = createParameterBuilder(propName, finalType, isRequired)

        constructorBuilder.addParameter(paramBuilder.build())
        classBuilder.addProperty(propertyBuilder.build())
    }

    /**
     * Generates generic Union sealed classes and their serializers for response union types.
     *
     * For each unique size in the set (e.g., 2, 3), generates:
     * - `Union<A, B>` or `Union3<A, B, C>` sealed class with First/Second/Third subclasses
     * - `UnionSerializer<A, B>` or `Union3Serializer<A, B, C>` custom serializer
     * - `UnionSerializersModule` extension property for easy Json configuration
     *
     * The serializer tries to deserialize in order: first type, then second type, etc.
     * If none match, it throws a SerializationException.
     */
    private fun generateUnionClasses(outputDir: File, sizes: Set<Int>) {
        // Create the union directory to ensure it exists
        val unionDir = File(outputDir, "com/hiczp/telegram/bot/protocol/union")
        unionDir.mkdirs()

        for (size in sizes.sorted()) {
            // Pass outputDir (root src directory), not unionDir, because writeTo creates package subdirs
            generateUnionClass(outputDir, size)
            generateUnionSerializer(outputDir, size)
        }

        // Generate the SerializersModule extension for all Union types
        generateUnionSerializersModule(outputDir)
    }

    /**
     * Generates a generic Union sealed class for the given size.
     *
     * For size 2, generates:
     * ```kotlin
     * @Serializable(with = UnionSerializer::class)
     * sealed class Union<out A, out B> {
     *     data class First<out A>(val value: A) : Union<A, Nothing>()
     *     data class Second<out B>(val value: B) : Union<Nothing, B>()
     * }
     * ```
     */
    private fun generateUnionClass(unionDir: File, size: Int) {
        val className = if (size == 2) "Union" else "Union$size"
        val serializerClassName = if (size == 2) "UnionSerializer" else "Union${size}Serializer"

        // Generate type parameter names: A, B, C, ...
        val typeParamNames = (0 until size).map { idx ->
            ('A' + idx).toString()
        }

        val fileSpec = FileSpec.builder(UNION_PACKAGE, className)
            .addFileComment("Auto-generated from Swagger specification, do not modify this file manually")
            .addImport("kotlinx.serialization", "Serializable")
            .indent("    ") // Use 4-space indentation

        // Build the sealed class with type parameters
        val classBuilder = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.SEALED)
            .addAnnotation(
                AnnotationSpec.builder(ClassName("kotlinx.serialization", "Serializable"))
                    .addMember("with = %T::class", ClassName(UNION_PACKAGE, serializerClassName))
                    .build()
            )

        // Add type parameters with out variance
        for (paramName in typeParamNames) {
            classBuilder.addTypeVariable(TypeVariableName.invoke(paramName, variance = KModifier.OUT))
        }

        // Add KDoc
        val typeParamsStr = typeParamNames.joinToString(", ")
        classBuilder.addKdoc(
            "Sealed class representing a union of %L possible types.\n\n" +
                    "Used for API responses that can return different types (e.g., a Message object or a Boolean).",
            typeParamsStr
        )

        // Generate subclasses: First, Second, Third, ...
        val subclassNames =
            listOf("First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh", "Eighth", "Ninth", "Tenth")
        for (idx in 0 until size) {
            val subclassName = subclassNames[idx]
            val typeParam = typeParamNames[idx]

            val subclassBuilder = TypeSpec.classBuilder(subclassName)
                .addModifiers(KModifier.DATA)
                .superclass(
                    ClassName(UNION_PACKAGE, className).parameterizedBy(
                        List(size) { i ->
                            if (i == idx) TypeVariableName.invoke(typeParamNames[i])
                            else ClassName("kotlin", "Nothing")
                        }
                    ))
                .addTypeVariable(TypeVariableName.invoke(typeParam, variance = KModifier.OUT))
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter("value", TypeVariableName.invoke(typeParam))
                        .build()
                )
                .addProperty(
                    PropertySpec.builder("value", TypeVariableName.invoke(typeParam))
                        .initializer("value")
                        .build()
                )
                .addKdoc("Holds a value of type %L.", typeParam)

            classBuilder.addType(subclassBuilder.build())
        }

        // Generate convenience accessor methods: firstOrNull(), secondOrNull(), etc.
        for (idx in 0 until size) {
            val subclassName = subclassNames[idx]
            val typeParam = typeParamNames[idx]
            val methodName = "${subclassName.lowercase()}OrNull"

            val accessorMethod = FunSpec.builder(methodName)
                .returns(TypeVariableName.invoke(typeParam).copy(nullable = true))
                .addStatement("return (this as? %L)?.value", subclassName)
                .addKdoc("Returns the value if this is [%L], null otherwise.", subclassName)
                .build()

            classBuilder.addFunction(accessorMethod)
        }

        fileSpec.addType(classBuilder.build())
        fileSpec.build().writeTo(unionDir)
    }

    /**
     * Generates a generic UnionSerializer class for the given size.
     *
     * For size 2, generates:
     * ```kotlin
     * class UnionSerializer<A, B>(
     *     private val serializerA: KSerializer<A>,
     *     private val serializerB: KSerializer<B>
     * ) : KSerializer<Union<A, B>> {
     *     override val descriptor: SerialDescriptor = buildSerialDescriptor("Union", SerialKind.CONTEXTUAL)
     *
     *     override fun deserialize(decoder: Decoder): Union<A, B> {
     *         require(decoder is JsonDecoder) { "Only JSON is supported" }
     *         val jsonElement = decoder.decodeJsonElement()
     *
     *         val deserializers = listOf(
     *             { Union.First(decoder.json.decodeFromJsonElement(serializerA, jsonElement)) },
     *             { Union.Second(decoder.json.decodeFromJsonElement(serializerB, jsonElement)) }
     *         )
     *
     *         val results = deserializers.map { runCatching(it) }
     *         return results.firstNotNullOfOrNull { it.getOrNull() }
     *             ?: throw SerializationException(
     *                 "Could not deserialize Union: none of the types matched",
     *                 results.last().exceptionOrNull()
     *             )
     *     }
     *
     *     override fun serialize(encoder: Encoder, value: Union<A, B>) {
     *         require(encoder is JsonEncoder) { "Only JSON is supported" }
     *         when (value) {
     *             is Union.First -> encoder.encodeSerializableValue(serializerA, value.value)
     *             is Union.Second -> encoder.encodeSerializableValue(serializerB, value.value)
     *         }
     *     }
     * }
     * ```
     */
    private fun generateUnionSerializer(unionDir: File, size: Int) {
        val className = if (size == 2) "UnionSerializer" else "Union${size}Serializer"
        val unionClassName = if (size == 2) "Union" else "Union$size"

        // Generate type parameter names: A, B, C, ...
        val typeParamNames = (0 until size).map { idx ->
            ('A' + idx).toString()
        }

        val fileSpec = FileSpec.builder(UNION_PACKAGE, className)
            .addFileComment("Auto-generated from Swagger specification, do not modify this file manually")
            .addImport("kotlinx.serialization", "KSerializer", "SerializationException")
            .addImport("kotlinx.serialization.descriptors", "SerialDescriptor", "SerialKind", "buildSerialDescriptor")
            .addImport("kotlinx.serialization.encoding", "Decoder", "Encoder")
            .addImport("kotlinx.serialization.json", "JsonDecoder", "JsonEncoder")
            .addAnnotation(
                AnnotationSpec.builder(ClassName("kotlin", "OptIn"))
                    .addMember("%T::class", ClassName("kotlinx.serialization", "InternalSerializationApi"))
                    .build()
            )
            .indent("    ") // Use 4-space indentation

        // Build the serializer class
        val classBuilder = TypeSpec.classBuilder(className)

        // Add type parameters
        for (paramName in typeParamNames) {
            classBuilder.addTypeVariable(TypeVariableName.invoke(paramName))
        }

        // Build the Union<A, B, C> type
        val unionType = ClassName(UNION_PACKAGE, unionClassName).parameterizedBy(
            typeParamNames.map { TypeVariableName.invoke(it) }
        )

        // Add KSerializer interface implementation
        val kSerializerType = ClassName("kotlinx.serialization", "KSerializer")
            .parameterizedBy(unionType)
        classBuilder.addSuperinterface(kSerializerType)

        // Add primary constructor with serializer parameters
        val constructorBuilder = FunSpec.constructorBuilder()
        for (idx in 0 until size) {
            val paramName = "serializer${typeParamNames[idx]}"
            val serializerType = ClassName("kotlinx.serialization", "KSerializer")
                .parameterizedBy(TypeVariableName.invoke(typeParamNames[idx]))
            constructorBuilder.addParameter(paramName, serializerType)
            classBuilder.addProperty(
                PropertySpec.builder(paramName, serializerType)
                    .initializer(paramName)
                    .addModifiers(KModifier.PRIVATE)
                    .build()
            )
        }

        classBuilder.primaryConstructor(constructorBuilder.build())

        // Add KDoc
        classBuilder.addKdoc(
            "Serializer for [Union] that tries to deserialize as each type in order.\n\n" +
                    "If none of the types match, throws a [SerializationException]."
        )

        // Add descriptor property - use fully qualified name string directly
        val unionQualifiedClassName = "$UNION_PACKAGE.$unionClassName"
        classBuilder.addProperty(
            PropertySpec.builder("descriptor", ClassName("kotlinx.serialization.descriptors", "SerialDescriptor"))
                .addModifiers(KModifier.OVERRIDE)
                .initializer("buildSerialDescriptor(%S, SerialKind.CONTEXTUAL)", unionQualifiedClassName)
                .build()
        )

        // Add deserialize method
        val subclassNames =
            listOf("First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh", "Eighth", "Ninth", "Tenth")

        // Build lazy evaluation - try each deserializer in order until one succeeds
        val deserializeCodeBuilder = CodeBlock.builder()
            .addStatement("require(decoder is JsonDecoder) { \"Only JSON is supported\" }")
            .addStatement("val jsonElement = decoder.decodeJsonElement()")
            .addStatement("")

        // Try each deserializer in order, return on first success
        for (idx in 0 until size) {
            val typeParam = typeParamNames[idx]
            val serializerParam = "serializer$typeParam"
            val subclassName = subclassNames[idx]

            deserializeCodeBuilder
                .add("runCatching {\n")
                .indent()
                .addStatement(
                    "$unionClassName.$subclassName(decoder.json.decodeFromJsonElement($serializerParam, jsonElement))"
                )
                .unindent()
                .addStatement("}.getOrNull()?.let { return it }")
        }

        // If we get here, none of the deserializers succeeded - build type names list from serializers
        val typeNamesList = typeParamNames.joinToString(", ") { "serializer$it.descriptor.serialName" }
        deserializeCodeBuilder.addStatement(
            "val typeNames = listOf($typeNamesList).joinToString(\", \")"
        )
        deserializeCodeBuilder.addStatement(
            "throw SerializationException(\"Could not deserialize $unionClassName<\$typeNames>\")"
        )

        classBuilder.addFunction(
            FunSpec.builder("deserialize")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("decoder", ClassName("kotlinx.serialization.encoding", "Decoder"))
                .returns(unionType)
                .addCode(deserializeCodeBuilder.build())
                .build()
        )

        // Add serialize method
        val serializeCodeBuilder = CodeBlock.builder()
            .addStatement("require(encoder is JsonEncoder) { \"Only JSON is supported\" }")
            .beginControlFlow("when (value)")

        for (idx in 0 until size) {
            val serializerParam = "serializer${typeParamNames[idx]}"
            val subclassName = subclassNames[idx]
            serializeCodeBuilder.addStatement(
                "is $unionClassName.$subclassName -> encoder.encodeSerializableValue($serializerParam, value.value)"
            )
        }

        serializeCodeBuilder.endControlFlow()

        classBuilder.addFunction(
            FunSpec.builder("serialize")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("encoder", ClassName("kotlinx.serialization.encoding", "Encoder"))
                .addParameter("value", unionType)
                .addCode(serializeCodeBuilder.build())
                .build()
        )

        fileSpec.addType(classBuilder.build())
        fileSpec.build().writeTo(unionDir)
    }

    /**
     * Generates a SerializersModule extension property that registers all Union serializers
     * for the specific union types used in the Telegram Bot API.
     *
     * This allows easy configuration of Json with all required Union serializers:
     * ```kotlin
     * val json = Json {
     *     serializersModule += unionSerializersModule
     * }
     * ```
     *
     * The generated module includes contextual serializers for each unique Union type combination
     * (e.g., `Union<Message, Boolean>`, `Union3<Message, Boolean, Array<Message>>`).
     */
    private fun generateUnionSerializersModule(outputDir: File) {
        val fileSpec = FileSpec.builder(UNION_PACKAGE, "UnionSerializersModule")
            .addFileComment(
                """
                Auto-generated from Swagger specification, do not modify this file manually

                A SerializersModule that provides serializers for all Union types used in the Telegram Bot API.

                Usage:
                ```kotlin
                val json = Json {
                    serializersModule += unionSerializersModule
                }
                ```
                """.trimIndent()
            )
            .addImport("kotlinx.serialization.modules", "SerializersModule")
            .indent("    ") // Use 4-space indentation

        // Collect all unique union type combinations from responseUnionOperations
        // Group by the member types (not just size) to avoid duplicate registrations
        val uniqueUnions = mutableMapOf<String, ResponseUnionInfo>()

        for ((_, info) in responseUnionOperations) {
            // Create a key from the member types
            val key = info.members.joinToString(", ") { it.kotlinTypeName() }
            if (key !in uniqueUnions) {
                uniqueUnions[key] = info
            }
        }

        // Build the getter code using CodeBlock
        val getterCodeBuilder = CodeBlock.builder()
            .add("return SerializersModule·{\n")
            .indent()

        for ((_, info) in uniqueUnions) {
            val members = info.members
            val size = members.size
            val unionClassName = if (size == 2) "Union" else "Union$size"
            val serializerClassName = if (size == 2) "UnionSerializer" else "Union${size}Serializer"

            // Build the serializer arguments - access serializers from args array
            val serializerArgs = members.indices.joinToString(", ") { idx ->
                "args[$idx]"
            }

            // Add contextual registration using provider pattern
            // contextual(Union::class) { args -> UnionSerializer(args[0], args[1]) }
            getterCodeBuilder.addStatement(
                "contextual($unionClassName::class) { args -> $serializerClassName($serializerArgs) }"
            )
        }

        getterCodeBuilder.unindent().add("}\n")

        val extensionProperty = PropertySpec.builder(
            "unionSerializersModule",
            ClassName("kotlinx.serialization.modules", "SerializersModule")
        )
            .getter(
                FunSpec.getterBuilder()
                    .addCode(getterCodeBuilder.build())
                    .build()
            )
            .build()

        fileSpec.addProperty(extensionProperty)
        fileSpec.build().writeTo(outputDir)
    }

    private fun generateReplyMarkupInterface(outputDir: File) {
        val packageName = MODEL_PACKAGE
        val className = REPLY_MARKUP_INTERFACE

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
        fileSpec.build().writeToWithUnixLineEndings(outputDir)
    }

    private fun generateModels(swagger: JsonNode, outputDir: File) {
        val schemas = swagger.get("components")?.get("schemas") ?: return
        val modelPackage = MODEL_PACKAGE

        schemas.properties().forEach { (schemaName, schemaNode) ->
            if (schemaName in processedSchemas) return@forEach

            // Skip InputFile model generation - handled by handwritten sealed InputFile types
            if (schemaName == INPUT_FILE_TYPE) {
                processedSchemas.add(schemaName)
                return@forEach
            }

            try {
                generateModel(modelPackage, schemaName, schemaNode, outputDir)
            } catch (e: Exception) {
                logger.warn("Failed to generate model for $schemaName: ${e.message}")
            }
        }
    }

    private fun generateModel(packageName: String, className: String, schema: JsonNode, outputDir: File) {
        val description = schema.get("description")?.asText()
        val oneOf = schema.get("oneOf")

        // Handle union types
        if (oneOf != null && oneOf.isArray) {
            val unionMembers = extractTypeNamesFromRefArray(oneOf)

            // Special handling for MaybeInaccessibleMessage: use Message as the deserialization type
            // instead of generating a sealed interface. This is because Message and InaccessibleMessage
            // have overlapping fields and we want to deserialize to the richer Message type.
            if (className == MAYBE_INACCESSIBLE_MESSAGE_TYPE) {
                logger.lifecycle("Using $MESSAGE_TYPE for $MAYBE_INACCESSIBLE_MESSAGE_TYPE (special handling)")
                // Don't generate the union type itself, but ensure all member types are still generated
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

            // First, check if the schema has an explicit discriminator definition
            val discriminatorResult = extractDiscriminatorFromSchema(schema, unionMembers)
            val fallbackDiscriminatorInfo =
                if (discriminatorResult == null) findDiscriminatorInfo(unionMembers) else null

            if (discriminatorResult != null && discriminatorResult.isComplete) {
                // Complete discriminator mapping - generate polymorphic sealed interface
                generatePolymorphicSealedInterface(
                    packageName,
                    className,
                    description,
                    unionMembers,
                    discriminatorResult.info,
                    outputDir
                )
            } else if (discriminatorResult != null) {
                // Incomplete discriminator mapping - warn and generate standalone classes
                logger.warn("Discriminator mapping mismatch for $className: oneOf has ${unionMembers.size} members but mapping has fewer entries. Generating standalone classes.")
                generateStandaloneClassesWithDiscriminator(
                    packageName,
                    className,
                    description,
                    unionMembers,
                    discriminatorResult.info,
                    outputDir
                )
            } else if (fallbackDiscriminatorInfo != null) {
                // Found discriminator via fallback method - generate polymorphic sealed interface
                generatePolymorphicSealedInterface(
                    packageName,
                    className,
                    description,
                    unionMembers,
                    fallbackDiscriminatorInfo,
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

        // Check if this is a Form class (in form package)
        val isFormClass = packageName.endsWith(".form")

        val fileSpec = createFileSpec(packageName, className)

        when (type) {
            "object" -> {
                val properties = schema.get("properties")

                // If no properties, generate a simple object instead of a data class
                if (properties == null || !properties.isObject || properties.properties().isEmpty()) {
                    val objectBuilder = TypeSpec.objectBuilder(className)
                        .configureTypeBuilder(className, description, isFormClass)

                    fileSpec.addType(objectBuilder.build())
                } else {
                    val classBuilder = TypeSpec.classBuilder(className)
                        .addModifiers(KModifier.DATA)
                        .configureTypeBuilder(className, description, isFormClass)

                    val required = schema.get("required")?.map { it.asText() }?.toSet() ?: emptySet()
                    val constructorBuilder = FunSpec.constructorBuilder()

                    properties.properties().forEach { (propName, propSchema) ->
                        // For Form classes, map upload-related fields to InputFile (and List<InputFile> for arrays)
                        val context = "$className.$propName"
                        val propType = if (isFormClass) {
                            convertToPartDataIfNeeded(propSchema, context)
                        } else {
                            determinePropertyType(propSchema, context = context)
                        }
                        val isRequired = required.contains(propName)
                        val propDescription = propSchema.get("description")?.asText()

                        addPropertyWithParameter(
                            classBuilder,
                            constructorBuilder,
                            propName,
                            propType,
                            propDescription,
                            isRequired
                        )
                    }

                    // For Form classes, check for additionalProperties with binary format (dynamic file attachments)
                    if (isFormClass) {
                        val additionalProperties = schema.get("additionalProperties")
                        val hasBinaryAdditionalProperties = additionalProperties != null &&
                                additionalProperties.get("type")?.asText() == "string" &&
                                additionalProperties.get("format")?.asText() == "binary"

                        if (hasBinaryAdditionalProperties) {
                            val attachmentsType = LIST.parameterizedBy(
                                FORM_PART_TYPE.parameterizedBy(CHANNEL_PROVIDER_TYPE)
                            ).copy(nullable = true)

                            val attachmentsDescription = additionalProperties.get("description")?.asText()
                                ?: "Additional file attachments referenced via attach://<file_attach_name> in media fields"

                            addPropertyWithParameter(
                                classBuilder,
                                constructorBuilder,
                                "attachments",
                                attachmentsType,
                                attachmentsDescription,
                                false // not required
                            )
                        }
                    }

                    classBuilder.primaryConstructor(constructorBuilder.build())
                    fileSpec.addType(classBuilder.build())
                }
            }

            else -> {
                // For non-object types, create a typealias
                val aliasType = determinePropertyType(schema, context = className)
                fileSpec.addTypeAlias(TypeAliasSpec.builder(className, aliasType).build())
            }
        }

        fileSpec.build().writeToWithUnixLineEndings(outputDir)
    }

    private fun isReplyMarkupType(className: String): Boolean {
        // Use the dynamically collected replyMarkupTypes instead of hardcoding
        return className in replyMarkupTypes
    }

    private fun isUpdateFieldType(className: String): Boolean {
        // Check if this type is used in Update's optional non-primitive fields
        return className in updateFieldTypes
    }

    private data class DiscriminatorInfo(
        val fieldName: String,
        val memberValues: Map<String, String> // memberClassName -> discriminatorValue
    )

    /**
     * Result of extracting discriminator info from the schema.
     * Contains both the discriminator info and whether the mapping is complete.
     */
    private data class DiscriminatorExtractionResult(
        val info: DiscriminatorInfo,
        val isComplete: Boolean // true if all oneOf members are in the mapping
    )

    /**
     * Extract discriminator info from the OpenAPI discriminator field in the schema.
     * 
     * Returns a DiscriminatorExtractionResult that includes:
     * - The discriminator info (property name and member values)
     * - Whether the mapping is complete (all oneOf members have mappings)
     * 
     * For incomplete mappings (e.g., InlineQueryResult with 20 members but only 13 in mapping),
     * we still extract values from member schemas but mark the result as incomplete.
     */
    private fun extractDiscriminatorFromSchema(
        schema: JsonNode,
        unionMembers: List<String>
    ): DiscriminatorExtractionResult? {
        val discriminator = schema.get("discriminator") ?: return null
        val propertyName = discriminator.get("propertyName")?.asText() ?: return null
        val mapping = discriminator.get("mapping")

        val memberValues = mutableMapOf<String, String>()
        var mappingCount = 0

        // Build reverse mapping from explicit discriminator mapping: className -> discriminatorValue
        if (mapping != null && mapping.isObject) {
            mapping.properties().forEach { (discriminatorValue, ref) ->
                val className = ref.asText().substringAfterLast("/")
                mappingCount++
                if (className in unionMembers) {
                    memberValues[className] = discriminatorValue
                }
            }
        }

        val isComplete = mappingCount == unionMembers.size

        // For members not in the mapping, try to extract discriminator value from their schema
        val missingMembers = unionMembers.filter { it !in memberValues }
        for (memberName in missingMembers) {
            val memberSchema = allSchemas[memberName] ?: continue
            val properties = memberSchema.get("properties") ?: continue
            val discriminatorField = properties.get(propertyName) ?: continue

            val value = extractDiscriminatorValue(discriminatorField, memberName)
            if (value != null) {
                memberValues[memberName] = value
            }
        }

        // Check if we have mappings for all members
        if (memberValues.size == unionMembers.size) {
            return DiscriminatorExtractionResult(DiscriminatorInfo(propertyName, memberValues), isComplete)
        }

        // Log which members are still missing for debugging
        val stillMissing = unionMembers.filter { it !in memberValues }
        if (stillMissing.isNotEmpty()) {
            logger.warn("Discriminator '$propertyName' incomplete for members: $stillMissing")
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

            // If we found values for all members, and they're unique, we have a discriminator
            if (memberValues.size == unionMembers.size && memberValues.values.toSet().size == unionMembers.size) {
                return DiscriminatorInfo(discriminator, memberValues)
            }
        }

        return null
    }

    private fun extractDiscriminatorValue(field: JsonNode, memberName: String): String? {
        // Check for enum with a single value
        val enumValues = field.get("enum")
        if (enumValues != null && enumValues.isArray && enumValues.size() == 1) {
            return enumValues.get(0).asText()
        }

        // Extract from description patterns like: always "value" or must be "value"
        val description = field.get("description")?.asText() ?: return null

        // Pattern: always "value" or Type of ..., always "value"
        val alwaysPattern = """always\s+"([^"]+)"""".toRegex()
        val match = alwaysPattern.find(description)
        if (match != null) {
            return match.groupValues[1]
        }

        // Pattern: must be "value" (with quotes)
        val mustBePattern = """must be\s+"([^"]+)"""".toRegex()
        val mustMatch = mustBePattern.find(description)
        if (mustMatch != null) {
            return mustMatch.groupValues[1]
        }

        // Pattern: must be *value* (Markdown format, e.g., "must be *audio*")
        val mustBeMarkdownPattern = """must be\s+\*([^*]+)\*""".toRegex()
        val mustMarkdownMatch = mustBeMarkdownPattern.find(description)
        if (mustMarkdownMatch != null) {
            return mustMarkdownMatch.groupValues[1]
        }

        // Fallback: try to infer from the class name (e.g., BackgroundFillSolid -> "solid")
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

        // Create the sealed interface
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

        fileSpec.build().writeToWithUnixLineEndings(outputDir)
    }

    private fun generateSubclass(
        packageName: String,
        className: String,
        schema: JsonNode,
        parentInterface: String,
        discriminatorInfo: DiscriminatorInfo
    ): TypeSpec? {
        val info = extractClassGenerationInfo(schema) ?: return null
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
            .addKDocIfPresent(info.description)

        // Add IncomingUpdate interface if this class is used in Update's optional fields
        if (isUpdateFieldType(className)) {
            classBuilder.addSuperinterface(ClassName(ANNOTATION_PACKAGE, INCOMING_UPDATE_INTERFACE))
        }

        val constructorBuilder = FunSpec.constructorBuilder()

        info.properties.properties().forEach { (propName, propSchema) ->
            // Skip the discriminator field - @JsonClassDiscriminator handles it automatically
            if (propName == discriminatorInfo.fieldName) {
                return@forEach
            }

            addPropertyWithParameter(
                classBuilder,
                constructorBuilder,
                propName,
                propSchema,
                info.required.contains(propName),
                className
            )
        }

        // Only add a primary constructor if there are parameters
        if (constructorBuilder.parameters.isNotEmpty()) {
            classBuilder.primaryConstructor(constructorBuilder.build())
        } else {
            // If no properties, remove the DATA modifier and make it a regular class
            classBuilder.modifiers.remove(KModifier.DATA)
        }
        return classBuilder.build()
    }

    /**
     * Generate standalone classes for union types with incomplete discriminator mapping.
     * 
     * This is used when the discriminator mapping in the swagger spec doesn't cover all oneOf members.
     * For example, InlineQueryResult has 20 members but only 13 in the mapping.
     * 
     * Generates:
     * - An empty interface with the parent name
     * - Each member as a standalone class (no inheritance) with:
     *   - @Serializable annotation
     *   - @SerialName annotation with the discriminator value
     *   - The discriminator field included with a default value
     * 
     * All classes are generated in the same file named after the parent interface.
     */
    private fun generateStandaloneClassesWithDiscriminator(
        packageName: String,
        parentName: String,
        description: String?,
        unionMembers: List<String>,
        discriminatorInfo: DiscriminatorInfo,
        outputDir: File
    ) {
        val fileSpec = createFileSpec(packageName, parentName)

        // Generate an empty sealed interface (same as a normal parent, but subclasses don't inherit from it)
        val interfaceBuilder = TypeSpec.interfaceBuilder(parentName)
            .addModifiers(KModifier.SEALED)
            .addAnnotation(
                AnnotationSpec.builder(ClassName("kotlinx.serialization", "Serializable"))
                    .build()
            )
        if (description != null) {
            interfaceBuilder.addKdoc(sanitizeKDoc(description))
        }
        fileSpec.addType(interfaceBuilder.build())

        // Generate each member as a standalone class in the same file
        unionMembers.forEach { memberName ->
            val memberSchema = allSchemas[memberName]
            if (memberSchema != null) {
                val memberClass = generateStandaloneClassWithDiscriminator(
                    memberName,
                    memberSchema,
                    discriminatorInfo
                )
                if (memberClass != null) {
                    fileSpec.addType(memberClass)
                    processedSchemas.add(memberName)
                }
            }
        }

        fileSpec.build().writeToWithUnixLineEndings(outputDir)
    }

    /**
     * Generate a standalone class with the discriminator field and @SerialName annotation.
     * Returns a TypeSpec to be added to the parent file.
     */
    private fun generateStandaloneClassWithDiscriminator(
        className: String,
        schema: JsonNode,
        discriminatorInfo: DiscriminatorInfo
    ): TypeSpec? {
        val info = extractClassGenerationInfo(schema) ?: return null
        val discriminatorValue = discriminatorInfo.memberValues[className] ?: return null

        val classBuilder = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.DATA)
            .addAnnotation(
                AnnotationSpec.builder(ClassName("kotlinx.serialization", "Serializable"))
                    .build()
            )
            .addAnnotation(
                AnnotationSpec.builder(ClassName("kotlinx.serialization", "SerialName"))
                    .addMember("%S", discriminatorValue)
                    .build()
            )
            .addKDocIfPresent(info.description)

        // Add IncomingUpdate interface if this class is used in Update's optional fields
        if (isUpdateFieldType(className)) {
            classBuilder.addSuperinterface(ClassName(ANNOTATION_PACKAGE, INCOMING_UPDATE_INTERFACE))
        }

        val constructorBuilder = FunSpec.constructorBuilder()

        // Add the discriminator field first with a default value
        val discriminatorFieldName = discriminatorInfo.fieldName
        val discriminatorCamelName = snakeToCamelCase(discriminatorFieldName)

        // Add the discriminator property with a default value
        val discriminatorProperty = PropertySpec.builder(discriminatorCamelName, STRING)
            .initializer(discriminatorCamelName)
        if (discriminatorCamelName != discriminatorFieldName) {
            discriminatorProperty.addAnnotation(
                AnnotationSpec.builder(ClassName("kotlinx.serialization", "SerialName"))
                    .addMember("%S", discriminatorFieldName)
                    .build()
            )
        }
        classBuilder.addProperty(discriminatorProperty.build())

        // Add discriminator parameter with default value
        constructorBuilder.addParameter(
            ParameterSpec.builder(discriminatorCamelName, STRING)
                .defaultValue("%S", discriminatorValue)
                .build()
        )

        // Add other properties (skip the discriminator field since we already added it)
        info.properties.properties().forEach { (propName, propSchema) ->
            if (propName == discriminatorFieldName) {
                return@forEach
            }

            addPropertyWithParameter(
                classBuilder,
                constructorBuilder,
                propName,
                propSchema,
                info.required.contains(propName),
                className
            )
        }

        classBuilder.primaryConstructor(constructorBuilder.build())
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

        fileSpec.build().writeToWithUnixLineEndings(outputDir)
    }

    private fun generateSimpleSubclass(
        packageName: String,
        className: String,
        schema: JsonNode,
        parentInterface: String
    ): TypeSpec? {
        val info = extractClassGenerationInfo(schema) ?: return null

        val classBuilder = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.DATA)
            .addSuperinterface(ClassName(packageName, parentInterface))
            .addAnnotation(
                AnnotationSpec.builder(ClassName("kotlinx.serialization", "Serializable"))
                    .build()
            )
            .addKDocIfPresent(info.description)

        // Add IncomingUpdate interface if this class is used in Update's optional fields
        if (isUpdateFieldType(className)) {
            classBuilder.addSuperinterface(ClassName(ANNOTATION_PACKAGE, INCOMING_UPDATE_INTERFACE))
        }

        val constructorBuilder = FunSpec.constructorBuilder()

        info.properties.properties().forEach { (propName, propSchema) ->
            addPropertyWithParameter(
                classBuilder,
                constructorBuilder,
                propName,
                propSchema,
                info.required.contains(propName),
                className
            )
        }

        // Only add a primary constructor if there are parameters
        if (constructorBuilder.parameters.isNotEmpty()) {
            classBuilder.primaryConstructor(constructorBuilder.build())
        } else {
            // If no properties, remove the DATA modifier and make it a regular class
            classBuilder.modifiers.remove(KModifier.DATA)
        }
        return classBuilder.build()
    }

    /**
     * Determine the property type from a JSON schema.
     * This is the unified type determination function used for both model classes and form classes.
     *
     * @param schema The JSON schema node
     * @param forMultipart If true, multipart fields use InputFile-aware type resolution
     * @param context Optional context string for logging (e.g., "ClassName.propertyName")
     */
    private fun determinePropertyType(
        schema: JsonNode?,
        forMultipart: Boolean = false,
        context: String? = null
    ): TypeName {
        if (schema == null) return STRING

        val typeInfo = extractSchemaTypeInfo(schema)

        // For multipart forms, check if this is an InputFile type first
        if (forMultipart && isInputFileType(typeInfo.ref, typeInfo.oneOf, typeInfo.allOf)) {
            return CHANNEL_PROVIDER_TYPE
        }

        return when {
            // Handle direct $ref
            typeInfo.ref != null -> {
                val typeName = typeInfo.ref.substringAfterLast("/")
                resolveTypeName(typeName)
            }

            // Handle allOf - typically used for a single type reference
            typeInfo.allOf != null && typeInfo.allOf.isArray -> {
                val refs = extractTypeNamesFromRefArray(typeInfo.allOf)
                if (refs.isNotEmpty()) {
                    resolveTypeName(refs.first())
                } else {
                    context?.let { logger.warn("Using JsonElement for $it: allOf has no refs") }
                    JSON_ELEMENT_TYPE.copy(nullable = true)
                }
            }

            // Handle oneOf - union types
            typeInfo.oneOf != null && typeInfo.oneOf.isArray -> determineOneOfType(typeInfo.oneOf, context)

            // Handle arrays
            typeInfo.type == "array" -> {
                val items = schema.get("items")
                val itemType = if (items == null || items.isNull) {
                    context?.let { logger.warn("Using JsonElement for $it: array has no items definition") }
                    JSON_ELEMENT_TYPE
                } else {
                    // For multipart arrays, check if items are InputFile
                    if (forMultipart) {
                        val itemTypeInfo = extractSchemaTypeInfo(items)
                        if (isInputFileType(itemTypeInfo.ref, itemTypeInfo.oneOf, itemTypeInfo.allOf)) {
                            return LIST.parameterizedBy(CHANNEL_PROVIDER_TYPE)
                        }
                    }
                    val determinedType = determinePropertyType(items, forMultipart, context?.let { "$it[]" })
                    if (determinedType == ANY.copy(nullable = true)) {
                        context?.let { logger.warn("Using JsonElement for $it: array item type resolved to Any") }
                        JSON_ELEMENT_TYPE
                    } else {
                        determinedType
                    }
                }
                LIST.parameterizedBy(itemType)
            }

            // Handle inline objects (no concrete type available)
            typeInfo.type == "object" -> {
                context?.let { logger.warn("Using JsonElement for $it: inline object without concrete type") }
                JSON_ELEMENT_TYPE.copy(nullable = true)
            }

            // Handle primitive types
            typeInfo.type == "string" -> STRING
            typeInfo.type == "integer" -> if (typeInfo.format == "int64") LONG else INT
            typeInfo.type == "number" -> if (typeInfo.format == "float") FLOAT else DOUBLE
            typeInfo.type == "boolean" -> BOOLEAN

            // Fallback
            else -> {
                context?.let { logger.warn("Using JsonElement for $it: unknown schema type '${typeInfo.type}'") }
                JSON_ELEMENT_TYPE.copy(nullable = true)
            }
        }
    }

    /**
     * Resolve a type name to a TypeName, including special handling for handwritten InputFile.
     */
    private fun resolveTypeName(typeName: String): TypeName {
        return when (typeName) {
            MAYBE_INACCESSIBLE_MESSAGE_TYPE -> ClassName(MODEL_PACKAGE, MESSAGE_TYPE)
            INPUT_FILE_TYPE -> STRING  // InputFile schema maps to String in non-form contexts; multipart uses InputFile-aware mapping
            else -> ClassName(MODEL_PACKAGE, typeName)
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
     * 
     * @param oneOf The oneOf JSON node
     * @param context Optional context string for logging (e.g., "ClassName.propertyName")
     */
    private fun determineLargestPrimitiveType(oneOf: JsonNode, context: String? = null): TypeName {
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
            else -> {
                if (context != null) {
                    val refs = oneOf.mapNotNull { it.get($$"$ref")?.asText()?.substringAfterLast("/") }
                    logger.warn("Using JsonElement for $context: oneOf contains no primitive types, refs=$refs")
                }
                ClassName("kotlinx.serialization.json", "JsonElement").copy(nullable = true)
            }
        }
    }

    private fun generateApiInterface(swagger: JsonNode, outputDir: File) {
        val packageName = BASE_PACKAGE
        val className = "TelegramBotApi"

        val fileSpec = createFileSpec(packageName, className)
        // Add `@file:Suppress` annotation to suppress duplicate code warnings
        fileSpec.addAnnotation(
            AnnotationSpec.builder(ClassName("kotlin", "Suppress"))
                .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                .addMember("%S", "RedundantVisibilityModifier")
                .addMember("%S", "unused")
                .addMember("%S", "GrazieStyle")
                .addMember("%S", "GrazieInspection")
                .build()
        )

        val interfaceBuilder = TypeSpec.interfaceBuilder(className)

        // Add @TelegramBotApiVersion annotation with version from swagger spec
        val apiVersion = swagger.get("info")?.get("version")?.asText()
        if (apiVersion != null) {
            interfaceBuilder.addAnnotation(
                AnnotationSpec.builder(ClassName(ANNOTATION_PACKAGE, TELEGRAM_BOT_API_VERSION_ANNOTATION))
                    .addMember("%S", apiVersion)
                    .build()
            )
            logger.lifecycle("Generated TelegramBotApi with API version: $apiVersion")
        }

        // Parse paths
        val paths = swagger.get("paths") ?: return
        paths.properties().forEach { (path, methods) ->
            methods.properties().forEach { (method, operation) ->
                try {
                    val functions = generateFunction(path.trimStart('/'), method, operation, outputDir)
                    functions.forEach { function ->
                        interfaceBuilder.addFunction(function)
                    }
                } catch (e: Exception) {
                    logger.warn("Failed to generate function for $method $path: ${e.message}")
                }
            }
        }

        // Add the downloadFile method at the end
        val downloadFileFunction = FunSpec.builder("downloadFile")
            .addModifiers(KModifier.SUSPEND, KModifier.ABSTRACT)
            .addAnnotation(
                AnnotationSpec.builder(ClassName(PLUGIN_PACKAGE, TELEGRAM_FILE_DOWNLOAD_ANNOTATION))
                    .build()
            )
            .addAnnotation(
                AnnotationSpec.builder(ClassName(KTORFIT_HTTP_PACKAGE, GET_ANNOTATION))
                    .addMember("%S", "{filePath}")
                    .build()
            )
            .addAnnotation(
                AnnotationSpec.builder(ClassName(KTORFIT_HTTP_PACKAGE, STREAMING_ANNOTATION))
                    .build()
            )
            .addParameter(
                ParameterSpec.builder("filePath", String::class)
                    .addAnnotation(
                        AnnotationSpec.builder(ClassName(KTORFIT_HTTP_PACKAGE, PATH_ANNOTATION))
                            .addMember("%S", "filePath")
                            .build()
                    )
                    .build()
            )
            .returns(ClassName(KTOR_STATEMENT_PACKAGE, HTTP_STATEMENT_TYPE))
            .addKdoc("Downloads a file from Telegram servers.")
            .build()

        interfaceBuilder.addFunction(downloadFileFunction)

        fileSpec.addType(interfaceBuilder.build())
        fileSpec.build().writeToWithUnixLineEndings(outputDir)

        // Generate extension functions for multipart operations
        if (multipartOperations.isNotEmpty()) {
            generateMultipartExtensions(packageName, className, outputDir)
        }

        // Generate extension functions for GET query parameters that need JSON serialization
        if (queryStringOperations.isNotEmpty()) {
            generateQueryExtensions(outputDir)
        }

        // Generate scatter extension functions for JSON body operations
        if (jsonBodyOperations.isNotEmpty()) {
            generateJsonBodyExtensions(outputDir)
        }
    }

    /**
     * Creates a base function builder with the common setup:
     * - abstract and suspend modifiers
     * - KDoc from description
     * - HTTP method annotation
     * - Query/path/header parameters from operation
     */
    private fun createBaseFunctionBuilder(
        operationId: String,
        description: String?,
        method: String,
        path: String,
        operation: JsonNode
    ): FunSpec.Builder {
        val functionBuilder = FunSpec.builder(operationId)
            .addModifiers(KModifier.ABSTRACT, KModifier.SUSPEND)

        if (description != null) {
            functionBuilder.addKdoc(sanitizeKDoc(description))
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

        // Add query/path/header parameters from the operation
        val parameters = operation.get("parameters")
        if (parameters != null && parameters.isArray) {
            parameters.forEach { param ->
                addParameter(functionBuilder, param, operationId, operation, method)
            }
        }

        return functionBuilder
    }

    private fun generateFunction(path: String, method: String, operation: JsonNode, outputDir: File): List<FunSpec> {
        val operationId = operation.get("operationId")?.asText() ?: generateOperationId(method, path)
        val description = operation.get("description")?.asText()
        val requestBody = operation.get("requestBody")
        val returnType = determineReturnType(operation, operationId)

        // Check if this is a multipart operation
        val content = requestBody?.get("content")
        val jsonContent = content?.get("application/json")
        val multipartContent = content?.get("multipart/form-data")
        val isMultipart = jsonContent == null && multipartContent != null

        if (isMultipart) {
            // Generate only the MultiPartFormDataContent overload for multipart operations
            // The scattered parameters version will be generated as an extension function
            val schema = multipartContent.get("schema")
            if (schema != null) {
                // Generate MultiPartFormDataContent overload
                val multipartFormFunction = createBaseFunctionBuilder(operationId, description, method, path, operation)
                    .returns(returnType)
                    .addParameter(
                        ParameterSpec.builder(
                            "formData",
                            MULTIPART_FORM_DATA_CONTENT_TYPE
                        ).addAnnotation(
                            AnnotationSpec.builder(ClassName("de.jensklingenberg.ktorfit.http", "Body"))
                                .build()
                        ).build()
                    )
                    .build()

                // Store schema and return type for later extension function generation
                multipartOperations[operationId] = MultipartOperationInfo(schema, returnType, description)

                // Generate form class for this multipart operation
                generateFormClass(operationId, schema, outputDir)

                return listOf(multipartFormFunction)
            }
        }

        // For non-multipart operations, generate a single function
        val functionBuilder = createBaseFunctionBuilder(operationId, description, method, path, operation)
        functionBuilder.returns(returnType)

        if (requestBody != null) {
            addBodyParameter(functionBuilder, requestBody, operationId, returnType, outputDir)

            // Track JSON body operations for scatter extension generation
            // This excludes multipart operations (already handled above) and operations without JSON content
            if (jsonContent != null) {
                val schema = jsonContent.get("schema")
                if (schema != null) {
                    // Determine the request class name
                    val requestClassName = schema.get($$"$ref")?.asText()
                        ?.substringAfterLast("/")
                        ?: if (schema.get("type")?.asText() == "object") {
                            // Inline object schema - class name is generated as operationId + "Request"
                            operationId.replaceFirstChar { it.uppercase() } + "Request"
                        } else {
                            // Other types (should be rare for request bodies)
                            null
                        }

                    if (requestClassName != null) {
                        jsonBodyOperations[operationId] = JsonBodyOperationInfo(
                            requestClassName = requestClassName,
                            requestSchema = schema,
                            returnType = returnType,
                            description = description
                        )
                    }
                }
            }
        }

        return listOf(functionBuilder.build())
    }

    /**
     * Convert InputFile types for generated multipart forms and form extensions.
     *
     * - Direct InputFile fields are generated as InputFile
     * - Arrays of InputFile are generated as List<InputFile>
     * - Other types are resolved via normal schema mapping
     */
    private fun convertToPartDataIfNeeded(schema: JsonNode, context: String? = null): TypeName {
        val typeInfo = extractSchemaTypeInfo(schema)

        // Check if this is an InputFile type - keep it as InputFile
        if (isInputFileType(typeInfo.ref, typeInfo.oneOf, typeInfo.allOf)) {
            return ClassName(TYPE_PACKAGE, INPUT_FILE_TYPE)
        }

        // Check if this is an array of InputFile - keep it as List<InputFile>
        if (typeInfo.type == "array") {
            val items = schema.get("items")
            if (items != null) {
                val itemTypeInfo = extractSchemaTypeInfo(items)
                if (isInputFileType(itemTypeInfo.ref, itemTypeInfo.oneOf, itemTypeInfo.allOf)) {
                    return LIST.parameterizedBy(ClassName(TYPE_PACKAGE, INPUT_FILE_TYPE))
                }
            }
        }

        // For non-file types, use normal property type determination
        return determinePropertyType(schema, forMultipart = false, context = context)
    }

    private fun addParameter(
        functionBuilder: FunSpec.Builder,
        param: JsonNode,
        operationId: String,
        operation: JsonNode,
        method: String
    ) {
        val name = param.get("name")?.asText() ?: return
        val paramIn = param.get("in")?.asText() ?: return
        val required = param.get("required")?.asBoolean() ?: false
        val schema = param.get("schema")
        val description = param.get("description")?.asText()

        val rawParamType = determinePropertyType(schema, context = "parameter.$name")
        val shouldConvertToStringQuery = shouldUseStringQueryType(paramIn, rawParamType) &&
                shouldGenerateQueryExtension(operationId, operation, method)
        if (shouldConvertToStringQuery) {
            queryStringOperations[operationId] = operation
        }

        val paramType = if (shouldConvertToStringQuery) STRING else rawParamType
        val finalType = if (required) paramType else paramType.copy(nullable = true)
        val camelName = snakeToCamelCase(name)

        val paramBuilder = ParameterSpec.builder(camelName, finalType)

        // Add annotation based on a parameter location
        when (paramIn) {
            "query" -> {
                val queryAnnotationBuilder =
                    AnnotationSpec.builder(ClassName("de.jensklingenberg.ktorfit.http", "Query"))
                if (camelName != name) {
                    queryAnnotationBuilder.addMember("%S", name)
                }
                paramBuilder.addAnnotation(queryAnnotationBuilder.build())
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

        if (description != null) {
            paramBuilder.addKdoc(sanitizeKDoc(description))
        }

        functionBuilder.addParameter(paramBuilder.build())
    }

    private fun shouldUseStringQueryType(paramIn: String, paramType: TypeName): Boolean {
        if (paramIn != "query") return false
        return paramType !in setOf(STRING, BOOLEAN, INT, LONG, FLOAT, DOUBLE)
    }

    private fun shouldGenerateQueryExtension(operationId: String, operation: JsonNode, method: String): Boolean {
        if (operationId in queryStringOperations) return true
        if (method.uppercase() != "GET") return false
        val requestBody = operation.get("requestBody")
        return requestBody == null || requestBody.isNull
    }

    @Suppress("unused")
    private fun addBodyParameter(
        functionBuilder: FunSpec.Builder,
        requestBody: JsonNode,
        operationId: String,
        returnType: TypeName,
        outputDir: File
    ) {
        val content = requestBody.get("content") ?: return

        // Only handle JSON content (multipart is handled in generateFunction)
        val jsonContent = content.get("application/json") ?: return
        val schema = jsonContent.get("schema") ?: return

        // For JSON requests, use @Body parameter
        addJsonBodyParameter(functionBuilder, schema, operationId, outputDir)
    }

    private fun addJsonBodyParameter(
        functionBuilder: FunSpec.Builder,
        schema: JsonNode,
        operationId: String,
        outputDir: File
    ) {
        // Check if the schema is an inline object definition (no $ref)
        val ref = schema.get($$"$ref")?.asText()
        val bodyType = if (ref != null) {
            // Use existing schema reference
            determinePropertyType(schema, context = "$operationId.body")
        } else if (schema.get("type")?.asText() == "object") {
            // Generate a request body class for inline object schema
            val className = generateRequestBodyClass(operationId, schema, outputDir)
            ClassName(MODEL_PACKAGE, className)
        } else {
            // Fallback to determinePropertyType for other cases
            determinePropertyType(schema, context = "$operationId.body")
        }

        val paramBuilder = ParameterSpec.builder("body", bodyType)
        val annotation = AnnotationSpec.builder(ClassName("de.jensklingenberg.ktorfit.http", "Body"))
            .build()
        paramBuilder.addAnnotation(annotation)
        functionBuilder.addParameter(paramBuilder.build())
    }

    private fun generateRequestBodyClass(
        operationId: String,
        schema: JsonNode,
        outputDir: File,
        isMultipart: Boolean = false
    ): String {
        // Check if we already generated this request body
        generatedRequestBodies[operationId]?.let { return it }

        // Generate class name from operation ID
        // For multipart: sendMessage -> SendMessageForm
        // For JSON: sendMessage -> SendMessageRequest
        val className = operationId.replaceFirstChar { it.uppercase() } + if (isMultipart) "Form" else "Request"

        // Generate the request body class in the appropriate package
        val packageName = if (isMultipart) FORM_PACKAGE else MODEL_PACKAGE
        generateRegularClass(packageName, className, schema, outputDir)

        // Mark as processed
        processedSchemas.add(className)
        generatedRequestBodies[operationId] = className

        return className
    }

    /**
     * Generate a form class for multipart operations.
     * Form classes are generated in the form package and keep InputFile fields as InputFile.
     */
    private fun generateFormClass(operationId: String, schema: JsonNode, outputDir: File): String {
        // Check if we already generated this form class
        generatedRequestBodies[operationId]?.let { return it }

        // Generate the class name from operation ID: sendPhoto -> SendPhotoForm
        val className = operationId.replaceFirstChar { it.uppercase() } + "Form"
        val packageName = FORM_PACKAGE

        val fileSpec = createFileSpec(packageName, className)

        val properties = schema.get("properties")
        val required = schema.get("required")?.map { it.asText() }?.toSet() ?: emptySet()

        // Check for additionalProperties with binary format (dynamic file attachments)
        val additionalProperties = schema.get("additionalProperties")
        val hasBinaryAdditionalProperties = additionalProperties != null &&
                additionalProperties.get("type")?.asText() == "string" &&
                additionalProperties.get("format")?.asText() == "binary"

        if (properties != null && properties.isObject && properties.properties().isNotEmpty()) {
            val classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(KModifier.DATA)

            val constructorBuilder = FunSpec.constructorBuilder()

            properties.properties().forEach { (propName, propSchema) ->
                val isRequired = required.contains(propName)
                // For form classes, keep InputFile types as InputFile
                val propType = convertToPartDataIfNeeded(propSchema, "$className.$propName")
                val propDescription = propSchema.get("description")?.asText()

                addPropertyWithParameter(
                    classBuilder,
                    constructorBuilder,
                    propName,
                    propType,
                    propDescription,
                    isRequired
                )
            }

            // Add attachments field if additionalProperties has the binary format
            if (hasBinaryAdditionalProperties) {
                val attachmentsType = LIST.parameterizedBy(
                    FORM_PART_TYPE.parameterizedBy(CHANNEL_PROVIDER_TYPE)
                ).copy(nullable = true)

                val attachmentsDescription = additionalProperties.get("description")?.asText()
                    ?: "Additional file attachments referenced via attach://<file_attach_name> in media fields"

                addPropertyWithParameter(
                    classBuilder,
                    constructorBuilder,
                    "attachments",
                    attachmentsType,
                    attachmentsDescription,
                    false // not required
                )
            }

            classBuilder.primaryConstructor(constructorBuilder.build())
            fileSpec.addType(classBuilder.build())
        }

        fileSpec.build().writeToWithUnixLineEndings(outputDir)

        // Mark as processed
        generatedRequestBodies[operationId] = className

        return className
    }

    private fun determineReturnType(operation: JsonNode, operationId: String): TypeName {
        val responses = operation.get("responses") ?: return ClassName(
            TYPE_PACKAGE,
            TELEGRAM_RESPONSE_TYPE
        ).parameterizedBy(BOOLEAN)
        val successResponse = responses.get("200") ?: responses.get("201") ?: return ClassName(
            TYPE_PACKAGE,
            TELEGRAM_RESPONSE_TYPE
        ).parameterizedBy(BOOLEAN)
        val content = successResponse.get("content") ?: return ClassName(
            TYPE_PACKAGE,
            TELEGRAM_RESPONSE_TYPE
        ).parameterizedBy(BOOLEAN)
        val jsonContent = content.get("application/json") ?: return ClassName(
            TYPE_PACKAGE,
            TELEGRAM_RESPONSE_TYPE
        ).parameterizedBy(BOOLEAN)
        val schema = jsonContent.get("schema") ?: return ClassName(
            TYPE_PACKAGE,
            TELEGRAM_RESPONSE_TYPE
        ).parameterizedBy(BOOLEAN)

        // Check if this is a Telegram API response wrapper (has "ok" and "result" properties)
        val properties = schema.get("properties")
        if (properties != null && properties.has("ok") && properties.has("result")) {
            // Extract the result type
            val resultProperty = properties.get("result")
            val resultType = determinePropertyType(resultProperty, context = "$operationId.response.result")
            return ClassName(TYPE_PACKAGE, TELEGRAM_RESPONSE_TYPE).parameterizedBy(resultType)
        }

        // Fallback to the schema type directly wrapped in TelegramResponse
        val schemaType = determinePropertyType(schema, context = "$operationId.response")
        return ClassName(TYPE_PACKAGE, TELEGRAM_RESPONSE_TYPE).parameterizedBy(schemaType)
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
            .addFileComment("Auto-generated from Swagger specification, do not modify this file manually")
            .apply {
                additionalComments.forEach { addFileComment(it) }
            }
    }

    /**
     * Write FileSpec to the directory with Unix line endings (LF) regardless of platform
     */
    private fun FileSpec.writeToWithUnixLineEndings(directory: File) {
        val stringBuilder = StringBuilder()
        writeTo(stringBuilder)
        val content = stringBuilder.toString().replace("\r\n", "\n")

        val outputDirectory = directory.resolve(packageName.replace('.', File.separatorChar))
        outputDirectory.mkdirs()
        val outputFile = outputDirectory.resolve("$name.kt")
        outputFile.writeText(content, Charsets.UTF_8)
    }

    private fun snakeToCamelCase(snakeCase: String): String {
        return snakeToCamelCaseCache.getOrPut(snakeCase) {
            if (!snakeCase.contains('_')) return@getOrPut snakeCase

            snakeCase.split('_')
                .mapIndexed { index, part ->
                    if (index == 0) part else part.capitalize()
                }
                .joinToString("")
        }
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
     * Generate extension functions for GET operations that include non-primitive query parameters.
     *
     * The generated extensions target `TelegramBotApi`, where the underlying interface methods
     * use String/String? For such query parameters. Extensions in query/Queries.kt expose strongly
     * typed parameters and serialize them with Json.encodeToString.
     */
    private fun generateQueryExtensions(outputDir: File) {
        val fileSpec = createFileSpec(
            QUERY_PACKAGE,
            "Queries",
            """
                Extension functions for Telegram Bot API query operations.

                This file provides typed query extension functions for methods whose query parameters
                require JSON serialization before sending to Telegram API.
            """.trimIndent()
        )
        fileSpec.addAnnotation(
            AnnotationSpec.builder(ClassName("kotlin", "Suppress"))
                .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                .addMember("%S", "RedundantVisibilityModifier")
                .addMember("%S", "unused")
                .addMember("%S", "DuplicatedCode")
                .build()
        )

        queryStringOperations.forEach { (operationId, operation) ->
            val description = operation.get("description")?.asText()
            val parameters = operation.get("parameters")
            if (parameters == null || !parameters.isArray) return@forEach

            val functionBuilder = FunSpec.builder(operationId)
                .addModifiers(KModifier.SUSPEND)
                .receiver(ClassName(BASE_PACKAGE, "TelegramBotApi"))

            if (description != null) {
                functionBuilder.addKdoc(sanitizeKDoc(description))
            }

            val callArguments = mutableListOf<String>()

            parameters.forEach { param ->
                val name = param.get("name")?.asText() ?: return@forEach
                val paramIn = param.get("in")?.asText() ?: return@forEach
                if (paramIn != "query") return@forEach

                val required = param.get("required")?.asBoolean() ?: false
                val schema = param.get("schema")
                val rawType = determinePropertyType(schema, context = "query.extension.$operationId.$name")
                val camelName = snakeToCamelCase(name)
                val isConverted = shouldUseStringQueryType(paramIn, rawType)
                val finalType = if (required) rawType else rawType.copy(nullable = true)

                val paramBuilder = ParameterSpec.builder(camelName, finalType)
                if (!required) {
                    paramBuilder.defaultValue("null")
                }
                val paramDescription = param.get("description")?.asText()
                if (paramDescription != null) {
                    paramBuilder.addKdoc(sanitizeKDoc(paramDescription))
                }
                functionBuilder.addParameter(paramBuilder.build())

                val callArgument = if (isConverted) {
                    if (required) {
                        "$camelName = %T.encodeToString($camelName)"
                    } else {
                        "$camelName = $camelName?.let { %T.encodeToString(it) }"
                    }
                } else {
                    "$camelName = $camelName"
                }
                callArguments.add(callArgument)
            }

            val returnType = determineReturnType(operation, operationId)
            functionBuilder.returns(returnType)

            val codeBuilder = CodeBlock.builder()
            if (callArguments.isEmpty()) {
                codeBuilder.addStatement("return %N()", operationId)
            } else {
                codeBuilder.add("return %N(\n", operationId)
                codeBuilder.indent()
                callArguments.forEachIndexed { index, argumentTemplate ->
                    if (argumentTemplate.contains("%T.encodeToString")) {
                        codeBuilder.add(argumentTemplate, ClassName("kotlinx.serialization.json", "Json"))
                    } else {
                        codeBuilder.add(argumentTemplate)
                    }
                    if (index < callArguments.size - 1) {
                        codeBuilder.add(",\n")
                    } else {
                        codeBuilder.add("\n")
                    }
                }
                codeBuilder.unindent()
                codeBuilder.add(")")
            }
            functionBuilder.addCode(codeBuilder.build())

            fileSpec.addFunction(functionBuilder.build())
        }

        fileSpec.build().writeToWithUnixLineEndings(outputDir)
    }

    /**
     * Generate extension functions for multipart operations that accept Form data classes
     */
    @Suppress("SameParameterValue")
    private fun generateMultipartExtensions(packageName: String, interfaceName: String, outputDir: File) {
        val fileSpec = createFileSpec(
            "$packageName.form",
            "Forms",
            """
                    Extension functions for Telegram Bot API multipart operations.
    
                    This file provides two types of convenient functions for operations that require
                    multipart form data with file uploads:
                    
                    1. **Scatter functions**: Accept individual parameters as named arguments
                       Example: `api.sendPhoto(chatId = 123, photo = inputFile)`
                    
                    2. **Form functions**: Accept pre-constructed form wrapper classes
                       Example: `api.sendPhoto(form = SendPhotoForm(...))`
                   """.trimIndent()
        )
        // Add `@file:Suppress` annotation to suppress duplicate code warnings
        fileSpec.addAnnotation(
            AnnotationSpec.builder(ClassName("kotlin", "Suppress"))
                .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                .addMember("%S", "RedundantVisibilityModifier")
                .addMember("%S", "unused")
                .addMember("%S", "DuplicatedCode")
                .build()
        )

        multipartOperations.forEach { (operationId, info) ->
            // Form class name is operationId with the first letter uppercase + "Form"
            val formClassName = operationId.replaceFirstChar { it.uppercase() } + "Form"

            // 1. Generate scattered parameters extension function (builds MultiPartFormDataContent)
            val scatteredExtensionFunction = generateScatteredParametersExtensionFunction(
                interfaceName,
                operationId,
                info.schema,
                info.returnType,
                info.description
            )
            fileSpec.addFunction(scatteredExtensionFunction)

            // 2. Generate Form-based extension function (calls the scattered parameters extension)
            val formExtensionFunction = generateFormExtensionFunction(
                interfaceName,
                operationId,
                formClassName,
                info.schema,
                info.returnType,
                info.description
            )
            fileSpec.addFunction(formExtensionFunction)
        }

        fileSpec.build().writeToWithUnixLineEndings(outputDir)
    }

    /**
     * Generate an extension function with scattered parameters that builds MultiPartFormDataContent
     * and calls the interface method.
     *
     * The generated signature mirrors the multipart request body fields and uses InputFile
     * for direct file upload fields.
     */
    private fun generateScatteredParametersExtensionFunction(
        interfaceName: String,
        operationId: String,
        schema: JsonNode,
        returnType: TypeName,
        description: String?
    ): FunSpec {
        val properties = schema.get("properties")
        val required = schema.get("required")?.map { it.asText() }?.toSet() ?: emptySet()

        val functionBuilder = FunSpec.builder(operationId)
            .addModifiers(KModifier.SUSPEND)
            .receiver(ClassName(BASE_PACKAGE, interfaceName))
            .returns(returnType)

        if (description != null) {
            functionBuilder.addKdoc(sanitizeKDoc(description))
        }

        // Add parameters for each property
        properties?.properties()?.forEach { (propName, propSchema) ->
            val camelName = snakeToCamelCase(propName)
            val isRequired = required.contains(propName)
            val propType = convertToPartDataIfNeeded(propSchema, "$operationId.$propName")
            val finalType = if (isRequired) propType else propType.copy(nullable = true)

            val paramBuilder = ParameterSpec.builder(camelName, finalType)
            if (!isRequired) {
                paramBuilder.defaultValue("null")
            }
            val paramDescription = propSchema.get("description")?.asText()
            if (paramDescription != null) {
                paramBuilder.addKdoc(sanitizeKDoc(paramDescription))
            }
            functionBuilder.addParameter(paramBuilder.build())
        }

        // Check for additionalProperties with binary format (dynamic file attachments)
        val additionalProperties = schema.get("additionalProperties")
        val hasBinaryAdditionalProperties = additionalProperties != null &&
                additionalProperties.get("type")?.asText() == "string" &&
                additionalProperties.get("format")?.asText() == "binary"

        if (hasBinaryAdditionalProperties) {
            val attachmentsType = LIST.parameterizedBy(
                FORM_PART_TYPE.parameterizedBy(CHANNEL_PROVIDER_TYPE)
            ).copy(nullable = true)

            val attachmentsParam = ParameterSpec.builder("attachments", attachmentsType)
                .defaultValue("null")
                .addKdoc(
                    sanitizeKDoc(
                        additionalProperties.get("description")?.asText()
                            ?: "Additional file attachments referenced via attach://<file_attach_name> in media fields"
                    )
                )
                .build()
            functionBuilder.addParameter(attachmentsParam)
        }

        // Build the function body - construct MultiPartFormDataContent and call the interface method
        val codeBuilder = CodeBlock.builder()
        codeBuilder.addStatement(
            "val formData = %T(%T {",
            MULTIPART_FORM_DATA_CONTENT_TYPE,
            FORM_DATA_BUILDER_TYPE
        )
        codeBuilder.indent()

        // Add each parameter to the form data, skipping null values
        properties?.properties()?.forEach { (propName, propSchema) ->
            val camelName = snakeToCamelCase(propName)
            val isRequired = required.contains(propName)
            val typeInfo = extractSchemaTypeInfo(propSchema)
            val isInputFile = isInputFileType(typeInfo.ref, typeInfo.oneOf, typeInfo.allOf)
            val isArray = typeInfo.type == "array"

            if (isRequired) {
                // Required parameter - add directly
                if (isInputFile) {
                    codeBuilder.addStatement(
                        "append(%N.%M(%S))",
                        camelName,
                        MemberName(TYPE_PACKAGE, "toFormPart"),
                        propName
                    )
                } else if (isArray) {
                    // Check if array items are InputFile
                    val items = propSchema.get("items")
                    val itemTypeInfo = extractSchemaTypeInfo(items)
                    val isInputFileArray = isInputFileType(itemTypeInfo.ref, itemTypeInfo.oneOf, itemTypeInfo.allOf)
                    if (isInputFileArray) {
                        codeBuilder.addStatement(
                            "%N.forEach { append(it.%M(%S)) }",
                            camelName,
                            MemberName(TYPE_PACKAGE, "toFormPart"),
                            propName
                        )
                    } else {
                        codeBuilder.addStatement(
                            "append(%S, %T.encodeToString(%N))",
                            propName,
                            ClassName("kotlinx.serialization.json", "Json"),
                            camelName
                        )
                    }
                } else {
                    // Primitive or complex type
                    appendRequiredFormData(codeBuilder, propName, camelName, propSchema)
                }
            } else {
                // Optional parameter - check for null
                if (isInputFile) {
                    codeBuilder.addStatement(
                        "%N?.let { append(it.%M(%S)) }",
                        camelName,
                        MemberName(TYPE_PACKAGE, "toFormPart"),
                        propName
                    )
                } else if (isArray) {
                    val items = propSchema.get("items")
                    val itemTypeInfo = extractSchemaTypeInfo(items)
                    val isInputFileArray = isInputFileType(itemTypeInfo.ref, itemTypeInfo.oneOf, itemTypeInfo.allOf)
                    if (isInputFileArray) {
                        codeBuilder.addStatement(
                            "%N?.forEach { append(it.%M(%S)) }",
                            camelName,
                            MemberName(TYPE_PACKAGE, "toFormPart"),
                            propName
                        )
                    } else {
                        codeBuilder.beginControlFlow("%N?.let", camelName)
                        codeBuilder.addStatement(
                            "append(%S, %T.encodeToString(it))",
                            propName,
                            ClassName("kotlinx.serialization.json", "Json")
                        )
                        codeBuilder.endControlFlow()
                    }
                } else {
                    codeBuilder.beginControlFlow("%N?.let", camelName)
                    appendOptionalFormData(codeBuilder, propName, propSchema)
                    codeBuilder.endControlFlow()
                }
            }
        }

        // Append attachments if this form has binary additionalProperties
        if (hasBinaryAdditionalProperties) {
            codeBuilder.addStatement("attachments?.forEach { append(it) }")
        }

        codeBuilder.unindent()
        codeBuilder.addStatement("})")
        codeBuilder.addStatement("return %N(formData)", operationId)

        functionBuilder.addCode(codeBuilder.build())

        return functionBuilder.build()
    }

    /**
     * Appends a required form data field to the multipart form data content.
     *
     * Handles primitive types and complex objects with proper serialization.
     */
    private fun appendRequiredFormData(
        codeBuilder: CodeBlock.Builder,
        propName: String,
        camelName: String,
        propSchema: JsonNode
    ) {
        val resolvedType = determinePropertyType(propSchema, forMultipart = true, context = null)
        when (resolvedType) {
            STRING -> codeBuilder.addStatement("append(%S, %L)", propName, camelName)
            INT, LONG, FLOAT, DOUBLE, BOOLEAN -> codeBuilder.addStatement(
                "append(%S, %L.toString())",
                propName,
                camelName
            )

            else -> codeBuilder.addStatement(
                "append(%S, %T.encodeToString(%L))",
                propName,
                ClassName("kotlinx.serialization.json", "Json"),
                camelName
            )
        }
    }

    /**
     * Appends an optional form data field to the multipart form data content.
     *
     * Wraps the field in a `?.let { }` block for null safety.
     */
    private fun appendOptionalFormData(codeBuilder: CodeBlock.Builder, propName: String, propSchema: JsonNode) {
        val resolvedType = determinePropertyType(propSchema, forMultipart = true, context = null)
        when (resolvedType) {
            STRING -> codeBuilder.addStatement("append(%S, it)", propName)
            INT, LONG, FLOAT, DOUBLE, BOOLEAN -> codeBuilder.addStatement("append(%S, it.toString())", propName)
            else -> codeBuilder.addStatement(
                "append(%S, %T.encodeToString(it))",
                propName,
                ClassName("kotlinx.serialization.json", "Json")
            )
        }
    }

    /**
     * Generate an extension function that accepts the generated form wrapper class,
     * then forwards to the scattered-parameters extension.
     */
    private fun generateFormExtensionFunction(
        interfaceName: String,
        operationId: String,
        formClassName: String,
        schema: JsonNode,
        returnType: TypeName,
        description: String?
    ): FunSpec {
        val properties = schema.get("properties")

        val functionBuilder = FunSpec.builder(operationId)
            .addModifiers(KModifier.SUSPEND)
            .receiver(ClassName(BASE_PACKAGE, interfaceName))
            .addParameter(
                ParameterSpec.builder("form", ClassName(FORM_PACKAGE, formClassName))
                    .addKdoc("Multipart form object for this operation")
                    .build()
            )
            .returns(returnType)

        if (description != null) {
            functionBuilder.addKdoc(sanitizeKDoc(description))
        }

        // Build the function call - call the scattered parameter extension function
        val codeBuilder = CodeBlock.builder()
        codeBuilder.add("return %N(\n", operationId)
        codeBuilder.indent()

        val paramsList = mutableListOf<String>()
        properties?.properties()?.forEach { (propName, _) ->
            val camelName = snakeToCamelCase(propName)
            paramsList.add("$camelName = form.$camelName")
        }

        // Check for additionalProperties with binary format (dynamic file attachments)
        val additionalProperties = schema.get("additionalProperties")
        val hasBinaryAdditionalProperties = additionalProperties != null &&
                additionalProperties.get("type")?.asText() == "string" &&
                additionalProperties.get("format")?.asText() == "binary"

        if (hasBinaryAdditionalProperties) {
            paramsList.add("attachments = form.attachments")
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

    /**
     * Generate scatter extension functions for JSON body operations.
     *
     * These extension functions accept individual parameters (scattered from the Request class fields)
     * and build the Request object internally before calling the original method.
     *
     * For example, for `sendMessage(@Body body: SendMessageRequest)`:
     * ```kotlin
     * public suspend fun TelegramBotApi.sendMessage(
     *     chatId: String,
     *     text: String,
     *     parseMode: String? = null,
     *     // ... other parameters from SendMessageRequest
     * ): TelegramResponse<Message> {
     *     val request = SendMessageRequest(
     *         chatId = chatId,
     *         text = text,
     *         parseMode = parseMode,
     *         // ... other parameters
     *     )
     *     return sendMessage(request)
     * }
     * ```
     *
     * This allows users to call API methods with named parameters instead of constructing Request objects manually.
     */
    private fun generateJsonBodyExtensions(outputDir: File) {
        val fileSpec = createFileSpec(
            MODEL_PACKAGE,
            "Bodies",
            """
                    Scatter extension functions for Telegram Bot API JSON body operations.
                    
                    This file provides typed extension functions for methods that accept JSON request bodies,
                    allowing users to pass individual parameters instead of constructing Request objects manually.
                    
                    Example:
                    ```kotlin
                    // Instead of:
                    api.sendMessage(SendMessageRequest(chatId = "123", text = "Hello"))
                    
                    // You can use:
                    api.sendMessage(chatId = "123", text = "Hello")
                    ```
                    
                    These functions are auto-generated and should not be edited manually.
                   """.trimIndent()
        )
        // Add `@file:Suppress` annotation to suppress duplicate code warnings
        fileSpec.addAnnotation(
            AnnotationSpec.builder(ClassName("kotlin", "Suppress"))
                .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                .addMember("%S", "RedundantVisibilityModifier")
                .addMember("%S", "unused")
                .addMember("%S", "DuplicatedCode")
                .build()
        )

        jsonBodyOperations.forEach { (operationId, info) ->
            val requestClassName = info.requestClassName
            val requestSchema = info.requestSchema

            val scatterFunction = generateJsonBodyScatterFunction(
                operationId = operationId,
                requestClassName = requestClassName,
                requestSchema = requestSchema,
                returnType = info.returnType,
                description = info.description
            )
            fileSpec.addFunction(scatterFunction)
        }

        fileSpec.build().writeToWithUnixLineEndings(outputDir)
    }

    /**
     * Generate a scatter extension function for a JSON body operation.
     *
     * The function accepts individual parameters matching the Request class fields,
     * constructs the Request object internally, and calls the original method.
     */
    private fun generateJsonBodyScatterFunction(
        operationId: String,
        requestClassName: String,
        requestSchema: JsonNode,
        returnType: TypeName,
        description: String?
    ): FunSpec {
        val properties = requestSchema.get("properties")
        val required = requestSchema.get("required")?.map { it.asText() }?.toSet() ?: emptySet()

        val functionBuilder = FunSpec.builder(operationId)
            .addModifiers(KModifier.SUSPEND)
            .receiver(ClassName(BASE_PACKAGE, "TelegramBotApi"))
            .returns(returnType)

        if (description != null) {
            functionBuilder.addKdoc(sanitizeKDoc(description))
        }

        // Track parameter names and their expressions for the request constructor call
        val constructorArgs = mutableListOf<String>()

        // Add parameters for each property of the Request class
        properties?.properties()?.forEach { (propName, propSchema) ->
            val camelName = snakeToCamelCase(propName)
            val isRequired = required.contains(propName)
            val propType = determinePropertyType(propSchema, context = "$requestClassName.$propName")
            val finalType = if (isRequired) propType else propType.copy(nullable = true)

            val paramBuilder = ParameterSpec.builder(camelName, finalType)
            if (!isRequired) {
                paramBuilder.defaultValue("null")
            }

            // Add KDoc from the property description
            val propDescription = propSchema.get("description")?.asText()
            if (propDescription != null) {
                paramBuilder.addKdoc(sanitizeKDoc(propDescription))
            }

            functionBuilder.addParameter(paramBuilder.build())

            // Add to the constructor argument list
            constructorArgs.add("$camelName = $camelName")
        }

        // Build the function body - construct the Request object and call the original method
        val codeBuilder = CodeBlock.builder()
        codeBuilder.addStatement("val request = %T(", ClassName(MODEL_PACKAGE, requestClassName))
        codeBuilder.indent()

        constructorArgs.forEachIndexed { index, arg ->
            codeBuilder.add(arg)
            if (index < constructorArgs.size - 1) {
                codeBuilder.add(",\n")
            } else {
                codeBuilder.add("\n")
            }
        }

        codeBuilder.unindent()
        codeBuilder.addStatement(")")
        codeBuilder.addStatement("return %N(request)", operationId)

        functionBuilder.addCode(codeBuilder.build())

        return functionBuilder.build()
    }
}
