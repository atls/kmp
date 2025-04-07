package com.atls.generator

import com.atls.generator.compose.GenerateComposeModuleTask
import com.atls.generator.kmp.GenerateKmpModuleTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class ModuleGeneratorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("generateKmpModule", GenerateKmpModuleTask::class.java)
        project.tasks.register("generateComposeModule", GenerateComposeModuleTask::class.java)
    }
}
