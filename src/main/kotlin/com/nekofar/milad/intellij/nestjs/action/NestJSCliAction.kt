package com.nekofar.milad.intellij.nestjs.action

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.lookup.CharFilter
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.ComboboxSpeedSearch
import com.intellij.ui.EditorTextField
import com.intellij.ui.TextFieldWithAutoCompletion
import com.intellij.ui.TextFieldWithAutoCompletionListProvider
import com.intellij.ui.dsl.builder.TopGap
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.nekofar.milad.intellij.nestjs.NestIcons
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.*


class NestJSCliCompleteProvider(private val items: List<String>) : TextFieldWithAutoCompletionListProvider<String>(items) {

    override fun getLookupString(item: String): String {
        return item
    }

    override fun getItems(prefix: String?, cached: Boolean, parameters: CompletionParameters?): List<String> {
        println(prefix)
        return if (prefix == null) emptyList() else items.filter { it.contains(prefix, ignoreCase = true) }
    }

    override fun acceptChar(c: Char): CharFilter.Result? {
        return if (c == '-') CharFilter.Result.ADD_TO_PREFIX else null
    }

    override fun compare(left: String?, right: String?): Int {
        return when {
            left == null -> -1
            right == null -> 1
            else -> left.compareTo(right)
        }
    }
}

class NestjsCliAction : AnAction(NestIcons.ProjectGenerator) {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        val dialog = object : DialogWrapper(project) {
            private val textField = JTextField()
            private val items1 = listOf("application",
                    "class",
                    "configuration",
                    "controller",
                    "decorator",
                    "filter",
                    "gateway",
                    "guard",
                    "interceptor",
                    "interface",
                    "library",
                    "middleware",
                    "module",
                    "pipe",
                    "provider",
                    "resolver",
                    "resource",
                    "service",
                    "sub-app")
            val options = listOf(
                    "--dry-run","--project", "--flat",
                    "--no-flat", "--spec", "--skip-import", "--no-spec"
            )
            private val autoCompleteField = TextFieldWithAutoCompletion(
                    project, NestJSCliCompleteProvider(options), false, null
            )
            private val comboBoxModel = DefaultComboBoxModel(
                    items1.toTypedArray()
            )
            private val comboBox = ComboBox(comboBoxModel)

            override fun createCenterPanel(): JComponent {
                return panel {
                    row("Generate Controller/Services/Guards/etc:") {

                    }
                    row("Type:") {

                    }
                    row {
                        cell(comboBox).horizontalAlign(
                                HorizontalAlign.FILL
                        )
                    }
                    row("Options:") {
                    }
                    row {
                        cell(autoCompleteField).horizontalAlign(HorizontalAlign.FILL)
                        rowComment("filename --option").topGap(TopGap.SMALL)
                    }
                }
            }

            // Optional preview function
            init {
                init()
                val options = listOf(
                        "-d", "--dry-run", "-p","--project", "--flat",
                        "--no-flat", "--spec", "--skip-import", "--no-spec"
                )
                textField.text = "name --options"
                ComboboxSpeedSearch(comboBox)
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
