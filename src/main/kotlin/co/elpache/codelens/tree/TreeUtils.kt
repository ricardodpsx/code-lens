package co.elpache.codelens.tree

import java.util.LinkedList


private fun <T> descendants(tree: Tree<T>, vid: String): List<Vid> {
  val list = LinkedList<Vid>()
  var p: Vid? = vid
  while (p != null) {
    list.add(p)
    p = tree.parent(p)
  }
  return list.toList()
}


fun <T> buildTreeFromChildren(sourceTree: Tree<T>, res: List<Vid>): Tree<T> {
  var resTree = Tree<T>()

  res.forEach { vid ->
    descendants(sourceTree, vid).reversed()
      .windowed(2, partialWindows = true).forEach {
        resTree = resTree.addIfAbsent(it[0], sourceTree.v(it[0]))
        if (it.size == 2) resTree = resTree.addChild(it[0], it[1], sourceTree.v(it[1]))
      }
  }
  resTree.rootVid = sourceTree.rootVid
  return resTree
}


fun <T> subTree(tree: Tree<T>, vid: Vid): Tree<T> {
  val ct = Tree(tree.vertices)
  ct.rootVid = vid
  return ct
}

fun <T> join(parent: Tree<T>, child: Tree<T>): Tree<T> {
  assert(parent.vertices.keys.intersect(child.vertices.keys).isEmpty()) { "Trees should be disjoint" }

  var tree = Tree(parent.vertices.plus(child.vertices))
    .addChild(parent.rootVid(), child.rootVid())
  tree.rootVid = parent.rootVid
  return tree
}




