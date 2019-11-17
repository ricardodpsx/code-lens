package co.elpache.codelens.codeLoader

import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.VData
import co.elpache.codelens.tree.Vid
import co.elpachecode.codelens.cssSelector.search.finder

interface NodeLoader {
  fun traverse(visitor: (node: VData, parent: VData?) -> Unit, parent: VData?)
}

class CodeLoader {

  fun expandFullCodeTree(node: FolderLoader): CodeTree {
    val tree = CodeTree()
    _expandTreeNode(tree, node)
    applyAnalytics(tree)
    return tree
  }

  val ids: HashSet<Vid> = HashSet()

  private fun _expandTreeNode(tree: CodeTree, node: FolderLoader): CodeLoader {

    node.traverse({ node, parent ->
      val vid = ids.size.toString()
      ids.add(vid)
      node["vid"] = vid

      if (parent != null)
        tree.addChild(parent["vid"] as Vid, vid, node)
      else
        tree.addIfAbsent("0", node)
    }, null)

    tree.rootVid = "0"
    return this
  }

  //Todo: This should be pluggable
  private fun applyAnalytics(tree: CodeTree): CodeLoader {
    tree.finder().find("file")
      .map { it to it.codeNode() }
      .forEach {
        languageSupportRegistry[it.second["extension"]]?.apply {
          applyMetrics(it.first)
        }
      }
    return this
  }
}