plugins {
    id("android-library-configuration")
}

android {
    namespace = "com.brsv44n.some_courier.core.android_utils"
}

dependencies {
    implementation(project(":core:utils"))
    implementation(project(":core:network"))
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.kotlinx.serialization.json)
}