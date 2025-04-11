plugins.withId("maven-publish") {
    configure<PublishingExtension> {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/atls/kmp")
				version = project.version.toString()
                credentials {
                    username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                    password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
                }
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
