package co.elpache.codelens

import co.elpache.codelens.codetree.CodeEntity
import co.elpache.codelens.codetree.CodeTree
import co.elpache.codelens.tree.Tree
import co.elpache.codelens.tree.VData
import co.elpache.codelens.tree.Vid
import co.elpache.codelens.tree.join
import co.elpache.codelens.tree.toVData
import co.elpache.codelens.tree.vDataOf
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
  object : CodeEntity("", "") {
    init {
      this.data.putAll(data)
    }

    override fun expand(): List<CodeEntity> = emptyList()
    override fun toString() = data.toString()
  }

fun codeTree(vid: String, node: CodeEntity, vararg expands: CodeTree): CodeTree {
  var tree = CodeTree()
  tree.tree.addIfAbsent(vid, node.data.toVData())
  tree.tree.rootVid = vid
  expands.forEach {
    join(tree.tree, it.tree)
  }
  return tree
}

fun tree(vid: String, vararg expands: Tree): Tree {
  var tree = Tree()
  tree = tree.addIfAbsent(vid, vDataOf("value" to vid))
  tree.rootVid = vid
  expands.forEach {
    join(tree, it)
  }
  return tree
}


fun inorder(codeTree: Tree): List<Vid> {
  val out = mutableListOf<VData>()
  out.add(codeTree.root())
  dfs(codeTree.rootVid(), codeTree, out)
  return out.map { it["value"] as String }
}



fun selectCode(
  tree: CodeTree,
  cssSelector: String
): List<Map<String, Any>> {
  return tree.finder().find(cssSelector).data()
}
