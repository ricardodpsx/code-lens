package co.elpache.codelens.codeLoader

import co.elpachecode.codelens.cssSelector.search.NodeResult
import java.io.File

val languageSupportRegistry = mutableMapOf<String, LanguageIntegration>()

data class LanguageIntegration(
  val fileLoaderBuilder: (File) -> NodeLoader,
  val applyMetrics: (vid: NodeResult) -> Unit,
  val onBaseCodeLoad: (File) -> Unit = {}
)