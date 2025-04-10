package com.atls.generator.tasks.kmp

import com.atls.generator.kmp.KmpModuleGenerator
import com.atls.generator.tasks.BaseGeneratorTask
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
