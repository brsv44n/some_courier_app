package com.brsv44n.some_courier.gradleconfig

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.io.IOException
import java.util.Properties

class SigningConfigPlugin : Plugin<Project> {

    companion object {
        object Debug {
            const val file = "debug.keystore"
            const val keyAlias = "androiddebugkey"
            const val keyPassword = "android"
            const val storePassword = "android"
        }
    }

    override fun apply(target: Project) {
        setupDebugSigningConfig(target)
        setupReleaseSigningConfig(target)
    }

    private fun setupDebugSigningConfig(target: Project) {
        target.extensions.configure(BaseAppModuleExtension::class.java) {
            val dir = File(target.rootDir, "signing")
            signingConfigs.getByName("debug") {
                storeFile = File(dir, Debug.file)
                keyAlias = Debug.keyAlias
                keyPassword = Debug.keyPassword
                storePassword = Debug.storePassword
            }
        }
    }

    private fun setupReleaseSigningConfig(target: Project) {
        target.extensions.configure(BaseAppModuleExtension::class.java) {
            val properties = Properties()
            val dir = File(target.rootDir, "signing")
            try {
                properties.load(File(dir, "keystore.properties").inputStream())
                signingConfigs.create("release") {
                    storeFile = File(dir, "keystore.jks")
                    keyPassword = properties.getProperty("keyPassword")
                    storePassword = properties.getProperty("keystorePassword")
                    keyAlias = properties.getProperty("keyAlias")
                }
            } catch (e: IOException) {
                println(
                    "Cannot load keystore.properties file for release signing. " +
                            "Did you forget to run signing/generate.sh script?"
                )
                signingConfigs.create("release") {
                    storeFile = File("")
                    keyPassword = ""
                    storePassword = ""
                    keyAlias = ""
                }
            }
        }
    }


}