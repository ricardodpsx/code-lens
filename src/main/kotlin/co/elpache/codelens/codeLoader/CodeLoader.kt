package co.elpache.codelens.codeLoader

import co.elpache.codelens.codeSearch.search.finder
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.Vid

interface NodeLoader {
  fun load(): CodeTree
}

class CodeLoader {

  fun expandFullCodeTree(node: FolderLoader): CodeTree {
    val tree = node.load()
    applyMetrics(tree)
    return tree
  }

  val ids: HashSet<Vid> = HashSet()

  private fun applyMetrics(tree: CodeTree): CodeLoader {
    languageSupportRegistry.forEach { it.value.applyMetrics(tree.finder()) }
    return this
  }
}