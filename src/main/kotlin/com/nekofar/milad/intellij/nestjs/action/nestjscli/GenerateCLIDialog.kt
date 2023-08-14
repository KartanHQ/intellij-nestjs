package com.nekofar.milad.intellij.nestjs.action.nestjscli

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.ComboboxSpeedSearch
import com.intellij.ui.TextFieldWithAutoCompletion
import com.intellij.ui.components.JBLabel
import com.intellij.ui.dsl.builder.TopGap
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.nekofar.milad.intellij.nestjs.action.nestjscli.store.Action
import com.nekofar.milad.intellij.nestjs.action.nestjscli.store.CLIStore.store
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent

class GenerateCLIDialog(private val project: Project?, e: AnActionEvent) : DialogWrapper(project) {
    private val autoCompleteField = TextFieldWithAutoCompletion(
        project,
        CLIOptionsCompletionProvider(CLIOptionsCompletionProvider.options.keys.toList()), false,
        null
    ).apply {
        setPlaceholder("filename --options")
    }

    private val pathLabel = JBLabel()

    private val comboBoxModel = DefaultComboBoxModel(
        CLIOptionsCompletionProvider.generateItems.keys.toTypedArray()
    )
    private val comboBox = ComboBox(comboBoxModel).apply {
        setRenderer(GenerateTypeComboRenderer())
    }
    private val virtualFile: VirtualFile = e.getRequiredData(CommonDataKeys.VIRTUAL_FILE)
    private val directory = when {
        virtualFile.isDirectory -> virtualFile // If it's directory, use it
        else -> virtualFile.parent // Otherwise, get its parent directory
    }

    init {
        title = "Nest CLI/Schematics Generate"
        val state = store.getState()
        comboBox.item = state.type
        autoCompleteField.text = state.parameter
        pathLabel.text = directory.path
        pathLabel.icon = AllIcons.Actions.GeneratedFolder
        init()
        ComboboxSpeedSearch(comboBox)
    }

    override fun createCenterPanel(): JComponent {
        return panel {
            row("Path:") {}
            row {
                cell(pathLabel).horizontalAlign(
                    HorizontalAlign.FILL
                )
            }
            row("Type:") {}.topGap(TopGap.SMALL)
            row {
                cell(comboBox).horizontalAlign(HorizontalAlign.FILL)
            }
            row("Parameters:") {}.topGap(TopGap.SMALL)
            row {
                cell(autoCompleteField).horizontalAlign(HorizontalAlign.FILL)
                rowComment(" Filename --options")
            }
        }
    }

    override fun doValidate(): ValidationInfo? {
        val fileName = autoCompleteField.text.split(" ")[0]
        var invalidFileName = false
        if (fileName.isNotBlank() && fileName.startsWith("-", ignoreCase = true)) {
            invalidFileName = true
        }
        return if (fileName.isBlank() || autoCompleteField.text.isBlank() ) {
            ValidationInfo("Filename cannot be blank", autoCompleteField)
        } else if(invalidFileName) {
            ValidationInfo("$fileName in an invalid filename", autoCompleteField)
        } else null
    }

    override fun doOKAction() {
        store.dispatch(
            Action.GenerateCLIAction(
                type = comboBox.item,
                options = autoCompleteField.text,
                filePath = directory.path,
                project = project!!,
                workingDir = directory
            )
        )
        super.doOKAction()
    }

}
