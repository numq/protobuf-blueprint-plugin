package io.github.numq.protobufblueprint.feature.generator.core.usecase

import arrow.core.raise.Raise
import io.github.numq.protobufblueprint.common.core.usecase.UseCase
import io.github.numq.protobufblueprint.feature.generator.core.GeneratorRecord
import io.github.numq.protobufblueprint.feature.generator.core.GeneratorService
import kotlinx.coroutines.flow.Flow

class ObserveGeneratorRecord(private val generatorService: GeneratorService) : UseCase.Query<Flow<GeneratorRecord>> {
    override suspend fun Raise<Throwable>.query() = generatorService.record
}