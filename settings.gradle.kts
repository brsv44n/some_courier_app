pluginManagement {
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
        maven { setUrl("https://developer.huawei.com/repo/") }
        maven { url = uri("https://artifactory-external.vkpartner.ru/artifactory/maven") }
    }
    includeBuild("includedBuild/gradleConfig")
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://developer.huawei.com/repo/") }
        maven { url = uri("https://artifactory-external.vkpartner.ru/artifactory/maven") }
    }
}

rootProject.name = "Some Android Courier"

include(":app")
include(":core:domain")
include(":core:utils")
include(":core:android_utils")
include(":core:popup")
include(":core:presentation")
include(":core:network")
include(":core:data")
include(":core:di")
