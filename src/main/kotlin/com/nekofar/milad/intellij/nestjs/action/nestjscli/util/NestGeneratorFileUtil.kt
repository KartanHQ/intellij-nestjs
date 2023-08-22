package com.nekofar.milad.intellij.nestjs.action.nestjscli.util

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VirtualFile
import java.nio.file.Paths

object NestGeneratorFileUtil {

    fun getFilePath(project: Project, e: AnActionEvent, workingDir: VirtualFile): String {
        return getPathDifference(
            findClosestModuleFileDir(project, e, workingDir).path, workingDir.path
        )
    }

    fun findClosestModuleFileDir(project: Project, e: AnActionEvent, workingDir: VirtualFile): VirtualFile {
        val moduleFile = findClosestModuleFile(project, e, workingDir)
        return when {
            moduleFile.isDirectory -> moduleFile
            else -> moduleFile.parent
        }
    }

    fun findClosestModuleFile(project: Project, e: AnActionEvent, workingDir: VirtualFile): VirtualFile {
        val virtualFile: VirtualFile = e.getRequiredData(CommonDataKeys.VIRTUAL_FILE)
        return recursivelyFindModuleInParentFolders(virtualFile) ?: workingDir
    }

    private fun getPathDifference(moduleFilePath: String, workingDirPath: String) = workingDirPath.replace(
        moduleFilePath, ""
    )

    private fun recursivelyFindModuleInParentFolders(virtualFile: VirtualFile?): VirtualFile? {
        val regex = Regex(".*module\\.ts$")
        var parentDirectory: VirtualFile? = virtualFile


            while (parentDirectory != null) {
            val childrenFiles = parentDirectory.children

            val moduleFile = childrenFiles.firstOrNull {
                it.isInLocalFileSystem && it.fileType.defaultExtension == "ts"
                        && regex.matches(it.name)
            }

            if (moduleFile != null) {
                return moduleFile
            }

            parentDirectory = parentDirectory.parent
        }

        return null
    }

    fun computeGeneratePath(type: String, project: Project, virtualFile: VirtualFile): String {
        if (type == "app" ||
            type == "sub-app" ||
            type == "library") {
            return project.guessProjectDir()?.path ?: ""
        }
        return getRelativePath(project, virtualFile)
    }

    fun getRelativePath(project: Project, virtualFile: VirtualFile): String {
        val basePath = project.basePath
        val filePath = virtualFile.path

        val projectPath = Paths.get(basePath!!)
        val relativePath = projectPath.relativize(Paths.get(filePath))

        return relativePath.toString()
    }
}
