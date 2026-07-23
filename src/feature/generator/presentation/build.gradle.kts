plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.compose.compiler)
}

dependencies {
    implementation(projects.common.presentation)
    implementation(projects.feature.generator.core)
    implementation(libs.koin.compose)
    implementation(libs.jewel.foundation)
    implementation(libs.jewel.ui)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}