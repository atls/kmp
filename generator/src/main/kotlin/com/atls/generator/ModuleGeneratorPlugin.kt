package com.atls.generator

import com.atls.generator.tasks.compose.GenerateComposeModuleTask
import com.atls.generator.tasks.kmp.GenerateKmpModuleTask
import org.gradle.api.Plugin
import org.gradle.api.Project

const val kmpTask = "generateKmpModule"
const val composeTask = "generateComposeModule"

class ModuleGeneratorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register(kmpTask, GenerateKmpModuleTask::class.java)
        project.tasks.register(composeTask, GenerateComposeModuleTask::class.java)
    }
}
