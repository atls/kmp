plugins {
  `maven-publish`
  `kotlin-dsl`
  id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
  id("org.jetbrains.dokka") version "1.9.10" apply false
  signing
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0")
  implementation("com.android.tools.build:gradle:8.9.0")
}

repositories {
  google()
  mavenCentral()
  mavenLocal()
  gradlePluginPortal()
  maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

val versionFile = file("version.properties")
val projectVersion = versionFile.readText().substringAfter("VERSION_NAME=").trim()

allprojects {
  group = "com.rees46"
  version = projectVersion

  apply(plugin = "maven-publish")
  apply(plugin = "signing")

  tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(
        project.extensions.findByType<SourceSetContainer>()?.getByName("main")?.allSource
            ?: (kotlin.sourceSets["main"].kotlin.srcDirs))
  }

  tasks.register<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    val javadocTask = tasks.findByName("dokkaHtml") ?: tasks.findByName("javadoc")
    from(javadocTask?.outputs)
  }

  publishing {
    publications {
      withType<MavenPublication> {
        artifact(tasks["sourcesJar"])
        artifact(tasks["javadocJar"])

        pom {
          name.set("rees46 KMP Library")
          description.set("Kotlin Multiplatform library for rees46 e-commerce platform")
          url.set("https://github.com/rees46/kmp")

          licenses {
            license {
              name.set("The Apache License, Version 2.0")
              url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
          }

          developers {
            developer {
              id.set("")
              name.set("")
              email.set("")
            }
          }

          scm {
            connection.set("scm:git:github.com:rees46/kmp.git")
            developerConnection.set("scm:git:ssh://github.com:rees46/kmp.git")
            url.set("https://github.com/rees46/kmp/tree/master")
          }
        }
      }
    }
  }

  signing {
    setRequired { gradle.taskGraph.allTasks.any { it is PublishToMavenRepository } }

    val signing_keyId: String? by project
    val signing_key: String? by project
    val signing_password: String? by project

    if (signing_key != null) {
        useInMemoryPgpKeys(signing_keyId, signing_key, signing_password)
    } else {
        logger.warn("Signing key not configured - artifacts will not be signed")
    }

    sign(publishing.publications)
  }
}

subprojects {
  plugins.withType<MavenPublishPlugin> {
    tasks.withType<PublishToMavenRepository>().configureEach {
      mustRunAfter(tasks.withType<Sign>())
    }
  }
}

tasks.register("printVersion") {
    doLast {
        println(version)
    }
}

tasks.register("updateVersion") {
    doLast {
        val newVersion = project.property("newVersion") as String
        
        file("version.properties").writeText("version=$newVersion")
        
        allprojects {
            version = newVersion
        }
        
        println("Version updated to $newVersion")
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

tasks.withType<PublishToMavenRepository>().configureEach {
  notCompatibleWithConfigurationCache("Nexus tasks are not compatible with config cache")
}
