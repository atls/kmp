package com.atls.generator.kmp

import com.atls.generator.BaseGeneratorTask
import org.gradle.api.tasks.TaskAction

abstract class GenerateKmpModuleTask : BaseGeneratorTask() {

    init {
        group = "generator"
        description = "Generates a new KMP module"
    }

    @TaskAction
    fun generate() {
        KmpModuleGenerator(
            project = project,
            moduleName = moduleName.get(),
            organization = organization.get()
        ).generate()
    }
}
