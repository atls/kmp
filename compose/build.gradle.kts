plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.atls.plugins"
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
            id = "com.atls.compose"
            implementationClass = "com.atls.compose.ComposePlugin"
        }
    }
}

tasks.register("bumpPatchVersion") {
	doNotTrackState("Modifies build files")

    doLast {
        val (major, minor, patch) = version.toString().split(".").map { it.toInt() }
        val newVersion = "$major.$minor.${patch + 1}"

        val buildFile = file("build.gradle.kts")
        val content = buildFile.readText()
            .replace(
                Regex("version\\s*=\\s*[\"']$version[\"']"), 
                "version = \"$newVersion\""
            )

        buildFile.writeText(content)
        println("Version updated from $version to $newVersion")
    }
}
