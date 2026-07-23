plugins {
    alias(libs.plugins.kotlin)
}

dependencies {
    implementation(projects.common.core)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}