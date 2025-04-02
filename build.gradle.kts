plugins {
    `maven-publish`
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    mavenLocal()
    gradlePluginPortal()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0")
    implementation("com.android.tools.build:gradle:8.9.0")
    implementation("org.jetbrains.multiplatform:multiplatform-gradle-plugin:1.7.0")
}
