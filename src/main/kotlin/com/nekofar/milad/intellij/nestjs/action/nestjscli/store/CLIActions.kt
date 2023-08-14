package com.nekofar.milad.intellij.nestjs.action.nestjscli.store
class Action {
    data class GenerateCLIAction(
        val type: String,
        val options: String,
        val filePath: String,
    )
}
