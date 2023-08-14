package com.nekofar.milad.intellij.nestjs.action.nestjscli.store

import org.reduxkotlin.Reducer
import org.reduxkotlin.createThreadSafeStore

data class GenerateCLIState(
    val generateCommand: String = "",
    val type: String = "",
    val parameter: String = ""

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
               generateCommand = "nest generate ${action.type} ${action.options} ${action.filePath}"
           )
        }
        else -> state
    }
}
