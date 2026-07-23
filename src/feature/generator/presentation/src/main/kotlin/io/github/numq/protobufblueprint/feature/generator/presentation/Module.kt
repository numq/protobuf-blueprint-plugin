package io.github.numq.protobufblueprint.feature.generator.presentation

import io.github.numq.protobufblueprint.common.core.di.ScopeQualifier
import io.github.numq.protobufblueprint.common.core.di.scopedOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val generatorFeaturePresentationModule = module {
    scope<ScopeQualifier.Type.Application> {
        scopedOwner {
            GeneratorReducer(
                clearGenerator = get(), copyText = get(), generateProto = get(), observeGeneratorRecord = get()
            )
        }

        scopedOwner {
            val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

            GeneratorFeature(scope = scope, reducer = get())
        }
    }
}