plugins {
    id("dk.appdo.plugin.app")
    id("dk.appdo.plugin.compose")
}

android {
    namespace = "dk.appdo.github"
}

dependencies {
    implementation(project(":common:navigation"))

    implementation(project(":feature:search"))
    implementation(project(":feature:details"))
}