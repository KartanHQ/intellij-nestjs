package com.nekofar.milad.intellij.nestjs.action.nestjscli

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.ComboboxSpeedSearch
import com.intellij.ui.TextFieldWithAutoCompletion
import com.intellij.ui.dsl.builder.TopGap
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.nekofar.milad.intellij.nestjs.action.nestjscli.store.Action
import com.nekofar.milad.intellij.nestjs.action.nestjscli.store.CLIStore.store
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent

class GenerateCLIDialog(project: Project?, val e: AnActionEvent): DialogWrapper(project) {
    private val autoCompleteField = TextFieldWithAutoCompletion<String>(
        project,
        CLIOptionsCompletionProvider(CLIOptionsCompletionProvider.options.keys.toList()), false,
        null
    ).apply {
        setPlaceholder("filename --options")
    }

    private val comboBoxModel = DefaultComboBoxModel(
        CLIOptionsCompletionProvider.generateItems.keys.toTypedArray()
    )
    private val comboBox = ComboBox(comboBoxModel).apply {
        setRenderer(GenerateTypeComboRenderer())
    }

    init {
        title = "Nest CLI/Schematics Generate"
        val state = store.getState()
        comboBox.item = state.type
        autoCompleteField.text = state.parameter
        init()
        ComboboxSpeedSearch(comboBox)
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

    override fun doOKAction() {
        val virtualFile: VirtualFile = e.getRequiredData(CommonDataKeys.VIRTUAL_FILE)
        val directory = when {
            virtualFile.isDirectory -> virtualFile // If it's directory, use it
            else -> virtualFile.parent // Otherwise, get its parent directory
        }
        store.dispatch(Action.GenerateCLIAction(
            type = comboBox.item,
            options = autoCompleteField.text,
            filePath = directory.path)
        )
        super.doOKAction()
    }

}
