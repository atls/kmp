package com.atls.ios

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

class IosPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val extension = extensions.create<IosPluginExtension>("iosConfig")
            pluginManager.apply("org.jetbrains.kotlin.multiplatform")

            afterEvaluate {
                val xcFrameworkName = extension.baseName.get()
                val xcFramework = XCFramework(xcFrameworkName)

                extensions.configure<KotlinMultiplatformExtension> {
                    iosX64().apply {
                        binaries.framework {
                            baseName = xcFrameworkName
                            xcFramework.add(this)
                        }
                    }

                    iosArm64().apply {
                        binaries.framework {
                            baseName = xcFrameworkName
                            xcFramework.add(this)
                        }
                    }

                    iosSimulatorArm64().apply {
                        binaries.framework {
                            baseName = xcFrameworkName
                            xcFramework.add(this)
                        }
                    }
                }
            }
        }
    }
}
