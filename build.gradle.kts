plugins {
    `maven-publish`
    `kotlin-dsl`
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

allprojects {
    version = "1.0.3"
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

nexusPublishing {
    repositories {
        sonatype {
            stagingProfileId.set(project.findProperty("stagingProfileId")?.toString() ?: "")
            username.set(project.findProperty("sonataUsername")?.toString())
            password.set(project.findProperty("sonataPassword")?.toString())
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}
