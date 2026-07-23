package io.github.numq.protobufblueprint.feature.generator.core

import io.github.numq.protobufblueprint.common.core.di.ScopeQualifier
import io.github.numq.protobufblueprint.common.core.di.scopedOwner
import io.github.numq.protobufblueprint.feature.generator.core.usecase.ClearGenerator
import io.github.numq.protobufblueprint.feature.generator.core.usecase.CopyText
import io.github.numq.protobufblueprint.feature.generator.core.usecase.GenerateProto
import io.github.numq.protobufblueprint.feature.generator.core.usecase.ObserveGeneratorRecord
import org.koin.dsl.bind
import org.koin.dsl.module

val generatorFeatureCoreModule = module {
    scope<ScopeQualifier.Type.Application> {
        scopedOwner { DefaultGeneratorService() } bind GeneratorService::class

        scopedOwner { ClearGenerator(generatorService = get()) }

        scopedOwner { CopyText(clipboardService = get()) }

        scopedOwner { GenerateProto(generatorService = get()) }

        scopedOwner { ObserveGeneratorRecord(generatorService = get()) }
    }
}