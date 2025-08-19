plugins {
    id("library-configuration")
    id("kotlinx-serialization")
}

dependencies {
    implementation(project(":core:utils"))
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.joda.time)
}