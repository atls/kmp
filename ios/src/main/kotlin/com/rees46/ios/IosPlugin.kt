package com.rees46.ios

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class IosPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val extension = extensions.create<IosPluginExtension>("iosConfig")

            pluginManager.apply("org.jetbrains.kotlin.multiplatform")

            afterEvaluate {

                val xcFrameworkName = extension.baseName.get()

                extensions.configure<KotlinMultiplatformExtension> {
                    iosX64 {
                        binaries.framework {
                            baseName = xcFrameworkName
                        }
                    }
                    iosArm64 {
                        binaries.framework {
                            baseName = xcFrameworkName
                        }
                    }
                    iosSimulatorArm64 {
                        binaries.framework {
                            baseName = xcFrameworkName
                        }
                    }
                }
            }
        }
    }
}
