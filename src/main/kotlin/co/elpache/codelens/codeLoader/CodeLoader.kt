package co.elpache.codelens.codeLoader

import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.Vid

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
    addLevels(tree.rootDirVid(), tree, 0)
    languageSupportRegistry.forEach { it.applyMetrics(tree) }
    return tree
  }

  private fun addLevels(vid: Vid, tree: CodeTree, level: Int) {
    tree.v(vid)["level"] = level
    tree.children(vid).forEach {
      addLevels(it, tree, level + 1)
    }
  }

}