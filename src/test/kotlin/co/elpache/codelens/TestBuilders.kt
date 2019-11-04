package co.elpache.codelens

import co.elpache.codelens.codetree.CodeTree
import co.elpache.codelens.codetree.CodeTreeNode
import co.elpache.codelens.tree.Tree
import co.elpache.codelens.tree.join
import co.elpache.codelens.useCases.CodeExplorerUseCases
import co.elpachecode.codelens.cssSelector.search.finder
import io.mockk.every
import io.mockk.mockk

fun createUseCases(codeTree: CodeTree): CodeExplorerUseCases {
  val factory = mockk<Factory>()
  every {
    factory.createBaseCode()
  } returns codeTree

  return CodeExplorerUseCases(factory)
}

fun codeTreeNode(vararg data: Pair<String, Any>) =
  object : CodeTreeNode() {
    init {
      this.data.putAll(data)
    }

    override fun expand(): List<CodeTreeNode> = emptyList()
    override fun toString() = data.toString()
  }

fun codeTree(vid: String, node: CodeTreeNode, vararg expands: CodeTree): CodeTree {
  var tree = CodeTree()
  tree.tree.addIfAbsent(vid, node)
  tree.tree.rootVid = vid
  expands.forEach {
    join(tree.tree, it.tree)
  }
  return tree
}

fun tree(vid: String, vararg expands: Tree<String>): Tree<String> {
  var tree = Tree<String>()
  tree = tree.addIfAbsent(vid, vid)
  tree.rootVid = vid
  expands.forEach {
    join(tree, it)
  }
  return tree
}

fun selectCode(
  tree: CodeTree,
  cssSelector: String
): List<Map<String, Any>> {
  return tree.finder().find(cssSelector).data()
}
