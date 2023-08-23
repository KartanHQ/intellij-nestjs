package com.nekofar.milad.intellij.nestjs.action.nestjscli.store

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class Action {
    data class GenerateCLIAction(
        val type: String,
        val options: String,
        val filePath: String,
        val project: Project,
        val generateInDir: VirtualFile,
        val closestModuleDir: VirtualFile
    )
    data class UpdateOptions(
        val options: String
    )
}
