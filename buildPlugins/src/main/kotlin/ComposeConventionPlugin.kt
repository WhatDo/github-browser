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
                val bom = versionCatalog.findLibrary("androidx-compose-bom").get()

                implementation(platform(bom))
                implementation(versionCatalog.findLibrary("androidx-ui").get())
                implementation(versionCatalog.findLibrary("androidx-ui-graphics").get())
                implementation(versionCatalog.findLibrary("androidx-ui-tooling-preview").get())
                implementation(versionCatalog.findLibrary("androidx-lifecycle-runtime-ktx").get())
                implementation(versionCatalog.findLibrary("androidx-lifecycle-runtime-compose").get())
                implementation(versionCatalog.findLibrary("androidx-material3").get())
                implementation(versionCatalog.findLibrary("kotlinx-collections-immutable").get())
                implementation(versionCatalog.findLibrary("androidx-navigation-compose").get())

                debugImplementation(versionCatalog.findLibrary("androidx-ui-tooling").get())
                debugImplementation(versionCatalog.findLibrary("androidx-ui-test-manifest").get())

                androidTestImplementation(platform(bom))
                androidTestImplementation(versionCatalog.findLibrary("androidx-ui-test-junit4").get())
            }
        }
    }
}

