import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import utils.androidLibrary
import utils.configureKotlin
import utils.implementation
import utils.intValue
import utils.testImplementation
import utils.versionCatalog

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val versionCatalog = versionCatalog

        with(pluginManager) {
            apply("com.android.library")
            apply("org.jetbrains.kotlin.android")
        }

        // Configure Kotlin & Java compilation
        configureKotlin(javaVersion = JavaVersion.VERSION_17)

        androidLibrary {
            compileSdk = versionCatalog.findVersion("androidCompileSdk").intValue

            defaultConfig {
                minSdk = versionCatalog.findVersion("androidMinSdk").intValue
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

                consumerProguardFiles("consumer-rules.pro")
            }

            buildFeatures {
                buildConfig = false
                compose = false
                aidl = false
                renderScript = false
                shaders = false
            }

            compileOptions {
                isCoreLibraryDesugaringEnabled = true
            }
        }

        dependencies {
            implementation(versionCatalog.findLibrary("kotlinx-coroutines-android").get())
            implementation(versionCatalog.findLibrary("kotlinx-coroutines-core").get())
            implementation(versionCatalog.findLibrary("timber").get())

            testImplementation(versionCatalog.findLibrary("kotlinx-coroutines-test").get())
            testImplementation(versionCatalog.findLibrary("junit").get())
        }
    }
}

