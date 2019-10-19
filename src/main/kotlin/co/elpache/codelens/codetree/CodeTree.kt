package co.elpache.codelens.codetree

import co.elpache.codelens.languages.js.applyJsMetrics
import co.elpache.codelens.languages.kotlin.applyKotlinMetrics
import co.elpache.codelens.tree.Tree
import co.elpache.codelens.tree.Vid
import co.elpache.codelens.tree.buildTreeFromChildren
import co.elpache.codelens.tree.subTree
import co.elpachecode.codelens.cssSelector.finder

class CodeTree(val tree: Tree<CodeTreeNode> = Tree()) {
  fun children(vid: String) = tree.children(vid).map {
    tree.v(it) as CodeEntity
  }

  fun file(vid: Vid) = tree.v(vid) as CodeFile

  fun data(vid: Vid) = tree.v(vid).data

  fun node(vid: Vid) = tree.v(vid)


  fun treeFromChildren(children: List<Vid>) =
    CodeTree(buildTreeFromChildren(tree, children))


  fun toMap(): Map<String, Any> {
    return tree.vertices.map {
      it.key to mapOf(
        "data" to it.value.node.data,
        "parent" to it.value.parentVid,
        "children" to it.value.children.toList()
      )
    }.toMap().plus("rootVid" to tree.rootVid!!).toMap()
  }

  fun subTreeFrom(vid: Vid) = CodeTree(subTree(tree, vid))

  fun descendants(vid: Vid, descendantList: ArrayList<Vid> = arrayListOf()): List<Vid> {
    for (cVid in tree.children(vid)) {
      descendantList.add(cVid)
      descendants(cVid, descendantList)
    }
    return descendantList
  }

  //Todo: Test tree expansion
  fun expandFullCodeTree(node: CodeTreeNode): CodeTree =
    _expandTreeNode(node)

  fun applyAnalytics(): CodeTree {
    finder().byType("file")
      .filter { it.codeNode() is CodeFile }
      .map { it to it.codeNode() as CodeFile }.forEach {
        if (it.second.lang == "js") applyJsMetrics(it.first)
        else if (it.second.lang == "kotlin") applyKotlinMetrics(it.first)
      }

    return this
  }

  private fun _expandTreeNode(
    node: CodeTreeNode,
    ids: HashSet<Vid> = HashSet(),
    parent: Vid? = null
  ): CodeTree {

    val vid = ids.size.toString()

    if (ids.contains(vid)) throw error("Duplicated Id $vid")

    ids.add(vid)

    //val vid = UUID.randomUUID().asString()
    tree.addIfAbsent(vid, node)

    if (parent != null)
      tree.addChild(parent, vid)

    node.expand().forEach {
      _expandTreeNode(it, ids, vid)
    }

    tree.rootVid = vid
    return this
  }

  fun asString(): String {
    val out = StringBuilder()
    val root = tree.v(tree.rootVid())
    out.append("${tree.rootVid}: ${root.data}\n")
    dfs(tree.rootVid(), "-", out)
    return out.toString()
  }

  private fun dfs(vid: Vid, tab: String, out: StringBuilder) {
    for (cVid in tree.children(vid)) {
      val child = tree.v(cVid)
      out.append(" $tab ${cVid}: ${child.data}\n")
      dfs(cVid, "$tab-", out)
    }
  }

}