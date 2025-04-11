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
    implementation(project(":ios"))
    implementation(project(":android"))
}

gradlePlugin {
    plugins {
        create("multiplatformPlugin") {
            id = "com.atls.multiplatform"
            implementationClass = "com.atls.multiplatform.MultiplatformPlugin"
        }
    }
}
