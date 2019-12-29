package co.elpache.codelens.codeLoader

import co.elpache.codelens.codeSearch.search.ContextNode
import java.io.File

val languageSupportRegistry = mutableMapOf<String, LanguageIntegration>()

typealias FileLoaderBuilder = (File, File) -> NodeLoader
data class LanguageIntegration(
  val fileLoaderBuilder: FileLoaderBuilder? = null,
  val applyMetrics: (vid: ContextNode) -> Unit,
  val onBaseCodeLoad: (File) -> Unit = {},
  val ignorePatterns: List<String> = listOf()
)