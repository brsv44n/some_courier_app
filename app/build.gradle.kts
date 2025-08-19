plugins {
    alias(libs.plugins.android.application)
    id("io.sentry.android.gradle") version "5.3.0"
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serilization)
    alias(libs.plugins.ksp)

    id("decompose-configuration")
    id("mvi-configuration")
    id("kotlin-inject-configuration")
    id("signing-configuration")
    id("com.huawei.agconnect")
    id("kotlin-parcelize")
}

android {
    namespace = "com.brsv44n.some_courier"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.brsv44n.some_courier"
        minSdk = 24
        targetSdk = 35
        versionCode = 2
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = null
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            signingConfig = null
        }
    }

    flavorDimensions.add("client")
    productFlavors {
        val testFlavorSigningConfig = signingConfigs.getByName("debug")
        val productionFlavorSigningConfig = signingConfigs.getByName("release")

        create("clientProduction") {
            dimension = "client"
            signingConfig = productionFlavorSigningConfig
            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://some_back/api/\""
            )
            buildConfigField("String", "RUSTORE_PROJECT_ID", "\"some_id\"")
            manifestPlaceholders["SENTRY_DSN"] =
                "sentry_key"
        }
        create("clientTest") {
            dimension = "client"
            applicationIdSuffix = ".test"
            versionNameSuffix = "-test"
            signingConfig = testFlavorSigningConfig
            buildConfigField("String", "BASE_URL", "\"https://some_test_back/api/\"")
            buildConfigField("String", "RUSTORE_PROJECT_ID", "\"test_key\"")
            manifestPlaceholders["SENTRY_DSN"] =
                "also_test"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.browser)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.decompose)
    implementation(libs.decompose.compose)

    implementation(libs.mvikotlin.logging)
    implementation(libs.mvikotlin.timetravel)

    implementation(libs.timber)
    implementation(libs.joda.time)

    implementation(libs.lifecycle.coroutines)

    implementation(project(":core:utils"))
    implementation(project(":core:presentation"))
    implementation(project(":core:di"))
    implementation(project(":core:android_utils"))
    implementation(project(":core:popup"))
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":core:data"))

    implementation(libs.retrofit)
    implementation(libs.jetbrains.kotlinx.serialization.json.v180)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp)
    implementation(libs.okhttp.sse)
    implementation(libs.logging.interceptor)

    implementation(libs.play.services.location)
    implementation(libs.hms.core.location)

    implementation(libs.accompanist.permissions)

    implementation(libs.rustore.pushclient)
    implementation(libs.androidx.appcompat)

}