package io.github.numq.protobufblueprint.feature.generator.core

import arrow.core.Either
import kotlinx.coroutines.flow.StateFlow

interface GeneratorService {
    val record: StateFlow<GeneratorRecord>

    suspend fun generate(input: String): Either<Throwable, Unit>

    suspend fun clear(): Either<Throwable, Unit>
}