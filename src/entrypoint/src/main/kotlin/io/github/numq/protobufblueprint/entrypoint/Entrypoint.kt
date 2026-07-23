package io.github.numq.protobufblueprint.entrypoint

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import io.github.numq.protobufblueprint.common.core.di.ScopeQualifier
import io.github.numq.protobufblueprint.feature.generator.presentation.view.GeneratorView
import io.github.numq.protobufblueprint.feature.navigation.presentation.feature.NavigationView
import org.jetbrains.jewel.foundation.ExperimentalJewelApi
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.koin.compose.KoinIsolatedContext
import org.koin.core.KoinApplication

object Entrypoint {
    private const val APPLICATION_SCOPE_ID = "APPLICATION"

    @OptIn(ExperimentalJewelApi::class)
    @Composable
    fun Initialize(koinApp: KoinApplication) {
        KoinIsolatedContext(context = koinApp) {
            val koin = koinApp.koin

            val applicationScope = remember {
                val qualifier = ScopeQualifier.Application

                koin.getOrCreateScope(scopeId = APPLICATION_SCOPE_ID, qualifier = qualifier)
            }

            DisposableEffect(applicationScope.id) {
                onDispose {
                    applicationScope.close()
                }
            }

            val contentColor = JewelTheme.contentColor

            val panelBackground = JewelTheme.globalColors.panelBackground

            NavigationView(applicationScope = applicationScope, handleError = Throwable::printStackTrace, generator = {
                GeneratorView(
                    applicationScope = applicationScope,
                    handleError = Throwable::printStackTrace,
                    panelBackground = panelBackground,
                    contentColor = contentColor
                )
            })
        }
    }
}