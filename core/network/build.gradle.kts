plugins {
    id("library-configuration")
    id("kotlinx-serialization")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(project(":core:utils"))
}