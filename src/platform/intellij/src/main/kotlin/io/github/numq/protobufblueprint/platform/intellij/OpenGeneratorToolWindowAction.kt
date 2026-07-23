package io.github.numq.protobufblueprint.platform.intellij

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.wm.ToolWindowManager

internal class OpenGeneratorToolWindowAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Protobuf Blueprint")

        toolWindow?.show(null)
    }
}