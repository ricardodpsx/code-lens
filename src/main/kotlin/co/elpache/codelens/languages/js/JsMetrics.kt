package co.elpache.codelens.languages.js

import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.Vid
import co.elpachecode.codelens.cssSelector.search.NodeResult
import kotlin.math.max

fun applyJsMetrics(fileNode: NodeResult) {

  fileNode.data["lines"] = fileNode.code.relevantCodeLines()
  fileNode.data["textLines"] = fileNode.code.split("\n").size
  fileNode.data["functions"] = fileNode.find("$>fun").size
  fileNode.data["classes"] = fileNode.find("$>class").size
  fileNode.data["bindings"] = fileNode.find("$>binding").size

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

      var body = it.find("$>ClassBody>body").first()
      it.data["constructors"] = body.find("$ fun[kind='constructor']").size
      it.data["methods"] =
        body.find("$ fun[kind='method']").size + body.find("$ fun[kind='get']").size + it.data.getInt("constructors")
      it.data["properties"] = body.find("$>binding").size
      it.data["members"] = body.code.relevantCodeLines()
    }
  }
}

private fun depth(tree: CodeTree, vid: Vid): Int {
  var maxDepth = 0
  for (cVid in tree.children(vid))
    maxDepth = max(depth(tree, cVid), maxDepth)

  return (if (increasesNesting(tree.v(vid).type)) 1 else 0) + maxDepth
}

fun increasesNesting(type: String) = listOf("fun", "if", "loop").contains(type)

fun String.relevantCodeLines() =
  split("\n")
    .map { it.trim() }
    .filter { !it.isBlank() }
    .filter { it.length > 1 }
    .size
