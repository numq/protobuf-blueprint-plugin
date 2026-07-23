package io.github.numq.protobufblueprint.feature.generator.presentation

import io.github.numq.protobufblueprint.common.presentation.feature.*
import io.github.numq.protobufblueprint.feature.generator.core.usecase.ClearGenerator
import io.github.numq.protobufblueprint.feature.generator.core.usecase.CopyText
import io.github.numq.protobufblueprint.feature.generator.core.usecase.GenerateProto
import io.github.numq.protobufblueprint.feature.generator.core.usecase.ObserveGeneratorRecord
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

internal class GeneratorReducer(
    private val clearGenerator: ClearGenerator,
    private val copyText: CopyText,
    private val generateProto: GenerateProto,
    private val observeGeneratorRecord: ObserveGeneratorRecord,
) : Reducer<GeneratorState, GeneratorCommand, GeneratorEvent> {
    override fun reduce(state: GeneratorState, command: GeneratorCommand) = when (command) {
        is GeneratorCommand.HandleFailure -> transition(state).event(GeneratorEvent.HandleFailure(throwable = command.throwable))

        is GeneratorCommand.ObserveGeneratorRecord -> when (state) {
            is GeneratorState.Empty -> transition(GeneratorState.Loading).effect(
                action(key = command.key, fallback = GeneratorCommand::HandleFailure) {
                    observeGeneratorRecord().fold(
                        ifLeft = GeneratorCommand::HandleFailure,
                        ifRight = GeneratorCommand::ObserveGeneratorRecordSuccess
                    )
                })

            else -> transition(state)
        }

        is GeneratorCommand.ObserveGeneratorRecordSuccess -> transition(state).effect(
            stream(
                key = command.key,
                flow = command.flow.map(GeneratorCommand::UpdateGeneratorRecord),
                fallback = GeneratorCommand::HandleFailure
            )
        )

        is GeneratorCommand.UpdateGeneratorRecord -> transition(
            when (state) {
                is GeneratorState.Ready -> state.copy(record = command.record)

                else -> GeneratorState.Ready(record = command.record)
            }
        )

        is GeneratorCommand.GenerateProto -> when (state) {
            is GeneratorState.Ready -> transition(state).effect(
                action(
                    key = command.key, fallback = GeneratorCommand::HandleFailure
                ) {
                    delay(300.milliseconds)

                    generateProto(input = GenerateProto.Input(text = command.input)).fold(
                        ifLeft = GeneratorCommand::HandleFailure, ifRight = { GeneratorCommand.GenerateProtoSuccess })
                })

            else -> transition(state)
        }

        is GeneratorCommand.GenerateProtoSuccess -> transition(state)

        is GeneratorCommand.CopyOutput -> when (state) {
            is GeneratorState.Ready -> transition(state).effect(
                action(key = command.key, fallback = GeneratorCommand::HandleFailure) {
                    copyText(input = CopyText.Input(text = state.record.output)).fold(
                        ifLeft = GeneratorCommand::HandleFailure, ifRight = { GeneratorCommand.CopySuccess })
                })

            else -> transition(state)
        }

        is GeneratorCommand.CopySuccess -> when (state) {
            is GeneratorState.Ready -> transition(state.copy(copyFeedback = true)).effect(
                action(key = command.key, fallback = GeneratorCommand::HandleFailure) {
                    delay(1.seconds)

                    GeneratorCommand.ResetCopyFeedback
                })

            else -> transition(state)
        }

        is GeneratorCommand.ResetCopyFeedback -> when (state) {
            is GeneratorState.Ready -> transition(state.copy(copyFeedback = false))

            else -> transition(state)
        }
    }
}