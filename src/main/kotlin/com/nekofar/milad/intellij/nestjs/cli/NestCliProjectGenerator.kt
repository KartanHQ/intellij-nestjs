package com.nekofar.milad.intellij.nestjs.cli

import com.intellij.execution.filters.Filter
import com.intellij.lang.javascript.boilerplate.NpmPackageProjectGenerator
import com.intellij.lang.javascript.boilerplate.NpxPackageDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ContentEntry
import com.intellij.openapi.vfs.VirtualFile
import com.nekofar.milad.intellij.nestjs.NestBundle
import com.nekofar.milad.intellij.nestjs.NestIcons
import javax.swing.Icon

class NestCliProjectGenerator: NpmPackageProjectGenerator() {
    private val packageName = "@nestjs/cli"
    private val executable = "nest"
    private val initCommand = "new"

    override fun getName(): String {
        return NestBundle.message("nest.project.generator.name")
    }

    override fun getDescription(): String {
        return NestBundle.message("nest.project.generator.description")
    }

    override fun filters(project: Project, baseDir: VirtualFile): Array<Filter> {
        return emptyArray()
    }

    override fun customizeModule(p0: VirtualFile, p1: ContentEntry?) {}

    override fun packageName(): String {
        return packageName
    }

    override fun presentablePackageName(): String {
        return NestBundle.message("nest.project.generator.presentable.package.name")
    }

    override fun getNpxCommands(): List<NpxPackageDescriptor.NpxCommand> {
        return listOf(NpxPackageDescriptor.NpxCommand(packageName, executable))
    }

    override fun generatorArgs(project: Project?, dir: VirtualFile?, settings: Settings?): Array<String> {
        return arrayOf(initCommand, "--directory", ".", "--package-manager", "npm")
    }

    override fun getIcon(): Icon {
        return NestIcons.ProjectGenerator
    }
}