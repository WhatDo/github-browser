plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("dk.appdo.plugin.app") {
            id = "dk.appdo.plugin.app"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("dk.appdo.plugin.lib") {
            id = "dk.appdo.plugin.lib"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("dk.appdo.plugin.compose") {
            id = "dk.appdo.plugin.compose"
            implementationClass = "ComposeConventionPlugin"
        }
    }
}
