package io.github.numq.protobufblueprint.feature.generator.presentation

import io.github.numq.protobufblueprint.common.presentation.feature.Feature
import kotlinx.coroutines.CoroutineScope

internal class GeneratorFeature(
    scope: CoroutineScope, reducer: GeneratorReducer,
) : Feature<GeneratorState, GeneratorCommand, GeneratorEvent> by Feature(
    initialState = GeneratorState.Empty,
    initialCommands = arrayOf(GeneratorCommand.ObserveGeneratorRecord),
    scope = scope,
    reducer = reducer
)