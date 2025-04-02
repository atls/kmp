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
    implementation(libs.compose.gradle.plugin)
    implementation(project(":ios"))
    implementation(project(":android"))
}

gradlePlugin {
    plugins {
        create("composePlugin") {
            id = "com.rees46.compose"
            implementationClass = "com.rees46.compose.ComposePlugin"
        }
    }
}
