plugins {
    `java-library`
    alias(libs.plugins.kotlin)
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    api(libs.arrow.core)
    api(libs.koin.core)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}