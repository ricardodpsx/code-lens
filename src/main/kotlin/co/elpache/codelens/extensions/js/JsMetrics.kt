package co.elpache.codelens.extensions.js

import co.elpache.codelens.codeLoader.fileId
import co.elpache.codelens.codeSearch.search.find
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.Vertice
import co.elpache.codelens.tree.Vid
import java.io.File
import kotlin.math.max


fun applyJsMetrics(tree: CodeTree) {

  //Todo: Somethings can be moved to nodePosprocess
  tree.find("codeFile[lang='js']").forEach { fileNode ->
    //Todo Can we use a FoundVertice abstraction?
    fun Vertice.find(query: String) = tree.find(query, this)

    fun Vertice.code() = tree.code(this.vid)

    with(fileNode) {

      find("params>*").forEach {
        it.addType("param")
      }

      find("args>*").forEach {
        it.addType("arg")
      }

      find("Identifier").forEach {
        tree.parentNode(it.vid)?.set("name", it.getString("name"))
      }

      find("fun").forEach {
        it["textLines"] = tree.code(it.vid).split("\n").size
        it["lines"] = tree.code(it.vid).relevantCodeLines() - 1
        it["depth"] = depth(tree, it.vid) - 1
      }

      find("call[{$>args>arg | count()} as args]")
      find("fun[{$>params>param | count()} as params]")
      find("class[{$>binding | count()} as properties]")
      find("class[{$ fun[kind='constructor'] | count()} as constructors]")
      find("class[{$ fun[kind='method' || kind='get' || kind='constructor'] | count()} as methods]")

      find("class").forEach {
        it["lines"] = tree.code(it.vid).relevantCodeLines()
      }

      find("import>string").forEach { f ->
        val relPath = File(f.getString("value"))
        val fPath = File(tree.rootDir().getString("path") + "/" + fileNode.getString("path")).parentFile

        val possiblePath = fileId(fPath.resolve("$relPath.js").normalize().toString())

        if (tree.contains(possiblePath))
          tree.addRelation("imports", fileNode.vid, possiblePath)
      }
    }

    fileNode["lines"] = fileNode.code().relevantCodeLines()
    fileNode["textLines"] = fileNode.code().split("\n").size
    fileNode["functions"] = fileNode.find("$>body>fun").size
    fileNode["classes"] = fileNode.find("$>body>class").size
    fileNode["bindings"] = fileNode.find("$>body>binding").size
  }
}

private fun depth(tree: CodeTree, vid: Vid): Int {
  var maxDepth = 0
  for (cVid in tree.children(vid))
    maxDepth = max(depth(tree, cVid), maxDepth)

  return (if (increasesNesting(tree.v(vid))) 1 else 0) + maxDepth
}

fun increasesNesting(item: Vertice) = listOf("fun", "if", "loop").any { item.isA(it) }

fun String.relevantCodeLines() =
  split("\n")
    .map { it.trim() }
    .filter { !it.isBlank() }
    .filter { it.length > 1 }
    .size
