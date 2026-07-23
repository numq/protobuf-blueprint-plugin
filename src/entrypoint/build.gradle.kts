plugins {
    `java-library`
    alias(libs.plugins.kotlin)
    alias(libs.plugins.compose.compiler)
}

dependencies {
    api(projects.common.presentation)
    implementation(projects.feature.navigation.core)
    implementation(projects.feature.navigation.presentation)
    implementation(projects.feature.generator.core)
    implementation(projects.feature.generator.presentation)
    implementation(projects.service.clipboard)
    implementation(libs.koin.compose)
    implementation(libs.jewel.foundation)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}