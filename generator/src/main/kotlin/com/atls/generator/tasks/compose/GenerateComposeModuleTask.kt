package com.atls.generator.tasks.compose

import com.atls.generator.compose.ComposeModuleGenerator
import com.atls.generator.tasks.BaseGeneratorTask
import org.gradle.api.tasks.TaskAction

abstract class GenerateComposeModuleTask : BaseGeneratorTask() {

    init {
        group = "generator"
        description = "Generates a new Compose module"
    }

    @TaskAction
    fun generate() {
        ComposeModuleGenerator(
            project = project,
            moduleName = moduleName.get(),
            organization = organization.get()
        ).generate()
    }
}
