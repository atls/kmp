plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}
group = "com.atls.plugins"
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
            id = "com.atls.ios"
            implementationClass = "com.atls.ios.IosPlugin"
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
