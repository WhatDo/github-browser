plugins {
    id("dk.appdo.plugin.lib")
}

android {
    namespace = "dk.appdo.common.models"
}

dependencies {
    api(libs.ktor.client.core)
}