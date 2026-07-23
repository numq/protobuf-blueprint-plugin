fun properties(key: String) = providers.gradleProperty(key)

plugins {
    alias(libs.plugins.changelog)
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.compose.compiler) apply false
}

group = properties("pluginGroup").get()
version = properties("pluginVersion").get()

tasks {
    wrapper {
        gradleVersion = providers.gradleProperty("gradleVersion").get()
    }
}