// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlin.serilization) apply false
    id("gradle-configuration")
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ksp) apply false
}

buildscript {
    dependencies {
        classpath(libs.agcp)
        classpath(libs.android.gradle)
    }
}

gradle.startParameter.excludedTaskNames.add(":gradleConfig:testClasses")