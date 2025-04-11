plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

apply(from = rootProject.file("publishing.gradle.kts"))

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        create("iosPlugin") {
            id = "com.atls.ios"
            implementationClass = "com.atls.ios.IosPlugin"
        }
    }
}

