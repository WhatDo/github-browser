plugins {
    id("dk.appdo.plugin.lib")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "dk.appdo.repo"
}

dependencies {
    api(project(":common:models"))

    api(libs.androidx.paging)
    api(libs.arrow.core)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)

    implementation(libs.kotlinx.serialization.json)
}