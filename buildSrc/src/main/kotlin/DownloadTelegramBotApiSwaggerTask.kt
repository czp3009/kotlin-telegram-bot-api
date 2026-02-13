import com.fasterxml.jackson.databind.ObjectMapper
import org.gradle.api.DefaultTask
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.net.URI
import javax.inject.Inject

/**
 * A Gradle task that downloads the latest Telegram Bot API OpenAPI/Swagger specification file
 * from a GitHub repository.
 *
 * This task connects to the GitHub API to fetch the latest release from the specified repository,
 * locates the target file in the release assets, and downloads it to the project's build directory.
 * The downloaded specification file is used as the input for generating Kotlin API bindings.
 *
 * @property repository The GitHub repository in format "owner/repo". Defaults to "czp3009/telegram-bot-api-swagger".
 * @property fileName The name of the file to download from the release assets. Defaults to "telegram-bot-api.json".
 * @property outputFile The destination file path where the downloaded spec will be saved.
 *                     Defaults to `build/generated/swagger/telegram-bot-api.json`.
 *
 * @constructor Injects the ProjectLayout to provide file system access for the output file configuration.
 */
abstract class DownloadTelegramBotApiSwaggerTask @Inject constructor(
    layout: ProjectLayout
) : DefaultTask() {

    /** The GitHub repository in "owner/repo" format. Defaults to "czp3009/telegram-bot-api-swagger". */
    @get:Input
    abstract val repository: Property<String>

    /** The name of the file to download from release assets. Defaults to "telegram-bot-api.json". */
    @get:Input
    abstract val fileName: Property<String>

    /** The destination file where the downloaded spec will be saved. Defaults to `build/generated/swagger/telegram-bot-api.json`. */
    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    init {
        group = "swagger"
        description = "Download telegram-bot-api.json from GitHub release"

        repository.convention("czp3009/telegram-bot-api-swagger")
        fileName.convention("telegram-bot-api.json")
        outputFile.convention(layout.buildDirectory.file(fileName.map { "generated/swagger/$it" }))
    }

    /**
     * Downloads the latest OpenAPI specification file from the GitHub repository.
     *
     * This method performs the following steps:
     * 1. Resolves the download URL by querying the GitHub API for the latest release
     * 2. Creates the parent directory for the output file if it doesn't exist
     * 3. Downloads the file from the resolved URL
     * 4. Writes the content to the output file
     *
     * @throws IllegalStateException if the target file is not found in the release assets
     */
    @TaskAction
    fun download() {
        val downloadUrl = getDownloadUrl()
        val file = outputFile.get().asFile

        logger.lifecycle("Downloading ${fileName.get()} from ${repository.get()}")

        file.parentFile.mkdirs()

        URI.create(downloadUrl).toURL().openStream().use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        logger.lifecycle("Successfully downloaded to ${file.absolutePath}")
    }

    private fun getDownloadUrl(): String {
        val apiUrl = "https://api.github.com/repos/${repository.get()}/releases/latest"
        val response = URI.create(apiUrl).toURL().readText()

        val root = ObjectMapper().readTree(response)
        val targetFileName = fileName.get()
        val downloadUrl = root["assets"]
            .firstOrNull { it["name"].asText() == targetFileName }
            ?.get("browser_download_url")
            ?.asText()

        checkNotNull(downloadUrl) { "Failed to find download URL for $targetFileName in release assets" }
        return downloadUrl
    }
}
