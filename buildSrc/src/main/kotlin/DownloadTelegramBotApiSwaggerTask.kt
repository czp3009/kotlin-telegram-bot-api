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

abstract class DownloadTelegramBotApiSwaggerTask @Inject constructor(
    layout: ProjectLayout
) : DefaultTask() {

    @get:Input
    abstract val repository: Property<String>

    @get:Input
    abstract val fileName: Property<String>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    init {
        group = "swagger"
        description = "Download telegram-bot-api.json from GitHub release"

        repository.convention("czp3009/telegram-bot-api-swagger")
        fileName.convention("telegram-bot-api.json")
        outputFile.convention(layout.buildDirectory.file(fileName.map { "generated/swagger/$it" }))
    }

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
