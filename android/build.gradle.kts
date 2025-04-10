plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}
group = "com.atls.plugins"
version = "1.0.0"

apply(from = rootProject.file("publishing.gradle.kts"))

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
    compileOnly("com.android.tools.build:gradle:8.9.1")
}

gradlePlugin {
    plugins {
        create("androidPlugin") {
            id = "com.atls.android"
            implementationClass = "com.atls.android.AndroidPlugin"
        }
    }
}
