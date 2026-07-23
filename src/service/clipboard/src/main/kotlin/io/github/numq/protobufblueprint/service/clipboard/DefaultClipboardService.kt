package io.github.numq.protobufblueprint.service.clipboard

import arrow.core.Either
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable

internal class DefaultClipboardService : ClipboardService {
    override suspend fun copyToClipboard(text: String) = Either.catch {
        val contents = object : Transferable {
            override fun getTransferDataFlavors() = arrayOf(DataFlavor.stringFlavor)

            override fun isDataFlavorSupported(flavor: DataFlavor?) = true

            override fun getTransferData(flavor: DataFlavor?) = text
        }

        Toolkit.getDefaultToolkit().systemClipboard.setContents(contents, null)
    }
}