package co.elpache.codelens.codeLoader

import co.elpache.codelens.codeSearch.search.ContextNode
import java.io.File

val languageSupportRegistry = mutableMapOf<String, LanguageIntegration>()

data class LanguageIntegration(
  val fileLoaderBuilder: (File) -> NodeLoader,
  val applyMetrics: (vid: ContextNode) -> Unit,
  val onBaseCodeLoad: (File) -> Unit = {}
)