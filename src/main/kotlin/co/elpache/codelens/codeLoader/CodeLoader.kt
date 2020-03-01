package co.elpache.codelens.codeLoader

import co.elpache.codelens.codeSearch.search.ContextNode
import co.elpache.codelens.tree.CodeTree

abstract class NodeLoader {
  abstract fun doLoad(): CodeTree

  companion object {
    var languageSupportRegistry = listOf<LanguageIntegration>()
  }

  fun extensions(vararg extensions: LanguageIntegration): NodeLoader {
    languageSupportRegistry = extensions.toList().distinctBy { it }
    return this
  }

  fun load(): CodeTree {
    val tree = doLoad()
    languageSupportRegistry.forEach { it.applyMetrics(ContextNode(tree.rootVid(), tree)) }
    return tree
  }


}