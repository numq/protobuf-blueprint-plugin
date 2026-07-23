plugins {
    `java-library`
    alias(libs.plugins.kotlin)
}

dependencies {
    api(projects.common.core)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}