package io.github.numq.protobufblueprint.feature.navigation.presentation.feature

import io.github.numq.protobufblueprint.common.presentation.feature.Reducer
import io.github.numq.protobufblueprint.common.presentation.feature.event

internal class NavigationReducer : Reducer<NavigationState, NavigationCommand, NavigationEvent> {
    override fun reduce(state: NavigationState, command: NavigationCommand) = when (command) {
        is NavigationCommand.HandleFailure -> transition(state).event(NavigationEvent.HandleFailure(throwable = command.throwable))

        is NavigationCommand.NavigateTo -> transition(state.copy(destination = command.destination))
    }
}