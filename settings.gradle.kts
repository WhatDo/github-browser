pluginManagement {
    includeBuild("buildPlugins")
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

rootProject.name = "dawn-github-browser"
include(":app")
include(
    ":feature:search",
    ":feature:details"
)
include(":common:models")
include(":common:navigation")
include(":common:resources")
include(":common:repo")
