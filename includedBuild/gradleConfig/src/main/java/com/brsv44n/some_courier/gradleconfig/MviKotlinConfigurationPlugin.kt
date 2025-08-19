package com.brsv44n.some_courier.gradleconfig

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class MviKotlinConfigurationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.dependencies {
            add("implementation", target.libs.findLibrary("mvikotlin").get())
            add("implementation", target.libs.findLibrary("mvikotlin-main").get())
            add("implementation", target.libs.findLibrary("mvikotlin-extensions-coroutines").get())
            add("implementation", target.libs.findLibrary("mvikotlin-rx").get())
        }
    }
}
