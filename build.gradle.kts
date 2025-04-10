plugins {
    `maven-publish`
    `kotlin-dsl`
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

version = "1.0.3"

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

val githubUser: String = System.getenv("GITHUB_USER") ?: "defaultUser"
val githubToken: String = System.getenv("GITHUB_TOKEN") ?: "defaultToken"
val githubRepo: String = System.getenv("GITHUB_REPO") ?: "kmp"

publishing {
    publications {
        create<MavenPublication>("multiplatform") {
            group = "com.github.$githubUser"
            artifactId = project.name

            from(components["kotlin"])

            pom {
                name.set("KMP")
                description.set("Kotlin Multiplatform Modules")
                url.set("https://github.com/$githubRepo")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set(githubUser)
                        name.set(githubUser)
                        email.set("")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/$githubRepo.git")
                    developerConnection.set("scm:git:ssh://github.com/$githubRepo.git")
                    url.set("https://github.com/$githubRepo")
                }
            }
        }
        afterEvaluate {
            tasks.withType<PublishToMavenRepository>().configureEach {
                if (publication.name.contains("pluginMaven", ignoreCase = true)) {
                    enabled = false
                }
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/$githubRepo")

            credentials {
                username = githubUser
                password = githubToken
            }
        }
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
