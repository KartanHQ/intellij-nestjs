package com.nekofar.milad.intellij.nestjs.action.nestjscli

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vfs.VirtualFile
import com.nekofar.milad.intellij.nestjs.NestIcons
import com.nekofar.milad.intellij.nestjs.action.nestjscli.store.CLIStore.store
import java.nio.file.Files
import java.nio.file.Paths


class NestjsCliAction : AnAction(NestIcons.ProjectGenerator) {

    init {
       store.subscribe {
        println(store.state)
       }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return super.getActionUpdateThread()
    }


    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        val dialog = GenerateCLIDialog(project, e)
        val clickedOk = dialog.showAndGet()
        if (clickedOk) {
        }
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
