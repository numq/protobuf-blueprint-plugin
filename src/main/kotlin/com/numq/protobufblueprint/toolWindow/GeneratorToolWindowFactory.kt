package com.numq.protobufblueprint.toolWindow

import com.intellij.icons.AllIcons
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBColor
import com.intellij.ui.content.ContentFactory
import com.intellij.util.ui.JBUI
import com.numq.protobufblueprint.AppBundle
import com.numq.protobufblueprint.forms.RootForm
import com.numq.protobufblueprint.services.GeneratorService
import com.numq.protobufblueprint.standard.ProtobufStandard
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.event.ActionListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.DefaultHighlighter


class GeneratorToolWindowFactory : ToolWindowFactory {

    private val contentFactory = ContentFactory.getInstance()

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val content = contentFactory
            .createContent(
                GeneratorToolWindow(toolWindow)
                    .getContent(), null, false
            )
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class GeneratorToolWindow(toolWindow: ToolWindow) {

        private val generator = toolWindow.project.service<GeneratorService>()
        private val errorHighlighter = DefaultHighlighter.DefaultHighlightPainter(JBColor.YELLOW)

        private var inputTextAreaDocumentListener: DocumentListener? = null
        private var highlightTooltipTextListener: MouseAdapter? = null
        private var generatedTextAreaDocumentListener: DocumentListener? = null
        private var copyButtonActionListener: ActionListener? = null

        fun getContent() = RootForm().apply {
            hintLabel.apply {
                text = """
                    <html>
                        <body style='width: 200px;'>
                            <p style='color:gray; font-size:10px; margin-top:0px; margin-bottom:0px;'>
                                Format Requirements:
                            </p>
                            <ul style='color:gray; font-size:10px; margin-top:0px; margin-bottom:0px;'>
                                <li>message nameA nameB<br><i>message token userData</i></li>
                                <li>enum name value_a value_b<br><i>enum token access refresh self_signed</i></li>
                                <li>service name rpcA rpcB<br><i>service user getById create delete</i></li>
                            </ul>
                        </body>
                    </html>
                """.trimIndent()
            }
            inputTextArea.apply {
                border = JBUI.Borders.empty(8)
                inputTextAreaDocumentListener?.let(document::removeDocumentListener)
                document.addDocumentListener(object : DocumentListener {
                    override fun changedUpdate(e: DocumentEvent?) {
                        update()
                    }

                    override fun insertUpdate(e: DocumentEvent?) {
                        update()
                    }

                    override fun removeUpdate(e: DocumentEvent?) {
                        update()
                    }

                    private fun update() {
                        runCatching {
                            val generatedText = generator.generate(text)
                            generatedTextArea.apply {
                                text = generatedText.takeIf { it.isNotBlank() }
                                    ?: AppBundle.message("generatedTextPlaceholder")
                                highlighter.apply {
                                    removeAllHighlights()
                                    text.split(Regex("\n+"))
                                        .filter { it.length > ProtobufStandard.MAX_LINE_LENGTH }.forEach {
                                            val start = text.indexOf(it)
                                            val end = start + it.length
                                            addHighlight(start, end, errorHighlighter)
                                        }
                                }
                                highlightTooltipTextListener?.let(::removeMouseMotionListener)
                                if (generatedText.isNotBlank()) {
                                    addMouseMotionListener(object : MouseAdapter() {
                                        override fun mouseMoved(e: MouseEvent) {
                                            val pos: Int = viewToModel2D(e.point)
                                            val highlighter = highlighter
                                            val highlights = highlighter.highlights
                                            for (h in highlights) {
                                                if (pos >= h.startOffset && pos < h.endOffset) {
                                                    toolTipText = AppBundle.message("exceededLengthOfLineTooltip")
                                                    return
                                                }
                                            }
                                            toolTipText = null
                                        }
                                    }.apply {
                                        highlightTooltipTextListener = this
                                    })
                                }
                            }
                        }.onFailure(thisLogger()::warn)
                    }
                }.apply {
                    inputTextAreaDocumentListener = this
                })
            }
            generatedTextArea.apply {
                val generatedTextPlaceholder = AppBundle.message("generatedTextPlaceholder")
                border = JBUI.Borders.empty(8)
                text = generatedTextPlaceholder
                generatedTextAreaDocumentListener?.let(document::removeDocumentListener)
                document.addDocumentListener(object : DocumentListener {
                    override fun changedUpdate(e: DocumentEvent?) {
                        update()
                    }

                    override fun insertUpdate(e: DocumentEvent?) {
                        update()
                    }

                    override fun removeUpdate(e: DocumentEvent?) {
                        update()
                    }

                    private fun update() {
                        copyButton.isEnabled = text.isNotBlank() && text != generatedTextPlaceholder
                    }
                }.apply {
                    generatedTextAreaDocumentListener = this
                })
            }
            copyButton.apply {
                icon = AllIcons.Actions.Copy
                copyButtonActionListener?.let(::removeActionListener)
                addActionListener(ActionListener {
                    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                    val selection = StringSelection(generatedTextArea.text)
                    clipboard.setContents(selection, null)
                }.apply {
                    copyButtonActionListener = this
                })
            }
        }.rootPanel
    }
}