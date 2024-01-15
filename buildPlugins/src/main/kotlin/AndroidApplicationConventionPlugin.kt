import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import utils.configureKotlin
import utils.implementation
import utils.intValue
import utils.testImplementation
import utils.versionCatalog
import java.io.ByteArrayOutputStream

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val versionCatalog = versionCatalog

        with(pluginManager) {
            apply("com.android.application")
            apply("org.jetbrains.kotlin.android")
        }

        // Configure Kotlin & Java compilation
        configureKotlin(javaVersion = JavaVersion.VERSION_17)

        android {
            compileSdk = versionCatalog.findVersion("androidCompileSdk").intValue

//            signingConfigs {
//                create("release") {
//                    storeFile =
//                        rootProject.file("keystore/debug.keystore")
//                    storePassword = "android"
//                    keyAlias = "androiddebugkey"
//                    keyPassword = "android"
//                }
//            }

            defaultConfig {
                minSdk = versionCatalog.findVersion("androidMinSdk").intValue
                targetSdk = versionCatalog.findVersion("androidTargetSdk").intValue

                versionName = rootProject.file("version.txt")
                    .takeIf { it.exists() }
                    ?.readText()
                    ?.takeIf { it.isNotBlank() }
                    ?: project.properties["versionName"]
                        ?.toString()
                        ?.ifBlank { null }
                            ?: "0.0.0"

                val versionCodeOs = ByteArrayOutputStream()
                exec {
                    commandLine("git", "rev-list", "HEAD", "--count")
                    standardOutput = versionCodeOs
                }.assertNormalExitValue()

                val offset = 0 // current play store version code
                versionCode = String(versionCodeOs.toByteArray()).trim().toInt() + offset

                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }

            buildTypes {
                getByName("debug") {
                    applicationIdSuffix = ".debug"
                    isMinifyEnabled = false
                    isDebuggable = true
                }

                getByName("release") {
//                    signingConfig = signingConfigs.getByName("release")
                    isMinifyEnabled = true
                    isShrinkResources = true
                    isDebuggable = false
                }

                create("staging") {
                    initWith(getByName("release"))
                    versionNameSuffix = getBuildNumber()?.let { build -> "-dev$build" }
                    matchingFallbacks += "release"
                }
            }

            buildFeatures {
                buildConfig = false
                compose = false
                aidl = false
                renderScript = false
                shaders = false
            }

            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1,LICENSE.md,LICENSE-notice.md}"
//                    excludes += "/META-INF/*"
                }
            }
        }

        dependencies {
            implementation(versionCatalog.findLibrary("androidx-activity-compose").get())
            implementation(versionCatalog.findLibrary("kotlinx-coroutines-android").get())
            implementation(versionCatalog.findLibrary("timber").get())

            testImplementation(versionCatalog.findLibrary("kotlinx-coroutines-test").get())
            testImplementation(versionCatalog.findLibrary("junit").get())
        }
    }
}

private fun Project.android(configure: Action<ApplicationExtension>): Unit =
    extensions.configure("android", configure)

private fun getBuildNumber(): String? {
    return System.getenv("CI_BUILD_NUMBER")?.trim()?.takeIf(String::isNotBlank)
}
