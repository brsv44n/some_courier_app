plugins {
    id("android-library-configuration")
    id("decompose-configuration")
}

android {
    namespace = "com.brsv44n.some_courier.core.popup"
}

dependencies {
    implementation(project(":core:utils"))
    implementation(libs.kotlinx.coroutines.core)
}