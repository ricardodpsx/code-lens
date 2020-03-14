package co.elpache.codelens.extensions.kotlin

import co.elpache.codelens.codeSearch.search.find
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.Vertice
import co.elpache.codelens.tree.Vid
import kotlin.math.max

fun applyKotlinMetrics(tree: CodeTree) {
  val filesByPackage = HashMap<String, ArrayList<Vid>>()

//  ctx.find("file[lang='kotlin']>packageDirective").forEach {
//    val packageName = it.vertice.getString("name")
//    filesByPackage.putIfAbsent(packageName, ArrayList())
//    filesByPackage[packageName]?.add(
//      it.vertice.getString("fileVid")
//    )
//  }

  tree.find("codeFile[lang='kotlin']").forEach { fileNode ->

    fun Vertice.find(query: String) = tree.find(query, this)
    fun Vertice.code() = tree.code(this.vid)

    fileNode["lines"] = fileNode.code().relevantCodeLines()
    fileNode["textLines"] = fileNode.code().split("\n").size
    fileNode["functions"] = fileNode.find("fun").size
    fileNode["classes"] = fileNode.find("class").size
    fileNode["bindings"] = fileNode.find("binding").size

    with(fileNode) {

      find("import").forEach {
        val parts = it.getString("name").split(".")
        val pack = parts.dropLast(1).joinToString(".")
        val name = parts.last()

        val found = tree.find("codeFile[{$>body>packageDirective[name^='$pack']} && {$>body>#$name}]").firstOrNull()
        if (found != null) {
          tree.addRelation("imports", fileNode.vid, found.vid)
        }
      }

      find("call").forEach {
        it["args"] = it.find("$>args>arg").size
      }

      find("fun").forEach {
        it["textLines"] = it.code().split("\n").size
        it["lines"] = it.code().relevantCodeLines() - 1
        it["depth"] = depth(tree, it.vid) - 1
        it["params"] = it.find("$>params>param").size
      }

      find("class").forEach { c ->
        c["lines"] = c.code().relevantCodeLines()

        c.find("$>ClassBody").firstOrNull()?.let {
          c["constructors"] = it.find("$>fun[astType*='Constructor']").size
          c["methods"] = it.find("$>fun").size
          c["properties"] = it.find("$>binding").size
          c["lines"] = it.code().relevantCodeLines()
        }
      }
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
