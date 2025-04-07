package com.atls.generator.compose

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class GenerateComposeModuleTask : DefaultTask() {
    init {
        group = "generator"
        description = "Generates a new compose module"
    }

    @TaskAction
    fun generate() {
        val moduleName = project.findProperty("moduleName") as? String
            ?: error("Specify -PmoduleName=<name>")
        val organization = project.findProperty("organization") as? String
            ?: error("Specify -Porganization=<org>")
        
        ComposeModuleGenerator(project, moduleName, organization).generate()
    }
}
