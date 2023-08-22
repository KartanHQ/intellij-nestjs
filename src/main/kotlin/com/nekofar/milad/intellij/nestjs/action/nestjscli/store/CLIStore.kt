package com.nekofar.milad.intellij.nestjs.action.nestjscli.store

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.reduxkotlin.Reducer
import org.reduxkotlin.createThreadSafeStore

data class GenerateCLIState(
    val type: String? = null,
    val parameter: String = "",
    val project: Project? = null,
    val filePath: String = "",
    val generateInDir: VirtualFile? = null,
    val closestModuleDir: VirtualFile? = null

)

object CLIStore {
    val store = createThreadSafeStore(reducer, GenerateCLIState())
}

val reducer: Reducer<GenerateCLIState> = {state, action ->
    when(action) {
        is Action.GenerateCLIAction -> {
           GenerateCLIState(
               type = action.type,
               parameter = action.options,
               filePath = action.filePath,
               project = action.project,
               closestModuleDir = action.closestModuleDir,
               generateInDir = action.generateInDir
           )
        }
        is Action.UpdateOptions -> {
            state.copy(
                parameter = action.options
            )
        }
        else -> state
    }
}
