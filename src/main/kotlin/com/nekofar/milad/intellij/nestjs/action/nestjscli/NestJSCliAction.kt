package com.nekofar.milad.intellij.nestjs.action.nestjscli

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.ComboboxSpeedSearch
import com.intellij.ui.TextFieldWithAutoCompletion
import com.intellij.ui.dsl.builder.TopGap
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.nekofar.milad.intellij.nestjs.NestIcons
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent
import javax.swing.JTextField

class NestjsCliAction : AnAction(NestIcons.ProjectGenerator) {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return super.getActionUpdateThread()
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        val placeholderText = "Filename --options"
        val dialog = object : DialogWrapper(project) {
            private val textField = JTextField()
            private val autoCompleteField = TextFieldWithAutoCompletion<String>(
                project,
                CliOptionsCompletionProvider(CliOptionsCompletionProvider.options.keys.toList()), false,
                null
            )

            private val comboBoxModel = DefaultComboBoxModel(
                CliOptionsCompletionProvider.generateItems.keys.toTypedArray()
            )
            private val comboBox = ComboBox(comboBoxModel).apply {
                setRenderer(GenerateTypeComboRenderer())
            }

            override fun createCenterPanel(): JComponent {
                return panel {
                    row("Generate Controller/Services/Guards/etc:") {}
                    row("Type:") {}
                    row {
                        cell(comboBox).horizontalAlign(HorizontalAlign.FILL)
                    }
                    row("Parameters:") {}
                    row {
                        cell(autoCompleteField).horizontalAlign(HorizontalAlign.FILL)
                        rowComment("Filename --options").topGap(TopGap.SMALL)
                    }
                }
            }

            // Optional preview function
            init {
                init()
                ComboboxSpeedSearch(comboBox)
            }
        }.apply {
            title = "NestJS Generate"
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
