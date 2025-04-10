plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.atls"
version = "1.0.0"

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
