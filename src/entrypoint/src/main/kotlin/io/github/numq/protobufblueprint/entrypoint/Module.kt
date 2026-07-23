package io.github.numq.protobufblueprint.entrypoint

import io.github.numq.protobufblueprint.feature.generator.core.generatorFeatureCoreModule
import io.github.numq.protobufblueprint.feature.generator.presentation.generatorFeaturePresentationModule
import io.github.numq.protobufblueprint.feature.navigation.core.navigationFeatureCoreModule
import io.github.numq.protobufblueprint.feature.navigation.presentation.navigationFeaturePresentationModule
import io.github.numq.protobufblueprint.service.clipboard.clipboardServiceModule
import org.koin.dsl.module

private val feature = module {
    includes(generatorFeatureCoreModule)
    includes(generatorFeaturePresentationModule)
    includes(navigationFeatureCoreModule)
    includes(navigationFeaturePresentationModule)
}

private val service = module {
    includes(clipboardServiceModule)
}

val applicationModule = feature + service