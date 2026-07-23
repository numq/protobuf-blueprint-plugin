package io.github.numq.protobufblueprint.service.clipboard

import arrow.core.Either

interface ClipboardService {
    suspend fun copyToClipboard(text: String): Either<Throwable, Unit>
}