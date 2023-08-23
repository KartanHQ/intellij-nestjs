package com.nekofar.milad.intellij.nestjs.action.nestjscli

import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.SimpleTextAttributes
import com.jetbrains.rd.util.first
import javax.swing.JList

class GenerateTypeComboRenderer: ColoredListCellRenderer<String>() {
    override fun customizeCellRenderer(
        list: JList<out String>,
        value: String?,
        index: Int,
        selected: Boolean,
        hasFocus: Boolean
    ) {
        if (value != null) {
            val desc = CLIOptionsCompletionProvider.generateItems.
            filter { it.key == value }.first().value
            append(value)
            append("    ", SimpleTextAttributes.REGULAR_ATTRIBUTES)
            append(desc, SimpleTextAttributes.GRAY_ATTRIBUTES)
            setSize(list.width, Int.MAX_VALUE)
        }
    }
}
