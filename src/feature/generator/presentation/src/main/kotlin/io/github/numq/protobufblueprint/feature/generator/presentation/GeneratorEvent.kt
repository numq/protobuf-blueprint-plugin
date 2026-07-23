package io.github.numq.protobufblueprint.feature.generator.presentation

internal sealed interface GeneratorEvent {
    data class HandleFailure(val throwable: Throwable) : GeneratorEvent
}