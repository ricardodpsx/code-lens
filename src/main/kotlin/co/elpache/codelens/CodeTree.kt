package co.elpache.codelens

import co.elpache.codelens.languages.kotlin.applyAnalytics
import co.elpache.codelens.tree.Tree
import co.elpache.codelens.tree.Vid
import java.lang.StringBuilder

typealias CodeTree = Tree<CodeTreeNode>

//Todo: Test tree expansion
fun expandFullCodeTree(node: CodeTreeNode): CodeTree =
  applyAnalytics(_expandTreeNode(CodeTree(), node, node.data()["name"] as String))

fun toMap(tree: CodeTree): Map<String, Any> {
  return tree.vertices.map {
    it.key to mapOf(
      "data" to it.value.node.data(),
      "parent" to it.value.parentVid,
      "children" to it.value.children.toList()
    )
  }.toMap().plus("rootVid" to tree.rootVid!!).toMap()
}


private fun _expandTreeNode(
  tree: CodeTree,
  node: CodeTreeNode,
  parents: String,
  ids: HashSet<Vid> = HashSet(),
  parent: Vid? = null
): CodeTree {

  val vid = ids.size.toString()

  if(ids.contains(vid)) throw error("Duplicated Id $vid")
  ids.add(vid)

  //val vid = UUID.randomUUID().toString()
  var treeCopy = tree.addIfAbsent(vid, node)

  if (parent != null)
    treeCopy = treeCopy.addChild(parent, vid)

  node.expand().forEach {
    treeCopy = _expandTreeNode(treeCopy, it, parents, ids, vid)
  }
  treeCopy.rootVid = vid
  return treeCopy
}

fun printTree(tree: CodeTree): String {
  val out = StringBuilder()
  val root = tree.v(tree.rootVid())
  out.append("${root.data()}\n")
  dfs(tree, tree.rootVid(), "-", out)
  return out.toString()
}

private fun dfs(tree: CodeTree, vid: Vid, tab: String, out: StringBuilder) {
  for (cVid in tree.children(vid)) {
    val child = tree.v(cVid)
    out.append(" $tab ${child.data()}\n")
    dfs(tree, cVid, "$tab-", out)
  }
}