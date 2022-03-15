package com.nekofar.milad.intellij.nestjs.cli

import com.intellij.execution.filters.Filter
import com.intellij.lang.javascript.boilerplate.NpmPackageProjectGenerator
import com.intellij.lang.javascript.boilerplate.NpxPackageDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ContentEntry
import com.intellij.openapi.vfs.VirtualFile
import com.nekofar.milad.intellij.nestjs.NestBundle
import com.nekofar.milad.intellij.nestjs.NestIcons

class NestCliProjectGenerator : NpmPackageProjectGenerator() {
    private val packageName = "@nestjs/cli"
    private val executable = "nest"
    private val initCommand = "new"

    override fun getIcon() = NestIcons.ProjectGenerator

    override fun getName() = NestBundle.message("nest.project.generator.name")

    override fun getDescription() = NestBundle.message("nest.project.generator.description")

    override fun filters(project: Project, baseDir: VirtualFile): Array<Filter> = emptyArray()

    override fun customizeModule(baseDir: VirtualFile, entry: ContentEntry?) { /* Do nothing */ }

    override fun packageName() = packageName

    override fun presentablePackageName() = NestBundle.message("nest.project.generator.presentable.package.name")

    override fun getNpxCommands() = listOf(NpxPackageDescriptor.NpxCommand(packageName, executable))

    override fun generatorArgs(project: Project?, dir: VirtualFile?, settings: Settings?): Array<String> {
        val projectName = project?.name.orEmpty()
        return arrayOf(initCommand, "--directory", ".", "--package-manager", "npm", projectName)
    }
}