package com.atls.generator.utils

import org.gradle.api.Project
import java.io.File

abstract class ModuleGenerator(
    protected val project: Project,
    protected val modulePath: String,
    protected val organization: String
) {
    protected abstract val gradleTemplatePath: String
    protected abstract val kotlinTemplatePath: String

    private val pathComponents = modulePath.split(":")
    private val moduleName = pathComponents.last()
    private val parentDirs = pathComponents.dropLast(1)

    fun generate() {
        val moduleDir = resolveModuleDir()
        check(!moduleDir.exists()) { "Module '$modulePath' already exists at ${moduleDir.relativeTo(project.rootDir)}" }

        createDirectoryStructure(moduleDir)
        generateGradleFile(moduleDir)
        generateKotlinFiles(moduleDir)
        addGitignore(moduleDir)
        addToSettingsGradle()

        println("Created module '$modulePath' at ${moduleDir.relativeTo(project.rootDir)}")
    }

    private fun resolveModuleDir(): File {
        return parentDirs.fold(project.rootDir) { dir, name ->
            File(dir, name).apply { mkdirs() }
        }.resolve(moduleName)
    }

    protected open fun createDirectoryStructure(moduleDir: File) {
        val kotlinPath = "src/commonMain/kotlin/${organization.replace(".", "/")}/$moduleName"
        File(moduleDir, kotlinPath).mkdirs()
    }

    protected open fun generateGradleFile(moduleDir: File) {
        val template = readResource(gradleTemplatePath)
            .replace("{{PACKAGE}}", "$organization.$moduleName")
            .replace("{{BASENAME}}", "${moduleName}Kit")

        File(moduleDir, "build.gradle.kts").writeText(template)
    }

    protected open fun generateKotlinFiles(moduleDir: File) {
        val template = readResource(kotlinTemplatePath)
            .replace("{{PACKAGE}}", "$organization.$moduleName")
            .replace("{{MODULENAME}}", moduleName)

        val kotlinPath = "src/commonMain/kotlin/${organization.replace(".", "/")}/$moduleName"
        File(moduleDir, "$kotlinPath/Greeting.kt").writeText(template)
    }

    private fun addGitignore(moduleDir: File) {
        val gitignoreContent = """
            /build
        """.trimIndent()

        File(moduleDir, ".gitignore").writeText(gitignoreContent)
        println("Added .gitignore to ${moduleDir.relativeTo(project.rootDir)}")
    }

    private fun addToSettingsGradle() {
        val settingsFile = project.rootDir.resolve("settings.gradle.kts")
        if (!settingsFile.exists()) return

        val includePath = ":" + modulePath.replace(":", ":")
        val includeStatement = "include(\"$includePath\")"

        val content = settingsFile.readText()
        if (!content.contains(includeStatement)) {
            settingsFile.appendText("\n$includeStatement\n")
            println("Added to settings.gradle.kts: $includeStatement")
        }
    }

    protected fun readResource(path: String): String {
        return javaClass.classLoader.getResourceAsStream(path)
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: error("Resource '$path' not found")
    }
}
