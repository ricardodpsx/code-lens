package co.elpache.codelens.codetree

import co.elpache.codelens.languages.js.applyJsMetrics
import co.elpache.codelens.languages.kotlin.applyKotlinMetrics
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.VData
import co.elpache.codelens.tree.Vid
import co.elpachecode.codelens.cssSelector.search.finder

interface CodeTreeLoader {
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
  fun applyAnalytics(tree: CodeTree): CodeLoader {
    tree.finder().find("file")
      .map { it to it.codeNode() }
      .forEach {
        if (it.second["lang"] == "js") applyJsMetrics(it.first)
        else if (it.second["lang"] == "kotlin") applyKotlinMetrics(it.first)
      }
    return this
  }
}