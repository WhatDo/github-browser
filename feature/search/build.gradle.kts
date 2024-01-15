plugins {
    id("dk.appdo.plugin.lib")
    id("dk.appdo.plugin.compose")
}

android {
    namespace = "dk.appdo.feature.search"
}

dependencies {
    implementation(project(":common:resources"))
}