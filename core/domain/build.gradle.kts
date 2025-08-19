plugins {
    id("library-configuration")
    id("kotlinx-serialization")
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.joda.time)
}