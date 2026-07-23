package io.github.numq.protobufblueprint.platform.intellij

import androidx.compose.runtime.*
import androidx.compose.ui.awt.ComposePanel
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBColor
import com.intellij.ui.content.ContentFactory
import io.github.numq.protobufblueprint.entrypoint.Entrypoint
import io.github.numq.protobufblueprint.entrypoint.applicationModule
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import org.koin.dsl.koinApplication

internal class GeneratorToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val koinApp = koinApplication {
            modules(applicationModule)
        }

        Disposer.register(toolWindow.disposable) {
            koinApp.close()
        }

        val composePanel = ComposePanel().apply {
            setContent {
                var isDark by remember { mutableStateOf(JBColor.isBright().not()) }

                DisposableEffect(Unit) {
                    val connection = ApplicationManager.getApplication().messageBus.connect(toolWindow.disposable)

                    val topic = LafManagerListener.TOPIC

                    val handler = LafManagerListener {
                        isDark = JBColor.isBright().not()
                    }

                    connection.subscribe(topic, handler)

                    onDispose(connection::disconnect)
                }

                IntUiTheme(isDark = isDark) {
                    Entrypoint.Initialize(koinApp = koinApp)
                }
            }
        }

        val content = ContentFactory.getInstance().createContent(composePanel, null, false)

        toolWindow.contentManager.addContent(content)
    }
}