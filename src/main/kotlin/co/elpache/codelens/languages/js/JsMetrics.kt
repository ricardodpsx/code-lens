package co.elpache.codelens.languages.js

import co.elpache.codelens.codetree.CodeTree
import co.elpache.codelens.tree.Vid
import co.elpachecode.codelens.cssSelector.search.NodeResult
import kotlin.math.max

fun applyJsMetrics(it: NodeResult) {

  it.data["lines"] = it.code.relevantCodeLines()
  it.data["textLines"] = it.code.split("\n").size
  it.data["functions"] = it.find("$>fun").size
  it.data["classes"] = it.find("$>class").size
  it.data["bindings"] = it.find("$>binding").size

  with(it) {
    find("call").forEach {
      it.data["args"] = it.find("$>args>arg").size
    }

    find("fun").forEach {
      it.data["textLines"] = it.code.split("\n").size
      it.data["lines"] = it.code.relevantCodeLines() - 1
      it.data["depth"] = depth(it.codeBase, it.vid) - 1
      it.data["params"] = it.find("$>params>param").size
    }

    find("class").forEach {
      it.data["lines"] = it.code.relevantCodeLines()

      var body = it.find("$>ClassBody>body").first()
      it.data["constructors"] = body.find("$>fun[kind='constructor']").size
      it.data["methods"] = body.find("$>fun").size
      it.data["properties"] = body.find("$>binding").size
      it.data["members"] = body.code.relevantCodeLines()
    }
  }
}

private fun depth(tree: CodeTree, vid: Vid): Int {
  var maxDepth = 0
  for (cVid in tree.tree.children(vid))
    maxDepth = max(depth(tree, cVid), maxDepth)

  return (if (co.elpache.codelens.languages.kotlin.increasesNesting(tree.node(vid).type)) 1 else 0) + maxDepth
}


fun increasesNesting(type: String) = listOf("fun", "if", "loop").contains(type)


fun String.relevantCodeLines() =
  split("\n")
    .map { it.trim() }
    .filter { !it.isBlank() }
    .filter { it.length > 1 }
    .size
