package co.elpache.codelens.languages.js

import co.elpache.codelens.codeSearch.search.ContextNode
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.Vid
import kotlin.math.max

fun applyJsMetrics(code: ContextNode) {
  code.find("file[lang='js']").forEach { fileNode ->
    fileNode.data["lines"] = fileNode.code.relevantCodeLines()
    fileNode.data["textLines"] = fileNode.code.split("\n").size
    fileNode.data["functions"] = fileNode.find("$>fun").size
    fileNode.data["classes"] = fileNode.find("$>class").size
    fileNode.data["bindings"] = fileNode.find("$>binding").size

    with(fileNode) {
      setQuery("SET {call} args = {$>args>arg | count}")
      setQuery("SET {fun} params = {$>params>param | count}")
      setQuery("SET {class} properties = {$>binding | count}")
      setQuery("SET {class} constructors = {$ fun[kind='constructor'] | count}")

      find("fun").forEach {
        it.data["textLines"] = it.code.split("\n").size
        it.data["lines"] = it.code.relevantCodeLines() - 1
        it.data["depth"] = depth(it.tree, it.vid) - 1
      }

      find("class").forEach {
        it.data["lines"] = it.code.relevantCodeLines()

        val body = it.find("$>ClassBody>body").first()
        it.data["methods"] = body.findValue("$ fun[kind='method' || kind='get' || kind='constructor'] | count") as Int

        it.data["lines"] = body.code.relevantCodeLines()
      }
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
