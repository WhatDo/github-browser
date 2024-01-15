plugins {
    id("dk.appdo.plugin.lib")
    id("dk.appdo.plugin.compose")
}

android {
    namespace = "dk.appdo.feature.details"
}
dependencies {
    implementation(project(":common:repo"))
    implementation(project(":common:resources"))

    implementation(libs.coil)
    implementation(project(":common:ui"))
}
