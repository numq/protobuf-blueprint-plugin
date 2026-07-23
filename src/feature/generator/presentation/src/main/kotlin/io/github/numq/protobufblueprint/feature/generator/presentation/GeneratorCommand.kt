package io.github.numq.protobufblueprint.feature.generator.presentation

import io.github.numq.protobufblueprint.feature.generator.core.GeneratorRecord
import kotlinx.coroutines.flow.Flow

internal sealed interface GeneratorCommand {
    enum class Key {
        OBSERVE_GENERATOR_RECORD, OBSERVE_GENERATOR_RECORD_SUCCESS, GENERATE_PROTO, COPY_OUTPUT, COPY_OUTPUT_SUCCESS
    }

    data class HandleFailure(val throwable: Throwable) : GeneratorCommand

    data object ObserveGeneratorRecord : GeneratorCommand {
        val key = Key.OBSERVE_GENERATOR_RECORD
    }

    data class ObserveGeneratorRecordSuccess(val flow: Flow<GeneratorRecord>) : GeneratorCommand {
        val key = Key.OBSERVE_GENERATOR_RECORD_SUCCESS
    }

    data class UpdateGeneratorRecord(val record: GeneratorRecord) : GeneratorCommand

    data class GenerateProto(val input: String) : GeneratorCommand {
        val key = Key.GENERATE_PROTO
    }

    data object GenerateProtoSuccess : GeneratorCommand

    data object CopyOutput : GeneratorCommand {
        val key = Key.COPY_OUTPUT
    }

    data object CopySuccess : GeneratorCommand {
        val key = Key.COPY_OUTPUT_SUCCESS
    }

    data object ResetCopyFeedback : GeneratorCommand
}