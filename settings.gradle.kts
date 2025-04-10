rootProject.name = "convention-plugins"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":multiplatform")
include(":compose")
include(":android")
include(":ios")

include(":generator")
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
