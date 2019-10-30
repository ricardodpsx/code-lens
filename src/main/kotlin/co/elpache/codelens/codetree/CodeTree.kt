package co.elpache.codelens.codetree

import co.elpache.codelens.languages.js.applyJsMetrics
import co.elpache.codelens.languages.kotlin.applyKotlinMetrics
import co.elpache.codelens.tree.Tree
import co.elpache.codelens.tree.Vid
import co.elpache.codelens.tree.buildTreeFromChildren
import co.elpache.codelens.tree.subTree
import co.elpachecode.codelens.cssSelector.search.finder
import java.util.LinkedList

class CodeTree(val tree: Tree<CodeTreeNode> = Tree()) {
  fun children(vid: String) = tree.children(vid).map {
    tree.v(it) as CodeEntity
  }

  fun file(vid: Vid) = tree.v(vid) as CodeFile

  fun data(vid: Vid) = tree.v(vid).data

  fun <T> node(vid: Vid) = tree.v(vid) as T

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


  private fun dfs2(vid: Vid, tab: String, out: StringBuilder) {
    for (cVid in tree.children(vid)) {
      val child = tree.v(cVid)
      out.append(" $tab $cVid: ${child.data}\n")
      dfs(cVid, "$tab-", out)
    }
  }

  fun descendants(vid: Vid, descendantList: ArrayList<Vid> = arrayListOf()): List<Vid> {
    for (cVid in tree.children(vid)) {
      descendantList.add(cVid)
      descendants(cVid, descendantList)
    }
    return descendantList
  }


  fun expandFullCodeTree(node: CodeTreeNode): CodeTree = _expandTreeNode(node as CodeEntity)

  val ids: HashSet<Vid> = HashSet()

  private fun _expandTreeNode(node: CodeEntity): CodeTree {


    val queue = LinkedList<Pair<CodeEntity, Vid?>>()

    queue.add(node to null)

    while (queue.isNotEmpty()) {
      val cur = queue.removeFirst()
      val vid = generateVid(cur.first, cur.second)
      tree.addIfAbsent(vid, cur.first)

      cur.second?.let {
        tree.addChild(it, vid)
      }

      if (tree.rootVid == null) tree.rootVid = vid

      cur.first.expand().forEach {
        queue.addLast((it as CodeEntity) to vid)
      }
    }

    return this
  }

  fun generateVid(node: CodeEntity, parent: Vid?): Vid {
    val vid = when (node) {
      is CodeFile -> node.file.path
      is CodeFolder -> node.file.path.toString()
      else -> ids.size.toString()
    }

    if (ids.contains(vid)) throw error("Duplicated Id $vid")
    ids.add(vid)
    return vid
  }

  //Todo: This should be pluggable
  fun applyAnalytics(): CodeTree {
    finder().find("file")
      .filter { it.codeNode() is CodeFile }
      .map { it to it.codeNode() as CodeFile }.forEach {
        if (it.second.lang == "js") applyJsMetrics(it.first)
        else if (it.second.lang == "kotlin") applyKotlinMetrics(it.first)
      }

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