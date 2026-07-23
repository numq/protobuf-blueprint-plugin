package io.github.numq.protobufblueprint.feature.generator.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.numq.protobufblueprint.feature.generator.presentation.GeneratorEvent
import io.github.numq.protobufblueprint.feature.generator.presentation.GeneratorFeature
import io.github.numq.protobufblueprint.feature.generator.presentation.GeneratorState
import org.jetbrains.jewel.ui.component.CircularProgressIndicator
import org.koin.compose.koinInject
import org.koin.core.scope.Scope

@Composable
fun GeneratorView(
    applicationScope: Scope, handleError: (Throwable) -> Unit, panelBackground: Color, contentColor: Color
) {
    val feature = koinInject<GeneratorFeature>(scope = applicationScope)

    val state by feature.state.collectAsState()

    LaunchedEffect(Unit) {
        feature.events.collect { event ->
            when (event) {
                is GeneratorEvent.HandleFailure -> handleError(event.throwable)
            }
        }
    }

    when (val currentState = state) {
        is GeneratorState.Empty, is GeneratorState.Loading -> {
            Box(modifier = Modifier.fillMaxSize().background(panelBackground), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is GeneratorState.Ready -> GeneratorViewReady(
            state = currentState, feature = feature, panelBackground = panelBackground, contentColor = contentColor
        )
    }
}