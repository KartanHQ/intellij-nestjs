package com.nekofar.milad.intellij.nestjs.action.nestjscli

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.ComboboxSpeedSearch
import com.intellij.ui.TextFieldWithAutoCompletion
import com.intellij.ui.dsl.builder.TopGap
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent
import javax.swing.JTextField

class GenerateCLIDialog(project: Project?): DialogWrapper(project) {
    private val textField = JTextField()
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

}
