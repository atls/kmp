package com.rees46.multiplatform

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.rees46.ios")
            pluginManager.apply("com.rees46.android")

            extensions.getByType<KotlinMultiplatformExtension>().apply {
                sourceSets.getByName("commonMain").dependencies {
                    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.0")
                }
            }
        }
    }
}
