package com.nekofar.milad.intellij.nestjs.config

import com.intellij.ide.IconProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.nekofar.milad.intellij.nestjs.NestIcons
import javax.swing.Icon

class NestConfigIconProvider : IconProvider(), DumbAware {
    override fun getIcon(element: PsiElement, flags: Int): Icon? {
        val fileElement = element as? PsiFile
        return if (fileElement != null) {
            when {
                fileElement.name.equals("nest-cli.json", true) -> NestIcons.FileType
                else -> null
            }
        } else null
    }
}