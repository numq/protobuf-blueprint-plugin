package io.github.numq.protobufblueprint.feature.navigation.core

sealed interface Destination {
    data object Generator : Destination
}