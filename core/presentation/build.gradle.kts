plugins {
    id("android-library-configuration")
    id("decompose-configuration")
    id("mvi-configuration")
}

android {
    namespace = "com.brsv44n.some_courier.core.presentation"
    lint {
        disable.add("CoroutineCreationDuringComposition")
    }
}
dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:utils"))
    implementation(libs.joda.time)
}
