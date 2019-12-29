package co.elpache.codelens.codeLoader

import co.elpache.codelens.codeSearch.search.finder
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.VData
import co.elpache.codelens.tree.Vid

interface NodeLoader {
  fun traverse(visitor: (node: VData, parent: VData?) -> Unit, parent: VData?)
}

class CodeLoader {

  fun expandFullCodeTree(node: FolderLoader): CodeTree {
    val tree = CodeTree()
    _expandTreeNode(tree, node)
    applyMetrics(tree)
    return tree
  }

  val ids: HashSet<Vid> = HashSet()

  private fun _expandTreeNode(tree: CodeTree, node: FolderLoader): CodeLoader {

    tree.rootVid = "0"
    node.traverse({ node, parent ->
      val vid = ids.size.toString()
      ids.add(vid)
      node["vid"] = vid

      if (parent != null)
        tree.addChild(parent["vid"] as Vid, vid, node)
      else
        tree.addIfAbsent("0", node)

      node["fileVid"] = tree.ancestors(vid).find { tree.v(it).type == "file" } ?: ""

    }, null)


    return this
  }

  //Todo: This should be pluggable
  private fun applyMetrics(tree: CodeTree): CodeLoader {
    languageSupportRegistry.forEach { it.value.applyMetrics(tree.finder()) }
    return this
  }
}