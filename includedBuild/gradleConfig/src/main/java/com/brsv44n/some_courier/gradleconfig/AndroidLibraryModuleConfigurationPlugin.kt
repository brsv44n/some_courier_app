package com.brsv44n.some_courier.gradleconfig

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

class AndroidLibraryModuleConfigurationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target.plugins) {
            if (!hasPlugin("com.android.library")) apply("com.android.library")
            if (!hasPlugin("org.jetbrains.kotlin.android")) apply("org.jetbrains.kotlin.android")
        }
        target.extensions.configure(LibraryExtension::class.java) {
            compileSdk = 35

            defaultConfig {
                minSdk = 24
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                consumerProguardFiles("consumer-rules.pro")
            }

            buildTypes {
                release {
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }

            target.extensions.configure("java", Action<JavaPluginExtension> {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
                toolchain {
                    languageVersion.set(JavaLanguageVersion.of(17))
                }
            })

            (this as ExtensionAware).extensions.configure(KotlinJvmOptions::class.java) {
                jvmTarget = "17"
            }
        }

        target.dependencies {
            add("testImplementation", "junit:junit:4.13.2")
            add("androidTestImplementation", "androidx.test.ext:junit:1.1.5")
            add("androidTestImplementation", "androidx.test.espresso:espresso-core:3.5.1")
            add("implementation", target.libs.findLibrary("androidx-core-ktx").get())
        }
    }
}
