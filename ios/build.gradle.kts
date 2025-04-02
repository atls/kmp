plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}
group = "com.rees46.plugins"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        create("iosPlugin") {
            id = "com.rees46.ios"
            implementationClass = "com.rees46.ios.IosPlugin"
        }
    }
}
