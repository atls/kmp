plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}
group = "com.rees46.plugins"
version = "1.0.0"

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.android.gradle.plugin)
}

gradlePlugin {
    plugins {
        create("androidPlugin") {
            id = "com.rees46.android"
            implementationClass = "com.rees46.android.AndroidPlugin"
        }
    }
}
