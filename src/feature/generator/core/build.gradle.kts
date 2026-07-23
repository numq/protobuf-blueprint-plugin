plugins {
    alias(libs.plugins.kotlin)
}

dependencies {
    implementation(projects.common.core)
    implementation(libs.kotlinpoet)
    implementation(libs.protobuf.java)
    implementation(libs.protobuf.java.util)
    implementation(projects.service.clipboard)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}