package com.nekofar.milad.intellij.nestjs.action.nestjscli

import com.jetbrains.rd.util.first
import java.awt.Component
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.ListCellRenderer

class GenerateTypeComboRenderer: ListCellRenderer<String> {
    private val myLabel = JLabel()
    override fun getListCellRendererComponent(list: JList<out String>?, value: String?, index: Int,
                                              isSelected: Boolean, cellHasFocus: Boolean): Component {
        if (value != null) {
            val desc = CliOptionsCompletionProvider.generateItems.
                                filter { it.key == value }.first().value
            myLabel.setText("<html>$value <font color='gray'> $desc </font></html>")
        }
        return myLabel
    }
}
