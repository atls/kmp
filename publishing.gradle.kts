plugins.withId("maven-publish") {
    configure<PublishingExtension> {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/rees46/kmp")
				version = project.version.toString()
                credentials {
                    username = project.findProperty("ossrhUsername") as String? ?: System.getenv("GITHUB_ACTOR")
                    password = project.findProperty("ossrhPassword") as String? ?: System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}
