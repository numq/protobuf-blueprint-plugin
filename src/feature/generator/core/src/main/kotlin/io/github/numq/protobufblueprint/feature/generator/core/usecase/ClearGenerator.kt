package io.github.numq.protobufblueprint.feature.generator.core.usecase

import arrow.core.raise.Raise
import io.github.numq.protobufblueprint.common.core.usecase.UseCase
import io.github.numq.protobufblueprint.feature.generator.core.GeneratorService

class ClearGenerator(private val generatorService: GeneratorService) : UseCase.Action {
    override suspend fun Raise<Throwable>.action() = generatorService.clear().bind()
}