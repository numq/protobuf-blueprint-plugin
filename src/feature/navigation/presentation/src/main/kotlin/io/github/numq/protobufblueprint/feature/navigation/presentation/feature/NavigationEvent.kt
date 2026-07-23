package io.github.numq.protobufblueprint.feature.navigation.presentation.feature

internal sealed interface NavigationEvent {
    data class HandleFailure(val throwable: Throwable) : NavigationEvent
}