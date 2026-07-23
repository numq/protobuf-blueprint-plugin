package io.github.numq.protobufblueprint.service.clipboard

import io.github.numq.protobufblueprint.common.core.di.ScopeQualifier
import io.github.numq.protobufblueprint.common.core.di.scopedOwner
import org.koin.dsl.bind
import org.koin.dsl.module

val clipboardServiceModule = module {
    scope<ScopeQualifier.Type.Application> {
        scopedOwner { DefaultClipboardService() } bind ClipboardService::class
    }
}