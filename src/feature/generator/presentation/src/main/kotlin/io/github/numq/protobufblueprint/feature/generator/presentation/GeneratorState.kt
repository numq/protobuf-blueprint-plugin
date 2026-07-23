package io.github.numq.protobufblueprint.feature.generator.presentation

import io.github.numq.protobufblueprint.feature.generator.core.GeneratorRecord

internal sealed interface GeneratorState {
    data object Empty : GeneratorState

    data object Loading : GeneratorState

    data class Ready(
        val record: GeneratorRecord = GeneratorRecord(), val copyFeedback: Boolean = false
    ) : GeneratorState
}