plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

apply(from = rootProject.file("publishing.gradle.kts"))

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
            id = "com.atls.compose"
            implementationClass = "com.atls.compose.ComposePlugin"
        }
    }
}
