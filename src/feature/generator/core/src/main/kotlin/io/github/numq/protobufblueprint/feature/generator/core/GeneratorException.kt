package io.github.numq.protobufblueprint.feature.generator.core

class GeneratorException(message: String, val line: Int? = null, val column: Int? = null) : Exception(message)