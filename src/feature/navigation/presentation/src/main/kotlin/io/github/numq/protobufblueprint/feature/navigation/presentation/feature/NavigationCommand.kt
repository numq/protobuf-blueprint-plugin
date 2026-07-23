package io.github.numq.protobufblueprint.feature.navigation.presentation.feature

import io.github.numq.protobufblueprint.feature.navigation.core.Destination

internal sealed interface NavigationCommand {
    data class HandleFailure(val throwable: Throwable) : NavigationCommand

    data class NavigateTo(val destination: Destination) : NavigationCommand
}