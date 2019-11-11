package co.elpache.codelens.codetree

import co.elpache.codelens.languages.js.applyJsMetrics
import co.elpache.codelens.languages.kotlin.applyKotlinMetrics
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.Vid
import co.elpache.codelens.tree.toVData
import co.elpachecode.codelens.cssSelector.search.finder
import java.util.LinkedList

class CodeLoader {

  fun expandFullCodeTree(node: CodeEntity): CodeTree {
    val tree = CodeTree()
    _expandTreeNode(tree, node)
    applyAnalytics(tree)
    return tree
  }

  val ids: HashSet<Vid> = HashSet()

  private fun _expandTreeNode(tree: CodeTree, node: CodeEntity): CodeLoader {

    val queue = LinkedList<Pair<CodeEntity, Vid?>>()

    queue.add(node to null)

    while (queue.isNotEmpty()) {
      val cur = queue.removeFirst()
      val vid = generateVid(cur.first, cur.second)

      tree.addIfAbsent(vid, cur.first.data.toVData())

      cur.second?.let {
        tree.addChild(it, vid)
      }

      if (tree.rootVid == null) tree.rootVid = vid

      cur.first.expand().forEach {
        queue.addLast(it to vid)
      }
    }

    return this
  }


  fun generateVid(node: CodeEntity, parent: Vid?): Vid {
    val vid = ids.size.toString()
    //Wip: Find stable Ids
//    when (node) {
//      is CodeFile -> node.file.path.replace("/", "-")
//      is CodeFolder -> node.file.path.toString().replace("/", "-")
//      else -> ids.size.toString()
//    }

    if (ids.contains(vid)) throw error("Duplicated Id $vid")
    ids.add(vid)
    return vid
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