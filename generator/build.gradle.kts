plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

apply(from = rootProject.file("publishing.gradle.kts"))

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
}

gradlePlugin {
    plugins {
        create("moduleGenerator") {
            id = "com.atls.module-generator"
            implementationClass = "com.atls.generator.ModuleGeneratorPlugin"
        }
    }
}

sourceSets.main {
    resources.srcDirs("src/main/resources")
}

tasks.withType<ProcessResources>().configureEach {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
