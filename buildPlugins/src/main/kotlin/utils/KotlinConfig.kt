@file:Suppress("UnstableApiUsage")

package utils

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlin(javaVersion: JavaVersion) {
    android {
        // Set JVM targets to the same version, for consistency
        compileOptions {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }

        configureKotlinOptions(javaVersion)
    }

    // Set JVM targets to the same version, for consistency
    java {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}

/**
 * Configure base Kotlin options for JVM (non-Android)
 */
internal fun Project.configureKotlinJvm(javaVersion: JavaVersion) {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    configureKotlinOptions(javaVersion)
}

/**
 * Configure base Kotlin options
 */
private fun Project.configureKotlinOptions(javaVersion: JavaVersion) {
    // Use withType to workaround https://youtrack.jetbrains.com/issue/KT-55947
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-opt-in=kotlin.RequiresOptIn",
                // Enable experimental coroutines APIs, including Flow
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
            )

            // Set JVM targets to the same version, for consistency
            jvmTarget = javaVersion.toString()
        }
    }
}

internal fun Project.androidLibrary(block: Action<LibraryExtension>): Unit =
    extensions.configure("android", block)

internal fun Project.android(block: Action<CommonExtension<*, *, *, *, *, *>>): Unit =
    extensions.configure("android", block)

private fun Project.java(block: Action<JavaPluginExtension>): Unit =
    extensions.configure("java", block)

internal fun CommonExtension<*, *, *, *, *, *>.kotlinOptions(block: Action<KotlinJvmOptions>) =
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)

internal fun Project.kapt(configure: Action<KaptExtension>): Unit =
    extensions.configure("kapt", configure)

