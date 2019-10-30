package co.elpache.codelens.tree

import org.jetbrains.kotlin.backend.common.pop
import java.util.LinkedList


fun <T> ancestors(tree: Tree<T>, vid: String): List<Vid> {
  val list = LinkedList<Vid>()
  var p: Vid? = vid
  while (p != null) {
    list.add(p)
    p = tree.parent(p)
  }
  return list.toList()
}


fun <T> buildTreeFromChildren(sourceTree: Tree<T>, children: List<Vid>): Tree<T> {
  var resTree = Tree<T>()

  children.forEach { vid ->
    ancestors(sourceTree, vid).reversed()
      .windowed(2, partialWindows = true).forEach {
        resTree = resTree.addIfAbsent(it[0], sourceTree.v(it[0]))
        if (it.size == 2) resTree = resTree.addChild(it[0], it[1], sourceTree.v(it[1]))
      }
  }
  resTree.rootVid = sourceTree.rootVid
  return resTree
}


//Todo: Prune subTree
fun <T> subTree(tree: Tree<T>, vid: Vid): Tree<T> {
  val ct = Tree<T>()
  ct.rootVid = vid
  val items = ArrayList<Vid>()
  items.add(vid)
  while (items.isNotEmpty()) {
    val p = items.pop()
    ct.vertices.put(p, tree.vertices[p]!!)
    tree.children(p).forEach {
      items.add(it)
    }
  }

  return ct
}

fun <T> join(parent: Tree<T>, child: Tree<T>) {
  assert(parent.vertices.keys.intersect(child.vertices.keys).isEmpty()) { "Trees should be disjoint" }

  parent.vertices.putAll(child.vertices)
  parent.addChild(parent.rootVid(), child.rootVid())

}




