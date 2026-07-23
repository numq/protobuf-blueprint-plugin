rootProject.name = "protobuf-blueprint"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://cache-redirector.jetbrains.com/intellij-dependencies")
        maven("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies")
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://cache-redirector.jetbrains.com/intellij-dependencies")
        maven("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies")
        maven("https://www.jetbrains.com/intellij-repository/releases")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

val rootModules = listOf("common", "entrypoint", "feature", "platform", "service")

val commonModules = listOf("core", "presentation")

val featureModules = listOf("generator", "navigation")

val featureSubmodules = listOf("core", "presentation")

val platformModules = listOf("intellij")

val serviceModules = listOf("clipboard")

rootModules.forEach { moduleName ->
    include(":$moduleName")
    project(":$moduleName").projectDir = file("src/$moduleName")
}

commonModules.forEach { moduleName ->
    val path = ":common:$moduleName"
    include(path)
    project(path).projectDir = file("src/common/$moduleName")
}

featureModules.forEach { moduleName ->
    val featureParent = ":feature:$moduleName"

    featureSubmodules.forEach { submoduleName ->
        val submodule = "$featureParent:$submoduleName"
        include(submodule)
        project(submodule).projectDir = file("src/feature/$moduleName/$submoduleName")
    }
}

platformModules.forEach { moduleName ->
    val path = ":platform:$moduleName"
    include(path)
    project(path).projectDir = file("src/platform/$moduleName")
}

serviceModules.forEach { moduleName ->
    val path = ":service:$moduleName"
    include(path)
    project(path).projectDir = file("src/service/$moduleName")
}