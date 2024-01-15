plugins {
    id("dk.appdo.plugin.app")
    id("dk.appdo.plugin.compose")
}

android {
    namespace = "dk.appdo.github"
}

dependencies {
    implementation(project(":common:navigation"))
    implementation(project(":common:repo"))

    implementation(project(":feature:search"))
    implementation(project(":feature:details"))

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.json)
    implementation(libs.okhttp.logging)
}