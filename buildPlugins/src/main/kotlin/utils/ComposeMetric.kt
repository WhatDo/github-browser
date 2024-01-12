@file:Suppress("unused")

package utils

import org.gradle.api.Project
import java.io.File

internal fun Project.buildComposeMetricsParameters(): List<String> =
    if (project.properties["enableComposeCompilerMetrics"]?.toString().toBoolean()) {
        listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination="
                    + project.layout.buildDirectory.file("compose-metrics").get().asFile.absolutePath
        )
    } else listOf()


internal fun Project.buildComposeReportsParameters(): List<String> =
    if (project.properties["enableComposeCompilerReports"]?.toString().toBoolean()) {
        listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination="
                    + project.layout.buildDirectory.file("compose-metrics").get().asFile.absolutePath
        )
    } else listOf()
