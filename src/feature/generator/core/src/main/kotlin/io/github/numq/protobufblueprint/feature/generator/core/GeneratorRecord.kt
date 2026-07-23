package io.github.numq.protobufblueprint.feature.generator.core

data class GeneratorRecord(
    val input: String = "",
    val output: String = "",
    val error: String? = null,
    val isProcessing: Boolean = false,
)