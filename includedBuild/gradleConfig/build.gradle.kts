import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
    google()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    jvmToolchain(17)
}

val libs = the<LibrariesForLibs>()

dependencies {
    implementation(libs.android.gradle)
    implementation(libs.kotlin.gradle)
    implementation(libs.detekt.gradle)

    implementation(libs.eclipse.jgit)

    //kotlin dsl
    implementation(libs.kotlin.stdlib.jdk8)
}

gradlePlugin {
    plugins.register("gradle-configuration") {
        id = "gradle-configuration"
        implementationClass = "com.brsv44n.some_courier.gradleconfig.GradleConfigurationPlugin"
    }
    plugins.register("decompose-configuration") {
        id = "decompose-configuration"
        implementationClass = "com.brsv44n.some_courier.gradleconfig.DecomposeConfigurationPlugin"
    }
    plugins.register("mvi-configuration") {
        id = "mvi-configuration"
        implementationClass = "com.brsv44n.some_courier.gradleconfig.MviKotlinConfigurationPlugin"
    }
    plugins.register("android-library-configuration") {
        id = "android-library-configuration"
        implementationClass = "com.brsv44n.some_courier.gradleconfig.AndroidLibraryModuleConfigurationPlugin"
    }
    plugins.register("library-configuration") {
        id = "library-configuration"
        implementationClass = "com.brsv44n.some_courier.gradleconfig.LibraryModuleConfigurationPlugin"
    }
    plugins.register("compose-configuration") {
        id = "compose-configuration"
        implementationClass = "com.brsv44n.some_courier.gradleconfig.ComposeConfigurationPlugin"
    }
    plugins.register("signing-configuration") {
        id = "signing-configuration"
        implementationClass = "com.brsv44n.some_courier.gradleconfig.SigningConfigPlugin"
    }
    plugins.register("kotlin-inject-configuration") {
        id = "kotlin-inject-configuration"
        implementationClass = "com.brsv44n.some_courier.gradleconfig.KotlinInjectConfigurationPlugin"
    }
}
