package co.elpache.codelens

import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.VData
import co.elpache.codelens.tree.Vid
import co.elpache.codelens.tree.vDataOf
import co.elpache.codelens.useCases.CodeExplorerUseCases
import co.elpache.codelens.useCases.CodeSmellsUseCases
import co.elpachecode.codelens.cssSelector.search.finder
import io.mockk.every
import io.mockk.mockk

fun createCodeExplorerUseCases(codeTree: CodeTree): CodeExplorerUseCases {
  return CodeExplorerUseCases(getMockFactory(codeTree))
}

fun createCodeSmellsUseCases(codeTree: CodeTree): CodeSmellsUseCases {
  return CodeSmellsUseCases(getMockFactory(codeTree))
}

private fun getMockFactory(codeTree: CodeTree): Factory {
  val factory = mockk<Factory>()
  every {
    factory.createBaseCode()
  } returns codeTree
  return factory
}


fun codeTree(vid: String, node: VData, vararg expands: CodeTree): CodeTree {
  var tree = CodeTree()
  tree.addIfAbsent(vid, node)
  tree.rootVid = vid
  expands.forEach {
    join(tree, it)
  }
  return tree
}

fun tree(vid: String, vararg expands: CodeTree): CodeTree {
  var tree = CodeTree()
  tree = tree.addIfAbsent(vid, vDataOf("value" to vid))
  tree.rootVid = vid
  expands.forEach {
    join(tree, it)
  }
  return tree
}


fun inorder(codeTree: CodeTree): List<Vid> {
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


fun join(parent: CodeTree, child: CodeTree) {
  assert(parent.vertices.keys.intersect(child.vertices.keys).isEmpty()) { "Trees should be disjoint" }

  parent.vertices.putAll(child.vertices)
  parent.addChild(parent.rootVid(), child.rootVid())

}

