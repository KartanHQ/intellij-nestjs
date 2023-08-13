package com.nekofar.milad.intellij.nestjs.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.layout.CCFlags
import com.nekofar.milad.intellij.nestjs.NestIcons
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.JComponent
import javax.swing.JTextField

class NestjsSchematicsAction : AnAction(NestIcons.ProjectGenerator) {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        val dialog = object : DialogWrapper(project) {
            private val textField = JTextField()

            override fun createCenterPanel(): JComponent {
                return panel {
                    row("Create Controller/Services/Guards:") {

                    }
                    row {
                        textField().focused()
                    }
                }
            }

            // Optional preview function
            init {
                init()
                textField.text = "Initial text"
            }
        }

        dialog.showAndGet()
    }

    override fun update(e: AnActionEvent) {
        // Display action only if it is a nest project.
        val project = e.project
        val isProjectOpen = project != null && !project.isDisposed
        var isFileExists = false

        if (isProjectOpen) {
            assert(project != null)
            val projectDirectory: VirtualFile = project!!.baseDir
            val filePath = Paths.get(projectDirectory.path, "nest-cli.json")
            isFileExists = Files.exists(filePath)
        }
        e.presentation.isEnabledAndVisible = isProjectOpen && isFileExists
    }

}
