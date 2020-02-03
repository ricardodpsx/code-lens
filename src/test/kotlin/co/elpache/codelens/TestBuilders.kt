package co.elpache.codelens

import co.elpache.codelens.codeSearch.search.finder
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.Vertice
import co.elpache.codelens.tree.Vid
import co.elpache.codelens.tree.vDataOf
import co.elpache.codelens.useCases.CodeExplorerUseCases
import co.elpache.codelens.useCases.CodeSmellsUseCases
import io.mockk.every
import io.mockk.mockk

fun createCodeExplorerUseCases(codeTree: CodeTree): CodeExplorerUseCases {
  return CodeExplorerUseCases(getMockFactory(codeTree))
}

fun createCodeSmellsUseCases(codeTree: CodeTree): CodeSmellsUseCases {
  return CodeSmellsUseCases(CodeExplorerUseCases(getMockFactory(codeTree)))
}

private fun getMockFactory(codeTree: CodeTree): Factory {
  val factory = mockk<Factory>()
  every {
    factory.createBaseCode()
  } returns codeTree
  return factory
}


fun codeTree(node: Vertice, vararg expands: CodeTree): CodeTree {
  var tree = CodeTree()
  tree.addIfAbsent(node)
  expands.forEach {
    tree.join(it)
  }
  return tree
}

fun createCommits(vararg commits: String) = commits.map { createCommit(it) }


fun tree(vid: String, vararg expands: CodeTree): CodeTree {
  var tree = CodeTree()
  tree.addIfAbsent(vDataOf(vid, "value" to vid))
  expands.forEach {
    tree.join(it)
  }
  return tree
}


fun selectCode(
  tree: CodeTree,
  cssSelector: String
): List<Map<String, Any>> {
  return tree.finder().find(cssSelector).map { it.vertice.toMap() }
}

fun createCommit(commit: String) = Commit(commit, "", 0)