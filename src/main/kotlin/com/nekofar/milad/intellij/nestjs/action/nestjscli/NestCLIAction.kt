package com.nekofar.milad.intellij.nestjs.action.nestjscli

import com.intellij.javascript.nodejs.CompletionModuleInfo
import com.intellij.javascript.nodejs.NodeModuleSearchUtil
import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterManager
import com.intellij.javascript.nodejs.util.NodePackage
import com.intellij.lang.javascript.JavaScriptBundle
import com.intellij.lang.javascript.boilerplate.NpmPackageProjectGenerator
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.nekofar.milad.intellij.nestjs.NestIcons
import com.nekofar.milad.intellij.nestjs.action.nestjscli.store.Action
import com.nekofar.milad.intellij.nestjs.action.nestjscli.store.CLIStore.store
import com.nekofar.milad.intellij.nestjs.action.nestjscli.store.GenerateCLIState
import java.nio.file.Files
import java.nio.file.Paths


class NestjsCliAction : DumbAwareAction(NestIcons.ProjectGenerator) {

    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        val dialog = GenerateCLIDialog(project!!, e)
        val clickedOk = dialog.showAndGet()
        if (clickedOk) {
            if (ApplicationInfo.getInstance().majorVersion.toInt() >= 2023) {
                runGenerator(store.state.project!!, store.state)
            } else {
                ApplicationManager.getApplication().executeOnPooledThread {
                    ApplicationManager.getApplication().invokeLater {
                        runGenerator(store.state.project!!, store.state)
                    }
                }
            }
        }
    }

    override fun update(e: AnActionEvent) {
        // Display action only if it is a nest project.
        val project = e.project
        val isProjectOpen = project != null && !project.isDisposed
        var isFileExists = false

        if (isProjectOpen) {
            assert(project != null)
            val projectDirectory: VirtualFile = project!!.guessProjectDir()!!
            val filePath = Paths.get(projectDirectory.path, "nest-cli.json")
            isFileExists = Files.exists(filePath)
        }
        e.presentation.isEnabledAndVisible = isProjectOpen && isFileExists
    }

    private fun runGenerator(
        project: Project,
        schematic: GenerateCLIState
    ) {
        val interpreter = NodeJsInterpreterManager.getInstance(project).interpreter ?: return

        val modules: MutableList<CompletionModuleInfo> = mutableListOf()
        val cli: VirtualFile = project.guessProjectDir()!!

        NodeModuleSearchUtil.findModulesWithName(modules, "@nestjs/cli", cli, null)

        val module = modules.firstOrNull() ?: return
        val parameters = schematic.parameter.split(" ")
            .toMutableList()
            .map { it.trim() }
            .filter { it != "" }
            .toMutableList()

        store.dispatch(Action.UpdateOptions(parameters.joinToString(" ")))

        var generateFileWithFullPath = parameters.removeAt(0)
        var runCommandInDir = project.guessProjectDir()

        if (schematic.type != "app" &&
            schematic.type != "sub-app" &&
            schematic.type != "library"
        ) {
            runCommandInDir = schematic.closestModuleDir
            generateFileWithFullPath = "${schematic.filePath}/$generateFileWithFullPath"
        }

        NpmPackageProjectGenerator.generate(
            interpreter, NodePackage(module.virtualFile?.path!!),
            { pkg -> pkg.findBinFile("nest", null)?.absolutePath },
            cli, VfsUtilCore.virtualToIoFile(runCommandInDir ?: cli), project,
            null, JavaScriptBundle.message("generating.0", cli.name),
            arrayOf(), "generate", schematic.type,
            generateFileWithFullPath,
            *parameters.toTypedArray(),
        )

    }
}
