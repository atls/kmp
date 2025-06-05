package com.atls.compose

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

const val composeVersion = "1.8.0"
const val androidxLifecycleVersion = "2.8.4"

class ComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.multiplatform")
            pluginManager.apply("org.jetbrains.compose")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
            pluginManager.apply("com.atls.ios")
            pluginManager.apply("com.atls.android")

            val composeDependencies = listOf(
                "org.jetbrains.compose.runtime:runtime:$composeVersion",
                "org.jetbrains.compose.foundation:foundation:$composeVersion",
                "org.jetbrains.compose.material:material:$composeVersion",
                "org.jetbrains.compose.ui:ui:$composeVersion",
                "org.jetbrains.compose.components:components-resources:$composeVersion",
                "org.jetbrains.compose.components:components-ui-tooling-preview:$composeVersion"
            )

            extensions.getByType<KotlinMultiplatformExtension>().apply {
                sourceSets.getByName("commonMain").dependencies {
                    composeDependencies.forEach { implementation(it) }
                }
                sourceSets.getByName("androidMain").dependencies {
                    implementation("androidx.lifecycle:lifecycle-viewmodel:$androidxLifecycleVersion")
                    implementation("androidx.lifecycle:lifecycle-runtime-compose:$androidxLifecycleVersion")
                    implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:${androidxLifecycleVersion}")
                }
            }
        }
    }
}
