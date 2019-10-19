package co.elpache.codelens.languages.js

import co.elpache.codelens.codetree.CodeEntity
import co.elpache.codelens.codetree.CodeTree
import co.elpache.codelens.tree.Vid
import co.elpachecode.codelens.cssSelector.NodeResult
import kotlin.math.max

fun applyJsMetrics(it: NodeResult) {

  it.data["lines"] = it.code.relevantCodeLines()
  it.data["textLines"] = it.code.split("\n").size
  it.data["functions"] = it.children("fun").size
  it.data["classes"] = it.children("class").size
  it.data["bindings"] = it.children("binding").size

  with(it) {
    byType("call").forEach {
      it.data["args"] = it.firstChildren("args").children("arg").size
    }

    byType("fun").forEach {
      it.data["textLines"] = it.code.split("\n").size
      it.data["lines"] = it.code.relevantCodeLines() - 1
      it.data["depth"] = depth(it.codeBase, it.vid) - 1
      it.data["params"] = it.firstChildren("params").children("param").size
    }

    byType("class").forEach {
      it.data["lines"] = it.code.relevantCodeLines()

      var body = it.firstChildren("ClassBody").firstChildren("body")
      it.data["constructors"] = body.children("fun[kind='constructor']").size
      it.data["methods"] = body.children("fun").size
      it.data["properties"] = body.children("binding").size
      it.data["members"] = body.code.relevantCodeLines()
    }
  }
}

private fun depth(tree: CodeTree, vid: Vid): Int {
  var maxDepth = 0
  for (cVid in tree.tree.children(vid))
    maxDepth = max(depth(tree, cVid), maxDepth)

  return (if (co.elpache.codelens.languages.kotlin.increasesNesting((tree.tree.v(vid) as CodeEntity).type)) 1 else 0) + maxDepth
}


fun increasesNesting(type: String) = listOf("fun", "if", "loop").contains(type)


fun String.relevantCodeLines() =
  split("\n")
    .map { it.trim() }
    .filter { !it.isBlank() }
    .filter { it.length > 1 }
    .size
