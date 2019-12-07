package co.elpache.codelens.languages.kotlin

import co.elpache.codelens.codeSearch.search.ContextNode
import co.elpache.codelens.codeSearch.search.firstNode
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.Vid
import kotlin.math.max

fun applyKotlinMetrics(fileNode: ContextNode) {

  fileNode.data["lines"] = fileNode.code.relevantCodeLines()
  fileNode.data["textLines"] = fileNode.code.split("\n").size
  fileNode.data["functions"] = fileNode.find("fun").size
  fileNode.data["classes"] = fileNode.find("class").size
  fileNode.data["bindings"] = fileNode.find("binding").size

  with(fileNode) {
    find("call").forEach {
      it.data["args"] = it.find("$>args>arg").size
    }

    find("fun").forEach {
      it.data["textLines"] = it.code.split("\n").size
      it.data["lines"] = it.code.relevantCodeLines() - 1
      it.data["depth"] = depth(it.tree, it.vid) - 1
      it.data["params"] = it.find("$>params>param").size
    }

    find("class").forEach {
      it.data["lines"] = it.code.relevantCodeLines()

      val body = it.find("$>ClassBody").firstNode()
      it.data["constructors"] = body.find("$>fun[astType*='Constructor']").size
      it.data["methods"] = body.find("$>fun").size
      it.data["properties"] = body.find("$>binding").size
      it.data["lines"] = it.code.relevantCodeLines()
    }
  }
}

private fun depth(tree: CodeTree, vid: Vid): Int {
  var maxDepth = 0
  for (cVid in tree.children(vid))
    maxDepth = max(depth(tree, cVid), maxDepth)

  return (if (increasesNesting(tree.v(vid)["type"] as String)) 1 else 0) + maxDepth
}


fun increasesNesting(type: String) = listOf("fun", "if", "loop").contains(type)


fun String.relevantCodeLines() =
  split("\n")
    .map { it.trim() }
    .filter { !it.isBlank() }
    .filter { it.length > 1 }
    .size
