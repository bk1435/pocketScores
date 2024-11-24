pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS) // Use repositories from settings.gradle.kts
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "pocketScores2"
include(":app")
