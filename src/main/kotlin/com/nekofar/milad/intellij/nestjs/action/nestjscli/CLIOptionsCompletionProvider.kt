package com.nekofar.milad.intellij.nestjs.action.nestjscli

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.lookup.CharFilter
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.ui.TextFieldWithAutoCompletionListProvider
import com.jetbrains.rd.util.first

class CLIOptionsCompletionProvider(private val items: List<String>) : TextFieldWithAutoCompletionListProvider<String>(items) {

    companion object {
        val generateItems = mapOf(
                "app" to "Generate a new application within a monorepo.",
                "class" to "Generate a new class",
                "configuration" to "Generate a CLI configuration file",
                "controller" to "Generate a controller declaration",
                "decorator" to "Generate a custom decorator",
                "filter" to "Generate a filter declaration",
                "gateway" to "Generate a gateway declaration",
                "guard" to "Generate a guard declaration",
                "interceptor" to "Generate an interceptor declaration",
                "interface" to "Generate an interface",
                "library" to "Generate a new library within a monorepo.",
                "middleware" to "Generate a middleware declaration",
                "module" to "Generate a module declaration",
                "pipe" to "Generate a pipe declaration",
                "provider" to "Generate a provider declaration",
                "resolver" to "Generate a GraphQL resolver declaration",
                "resource" to "Generate a new CRUD resource",
                "service" to "Generate a service declaration",
                "sub-app" to "Generate a new application within a monorepo"
        )
        val options = mapOf(
                "--dry-run" to "Report actions that would be taken without writing out results.",
                "--project" to "Project in which to generate files.",
                "--flat" to "Enforce flat structure of generated element.",
                "--no-flat" to "Enforce that directories are generated.",
                "--spec" to "Enforce spec files generation. (default: true)",
                "--skip-import" to "Skip importing (default: false)",
                "--no-spec" to " Disable spec files generation."
        )
    }

    override fun getLookupString(item: String): String {
        return item
    }

    override fun getItems(prefix: String?, cached: Boolean, parameters: CompletionParameters?): List<String> {
        return if (prefix == null) emptyList() else items.filter { it.contains(prefix, ignoreCase = true) }
    }

    override fun acceptChar(c: Char): CharFilter.Result? {
        return if (c == '-') CharFilter.Result.ADD_TO_PREFIX else null
    }

    override fun compare(left: String?, right: String?): Int {
        return when {
            left == null -> -1
            right == null -> 1
            else -> left.compareTo(right)
        }
    }

    override fun createLookupBuilder(item: String): LookupElementBuilder {
        val description = options.filter {
            it.key == item }.first().value
        return super.createLookupBuilder(item)
                .withTailText("  $description", true)
    }
}

