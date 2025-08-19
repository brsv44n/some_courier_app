package com.brsv44n.some_courier.gradleconfig

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class ComposeConfigurationPlugin : Plugin<Project> {

    companion object {
        private const val COMPOSE_COMPILER_EXTENSION_VERSION = "1.5.15"
    }

    override fun apply(target: Project) {
        with(target.plugins) {
            if (!hasPlugin("org.jetbrains.kotlin.plugin.compose")) {
                apply("org.jetbrains.kotlin.plugin.compose")
            }
        }

        target.extensions.findByType(BaseAppModuleExtension::class.java)?.apply {
            buildFeatures {
                compose = true
            }

            composeOptions {
                kotlinCompilerExtensionVersion = COMPOSE_COMPILER_EXTENSION_VERSION
            }
        } ?: target.extensions.findByType(LibraryExtension::class.java)?.apply {
            buildFeatures {
                compose = true
            }

            composeOptions {
                kotlinCompilerExtensionVersion = COMPOSE_COMPILER_EXTENSION_VERSION
            }
        }

        target.dependencies {
            add("implementation", platform(target.libs.findLibrary("androidx-compose-bom").get()))
            add("androidTestImplementation", platform(target.libs.findLibrary("androidx-compose-bom").get()))
            add("implementation", target.libs.findLibrary("androidx-material3").get())
            add("implementation", target.libs.findLibrary("androidx-ui-tooling-preview").get())
            add("debugImplementation", target.libs.findLibrary("androidx-ui-tooling").get())
            add("androidTestImplementation", target.libs.findLibrary("androidx-ui-test-junit4").get())
            add("debugImplementation", target.libs.findLibrary("androidx-ui-test-junit4").get())
            add("implementation", target.libs.findLibrary("androidx-material-icons-extended").get())
            add("implementation", target.libs.findLibrary("androidx-activity-compose").get())
        }
    }
}
