package com.brsv44n.some_courier.gradleconfig

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class DecomposeConfigurationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        if (!target.plugins.hasPlugin("org.jetbrains.kotlin.plugin.serialization")) {
            target.plugins.apply("org.jetbrains.kotlin.plugin.serialization")
        }
        target.dependencies {
            add("implementation", target.libs.findLibrary("decompose").get())
            add("implementation", target.libs.findLibrary("decompose-compose").get())
        }
    }
}
