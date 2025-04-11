plugins.withId("maven-publish") {
    configure<PublishingExtension> {
        repositories {
            maven {
                name = "GitHubPackages"
                // url = uri("https://maven.pkg.github.com/atls/convention-plugins")
                url = uri("https://maven.pkg.github.com/atls/kmp")
                credentials {
                    username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                    password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}
