package io.github.numq.protobufblueprint.feature.generator.core.usecase

import arrow.core.raise.Raise
import io.github.numq.protobufblueprint.common.core.usecase.UseCase
import io.github.numq.protobufblueprint.feature.generator.core.GeneratorService

class GenerateProto(private val generatorService: GeneratorService) : UseCase.Command<GenerateProto.Input> {
    data class Input(val text: String)

    override suspend fun Raise<Throwable>.command(input: Input) = with(input) {
        generatorService.generate(input = text).bind()
    }
}