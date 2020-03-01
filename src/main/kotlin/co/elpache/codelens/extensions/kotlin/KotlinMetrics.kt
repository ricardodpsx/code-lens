package co.elpache.codelens.extensions.kotlin

import co.elpache.codelens.codeSearch.search.ContextNode
import co.elpache.codelens.codeSearch.search.firstNode
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.Vid
import kotlin.math.max

fun applyKotlinMetrics(ctx: ContextNode) {
  val filesByPackage = HashMap<String, ArrayList<Vid>>()

//  ctx.find("file[lang='kotlin']>packageDirective").forEach {
//    val packageName = it.vertice.getString("name")
//    filesByPackage.putIfAbsent(packageName, ArrayList())
//    filesByPackage[packageName]?.add(
//      it.vertice.getString("fileVid")
//    )
//  }

  ctx.find("codeFile[lang='kotlin']").forEach { fileNode ->
    fileNode.vertice["lines"] = fileNode.code.relevantCodeLines()
    fileNode.vertice["textLines"] = fileNode.code.split("\n").size
    fileNode.vertice["functions"] = fileNode.find("fun").size
    fileNode.vertice["classes"] = fileNode.find("class").size
    fileNode.vertice["bindings"] = fileNode.find("binding").size

    with(fileNode) {

      find("import").forEach {
        val parts = it.vertice.getString("name").split(".")
        val pack = parts.dropLast(1).joinToString(".")
        val name = parts.last()

        val found = ctx.find("codeFile[{$>body>packageDirective[name^='$pack']} && {$>body>#$name}]").firstOrNull()
        if (found != null) {
          tree.addRelation("imports", fileNode.vid, found.vid)
        }
      }


//      find("import").forEach {
//        val parts = it.vertice.getString("name").split(".")
//        val pack = parts.dropLast(1).joinToString(".")
//        val name = parts.last()
//        val found = ctx.find("file[{packageDirective[name^='$pack']} && {$>body>#$name}]").firstOrNull()
//        if(found != null) {
//          tree.addRelation("imports", fileNode.vid, found.vid)
//        }
//      }

      find("call").forEach {
        it.vertice["args"] = it.find("$>args>arg").size
      }

      find("fun").forEach {
        it.vertice["textLines"] = it.code.split("\n").size
        it.vertice["lines"] = it.code.relevantCodeLines() - 1
        it.vertice["depth"] = depth(it.tree, it["vid"].toString()) - 1
        it.vertice[":params"] = "$>params>param"
        it.vertice["params"] = it.find("$>params>param").size
      }

      find("class").forEach {
        it.vertice["lines"] = it.code.relevantCodeLines()

        val body = it.find("$>ClassBody").firstNode()
        it.vertice["constructors"] = body.find("$>fun[astType*='Constructor']").size
        it.vertice["methods"] = body.find("$>fun").size
        it.vertice["properties"] = body.find("$>binding").size
        it.vertice["lines"] = it.code.relevantCodeLines()
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
