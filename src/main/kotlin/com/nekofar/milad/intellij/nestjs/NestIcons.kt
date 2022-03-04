package com.nekofar.milad.intellij.nestjs

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

@Suppress("unused")
object NestIcons {
    @JvmField
    val FileType = IconLoader.getIcon("/icons/nest.svg", javaClass)

    @JvmField
    val ProjectGenerator: Icon = IconLoader.getIcon("/icons/nest.png", javaClass)
}
