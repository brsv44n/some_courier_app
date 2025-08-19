package com.brsv44n.some_courier.gradleconfig

import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.dependencies

class LibraryModuleConfigurationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target.plugins) {
            if (!hasPlugin("java-library")) apply("java-library")
            if (!hasPlugin("org.jetbrains.kotlin.jvm")) apply("org.jetbrains.kotlin.jvm")
        }

        target.extensions.configure("java", Action<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        })

        target.dependencies {
            add("testImplementation", "junit:junit:4.13.2")
        }
    }
}
