package com.brsv44n.some_courier.gradleconfig

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KotlinInjectConfigurationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        if (!target.plugins.hasPlugin("com.google.devtools.ksp")) {
            target.plugins.apply("com.google.devtools.ksp")
        }
        target.dependencies {
            add("implementation", target.libs.findLibrary("kotlin-inject-runtime").get())
            add("ksp", target.libs.findLibrary("kotlin-inject-ksp").get())
        }
    }
}
