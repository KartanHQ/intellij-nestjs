package com.nekofar.milad.intellij.nestjs.action.nestjscli.store

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.reduxkotlin.Reducer
import org.reduxkotlin.createThreadSafeStore

data class GenerateCLIState(
    val generateCommand: String = "",
    val type: String = "",
    val parameter: String = "",
    val project: Project? = null,
    val workingDir: VirtualFile? = null

)

object CLIStore {
    val store = createThreadSafeStore(reducer, GenerateCLIState(generateCommand = ""))
}

val reducer: Reducer<GenerateCLIState> = {state, action ->
    when(action) {
        is Action.GenerateCLIAction -> {
           GenerateCLIState(
               type = action.type,
               parameter = action.options,
               workingDir = action.workingDir,
               project = action.project,
               generateCommand = "nest generate ${action.type} ${action.options} ${action.filePath}"
           )
        }
        else -> state
    }
}
