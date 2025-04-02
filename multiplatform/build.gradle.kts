plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.rees46.plugins"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
    implementation(project(":ios"))
    implementation(project(":android"))
}

gradlePlugin {
    plugins {
        create("multiplatformPlugin") {
            id = "com.rees46.multiplatform"
            implementationClass = "com.rees46.multiplatform.MultiplatformPlugin"
        }
    }
}
