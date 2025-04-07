package com.atls.generator.kmp

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class GenerateKmpModuleTask : DefaultTask() {
    init {
        group = "generator"
        description = "Generates a new kmp module"
    }

    @TaskAction
    fun generate() {
        val moduleName = project.findProperty("moduleName") as? String
            ?: error("Specify -PmoduleName=<name>")
        val organization = project.findProperty("organization") as? String
            ?: error("Specify -Porganization=<org>")

        KmpModuleGenerator(project, moduleName, organization).generate()
    }
}
