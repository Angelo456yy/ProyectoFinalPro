pluginManagement {
    plugins {
        id("com.android.application") version "8.6.0"
        id("org.jetbrains.kotlin.android") version "1.8.20"
        // ... other plugins
    }
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ProyectoVersion22"
include(":app")
include(":compose_ui")