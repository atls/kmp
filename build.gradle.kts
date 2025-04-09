plugins {
    `maven-publish`
    `kotlin-dsl`
}

version = "1.0.0"

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
    // implementation("org.jetbrains.multiplatform:multiplatform-gradle-plugin:1.7.0")
}

tasks.register("bumpAllVersions") {
    dependsOn(
        ":android:bumpPatchVersion",
        ":compose:bumpPatchVersion",
        ":ios:bumpPatchVersion",
        ":multiplatform:bumpPatchVersion"
    )
}

// kotlin {
    // android()
    // ios()
// }

val githubUser: String = System.getenv("GITHUB_USER") ?: "defaultUser"
val githubToken: String = System.getenv("GITHUB_TOKEN") ?: "defaultToken"
val githubRepo: String = System.getenv("GITHUB_REPO") ?: "kmp"

publishing {
    publications {
        create<MavenPublication>("multiplatform") {
			group = "com.github.$githubUser"
            artifactId = project.name

            from(components["kotlin"])

            // artifact(sourcesJar.get())
            // artifact(javadocJar.get())

            pom {
                name.set("KMP")
                description.set("Kotlin Multiplatform Modules")
                url.set("https://github.com/$githubUser/$githubRepo")
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
                    connection.set("scm:git:git://github.com/$githubUser/$githubRepo.git")
                    developerConnection.set("scm:git:ssh://github.com/$githubUser/$githubRepo.git")
                    url.set("https://github.com/$githubUser/$githubRepo")
                }
            }
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/$githubUser/$githubRepo")

            credentials {
                username = githubUser
                password = githubToken
            }
        }
    }
}
