package com.atls.generator.utils

import org.gradle.api.Project
import org.gradle.api.logging.Logger
import java.io.File
import java.io.IOException

abstract class ModuleGenerator(
    protected val project: Project,
    protected val modulePath: String,
    protected val organization: String
) {
    protected abstract val gradleTemplatePath: String
    protected abstract val kotlinTemplatePath: String

    protected val logger: Logger = project.logger
    private val pathComponents: List<String>
    private val moduleName: String
    private val parentDirs: List<String>

    init {
        require(modulePath.isNotBlank()) { "Module path cannot be blank" }
        require(organization.isNotBlank()) { "Organization cannot be blank" }

        pathComponents = modulePath.split(":").filter { it.isNotBlank() }
        require(pathComponents.isNotEmpty()) { "Module path cannot be empty" }
        require(pathComponents.all { it.isNotBlank() }) { "Module path components cannot be blank" }

        moduleName = pathComponents.last()
        parentDirs = pathComponents.dropLast(1)
    }

    fun generate() {
        try {
            val moduleDir = resolveModuleDir()
            validateModuleDoesNotExist(moduleDir)

            createDirectoryStructure(moduleDir)
            generateGradleFile(moduleDir)
            generateKotlinFiles(moduleDir)
            addGitignore(moduleDir)
            addToSettingsGradle()

            logger.lifecycle("Created module '$modulePath' at ${moduleDir.relativeTo(project.rootDir)}")
        } catch (e: Exception) {
            logger.error("Failed to generate module '$modulePath'", e)
            throw e
        }
    }

    private fun resolveModuleDir(): File {
        return parentDirs.fold(project.rootDir) { dir, name ->
            File(dir, name).also {
                if (!it.exists() && !it.mkdirs()) {
                    throw IOException("Failed to create directory: ${it.absolutePath}")
                }
            }
        }.resolve(moduleName)
    }

    private fun validateModuleDoesNotExist(moduleDir: File) {
        if (moduleDir.exists()) {
            throw IllegalStateException(
                "Module '$modulePath' already exists at ${moduleDir.relativeTo(project.rootDir)}"
            )
        }
    }

    protected open fun createDirectoryStructure(moduleDir: File) {
        val kotlinPath = "src/commonMain/kotlin/${organization.replace(".", "/")}/$moduleName"
        File(moduleDir, kotlinPath).mkdirs().also { success ->
            if (!success) {
                throw IOException("Failed to create directory structure in ${moduleDir.absolutePath}")
            }
        }
    }

    protected open fun generateGradleFile(moduleDir: File) {
        try {
            val template = readResource(gradleTemplatePath)
                .replace("{{PACKAGE}}", "$organization.$moduleName")
                .replace("{{BASENAME}}", "${moduleName}Kit")

            File(moduleDir, "build.gradle.kts").writeText(template)
        } catch (e: Exception) {
            throw IllegalStateException("Failed to generate Gradle file", e)
        }
    }

    protected open fun generateKotlinFiles(moduleDir: File) {
        try {
            val template = readResource(kotlinTemplatePath)
                .replace("{{PACKAGE}}", "$organization.$moduleName")
                .replace("{{MODULENAME}}", moduleName)

            val kotlinPath = "src/commonMain/kotlin/${organization.replace(".", "/")}/$moduleName"
            File(moduleDir, "$kotlinPath/Greeting.kt").writeText(template)
        } catch (e: Exception) {
            throw IllegalStateException("Failed to generate Kotlin files", e)
        }
    }

    private fun addGitignore(moduleDir: File) {
        try {
            val gitignoreContent = """
                /build
            """.trimIndent()

            File(moduleDir, ".gitignore").writeText(gitignoreContent)
            logger.debug("Added .gitignore to ${moduleDir.relativeTo(project.rootDir)}")
        } catch (e: Exception) {
            logger.warn("Failed to add .gitignore file", e)
        }
    }

    private fun addToSettingsGradle() {
        val settingsFile = project.rootDir.resolve("settings.gradle.kts")
        if (!settingsFile.exists()) {
            logger.warn("settings.gradle.kts not found in project root")
            return
        }

        val includePath = ":" + modulePath.replace(":", ":")
        val includeStatement = "include(\"$includePath\")"

        try {
            val content = settingsFile.readText()
            val pattern = Regex("""include\s*\(["']$includePath["']\)""")
            if (pattern.containsMatchIn(content)) {
                logger.lifecycle("Module '$includePath' is already included in settings.gradle.kts")
                return
            }

            val newContent = if (content.isNotBlank() && !content.endsWith("\n")) {
                "$content\n$includeStatement\n"
            } else {
                "$content$includeStatement\n"
            }

            settingsFile.writeText(newContent)
            logger.lifecycle("Added to settings.gradle.kts: $includeStatement")
        } catch (e: Exception) {
            throw IllegalStateException("Failed to update settings.gradle.kts", e)
        }
    }

    protected fun readResource(path: String): String {
        return javaClass.classLoader.getResourceAsStream(path)?.use { stream ->
            stream.bufferedReader().use { it.readText() }
        } ?: throw IllegalStateException("Resource '$path' not found. Make sure the template exists.")
    }
}