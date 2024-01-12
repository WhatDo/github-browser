import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import utils.android
import utils.androidTestImplementation
import utils.buildComposeMetricsParameters
import utils.buildComposeReportsParameters
import utils.debugImplementation
import utils.implementation
import utils.value
import utils.versionCatalog
import utils.kotlinOptions

@Suppress("UnstableApiUsage")
class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val versionCatalog = versionCatalog

        android {
            buildFeatures {
                compose = true
            }

            composeOptions {
                kotlinCompilerExtensionVersion = versionCatalog.findVersion("androidx-compose-compiler").value
            }

            kotlinOptions {
                freeCompilerArgs = freeCompilerArgs +
                        // Enable Compose Metrics if "enableComposeCompilerMetrics" property is true
                        buildComposeMetricsParameters() +
                        // Enable Compose Reports if "enableComposeCompilerReports" property is true
                        buildComposeReportsParameters()
            }

            dependencies {
                val bom = versionCatalog.findLibrary("compose-bom").get()

                implementation(platform(bom))
                implementation(versionCatalog.findLibrary("ui").get())
                implementation(versionCatalog.findLibrary("ui-graphics").get())
                implementation(versionCatalog.findLibrary("ui-tooling-preview").get())
                implementation(versionCatalog.findLibrary("lifecycle-runtime-ktx").get())
                implementation(versionCatalog.findLibrary("lifecycle-runtime-compose").get())
                implementation(versionCatalog.findLibrary("material3").get())
                implementation(versionCatalog.findLibrary("kotlinx-collections-immutable").get())
                implementation(versionCatalog.findLibrary("androidx-navigation-compose").get())

                debugImplementation(versionCatalog.findLibrary("ui-tooling").get())
                debugImplementation(versionCatalog.findLibrary("ui-test-manifest").get())

                androidTestImplementation(platform(bom))
                androidTestImplementation(versionCatalog.findLibrary("ui-test-junit4").get())
            }
        }
    }
}

