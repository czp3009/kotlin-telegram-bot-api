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

/**
 * Gradle task that generates Kotlin code from a Telegram Bot API OpenAPI/Swagger specification.
 *
 * This task produces type-safe, auto-generated Kotlin bindings for Telegram's REST API,
 * including data models, form classes, and Ktorfit interfaces for HTTP operations.
 *
 * ## Generated Components
 *
 * ### Data Models (`model/` package)
 * Generates serializable data classes for all Telegram API entities (Message, User, Chat, etc.)
 * with proper type handling including:
 * - Union types (oneOf) → sealed interfaces with `@JsonClassDiscriminator`
 * - Nested objects and arrays
 * - `@SerialName` annotations for snake_case to camelCase mapping
 * - Automatic interface inheritance for `ReplyMarkup` and `IncomingUpdate` types
 *
 * ### Form Data Classes (`form/` package)
 * Generates wrapper classes for multipart form data requests where file uploads are required.
 * These classes convert `InputFile` references to `FormPart<ChannelProvider>` for Ktor's multipart handling.
 * Additional properties with binary format generate an `attachments` parameter for dynamic file attachments
 * referenced via `attach://<file_attach_name>` in media fields.
 *
 * Also generates `Forms.kt` containing extension functions that build `MultiPartFormDataContent` from
 * form objects and call the corresponding API methods.
 *
 * ### Ktorfit Interface (`TelegramBotApi.kt`)
 * Generates the main `TelegramBotApi` interface with Ktorfit annotations:
 * - `@GET` for methods without request bodies
 * - `@POST` for methods with request bodies
 * - `@Body` for JSON request bodies
 * - `@Query` for URL query parameters
 *
 * ## Type Handling
 *
 * The generator handles several special type patterns from the OpenAPI specification:
 *
 * - **ReplyMarkup union types**: Dynamically collected from the specification; all types
 *   implement a `ReplyMarkup` sealed interface with `@JsonClassDiscriminator("type")`
 *
 * - **IncomingUpdate types**: Types used in `Update`'s optional non-primitive fields
 *   implement `IncomingUpdate` interface for polymorphic update handling
 *
 * - **Field-overlapping unions**: For unions like `MaybeInaccessibleMessage` where subclasses
 *   share >70% of properties, the largest subclass (e.g., `Message`) is used for deserialization
 *
 * - **Complete discriminator mappings**: When OpenAPI discriminator covers all oneOf members,
 *   generates a sealed interface with `@JsonClassDiscriminator` and subclasses with `@SerialName`
 *
 * - **Incomplete discriminator mappings**: When discriminator mapping doesn't cover all oneOf members
 *   (e.g., InlineQueryResult with 20 members but 13 in mapping), generates an empty sealed interface
 *   and standalone classes (no inheritance) with `@SerialName` annotations and discriminator field
 *
 * - **Discriminator value extraction**: Attempts multiple strategies to find discriminator values:
 *   1. Explicit discriminator.mapping from OpenAPI spec
 *   2. Single-value enum fields in member schemas
 *   3. Description patterns: `always "value"`, `must be "value"`, `must be *value*`
 *   4. Fallback inference from class names (e.g., BackgroundFillSolid → "solid")
 *
 * ## Configuration
 *
 * The task requires two inputs:
 * - `swaggerFile`: Path to the OpenAPI/Swagger JSON specification file
 * - `outputDir`: Directory where generated Kotlin code will be written (defaults to `src/commonMain/kotlin`)
 *
 * ## Clean Build
 *
 * Before generation, the task cleans specific output directories:
 * - Deletes `model/` and `form/` directories completely
 * - Deletes `TelegramBotApi.kt` interface file
 * - Preserves `type/` directory which contains handwritten code (`TelegramResponse`, `IncomingUpdate`)
 */
abstract class GenerateKtorfitInterfacesTask : DefaultTask() {
    companion object {
        // Package names
        private const val BASE_PACKAGE = "com.hiczp.telegram.bot.api"
        private const val MODEL_PACKAGE = "$BASE_PACKAGE.model"
        private const val FORM_PACKAGE = "$BASE_PACKAGE.form"
        private const val TYPE_PACKAGE = "$BASE_PACKAGE.type"
        private const val PLUGIN_PACKAGE = "$BASE_PACKAGE.plugin"
        private const val KTORFIT_HTTP_PACKAGE = "de.jensklingenberg.ktorfit.http"
        private const val KTOR_STATEMENT_PACKAGE = "io.ktor.client.statement"

        // Type names
        private const val TELEGRAM_RESPONSE_TYPE = "TelegramResponse"
        private const val REPLY_MARKUP_INTERFACE = "ReplyMarkup"
        private const val INCOMING_UPDATE_INTERFACE = "IncomingUpdate"
        private const val INPUT_FILE_TYPE = "InputFile"
        private const val MESSAGE_TYPE = "Message"
        private const val MAYBE_INACCESSIBLE_MESSAGE_TYPE = "MaybeInaccessibleMessage"
        private const val TELEGRAM_FILE_DOWNLOAD_ANNOTATION = "TelegramFileDownload"
        private const val HTTP_STATEMENT_TYPE = "HttpStatement"

        // Ktorfit HTTP annotation names
        private const val GET_ANNOTATION = "GET"
        private const val STREAMING_ANNOTATION = "Streaming"
        private const val PATH_ANNOTATION = "Path"

        // Content types
        private const val CONTENT_TYPE_JSON = "application/json"
        private const val CONTENT_TYPE_MULTIPART = "multipart/form-data"
    }

    /**
     * Input property for the OpenAPI/Swagger specification file.
     *
     * This file defines the Telegram Bot API structure including all endpoints, request/response schemas,
     * and data models. The task parses this specification to generate corresponding Kotlin code.
     */
    @get:InputFile
    abstract val swaggerFile: RegularFileProperty

    /**
     * Output directory for generated Kotlin code.
     *
     * Generated files are organized under this directory:
     * - `com/hiczp/telegram/bot/api/model/` - Data model classes (100+ files)
     * - `com/hiczp/telegram/bot/api/form/` - Form wrapper classes (*Form.kt) and Forms.kt extension functions
     * - `com/hiczp/telegram/bot/api/TelegramBotApi.kt` - Ktorfit HTTP interface
     *
     * The `type/` subdirectory is preserved as it contains handwritten code:
     * - `IncomingUpdate.kt` - Marker interface for Update field types
     * - `InputFile.kt` - Input file handling
     * - `TelegramResponse.kt` - Response wrapper with error handling
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

    private data class MultipartOperationInfo(val schema: JsonNode, val returnType: TypeName)

    private val multipartOperations = mutableMapOf<String, MultipartOperationInfo>() // operationId -> info
    private val replyMarkupTypes = mutableSetOf<String>() // Collected ReplyMarkup types
    private val updateFieldTypes = mutableSetOf<String>() // Types used in Update's optional non-primitive fields

    /**
     * Main entry point for code generation from the OpenAPI specification.
     *
     * This method orchestrates the entire code generation process:
     *
     * 1. **Parses the specification**: Reads the OpenAPI/Swagger JSON file using Jackson ObjectMapper
     * 2. **Cleans output directories**: Deletes `model/`, `form/` directories and `TelegramBotApi.kt` file
     * 3. **Stores all schemas**: Caches all schema definitions for reference during generation
     * 4. **Collects ReplyMarkup types**: Scans all `reply_markup` fields to identify types for sealed interface
     * 5. **Collects IncomingUpdate types**: Identifies types used in `Update`'s optional non-primitive fields
     * 6. **Generates ReplyMarkup interface**: Creates sealed interface if any ReplyMarkup types were found
     * 7. **Generates data models**: Creates all data classes for API entities (excluding InputFile)
     * 8. **Generates API interface**: Creates the Ktorfit HTTP interface with all endpoints
     *
     * ## Output Structure
     *
     * Generated files are written to the configured output directory:
     * ```
     * com/hiczp/telegram/bot/api/
     * ├── TelegramBotApi.kt           # Ktorfit HTTP interface with all API methods
     * ├── form/                       # Form data wrapper classes for multipart uploads
     * │   ├── *Form.kt                # Classes with InputFile → FormPart<ChannelProvider> conversion
     * │   └── Forms.kt                # Extension functions for multipart operations
     * ├── model/                      # Data model classes (100+ files)
     * │   ├── ReplyMarkup.kt          # Sealed interface with @JsonClassDiscriminator
     * │   └── *.kt                    # All other data classes and sealed interfaces
     * └── type/                       # Handwritten code (preserved, not regenerated)
     *     ├── IncomingUpdate.kt       # Marker interface for Update field types
     *     ├── InputFile.kt            # Input file handling
     *     └── TelegramResponse.kt     # Response wrapper with error handling
     * ```
     *
     * ## Type Collection Strategy
     *
     * - **ReplyMarkup types**: Collects from all `reply_markup` fields in request bodies and schemas,
     *   extracting type names from oneOf/allOf $ref arrays
     *
     * - **IncomingUpdate types**: Collects from all optional (non-required) non-primitive fields in
     *   the `Update` schema, excluding `MaybeInaccessibleMessage`
     *
     * ## Clean Build Strategy
     *
     * Before generating new code, the task cleans specific output locations:
     * - Deletes `model/` directory completely (all generated data classes)
     * - Deletes `form/` directory completely (all generated form classes)
     * - Deletes `TelegramBotApi.kt` interface file
     * - **Preserves** `type/` directory which contains handwritten code
     *
     * @throws Exception if specification parsing or code generation fails
     */
    @TaskAction
    fun generate() {
        val swagger = ObjectMapper().readTree(swaggerFile.get().asFile)
        val outputDirectory = outputDir.get().asFile

        logger.lifecycle("Generating Ktorfit interfaces from ${swaggerFile.get().asFile.name}")

        // Clean output directory (except the type directory which contains handwritten TelegramResponse)
        val apiDir = File(outputDirectory, "com/hiczp/telegram/bot/api")
        val modelDir = File(apiDir, "model")
        val formDir = File(apiDir, "form")

        // Delete model and form directories, but keep the type directory
        modelDir.deleteRecursively()
        formDir.deleteRecursively()

        // Delete the API interface file
        File(apiDir, "TelegramBotApi.kt").delete()

        // Create directories
        modelDir.mkdirs()
        formDir.mkdirs()

        // Store all schemas for reference
        allSchemas = swagger.get("components")?.get("schemas")?.fields()?.asSequence()
            ?.associate { it.key to it.value } ?: emptyMap()

        // Collect all ReplyMarkup types from the swagger spec
        collectReplyMarkupTypes(swagger)
        logger.lifecycle("Detected ReplyMarkup types: $replyMarkupTypes")

        // Collect all types used in Update's optional non-primitive fields
        collectUpdateFieldTypes()
        logger.lifecycle("Detected Update field types: $updateFieldTypes")

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


    /**
     * Collects all types that appear in `reply_markup` fields across the entire OpenAPI specification.
     *
     * This method scans:
     * 1. All request bodies in API paths (both JSON and multipart content types)
     * 2. All schemas with `reply_markup` properties
     *
     * For each `reply_markup` field found, extracts type names from:
     * - oneOf arrays (multiple possible types)
     * - allOf arrays (single type references)
     *
     * The collected types are stored in `replyMarkupTypes` and used to generate the `ReplyMarkup` sealed interface.
     */
    private fun collectReplyMarkupTypes(swagger: JsonNode) {
        val paths = swagger.get("paths") ?: return

        paths.fields().forEach { (_, methods) ->
            methods.fields().forEach { (_, operation) ->
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
        val updateSchema = allSchemas["Update"] ?: return
        val properties = updateSchema.get("properties") ?: return
        val required = updateSchema.get("required")?.map { it.asText() }?.toSet() ?: emptySet()

        properties.fields().forEach { (propName, propSchema) ->
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
     * Form classes don't get @Serializable because they contain Ktor-specific types
     * (FormPart<ChannelProvider>) that aren't serializable.
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
            addSuperinterface(ClassName(TYPE_PACKAGE, INCOMING_UPDATE_INTERFACE))
        }
        return this
    }

    /**
     * Adds KDoc documentation to a type builder if the description is present.
     *
     * The description is sanitized to ensure valid KDoc format (escaping special characters, etc.).
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
     * Finds a parent type in allSchemas that has a oneOf containing all the given refs.
     */
    private fun findParentTypeForOneOf(refs: List<String>): String? {
        val refsSet = refs.toSet()

        for ((schemaName, schema) in allSchemas) {
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
     * Creates a constructor parameter builder with optional default value.
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

        schemas.fields().forEach { (schemaName, schemaNode) ->
            if (schemaName in processedSchemas) return@forEach

            // Skip InputFile - it's hardcoded as String
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

            // Check if this is a field-overlapping case (use the largest subclass)
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
                if (properties == null || !properties.isObject || !properties.fields().hasNext()) {
                    val objectBuilder = TypeSpec.objectBuilder(className)
                        .configureTypeBuilder(className, description, isFormClass)

                    fileSpec.addType(objectBuilder.build())
                } else {
                    val classBuilder = TypeSpec.classBuilder(className)
                        .addModifiers(KModifier.DATA)
                        .configureTypeBuilder(className, description, isFormClass)

                    val required = schema.get("required")?.map { it.asText() }?.toSet() ?: emptySet()
                    val constructorBuilder = FunSpec.constructorBuilder()

                    properties.fields().forEach { (propName, propSchema) ->
                        // For Form classes, convert file types to PartData
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
                                ClassName("io.ktor.client.request.forms", "FormPart")
                                    .parameterizedBy(ClassName("io.ktor.client.request.forms", "ChannelProvider"))
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

    private fun shouldUseLargestSubclass(parentName: String, unionMembers: List<String>): Boolean {
        // Special handling for MaybeInaccessibleMessage: use Message as a deserialization type
        // but keep InaccessibleMessage as a separate serializable class
        if (parentName == MAYBE_INACCESSIBLE_MESSAGE_TYPE) return true

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
            mapping.fields().forEach { (discriminatorValue, ref) ->
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
            classBuilder.addSuperinterface(ClassName(TYPE_PACKAGE, INCOMING_UPDATE_INTERFACE))
        }

        val constructorBuilder = FunSpec.constructorBuilder()

        info.properties.fields().forEach { (propName, propSchema) ->
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
            classBuilder.addSuperinterface(ClassName(TYPE_PACKAGE, INCOMING_UPDATE_INTERFACE))
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
        info.properties.fields().forEach { (propName, propSchema) ->
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
            classBuilder.addSuperinterface(ClassName(TYPE_PACKAGE, INCOMING_UPDATE_INTERFACE))
        }

        val constructorBuilder = FunSpec.constructorBuilder()

        info.properties.fields().forEach { (propName, propSchema) ->
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
     * @param forMultipart If true, InputFile types will be converted to PartData
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
            return ClassName("io.ktor.client.request.forms", "ChannelProvider")
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
                    if (context != null) {
                        logger.warn("Using JsonElement for $context: allOf has no refs")
                    }
                    ClassName("kotlinx.serialization.json", "JsonElement").copy(nullable = true)
                }
            }

            // Handle oneOf - union types
            typeInfo.oneOf != null && typeInfo.oneOf.isArray -> determineOneOfType(typeInfo.oneOf, context)

            // Handle arrays
            typeInfo.type == "array" -> {
                val items = schema.get("items")
                val itemType = if (items == null || items.isNull) {
                    if (context != null) {
                        logger.warn("Using JsonElement for $context: array has no items definition")
                    }
                    ClassName("kotlinx.serialization.json", "JsonElement")
                } else {
                    // For multipart arrays, check if items are InputFile
                    if (forMultipart) {
                        val itemTypeInfo = extractSchemaTypeInfo(items)
                        if (isInputFileType(itemTypeInfo.ref, itemTypeInfo.oneOf, itemTypeInfo.allOf)) {
                            return LIST.parameterizedBy(ClassName("io.ktor.client.request.forms", "ChannelProvider"))
                        }
                    }
                    val determinedType = determinePropertyType(items, forMultipart, context?.let { "$it[]" })
                    if (determinedType == ANY.copy(nullable = true)) {
                        if (context != null) {
                            logger.warn("Using JsonElement for $context: array item type resolved to Any")
                        }
                        ClassName("kotlinx.serialization.json", "JsonElement")
                    } else {
                        determinedType
                    }
                }
                LIST.parameterizedBy(itemType)
            }

            // Handle inline objects (no concrete type available)
            typeInfo.type == "object" -> {
                if (context != null) {
                    logger.warn("Using JsonElement for $context: inline object without concrete type")
                }
                ClassName("kotlinx.serialization.json", "JsonElement").copy(nullable = true)
            }

            // Handle primitive types
            typeInfo.type == "string" -> STRING
            typeInfo.type == "integer" -> {
                when (typeInfo.format) {
                    "int64" -> LONG
                    else -> INT
                }
            }

            typeInfo.type == "number" -> {
                when (typeInfo.format) {
                    "float" -> FLOAT
                    else -> DOUBLE
                }
            }

            typeInfo.type == "boolean" -> BOOLEAN

            // Fallback
            else -> {
                if (context != null) {
                    logger.warn("Using JsonElement for $context: unknown schema type '${typeInfo.type}'")
                }
                ClassName("kotlinx.serialization.json", "JsonElement").copy(nullable = true)
            }
        }
    }

    /**
     * Resolve a type name to a TypeName, handling special cases like MaybeInaccessibleMessage and InputFile.
     */
    private fun resolveTypeName(typeName: String): TypeName {
        return when (typeName) {
            MAYBE_INACCESSIBLE_MESSAGE_TYPE -> ClassName(MODEL_PACKAGE, MESSAGE_TYPE)
            INPUT_FILE_TYPE -> STRING  // InputFile is hardcoded to String, no class generated
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

        // Parse paths
        val paths = swagger.get("paths") ?: return
        paths.fields().forEach { (path, methods) ->
            methods.fields().forEach { (method, operation) ->
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

        // Add downloadFile method at the end
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
                addParameter(functionBuilder, param)
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
                            ClassName("io.ktor.client.request.forms", "MultiPartFormDataContent")
                        ).addAnnotation(
                            AnnotationSpec.builder(ClassName("de.jensklingenberg.ktorfit.http", "Body"))
                                .build()
                        ).build()
                    )
                    .build()

                // Store schema and return type for later extension function generation
                multipartOperations[operationId] = MultipartOperationInfo(schema, returnType)

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
        }

        return listOf(functionBuilder.build())
    }

    /**
     * Convert InputFile types to PartData for multipart operations.
     * For other types, delegates to the unified determinePropertyType function.
     */
    private fun convertToPartDataIfNeeded(schema: JsonNode, context: String? = null): TypeName {
        val typeInfo = extractSchemaTypeInfo(schema)

        // Check if this is an InputFile type - return FormPart<ChannelProvider>
        if (isInputFileType(typeInfo.ref, typeInfo.oneOf, typeInfo.allOf)) {
            val formPartClass = ClassName("io.ktor.client.request.forms", "FormPart")
            val channelProviderClass = ClassName("io.ktor.client.request.forms", "ChannelProvider")
            return formPartClass.parameterizedBy(channelProviderClass)
        }

        // Check if this is an array of InputFile - return List<FormPart<ChannelProvider>>
        if (typeInfo.type == "array") {
            val items = schema.get("items")
            if (items != null) {
                val itemTypeInfo = extractSchemaTypeInfo(items)
                if (isInputFileType(itemTypeInfo.ref, itemTypeInfo.oneOf, itemTypeInfo.allOf)) {
                    val formPartClass = ClassName("io.ktor.client.request.forms", "FormPart")
                    val channelProviderClass = ClassName("io.ktor.client.request.forms", "ChannelProvider")
                    return LIST.parameterizedBy(formPartClass.parameterizedBy(channelProviderClass))
                }
            }
        }

        // For non-file types, use normal property type determination
        return determinePropertyType(schema, forMultipart = false, context = context)
    }

    private fun addParameter(functionBuilder: FunSpec.Builder, param: JsonNode) {
        val name = param.get("name")?.asText() ?: return
        val paramIn = param.get("in")?.asText() ?: return
        val required = param.get("required")?.asBoolean() ?: false
        val schema = param.get("schema")
        val description = param.get("description")?.asText()

        val paramType = determinePropertyType(schema, context = "parameter.$name")
        val finalType = if (required) paramType else paramType.copy(nullable = true)
        val camelName = snakeToCamelCase(name)

        val paramBuilder = ParameterSpec.builder(camelName, finalType)

        // Add annotation based on a parameter location
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

        if (description != null) {
            paramBuilder.addKdoc(sanitizeKDoc(description))
        }

        functionBuilder.addParameter(paramBuilder.build())
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
     * Form classes are generated in the form package and use PartData for InputFile types.
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

        if (properties != null && properties.isObject && properties.fields().hasNext()) {
            val classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(KModifier.DATA)

            val constructorBuilder = FunSpec.constructorBuilder()

            properties.fields().forEach { (propName, propSchema) ->
                val isRequired = required.contains(propName)
                // For form classes, convert InputFile types to PartData
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

            // Add attachments field if additionalProperties has binary format
            if (hasBinaryAdditionalProperties) {
                val attachmentsType = LIST.parameterizedBy(
                    ClassName("io.ktor.client.request.forms", "FormPart")
                        .parameterizedBy(ClassName("io.ktor.client.request.forms", "ChannelProvider"))
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
     * Generate extension functions for multipart operations that accept Form data classes
     */
    @Suppress("SameParameterValue")
    private fun generateMultipartExtensions(packageName: String, interfaceName: String, outputDir: File) {
        val fileSpec = createFileSpec(
            "$packageName.form",
            "Forms",
            """
                    Extension functions for Telegram Bot API multipart operations.
    
                    This file provides three types of convenient functions for operations that require
                    multipart form data with file uploads:
                    
                    1. **Scatter functions**: Accept individual parameters as named arguments
                       Example: `api.sendPhoto(chatId = 123, photo = fileChannel)`
                    
                    2. **Bridge functions**: Accept String file refs and convert to FormPart
                       Example: `api.sendPhoto(chatId = 123, photo = "fileId")`
                    
                    3. **Form functions**: Accept pre-constructed form wrapper classes
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
                info.returnType
            )
            fileSpec.addFunction(scatteredExtensionFunction)

            // 2. Generate bridge extension function (converts String to FormPart and calls the scattered extension)
            val bridgeExtensionFunction = generateBridgeExtensionFunction(
                interfaceName,
                operationId,
                info.schema,
                info.returnType
            )
            if (bridgeExtensionFunction != null) {
                fileSpec.addFunction(bridgeExtensionFunction)
            }

            // 3. Generate Form-based extension function (calls the scattered parameters extension)
            val formExtensionFunction = generateFormExtensionFunction(
                interfaceName,
                operationId,
                formClassName,
                info.schema,
                info.returnType
            )
            fileSpec.addFunction(formExtensionFunction)
        }

        fileSpec.build().writeToWithUnixLineEndings(outputDir)
    }

    /**
     * Generate an extension function with scattered parameters that builds MultiPartFormDataContent
     * and calls the interface method.
     */
    private fun generateScatteredParametersExtensionFunction(
        interfaceName: String,
        operationId: String,
        schema: JsonNode,
        returnType: TypeName
    ): FunSpec {
        val properties = schema.get("properties")
        val required = schema.get("required")?.map { it.asText() }?.toSet() ?: emptySet()

        val functionBuilder = FunSpec.builder(operationId)
            .addModifiers(KModifier.SUSPEND)
            .receiver(ClassName(BASE_PACKAGE, interfaceName))
            .returns(returnType)

        // Add parameters for each property
        properties?.fields()?.forEach { (propName, propSchema) ->
            val camelName = snakeToCamelCase(propName)
            val isRequired = required.contains(propName)
            val propType = convertToPartDataIfNeeded(propSchema, "$operationId.$propName")
            val finalType = if (isRequired) propType else propType.copy(nullable = true)

            val paramBuilder = ParameterSpec.builder(camelName, finalType)
            if (!isRequired) {
                paramBuilder.defaultValue("null")
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
                ClassName("io.ktor.client.request.forms", "FormPart")
                    .parameterizedBy(ClassName("io.ktor.client.request.forms", "ChannelProvider"))
            ).copy(nullable = true)

            val attachmentsParam = ParameterSpec.builder("attachments", attachmentsType)
                .defaultValue("null")
                .build()
            functionBuilder.addParameter(attachmentsParam)
        }

        // Build the function body - construct MultiPartFormDataContent and call the interface method
        val codeBuilder = CodeBlock.builder()
        codeBuilder.addStatement(
            "val formData = %T(%T {",
            ClassName("io.ktor.client.request.forms", "MultiPartFormDataContent"),
            ClassName("io.ktor.client.request.forms", "formData")
        )
        codeBuilder.indent()

        // Add each parameter to the form data, skipping null values
        properties?.fields()?.forEach { (propName, propSchema) ->
            val camelName = snakeToCamelCase(propName)
            val isRequired = required.contains(propName)
            val typeInfo = extractSchemaTypeInfo(propSchema)
            val isInputFile = isInputFileType(typeInfo.ref, typeInfo.oneOf, typeInfo.allOf)
            val isArray = typeInfo.type == "array"

            if (isRequired) {
                // Required parameter - add directly
                if (isInputFile) {
                    // Since FormPart is now the parameter type, append it directly
                    codeBuilder.addStatement("append(%L)", camelName)
                } else if (isArray) {
                    // Check if array items are InputFile
                    val items = propSchema.get("items")
                    val itemTypeInfo = extractSchemaTypeInfo(items)
                    val isInputFileArray = isInputFileType(itemTypeInfo.ref, itemTypeInfo.oneOf, itemTypeInfo.allOf)
                    if (isInputFileArray) {
                        // Since the parameter is now List<FormPart>, append each FormPart directly
                        codeBuilder.addStatement("%L.forEach { append(it) }", camelName)
                    } else {
                        codeBuilder.addStatement(
                            "append(%S, %T.encodeToString(%L))",
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
                    // Since FormPart is now the parameter type, append it directly if not null
                    codeBuilder.addStatement("%L?.let { append(it) }", camelName)
                } else if (isArray) {
                    val items = propSchema.get("items")
                    val itemTypeInfo = extractSchemaTypeInfo(items)
                    val isInputFileArray = isInputFileType(itemTypeInfo.ref, itemTypeInfo.oneOf, itemTypeInfo.allOf)
                    if (isInputFileArray) {
                        // Since the parameter is now List<FormPart>?, append each FormPart directly if not null
                        codeBuilder.addStatement("%L?.forEach { append(it) }", camelName)
                    } else {
                        codeBuilder.beginControlFlow("%L?.let", camelName)
                        codeBuilder.addStatement(
                            "append(%S, %T.encodeToString(it))",
                            propName,
                            ClassName("kotlinx.serialization.json", "Json")
                        )
                        codeBuilder.endControlFlow()
                    }
                } else {
                    codeBuilder.beginControlFlow("%L?.let", camelName)
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
     * Generates a bridge extension function with String parameters that converts them to FormPart
     * and calls the scattered parameters extension.
     */
    private fun generateBridgeExtensionFunction(
        interfaceName: String,
        operationId: String,
        schema: JsonNode,
        returnType: TypeName
    ): FunSpec? {
        val properties = schema.get("properties")
        val required = schema.get("required")?.map { it.asText() }?.toSet() ?: emptySet()

        // Check if there are any file parameters
        var hasFileParameters = false
        properties?.fields()?.forEach { (_, propSchema) ->
            val typeInfo = extractSchemaTypeInfo(propSchema)
            val isInputFile = isInputFileType(typeInfo.ref, typeInfo.oneOf, typeInfo.allOf)
            if (isInputFile) {
                hasFileParameters = true
                return@forEach
            }
            if (typeInfo.type == "array") {
                val items = propSchema.get("items")
                val itemTypeInfo = extractSchemaTypeInfo(items)
                if (isInputFileType(itemTypeInfo.ref, itemTypeInfo.oneOf, itemTypeInfo.allOf)) {
                    hasFileParameters = true
                    return@forEach
                }
            }
        }

        // If no file parameters, don't generate the bridge function
        if (!hasFileParameters) {
            return null
        }

        val functionBuilder = FunSpec.builder(operationId)
            .addModifiers(KModifier.SUSPEND)
            .receiver(ClassName("com.hiczp.telegram.bot.api", interfaceName))
            .returns(returnType)

        // Collect parameter information
        val params = mutableListOf<Triple<String, JsonNode, Boolean>>()
        properties?.fields()?.forEach { (propName, propSchema) ->
            params.add(Triple(propName, propSchema, required.contains(propName)))
        }

        // Add parameters for each property, replacing FormPart with String
        params.forEach { (propName, propSchema, isRequired) ->
            val camelName = snakeToCamelCase(propName)
            val typeInfo = extractSchemaTypeInfo(propSchema)
            val isInputFile = isInputFileType(typeInfo.ref, typeInfo.oneOf, typeInfo.allOf)
            val isArray = typeInfo.type == "array"

            val propType = if (isInputFile) {
                // Replace FormPart<ChannelProvider> with String
                STRING
            } else if (isArray) {
                val items = propSchema.get("items")
                val itemTypeInfo = extractSchemaTypeInfo(items)
                val isInputFileArray = isInputFileType(itemTypeInfo.ref, itemTypeInfo.oneOf, itemTypeInfo.allOf)
                if (isInputFileArray) {
                    // Replace List<FormPart<ChannelProvider>> with List<String>
                    LIST.parameterizedBy(STRING)
                } else {
                    convertToPartDataIfNeeded(propSchema, "$operationId.$propName")
                }
            } else {
                convertToPartDataIfNeeded(propSchema, "$operationId.$propName")
            }

            val finalType = if (isRequired) propType else propType.copy(nullable = true)
            val paramBuilder = ParameterSpec.builder(camelName, finalType)
            if (!isRequired) {
                paramBuilder.defaultValue("null")
            }
            functionBuilder.addParameter(paramBuilder.build())
        }

        // Build the function body - convert String to FormPart and call the original extension
        val codeBuilder = CodeBlock.builder()
        codeBuilder.add("return %N(\n", operationId)
        codeBuilder.indent()

        val isFirst = mutableListOf(true)
        params.forEach { (propName, propSchema, isRequired) ->
            val camelName = snakeToCamelCase(propName)
            val typeInfo = extractSchemaTypeInfo(propSchema)
            val isInputFile = isInputFileType(typeInfo.ref, typeInfo.oneOf, typeInfo.allOf)
            val isArray = typeInfo.type == "array"

            if (!isFirst[0]) {
                codeBuilder.add(",\n")
            }
            isFirst[0] = false

            codeBuilder.add("%N = ", camelName)

            if (isInputFile) {
                // Convert String to FormPart<ChannelProvider>
                if (isRequired) {
                    codeBuilder.add(
                        "%T(%S, %T { %T(%N) })",
                        ClassName("io.ktor.client.request.forms", "FormPart"),
                        propName,
                        ClassName("io.ktor.client.request.forms", "ChannelProvider"),
                        ClassName("io.ktor.utils.io", "ByteReadChannel"),
                        camelName
                    )
                } else {
                    codeBuilder.add(
                        "%L?.let { %T(%S, %T { %T(it) }) }",
                        camelName,
                        ClassName("io.ktor.client.request.forms", "FormPart"),
                        propName,
                        ClassName("io.ktor.client.request.forms", "ChannelProvider"),
                        ClassName("io.ktor.utils.io", "ByteReadChannel")
                    )
                }
            } else if (isArray) {
                val items = propSchema.get("items")
                val itemTypeInfo = extractSchemaTypeInfo(items)
                val isInputFileArray = isInputFileType(itemTypeInfo.ref, itemTypeInfo.oneOf, itemTypeInfo.allOf)
                if (isInputFileArray) {
                    // Convert List<String> to List<FormPart<ChannelProvider>>
                    if (isRequired) {
                        codeBuilder.add(
                            "%L.map { %T(%S, %T { %T(it) }) }",
                            camelName,
                            ClassName("io.ktor.client.request.forms", "FormPart"),
                            propName,
                            ClassName("io.ktor.client.request.forms", "ChannelProvider"),
                            ClassName("io.ktor.utils.io", "ByteReadChannel")
                        )
                    } else {
                        codeBuilder.add(
                            "%L?.map { %T(%S, %T { %T(it) }) }",
                            camelName,
                            ClassName("io.ktor.client.request.forms", "FormPart"),
                            propName,
                            ClassName("io.ktor.client.request.forms", "ChannelProvider"),
                            ClassName("io.ktor.utils.io", "ByteReadChannel")
                        )
                    }
                } else {
                    codeBuilder.add("%L", camelName)
                }
            } else {
                codeBuilder.add("%L", camelName)
            }
        }

        codeBuilder.add("\n")
        codeBuilder.unindent()
        codeBuilder.add(")\n")
        functionBuilder.addCode(codeBuilder.build())

        return functionBuilder.build()
    }

    private fun generateFormExtensionFunction(
        interfaceName: String,
        operationId: String,
        formClassName: String,
        schema: JsonNode,
        returnType: TypeName
    ): FunSpec {
        val properties = schema.get("properties")

        val functionBuilder = FunSpec.builder(operationId)
            .addModifiers(KModifier.SUSPEND)
            .receiver(ClassName(BASE_PACKAGE, interfaceName))
            .addParameter("form", ClassName(FORM_PACKAGE, formClassName))
            .returns(returnType)

        // Build the function call - call the scattered parameter extension function
        val codeBuilder = CodeBlock.builder()
        codeBuilder.add("return %N(\n", operationId)
        codeBuilder.indent()

        val paramsList = mutableListOf<String>()
        properties?.fields()?.forEach { (propName, _) ->
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
}
