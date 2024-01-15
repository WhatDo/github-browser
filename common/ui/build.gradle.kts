plugins {
    id("dk.appdo.plugin.lib")
    id("dk.appdo.plugin.compose")
}

android {
    namespace = "dk.appdo.common.ui"
}

dependencies {
    implementation(project(":common:resources"))
}